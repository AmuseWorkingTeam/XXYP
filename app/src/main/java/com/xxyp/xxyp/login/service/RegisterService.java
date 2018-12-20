
package com.xxyp.xxyp.login.service;

import com.xxyp.xxyp.common.bean.UserInfo;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Description : 注册service Created by sunpengfei on 2017/7/31. Person in charge
 * : sunpengfei
 */
public interface RegisterService {

    String CREATE_USER_INFO = "createUserInfo";

    /**
     * 创建用户
     * @param userInfo  用户信息
     * @return Observable<ResponseBody>
     */
    @POST(CREATE_USER_INFO)
    Observable<ResponseBody> createUserInfo(@Body UserInfo userInfo);

    String LOGIN = "login";

    /**
     * 登陆
     * @param userMap  用户信息
     * @return Observable<ResponseBody
     */
    @FormUrlEncoded
    @POST(LOGIN)
    Observable<ResponseBody> login(@FieldMap Map<String, String> userMap);
}
