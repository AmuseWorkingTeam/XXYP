
package com.xxyp.xxyp.main.model;

import com.xxyp.xxyp.main.bean.WorkHotListBean;
import com.xxyp.xxyp.main.contract.MainContract;
import com.xxyp.xxyp.main.service.MainServiceManager;

import rx.Observable;

/**
 * Description : 首页model Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class MainModel implements MainContract.Model {

    @Override
    public Observable<WorkHotListBean> getHotWorks() {
        return MainServiceManager.getHotWorks();
    }
}
