
package com.xxyp.xxyp.common.utils.qiniu;

import org.json.JSONException;

/**
 * Description : 七牛上传云存储所需配置
 *
 * */
public class QiNiuConfig {

    public static final String QI_NIU_AK = "_sHam75_uL6qvfvd8FyblLgsU-MWxHeubWeDg4Dk";

    public static final String QI_NIU_SK = "fMXH6AhK7A7tPSPxU_w0zh9epXZ8Ps1VyghA3xpJ";

    public static final String QI_NIU_BUCK_NAME = "liuyibo-bucket";

    public static final String QI_NIU_DNS_PREFIX = "http://pkbog7zq7.bkt.clouddn.com/";

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
