package com.shug.httputil.mapper.annotation;

import com.shug.httputil.mapper.config.ParameterType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpBody {
    String value() default "";
    ParameterType type() default ParameterType.STRING;
}
