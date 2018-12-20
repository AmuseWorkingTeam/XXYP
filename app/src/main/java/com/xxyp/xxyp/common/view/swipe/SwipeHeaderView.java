
package com.xxyp.xxyp.common.view.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.xxyp.xxyp.R;

/**
 * Description : 下拉控件头部
 * Created by sunpengfei on 2017/8/23.
 * Person in charge : sunpengfei
 */
public class SwipeHeaderView extends FrameLayout implements SwipeRefreshTrigger, SwipeTrigger {

    private Context mContext;

    public SwipeHeaderView(Context context) {
        this(context, null);
    }

    public SwipeHeaderView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SwipeHeaderView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initView();
    }

    private void initView(){
        inflate(mContext, R.layout.swipe_header, this);
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onPrepare() {

    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {

    }

    @Override
    public void onRelease() {

    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onReset() {

    }
}
