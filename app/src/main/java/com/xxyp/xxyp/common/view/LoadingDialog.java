
package com.xxyp.xxyp.common.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.xxyp.xxyp.R;

/**
 * Description : 加载dialog Created by sunpengfei on 2017/7/29. Person in charge :
 * sunpengfei
 */
public class LoadingDialog extends Dialog {

    private Context mContext;

    private ProgressBar mProgressBar;

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.dialog_normal);
        init(context);
    }

    /**
     * 初始化view
     * @param context 上下文
     */
    private void init(Context context) {
        mContext = context;
        setContentView(R.layout.loading);
        setProperty();
        setCancelable(false);
        mProgressBar = ((ProgressBar)findViewById(R.id.loading_progressBar));
    }

    private void setProperty() {
        Window window = getWindow();
        if (window == null) {
            return;
        }
        WindowManager.LayoutParams params = window.getAttributes();
        Display display = window.getWindowManager().getDefaultDisplay();
        params.height = display.getHeight();
        params.width = display.getWidth();
        window.setAttributes(params);
    }
}
