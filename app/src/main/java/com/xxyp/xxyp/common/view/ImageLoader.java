
package com.xxyp.xxyp.common.view;

import android.support.annotation.DrawableRes;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;

/**
 * Description : 图片展示工具 Created by sunpengfei on 2017/7/29. Person in charge :
 * sunpengfei
 */
public class ImageLoader {
    private static ImageLoader mInstance;

    public static ImageLoader getInstance() {
        if (mInstance == null) {
            synchronized (ImageLoader.class) {
                if (mInstance == null) {
                    mInstance = new ImageLoader();
                }
            }
        }
        return mInstance;
    }

    public void display(SimpleDraweeView imageView, @DrawableRes int resId) {
        ImageRequestConfig config = new ImageRequestConfig.Builder().build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(config.getImageRequest(resId))
                .setOldController(imageView.getController()).build();
        imageView.setController(controller);
    }

    public void display(SimpleDraweeView imageView, @DrawableRes int resId,
            BaseControllerListener<ImageInfo> controllerListener) {
        ImageRequestConfig config = new ImageRequestConfig.Builder().build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(config.getImageRequest(resId))
                .setControllerListener(controllerListener)
                .setOldController(imageView.getController()).build();
        imageView.setController(controller);
    }

    public void display(SimpleDraweeView imageView, @DrawableRes int resId,
            ImageRequestConfig config) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(config.getImageRequest(resId))
                .setOldController(imageView.getController()).build();
        imageView.setController(controller);
    }

    public void display(SimpleDraweeView imageView, @DrawableRes int resId,
            ImageRequestConfig config, BaseControllerListener<ImageInfo> controllerListener) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(config.getImageRequest(resId))
                .setControllerListener(controllerListener)
                .setOldController(imageView.getController()).build();
        imageView.setController(controller);
    }

    public void display(SimpleDraweeView imageView, String uri) {
        ImageRequestConfig config = new ImageRequestConfig.Builder().build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(config.getImageRequest(uri))
                .setOldController(imageView.getController()).build();
        imageView.setController(controller);
    }

    public void display(SimpleDraweeView imageView, String uri,
            BaseControllerListener<ImageInfo> controllerListener) {
        ImageRequestConfig config = new ImageRequestConfig.Builder().build();
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(config.getImageRequest(uri))
                .setControllerListener(controllerListener)
                .setOldController(imageView.getController()).build();
        imageView.setController(controller);
    }

    public void display(SimpleDraweeView imageView, String uri, ImageRequestConfig config) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(config.getImageRequest(uri))
                .setOldController(imageView.getController()).build();
        imageView.setController(controller);
    }

    public void display(SimpleDraweeView imageView, String uri, ImageRequestConfig config,
            BaseControllerListener<ImageInfo> controllerListener) {
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setImageRequest(config.getImageRequest(uri))
                .setControllerListener(controllerListener)
                .setOldController(imageView.getController()).build();
        imageView.setController(controller);
    }
}
