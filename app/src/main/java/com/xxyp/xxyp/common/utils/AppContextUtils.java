
package com.xxyp.xxyp.common.utils;

import android.content.Context;

/**
 * Description : 上下文工具类
 */

public final class AppContextUtils {
    private static Context mContext;

    public static Context getAppContext() {
        if (mContext == null) {
            throw new IllegalStateException("AppContextUtils 's initApp not called!!!");
        }
        return mContext;
    }

    public static void initApp(Context context) {
        mContext = context;
        ScreenUtils.init(context);
    }

    /**
     * 获取包名
     */
    public static String getPackageName(Context context) {
        try {
            return context.getPackageName();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
