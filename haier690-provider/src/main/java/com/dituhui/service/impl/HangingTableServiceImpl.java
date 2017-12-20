package com.dituhui.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dituhui.config.LayerCodeList;
import com.dituhui.service.HangingTableService;
import com.dituhui.utils.HttpClientUtils;
import com.dituhui.utils.SaasSignUtils;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Xugn on 2017/12/19.
 */
@Service(version = "1.0.0")
public class HangingTableServiceImpl implements HangingTableService {

    private static String urlPrefix = "http://114.55.43.0:16907/v1/";
    //private static String urlPrefix = "http://saasapi.dituhui.com/v1/";
    private static final String defaultUrl = "{urlPrefix}/{interfaceName}/{methodName}"; // 默认Url
    private static final String ak = "0142dd73d78b475cb3fb3ba952ebfe78";
    private static final String secretKey = "8aafdd8c5d8eaf58015d97c032780027";
   //    private static final String ak = "af99ac257f4449ce86f2a3f402abcc53";

    Timer timer = new Timer();

    /**
     * 对请求进行包装
     *
     * @param interfaceName 调用的接口名
     * @param methodName 调用的方法名
     * @param urlParam url参数
     * @return 包装后的URL请求
     */
    private static String wrapRequest(String interfaceName, String methodName, String urlParam) {
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

    @Override
    public void timerSearch() {
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                for(Object s : LayerCodeList.list){
                    String layercode = String.valueOf(s);
                    Map<String,Object> map = new HashMap<String,Object>();
                    map.put("fieldValue",1);
                    map.put("fieldName","属性状态");
                    hangingtableSearch(urlPrefix,map,layercode);
                    Map<String,Object> maps = new HashMap<String,Object>();
                    maps.put("fieldValue",2);
                    maps.put("fieldName","属性状态");
                    hangingtableSearch(urlPrefix,maps,layercode);
                }
            }
        }, 1000 , 5000);
    }


    @Override
    public String hangingtableSearch(String url,Map<String,Object> paramMap,String layercode) {
        String jsonparam = JSONObject.toJSONString(paramMap);
        try{
            jsonparam = java.net.URLEncoder.encode(jsonparam, "UTF-8");
            String params = "custom_id="+jsonparam+"&ak="+ak+"&layercode="+layercode;
            String requestUrl = wrapRequest("properties", "search", params);
            HttpClient client = HttpClientUtils.acceptsUntrustedCertsHttpClient();
            //发送get请求
            HttpGet request = new HttpGet(requestUrl);
            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                JSONObject jsoninfos = JSONObject.parseObject(strResult);
                if("S001".equals(jsoninfos.getString("code"))) {
                    JSONArray jsonArray =jsoninfos.getJSONArray("result");
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jo = jsonArray.getJSONObject(i);
                            JSONArray jsonarr = jo.getJSONArray("infos");
                            for (int j = 0; j < jsonarr.size(); j++) {
                                JSONObject jon = jsonarr.getJSONObject(j);
                                if ("属性状态".equals(jon.getString("fieldName"))) {
                                    System.out.println(jon.getString("fieldValue"));
                                    System.out.println(jo.getString("id"));
                                    String fiel = jon.getString("fieldValue");
                                    String id = jo.getString("id");
                                    if ("1".equals(fiel)) {
                                        return id;
                                    } else if ("2".equals(fiel)) {
                                        Map<String, Object> mapdel = new HashMap<String, Object>();
                                        String fieldName = String.valueOf(jon.getString("fieldName"));
                                        String fieldValue = String.valueOf(jon.getString("fieldValue"));
                                        mapdel.put("fieldName", fieldName);
                                        mapdel.put("fieldValue", fieldValue);
                                        hangingtableDel(urlPrefix, mapdel, layercode);
                                        return id;
                                    }
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 删除外 挂表
     * @param url
     * @param paramMap
     * @param layercode
     */
    @Override
    public void hangingtableDel(String url,Map<String,Object> paramMap,String layercode) {
        String jsonparam = JSONObject.toJSONString(paramMap);
        try{
            jsonparam = java.net.URLEncoder.encode(jsonparam, "UTF-8");
            String date = String.valueOf(System.currentTimeMillis());
            Map<String,String> signMap = new HashMap<String, String>();
            signMap.put("ak", ak);
            signMap.put("custom_id",jsonparam);
            signMap.put("layercode", layercode);
            signMap.put("t", date);
            String sign = SaasSignUtils.signRequest(signMap,secretKey);
            String params = "custom_id="+jsonparam+"&ak="+ak+"&layercode="+layercode+"&t="+date+"&sign="+sign;
            String requestUrl = wrapRequest("properties", "delByCustomId", params);
//            HttpClient client = HttpClientUtils.acceptsUntrustedCertsHttpClient();
//            //发送post请求
//            HttpPost request = new HttpPost(requestUrl);
//            HttpResponse response = client.execute(request);
//
//            /**请求发送成功，并得到响应**/
//            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
//                /**读取服务器返回过来的json字符串数据**/
//                String strResult = EntityUtils.toString(response.getEntity());
//                JSONObject jsoninfos = JSONObject.parseObject(strResult);
//                JSONArray jsonArray =jsoninfos.getJSONArray("result");
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
