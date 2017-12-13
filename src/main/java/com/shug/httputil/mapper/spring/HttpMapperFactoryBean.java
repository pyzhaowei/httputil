package com.shug.httputil.mapper.spring;

import com.shug.httputil.mapper.HttpMapperProxyFactory;
import org.springframework.beans.factory.FactoryBean;

/**
 * Created by zhaozhengwei03 on 2017/12/13.
 */
public class HttpMapperFactoryBean<T> implements FactoryBean<T> {
    private Class<T> mapperInterface;

    public HttpMapperFactoryBean() {

    }

    public HttpMapperFactoryBean(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }


    public void setMapperInterface(Class<T> mapperInterface) {
        this.mapperInterface = mapperInterface;
    }

    public void setMapperInterface(String className) throws ClassNotFoundException {
        this.mapperInterface = (Class<T>)Class.forName(className);
    }

    @Override
    public T getObject() throws Exception {
        return  (T)new HttpMapperProxyFactory(this.mapperInterface).newInstance();
    }

    @Override
    public Class<?> getObjectType() {
        return mapperInterface;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
