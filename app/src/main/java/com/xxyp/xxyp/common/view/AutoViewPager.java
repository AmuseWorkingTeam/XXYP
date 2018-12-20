
package com.xxyp.xxyp.common.view;

import android.content.Context;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Description : 自动滚动的viewPager Created by sunpengfei on 2017/7/29. Person in
 * charge : sunpengfei
 */
public class AutoViewPager extends ViewPager {

    private static final int AUTO_PLAY_DURATION = 3000;

    @SuppressWarnings("unchecked")
    private WeakHandler<AutoViewPager> handler = new WeakHandler(this) {

        @Override
        protected void handlerWeakMessage(Message msg) {
            super.handlerWeakMessage(msg);
            int what = msg.what;
            if(what == 1){
                int position = getCurrentItem();
                if (position == getAdapter().getCount() - 1) {
                    position = 0;
                } else {
                    position++;
                }
                setCurrentItem(position, true);
                handler.sendEmptyMessageDelayed(1, AUTO_PLAY_DURATION);
            }
        }
    };

    public AutoViewPager(Context context) {
        super(context);
    }

    public AutoViewPager(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                start();
                break;
            case MotionEvent.ACTION_DOWN:
                handler.removeCallbacksAndMessages(null);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        start();
    }

    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    public void setAdapter(PagerAdapter adapter) {
        if (adapter != null && adapter.getCount() > 1) {
            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(1, AUTO_PLAY_DURATION + 200);
        }
        super.setAdapter(adapter);
    }

    public void setCurrentItem(int item) {
        if (getAdapter() != null && getAdapter().getCount() > 1) {
            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(1, AUTO_PLAY_DURATION + 200);
        }
        super.setCurrentItem(item);
    }

    public void start() {
        if (getAdapter() != null && getAdapter().getCount() > 1) {
            handler.removeCallbacksAndMessages(null);
            handler.sendEmptyMessageDelayed(1, AUTO_PLAY_DURATION);
        }
    }

    public void stop() {
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}
