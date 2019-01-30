
package com.xxyp.xxyp.user.service;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.service.BaseServiceManager;
import com.xxyp.xxyp.user.bean.CreateFansInput;
import com.xxyp.xxyp.user.bean.FansFocusBean;
import com.xxyp.xxyp.user.bean.FansFocusListBean;
import com.xxyp.xxyp.user.bean.LogoutInput;
import com.xxyp.xxyp.user.bean.MyShotPhotoOutput;
import com.xxyp.xxyp.user.bean.UpdateFansInput;
import com.xxyp.xxyp.user.bean.UserInfoList;
import com.xxyp.xxyp.user.bean.UserShotListBean;
import com.xxyp.xxyp.user.bean.UserWorkListBean;

import java.util.List;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Description : 用户接口管理类 Created by sunpengfei on 2017/8/2. Person in charge :
 * sunpengfei
 */

public class UserServiceManager extends BaseServiceManager {

    /**
     * 获取粉丝点赞数目
     *
     * @return Observable
     */
    public static Observable<Object> getFansCount(String userId) {
        return mManager.create(UserService.class).getFansCount(userId).flatMap(new Func1<ResponseBody, Observable<Object>>() {
            @Override
            public Observable<Object> call(ResponseBody responseBody) {
                return toObservable(responseBody, Object.class);
            }
        });
    }

    /**
     * 获取粉丝点赞数目
     *
     * @return Observable
     */
    public static Observable<Object> getUserHasFansAndFollow(String myUserId, String otherUserId) {
        return mManager.create(UserService.class).getUserHasFansAndFollow(myUserId, otherUserId).flatMap(new Func1<ResponseBody, Observable<Object>>() {
            @Override
            public Observable<Object> call(ResponseBody responseBody) {
                return toObservable(responseBody, Object.class);
            }
        });
    }

    /**
     * 获取用户作品
     *
     * @param userId 用户id
     * @param workId 作品id  如果不穿则获取所有
     * @return Observable
     */
    public static Observable<UserWorkListBean> getWorks(String userId, String workId) {
        return mManager.create(UserService.class).getWorks(userId, workId)
                .flatMap(new Func1<ResponseBody, Observable<UserWorkListBean>>() {
                    @Override
                    public Observable<UserWorkListBean> call(ResponseBody responseBody) {
                        return toObservable(responseBody, UserWorkListBean.class);
                    }
                });
    }

    /**
     * 获取用户约拍
     *
     * @param userId 用户id
     * @param shotId 约拍id  如果不穿则获取所有
     * @return Observable
     */
    public static Observable<UserShotListBean> getDatingShot(String userId, String shotId) {
        return mManager.create(UserService.class).getDatingShot(userId, shotId)
                .flatMap(new Func1<ResponseBody, Observable<UserShotListBean>>() {
                    @Override
                    public Observable<UserShotListBean> call(ResponseBody responseBody) {
                        return toObservable(responseBody, UserShotListBean.class);
                    }
                });
    }

    /**
     * 获取用户约拍
     *
     * @return Observable
     */
    public static Observable<MyShotPhotoOutput> getWorkPhotos(String userId, int pageSize, int pageIndex) {
        return mManager.create(UserService.class).getWorkPhotos(userId, pageSize, pageIndex)
                .flatMap(new Func1<ResponseBody, Observable<MyShotPhotoOutput>>() {
                    @Override
                    public Observable<MyShotPhotoOutput> call(ResponseBody responseBody) {
                        return toObservable(responseBody, MyShotPhotoOutput.class);
                    }
                });
    }

    /**
     * 获取单个用户信息
     *
     * @param userId 用户id
     * @return Observable
     */
    public static Observable<UserInfo> selectUserInfo(String userId) {
        return mManager.create(UserService.class).selectUserInfo(userId)
                .flatMap(new Func1<ResponseBody, Observable<UserInfoList>>() {
                    @Override
                    public Observable<UserInfoList> call(ResponseBody responseBody) {
                        return toObservable(responseBody, UserInfoList.class);
                    }
                }).map(new Func1<UserInfoList, UserInfo>() {
                    @Override
                    public UserInfo call(UserInfoList userInfoList) {
                        if (userInfoList != null && userInfoList.getUsers() != null
                                && userInfoList.getUsers().size() > 0) {
                            return userInfoList.getUsers().get(0);
                        }
                        return null;
                    }
                });
    }

    /**
     * 批量获取用户信息
     *
     * @param userIds 用户id列表
     * @return Observable
     */
    public static Observable<List<UserInfo>> selectUserInfos(List<String> userIds) {
        if (userIds == null || userIds.size() == 0) {
            return Observable.empty();
        }
        StringBuilder result = new StringBuilder();
        int size = userIds.size();
        for (int i = 0; i < size; i++) {
            result.append(userIds.get(i));
            if (size != i + 1) {
                result.append(",");
            }
        }
        return mManager.create(UserService.class).selectUserInfo(result.toString())
                .flatMap(new Func1<ResponseBody, Observable<UserInfoList>>() {
                    @Override
                    public Observable<UserInfoList> call(ResponseBody responseBody) {
                        return toObservable(responseBody, UserInfoList.class);
                    }
                }).map(new Func1<UserInfoList, List<UserInfo>>() {
                    @Override
                    public List<UserInfo> call(UserInfoList userInfoList) {
                        if (userInfoList != null) {
                            return userInfoList.getUsers();
                        }
                        return null;
                    }
                });
    }

    /**
     * 关注某用户
     *
     * @param input 创建粉丝入参
     * @return Observable
     */
    public static Observable<Object> createFans(CreateFansInput input) {
        return mManager.create(UserService.class).createFans(input)
                .flatMap(new Func1<ResponseBody, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(ResponseBody responseBody) {
                        return toObservable(responseBody, Object.class);
                    }
                });
    }

    /**
     * 修改关注
     *
     * @param input 创建粉丝入参
     * @return Observable
     */
    public static Observable<Object> updateFans(UpdateFansInput input) {
        return mManager.create(UserService.class).updateFans(input)
                .flatMap(new Func1<ResponseBody, Observable<Object>>() {
                    @Override
                    public Observable<Object> call(ResponseBody responseBody) {
                        return toObservable(responseBody, Object.class);
                    }
                });
    }

    /**
     * 获取用户下的粉丝或关注
     *
     * @param fromUserId 如果传入from 获取此用户下的关注
     * @param toUserId   如果传入to 获取此用户下的粉丝
     * @return Observable
     */
    public static Observable<List<FansFocusBean>> getFans(String fromUserId, String toUserId) {
        return mManager.create(UserService.class).getFans(fromUserId, toUserId)
                .flatMap(new Func1<ResponseBody, Observable<FansFocusListBean>>() {
                    @Override
                    public Observable<FansFocusListBean> call(ResponseBody responseBody) {
                        return toObservable(responseBody, FansFocusListBean.class);
                    }
                }).map(new Func1<FansFocusListBean, List<FansFocusBean>>() {
                    @Override
                    public List<FansFocusBean> call(FansFocusListBean fansFocusListBean) {
                        return fansFocusListBean != null ? fansFocusListBean.getFans() : null;
                    }
                });
    }

    /**
     * 更新用户信息
     *
     * @param userInfo 用户信息
     * @return Observable
     */
    public static Observable<Object> updateUserInfo(UserInfo userInfo) {
        return mManager.create(UserService.class).updateUserInfo(userInfo).flatMap(new Func1<ResponseBody, Observable<Object>>() {
            @Override
            public Observable<Object> call(ResponseBody responseBody) {
                return toObservable(responseBody, Object.class);
            }
        });
    }

    /**
     * 退出登录
     *
     * @return Observable
     */
    public static Observable<Object> logout(LogoutInput input) {
        return mManager.create(UserService.class).logout(input).flatMap(new Func1<ResponseBody, Observable<Object>>() {
            @Override
            public Observable<Object> call(ResponseBody responseBody) {
                return toObservable(responseBody, Object.class);
            }
        });
    }
}
