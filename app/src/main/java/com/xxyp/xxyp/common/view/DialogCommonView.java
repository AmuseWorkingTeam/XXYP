
package com.xxyp.xxyp.common.view;

import android.app.Activity;
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
 * Description : 公共弹出框样式 Created by sunpengfei on 2017/7/29. Person in charge :
 * sunpengfei
 */
public class DialogCommonView extends DialogFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_view, container);
        init();
        return view;
    }

    private void init() {
        setStyle(R.style.dialog_normal, 0);
        if (getDialog() != null) {
            getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (getDialog().getWindow() != null) {
                getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            }
        }
    }

    public void showDialog(Activity activity) {
        show(activity.getFragmentManager(), "DialogCommonView");
    }
}
