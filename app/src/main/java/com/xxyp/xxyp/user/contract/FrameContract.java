
package com.xxyp.xxyp.user.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.main.bean.WorkBean;
import com.xxyp.xxyp.user.bean.UserWorkListBean;

import java.util.List;
import java.util.Map;

import rx.Observable;

/**
 * Description : frame Created by sunpengfei on 2017/8/2. Person in charge :
 * sunpengfei
 */
public interface FrameContract {

    /**
     * frame页面view
     */
    interface View extends IBaseView<Presenter> {

        /**
         * 展示用户信息
         *
         * @param userInfo 用户信息
         */
        void showUserInfo(UserInfo userInfo);

        /**
         * 展示作品
         *
         * @param workBeans 作品列表
         */
        void showUserWorks(List<WorkBean> workBeans);

        /**
         * 展示关注 粉丝数目
         *
         * @param followCount 关注人数
         * @param fansCount   粉丝人数
         */
        void showFansFollowCount(int followCount, int fansCount);

        /**
         * 展示加载框
         *
         * @param cancelable 是否可取消
         */
        void showFrameDialog(boolean cancelable);

        /**
         * 取消加载框
         */
        void cancelFrameDialog();

        /**
         * 显示关注
         */
        void showFocus(boolean isFocus);
    }

    /**
     * frame页面presenter
     */
    interface Presenter extends IBasePresenter<View> {

        /**
         * 获取用户信息
         *
         * @param userId 用户id
         */
        void getUserInfo(String userId);

        /**
         * 获取粉丝关注人数
         *
         * @param userId 用户id
         */
        void getFansFollowCount(String userId);

        /**
         * 获取用户作品
         *
         * @param userId 用户id
         */
        void obtainUserWorks(String userId);

        /**
         * 进入作品详情
         *
         * @param userId 用户id
         * @param workId 作品id
         */
        void openProduct(String userId, String workId);

        /**
         * 进入聊天页面
         *
         * @param userId 用户id
         */
        void openChat(String userId);

        /**
         * 打开粉丝
         *
         * @param userId 用户id
         */
        void openFans(String userId);

        /**
         * 打开关注
         *
         * @param userId 用户id
         */
        void openFocus(String userId);

        /**
         * 关注此用户
         *
         * @param userId 被关注的userId
         */
        void focusUser(String userId);

        /**
         * 取消关注
         *
         * @param userId 被关注的userId
         */
        void cancelFocus(String userId);

        void getUserHasFansAndFollow(String userId, String otherUserId);
    }

    /**
     * frame页面model
     */
    interface Model {

        /**
         * 获取作品列表
         *
         * @param userId 用户id
         * @return Observable
         */
        Observable<UserWorkListBean> getWorks(String userId);

        /**
         * 关注此用户
         *
         * @param userId 被关注的userId
         * @return Observable
         */
        Observable<Object> focusUser(String userId);

        /**
         * 取消关注用户
         *
         * @param userId 被关注的userId
         * @return Observable
         */
        Observable<Object> cancelFocus(String userId);

        /**
         * 根据useId获取关注和粉丝的数目
         *
         * @return Observable
         */
        Observable<Map<String, Integer>> getFansCount(String useId);

        /**
         * 根据userid获取关系
         *
         * @return Observable
         */
        Observable<Map<String, Integer>> getUserHasFansAndFollow(String myUserId, String otherUserId);
    }

}
