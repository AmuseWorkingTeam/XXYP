package com.xxyp.xxyp.common.utils.amap;

import android.content.Context;

public class TransformLocationUtil {

    public static final int LOCATION_SUCCESS = 0;
    public static final int LOCATION_FAIL = 1;
    public static final int DEFAULT_LOCATION_TIME = 2000;
    private static double pi = 3.141592653589793D;
    private static double a = 6378245.0D;
    private static double ee = 0.006693421622965943D;
    private ILocationListener locationGDUtil;

    public TransformLocationUtil(Context context, LocationChangeListener listener, int time) {
        this.setLocationListener(context, listener, time);
    }

    public void setLocationListener(Context context, LocationChangeListener listener, int time) {
        if (this.locationGDUtil == null) {
            this.locationGDUtil = new LocationUtils();
        }

        this.locationGDUtil.startLocation(context, listener, time);
    }

    public void stopLocation() {
        if (null != this.locationGDUtil) {
            this.locationGDUtil.cancelLocation();
        }

    }

    public static GpsBean gcjToGps84(double lat, double lon) {
        GpsBean gps = transform(lat, lon);
        double longitude = lon * 2.0D - gps.getLongitude();
        double latitude = lat * 2.0D - gps.getLatitude();
        return new GpsBean(latitude, longitude);
    }

    public static GpsBean gps84ToGcj02(double lat, double lon) {
        double dLat = transformLat(lon - 105.0D, lat - 35.0D);
        double dLon = transformLon(lon - 105.0D, lat - 35.0D);
        double radLat = lat / 180.0D * pi;
        double magic = Math.sin(radLat);
        magic = 1.0D - ee * magic * magic;
        double sqrtMagic = Math.sqrt(magic);
        dLat = dLat * 180.0D / (a * (1.0D - ee) / (magic * sqrtMagic) * pi);
        dLon = dLon * 180.0D / (a / sqrtMagic * Math.cos(radLat) * pi);
        double mgLat = lat + dLat;
        double mgLon = lon + dLon;
        return new GpsBean(mgLat, mgLon);
    }

    public static boolean outOfChina(double lat, double lon) {
        if (lon >= 72.004D && lon <= 137.8347D) {
            return lat < 0.8293D || lat > 55.8271D;
        } else {
            return true;
        }
    }

    public static GpsBean transform(double lat, double lon) {
        if (outOfChina(lat, lon)) {
            return new GpsBean(lat, lon);
        } else {
            double dLat = transformLat(lon - 105.0D, lat - 35.0D);
            double dLon = transformLon(lon - 105.0D, lat - 35.0D);
            double radLat = lat / 180.0D * pi;
            double magic = Math.sin(radLat);
            magic = 1.0D - ee * magic * magic;
            double sqrtMagic = Math.sqrt(magic);
            dLat = dLat * 180.0D / (a * (1.0D - ee) / (magic * sqrtMagic) * pi);
            dLon = dLon * 180.0D / (a / sqrtMagic * Math.cos(radLat) * pi);
            double mgLat = lat + dLat;
            double mgLon = lon + dLon;
            return new GpsBean(mgLat, mgLon);
        }
    }

    public static double transformLat(double x, double y) {
        double ret = -100.0D + 2.0D * x + 3.0D * y + 0.2D * y * y + 0.1D * x * y + 0.2D * Math.sqrt(Math.abs(x));
        ret += (20.0D * Math.sin(6.0D * x * pi) + 20.0D * Math.sin(2.0D * x * pi)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(y * pi) + 40.0D * Math.sin(y / 3.0D * pi)) * 2.0D / 3.0D;
        ret += (160.0D * Math.sin(y / 12.0D * pi) + 320.0D * Math.sin(y * pi / 30.0D)) * 2.0D / 3.0D;
        return ret;
    }

    public static double transformLon(double x, double y) {
        double ret = 300.0D + x + 2.0D * y + 0.1D * x * x + 0.1D * x * y + 0.1D * Math.sqrt(Math.abs(x));
        ret += (20.0D * Math.sin(6.0D * x * pi) + 20.0D * Math.sin(2.0D * x * pi)) * 2.0D / 3.0D;
        ret += (20.0D * Math.sin(x * pi) + 40.0D * Math.sin(x / 3.0D * pi)) * 2.0D / 3.0D;
        ret += (150.0D * Math.sin(x / 12.0D * pi) + 300.0D * Math.sin(x / 30.0D * pi)) * 2.0D / 3.0D;
        return ret;
    }
}
