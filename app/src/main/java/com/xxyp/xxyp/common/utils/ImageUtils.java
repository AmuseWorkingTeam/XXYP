package com.xxyp.xxyp.common.utils;

import android.text.TextUtils;

import com.xxyp.xxyp.common.utils.qiniu.QiNiuConfig;
import com.xxyp.xxyp.common.utils.qiniu.QiNiuDownloadUtils;

/**
 * Description : （类描述，描述当前类具体作用和功能）
 */
public class ImageUtils {

    /**
     * 生成网络图片缩略图
     *
     * @param imgUrl 图片地址
     * @return String
     */
    public static String getImgThumbUrl(String imgUrl) {
        return getImgThumbUrl(imgUrl, 300, 300);
    }

    /**
     * 生成网络图片缩略图
     *
     * @param imgUrl 图片地址
     * @return String
     */
    public static String getImgThumbUrl(String imgUrl, int width, int height) {
        if(TextUtils.isEmpty(imgUrl)){
            return "";
        }
        if(!imgUrl.startsWith(QiNiuConfig.QI_NIU_DNS_PREFIX)){
            //不是七牛云存储的图片直接返回
            return imgUrl;
        }
        return QiNiuDownloadUtils.buildPicThumbUrl(imgUrl,
                QiNiuDownloadUtils.ThumbModel.LEAST_MODEL, width, height, "webp");
    }

    /**
     * 生成网络头像图片缩略图
     *
     * @param avatarUrl 图片地址
     * @return String
     */
    public static String getAvatarUrl(String avatarUrl) {
        return getAvatarUrl(avatarUrl, 100, 100);
    }

    /**
     * 生成网络头像图片缩略图
     *
     * @param avatarUrl 图片地址
     * @return String
     */
    public static String getAvatarUrl(String avatarUrl, int width, int height) {
        if(TextUtils.isEmpty(avatarUrl)){
            return "";
        }
        if(!avatarUrl.startsWith(QiNiuConfig.QI_NIU_DNS_PREFIX)){
            //不是七牛云存储的图片直接返回
            return avatarUrl;
        }
        return QiNiuDownloadUtils.buildPicThumbUrl(avatarUrl,
                QiNiuDownloadUtils.ThumbModel.LEAST_MODEL, width, height, "webp");
    }
}
