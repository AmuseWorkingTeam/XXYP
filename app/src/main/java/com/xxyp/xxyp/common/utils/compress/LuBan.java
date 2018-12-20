
package com.xxyp.xxyp.common.utils.compress;

import android.graphics.Bitmap;
import android.text.TextUtils;

import com.xxyp.xxyp.common.utils.AppContextUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.xxyp.xxyp.common.utils.compress.Preconditions.checkNotNull;

/**
 * Description : luBan压缩 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public class LuBan {

    private static final String DEFAULT_DISK_CACHE_DIR = "XXYPCache";

    private static final String TAG = "LuBanCompress";

    private static LuBan mInstance;

    /*
     * 压缩一次
     */
    public static final int FIRST_GEAR = 1;

    /*
     * 三次压缩
     */
    public static final int THIRD_GEAR = 3;

    /*
     * 自定义压缩
     */
    public static final int CUSTOM_GEAR = 4;

    private LuBanBuilder mBuilder;

    private File mFile;

    private List<File> mFileList;

    public static LuBan getInstance() {
        if (mInstance == null) {
            synchronized (LuBan.class) {
                if (mInstance == null) {
                    mInstance = new LuBan(getPhotoCacheDir(null));
                }
            }
        }
        return mInstance;
    }

    private LuBan(File cacheDir) {
        mBuilder = new LuBanBuilder(cacheDir);
    }

    /**
     * 获取缓存文件路径
     * 
     * @param cacheName 文件名
     * @return File
     */
    private static File getPhotoCacheDir(String cacheName) {
        if (TextUtils.isEmpty(cacheName)) {
            cacheName = "XXYPCache";
        }
        File cacheDir = AppContextUtils.getAppContext().getExternalCacheDir();
        if (cacheDir != null) {
            File result = new File(cacheDir, cacheName);
            if (!result.mkdirs() && (!result.exists() || !result.isDirectory())) {
                // File wasn't able to create a directory, or the result exists
                // but not a directory
                return null;
            }
            File noMedia = new File(cacheDir + "/.nomedia");
            if (noMedia.mkdirs() || noMedia.exists()) {
                return result;
            }
            return result;
        }
        return null;
    }

    /**
     * 压缩单个文件
     * 
     * @param file 文件
     * @return LuBan
     */
    public LuBan compress(File file) {
        mFile = file;
        return this;
    }

    /**
     * 压缩文件列表
     * 
     * @param files 文件
     * @return LuBan
     */
    public LuBan compress(List<File> files) {
        mFileList = files;
        return this;
    }

    /*
     * 设置图片的压缩等级
     */
    public LuBan putGear(int gear) {
        mBuilder.gear = gear;
        return this;
    }

    /*
     * 设置图片的大小
     */
    public LuBan setMaxSize(int size) {
        mBuilder.maxSize = size;
        return this;
    }

    /*
     * 设置图片的最大宽度
     */
    public LuBan setMaxWidth(int width) {
        mBuilder.maxWidth = width;
        return this;
    }

    /*
     * 设置图片的最大高度
     */
    public LuBan setMaxHeight(int height) {
        mBuilder.maxHeight = height;
        return this;
    }
    /*
     * 设置压缩格式
     */

    public LuBan setCompressFormat(Bitmap.CompressFormat compressFormat) {
        mBuilder.compressFormat = compressFormat;
        return this;
    }

    /**
     * 设置压缩后的文件名字
     *
     * @param fileName 文件名字
     */
    public LuBan setCompressFileName(String fileName) {
        mBuilder.compressFileName = fileName;
        return this;
    }

    /**
     * 清空LuBan所产生的缓存
     */
    public LuBan clearCache() {
        if (mBuilder.cacheDir.exists()) {
            deleteFile(mBuilder.cacheDir);
        }
        return this;
    }

    /**
     * 删除目录
     * 
     * @param fileOrDirectory 目录
     */
    private void deleteFile(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File file : fileOrDirectory.listFiles()) {
                deleteFile(file);
            }
        }
        fileOrDirectory.delete();
    }

    /**
     * 压缩单个文件
     * @param path 文件路径
     * @return LuBan
     */
    public LuBan compressByPath(String path) {
        if (TextUtils.isEmpty(path)) {
            throw new IllegalArgumentException("filePath cannot be null!!!");
        }
        return compress(new File(path));
    }

    /**
     * 单个图片进行压缩
     *
     * @return LuBanCompress
     */
    public LuBan compressSingle(final OnCompressListener compressListener) {
        checkNotNull(mFile,
                "the image file cannot be null, please call .loadFile() before this method!");
        Observable<File> observable = asObservable();
        if (observable != null) {
            observable.subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread()).doOnRequest(new Action1<Long>() {
                        @Override
                        public void call(Long aLong) {
                            if (compressListener != null)
                                compressListener.onStart();
                        }
                    }).onErrorResumeNext(Observable.<File> empty())
                    .filter(new Func1<File, Boolean>() {
                        @Override
                        public Boolean call(File file) {
                            return file != null;
                        }
                    }).subscribe(new Action1<File>() {
                        @Override
                        public void call(File file) {
                            if (compressListener != null)
                                compressListener.onSuccess(file);
                        }
                    }, new Action1<Throwable>() {
                        @Override
                        public void call(Throwable throwable) {
                            if (compressListener != null)
                                compressListener.onError(throwable);
                        }
                    });
        }
        return this;
    }

    /**
     * 压缩多个文件
     * @param paths 文件路径
     * @return LuBan
     */
    public LuBan compressByPath(List<String> paths) {
        if (paths != null && paths.size() > 0) {
            List<File> files = new ArrayList<>();
            for (String path : paths) {
                files.add(new File(path));
            }
            return compress(files);
        }
        return this;
    }

    /**
     * 压缩多个文件并且等待所有图片压缩完成后回调给前端
     *
     * @param multiCompressListener 回调
     * @return LuBanCompress
     */
    public LuBan compressMultiple(final OnMultiCompressListener multiCompressListener) {
        checkNotNull(mFileList,
                "the image files cannot be null, please call .loadFile() before this method!");
        Observable<List<File>> observables = asListObservable();
        observables.subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread())
                .doOnRequest(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        if (multiCompressListener != null) {
                            multiCompressListener.onStart();
                        }
                    }
                }).filter(new Func1<List<File>, Boolean>() {
                    @Override
                    public Boolean call(List<File> files) {
                        return files != null && files.size() > 0;
                    }
                }).subscribe(new Action1<List<File>>() {
                    @Override
                    public void call(List<File> files) {
                        if (multiCompressListener != null) {
                            multiCompressListener.onSuccess(files);
                        }
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        if (multiCompressListener != null) {
                            multiCompressListener.onError(throwable);
                        }
                    }
                });
        return this;
    }

    /**
     * 返回File Observable，并在AndroidSchedulers.mainThread 订阅
     */
    public Observable<File> asObservable() {
        return asObservable(AndroidSchedulers.mainThread());
    }

    /**
     * 返回File Observable，并在指定线程订阅
     */
    public Observable<File> asObservable(Scheduler scheduler) {
        LuBanCompressor compressor = new LuBanCompressor(mBuilder);
        return compressor.singleAction(mFile, scheduler);
    }

    /**
     * 返回fileList Observable，并在AndroidSchedulers.mainThread 订阅
     */
    public Observable<List<File>> asListObservable() {
        return asListObservable(AndroidSchedulers.mainThread());
    }

    /**
     * 返回返回fileList Observable，并在指定线程订阅
     *
     * @param scheduler 指定订阅线程
     */
    public Observable<List<File>> asListObservable(Scheduler scheduler) {
        LuBanCompressor compressor = new LuBanCompressor(mBuilder);
        return compressor.multiAction(mFileList, scheduler);
    }

    /**
     * 单个回调
     */
    public interface OnCompressListener {
        /**
         * 压缩开始时调用，可具体实现
         */
        void onStart();

        /**
         * 压缩成功时触发
         */
        void onSuccess(File file);

        /**
         * 压缩过程出现意外
         */
        void onError(Throwable e);
    }

    /**
     * 多个压缩回调
     */
    public interface OnMultiCompressListener {

        /**
         * 开始压缩时触发
         */
        void onStart();

        /**
         * 所有文件压缩成功时触发
         */
        void onSuccess(List<File> fileList);

        /**
         * 压缩过程中出现意外
         */
        void onError(Throwable e);

    }
}
