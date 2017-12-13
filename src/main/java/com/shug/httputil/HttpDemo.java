package com.shug.httputil;

import com.shug.httputil.client.ResponseContent;
import com.shug.httputil.mapper.annotation.*;
import com.shug.httputil.mapper.config.HttpRequsetMethod;
import com.shug.httputil.mapper.config.ParameterType;

import java.util.Map;

/**
 * Created by zhaozhengwei03 on 2017/12/12.
 */
@HttpService(protocol = "https", host = "api.weixin.qq.com", path="/cgi-bin", code = "taoche", timeout = 1000)
public interface HttpDemo {
/*
* :////?access_token=ACCESS_TOKEN

* */
    @HttpSend(uri = "{abcdef}/uploadimg/?da=3&dddd", method = HttpRequsetMethod.GET, encoding = "utf-8")
    ResponseContent getAll(@HttpQuery("appid") String appid,
                           @HttpQuery(value = "ddd", type = ParameterType.JSON) DemoQuery demo,
                           @HttpPathVariable("abcdef") String rest,
                           @HttpBody("name") String name,
                           @HttpBody Map<String,String> map);

    public static class DemoQuery {
        int id = 0;
        String aaaa= "ssss";

        public int getId() {
            return id;
        }

        public String getAaaa() {
            return aaaa;
        }
    }


}

