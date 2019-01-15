
package com.xxyp.xxyp.user.service;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.user.bean.CreateFansInput;
import com.xxyp.xxyp.user.bean.LogoutInput;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Description : 用户接口 Created by sunpengfei on 2017/8/2. Person in charge :
 * sunpengfei
 */

public interface UserService {

    String GET_FANS_AND_FOLLOW_COUNT = "getUserFansAndFollowCounts";

    /**
     * 获取点赞粉丝人数
     * @return Observable
     */
    @GET(GET_FANS_AND_FOLLOW_COUNT)
    Observable<ResponseBody> getFansCount(@Query("userId") String userId);

    String SELECT_USER_INFO = "selectUserInfo";

    /**
     * 查询用户信息
     * @param userIds  用户id列表  批量为","隔开
     * @return Observable
     */
    @GET(SELECT_USER_INFO)
    Observable<ResponseBody> selectUserInfo(@Query("userIds") String userIds);

    String GET_WORKS = "getWorks";

    /**
     * 获取作品信息
     * 
     * @param userId 用户id
     * @param workId 作品id 如果不传则获取所有
     * @return Observable
     */
    @GET(GET_WORKS)
    Observable<ResponseBody> getWorks(@Query("userId") String userId,
            @Query("worksId") String workId);


    String GET_DATING_SHOT= "getDatingShot";

    /**
     * 获取作约拍信息
     *
     * @param userId 用户id
     * @param shotId 约拍id 如果不传则获取所有
     * @return Observable
     */
    @GET(GET_DATING_SHOT)
    Observable<ResponseBody> getDatingShot(@Query("userId") String userId,
            @Query("datingShotId") String shotId);

    /**
     * 创建粉丝
     */
    String CREATE_FANS = "createFans";
    @POST(CREATE_FANS)
    Observable<ResponseBody> createFans(@Body CreateFansInput input);

    /**
     * 获取粉丝或关注
     */
    String GET_FANS = "getFans";
    @POST(GET_FANS)
    Observable<ResponseBody> getFans(@Query("fromUserId") String fromUserId,
            @Query("toUserId") String toUserId);

    /**
     * 更新userInfo
     */
    String UPDATE_USER_INFO = "updateUserInfo";
    @POST(UPDATE_USER_INFO)
    Observable<ResponseBody> updateUserInfo(@Body UserInfo userInfo);

    String LOGOUT = "logout";
    @POST(LOGOUT)
    Observable<ResponseBody> logout(@Body LogoutInput input);
}
