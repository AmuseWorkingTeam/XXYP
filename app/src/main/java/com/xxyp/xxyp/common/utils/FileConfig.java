
package com.xxyp.xxyp.common.utils;

import android.os.Environment;

/**
 * Description : 文件常量 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */

public class FileConfig {

    //应用文件名
    private static String APP_DIR_NAME = "xxyp";

    //应用文件绝对路径
    public static String DIR_APP_NAME = Environment.getExternalStorageDirectory().getAbsolutePath()
            + "/" + APP_DIR_NAME;

    //应用缓存文件绝对路径
    public static String DIR_APP_CACHE = DIR_APP_NAME + "/cache";

    //应用相册绝对路径
    public static String DIR_APP_CACHE_CAMERA = DIR_APP_NAME + "/camera";

    //应用音频绝对路径
    public static String DIR_APP_CACHE_VOICE = DIR_APP_NAME + "/voice";

    //应用log绝对路径
    public static String DIR_APP_CRASH_LOG = DIR_APP_NAME + "/log";

    //应用下载绝对路径
    public static String DIR_APP_DOWNLOAD = DIR_APP_NAME + "/download";
}

