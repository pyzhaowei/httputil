package com.shug.httputil.mapper.config;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
/**
 * 枚举HttpMethods方法
 *
 * @author arron
 * @date 2015年11月17日 下午4:45:59
 * @version 1.0
 */
public enum HttpRequsetMethod {

    /**
     * 求获取Request-URI所标识的资源
     */
    GET(0, "GET"),

    /**
     * 向指定资源提交数据进行处理请求（例如提交表单或者上传文件）。数据被包含在请求体中。
     * POST请求可能会导致新的资源的建立和/或已有资源的修改
     */
    POST(1, "POST"),


    ;

    private int code;
    private String name;

    private HttpRequsetMethod(int code, String name){
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