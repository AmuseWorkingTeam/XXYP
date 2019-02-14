
package com.xxyp.xxyp.common.utils;

import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;

/**
 * Description : 文件操作管理类
 */
public class FileUtils {

    private FileUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 通过路径创建文件
     * @param path  路径
     * @return  File
     */
    public static File getFileByPath(String path) {
        if (TextUtils.isEmpty(path)) {
            return null;
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }

    /**
     * @return 检查sdcard 空间大于500kb true
     */
    public static Boolean checkSDCard() {
        if (!isSDExist()){
            return false;
        }
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            if (moreSDAvailableSpare()) {
                return true;
            }
        } else {
            return false;
        }
        return false;
    }

    /**
     * 判断SD卡是否存在
     *
     * @return boolean
     */
    public static boolean isSDExist() {
        return Environment.getExternalStorageDirectory().exists();
    }

    /**
     * @return sdcard 空间
     */
    public static boolean moreSDAvailableSpare() {
        File path = Environment.getExternalStorageDirectory();
        StatFs statfs = new StatFs(path.getPath());
        long blocSize = statfs.getBlockSize(); // byte
        long totalBlocks = statfs.getBlockCount();
        long availaBlock = statfs.getAvailableBlocks();
        long total = totalBlocks * blocSize;
        long availale = availaBlock * blocSize;
        int arrMemorySD = (int)((total - availale) / 1024); // kb
        return arrMemorySD > 500; // 500 kb
    }

}
