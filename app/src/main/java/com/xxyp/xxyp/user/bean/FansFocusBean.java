
package com.xxyp.xxyp.user.bean;

import com.xxyp.xxyp.common.bean.BaseBean;
import com.xxyp.xxyp.common.bean.UserInfo;

/**
 * Description : 全部粉丝关注 Created by sunpengfei on 2017/10/19. Person in charge :
 * sunpengfei
 */
public class FansFocusBean extends BaseBean {

    /* fansId */
    private int fansId;

    /* 关注用户 */
    private UserInfo fromUser;

    /* 被关注用户 */
    private UserInfo toUser;

    /* 双方关系状态 0 互相关注  1单一关系 被关注或关注*/
    private int relationStatus;

    private int status;

    public int getFansId() {
        return fansId;
    }

    public void setFansId(int fansId) {
        this.fansId = fansId;
    }

    public UserInfo getFromUser() {
        return fromUser;
    }

    public void setFromUser(UserInfo fromUser) {
        this.fromUser = fromUser;
    }

    public UserInfo getToUser() {
        return toUser;
    }

    public void setToUser(UserInfo toUser) {
        this.toUser = toUser;
    }

    public int getRelationStatus() {
        return relationStatus;
    }

    public void setRelationStatus(int relationStatus) {
        this.relationStatus = relationStatus;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
