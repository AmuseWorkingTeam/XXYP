
package com.xxyp.xxyp.common.utils.qiniu;

/**
 * Description : 上传接口的抽象实现类
 */

public interface QiNiuUploadCallback {

    /**
     * 上传进度
     * @param progress  进度
     */
    void onProgress(int progress);

    /**
     * 上传成功
     * @param file  上传后的文件
     */
    void onSuccess(String file);

    /**
     * 上传失败
     * @param errorCode  错误码
     * @param msg        错误信息
     */
    void onError(int errorCode, String msg);
}
