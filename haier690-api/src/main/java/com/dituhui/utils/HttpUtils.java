package com.dituhui.utils;

import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

/**
 * Created by Xugn on 2017/12/21.
 */
public class HttpUtils {

    /**
     * get方式请求
     * @param url
     * @return
     */
    public static JSONObject getHttpUrl(String url) {
        try {
            HttpClient client = HttpClientUtils.acceptsUntrustedCertsHttpClient();
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                JSONObject jsoninfos = JSONObject.parseObject(strResult);
                return jsoninfos;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * post方式请求
     * @param url
     * @return
     */
    public static JSONObject postHttpUrl(String url) {
        try {
            HttpClient client = HttpClientUtils.acceptsUntrustedCertsHttpClient();
            HttpPost request = new HttpPost(url);
            HttpResponse response = client.execute(request);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                JSONObject jsoninfos = JSONObject.parseObject(strResult);
                return jsoninfos;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
