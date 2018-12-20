
package com.xxyp.xxyp.common.net;

import android.text.TextUtils;

import com.xxyp.xxyp.common.utils.DeviceUtils;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Description : 网络工具类 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public class NetUtils {

    /**
     * 构造网络请求头
     * @return Map
     */
    public static Map<String, String> buildHeader() {
        Map<String, String> headers = new HashMap();
        String userId = SharePreferenceUtils.getInstance().getUserId();
        String token = SharePreferenceUtils.getInstance().getToken();
        headers.put("X-XXYP-User-ID", !TextUtils.isEmpty(userId) ? userId : "");
        headers.put("X-XXYP-User-Token", !TextUtils.isEmpty(token) ? token : "");

        StringBuilder agent = new StringBuilder("platform:");
        agent.append("android,").append("deviceId:").append("").append(",")
                .append("appVersion:").append("").append(",").append("platformVersion:")
                .append(DeviceUtils.getSDKVersion());
        headers.put("X-XXYP-User-Agent", agent.toString());
        return headers;
    }
}
