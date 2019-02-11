/*******************************************************************************
 * Copyright 2011, 2012 Chris Banes.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.xxyp.xxyp.map.pulltorefresh;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.map.pulltorefresh.PullToRefreshBase.Mode;

/**
 * Description : 滑动刷新列表头部刷新布局实现
 * Created by lx on 2016/6/21
 * Job number：137289
 * Phone ：        18611867932
 * Email：          lixiao3@syswin.com
 * Person in charge : lx
 * Leader：lx
 */
public class FlipLoadingLayout extends LoadingLayout {

    static final int FLIP_ANIMATION_DURATION = 150;

    private final Animation mRotateAnimation, mResetRotateAnimation,
            mCycleAnimation;

    public FlipLoadingLayout(Context context, final Mode mode) {
        super(context, mode);

        final int rotateAngle = mode == Mode.PULL_FROM_START ? -180 : 180;

        mRotateAnimation = new RotateAnimation(0, rotateAngle,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
        mRotateAnimation.setFillAfter(true);

        mResetRotateAnimation = new RotateAnimation(rotateAngle, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mResetRotateAnimation.setInterpolator(ANIMATION_INTERPOLATOR);
        mResetRotateAnimation.setDuration(FLIP_ANIMATION_DURATION);
        mResetRotateAnimation.setFillAfter(true);

        mCycleAnimation = AnimationUtils.loadAnimation(getContext(),
                R.anim.pull_refresh_rotate);
    }

    @Override
    protected void onLoadingDrawableSet(Drawable imageDrawable) {

    }

    @Override
    protected void onPullImpl(float scaleOfLayout) {
        // NO-OP
    }

    @Override
    protected void pullToRefreshImpl() {
        // Only start reset Animation, we've previously show the rotate anim
        if (mRotateAnimation == mIconView.getAnimation()) {
            mIconView.startAnimation(mResetRotateAnimation);
        }
    }

    @Override
    protected void refreshingImpl() {
        mIconView.clearAnimation();
        mIconView.setImageResource(R.drawable.pull_refresh_arrow_up);
        mLoadingIconView.setVisibility(View.VISIBLE);
        mLoadingIconView.startAnimation(mCycleAnimation);

    }

    @Override
    protected void releaseToRefreshImpl() {
        mIconView.startAnimation(mRotateAnimation);
        // mLoadingIconView.startAnimation(mRotateAnimation);
    }

    @Override
    protected void resetImpl() {
        mIconView.setImageResource(R.drawable.pull_refresh_arrow_down);
        mIconView.clearAnimation();
        mLoadingIconView.clearAnimation();
        mLoadingIconView.setVisibility(View.INVISIBLE);
        // mHeaderProgress.setVisibility(View.GONE);
        // mHeaderImage.setVisibility(View.VISIBLE);
    }

    @Override
    protected int getDefaultDrawableResId() {
        return R.drawable.pull_refresh_arrow_up;
    }

}
