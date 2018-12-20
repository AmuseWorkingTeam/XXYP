
package com.xxyp.xxyp.main.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

import java.util.List;

/**
 * Description : 作品数据 Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class WorkBean extends BaseBean {

    private List<WorkPhotoBean> list;

    private long releaseTime;

    private int status;

    private String userId;

    private String userImage;

    private String userName;

    private String worksAddress;

    private String worksId;

    private String worksIntroduction;

    private String worksTitle;

    public List<WorkPhotoBean> getList() {
        return list;
    }

    public void setList(List<WorkPhotoBean> list) {
        this.list = list;
    }

    public long getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(long releaseTime) {
        this.releaseTime = releaseTime;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getWorksAddress() {
        return worksAddress;
    }

    public void setWorksAddress(String worksAddress) {
        this.worksAddress = worksAddress;
    }

    public String getWorksId() {
        return worksId;
    }

    public void setWorksId(String worksId) {
        this.worksId = worksId;
    }

    public String getWorksIntroduction() {
        return worksIntroduction;
    }

    public void setWorksIntroduction(String worksIntroduction) {
        this.worksIntroduction = worksIntroduction;
    }

    public String getWorksTitle() {
        return worksTitle;
    }

    public void setWorksTitle(String worksTitle) {
        this.worksTitle = worksTitle;
    }
}
