
package com.xxyp.xxyp.common.utils.qiniu;

import android.text.TextUtils;

/**
 * Description : 七牛图片配置 Created by sunpengfei on 2017/8/15. Person in charge :
 * sunpengfei
 */
public class QiNiuUtils {

    /**
     * 生成图片缩略图url
     *
     * @param imageUrl 图片地址
     * @param model 生成缩略图模式
     *            {@link QiNiuDownloadUtils.ThumbModel}
     * @param width 缩略图的宽
     * @param height 缩略图的高
     * @param format 缩略图的格式：新图的输出格式 取值范围：jpg，gif，png，webp等，默认为原图格式。
     * @return 缩略图的url
     */
    public static String buildPicThumbUrl(String imageUrl, int model, int width, int height,
                                          String format) {
        String url = buildPicThumbUrl(imageUrl, model, width, height);
        if (TextUtils.isEmpty(url)) {
            throw new IllegalArgumentException("imageurl is llegal");
        }
        return url + "/format/" + format;
    }

    /**
     * 生成图片缩略图url
     *
     * @param imageUrl 图片地址
     * @param model 生成缩略图模式
     *            {@link QiNiuDownloadUtils.ThumbModel}
     * @param width 缩略图的宽
     * @param height 缩略图的高
     * @return 缩略图的url
     */
    public static String buildPicThumbUrl(String imageUrl, int model, int width, int height) {
        if (TextUtils.isEmpty(imageUrl)) {
            throw new IllegalArgumentException("imageurl is llegal");
        }
        StringBuilder url = new StringBuilder(imageUrl);
        if (model < 0 || model > 5) {
            model = QiNiuDownloadUtils.ThumbModel.EDGE_MOST_MODEL;
        }
        url.append("?imageView2/").append(model);
        // 9999七牛支持最大单位
        if (width > 0 && width < 9999) {
            url.append("/w/").append(width);
        }
        if (height > 0 && height < 9999) {
            url.append("/h/").append(height);
        }
        // 设置了此参数时，若图像处理的结果失败，则返回原图
        url.append("/ignore-error/").append(1);
        return url.toString();
    }
}
