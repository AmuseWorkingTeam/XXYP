
package com.xxyp.xxyp.message.utils;

import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.text.TextUtils;

import com.xxyp.xxyp.common.utils.compress.LuBan;
import com.xxyp.xxyp.message.bean.ChatMessageBean;

import java.io.File;
import java.io.IOException;

import rx.Observable;

/**
 * Description : 聊天工具类
 * Created by sunpengfei on 2017/8/25.
 * Person in charge : sunpengfei
 */
public class ChatUtils {

    private static ChatUtils mInstance;

    private ChatUtils() {
    }

    public static ChatUtils getInstance() {
        if (mInstance == null) {
            mInstance = new ChatUtils();
        }
        return mInstance;
    }

    /**
     * 压缩图片处理
     *
     * @param bean 消息体
     */
    public Observable<File> setImgChatInfo(final ChatMessageBean bean) {
        if (bean == null || bean.getImageBean() == null) {
            return Observable.empty();
        }
        if (!TextUtils.isEmpty(bean.getImageBean().getBigImagePath())) {
            // 本地压缩过的大图存在 不需要压缩直接返回
            return Observable.just(new File(bean.getImageBean().getBigImagePath()));
        } else if (!TextUtils.isEmpty(bean.getImageBean().getLocalImagePath())) {
            // 本地图片原始路径存在
            // 大长图 原始图片小于200k不压缩 直接返回
            int[] imgSize = getImageSize(bean.getImageBean().getLocalImagePath());
            File localPath = new File(bean.getImageBean().getLocalImagePath());
            // 是否是大长图 宽高比大于3 可以判定为大长图
            boolean isBigLong = Math.max(imgSize[0], imgSize[1])
                    / Math.min(imgSize[0], imgSize[1]) > 3;
            boolean notCompress = localPath.exists() && localPath.length() <= 200 * 1024
                    || isBigLong;
            if (notCompress) {
                return Observable.just(new File(bean.getImageBean().getLocalImagePath()));
            } else {
                LuBan luBan = LuBan.getInstance().compressByPath(bean.getImageBean().getLocalImagePath())
                        .putGear(LuBan.THIRD_GEAR);
                if (luBan != null) {
                    return luBan.asObservable();
                }
            }
        }
        return Observable.empty();

    }

    /**
     * 获取图片的宽高
     *
     * @param imagePath 图片路径
     * @return int[]
     */
    public int[] getImageSize(String imagePath) {
        int[] res = new int[2];

        if (TextUtils.isEmpty(imagePath)) {
            return res;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(imagePath, options);

        int degree = getImageSpinAngle(imagePath);
        if (degree == 90 || degree == 270) {
            // 反转180
            res[0] = options.outHeight;
            res[1] = options.outWidth;
            return res;
        }
        res[0] = options.outWidth;
        res[1] = options.outHeight;

        return res;
    }

    /**
     * 获取图片旋转角度
     * @param path  路径
     * @return int
     */
    public int getImageSpinAngle(String path) {
        int degree = 0;
        try {
            ExifInterface e = new ExifInterface(path);
            int orientation = e.getAttributeInt("Orientation", 1);
            switch(orientation) {
                case 3:
                    degree = 180;
                    break;
                case 6:
                    degree = 90;
                    break;
                case 8:
                    degree = 270;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
}
