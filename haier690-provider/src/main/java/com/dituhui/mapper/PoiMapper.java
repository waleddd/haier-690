package com.dituhui.mapper;

import com.dituhui.domain.PoiEntity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface PoiMapper {
//    @Select("SELECT * FROM map_poi_current where (smx BETWEEN ${lon}-${length} AND ${lon}+${length}) AND (smy BETWEEN ${lat}-${length} AND ${lat}+${length})")
//	@Results({
//            @Result(property = "smx",  column = "smx"),
//            @Result(property = "smy",  column = "smy"),
//            @Result(property = "poiId",  column = "poi_id"),
//            @Result(property = "poiName",  column = "poi_name")
//	})
    public List<PoiEntity> searchPoiByLonlat(@Param("lon")String lon, @Param("lat")String lat, @Param("length")String length);
}
