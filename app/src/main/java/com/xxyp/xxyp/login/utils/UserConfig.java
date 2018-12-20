
package com.xxyp.xxyp.login.utils;

/**
 * Description : 用户常量 Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public interface UserConfig {

    //用户信息
    String USER_INFO = "userInfo";

    //查看粉丝关注类型
    String USER_FANS_TYPE = "userFansType";

    /**
     * 登陆平台类型
     */
    interface LoginPlatform {

        //微信
        int WECHAT = 1;

        //微博
        int SINA = 2;

        //qq
        int QQ = 3;
    }

    /**
     * 用户类型
     */
    interface UserType {

        //约拍者
        int CUSTOMER = 1;

        //摄影师
        int CAMERAMER = 2;
    }

    /**
     * 查看粉丝 关注类型
     */
    interface UserFansType {

        //查看粉丝
        int FANS_TYPE = 1;

        //查看关注
        int FOCUS_TYPE = 2;
    }
}
