
package com.xxyp.xxyp.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Description : sp缓存工具类
 */

public class SharePreferenceUtils {

    private static final String SH_DATA_NAME = "xxyp_user_data";

    private static String USER_ID = "user_id";

    private static String TOKEN = "token";

    private static String DEVICE_ID = "device_id";

    private static String LOGIN_STATUS = "login_status";

    private static volatile SharePreferenceUtils instance;

    private static SharedPreferences.Editor saveEditor;

    private static SharedPreferences saveInfo;

    public static SharePreferenceUtils getInstance() {
        if (instance == null) {
            synchronized (SharePreferenceUtils.class){
                if (instance == null) {
                    instance = new SharePreferenceUtils();
                    saveInfo = AppContextUtils.getAppContext().getSharedPreferences(SH_DATA_NAME,
                            Context.MODE_PRIVATE);
                    saveEditor = saveInfo.edit();
                }
            }
        }
        return instance;
    }

    public boolean putUserId(String userId) {
        saveEditor.putString(USER_ID, userId);
        return saveEditor.commit();
    }


    public String getUserId() {
        if (saveInfo != null) {
            return saveInfo.getString(USER_ID, null);
        }
        return null;
    }

    public boolean putToken(String token) {
        saveEditor.putString(TOKEN, token);
        return saveEditor.commit();
    }

    public String getToken() {
        if (saveInfo != null) {
            return saveInfo.getString(TOKEN, null);
        }
        return null;
    }

    public boolean putDeviceId(String deviceId) {
        saveEditor.putString(DEVICE_ID, deviceId);
        return saveEditor.commit();
    }

    public String getDeviceId() {
        if (saveInfo != null) {
            return saveInfo.getString(DEVICE_ID, null);
        }
        return null;
    }


    public boolean putLoginStatus(boolean loginStatus) {
        saveEditor.putBoolean(LOGIN_STATUS, loginStatus);
        return saveEditor.commit();
    }

    public boolean getLoginStatus() {
        if (saveInfo != null) {
            return saveInfo.getBoolean(LOGIN_STATUS, false);
        }
        return false;
    }

    /**
     * 保存键盘高度
     *
     * @param currentInputMethod 当前输入法
     * @param height             软键盘高度
     */
    public void putKeyboardHeight(String currentInputMethod, int height) {
        saveEditor.putInt(currentInputMethod, height);
        saveEditor.commit();
    }

    public int getKeyboardHeight(String currentInputMethod) {
        if(saveInfo != null){
            return saveInfo.getInt(currentInputMethod, 0);
        }
        return 0;
    }
}
