package com.shug.httputil.mapper;

import com.shug.httputil.HttpDemo;
import com.shug.httputil.client.builder.HCB;
import com.shug.httputil.client.exception.HttpProcessException;
import com.shug.httputil.mapper.annotation.HttpService;
import com.shug.httputil.mapper.config.MapperMethod;
import org.apache.http.client.HttpClient;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
public class HttpMapperProxyFactory<T> {
    private final Class<T> mapperInterface;
    private final Map<Method, MapperMethod> methodCache = new ConcurrentHashMap<Method, MapperMethod>();

        public HttpMapperProxyFactory(Class<T> mapperInterface) {
            this.mapperInterface = mapperInterface;
        }

        public Class<T> getMapperInterface() {
            return mapperInterface;
        }

        public Map<Method, MapperMethod> getMethodCache() {
            return methodCache;
        }

        @SuppressWarnings("unchecked")
        protected T newInstance(HttpMapperProxy<T> mapperProxy) {
            return (T) Proxy.newProxyInstance(mapperInterface.getClassLoader(), new Class[] { mapperInterface }, mapperProxy);
        }

        public T newInstance() throws HttpProcessException{

            HttpService service = mapperInterface.getAnnotation(HttpService.class);

            HttpClient client = HCB.custom()
                    .timeout(service.timeout())
                    .pool(service.maxTotal(), service.defaultMaxPerRoute())
                    .retry(service.retry())
                    .build();

            final HttpMapperProxy<T> mapperProxy = new HttpMapperProxy<T>(client, mapperInterface, methodCache);
            return newInstance(mapperProxy);
        }

}
