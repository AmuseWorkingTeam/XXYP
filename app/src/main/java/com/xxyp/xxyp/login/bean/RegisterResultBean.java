
package com.xxyp.xxyp.login.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

/**
 * Description : 注册返回数据格式 Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class RegisterResultBean extends BaseBean {

    private String status;

    private String token;

    private String userId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
