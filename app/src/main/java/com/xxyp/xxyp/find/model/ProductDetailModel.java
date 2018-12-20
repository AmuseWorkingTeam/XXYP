
package com.xxyp.xxyp.find.model;

import com.xxyp.xxyp.find.contract.ProductDetailContract;
import com.xxyp.xxyp.user.bean.UserWorkListBean;
import com.xxyp.xxyp.user.service.UserServiceManager;

import rx.Observable;

/**
 * Description : 作品详情model
 * Created by sunpengfei on 2017/8/9.
 * Person in charge : sunpengfei
 */
public class ProductDetailModel implements ProductDetailContract.Model {
    @Override
    public Observable<UserWorkListBean> obtainWorks(String userId, String workId) {
        return UserServiceManager.getWorks(userId, workId);
    }
}
