
package com.xxyp.xxyp.login.provider;

import android.app.Activity;
import android.content.Intent;

import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.login.utils.UserConfig;
import com.xxyp.xxyp.login.view.RegisterActivity;

/**
 * Description : 注册provider Created by sunpengfei on 2017/7/31. Person in charge
 * : sunpengfei
 */
public class RegisterProvider {

    public static void openRegisterActivity(Activity activity, UserInfo userInfo, int requestCode) {
        Intent intent = new Intent(activity, RegisterActivity.class);
        intent.putExtra(UserConfig.USER_INFO, userInfo);
        activity.startActivityForResult(intent, requestCode);
    }
}
