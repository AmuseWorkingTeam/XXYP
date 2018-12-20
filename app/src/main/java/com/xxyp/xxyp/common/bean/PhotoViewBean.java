
package com.xxyp.xxyp.common.bean;

/**
 * Description : 查看大图bean
 * Created by sunpengfei on 2017/9/4.
 * Person in charge : sunpengfei
 */
public class PhotoViewBean extends BaseBean {

    /**
     * 原图或者压缩图的路径
     */
    private String localPath;

    /**
     * 图片网络地址
     */
    private String httpUrl;

    /**
     * 图片缩略图地址
     */
    private String thumbHttpUrl;

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getHttpUrl() {
        return httpUrl;
    }

    public void setHttpUrl(String httpUrl) {
        this.httpUrl = httpUrl;
    }

    public String getThumbHttpUrl() {
        return thumbHttpUrl;
    }

    public void setThumbHttpUrl(String thumbHttpUrl) {
        this.thumbHttpUrl = thumbHttpUrl;
    }
}
