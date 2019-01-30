
package com.xxyp.xxyp.user.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 修改
 * Created by sunpengfei on 2017/8/30.
 * Person in charge : sunpengfei
 */
public class UpdateFansInput extends BaseBean {

    private String fromUserId;// 用户的Id

    private String toUserId;// 关注用户Id

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }
}
