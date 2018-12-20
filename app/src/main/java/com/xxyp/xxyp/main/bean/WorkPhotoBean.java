
package com.xxyp.xxyp.main.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 热门bean Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class WorkPhotoBean extends BaseBean {

    private int status;

    private String worksId;

    private String worksImageCommentCount;

    private String worksImageLikeCount;

    private int worksImageOrder;

    private String worksPhoto;

    private int worksPhotoId;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getWorksId() {
        return worksId;
    }

    public void setWorksId(String worksId) {
        this.worksId = worksId;
    }

    public String getWorksImageCommentCount() {
        return worksImageCommentCount;
    }

    public void setWorksImageCommentCount(String worksImageCommentCount) {
        this.worksImageCommentCount = worksImageCommentCount;
    }

    public String getWorksImageLikeCount() {
        return worksImageLikeCount;
    }

    public void setWorksImageLikeCount(String worksImageLikeCount) {
        this.worksImageLikeCount = worksImageLikeCount;
    }

    public int getWorksImageOrder() {
        return worksImageOrder;
    }

    public void setWorksImageOrder(int worksImageOrder) {
        this.worksImageOrder = worksImageOrder;
    }

    public String getWorksPhoto() {
        return worksPhoto;
    }

    public void setWorksPhoto(String worksPhoto) {
        this.worksPhoto = worksPhoto;
    }

    public int getWorksPhotoId() {
        return worksPhotoId;
    }

    public void setWorksPhotoId(int worksPhotoId) {
        this.worksPhotoId = worksPhotoId;
    }
}
