package com.xxyp.xxyp.map.location.interfaces;


import com.xxyp.xxyp.map.location.beans.GpsBean;

/**
 * Description : 位置变化监听
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
