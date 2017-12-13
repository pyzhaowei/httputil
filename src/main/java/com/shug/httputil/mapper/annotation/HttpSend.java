package com.shug.httputil.mapper.annotation;

import com.shug.httputil.mapper.config.HttpRequsetMethod;

import java.lang.annotation.*;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpSend {
    String uri();
    HttpRequsetMethod method() default HttpRequsetMethod.GET;
    String encoding() default "utf-8";
}
