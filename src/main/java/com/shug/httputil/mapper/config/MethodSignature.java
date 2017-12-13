package com.shug.httputil.mapper.config;

import com.alibaba.fastjson.JSON;
import com.shug.httputil.mapper.annotation.*;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URLEncoder;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
public class MethodSignature {
    public MethodSignature(Class<?> mapperInterface, Method method) {
        HttpService service = mapperInterface.getAnnotation(HttpService.class);
        HttpSend send = method.getAnnotation(HttpSend.class);
        transformService(service);
        transformSend(send);
        transformParameter(method);
    }

    //region HttpService
    private String protocol;
    private String host;
    private String path;
    private String code;

    private void transformService(HttpService service) {
        this.protocol = service.protocol();
        this.host = service.host();
        this.code = service.code();
        this.path = service.path();
    }

    //endregion

    //region HttpSend
    private String uri;
    private HttpRequsetMethod method;
    private String encoding;
    private Map<String, String> urlQuery;

    private void transformSend(HttpSend send) {
        String tUri = send.uri();
        this.method = send.method();
        this.encoding = send.encoding();

        int index;
        if ((index  = tUri.indexOf("?")) < 0) {
            this.uri = tUri;
        } else {
            this.uri = tUri.substring(0, index);
            if (tUri.length() > 1) {
                urlQuery = new HashMap<>();
                String[] querys = tUri.substring(index + 1).split("&");
                for (String query : querys) {
                    int eIndex = query.indexOf("=");
                    if (eIndex < 0) {
                        urlQuery.put(query, "");
                    } else if (eIndex > 0) {
                        urlQuery.put(query.substring(0, eIndex), query.substring(eIndex + 1));
                    }
                }
            }
        }
    }
    //endregion

    //region HttpQuery HttpBody HttpHeader HttpCookie

    Map<String, ParameterConfig> mapPathVariable;
    Map<String, ParameterConfig> mapQuery;
    Map<String, ParameterConfig> mapBody;

//        Map<String, Object> mapHeader = new HashMap<String, Object>();
//        Map<String, Object> mapCookie = new HashMap<String, Object>();

    private void transformParameter(Method method) {
        Parameter[] parameters = method.getParameters();
        if (parameters != null && parameters.length > 0)
            for (int i = 0, size = parameters.length; i < size; i++) {
                Parameter param = parameters[i];
                transformPathVariable(param, i);
                transformQuery(param, i);
                transformBody(param, i);
//                transformHeader(param, i);
//                transformCookie(param, i);
            }
    }

    private void transformPathVariable(Parameter param, int index) {
        HttpPathVariable pathVariable = param.getAnnotation(HttpPathVariable.class);

        if (pathVariable != null) {
            if (mapPathVariable == null) {
                mapPathVariable = new HashMap<>();
            }
            String key = pathVariable.value();
            key = (key != null && key.trim().length() > 0? key : param.getName());
            mapPathVariable.put(key, new ParameterConfig(pathVariable.value(), ParameterType.STRING, index));
        }
    }
    private void transformQuery(Parameter param, int index) {
        HttpQuery query = param.getAnnotation(HttpQuery.class);
        if (query != null) {
            if (mapQuery == null) {
                mapQuery = new HashMap<>();
            }
            String key = query.value();
            key = (key != null && key.trim().length() > 0? key : param.getName());
            mapQuery.put(key, new ParameterConfig(query.value(), query.type(), index));
        }
    }
    private void transformBody(Parameter param, int index) {
        HttpBody body = param.getAnnotation(HttpBody.class);

        if (body != null) {
            if (mapBody == null) {
                mapBody = new HashMap<>();
            }
            String key = body.value();
            key = (key != null && key.trim().length() > 0? key : param.getName());
            mapBody.put(key, new ParameterConfig(body.value(), body.type(), index));
        }
    }
//        private void transformHeader(Parameter param, int index) {
//            HttpHeader header = param.getAnnotation(HttpHeader.class);
//            if (header != null) {
//                String key = header.value();
//                key = (key != null && key.trim().length() > 0? key : param.getName());
//                mapHeader.put(key, "");
//            }
//        }
//        private void transformCookie(Parameter param, int index) {
//            HttpCookie cookie = param.getAnnotation(HttpCookie.class);
//            if (cookie != null) {
//                String key = cookie.value();
//                key = (key != null && key.trim().length() > 0? key : param.getName());
//                mapCookie.put(key, "");
//            }
//        }
    //endregion

    //region request

    public String getUrl(Object[] args) {
        String tUri = this.uri;

        if (tUri.contains("{") && tUri.contains("}") && mapPathVariable != null) {
            for (Map.Entry<String, ParameterConfig> entry : mapPathVariable.entrySet()) {
                Object obj = args[entry.getValue().postion].toString();
                if (isBaseDataType(obj.getClass())) {
                    tUri = Pattern.compile("\\{\\s*" + entry.getKey() + "\\s*\\}", Pattern.CASE_INSENSITIVE).matcher(tUri).replaceAll(obj.toString());
                }
            }
        }

        TreeMap<String, Object> map = new TreeMap<>();
        if (urlQuery != null && urlQuery.size() > 0) {
            map.putAll(urlQuery);
        }
        if (mapQuery != null && mapQuery.size() > 0) {
            for (Map.Entry<String, ParameterConfig> entry : mapQuery.entrySet()) {
                ParameterConfig conf = entry.getValue();
                Object obj = args[conf.postion];
                if (isBaseDataType(obj.getClass())) {
                    map.put(entry.getKey(), obj);
                } else if (conf.type == ParameterType.STRING) {
                    if (obj instanceof Map) {
                        map.putAll((Map) obj);
                    } else {
                        map.putAll( JSON.parseObject(JSON.toJSONString(obj), Map.class));
                    }
                } else {
                    try {
                        map.put(entry.getKey(), URLEncoder.encode( JSON.toJSONString(obj), encoding));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        String queryString = "";
        if (map.size() > 0) {
            StringBuilder sb = new StringBuilder();
            Iterator it = map.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = (Map.Entry<String, Object>)it.next();
                sb.append(entry.getKey());
                if(entry.getValue() != null && entry.getValue().toString().trim().length() > 0) {
                    sb.append("=").append(entry.getValue());
                }
                sb.append("&");
            }
            queryString = "?" + sb.substring(0, sb.length() - 1);
        }

        return protocol + "://" + (host + "/" + path + "/" + tUri).replaceAll("[/]{2,}", "/") + queryString;
    }

    public HttpRequsetMethod getMethod() {
        return method;
    }

    public String getEncoding() {
        return encoding;
    }

    public String getCode() {
        return code;
    }

    public Map<String, Object> getBody(Object[] args) {
        TreeMap<String, Object> map = new TreeMap<>();
        if (mapBody != null && mapBody.size() > 0) {
            for (Map.Entry<String, ParameterConfig> entry : mapBody.entrySet()) {
                ParameterConfig conf = entry.getValue();
                Object obj = args[conf.postion];
                if (isBaseDataType(obj.getClass())) {
                    map.put(entry.getKey(), obj);
                } else if (conf.type == ParameterType.STRING) {
                    if (obj instanceof Map) {
                        map.putAll((Map) obj);
                    } else {
                        map.putAll( JSON.parseObject(JSON.toJSONString(obj), Map.class));
                    }
                } else {
                    try {
                        map.put(entry.getKey(), URLEncoder.encode( JSON.toJSONString(obj), encoding));
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return map;
    }


    /**
     * 判断一个类是否为基本数据类型。
     * @param clazz 要判断的类。
     * @return true 表示为基本数据类型。
     */
    private static boolean isBaseDataType(Class<?> clazz){
        return(
                clazz.equals(String.class) ||
                        clazz.equals(Integer.class)||
                        clazz.equals(Byte.class) ||
                        clazz.equals(Long.class) ||
                        clazz.equals(Double.class) ||
                        clazz.equals(Float.class) ||
                        clazz.equals(Character.class) ||
                        clazz.equals(Short.class) ||
                        clazz.equals(BigDecimal.class) ||
                        clazz.equals(BigInteger.class) ||
                        clazz.equals(Boolean.class) ||
                        clazz.equals(Date.class) ||
                        clazz.isPrimitive()
        );
    }

    //endregion
    public static class ParameterConfig {
        private String value;
        private ParameterType type;
        private int postion;
        public ParameterConfig(String value, ParameterType type, int postion) {
            this.value = value;
            this.type = type;
            this.postion = postion;
        }
    }
}

