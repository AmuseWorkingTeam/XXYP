package com.xxyp.xxyp.map.location.interfaces;

import android.content.Context;

/**
 * Description :
 * Created by 程磊 on 2016/6/22.
 * Job number：139268
 * Phone ：13141390126
 * Email：chenglei@syswin.com
 * Person in charge :
 * Leader：李晓
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
