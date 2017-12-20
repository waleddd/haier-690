package com.dituhui.client;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestOperations;

import java.util.Map;

@Component
public class RestTemplateClient {

    @Autowired
    private RestOperations restOperations;

    public JSONObject send(String url, Map<String, String> map) {
        JSONObject result = restOperations.getForObject(url, JSONObject.class, map);
        return result;
    }

}
