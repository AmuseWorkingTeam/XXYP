
package com.xxyp.xxyp.find.provider;

import android.app.Activity;
import android.content.Intent;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.bean.PhotoViewBean;
import com.xxyp.xxyp.common.bean.PhotoViewListBean;
import com.xxyp.xxyp.find.utils.FindConfig;
import com.xxyp.xxyp.find.view.PhotoDetailActivity;
import com.xxyp.xxyp.find.view.ProductDetailActivity;
import com.xxyp.xxyp.find.view.ShotDetailActivity;

import java.util.List;

/**
 * Description : 发现provider
 * Created by sunpengfei on 2017/8/9.
 * Person in charge : sunpengfei
 */
public class FindProvider {

    /**
     * 跳转作品详情页
     * @param activity  上下文
     * @param userId    用户id
     * @param workId    作品id
     */
    public static void openProduct(Activity activity, String userId, String workId){
        Intent intent = new Intent(activity, ProductDetailActivity.class);
        intent.putExtra(FindConfig.USER_ID, userId);
        intent.putExtra(FindConfig.WORK_ID, workId);
        activity.startActivity(intent);
    }

    /**
     * 跳转约拍详情页
     * @param activity  上下文
     * @param userId    用户id
     * @param shotId    约拍id
     */
    public static void openShot(Activity activity, String userId, String shotId){
        Intent intent = new Intent(activity, ShotDetailActivity.class);
        intent.putExtra(FindConfig.USER_ID, userId);
        intent.putExtra(FindConfig.SHOT_ID, shotId);
        activity.startActivity(intent);
    }

    /**
     * 打开约拍或作品图片查看
     *  @param activity 上下文
     * @param index 索引位置
     * @param userId 用户id
     * @param photoViewBeans 图片数据
     */
    public static void openPhotoDetail(Activity activity, int index, String userId,
            List<PhotoViewBean> photoViewBeans) {
        PhotoViewListBean photoViewListBean = new PhotoViewListBean();
        photoViewListBean.setPhotoViewBeans(photoViewBeans);
        Intent intent = new Intent(activity, PhotoDetailActivity.class);
        intent.putExtra(PhotoDetailActivity.PHOTO_INDEX, index);
        intent.putExtra(PhotoDetailActivity.PHOTO_DATA, photoViewListBean);
        intent.putExtra(FindConfig.USER_ID, userId);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.popwindow_alpha_in, 0);
    }
}
