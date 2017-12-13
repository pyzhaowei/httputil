package com.shug.httputil.mapper.spring;

import com.shug.httputil.mapper.annotation.HttpService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
public class HttpMapperBeanPostProcessor implements BeanPostProcessor {

    /**
     * 服务接口Class与代理对象的关系列表
     */
    private Map<String, Object> httpServiceProxys = new HashMap<String, Object>();

    /**
     *
     * 此方法在每个Bean实例化、依赖注入完成之后执行，在调用afterPropertiesSet(实现InitializingBean接口)、init-method方法前执行 <br>
     * 〈在这里为HttpService代理接口生成代理类,这样调用代理接口的方法其实调用的是代理类的方法，在代理类方法中完成HttpClient发送xml报文的代码封装〉
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // 为HttpService代理接口生成代理类
        wireHttpServiceBean(bean, beanName);
        return bean;
    }

    /**
     *
     * 为HttpService代理接口生成代理类 <br>
     * 〈功能详细描述〉
     *
     * @param bean
     * @param beanName
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private void wireHttpServiceBean(Object bean, String beanName) {
        // 扫描当前处理的Bean中的所有set方法使用了@HttpWired注解属性
        Class<?> clazz = bean.getClass();

        BeanInfo beanInfo = null;
        try {
            beanInfo = java.beans.Introspector.getBeanInfo(clazz);
        } catch (IntrospectionException e) {
            throw new RuntimeException("Wiring bean=[" + beanName + "] meet error.", e);
        }

        // 获取当前Bean中的所有属性描述列表
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {

            // 如果没有提供set方法
            Method writeMethod = propertyDescriptor.getWriteMethod();
            if (writeMethod == null) {
                continue;
            }

            // 如果set方法上没有使用@HttpWired注解
//            if (!writeMethod.isAnnotationPresent(HttpWired.class)) {
//                continue;
//            }

            // 获取注解接口的类型
            Class<?> httpServiceClass = propertyDescriptor.getPropertyType();
            // 获取代理接口对象
            Object httpServiceProxy = getHttpServiceProxy(httpServiceClass);

            try {
                // 调用set方法为代理接口注入代理对象
                writeMethod.invoke(bean, httpServiceProxy);
            } catch (Exception e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
    }

    /**
     *
     * 获取代理接口对象 <br>
     * 〈功能详细描述〉
     *
     * @param httpServiceClass
     * @return
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    private Object getHttpServiceProxy(Class<?> httpServiceClass) {
        // 该接口是否使用了@HttpService注解
        if (!httpServiceClass.isAnnotationPresent(HttpService.class)) {
            throw new RuntimeException(httpServiceClass + "该接口上没有使用@HttpService注解");
        }

        String key = httpServiceClass.hashCode() + httpServiceClass.getName();
        Object proxyObject = httpServiceProxys.get(key);
        // 判断该代理对象是否已经存在，因为不同的Bean中都会注入该@HttpService注解对象,防止重复创建代理对象
        if (proxyObject != null) {
            return proxyObject;
        }

       // HttpMapperProxy httpServiceProxy = new HttpMapperProxy(null, httpServiceClass);

       // httpServiceProxys.put(key, httpServiceProxy);

        return proxyObject;
    }

    /**
     *
     * 此方法在每个Bean实例化、依赖注入、在调用afterPropertiesSet(实现InitializingBean接口)、init-method方法之后执行: <br>
     * 〈功能详细描述〉
     *
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     * @see [相关类/方法](可选)
     * @since [产品/模块版本](可选)
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

}