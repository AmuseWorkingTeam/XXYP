
package com.xxyp.xxyp.user.provider;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.login.utils.UserConfig;
import com.xxyp.xxyp.user.model.UserModel;
import com.xxyp.xxyp.user.service.UserServiceManager;
import com.xxyp.xxyp.user.utils.FrameConfig;
import com.xxyp.xxyp.user.view.FrameActivity;
import com.xxyp.xxyp.user.view.LocationActivity;
import com.xxyp.xxyp.user.view.MyPhotoActivity;
import com.xxyp.xxyp.user.view.TotalFansFocusActivity;

import org.greenrobot.greendao.annotation.NotNull;

import java.util.List;

import rx.Observable;
import rx.functions.Func1;

/**
 * Description : user provider Created by sunpengfei on 2017/8/4. Person in
 * charge : sunpengfei
 */
public class UserProvider {

    /**
     * 插入数据库
     *
     * @param userInfo 用户信息
     * @return long
     */
    public static long addOrUpdateUserInfo(UserInfo userInfo) {
        return new UserModel().addOrUpdateUserInfo(userInfo);
    }

    /**
     * 批量插入数据库
     *
     * @param userInfos 用户信息
     */
    public static void addOrUpdateUserInfos(List<UserInfo> userInfos) {
        new UserModel().addOrUpdateUserInfos(userInfos);
    }

    /**
     * 获取本地userInfo
     *
     * @param userId 用户id
     * @return UserInfo
     */
    public static UserInfo getUserInfoByDB(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return null;
        }
        return new UserModel().getUserInfoByDB(userId);
    }

    /**
     * 批量获取本地userInfo
     *
     * @param userIds 用户id
     * @return List<UserInfo>
     */
    private static List<UserInfo> getUserInfosByDB(List<String> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return null;
        }
        return new UserModel().getUserInfosByDB(userIds);
    }

    /**
     * 网络获取用户信息
     *
     * @param userId 用户id
     * @return Observable<UserInfo>
     */
    public static Observable<UserInfo> getUserInfoByServer(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return Observable.just(null);
        }
        return UserServiceManager.selectUserInfo(userId).map(new Func1<UserInfo, UserInfo>() {
            @Override
            public UserInfo call(UserInfo userInfo) {
                addOrUpdateUserInfo(userInfo);
                return userInfo;
            }
        });
    }

    /**
     * 获取用户信息
     *
     * @param userId 用户id
     * @return Observable<UserInfo>
     */
    public static Observable<UserInfo> obtainUserInfo(String userId) {
        if (TextUtils.isEmpty(userId)) {
            return Observable.just(null);
        }
        UserInfo userInfo = new UserModel().getUserInfoByDB(userId);
        if (userInfo != null) {
            return Observable.just(userInfo);
        }
        return UserServiceManager.selectUserInfo(userId).map(new Func1<UserInfo, UserInfo>() {
            @Override
            public UserInfo call(UserInfo userInfo) {
                addOrUpdateUserInfo(userInfo);
                return userInfo;
            }
        });
    }

    /**
     * 批量获取用户信息
     *
     * @param userIds 用户id
     * @return Observable<UserInfo>
     */
    public static Observable<List<UserInfo>> obtainUserInfos(List<String> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return Observable.just(null);
        }
        return UserServiceManager.selectUserInfos(userIds).map(new Func1<List<UserInfo>, List<UserInfo>>() {
            @Override
            public List<UserInfo> call(List<UserInfo> userInfos) {
                addOrUpdateUserInfos(userInfos);
                return userInfos;
            }
        });
    }


    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     * @return Observable<Object>
     */
    public static Observable<Object> updateUserInfo(final UserInfo userInfo) {
        if (userInfo == null) {
            return Observable.just(null);
        }
        return UserServiceManager.updateUserInfo(userInfo).map(new Func1<Object, Object>() {
            @Override
            public Object call(Object o) {
                //更新数据库
                addOrUpdateUserInfo(userInfo);
                return o;
            }
        });
    }

    /**
     * 打开frame
     *
     * @param activity 上下文
     * @param userId   用户id
     */
    public static void openFrame(@NonNull Activity activity, String userId) {
        Intent intent = new Intent(activity, FrameActivity.class);
        intent.putExtra(FrameConfig.USER_ID, userId);
        activity.startActivity(intent);
    }

    /**
     * 打开位置
     *
     * @param activity 上下文
     */
    public static void openLocation(@NonNull Activity activity, int requestCode) {
        Intent intent = new Intent(activity, LocationActivity.class);
        activity.startActivityForResult(intent, requestCode);
    }

    /**
     * 打开粉丝列表
     *
     * @param activity 上下文
     * @param userId   用户id
     */
    public static void openFans(@NotNull Activity activity, String userId) {
        Intent intent = new Intent(activity, TotalFansFocusActivity.class);
        intent.putExtra(FrameConfig.USER_ID, userId);
        intent.putExtra(UserConfig.USER_FANS_TYPE, UserConfig.UserFansType.FANS_TYPE);
        activity.startActivity(intent);
    }

    /**
     * 打开关注列表
     *
     * @param activity 上下文
     * @param userId   用户id
     */
    public static void openFocus(@NotNull Activity activity, String userId) {
        Intent intent = new Intent(activity, TotalFansFocusActivity.class);
        intent.putExtra(FrameConfig.USER_ID, userId);
        intent.putExtra(UserConfig.USER_FANS_TYPE, UserConfig.UserFansType.FOCUS_TYPE);
        activity.startActivity(intent);
    }

    /**
     * 打开我的相册
     *
     * @param context
     * @param userId
     */
    public static void openMyPhoto(Activity context, String userId) {
        Intent intent = new Intent(context, MyPhotoActivity.class);
        intent.putExtra(FrameConfig.USER_ID, userId);
        context.startActivity(intent);
    }
}
