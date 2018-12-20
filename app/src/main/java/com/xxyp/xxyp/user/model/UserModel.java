
package com.xxyp.xxyp.user.model;

import android.text.TextUtils;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.user.db.UserDBManager;

import java.util.List;

/**
 * Description : 用户model Created by sunpengfei on 2017/8/2. Person in charge :
 * sunpengfei
 */
public class UserModel {

    /**
     * 插入用户信息
     * @param userInfo  用户信息
     * @return long
     */
    public long addOrUpdateUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return -1;
        }
        if(isExitUserInfo(userInfo.getUserId())){
            return UserDBManager.getInstance().updateUserInfo(userInfo);
        }
        return UserDBManager.getInstance().addUserInfo(null, userInfo);
    }

    /**
     * 批量插入用户信息
     * @param userInfos  用户信息列表
     */
    public void addOrUpdateUserInfos(List<UserInfo> userInfos) {
        if (userInfos == null || userInfos.size() == 0) {
            return;
        }
        UserDBManager.getInstance().addOrUpdateUserInfos(userInfos);
    }

    /**
     * 获取用户信息
     * @param userId 用户id
     * @return UserInfo
     */
    public UserInfo getUserInfoByDB(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        return UserDBManager.getInstance().getUserById(userId);
    }

    /**
     * 批量获取用户信息
     * @param userIds 用户id
     * @return List<UserInfo>
     */
    public List<UserInfo> getUserInfosByDB(List<String> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return null;
        }
        return UserDBManager.getInstance().getUsersByIds(userIds);
    }

    /**
     * 是否存在用户信息
     * @param userId  用户id
     * @return boolean
     */
    public boolean isExitUserInfo(String userId){
        if(TextUtils.isEmpty(userId)){
            return false;
        }
        return UserDBManager.getInstance().isExistUserInfo(userId);
    }
}
