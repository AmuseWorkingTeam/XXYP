package com.xxyp.xxyp.map.location.interfaces;


import com.xxyp.xxyp.map.location.beans.PluginMapLocationBean;
import com.xxyp.xxyp.map.location.beans.TNPUserCommonPosition;

/**
 * Description : 地图选择页面监听回调
 * Created by 郑晓飞 on 2016/4/11.
 * Job number：139268
 * Phone ：13141390126
 * Email：chenglei@syswin.com
 * Person in charge : 程磊
 * Leader：李晓
 */
public interface LocationMapCallBack {

    /*位置变化*/
    void onLocationChanged();

    /*屏幕截图*/
    void onScreenShot(PluginMapLocationBean bean);

    /*返回经纬度*/
    void onBackLatLon(PluginMapLocationBean bean);

    /*公共地址*/
    void onBackCommonLocation(TNPUserCommonPosition bean);

}
