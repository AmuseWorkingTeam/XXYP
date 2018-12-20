
package com.xxyp.xxyp.login.model;

import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.login.contract.LoginContract;
import com.xxyp.xxyp.login.service.RegisterServiceManager;

import rx.Observable;
import rx.functions.Func1;

/**
 * Description : 登陆model Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class LoginModel implements LoginContract.Model {

    @Override
    public Observable<Object> login(String userName, String userSource, String userSourceId) {
        return RegisterServiceManager.login(userName, userSource, userSourceId).map(new Func1<Object, Object>() {
            @Override
            public Object call(Object o) {
                //设置登陆状态为true
                SharePreferenceUtils.getInstance().putLoginStatus(true);
                return o;
            }
        });
    }
}
