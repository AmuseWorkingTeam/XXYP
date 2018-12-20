
package com.xxyp.xxyp.message.customsviews;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;

/**
 * Description : 聊天页面父布局 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class ChatViewGroup extends SwipeToLoadLayout {

    private InterceptTouchListener mListener;

    public ChatViewGroup(Context context) {
        this(context, null);
    }

    public ChatViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChatViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setRefreshEnabled(true);
        setLoadingMore(false);
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mListener != null && mListener.setInterceptTouchListener(ev)) {
            return true;
        }
        return super.onInterceptTouchEvent(ev);
    }

    public void setInterceptTouchListener(InterceptTouchListener listener) {
        mListener = listener;
    }

    public interface InterceptTouchListener {
        boolean setInterceptTouchListener(MotionEvent ev);
    }
}
