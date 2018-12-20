
package com.xxyp.xxyp.common.utils.compress;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.xxyp.xxyp.common.log.XXLog;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observable;
import rx.Scheduler;
import rx.functions.FuncN;
import rx.schedulers.Schedulers;

import static com.xxyp.xxyp.common.utils.compress.Preconditions.buildCacheName;
import static com.xxyp.xxyp.common.utils.compress.Preconditions.checkNotNull;

/**
 * Description:压缩算法 Created by wxh on 2016/12/20 Job number:138746 Phone
 * :15233620521 Email: wangxiaohui@syswin.com Person in charge:王晓辉 Leader:王晓辉
 */

public class LuBanCompressor {

    private static final String TAG = "LuBanCompressor";

    private final LuBanBuilder mLuBan;

    LuBanCompressor(LuBanBuilder builder) {
        this.mLuBan = builder;
    }

    /**
     * 压缩单个文件
     * 
     * @param file 待压缩的文件
     * @param scheduler 订阅线程
     * @return 观察者
     */
    Observable<File> singleAction(final File file, Scheduler scheduler) {
        return Observable.fromCallable(new Callable<File>() {
            @Override
            public File call() throws Exception {
                return compressImage(mLuBan.gear, file);
            }
        }).subscribeOn(Schedulers.computation()).observeOn(scheduler);
    }

    /**
     * 批量压缩文件
     * 
     * @param files 文件列表
     * @param scheduler 订阅线程
     * @return 观察者
     */
    Observable<List<File>> multiAction(List<File> files, Scheduler scheduler) {
        List<Observable<File>> observables = new ArrayList<>(files.size());
        for (final File file : files) {
            observables.add(Observable.fromCallable(new Callable<File>() {
                @Override
                public File call() throws Exception {
                    return compressImage(mLuBan.gear, file);
                }
            }));
        }
        return Observable.zip(observables, new FuncN<List<File>>() {
            @Override
            public List<File> call(Object... args) {
                List<File> files = new ArrayList<>(args.length);
                for (Object o : args) {
                    files.add((File)o);
                }
                return files;
            }
        }).subscribeOn(Schedulers.computation()).observeOn(scheduler);
    }

    /**
     * 压缩图片
     * 
     * @param gear 压缩级别
     * @param file 待压缩的图片
     * @return 压缩后文件
     */
    private File compressImage(int gear, File file) throws IOException {
        switch (gear) {
            case LuBan.THIRD_GEAR:
                return thirdCompress(file);
            case LuBan.CUSTOM_GEAR:
                return customCompress(file);
            case LuBan.FIRST_GEAR:
                return firstCompress(file);
            default:
                return file;
        }
    }

    /**
     * <ol>
     * 1,判断图片比例值，是否处于以下区间内；
     * <li>[1, 0.5625) 即图片处于 [1:1 ~ 9:16) 比例范围内</li>
     * <li>[0.5625, 0.5) 即图片处于 [9:16 ~ 1:2) 比例范围内</li>
     * <li>[0.5, 0) 即图片处于 [1:2 ~ 1:∞) 比例范围内</li>
     * <li></li>
     * </ol>
     * <ol>
     * 2,判断图片最长边是否过边界值:
     * <li>[1, 0.5625) 边界值为：1664 * n（n=1）, 4990 * n（n=2）, 1280 *
     * pow(2,n-1)（n≥3）</li>
     * <li>[0.5625, 0.5) 边界值为：1280 * pow(2, n-1)（n≥1） [0.5, 0) 边界值为：1280 *pow(2,
     * n-1)（n≥1）</li>
     * </ol>
     * <ol>
     * 3,计算压缩图片实际边长值，以第2步计算结果为准，超过某个边界值则： width / pow(2, n-1)，height/pow(2, n-1)
     * 计算压缩图片的实际文件大小，以第2、3步结果为准，图片比例越大则文件越大.size = (newW * newH) / (width *
     * height) * m；
     * <li>[1, 0.5625) 则 width & height,对应 1664，4990，1280 * n（n≥3），m 对应
     * 150，300，300；</li>
     * <li>[0.5625, 0.5) 则 width = 1440，height = 2560, m = 200；</li>
     * <li>[0.5, 0) 则 width= 1280，height = 1280 / scale，m = 500；</li>
     * 注：scale为比例值
     * </ol>
     * <ol>
     * 4,判断第4步的size是否过小
     * <li>[1, 0.5625) 则最小 size 对应 60，60，100</li>
     * <li>[0.5625, 0.5) 则最小 size都为 100</li>
     * <li>[0.5, 0) 则最小 size 都为 100</li>
     * </ol>
     * <ol>
     * 5,将前面求到的值压缩图片 width, height, size 传入压缩流程，压缩图片直到满足以上数值
     * </ol>
     */
    private File thirdCompress(@NonNull File file) {
        checkNotNull(mLuBan.cacheDir,
                "the cache dir cannot be null, please call .getPhotoCacheDir() before this method!");
        String thumb = mLuBan.cacheDir.getAbsolutePath() + "/"
                + buildCacheName(file, mLuBan.compressFileName);
        File thumbFile = new File(thumb);
        // 如果缓存文件已经存在，则直接返回已有文件
        if (thumbFile.exists()) {
            return thumbFile;
        }
        double size;
        String filePath = file.getAbsolutePath();
        // 旋转角度
        int angle = getImageSpinAngle(filePath);
        int[] sizes = getImageSize(filePath);
        // 图片宽度
        int width = sizes[0];
        // 图片高度
        int height = sizes[1];
        // 将宽高变为偶数
        int thumbW = width % 2 == 1 ? width + 1 : width;
        int thumbH = height % 2 == 1 ? height + 1 : height;
        // 变为宽小于高
        width = thumbW > thumbH ? thumbH : thumbW;
        height = thumbW > thumbH ? thumbW : thumbH;

        double scale = ((double)width / height);
        // 图片处于宽：高= [1:1 ~ 9:16)
        if (scale <= 1 && scale > 0.5625) {
            // 判断图片最长边是否过边界值
            if (height < 1664) {
                if (file.length() / 1024 < 150)
                    return file;
                // 超过边界值则重新计算大小
                size = (width * height) / Math.pow(1664, 2) * 150;
                size = size < 60 ? 60 : size;
            } else if (height >= 1664 && height < 4990) {
                thumbW = width / 2;
                thumbH = height / 2;
                size = (thumbW * thumbH) / Math.pow(2495, 2) * 300;
                size = size < 60 ? 60 : size;
            } else if (height >= 4990 && height < 10240) {
                thumbW = width / 4;
                thumbH = height / 4;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            } else {
                int multiple = height / 1280 == 0 ? 1 : height / 1280;
                thumbW = width / multiple;
                thumbH = height / multiple;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            }
            // 图片处于宽：高= [9:16 ~ 1:2)
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (height < 1280 && file.length() / 1024 < 200)
                return file;

            int multiple = height / 1280 == 0 ? 1 : height / 1280;
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = (thumbW * thumbH) / (1440.0 * 2560.0) * 400;
            size = size < 100 ? 100 : size;
            // 图片处于宽：高= [1:2 ~ 1:∞) 比例范围内
        } else {
            int multiple = (int)Math.ceil(height / (1280.0 / scale));
            thumbW = width / multiple;
            thumbH = height / multiple;
            size = ((thumbW * thumbH) / (1280.0 * (1280 / scale))) * 500;
            size = size < 100 ? 100 : size;
        }

        return compress(filePath, thumb, mLuBan.compressFormat, thumbW, thumbH, angle, (long)size);
    }

    /**
     * <ol>
     * 1,判断图片比例值，是否处于以下区间内；
     * <li>[1, 0.5625) 即图片处于 [1:1 ~ 9:16) 比例范围内</li>
     * <li>[0.5625, 0.5) 即图片处于 [9:16 ~ 1:2) 比例范围内</li>
     * <li>[0.5, 0) 即图片处于 [1:2 ~ 1:∞) 比例范围内</li>
     * <li></li>
     * </ol>
     * <ol>
     * 2,判断图片最长边是否过边界值:
     * <li>[1, 0.5625) 边界值为：1664 * n（n=1）, 4990 * n（n=2）, 1280 *
     * pow(2,n-1)（n≥3）</li>
     * <li>[0.5625, 0.5) 边界值为：1280 * pow(2, n-1)（n≥1） [0.5, 0) 边界值为：1280 *pow(2,
     * n-1)（n≥1）</li>
     * </ol>
     * <ol>
     * 3,计算压缩图片实际边长值，以第2步计算结果为准，超过某个边界值则： width / pow(2, n-1)，height/pow(2, n-1)
     * 计算压缩图片的实际文件大小，以第2、3步结果为准，图片比例越大则文件越大.size = (newW * newH) / (width *
     * height) * m；
     * <li>[1, 0.5625) 则 width & height,对应 1664，4990，1280 * n（n≥3），m 对应
     * 150，300，300；</li>
     * <li>[0.5625, 0.5) 则 width = 1440，height = 2560, m = 200；</li>
     * <li>[0.5, 0) 则 width= 1280，height = 1280 / scale，m = 500；</li>
     * 注：scale为比例值
     * </ol>
     * <ol>
     * 4,判断第4步的size是否过小
     * <li>[1, 0.5625) 则最小 size 对应 60，60，100</li>
     * <li>[0.5625, 0.5) 则最小 size都为 100</li>
     * <li>[0.5, 0) 则最小 size 都为 100</li>
     * </ol>
     * <ol>
     * 5,将前面求到的值压缩图片 width, height, size 传入压缩流程，压缩图片直到满足以上数值
     * </ol>
     */
    private File firstCompress(@NonNull File file) {
        checkNotNull(mLuBan.cacheDir,
                "the cache dir cannot be null, please call .getPhotoCacheDir() before this method!");
        int minSize = 60;
        int longSide = 720;
        int shortSide = 1280;
        String thumbFilePath = mLuBan.cacheDir.getAbsolutePath() + "/"
                + buildCacheName(file, mLuBan.compressFileName);
        File thumbFile = new File(thumbFilePath);
        // 如果缓存文件已经存在，则直接返回已有文件
        if (thumbFile.exists()) {
            return thumbFile;
        }
        String filePath = file.getAbsolutePath();
        long size = 0;
        long maxSize = file.length() / 5;
        // 获得图像旋转角度
        int angle = getImageSpinAngle(filePath);
        // 获取图片宽高
        int[] imgSize = getImageSize(filePath);
        int width = 0, height = 0;
        // 宽小于高：长图
        if (imgSize[0] <= imgSize[1]) {
            // 宽高比例
            double scale = (double)imgSize[0] / (double)imgSize[1];
            // 图片处于宽：高= [1:1 ~ 9:16)
            if (scale <= 1.0 && scale > 0.5625) {
                width = imgSize[0] > shortSide ? shortSide : imgSize[0];
                height = width * imgSize[1] / imgSize[0];
                size = minSize;
                // 图片处于宽：高= [1:2 ~ 1:∞) 比例范围内
            } else if (scale <= 0.5625) {
                height = imgSize[1] > longSide ? longSide : imgSize[1];
                width = height * imgSize[0] / imgSize[1];
                size = maxSize;
            }
            // 宽大于高：宽图
        } else {
            double scale = (double)imgSize[1] / (double)imgSize[0];
            // 图片处于高：宽 =[1:1 ~ 9:16)
            if (scale <= 1.0 && scale > 0.5625) {
                height = imgSize[1] > shortSide ? shortSide : imgSize[1];
                width = height * imgSize[0] / imgSize[1];
                size = minSize;
                // 图片处于高：宽 = [1:2 ~ 1:∞) 比例范围内
            } else if (scale <= 0.5625) {
                width = imgSize[0] > longSide ? longSide : imgSize[0];
                height = width * imgSize[1] / imgSize[0];
                size = maxSize;
            }
        }

        return compress(filePath, thumbFilePath, mLuBan.compressFormat, width, height, angle, size);
    }

    /**
     * 自定义压缩文件:为了满足不同业务对图片需求
     * 
     * @param file 文件
     * @return 压缩后的文件
     */
    private File customCompress(@NonNull File file) throws IOException {
        checkNotNull(mLuBan.cacheDir,
                "the cache dir cannot be null, please call .getPhotoCacheDir() before this method!");

        String thumbFilePath = mLuBan.cacheDir.getAbsolutePath() + "/"
                + buildCacheName(file, mLuBan.compressFileName);
        File thumbFile = new File(thumbFilePath);
        // 如果缓存文件已经存在，则直接返回已有文件
        if (thumbFile.exists()) {
            return thumbFile;
        }
        String filePath = file.getAbsolutePath();
        Bitmap.CompressFormat format = getImageFormat(filePath);
        // 如果格式为空，则不压缩，直接返回文件:只适合自定义压缩
        if (format == null) {
            return file;
        }
        int angle = getImageSpinAngle(filePath);
        // 如果文件小于设置的最大大小则按照源文件大小
        long fileSize = mLuBan.maxSize > 0 && mLuBan.maxSize < file.length() / 1024 ? mLuBan.maxSize
                : file.length() / 1024;
        // 获取源文件宽高
        int[] size = getImageSize(filePath);
        int width = size[0];
        int height = size[1];

        if (mLuBan.maxSize > 0 && mLuBan.maxSize < file.length() / 1024f) {
            // find a suitable size
            float scale = (float)Math.sqrt(file.length() / 1024f / mLuBan.maxSize);
            width = (int)(width / scale);
            height = (int)(height / scale);
        }

        // check the width&height
        if (mLuBan.maxWidth > 0) {
            width = Math.min(width, mLuBan.maxWidth);
        }
        if (mLuBan.maxHeight > 0) {
            height = Math.min(height, mLuBan.maxHeight);
        }
        float scale = Math.min((float)width / size[0], (float)height / size[1]);
        width = (int)(size[0] * scale);
        height = (int)(size[1] * scale);

        // 不压缩
        if (mLuBan.maxSize > file.length() / 1024f && scale == 1) {
            return file;
        }

        return compress(filePath, thumbFilePath, format, width, height, angle, fileSize);
    }

    /**
     * 指定参数压缩图片
     *
     * @param sourceFilePath 图片大图路径
     * @param thumbFilePath 压缩文件路径
     * @param width 压缩后的宽度
     * @param height 压缩后的高度
     * @param angle 旋转角度
     * @param size 压缩后大小
     */
    private File compress(String sourceFilePath, String thumbFilePath, Bitmap.CompressFormat format,
            int width, int height, int angle, long size) {
        Bitmap thbBitmap = compress(sourceFilePath, width, height);

        thbBitmap = rotatingImage(angle, thbBitmap);

        return saveImage(sourceFilePath, thumbFilePath, thbBitmap, format, size);
    }

    /**
     * 获取指定大小的缩略图
     * 
     * @param imagePath 目标路径
     * @param width 压缩后宽度
     * @param height 压缩后高度
     * @return {@link Bitmap}
     */
    private Bitmap compress(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();

        // 设置inJustDecodeBounds为true后，decodeFile并不分配空间，此时计算原始图片的长度和宽度
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int outH = options.outHeight;
        int outW = options.outWidth;
        int inSampleSize = 1;

        if (outH > height || outW > width) {
            int halfH = outH / 2;
            int halfW = outW / 2;

            while ((halfH / inSampleSize) > height && (halfW / inSampleSize) > width) {
                inSampleSize <<= 1;
            }
        }

        // 创建一个临时空间
        options.inTempStorage = new byte[100 * 1024];
        // options.inSampleSize是以2的指数的倒数被进行放缩
        options.inSampleSize = inSampleSize;
        // 这里一定要将其设置回false，因为之前我们将其设置成了true
        options.inJustDecodeBounds = false;
        // 以下两个属性必须一起使用，否则不起作用
        options.inInputShareable = true;
        // 允许可清除
        options.inPurgeable = true;

        int heightRatio = (int)Math.ceil(options.outHeight / (float)height);
        int widthRatio = (int)Math.ceil(options.outWidth / (float)width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio;
            } else {
                options.inSampleSize = widthRatio;
            }
        }
        options.inJustDecodeBounds = false;
        Bitmap bitmap = null;
        try {
            FileInputStream stream = new FileInputStream(new File(imagePath));
            bitmap = BitmapFactory.decodeFileDescriptor(stream.getFD(), null, options);
        } catch (OutOfMemoryError | Exception e) {
            XXLog.log_e(TAG, "compress image failed:" + e.getMessage());
        }

        return bitmap;
    }

    /**
     * 获取图片的宽高
     *
     * @param imagePath 图片路径
     */
    private int[] getImageSize(String imagePath) {
        int[] res = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        try {
            FileInputStream stream = new FileInputStream(new File(imagePath));
            BitmapFactory.decodeFileDescriptor(stream.getFD(), null, options);
        } catch (Exception e) {
            XXLog.log_e(TAG, "getImageSize failed:" + e.getMessage());
        }

        res[0] = options.outWidth;
        res[1] = options.outHeight;
        return res;
    }

    /**
     * 根据文件头获取图片的格式：如果格式为空则不压缩.
     * <p>
     * 
     * @link {http://www.jianshu.com/p/5018d0a69ed3}
     *       </p>
     * @param imagePath 文件路径
     */
    private Bitmap.CompressFormat getImageFormat(String imagePath) {
        if (TextUtils.isEmpty(imagePath)) {
            return null;
        }
        try {
            InputStream stream = new FileInputStream(imagePath);
            byte[] bytes = new byte[2];
            stream.read(bytes);
            if (bytes.length > 0) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < bytes.length; i++) {
                    int v = bytes[i] & 0xFF;
                    String hv = Integer.toHexString(v);
                    if (hv.length() < 2) {
                        stringBuilder.append(0);
                    }
                    stringBuilder.append(hv);
                }
                if (stringBuilder.length() == 0) {
                    return Bitmap.CompressFormat.JPEG;
                }
                if (stringBuilder.toString().equalsIgnoreCase("8950")
                        || stringBuilder.toString().equalsIgnoreCase("5089")) {
                    return Bitmap.CompressFormat.PNG;
                }
                if (stringBuilder.toString().equalsIgnoreCase("FFD8")
                        || stringBuilder.toString().equalsIgnoreCase("D8FF")
                        || stringBuilder.toString().equalsIgnoreCase("424D")
                        || stringBuilder.toString().equalsIgnoreCase("4D42")) {
                    return Bitmap.CompressFormat.JPEG;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 旋转角度
     */
    private int getImageSpinAngle(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     *
     * @param angle 旋转的角度
     * @param bitmap 目标图片
     */
    private static Bitmap rotatingImage(int angle, Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        // rotate image
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        Bitmap newBitmap;
        try {
            // 创建一个位图
            newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(),
                    matrix, true);
        } catch (OutOfMemoryError error) {
            newBitmap = bitmap;
        }

        if (newBitmap == null) {
            newBitmap = bitmap;
        }
        if (bitmap != newBitmap && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
        return newBitmap;
    }

    /**
     * 保存图片到指定路径
     *
     * @param sourceFilePath 原文件储存路径
     * @param filePath 储存路径
     * @param bitmap 目标图片
     * @param size 期望大小
     */
    private File saveImage(String sourceFilePath, String filePath, Bitmap bitmap,
            Bitmap.CompressFormat format, long size) {
        checkNotNull(bitmap, TAG + "bitmap cannot be null");
        if (format == null) {
            format = mLuBan.compressFormat;
        }
        File result = new File(filePath.substring(0, filePath.lastIndexOf("/")));

        if (!result.exists() && !result.mkdirs())
            return null;

        try {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            int options = 100;
            // 将bitmap放至数组中，意在bitmap的大小
            bitmap.compress(format, options, stream);
            int byteLength = stream.toByteArray().length / 1024;
            while (byteLength > size && options > 6) {
                stream.reset();
                options -= 6;
                bitmap.compress(format, options, stream);
                byteLength = stream.toByteArray().length / 1024;
            }
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
                System.gc();
            }
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
        } catch (OutOfMemoryError | Exception e) {
            e.printStackTrace();
            return new File(sourceFilePath);
        }
        return new File(filePath);
    }

}
