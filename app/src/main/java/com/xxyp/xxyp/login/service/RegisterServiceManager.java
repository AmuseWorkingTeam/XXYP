
package com.xxyp.xxyp.login.service;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.service.BaseServiceManager;
import com.xxyp.xxyp.login.bean.RegisterResultBean;

import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import rx.Observable;
import rx.functions.Func1;

/**
 * Description : 注册接口管理类 Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class RegisterServiceManager extends BaseServiceManager {

    /**
     * 创建用户
     * @param userInfo  用户信息
     * @return Observable<RegisterResultBean>
     */
    public static Observable<RegisterResultBean> createUserInfo(UserInfo userInfo) {
        if(userInfo == null){
            return Observable.empty();
        }
        return mManager.create(RegisterService.class).createUserInfo(userInfo)
                .flatMap(new Func1<ResponseBody, Observable<RegisterResultBean>>() {
                    @Override
                    public Observable<RegisterResultBean> call(ResponseBody responseBody) {
                        return toObservable(responseBody,
                                RegisterResultBean.class);
                    }
                });
    }

    /**
     * 登陆应用
     * @param userName    用户名称
     * @param userSource  用户登陆平台
     * @param userSourceId 平台id
     * @return Observable<Object>
     */
    public static Observable<Object> login(String userName, String userSource,
            String userSourceId) {
        Map<String, String> map = new HashMap();
        map.put("userName", userName);
        map.put("userSource", userSource);
        map.put("userSourceId", userSourceId);

        return mManager.create(RegisterService.class).login(map).flatMap(new Func1<ResponseBody, Observable<Object>>() {
            @Override
            public Observable<Object> call(ResponseBody responseBody) {
                return RegisterServiceManager.toObservable(responseBody,
                        Object.class);
            }
        });
    }
}
