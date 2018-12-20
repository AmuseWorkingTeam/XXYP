
package com.xxyp.xxyp.user.bean;

import com.xxyp.xxyp.common.bean.BaseBean;
import com.xxyp.xxyp.common.bean.UserInfo;

import java.util.List;

/**
 * Created by sunpengfei on 2017/8/14.
 * Person in charge : sunpengfei
 */
public class UserInfoList extends BaseBean {

    /**
     * 用户列表
     */
    private List<UserInfo> users;

    public List<UserInfo> getUsers() {
        return users;
    }

    public void setUsers(List<UserInfo> users) {
        this.users = users;
    }
}
