
package com.xxyp.xxyp.common.utils;

import java.io.File;

/**
 * @author Created by T@XiaoLv on 2015/11/12.
 */

/**
 * Description : 下载时回调
 * Created by wxh on 2016/6/21 13:34.
 * Job number：138746
 * Phone ：15233620521
 * Email：wangxiaohui@syswin.com
 * Person in charge : 王晓辉
 * Leader：王晓辉
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
