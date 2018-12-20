
package com.xxyp.xxyp.publish.utils;

/**
 * Description : 发布常量 Created by sunpengfei on 2017/8/2. Person in charge :
 * sunpengfei
 */
public interface PublishConfig {

    /**
     * 发布类型
     */
    String PUBLISH_TYPE = "publishType";

    /**
     * 发布类型
     */
    interface PublishType {

        // 发布约拍
        int PUBLISH_SHOT = 1;

        // 发布作品
        int PUBLISH_WORK = 2;
    }

    /**
     * 约拍目的
     */
    interface ShotPurpose {

        // 约摄影师
        String FIND_CAMERA = "1";

        // 约模特
        String FIND_MODEL = "2";
    }

    /**
     * 约拍付款方式
     */
    interface ShotPayMethod {

        // 收款
        String GATHER = "1";

        // 免费
        String FREE = "2";

        // 付款
        String PAY = "3";
    }
}
