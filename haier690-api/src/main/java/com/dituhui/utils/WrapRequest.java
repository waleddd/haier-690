package com.dituhui.utils;

import com.dituhui.service.HangingTableService;

/**
 * Created by Xugn on 2017/12/21.
 */
public class WrapRequest {

    private static final String defaultUrl = "{urlPrefix}/{interfaceName}/{methodName}"; // 默认Url

    /**
     * 对请求进行包装
     *
     * @param interfaceName 调用的接口名
     * @param methodName 调用的方法名
     * @param urlParam url参数
     * @return 包装后的URL请求
     */
    public static String wrapRequest(String interfaceName, String methodName, String urlParam, String urlPrefix) {
        // 组装请求URL
        String urlRequest = defaultUrl;
        urlRequest = urlRequest.replace("{urlPrefix}", urlPrefix);
        urlRequest = urlRequest.replace("{interfaceName}", interfaceName);
        urlRequest = urlRequest.replace("{methodName}", methodName);
        if ((urlParam != null) && (!urlParam.equals(""))) {
            // 如果url参数不为空则拼装
            urlRequest += "?" + urlParam;
        }
        return urlRequest;
    }
}
