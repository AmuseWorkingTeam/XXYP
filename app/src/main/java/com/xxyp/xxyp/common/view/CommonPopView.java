package com.xxyp.xxyp.common.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

/**
 * Description : 公共popWindow
 * Created by sunpengfei on 2017/8/14.
 * Person in charge : sunpengfei
 */
public class CommonPopView extends PopupWindow {

    private View mContainer;

    private Context mContext;

    public CommonPopView(Context context, View view){
        mContext = context;
        mContainer = view;
        init();
    }

    private void init(){
        mContainer.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int height = mContainer.getTop();
                int y = (int)event.getY();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (height < y) {
                        dismiss();
                    }
                }
                return true;
            }
        });

        ColorDrawable dw = new ColorDrawable(0x50000000);
        setBackgroundDrawable(dw);
        setContentView(mContainer);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }
}
