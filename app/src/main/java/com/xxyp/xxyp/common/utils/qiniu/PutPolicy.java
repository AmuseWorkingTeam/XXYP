
package com.xxyp.xxyp.common.utils.qiniu;

import com.qiniu.android.utils.StringUtils;

import org.json.JSONException;
import org.json.JSONStringer;


/**
 * Description : 上传策略配置
 */

public class PutPolicy {
    public String scope;

    public String callbackUrl;

    public String callbackBody;

    public String returnUrl;

    public String returnBody;

    public String asyncOps;

    public String endUser;

    public long expires;

    public String saveKey;

    public int insertOnly;

    public int detectMime;

    public long fsizeLimit;

    public String mimeLimit;

    public String persistentNotifyUrl;

    public String persistentOps;

    private long deadline;

    public String persistentPipeline;

    public PutPolicy(String scope) {
        this.scope = scope;
    }

    public String marshal() throws JSONException {
        JSONStringer stringer = new JSONStringer();
        stringer.object();
        stringer.key("scope").value(this.scope);
        if (this.callbackUrl != null && this.callbackUrl.length() > 0) {
            stringer.key("callbackUrl").value(this.callbackUrl);
        }

        if (this.callbackBody != null && this.callbackBody.length() > 0) {
            stringer.key("callbackBody").value(this.callbackBody);
        }

        if (this.returnUrl != null && this.returnUrl.length() > 0) {
            stringer.key("returnUrl").value(this.returnUrl);
        }

        if (this.returnBody != null && this.returnBody.length() > 0) {
            stringer.key("returnBody").value(this.returnBody);
        }

        if (this.asyncOps != null && this.asyncOps.length() > 0) {
            stringer.key("asyncOps").value(this.asyncOps);
        }

        if (this.saveKey != null && this.saveKey.length() > 0) {
            stringer.key("saveKey").value(this.saveKey);
        }

        if (this.insertOnly > 0) {
            stringer.key("insertOnly").value((long)this.insertOnly);
        }

        if (this.detectMime > 0) {
            stringer.key("detectMime").value((long)this.detectMime);
        }

        if (this.fsizeLimit > 0L) {
            stringer.key("fsizeLimit").value(this.fsizeLimit);
        }

        if (this.mimeLimit != null && this.mimeLimit.length() > 0) {
            stringer.key("mimeLimit").value(this.mimeLimit);
        }

        if (this.endUser != null && this.endUser.length() > 0) {
            stringer.key("endUser").value(this.endUser);
        }

        if (this.persistentNotifyUrl != null && this.persistentNotifyUrl.length() > 0) {
            stringer.key("persistentNotifyUrl").value(this.persistentNotifyUrl);
        }

        if (this.persistentOps != null && this.persistentOps.length() > 0) {
            stringer.key("persistentOps").value(this.persistentOps);
        }

        if (this.persistentPipeline != null && this.persistentPipeline.trim().length() > 0) {
            stringer.key("persistentPipeline").value(this.persistentPipeline);
        }

        stringer.key("deadline").value(this.deadline);
        stringer.endObject();
        return stringer.toString();
    }

    public String token(Mac mac) throws AuthException, JSONException {
        if (expires == 0) {
            expires = QiNiuConfig.QI_NIU_EXPIRES;
        }

        deadline = System.currentTimeMillis() / 1000 + expires;
        byte[] data = StringUtils.utf8Bytes(marshal());
        return DigestAuth.signWithData(mac, data);
    }
}
