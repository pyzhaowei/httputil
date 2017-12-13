package com.shug.httputil.mapper.config;

import com.alibaba.fastjson.JSON;
import com.shug.httputil.client.HttpClientUtil;
import com.shug.httputil.client.common.HttpConfig;
import com.shug.httputil.client.common.HttpMethods;
import com.shug.httputil.client.exception.HttpProcessException;
import org.apache.http.client.HttpClient;

import java.lang.reflect.Method;
import java.util.*;


/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
public class MapperMethod {
    public MethodSignature signature;

    public MapperMethod(Class<?> mapperInterface, Method method) {
        this.signature = new MethodSignature(mapperInterface, method);
    }

    public Object execute(HttpClient client, Object[] args)
            throws HttpProcessException {
        String url = signature.getUrl(args);
        String encoding = signature.getEncoding();
        HttpRequsetMethod method = signature.getMethod();

        HttpConfig config = HttpConfig.custom()
                .url(url)                    //设置请求的url
                .encoding(encoding);

        if (method == HttpRequsetMethod.GET) {
            config.method(HttpMethods.GET);
        } else if (method == HttpRequsetMethod.POST) {
            Map<String, Object> body = signature.getBody(args);
            config.method(HttpMethods.POST)
                    .map(body);
        }
        return HttpClientUtil.send(config);
    }
}
