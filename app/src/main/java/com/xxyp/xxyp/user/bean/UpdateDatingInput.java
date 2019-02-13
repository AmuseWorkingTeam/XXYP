
package com.xxyp.xxyp.user.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 创建粉丝
 * Created by sunpengfei on 2017/8/30.
 * Person in charge : sunpengfei
 */
public class UpdateDatingInput extends BaseBean {

    private String fromUserId;

    private String toUserId;

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
