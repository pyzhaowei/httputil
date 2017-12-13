package com.shug.httputil;

import com.shug.httputil.client.builder.HCB;
import com.shug.httputil.mapper.HttpMapperProxy;
import com.shug.httputil.mapper.annotation.HttpService;
import com.shug.httputil.mapper.spring.HttpMapperFactoryBean;
import org.apache.http.client.HttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
@Configuration
public class SpringBean {

    @Bean("http2")
    public HttpDemo httpDemo() throws Exception {
        HttpDemo demo =  new HttpMapperFactoryBean<HttpDemo>(HttpDemo.class).getObject();
        System.out.println(demo);
        return demo;
    }
}
