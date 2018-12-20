
package com.xxyp.xxyp.user.contract;

import com.xxyp.xxyp.common.base.IBasePresenter;
import com.xxyp.xxyp.common.base.IBaseView;
import com.xxyp.xxyp.main.bean.ShotBean;

import java.util.List;

/**
 * Description : 我的约拍contract
 * Created by sunpengfei on 2017/8/11.
 * Person in charge : sunpengfei
 */
public interface MyDatingShotContract {

    /**
     * 我的约拍view
     */
    interface View extends IBaseView<Presenter>{

        /**
         * 展示我的约拍数据
         * @param shotBeans  约拍数据
         */
        void showMyShot(List<ShotBean> shotBeans);
    }

    /**
     * 我的约拍presenter
     */
    interface Presenter extends IBasePresenter<View>{

        /**
         * 获取我的约拍
         */
        void getMyDatingShot();
    }

    /**
     * 我的约拍model
     */
    interface Model{

        /**
         * 获取我的约拍
         * @return List
         */
        List<ShotBean> getMyShotInfo();
    }

}
