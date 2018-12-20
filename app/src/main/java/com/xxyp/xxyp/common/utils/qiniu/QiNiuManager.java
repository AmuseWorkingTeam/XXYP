
package com.xxyp.xxyp.common.utils.qiniu;

import android.os.Build;
import android.text.TextUtils;

import com.qiniu.android.common.FixedZone;
import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.Configuration;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UpProgressHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.storage.UploadOptions;

import org.json.JSONObject;

import java.util.Random;

/**
 * Description : 七牛上传管理类 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public class QiNiuManager {

    private static QiNiuManager mInstance;

    private UploadManager mUploadManager;

    public static QiNiuManager getInstance() {
        if (mInstance == null) {
            synchronized (QiNiuManager.class) {
                if (mInstance == null) {
                    mInstance = new QiNiuManager();
                }
            }
        }
        return mInstance;
    }

    private QiNiuManager() {
        // 用华北一区的
        Configuration config = new Configuration.Builder().zone(FixedZone.zone1).build();
        mUploadManager = new UploadManager(config);
    }

    /**
     * 返回图片的实际地址
     *
     * @param url 远程路径
     * @return String
     */
    private String getRealUrl(String url) {
        return QiNiuConfig.QI_NIU_DNS_PREFIX + url;
    }

    /**
     * 上传
     *
     * @param filePath 文件路径
     * @param key 文件名
     * @param token token
     * @param listener 监听
     */
    private void upload(String filePath, String key, String token,
            final QiNiuUploadCallback listener) {
        mUploadManager.put(filePath, key, token, new UpCompletionHandler() {
            @Override
            public void complete(String key, ResponseInfo info, JSONObject response) {
                if (info != null && info.isOK()) {
                    String realUrl = getRealUrl(key);
                    if (listener != null) {
                        listener.onSuccess(realUrl);
                    }
                } else {
                    if (listener != null) {
                        listener.onError(info != null ? (info.statusCode) : -1,
                                info != null ? info.error : "");
                    }
                }
            }
        }, new UploadOptions(null, null, false, new UpProgressHandler() {
            public void progress(String key, double percent) {
                if (listener != null) {
                    listener.onProgress((int)(percent * 100));
                }
            }
        }, null));
    }

    /**
     * 上传图片
     * 
     * @param imagePath 图片地址
     * @param listener 上传监听
     */
    public void uploadImage(String imagePath, QiNiuUploadCallback listener) {
        if (!TextUtils.isEmpty(imagePath)) {
            final String fileUrlUUID = getFileUrlUUID();
            String token = QiNiuConfig.getUploadToken(null);
            if (token == null) {
                if (listener != null) {
                    listener.onError(-1, "token is null");
                }
                return;
            }
            upload(imagePath, fileUrlUUID, token, listener);
        }
    }

    /**
     * 上传图片
     *
     * @param voicePath 语音地址
     * @param listener 上传监听
     */
    public void uploadVoice(String voicePath, QiNiuUploadCallback listener) {
        if (!TextUtils.isEmpty(voicePath)) {
            final String fileUrlUUID = getFileUrlUUID();
            String token = QiNiuConfig.getUploadToken(null);
            if (token == null) {
                if (listener != null) {
                    listener.onError(-1, "token is null");
                }
                return;
            }
            upload(voicePath, fileUrlUUID, token, listener);
        }
    }

    private String getFileUrlUUID() {
        return (Build.MODEL + "_" + System.currentTimeMillis() + "_" + new Random().nextInt(500000)
                + "_" + new Random().nextInt(10000)).replace(".", "0").replace(" ", "");
    }
}
