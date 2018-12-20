
package com.xxyp.xxyp.find.model;

import com.xxyp.xxyp.find.contract.ShotDetailContract;
import com.xxyp.xxyp.user.bean.UserShotListBean;
import com.xxyp.xxyp.user.service.UserServiceManager;

import rx.Observable;

/**
 * Description : 约拍详情model
 * Created by sunpengfei on 2017/8/28.
 * Person in charge : sunpengfei
 */
public class ShotDetailModel implements ShotDetailContract.Model {
    @Override
    public Observable<UserShotListBean> obtainShot(String userId, String shotId) {
        return UserServiceManager.getDatingShot(userId, shotId);
    }
}
