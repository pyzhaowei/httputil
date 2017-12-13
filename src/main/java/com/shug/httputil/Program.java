package com.shug.httputil;

import com.shug.httputil.client.ResponseContent;
import com.shug.httputil.client.builder.HCB;
import com.shug.httputil.mapper.HttpMapperProxy;
import com.shug.httputil.mapper.HttpMapperProxyFactory;
import com.shug.httputil.mapper.annotation.HttpService;
import org.apache.http.client.HttpClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
@Component
public class Program {

    @Autowired
//    @Qualifier("httpDemo1")
//    @Resource(name = "httpDemo")
    public  HttpDemo httpDemo;

    public  static void main(String[] args) throws Exception{

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring-context.xml");
        context.start();


        System.out.print(((Program)context.getBean("program")).httpDemo);


        HttpDemo demo = (HttpDemo)context.getBean("httpDemo");


        System.out.println( (HttpDemo)context.getBean("httpDemo") ==  (HttpDemo)context.getBean("httpDemo"));






            Map<String, String> dddd= new HashMap<>();
            dddd.put("name","3");
            dddd.put("hell", "33");
           long st = System.currentTimeMillis();
            ResponseContent content = demo.getAll("33", new HttpDemo.DemoQuery(), "media", "333",dddd);
            System.out.println(System.currentTimeMillis() - st);

            st = System.currentTimeMillis();
            content = demo.getAll("33", new HttpDemo.DemoQuery(), "media", "333",dddd);
            System.out.println(System.currentTimeMillis() - st);

            st = System.currentTimeMillis();
            for (int i = 0; i < 100; i++) {
                 content = demo.getAll("33", new HttpDemo.DemoQuery(), "ddd", "333",dddd);
            }
            System.out.println((System.currentTimeMillis() - st) / 100.0);
            System.out.println(content.getStatusCode());
        }
}
