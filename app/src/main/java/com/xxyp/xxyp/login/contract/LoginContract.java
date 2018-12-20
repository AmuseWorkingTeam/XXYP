
package com.xxyp.xxyp.login.contract;

import android.content.Intent;

import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;

import rx.Observable;

/**
 * Description : 登陆contract Created by sunpengfei on 2017/7/31. Person in charge
 * : sunpengfei
 */
public interface LoginContract {

    /**
     * 登陆view
     */
    interface View extends IBaseView<Presenter> {

        /**
         * 展示加载框
         * @param cancelable  是否可取消
         */
        void showLoginLoading(boolean cancelable);

        /**
         * 取消加载框
         */
        void cancelLoginLoading();
    }

    /**
     * 登陆presenter
     */
    interface Presenter extends IBasePresenter<View> {

        /**
         * 检查是否需要进行登陆
         */
        void checkIsLogin();

        /**
         * 登陆
         * @param shareMedia  登陆方式 微信 qq 微博
         */
        void login(SHARE_MEDIA shareMedia);

        /**
         * 数据返回
         * @param requestCode   请求码
         * @param resultCode    返回码
         * @param data          返回数据
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);

    }

    /**
     * 登陆model
     */
    interface Model {

        /**
         * 登陆应用
         * @param userName    用户名称
         * @param userSource  用户登陆平台
         * @param userSourceId 平台id
         * @return Observable<Object>
         */
        Observable<Object> login(String userName, String userSource,
                                 String userSourceId);
    }

}
