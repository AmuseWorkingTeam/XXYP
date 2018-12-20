
package com.xxyp.xxyp.common.utils.dialog;

import android.app.DialogFragment;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.xxyp.xxyp.R;

/**
 * Description : 基础dialog Created by sunpengfei on 2017/6/8. Person in charge :
 * sunpengfei
 */
public abstract class BaseDialog extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        init();
        View view = onCreateDialogView();
        if (container != null) {
            container.addView(container);
        }
        return view;
    }

    /**
     * 初始化dialog
     */
    private void init() {
        setStyle(R.style.dialog_normal, 0);
        if (getDialog() != null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    /**
     * 创建dialogView
     * 
     * @return View
     */
    protected abstract View onCreateDialogView();
}
