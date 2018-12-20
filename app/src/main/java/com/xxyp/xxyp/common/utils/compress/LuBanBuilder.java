
package com.xxyp.xxyp.common.utils.compress;

import android.graphics.Bitmap;

import java.io.File;

/**
 * Description : luBan压缩 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public class LuBanBuilder {

    /*
     * 图片压缩最大size默认200k
     */
    int maxSize = 200;

    /*
     * 图片最大宽度
     */
    int maxWidth;

    /*
     * 图片最大高度
     */
    int maxHeight;

    /*
     * 缓存目录
     */
    File cacheDir;

    /*
     * 图片压缩格式
     */
    Bitmap.CompressFormat compressFormat = Bitmap.CompressFormat.JPEG;

    /*
     * 压缩后文件名，如果不设置，则用(源文件绝对路径+源文件大小)hash算法生成
     */
    String compressFileName;

    /*
     * 压缩级别
     */
    int gear = LuBan.THIRD_GEAR;

    LuBanBuilder(File cacheDir) {
        this.cacheDir = cacheDir;
    }
}
