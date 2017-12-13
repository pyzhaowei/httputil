package com.shug.httputil.mapper.config;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
public enum  ParameterType {
    STRING(0, "string"),
    JSON(1, "json"),

    ;

    private int code;
    private String name;

    private ParameterType(int code, String name){
        this.code = code;
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public int getCode() {
        return code;
    }
}
