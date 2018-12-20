
package com.xxyp.xxyp.message.customsviews.panel;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.xxyp.xxyp.common.utils.ScreenUtils;

/**
 * Description : 输入板操作 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class PanelContainer extends FrameLayout {

    private IPanelFactory mFactory;

    private int mHeight = ScreenUtils.dp2px(200);

    public PanelContainer(@NonNull Context context) {
        this(context, null);
    }

    public PanelContainer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PanelContainer(@NonNull Context context, @Nullable AttributeSet attrs,
            @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setLayoutParams(
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, mHeight));
        setVisibility(View.GONE);
        mFactory = new PanelFactoryImp((Activity)getContext());
    }

    private void showPanel() {
        if (!isShown()) {
            setVisibility(VISIBLE);
        }
    }

    public void hidePanel() {
        if (isShown()) {
            setVisibility(GONE);
        }
    }

    public void showPanel(int type, OnPanelItemListener itemListener) {
        removeAllViews();
        IPanel panel = mFactory.obtainPanel(type);
        if (panel != null) {
            addView(panel.obtainView(itemListener));
        }
        showPanel();
    }
}
