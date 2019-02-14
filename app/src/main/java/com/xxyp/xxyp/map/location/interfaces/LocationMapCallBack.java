package com.xxyp.xxyp.map.location.interfaces;


import com.xxyp.xxyp.map.location.beans.PluginMapLocationBean;
import com.xxyp.xxyp.map.location.beans.UserCommonPosition;

/**
 * Description : 地图选择页面监听回调
 */
public interface LocationMapCallBack {

    /*位置变化*/
    void onLocationChanged();

    /*屏幕截图*/
    void onScreenShot(PluginMapLocationBean bean);

    /*返回经纬度*/
    void onBackLatLon(PluginMapLocationBean bean);

    /*公共地址*/
    void onBackCommonLocation(UserCommonPosition bean);

}
