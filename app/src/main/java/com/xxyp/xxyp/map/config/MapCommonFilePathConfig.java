
package com.xxyp.xxyp.map.config;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Description :项目文件路径常量，包括本地和远程 Created by 143568 on 2016/6/3. Job
 * number：143568 Phone ：13683103169 Email：zhengbing@syswin.com Person in charge
 * : 郑兵 Leader：李晓
 */
public class MapCommonFilePathConfig {

    /**
     * 应用程序文件夹名称
     **/
    public static String APP_DIR_NAME = MapMetaData.TOON_FILE_NAME;

    /**
     * 应用程序文件夹
     **/
    @FilePathAnnotation(localFilePath = true)
    public static String DIR_APP_NAME = android.os.Environment.getExternalStorageDirectory()
            .getAbsolutePath() + "/" + APP_DIR_NAME;

    @FilePathAnnotation(localFilePath = true)
    public static String DIR_APP_CACHE = DIR_APP_NAME + "/cache";

    /**
     * 应用程序本地拍照图片文件夹
     **/
    @FilePathAnnotation(localFilePath = true)
    public static String DIR_APP_CACHE_CAMERA = DIR_APP_NAME + "/camera";

    /**
     * 本地文件路径的注解类 ：用法：要在本地建立文件夹，加入注解：@FilePathAnnotation(localFilePath = true)
     * 根据字段的值生成本地文件夹
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({
            ElementType.FIELD, ElementType.METHOD
    })

    @Documented
    public @interface FilePathAnnotation {
        boolean localFilePath();
    }
}
