
package com.xxyp.xxyp.common.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ScreenUtils;

/**
 * Description : 标题栏header Created by sunpengfei on 2017/7/29. Person in charge
 * : sunpengfei
 */
public class Header {

    private HeaderController mController;

    protected Header(HeaderController paramHeaderController) {
        this.mController = paramHeaderController;
    }

    public ImageView getBackIcon() {
        return this.mController.mBackIcon;
    }

    public TextView getBackText() {
        return this.mController.mBackTv;
    }

    public ImageView getRightIcon() {
        return this.mController.mRightIcon;
    }

    public TextView getRightText() {
        return this.mController.mRightTv;
    }

    public TextView getTitleText() {
        return this.mController.mTitleView;
    }

    public RelativeLayout getView() {
        return this.mController.mContainer;
    }

    public void setRightIcon(int resId) {
        ImageView rightIcon = this.mController.mRightIcon;
        if (rightIcon != null) {
            if (resId > 0) {
                rightIcon.setImageResource(resId);
            }else{
                rightIcon.setVisibility(View.GONE);
            }
        }
    }

    public void setRightIcon(Bitmap bitmap) {
        if (bitmap != null) {
            this.mController.mRightIcon.setImageBitmap(bitmap);
        }
    }

    public void setRightIcon(Drawable drawable) {
        ImageView rightIcon = this.mController.mRightIcon;
        if (drawable != null) {
            rightIcon.setImageDrawable(drawable);
        }
    }

    public void setTitle(int resId) {
        if (this.mController.mTitleView != null) {
            this.mController.mTitleView.setText(resId);
        }
    }

    public void setTitle(CharSequence title) {
        if (mController.mTitleView != null) {
            mController.mTitleView.setText(title);
        }
    }


    public void showHeader() {
        this.mController.mContainer.setVisibility(View.VISIBLE);
    }

    public void hideHeader() {
        this.mController.mContainer.setVisibility(View.GONE);
    }

    public static class Builder {

        public Context mContext;

        Header.HeaderController pHc;

        public Builder(Context context, RelativeLayout container) {
            mContext = context;
            pHc = new HeaderController(context);
            pHc.mContainer = container;
        }

        public Header build() {
            Header header = new Header(pHc);
            pHc.apply();
            return header;
        }

        public Builder setBackIcon(int resId, View.OnClickListener listener) {
            ImageView backIcon = new ImageView(mContext);
            backIcon.setImageResource(resId);
            backIcon.setOnClickListener(listener);
            pHc.mBackIcon = backIcon;
            return this;
        }

        public Builder setBackText(int resId, View.OnClickListener listener) {
            return setBackText(this.mContext.getResources().getString(resId),
                    listener);
        }

        public Builder setBackText(String title, View.OnClickListener listener) {
            TextView backText = new TextView(mContext);
            backText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            backText.setText(title);
            backText.setGravity(Gravity.CENTER);
            backText.setTextColor(mContext.getResources().getColor(R.color.c3));
            backText.setOnClickListener(listener);
            pHc.mBackTv = backText;
            return this;
        }

        public Builder setDefineView(View defineView) {
            pHc.mDefineView = defineView;
            return this;
        }

        public Builder setRightIcon(int resId, View.OnClickListener listener) {
            ImageView rightIcon = new ImageView(mContext);
            rightIcon.setImageResource(resId);
            rightIcon.setOnClickListener(listener);
            pHc.mRightIcon = rightIcon;
            return this;
        }

        public Builder setRightText(int resId) {
            return setRightText(mContext.getResources().getString(resId), null);
        }

        public Builder setRightText(int resId, View.OnClickListener listener) {
            return setRightText(mContext.getResources().getString(resId),
                    listener);
        }

        public Builder setRightText(String content) {
            return setRightText(content, null);
        }

        public Builder setRightText(String content, View.OnClickListener listener) {
            TextView rightText = new TextView(mContext);
            rightText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);
            rightText.setText(content);
            rightText.setGravity(Gravity.CENTER);
            rightText.setTextColor(mContext.getResources().getColor(R.color.c3));
            if (listener != null) {
                rightText.setOnClickListener(listener);
            }
            pHc.mRightTv = rightText;
            return this;
        }

        public Builder setTitle(int resId) {
            return setTitle(mContext.getResources().getString(resId), null);
        }

        public Builder setTitle(int resId, View.OnClickListener listener) {
            return setTitle(mContext.getResources().getString(resId), listener);
        }

        public Builder setTitle(CharSequence content) {
            return setTitle(content, null);
        }

        public Builder setTitle(CharSequence content,
                View.OnClickListener listener) {
            TextView titleView = new TextView(this.mContext);
            titleView.setText(content);
            titleView.setGravity(Gravity.CENTER);
            titleView.setSingleLine(true);
            titleView.setEllipsize(TextUtils.TruncateAt.END);
            titleView.setMaxEms(12);
            titleView.setTextColor(this.mContext.getResources().getColor(R.color.c3));
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 17);
            if (listener != null) {
                titleView.setOnClickListener(listener);
            }
            pHc.mTitleView = titleView;
            return this;
        }
    }

    private static class HeaderController {

        private ImageView mBackIcon;

        private TextView mBackTv;

        private RelativeLayout mContainer;

        private View mDefineView;

        private ImageView mRightIcon;

        private TextView mRightTv;

        private TextView mTitleView;

        private HeaderController(Context context){
            mContainer = new RelativeLayout(context);
        }

        void apply() {

            mContainer.removeAllViews();
            RelativeLayout.LayoutParams params;

            //返回按钮icon
            if (mBackIcon != null) {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                mBackIcon.setPadding(ScreenUtils.dp2px(16), 0, 0, 0);
                mContainer.addView(mBackIcon, params);
            }

            //返回文本
            if (mBackTv != null) {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
                mBackIcon.setPadding(ScreenUtils.dp2px(16), 0, 0, 0);
                mBackTv.setGravity(Gravity.CENTER);
                mContainer.addView(mBackTv, params);
            }

            //标题文本
            if (mTitleView != null) {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                mTitleView.setGravity(Gravity.CENTER);
                mContainer.addView(mTitleView, params);
            }

            //右上角icon
            if (mRightIcon != null) {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mRightIcon.setPadding(0, 0, ScreenUtils.dp2px(16), 0);
                mContainer.addView(mRightIcon, params);
            }

            //右上角文本
            if (mRightTv != null) {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.CENTER_VERTICAL);
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                mRightTv.setGravity(Gravity.CENTER);
                mRightTv.setPadding(0, 0, ScreenUtils.dp2px(16), 0);
                mContainer.addView(mRightTv, params);
            }

            //自定义view
            if (mDefineView != null) {
                params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.MATCH_PARENT);
                params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
                mContainer.addView(mDefineView, params);
            }
        }
    }
}
