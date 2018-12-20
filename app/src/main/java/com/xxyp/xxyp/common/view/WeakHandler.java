
package com.xxyp.xxyp.common.view;

import android.os.Handler;
import android.os.Message;

import java.lang.ref.WeakReference;

/**
 * Description : 软引用handler Created by sunpengfei on 2017/7/29. Person in charge
 * : sunpengfei
 */
public class WeakHandler<T> extends Handler {

    private WeakReference<T> mActivity;

    public WeakHandler(T paramT) {
        this.mActivity = new WeakReference(paramT);
    }

    @Override
    public final void handleMessage(Message msg) {
        super.handleMessage(msg);
        if(mActivity.get() == null){
            return;
        }
        handlerWeakMessage(msg);
    }

    protected void handlerWeakMessage(Message msg){
    }
}
