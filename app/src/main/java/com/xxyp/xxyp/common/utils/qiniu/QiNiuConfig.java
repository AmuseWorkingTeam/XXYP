
package com.xxyp.xxyp.common.utils.qiniu;

import org.json.JSONException;

/**
 * Description : 七牛上传云存储所需配置 Created by wxh on 2016/6/1. Job number：138746 Phone
 * ：15233620521 Email：wangxiaohui@syswin.com Person in charge : 王晓辉 Leader：王晓辉
 */

public class QiNiuConfig {

    public static final String QI_NIU_AK = "y1yO7Xj5suDG7zQid_v-bJlLrWUNmGUWtjPFxN44";

    public static final String QI_NIU_SK = "3Evf8prG_F6-eHYH9-Jp9QQcgzC4cyiW85tliW4B";

    public static final String QI_NIU_BUCK_NAME = "xxyuepai";

    public static final String QI_NIU_DNS_PREFIX = "http://opcse4yv1.bkt.clouddn.com/";

    /**
     * 有效期
     */
    public static final long QI_NIU_EXPIRES = 60 * 5 * 1000L;

    public static String getUploadToken(PutPolicy putPolicy) {
        Mac mac = new Mac(QI_NIU_AK,
                QI_NIU_SK);
        if (putPolicy == null) {
            putPolicy = new PutPolicy(QI_NIU_BUCK_NAME);
        }
        putPolicy.expires = QI_NIU_EXPIRES;
        try {
            return putPolicy.token(mac);
        } catch (AuthException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
