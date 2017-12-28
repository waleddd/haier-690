package com.dituhui.domain;

/**
 * @Description POI对象类
 * @Author LiuWei
 * @Date Create in 2017-12-28 14:35
 */
public class PoiEntity implements java.io.Serializable{
    private String smx;
    private String smy;
    private String poiId;
    private String poiName;

    public String getSmx() {
        return smx;
    }

    public void setSmx(String smx) {
        this.smx = smx;
    }

    public String getSmy() {
        return smy;
    }

    public void setSmy(String smy) {
        this.smy = smy;
    }

    public String getPoiId() {
        return poiId;
    }

    public void setPoiId(String poiId) {
        this.poiId = poiId;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }
}
