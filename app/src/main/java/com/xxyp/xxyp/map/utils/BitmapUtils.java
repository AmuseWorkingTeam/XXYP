package com.xxyp.xxyp.map.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;

import java.io.IOException;

public class BitmapUtils {

    private static final String TAG = BitmapUtils.class.getSimpleName();

    public BitmapUtils() {
    }

    public static Bitmap photoCompound(Bitmap bitmap1, Bitmap bitmap2) {
        Bitmap newBitmap = null;
        newBitmap = Bitmap.createBitmap(bitmap1);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        int w = bitmap1.getWidth();
        int h = bitmap1.getHeight();
        int w_2 = bitmap2.getWidth();
        int h_2 = bitmap2.getHeight();
        paint.setColor(-1);
        paint.setAlpha(0);
        canvas.drawRect(0.0F, 0.0F, (float) bitmap1.getWidth(), (float) bitmap1.getHeight(), paint);
        paint = new Paint();
        canvas.drawBitmap(bitmap2, (float) (Math.abs(w - w_2) / 2), (float) (Math.abs(h - h_2) / 2), paint);
        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();
        return newBitmap;
    }

    public static int getImageSpinAngle(String path) {
        short degree = 0;

        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt("Orientation", 1);
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
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return degree;
    }

    public static Bitmap rotatingImage(Bitmap bitmap, int rotate) {
        if (bitmap == null) {
            return null;
        } else {
            Matrix matrix = new Matrix();
            matrix.postRotate((float)rotate);

            Bitmap newBitmap;
            try {
                newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            } catch (OutOfMemoryError var5) {
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
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int height = options.outHeight;
        int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            int heightRatio = Math.round((float)height / (float)reqHeight);
            int widthRatio = Math.round((float)width / (float)reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
