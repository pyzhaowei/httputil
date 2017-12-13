package com.shug.httputil.mapper.annotation;

import javax.xml.ws.RequestWrapper;
import java.lang.annotation.*;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HttpService {

    String protocol() default "http";

    String host();

    String path() default "";

    String code() default "";

    int timeout() default 1000;

    int maxTotal() default 10;

    int defaultMaxPerRoute() default 5;

    int retry() default 0;

}
