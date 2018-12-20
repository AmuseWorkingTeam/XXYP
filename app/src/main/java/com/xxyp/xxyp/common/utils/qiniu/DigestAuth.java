
package com.xxyp.xxyp.common.utils.qiniu;


/**
 * Created by sunpengfei on 2017/7/27. Person in charge : sunpengfei
 */
public class DigestAuth {

    public DigestAuth() {
    }

    public static String sign(Mac mac, byte[] data) throws AuthException {
        if (mac == null) {
            throw new IllegalArgumentException("mac is null");
        }

        return mac.sign(data);
    }

    public static String signWithData(Mac mac, byte[] data) throws AuthException {
        if (mac == null) {
            throw new IllegalArgumentException("mac is null");
        }

        return mac.signWithData(data);
    }
}
