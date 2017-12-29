package com.dituhui.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.dituhui.domain.PoiEntity;
import com.dituhui.mapper.PoiMapper;
import com.dituhui.service.PoiService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @Description Poi服务实现类
 * @Author LiuWei
 * @Date Create in 2017-12-28 14:46
 */
@Service(version = "1.0.0",timeout = 60000)
public class PoiServiceImpl implements PoiService{
    @Autowired
    PoiMapper poiMapper;
    @Override
    public PoiEntity searchPoiByLonlat(double lon, double lat) {
        PoiEntity poiEntityResult = null;
        int length = 50;
        do{
            List<PoiEntity> result = poiMapper.searchPoiByLonlat(lon, lat, length);
            if(result.size()>0){
                double distance = 5000;
                for(int index = 0,size = result.size();index < size;index++){
                    PoiEntity poiEntity = result.get(index);
                    double smx = Double.valueOf(poiEntity.getSmx());
                    double smy = Double.valueOf(poiEntity.getSmy());
                    if(Math.pow(lon-smx,2)+Math.pow(lat-smy,2)<distance){
                        poiEntityResult = poiEntity;
                    }
                }
            }
            length *=2;
        }while(poiEntityResult==null||length>3500);
        return poiEntityResult;
    }
}
