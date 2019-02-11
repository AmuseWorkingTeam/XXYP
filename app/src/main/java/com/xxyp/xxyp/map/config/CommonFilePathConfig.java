package com.xxyp.xxyp.map.config;

import android.os.Environment;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public class CommonFilePathConfig {

    public static String APP_DIR_NAME;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static String DIR_APP_NAME;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static String DIR_APP_CACHE;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static String DIR_APP_LOG;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static String DIR_APP_CRASH_LOG;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static String DIR_APP_DOWNLOAD;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static String DIR_APP_CACHE_VOICE;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static final String DIR_APP_CACHE_VIDEO;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static final String DIR_APP_CACHE_FILE;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static final String DIR_APP_CACHE_VIDEO_THUMBNAIL;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static String DIR_APP_CACHE_CAMERA;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static String DIR_APP_CACHE_IMAGE_BIG;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static String DIR_APP_CACHE_IMAGE_COMPRESS;
    public static String DOWNLOAD_BASE_URL;
    @CommonFilePathConfig.FilePathAnnotation(
            localFilePath = true
    )
    public static String DIR_APP_CACHE_IMAGE_SCREEN_SHOT;
    public static String PLUGIN_BIG_ICON_URL;

    public CommonFilePathConfig() {
    }

    static {
        DIR_APP_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + APP_DIR_NAME;
        DIR_APP_CACHE = DIR_APP_NAME + "/cache";
        DIR_APP_LOG = DIR_APP_NAME + "/log";
        DIR_APP_CRASH_LOG = DIR_APP_NAME + "/crash/";
        DIR_APP_DOWNLOAD = DIR_APP_NAME + "/download/";
        DIR_APP_CACHE_VOICE = DIR_APP_NAME + "/voice";
        DIR_APP_CACHE_VIDEO = DIR_APP_NAME + "/videos";
        DIR_APP_CACHE_FILE = DIR_APP_NAME + "/files";
        DIR_APP_CACHE_VIDEO_THUMBNAIL = DIR_APP_NAME + "/thumbnails";
        DIR_APP_CACHE_CAMERA = DIR_APP_NAME + "/camera";
        DIR_APP_CACHE_IMAGE_BIG = DIR_APP_NAME + "/imagebig";
        DIR_APP_CACHE_IMAGE_COMPRESS = DIR_APP_NAME + "/imagecompress";
        DIR_APP_CACHE_IMAGE_SCREEN_SHOT = DIR_APP_NAME + "/imageshot";
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    @Documented
    public @interface FilePathAnnotation {
        boolean localFilePath();
    }

}
