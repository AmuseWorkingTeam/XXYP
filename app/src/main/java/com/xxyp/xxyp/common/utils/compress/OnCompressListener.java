
package com.xxyp.xxyp.common.utils.compress;

import java.io.File;

/**
 * Description : 压缩监听 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public interface OnCompressListener {

    /**
     * 压缩开始时调用，可具体实现
     */
    void onStart();

    /**
     * 压缩成功时触发
     */
    void onSuccess(File file);

    /**
     * 压缩过程出现意外
     */
    void onError(Throwable e);
}
