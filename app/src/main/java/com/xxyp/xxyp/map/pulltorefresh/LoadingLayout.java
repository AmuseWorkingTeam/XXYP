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
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.map.pulltorefresh.PullToRefreshBase.Mode;

/**
 * Description : 头顶部布局抽象类
 */
public abstract class LoadingLayout extends FrameLayout implements
		ILoadingLayout {

	static final String LOG_TAG = "PullToRefresh-LoadingLayout";

	static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();

	private View mInnerLayout;
	 protected final ImageView mIconView;
	protected final ImageView mLoadingIconView;

	private boolean mUseIntrinsicAnimation;

	private final TextView mHeaderText;

	protected final Mode mMode;

	private CharSequence mPullLabel;
	private CharSequence mRefreshingLabel;
	private CharSequence mReleaseLabel;

	public LoadingLayout(Context context, final Mode mode) {
		super(context);
		mMode = mode;
		LayoutInflater.from(context).inflate(R.layout.map_pull_to_refresh_header,
				this);
		mInnerLayout = findViewById(R.id.fl_inner);
		mHeaderText = (TextView) mInnerLayout
				.findViewById(R.id.pull_to_refresh_text);
		mLoadingIconView = (ImageView) mInnerLayout
				.findViewById(R.id.pull_to_refresh_progress);
		mIconView = (ImageView) mInnerLayout
		 .findViewById(R.id.pull_to_refresh_image);

		LayoutParams lp = (LayoutParams) mInnerLayout
				.getLayoutParams();

		switch (mode) {
		case PULL_FROM_END:
			lp.gravity = Gravity.TOP;
			mPullLabel = context
					.getString(R.string.pull_to_refresh_from_bottom_pull_label);
			mRefreshingLabel = context
					.getString(R.string.pull_to_refresh_from_bottom_refreshing_label);
			mReleaseLabel = context
					.getString(R.string.pull_to_refresh_from_bottom_release_label);
			break;

		case PULL_FROM_START:
		default:
			lp.gravity = Gravity.BOTTOM;
			mPullLabel = context.getString(R.string.pull_to_refresh_pull_label);
			mRefreshingLabel = context
					.getString(R.string.pull_to_refresh_refreshing_label);
			mReleaseLabel = context
					.getString(R.string.pull_to_refresh_release_label);
			break;
		}

		Drawable db = getResources().getDrawable(getDefaultDrawableResId());
		setLoadingDrawable(db);

		reset();
	}

	public final void setHeight(int height) {
		ViewGroup.LayoutParams lp = getLayoutParams();
		lp.height = height;
		requestLayout();
	}

	public final void setWidth(int width) {
		ViewGroup.LayoutParams lp = getLayoutParams();
		lp.width = width;
		requestLayout();
	}

	public final int getContentSize() {
		return mInnerLayout.getHeight();
	}

	public final void onPull(float scaleOfLayout) {
		if (!mUseIntrinsicAnimation) {
			onPullImpl(scaleOfLayout);
		}
	}

	public final void pullToRefresh() {
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
		}

		// Now call the callback
		pullToRefreshImpl();
	}

	public final void refreshing() {
		if (null != mHeaderText) {
			mHeaderText.setText(mRefreshingLabel);
		}

		if (mUseIntrinsicAnimation) {
			// ((AnimationDrawable) mHeaderImage.getDrawable()).start();
			XXLog.log_w(LoadingLayout.class.getSimpleName(),"refreshing");
		} else {
			// Now call the callback
			refreshingImpl();
		}

	}

	public final void releaseToRefresh() {
		if (null != mHeaderText) {
			mHeaderText.setText(mReleaseLabel);
		}

		// Now call the callback
		releaseToRefreshImpl();
	}

	public final void reset() {
		if (null != mHeaderText) {
			mHeaderText.setText(mPullLabel);
		}
		// mHeaderImage.setVisibility(View.VISIBLE);

		if (mUseIntrinsicAnimation) {
			// ((AnimationDrawable) mHeaderImage.getDrawable()).stop();
			XXLog.log_w(LoadingLayout.class.getSimpleName(),"reset");
		} else {
			// Now call the callback
			resetImpl();
		}

	}

	public final void setLoadingDrawable(Drawable imageDrawable) {
		// Set Drawable
		// mHeaderImage.setImageDrawable(imageDrawable);
		mUseIntrinsicAnimation = (imageDrawable instanceof AnimationDrawable);

		// Now call the callback
		onLoadingDrawableSet(imageDrawable);
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {

	}

	public void setPullLabel(CharSequence pullLabel) {
		mPullLabel = pullLabel;
	}

	public void setRefreshingLabel(CharSequence refreshingLabel) {
		mRefreshingLabel = refreshingLabel;
	}

	public void setReleaseLabel(CharSequence releaseLabel) {
		mReleaseLabel = releaseLabel;
	}

	/**
	 * Callbacks for derivative Layouts
	 */

	protected abstract int getDefaultDrawableResId();

	protected abstract void onLoadingDrawableSet(Drawable imageDrawable);

	protected abstract void onPullImpl(float scaleOfLayout);

	protected abstract void pullToRefreshImpl();

	protected abstract void refreshingImpl();

	protected abstract void releaseToRefreshImpl();

	protected abstract void resetImpl();

}
