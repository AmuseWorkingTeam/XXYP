
package com.xxyp.xxyp.common.utils.qiniu;

import com.qiniu.android.utils.StringUtils;
import com.qiniu.android.utils.UrlSafeBase64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.spec.SecretKeySpec;

/**
 */
public class Mac {
    public String accessKey;

    public String secretKey;

    public Mac(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    public String sign(byte[] data) throws AuthException {
        javax.crypto.Mac mac = null;

        try {
            mac = javax.crypto.Mac.getInstance("HmacSHA1");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");
            mac.init(secretKeySpec);
        } catch (InvalidKeyException var5) {
            throw new AuthException("invalid key!", var5);
        } catch (NoSuchAlgorithmException var6) {
            throw new AuthException("no algorithm called HmacSHA1!", var6);
        }

        String encodedSign1 = UrlSafeBase64.encodeToString(mac.doFinal(data));
        return accessKey + ":" + encodedSign1;
    }

    public String signWithData(byte[] data) throws AuthException {
        String str = UrlSafeBase64.encodeToString(data);
        return sign(StringUtils.utf8Bytes(str)) + ":" + str;
    }
}
