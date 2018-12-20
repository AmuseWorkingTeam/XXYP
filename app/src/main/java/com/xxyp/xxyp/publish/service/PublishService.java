
package com.xxyp.xxyp.publish.service;

import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.main.bean.WorkBean;

import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;
import rx.Observable;

/**
 * Description : 发布接口 Created by sunpengfei on 2017/8/2. Person in charge :
 * sunpengfei
 */
public interface PublishService {

    String CREATE_WORKS = "createWorks";

    /**
     * 发布作品
     * @param workBean  作品信息
     * @return Observable<ResponseBody>
     */
    @POST(CREATE_WORKS)
    Observable<ResponseBody> createWorks(@Body WorkBean workBean);

    String CREATE_DATING_SHOT = "createDatingShot";

    /**
     * 创建约拍
     * @param shotBean 约拍信息
     * @return Observable<ResponseBody>
     */
    @POST(CREATE_DATING_SHOT)
    Observable<ResponseBody> createDatingShot(@Body ShotBean shotBean);
}
