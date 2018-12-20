
package com.xxyp.xxyp.common.utils;

import android.net.Uri;
import android.support.annotation.DrawableRes;
import android.text.TextUtils;

import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Description : 图片配置常量 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public class ImageRequestConfig {

    private int width;

    private int height;

    private boolean isProgressiveRender = false;

    private boolean isRotate = true;

    private ResizeOptions mResizeOptions;

    private BasePostprocessor mPostprocessor;

    private ImageRequestConfig(Builder builder) {
        width = builder.width;
        height = builder.height;
        isProgressiveRender = builder.isProgressiveRender;
        isRotate = builder.isRotate;
        mPostprocessor = builder.mPostprocessor;
        if (width != 0 && height != 0) {
            mResizeOptions = new ResizeOptions(width, height);
        }
    }

    private ImageRequest setImageConfig(ImageRequestBuilder imageRequestBuilder) {
        return imageRequestBuilder
                //TODO
//                .setResizeOptions(mResizeOptions)
                .setAutoRotateEnabled(isRotate)
                .setPostprocessor(mPostprocessor)
                .setProgressiveRenderingEnabled(isProgressiveRender).build();
    }

    public ImageRequest getImageRequest(@DrawableRes int resId) {
        return setImageConfig(ImageRequestBuilder.newBuilderWithResourceId(resId));
    }

    public ImageRequest getImageRequest(String uri) {
        if (TextUtils.isEmpty(uri)) {
            return null;
        }
        return setImageConfig(ImageRequestBuilder.newBuilderWithSource(Uri.parse(uri)));
    }

    public static class Builder {

        private int width;

        private int height;

        private boolean isProgressiveRender = false;

        private boolean isRotate = true;

        private BasePostprocessor mPostprocessor;

        public ImageRequestConfig build() {
            return new ImageRequestConfig(this);
        }

        public Builder setLoadHeight(int height) {
            this.height = height;
            return this;
        }

        public Builder setLoadWidth(int width) {
            this.width = width;
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

        public Builder setPostprocessor(BasePostprocessor postprocessor){
            this.mPostprocessor = postprocessor;
            return this;
        }
    }
}
