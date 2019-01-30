package com.xxyp.xxyp.user.bean;

import com.xxyp.xxyp.main.bean.WorkPhotoBean;

import java.util.List;

public class MyShotPhotoOutput {

    private int pageSize;
    private int pageIndex;
    private List<WorkPhotoBean> worksPhotos;

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public List<WorkPhotoBean> getShotPhotos() {
        return worksPhotos;
    }

    public void setShotPhotos(List<WorkPhotoBean> shotPhotos) {
        this.worksPhotos = shotPhotos;
    }

}
