package com.xxyp.xxyp.map.location.interfaces;

import android.content.Context;

/**
 */
public interface ILocationListener {
    /**
     * 开始定位
     * @param context
     * @param listener 定位回调监听
     * @param time 定位时间间隔
     */
    void startLocation(Context context, LocationChangeListener listener, int time);

    /**
     * 停止定位
     */
    void cancelLocation();
}
