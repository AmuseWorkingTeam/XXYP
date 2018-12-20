
package com.xxyp.xxyp.common.utils.gallery;

import android.app.Activity;
import android.content.Intent;


/**
 * Description : 图片选择provider Created by sunpengfei on 2017/8/8. Person in
 * charge : sunpengfei
 */
public class GalleryProvider {

    /**
     * 跳转选择图片
     * @param activity   上下文
     * @param maxSelected 最大选择图片数
     * @param requestCode 请求码
     */
    public static void openGalley(Activity activity, int maxSelected, int requestCode){
        Intent intent = new Intent(activity, GalleryActivity.class);
        intent.putExtra("LIMIT_PICK_PHOTO", maxSelected);
        intent.putExtra("FILTER_MIME_TYPES", new String[0]);
        intent.putExtra("HAS_CAMERA", true);
        intent.putExtra("SINGLE_PHOTO", maxSelected <= 1);
        activity.startActivityForResult(intent, requestCode);
    }
}
