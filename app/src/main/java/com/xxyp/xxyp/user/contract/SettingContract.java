
package com.xxyp.xxyp.user.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;

import rx.Observable;

/**
 * Description : 设置
 * Created by sunpengfei on 2017/8/11.
 * Person in charge : sunpengfei
 */
public interface SettingContract {

    /**
     * 我的约拍view
     */
    interface View extends IBaseView<Presenter> {
        /**
         * 展示加载框
         *
         * @param cancelable 是否可取消
         */
        void showLoadingDialog(boolean cancelable);

        /**
         * 取消加载框
         */
        void dismissLoadingDialog();
    }

    /**
     * 我的约拍presenter
     */
    interface Presenter extends IBasePresenter<View> {

        void logout();

        void clearData();
    }

    /**
     * 我的约拍model
     */
    interface Model {

        Observable<Object> logout();
    }

}
