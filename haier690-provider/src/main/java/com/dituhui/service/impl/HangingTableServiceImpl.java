package com.dituhui.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.dituhui.config.LayerCodeList;
import com.dituhui.service.HangingTableService;
import com.dituhui.utils.HttpClientUtils;
import com.dituhui.utils.HttpUtils;
import com.dituhui.utils.SaasSignUtils;
import com.dituhui.utils.WrapRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
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

    //private static String urlPrefix = "http://114.55.43.0:16907/v1";
    private static String urlPrefix = "http://saasapi.dituhui.com/v1";
    private static final String defaultUrl = "{urlPrefix}/{interfaceName}/{methodName}"; // 默认Url
    // private static final String ak = "0142dd73d78b475cb3fb3ba952ebfe78";
    //private static final String secretKey = "8aafdd8c5d8eaf58015d97c032780027";
    private static final String secretKey = "df666850016d4fd69e02527ac8ff3709";
    private static final String ak = "af99ac257f4449ce86f2a3f402abcc53";
    private static  String haierUpdatetRegionblockbasicUrl = "http://hsi.haier.net:8899/internalapi/hsi/hSIToSuperMapController/updatetRegionblockbasic";

    Timer timer = new Timer();



    @Override
    public void timerSearch() {
        //添加
//        String layercode = "03c_001";
        String layercode = "041_001";
//        String  paramMap = "[{\"fieldName\":\"省级名称\",\"fieldValue\":\"广西省\"},{\"fieldName\":\"市级名称\",\"fieldValue\":\"南宁市\"},{\"fieldName\":\"区块ID\",\"fieldValue\":\"20110329NN0377\"},{\"fieldName\":\"名称\",\"fieldValue\":\"蒙山县黄村镇\"},{\"fieldName\":\"工贸名称\",\"fieldValue\":\"南宁工贸\"},{\"fieldName\":\"工贸编号\",\"fieldValue\":\"GFSH\"},{\"fieldName\":\"区块编码\",\"fieldValue\":\"NN03262\"}]";
//        hangingtableAdd(urlPrefix,paramMap,layercode);
        //删除
//        Map<String,Object> maps = new HashMap<String,Object>();
//        maps.put("fieldValue",0);
//        maps.put("fieldName","属性状态");
//        hangingtableDel(urlPrefix,maps,layercode);
        //查询
//        Map<String,Object> map = new HashMap<String,Object>();
//        map.put("fieldValue",1);
//        map.put("fieldName","属性状态");
//        hangingtableSearch(urlPrefix,map,layercode);
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
            hangingtableSearch(urlPrefix);
            }
        }, 1000 , 5000);
    }

    /**
     * 查询
     * @param url
     * @return
     */
    @Override
    public String hangingtableSearch(String url) {
        String paramMap = "[{\"fieldName\":\"属性状态\",\"fieldValue\":\"0\"}，{\"fieldName\":\"属性状态\",\"fieldValue\":\"1\"}，{\"fieldName\":\"属性状态\",\"fieldValue\":\"2\"}，{\"fieldName\":\"属性状态\",\"fieldValue\":\"3\"}]";
        String jsonparam = JSONObject.toJSONString(paramMap);
        try{
            jsonparam = java.net.URLEncoder.encode(jsonparam, "UTF-8");
            String params = "customs="+jsonparam+"&ak="+ak+"&data_type=Area&custom_composite_type=Or";
            String requestUrl = WrapRequest.wrapRequest("properties", "search", params, urlPrefix);
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
                            //for循环出所有infos的 数据
                            String fielval = "";
                            String layercode = "";
                            for (int j = 0; j < jsonarr.size(); j++) {
                                JSONObject jon = jsonarr.getJSONObject(j);
                                if ("区块ID".equals(jon.getString("fieldName"))) {
                                    //获取区块id
                                    fielval = "074C9B2C-14E6-E711-80D8-005056A354DD";
//                                    fielval = jon.getString("fieldValue");
                                }
                                if ("layercode".equals(jon.getString("fieldName"))) {
                                    //获取区块id
                                    layercode = "041_001";
//                                    layercode = jon.getString("layercode");
                                }
                                if ("属性状态".equals(jon.getString("fieldName"))) {
                                    String fiel = jon.getString("fieldValue");
                                    String id = jo.getString("id");
                                    if ("1".equals(fiel)) {
                                        String status = "100000001";    //确认中
                                        //如果状态为1，则返回
                                        String value = "{\n" +
                                                "    \"hsicrmRegionblockid\": \""+fielval+"\",\n" +
                                                "    \"status\": \""+status+"\",\n" +
                                                "    \"hsicrmBoundary\": \"(120.2,190.8),(120.4,181.3)\"      \n" +
                                                "}";
                                        String code = HttpUtils.pushAttendanceInfo(value,haierUpdatetRegionblockbasicUrl);
                                        JSONObject jsoncode = JSONObject.parseObject(code);
                                        if("success".equals(jsoncode.getString("flag"))){
                                            System.err.println("状态一返回数据成功");
                                        }
                                    } else if ("2".equals(fiel)) {
                                        String status = "100000000";    //审核通过
                                        //如果状态为2，则返回并删除
                                        String value = "{\n" +
                                                "    \"hsicrmRegionblockid\": \""+fielval+"\",\n" +
                                                "    \"status\": \""+status+"\",\n" +
                                                "    \"hsicrmBoundary\": \"(120.2,190.8),(120.4,181.3)\"      \n" +
                                                "}";
                                        String code = HttpUtils.pushAttendanceInfo(value,haierUpdatetRegionblockbasicUrl);
                                        JSONObject jsoncode = JSONObject.parseObject(code);
                                        if("success".equals(jsoncode.getString("flag"))){
                                            System.err.println("状态二返回数据成功");
                                            Map<String, Object> mapdel = new HashMap<String, Object>();
                                            String fieldName = String.valueOf(jon.getString("fieldName"));
                                            String fieldValue = String.valueOf(jon.getString("fieldValue"));
                                            mapdel.put("fieldName", fieldName);
                                            mapdel.put("fieldValue", fieldValue);
                                            hangingtableDel(urlPrefix, mapdel, layercode);
                                            return id;
                                        }else{
                                            return jsoncode.getString("flag");
                                        }
                                    }else if("3".equals(fiel)){
                                        String status = "100000003";    //审核驳回
                                        //如果状态为3，则返回并修改
                                        String value = "{\n" +
                                                "    \"hsicrmRegionblockid\": \""+fielval+"\",\n" +
                                                "    \"status\": \""+status+"\",\n" +
                                                "    \"hsicrmBoundary\": \"(120.2,190.8),(120.4,181.3)\"      \n" +
                                                "}";
                                        String code = HttpUtils.pushAttendanceInfo(value,haierUpdatetRegionblockbasicUrl);
                                        JSONObject jsoncode = JSONObject.parseObject(code);
                                        if("success".equals(jsoncode.getString("flag"))){
                                            System.err.println("状态三返回数据成功");
                                            Map<String, Object> mapdel = new HashMap<String, Object>();
                                            String fieldName = String.valueOf(jon.getString("fieldName"));
                                            String fieldValue = String.valueOf(jon.getString("fieldValue"));
                                            mapdel.put("fieldName", fieldName);
                                            mapdel.put("fieldValue", fieldValue);
                                            String infos = "[{\\\"fieldName\\\":\\\"属性状态\\\",\\\"fieldValue\\\":\\\"3\\\"}]";
//                                            hangingtableUpdate(urlPrefix, mapdel, layercode,infos);
                                            return id;
                                        }else{
                                            return jsoncode.getString("flag");
                                        }
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
            String date = String.valueOf(System.currentTimeMillis());
            Map<String,String> signMap = new HashMap<String, String>();
            signMap.put("ak", ak);
            signMap.put("custom_id",jsonparam);
            signMap.put("layercode", layercode);
            signMap.put("t", date);
            String sign = SaasSignUtils.signRequest(signMap,secretKey);
            jsonparam = java.net.URLEncoder.encode(jsonparam, "UTF-8");
            String params = "custom_id="+jsonparam+"&ak="+ak+"&layercode="+layercode+"&t="+date+"&sign="+sign;
            String requestUrl = WrapRequest.wrapRequest("properties", "delByCustomId", params, urlPrefix);
            HttpClient client = HttpClientUtils.acceptsUntrustedCertsHttpClient();
            //发送post请求
            HttpPost request = new HttpPost(requestUrl);
            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                JSONObject jsoninfos = JSONObject.parseObject(strResult);
                JSONArray jsonArray =jsoninfos.getJSONArray("result");
                System.err.println("状态二删除成功。图层："+layercode+"."+jsonparam);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * 添加外 挂表
     * @param url
     * @param paramMap
     * @param layercode
     */
    @Override
    public void hangingtableAdd(String url,String paramMap,String layercode) {
//        String jsonparam = JSONObject.toJSONString(paramMap);
        try{
            String date = String.valueOf(System.currentTimeMillis());
            Map<String,String> signMap = new HashMap<String, String>();
            signMap.put("ak", ak);
            signMap.put("infos",paramMap);
            signMap.put("layercode", layercode);
            signMap.put("t", date);
            String sign = SaasSignUtils.signRequest(signMap,secretKey);
            paramMap = java.net.URLEncoder.encode(paramMap, "UTF-8");
            String params = "infos="+paramMap+"&ak="+ak+"&layercode="+layercode+"&t="+date+"&sign="+sign;
            String requestUrl = WrapRequest.wrapRequest("properties", "add", params, urlPrefix);
            HttpClient client = HttpClientUtils.acceptsUntrustedCertsHttpClient();
            //发送post请求
            HttpPost request = new HttpPost(requestUrl);
            HttpResponse response = client.execute(request);

            /**请求发送成功，并得到响应**/
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                /**读取服务器返回过来的json字符串数据**/
                String strResult = EntityUtils.toString(response.getEntity());
                JSONObject jsoninfos = JSONObject.parseObject(strResult);
                if("S001".equals(jsoninfos.getString("code"))){
                    JSONArray jsonArray =jsoninfos.getJSONArray("result");
                    System.out.println("添加成功"+layercode);
                }else{
                    System.out.println("失败"+layercode);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
