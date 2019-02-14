
package com.xxyp.xxyp.common.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;

import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;

/**
 * Description : 图片配置常量
 */
public class ImageConfig {

    // 图片载入时间
    private int fadeDuration = 300;

    // 加载失败图
    private Drawable imageOnLoadFail;

    private int imageResOnLoadFail;

    // 加载过程图
    private Drawable imageOnLoading;

    private int imageResOnLoading;

    // 加载进度图
    private Drawable imageProgress;

    private int imageResProgress;

    // 重新加载图
    private Drawable imageRetry;

    private int imageResRetry;

    private boolean isProgressiveRender = false;

    private boolean isRotate = false;

    private GenericDraweeHierarchy mHierarchy;

    private ImageConfig(Context context, Builder builder) {
        imageResOnLoading = builder.imageResOnLoading;
        imageOnLoading = builder.imageOnLoading;
        imageResOnLoadFail = builder.imageResOnLoadFail;
        imageOnLoadFail = builder.imageOnLoadFail;
        imageResRetry = builder.imageResRetry;
        imageRetry = builder.imageRetry;
        imageResProgress = builder.imageResProgress;
        imageProgress = builder.imageProgress;
        fadeDuration = builder.fadeDuration;
        isProgressiveRender = builder.isProgressiveRender;
        isRotate = builder.isRotate;
        setFrescoConfig(context);
    }

    private void setFrescoConfig(Context context) {
        mHierarchy = GenericDraweeHierarchyBuilder.newInstance(context.getResources())
                .setFadeDuration(fadeDuration).setPlaceholderImage(imageResOnLoading)
                .setPlaceholderImage(imageOnLoading).setFailureImage(imageResOnLoadFail)
                .setFailureImage(imageOnLoadFail).setRetryImage(imageResRetry)
                .setRetryImage(imageRetry).setProgressBarImage(imageResProgress)
                .setProgressBarImage(imageProgress).build();
    }

    public GenericDraweeHierarchy getHierarchy() {
        return mHierarchy;
    }

    public static class Builder {

        private int fadeDuration = 300;

        private Drawable imageOnLoadFail;

        private Drawable imageOnLoading;

        private Drawable imageProgress;

        private int imageResOnLoadFail;

        private int imageResOnLoading;

        private int imageResProgress;

        private int imageResRetry;

        private Drawable imageRetry;

        private boolean isProgressiveRender = false;

        private boolean isRotate = false;

        public ImageConfig build(Context context) {
            return new ImageConfig(context, this);
        }

        public Builder setFadeDuration(int fadeDuration) {
            this.fadeDuration = fadeDuration;
            return this;
        }

        public Builder setRender(boolean isProgressiveRender) {
            this.isProgressiveRender = isProgressiveRender;
            return this;
        }

        public Builder setRotate(boolean isRotate) {
            this.isRotate = isRotate;
            return this;
        }

        public Builder showLoadFail(@DrawableRes int imageResOnLoadFail) {
            this.imageResOnLoadFail = imageResOnLoadFail;
            return this;
        }

        public Builder showLoadFail(Drawable imageOnLoadFail) {
            this.imageOnLoadFail = imageOnLoadFail;
            return this;
        }

        public Builder showLoading(@DrawableRes int imageResOnLoading) {
            this.imageResOnLoading = imageResOnLoading;
            return this;
        }

        public Builder showLoading(Drawable imageOnLoading) {
            this.imageOnLoading = imageOnLoading;
            return this;
        }

        public Builder showProgress(@DrawableRes int imageResProgress) {
            this.imageResProgress = imageResProgress;
            return this;
        }

        public Builder showProgress(Drawable imageProgress) {
            this.imageProgress = imageProgress;
            return this;
        }

        public Builder showRetry(@DrawableRes int imageResRetry) {
            this.imageResRetry = imageResRetry;
            return this;
        }

        public Builder showRetry(Drawable imageRetry) {
            this.imageRetry = imageRetry;
            return this;
        }
    }
}
