package com.dituhui.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.dituhui.domain.PoiEntity;
import com.dituhui.service.PoiService;
import com.supermap.convert.impl.SuperMapCoordinateConvertImpl;
import com.supermap.entity.Point;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description POI控制类
 * @Author LiuWei
 * @Date Create in 2017-12-28 8:41
 */
@Controller
@RequestMapping(value = "poi")
public class PoiController {
    @Reference(version = "1.0.0",timeout = 60000)
    PoiService poiService;
    @RequestMapping(value = "searchPoiByLonlat",method = { RequestMethod.GET, RequestMethod.POST })
    public @ResponseBody
    PoiEntity searchPoiByLonlat(String lon, String lat,String type){
        double lonDouble = Double.valueOf(lon);
        double latDouble = Double.valueOf(lat);
        if(type != null && type.equals("ll")){
            Point point = SuperMapCoordinateConvertImpl.smLL2MC(new Point(lonDouble,latDouble));
            return poiService.searchPoiByLonlat(point.getLon(),point.getLat());
        }else{
            return poiService.searchPoiByLonlat(lonDouble,latDouble);
        }

    }
}

