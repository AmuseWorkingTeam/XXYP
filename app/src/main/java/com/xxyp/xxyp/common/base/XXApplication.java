
package com.xxyp.xxyp.common.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.avos.avoscloud.AVOSCloud;
import com.facebook.cache.disk.DiskCacheConfig;
import com.facebook.common.internal.Supplier;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.backends.okhttp3.OkHttpImagePipelineConfigFactory;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.xxyp.xxyp.common.db.YuePaiDB;
import com.xxyp.xxyp.common.utils.AppContextUtils;
import com.xxyp.xxyp.common.utils.FileConfig;
import com.xxyp.xxyp.common.utils.FileUtils;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.message.service.MsgServiceManager;

import java.io.File;

import okhttp3.OkHttpClient;

/**
 * Description : 约拍application Created by sunpengfei on 2017/7/27. Person in
 * charge : sunpengfei
 */
public class XXApplication extends Application {

    private static XXApplication mInstance;

    public static XXApplication getInstance() {
        return mInstance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initShareApp();
        initUtils();
        initDB();
        initAVOSCloud();
        initFresco();
    }

    /**
     * 初始化第三方登陆 分享
     */
    private void initShareApp() {
        UMShareConfig config = new UMShareConfig();
        //在有效期内不会重复授权
        config.isNeedAuthOnGetUserInfo(true);
        //无论是否安装微博 都会唤起网页
        config.setSinaAuthType(UMShareConfig.AUTH_TYPE_SSO);
        UMShareAPI.get(this).setShareConfig(config);
        Config.DEBUG = true;
        PlatformConfig.setWeixin("wx1f1f6e78702ea72f", "965d53410c4995c84c3988897aaeb7c6");
        PlatformConfig.setQQZone("1106334456", "CPxycjEHDiTgXQJ6");
        PlatformConfig.setSinaWeibo("2782731568", "6a7bb7616dcd2258a8251b0fd11338ae",
                "http://sns.whalecloud.com");

    }

    /**
     * 初始化工具类
     */
    private void initUtils() {
        AppContextUtils.initApp(getApplicationContext());
//        Stetho.initializeWithDefaults(this);
        //创建文件夹
//        FileUtils.getFileByPath(FileConfig.DIR_APP_NAME);
    }

    /**
     * 创建数据库
     */
    public void initDB() {
        String str = SharePreferenceUtils.getInstance().getUserId();
        if (!TextUtils.isEmpty(str)) {
            YuePaiDB.getInstance().create(str);
        }
    }

    /**
     * 初始化leanCloud
     */
    public void initAVOSCloud() {
        AVOSCloud.initialize(this, "7JDvG8vrd5xwwxaaiVnOTkRh-gzGzoHsz", "1tWEe6zXISpvTwrlhdtHCfgq");
        AVOSCloud.setDebugLogEnabled(true);
        MsgServiceManager.getInstance()
                .startMsgService(SharePreferenceUtils.getInstance().getUserId());
    }

    /**
     * 初始化fresco图片查看
     */
    @SuppressWarnings("unchecked")
    private void initFresco() {
        //设置图片缓存目录
        DiskCacheConfig diskCacheConfig = DiskCacheConfig.newBuilder(this)
                .setBaseDirectoryPath(FileUtils.getFileByPath(FileConfig.DIR_APP_CACHE))
                .setBaseDirectoryPathSupplier(new Supplier() {
                    public File get() {
                        return getCacheDir();
                    }
                }).build();
        //设置网络调用为okHttp
        Fresco.initialize(this,
                OkHttpImagePipelineConfigFactory.newBuilder(this, new OkHttpClient())
                        .setMainDiskCacheConfig(diskCacheConfig).setDownsampleEnabled(true)
                        .build());
    }

}
