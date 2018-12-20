
package com.xxyp.xxyp.user.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.user.bean.FansFocusBean;

import java.util.List;

import rx.Observable;

/**
 * Description : 全部粉丝关注contract
 * Person in charge : sunpengfei
 */
public interface TotalFansFocusContract {

    /**
     * 全部粉丝关注view
     */
    interface View extends IBaseView<Presenter>{

        /**
         * 展示加载框
         * @param cancelable  是否可取消
         */
        void showFansFocusDialog(boolean cancelable);

        /**
         * 取消加载框
         */
        void cancelFansFocusDialog();

        /**
         * 展示关注粉丝数据
         * @param beans  数据
         */
        void showFansFocus(List<FansFocusBean> beans);
    }

    /**
     * 全部粉丝关注presenter
     */
    interface Presenter extends IBasePresenter<View>{

        /**
         * 获取全部粉丝
         * @param userId 用户id
         */
        void obtainTotalFans(String userId);

        /**
         * 获取用户下的全部关注
         * @param userId 用户id
         */
        void obtainTotalFocus(String userId);

        /**
         * 打开frame
         * @param userId 用户id
         */
        void openFrame(String userId);
    }

    /**
     * 全部粉丝关注model
     */
    interface Model {

        /**
         * 获取全部粉丝
         * @param userId 用户id
         */
        Observable<List<FansFocusBean>> obtainTotalFans(String userId);

        /**
         * 获取用户下的全部关注
         * @param userId 用户id
         */
        Observable<List<FansFocusBean>> obtainTotalFocus(String userId);
    }
}
