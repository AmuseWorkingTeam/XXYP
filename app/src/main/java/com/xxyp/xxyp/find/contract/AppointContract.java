
package com.xxyp.xxyp.find.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.main.bean.ShotBean;

import java.util.List;

/**
 * Description : 发现约拍contract
 * Created by sunpengfei on 2017/8/21.
 * Person in charge : sunpengfei
 */
public interface AppointContract {

    /**
     * 发现约拍view
     */
    interface View extends IBaseView<Presenter>{

        /**
         * 展示约拍数据
         * @param shotBeans  约拍数据
         */
        void showShotList(List<ShotBean> shotBeans);

        /**
         * 展示加载框
         * @param isCancelable  是否可取消
         */
        void showShotLoading(boolean isCancelable);

        /**
         * 取消加载框
         */
        void cancelShotLoading();

        /**
         * 还原下拉刷新
         */
        void resetRefresh();
    }

    /**
     * 发现约拍presenter
     */
    interface Presenter extends IBasePresenter<View>{

        /**
         * 获取约拍列表
         */
        void obtainShotList();

        /**
         * 跳转用户详情页
         * @param userId  用户id
         */
        void openFrame(String userId);

        /**
         * 跳转约拍详情
         * @param userId  用户id
         * @param shotId  约拍id
         */
        void openShotDetail(String userId, String shotId);
    }
}
