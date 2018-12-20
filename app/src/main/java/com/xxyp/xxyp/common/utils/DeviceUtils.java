
package com.xxyp.xxyp.common.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

/**
 * Description : 设备工具类 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public class DeviceUtils {

    private DeviceUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 展示软键盘
     * @param context  上下文
     * @param view     view
     */
    public static void showKeyBoard(Context context, View view) {
        if (view != null) {
            ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showSoftInput(view, 0);
        }
    }

    /**
     * 隐藏软键盘
     * @param context 上下文
     */
    public static void dismissKeyBoard(Context context) {
        View view = ((Activity)context).getWindow().peekDecorView();
        if (view != null) {
            ((InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 获取设备名称
     * @return String
     */
    public static String getDeviceName() {
        return Build.MODEL;
    }

    /**
     * 获取设备SDK版本号
     * @return int
     */
    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

}
