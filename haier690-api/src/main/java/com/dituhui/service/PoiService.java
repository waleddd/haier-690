package com.dituhui.service;

import com.dituhui.domain.PoiEntity;

public interface PoiService {
    public PoiEntity searchPoiByLonlat(double lon, double lat);
}
