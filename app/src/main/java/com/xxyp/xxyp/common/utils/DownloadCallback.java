
package com.xxyp.xxyp.common.utils;

import java.io.File;

/**
 */

/**
 * Description : 下载时回调
 */
public interface DownloadCallback {

    /**
     * 子线程
     */
    void postDownloadProgress(long totalSize, long currentSize);

    /**
     * 主线程
     */
    void postFail(File file, int errorCode);

    /**
     * 主线程
     */
    void postSuccess(File localFile);

    /**
     * 子线程
     */
    void postCancel(File file);

}
