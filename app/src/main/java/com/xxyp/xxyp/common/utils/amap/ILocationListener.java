package com.xxyp.xxyp.common.utils.amap;

import android.content.Context;

public interface ILocationListener {

    void startLocation(Context var1, LocationChangeListener var2, int var3);

    void cancelLocation();
}
