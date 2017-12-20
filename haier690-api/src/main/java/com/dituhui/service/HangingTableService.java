package com.dituhui.service;

import java.util.Map;

/**
 * Created by Xugn on 2017/12/19.
 */
public interface HangingTableService {

    String SEARCH = "/v1/properties/search";
    String DEL = "/v1/properties/delByCustomId";
    /**
     * 定时查询
     */
    void timerSearch();

    /**
     * 外挂表查询方法
     * @param url
     * @param paramMap
     * @return
     */
    String hangingtableSearch(String url,Map<String,Object> paramMap,String layercode);

    /**
     * 外挂表删除方法
     * @param url
     * @param paramMap
     */
    void hangingtableDel(String url,Map<String,Object> paramMap,String layercode);




}
