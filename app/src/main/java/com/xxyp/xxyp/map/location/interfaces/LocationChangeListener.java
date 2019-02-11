package com.xxyp.xxyp.map.location.interfaces;


import com.xxyp.xxyp.map.location.beans.GpsBean;

/**
 * Description : 位置变化监听
 * Created by Mc on 2016/1/23.
 * Job number：139268
 * Phone ：13141390126
 * Email：chenglei@syswin.com
 * Person in charge :程磊
 * Leader：李晓
 */
public interface LocationChangeListener {
    /**
     * 位置变化监听
     *
     * @param bean      返回数据
     * @param errorCode 返回错误码
     */
    void mapLocation(GpsBean bean, int errorCode);
}
