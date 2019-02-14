package com.xxyp.xxyp.map.location;

import android.content.Context;

import com.xxyp.xxyp.map.location.beans.GpsBean;
import com.xxyp.xxyp.map.location.interfaces.ILocationListener;
import com.xxyp.xxyp.map.location.interfaces.LocationChangeListener;

public class LocationUtil {

    public static final int LOCATION_SUCCESS = 0;// 定位成功

    public static final int LOCATION_FAIL = 1;// 定位失败

    public static final int DEFAULT_LOCATION_TIME = 2000;// 定位时间间隔

    private static double pi = 3.14159265358979324D;// 圆周率

    private static double a = 6378245.0D;// WGS 长轴半径

    private static double ee = 0.00669342162296594323D;// WGS 偏心率的平方

    private ILocationListener locationGDUtil;

    /**
     * 定位 自定义定位时长
     *
     * @param context  上下文
     * @param listener 定位监听
     * @param time     定位间隔,单位毫秒,一般2000
     */
    public LocationUtil(Context context, final LocationChangeListener listener, int time) {

        setLocationListener(context, listener, time);
    }

    /**
     * 设置定位
     *
     * @param context  上下文
     * @param listener 定位监听
     * @param time     定位时间
     */
    public void setLocationListener(Context context, final LocationChangeListener listener, int time) {

        if (locationGDUtil == null) {
            locationGDUtil = new LocationGDUtil();
        }
        locationGDUtil.startLocation(context, listener, time);
    }

    /**
     * 停止定位
     */
    public void stopLocation() {
        if (null != locationGDUtil) {
            locationGDUtil.cancelLocation();
        }
    }


    /**
     * * 火星坐标系 (GCJ-02) to 84
     */
    public static GpsBean gcjToGps84(double lat, double lon) {

        GpsBean gps = transform(lat, lon);
        double longitude = lon * 2 - gps.getLongitude();
        double latitude = lat * 2 - gps.getLatitude();
        return new GpsBean(latitude, longitude);
    }

    /**
     * 84 to 火星坐标系 (GCJ-02)
     */
    public static GpsBean gps84ToGcj02(double lat, double lon) {
        // if (outOfChina(lat, lon)) {
        // return new GpsBean(lat, lon);
        // }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new GpsBean(mgLat, mgLon);
    }

    /**
     * 在中国的坐标范围之内
     */
    public static boolean outOfChina(double lat, double lon) {

        if (lon < 72.004 || lon > 137.8347)
            return true;
        return lat < 0.8293 || lat > 55.8271;
    }

    public static GpsBean transform(double lat, double lon) {

        if (outOfChina(lat, lon)) {
            return new GpsBean(lat, lon);
        }
        double dLat = transformLat(lon - 105.0, lat - 35.0);
        double dLon = transformLon(lon - 105.0, lat - 35.0);
        double radLat = lat / 180.0 * pi;
        double magic = Math.sin(radLat);
        magic = 1 - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = (dLat * 180.0) / ((a * (1 - ee)) / (magic * sqrtMagic) * pi);
        dLon = (dLon * 180.0) / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new GpsBean(mgLat, mgLon);
    }

    public static double transformLat(double x, double y) {

        double ret = -100.0 + 2.0 * x + 3.0 * y + 0.2 * y * y + 0.1 * x * y
                + 0.2 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(y * pi) + 40.0 * Math.sin(y / 3.0 * pi)) * 2.0 / 3.0;
        ret += (160.0 * Math.sin(y / 12.0 * pi) + 320 * Math.sin(y * pi / 30.0)) * 2.0 / 3.0;
        return ret;
    }

    public static double transformLon(double x, double y) {

        double ret = 300.0 + x + 2.0 * y + 0.1 * x * x + 0.1 * x * y + 0.1 * Math.sqrt(Math.abs(x));
        ret += (20.0 * Math.sin(6.0 * x * pi) + 20.0 * Math.sin(2.0 * x * pi)) * 2.0 / 3.0;
        ret += (20.0 * Math.sin(x * pi) + 40.0 * Math.sin(x / 3.0 * pi)) * 2.0 / 3.0;
        ret += (150.0 * Math.sin(x / 12.0 * pi) + 300.0 * Math.sin(x / 30.0 * pi)) * 2.0 / 3.0;
        return ret;
    }
}
