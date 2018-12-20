
package com.xxyp.xxyp.common.utils.qiniu;

import android.text.TextUtils;

/**
 * Description : 用于从七牛服务器下载时，会对url做一些额外的封装，比如获取缩略图，水印,视频的第一帧等 Created by wxh on
 * 2016/6/3. Job number：138746 Phone ：15233620521 Email：wangxiaohui@syswin.com
 * Person in charge : 王晓辉 Leader：王晓辉
 */

public class QiNiuDownloadUtils {

    /**
     * 生成图片缩略图url
     *
     * @param imageUrl 图片地址
     * @param model 生成缩略图模式
     *            {}
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
            model = ThumbModel.EDGE_MOST_MODEL;
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

    /**
     * @param videoUrl 视频名字
     * @param width 宽度：缩略图宽度，单位：像素（px），取值范围为1-3840。
     * @param height 高度：缩略图高度，单位：像素（px），取值范围为1-2160。
     * @return String
     */
    public static String buildVideoThumbUrl(String videoUrl, int width, int height) {
        String format = "jpg";
        long offset = 0;
        String rotate = "auto";
        return buildVideoThumbUrl(videoUrl, format, offset, width, height, rotate);

    }

    /**
     * 获取视频缩略图url
     * 
     * @param videoUrl 视频名字
     * @param format 视频缩略图格式，必填：输出的目标截图格式，支持jpg、png等
     * @param offset 第几帧数据，必填：指定截取视频的时刻，单位：秒，精确到毫秒
     * @param width 宽度：缩略图宽度，单位：像素（px），取值范围为1-3840。
     * @param height 高度：缩略图高度，单位：像素（px），取值范围为1-2160。
     * @param rotate 旋转： 指定顺时针旋转的度数，可取值为90、180、270、auto，默认为不旋转。
     */
    public static String buildVideoThumbUrl(String videoUrl, String format, long offset, int width,
            int height, String rotate) {
        if (TextUtils.isEmpty(videoUrl) || TextUtils.isEmpty(format) || offset < 0) {
            throw new IllegalArgumentException("videoUrl or format or offset is Illegal");
        }
        StringBuilder url = new StringBuilder(videoUrl);
        url.append("?vframe/");
        url.append(format).append("/").append("offset/").append(offset);
        // 视频缩略图的宽度
        if (width > 1 && width < 3840) {
            url.append("/w/").append(width);
        }
        // 视频缩略图的高度
        if (height > 1 && height < 2160) {
            url.append("/h/").append(height);
        }
        // 旋转
        if (!TextUtils.isEmpty(rotate)) {
            url.append("/rotate/").append(rotate);
        }
        return url.toString();
    }

    public interface ThumbModel {
        /**
         * 限定缩略图的长边最多为LongEdge，短边最多为ShortEdge，进行等比缩放，不裁剪。如果只指定 w
         * 参数则表示限定长边（短边自适应），只指定 h 参数则表示限定短边（长边自适应）.更适合移动设备上做缩略图
         */
        int EDGE_MOST_MODEL = 0;

        /**
         * 限定缩略图的宽最少为Width，高最少为Height，进行等比缩放，居中裁剪。转后的缩略图通常恰好是 Width* Height
         * 的大小（有一个边缩放的时候会因为超出矩形框而被裁剪掉多余部分）。如果只指定 w 参数或只指定 h 参数，代表限定为长宽相等的正方图
         */
        int LEAST_CUT_MODEL = 1;

        /**
         * 限定缩略图的宽最多为Width，高最多为Height，进行等比缩放，不裁剪。如果只指定 w 参数则表示限定宽（长自适应），只指定 h
         * 参数则表示限定长（宽自适应）。它和模式0类似，区别只是限定宽和高，不是限定长边和短边.更适合PC设备上做缩略图
         */
        int MOST_MODEL = 2;

        /**
         * 限定缩略图的宽最少为Width，高最少为Height，进行等比缩放，不裁剪。如果只指定 w 参数或只指定 h
         * 参数，代表长宽限定为同样的值。你可以理解为模式1是模式3的结果再做居中裁剪得到的
         */
        int LEAST_MODEL = 3;

        /**
         * 限定缩略图的长边最少为LongEdge，短边最少为ShortEdge，进行等比缩放，不裁剪。如果只指定 w 参数或只指定 h
         * 参数，表示长边短边限定为同样的值。这个模式很适合在手持设备做图片的全屏查看（把这里的长边短边分别设为手机屏幕的分辨率即可），
         * 生成的图片尺寸刚好充满整个屏幕（某一个边可能会超出屏幕）。
         */
        int EDGE_LEAST_MODEL = 4;

        /**
         * 限定缩略图的长边最少为LongEdge，短边最少为ShortEdge，进行等比缩放，居中裁剪。如果只指定 w 参数或只指定 h
         * 参数，表示长边短边限定为同样的值。同上模式4，但超出限定的矩形部分会被裁剪
         */
        int EDGE_LEAST_CUT_MODEL = 5;
    }
}
