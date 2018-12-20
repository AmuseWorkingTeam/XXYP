
package com.xxyp.xxyp.login.model;

import com.xxyp.xxyp.common.base.XXApplication;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.login.bean.RegisterResultBean;
import com.xxyp.xxyp.login.contract.RegisterContract;
import com.xxyp.xxyp.login.service.RegisterServiceManager;
import com.xxyp.xxyp.user.provider.UserProvider;

import rx.Observable;
import rx.functions.Func1;

/**
 * Description : 注册model Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class RegisterModel implements RegisterContract.Model {

    /**
     * 初始化DB
     * @param userId  用户id
     */
    private void init(String userId) {
        XXApplication.getInstance().initDB();
        XXApplication.getInstance().initAVOSCloud();
    }

    @Override
    public Observable<UserInfo> createUserInfo(final UserInfo userInfo) {
        if(userInfo == null){
            return Observable.just(null);
        }
        return RegisterServiceManager.createUserInfo(userInfo).map(new Func1<RegisterResultBean, UserInfo>() {
            @Override
            public UserInfo call(RegisterResultBean registerResultBean) {
                if (registerResultBean != null) {
                    String userId = registerResultBean.getUserId();
                    String token = registerResultBean.getToken();
                    SharePreferenceUtils.getInstance().putUserId(userId);
                    SharePreferenceUtils.getInstance().putToken(token);

                    userInfo.setUserId(userId);
                    userInfo.setStatus(0);
                    init(userId);
                    UserProvider.addOrUpdateUserInfo(userInfo);
                    return userInfo;
                }
                return null;
            }
        });
    }
}
