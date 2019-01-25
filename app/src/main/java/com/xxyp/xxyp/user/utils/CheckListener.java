package com.xxyp.xxyp.user.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;


/**
 * 一般的checkListener，用于定义方法和常量
 */
public abstract class CheckListener {

    protected Context mContext;

    public CheckListener(Activity context) {
        mContext = context;
    }

    public abstract void handleCheck(View parentView, TextView showView,
                                     IWheelDataChangeCallback iWheelDataChangeCallback, String... valueDatas);
}
