
package com.xxyp.xxyp.user.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 退出登录
 * Created by sunpengfei on 2017/8/30.
 * Person in charge : sunpengfei
 */
public class LogoutInput extends BaseBean {

    private String userId;

    private String token;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
