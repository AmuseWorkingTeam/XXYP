
package com.xxyp.xxyp.main.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 约拍照片bean
 * Created by sunpengfei on 2017/8/10.
 * Person in charge : sunpengfei
 */
public class ShotPhotoBean extends BaseBean {

    /* 约拍照片id */
    private String datingShotPhotoId;

    /* 约拍id */
    private String datingShotId;

    /* 约拍图片 */
    private String datingShotPhoto;

    /* 约拍图片顺序 */
    private int datingShotImageOrder;

    /* 状态 */
    private int status;

    public String getDatingShotPhotoId() {
        return datingShotPhotoId;
    }

    public void setDatingShotPhotoId(String datingShotPhotoId) {
        this.datingShotPhotoId = datingShotPhotoId;
    }

    public String getDatingShotId() {
        return datingShotId;
    }

    public void setDatingShotId(String datingShotId) {
        this.datingShotId = datingShotId;
    }

    public String getDatingShotPhoto() {
        return datingShotPhoto;
    }

    public void setDatingShotPhoto(String datingShotPhoto) {
        this.datingShotPhoto = datingShotPhoto;
    }

    public int getDatingShotImageOrder() {
        return datingShotImageOrder;
    }

    public void setDatingShotImageOrder(int datingShotImageOrder) {
        this.datingShotImageOrder = datingShotImageOrder;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
