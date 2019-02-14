package com.xxyp.xxyp.map.location.beans;

/**
 * Description :定位的javabean
 */
public class GpsBean {

    // 纬度
    private double latitude;

    // 经度
    private double longitude;

    // 地址
    private String address;

    //省份
    private String province;

    //区
    private String district;

    // 当前城市
    private String city;

    // 地图原始经度
    private double mapLatitude;

    // 地图原始纬度
    private double mapLongitude;

    private String adCode;

    private String cityCode;
    private String poiName;
    private String aoiName;

    public String getAoiName() {
        return aoiName;
    }

    public void setAoiName(String aoiName) {
        this.aoiName = aoiName;
    }

    public String getPoiName() {
        return poiName;
    }

    public void setPoiName(String poiName) {
        this.poiName = poiName;
    }

    public double getMapLatitude() {

        return mapLatitude;
    }

    public void setMapLatitude(double mapLatitude) {

        this.mapLatitude = mapLatitude;
    }

    public double getMapLongitude() {

        return mapLongitude;
    }

    public void setMapLongitude(double mapLongitude) {

        this.mapLongitude = mapLongitude;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {

        return city;
    }

    public void setCity(String city) {

        this.city = city;
    }

    public double getLatitude() {

        return latitude;
    }

    public void setLatitude(double latitude) {

        this.latitude = latitude;
    }

    public String getAddress() {

        return address;
    }

    public void setAddress(String address) {

        this.address = address;
    }

    public double getLongitude() {

        return longitude;
    }

    public void setLongitude(double longitude) {

        this.longitude = longitude;
    }

    public String getAdCode() {

        return adCode;
    }

    public void setAdCode(String adCode) {

        this.adCode = adCode;
    }

    public String getCityCode() {

        return cityCode;
    }

    public void setCityCode(String cityCode) {

        this.cityCode = cityCode;
    }

    public GpsBean(double lat, double lon) {

        latitude = lat;
        longitude = lon;
    }

    public GpsBean() {

    }
}
