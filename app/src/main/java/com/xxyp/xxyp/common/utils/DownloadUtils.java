
package com.xxyp.xxyp.common.utils;

import com.xxyp.xxyp.common.log.XXLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Description : 下载工具类
 */
public class DownloadUtils {

    private final String TAG = DownloadUtils.class.getSimpleName();

    private static DownloadUtils mInstance;

    private OkHttpClient mOkHttpClient;

    public static DownloadUtils getInstance() {
        if (mInstance == null) {
            synchronized (DownloadUtils.class) {
                if (mInstance == null) {
                    mInstance = new DownloadUtils();
                }
            }
        }
        return mInstance;
    }

    private DownloadUtils() {
        mOkHttpClient = new OkHttpClient();
    }

    /**
     * 异步下载文件
     *
     * @param url 下载链接
     * @param fileDir 存入路径
     * @param suffix 文件后缀
     * @param callback 回调
     */
    public void downloadAsyn(final String url, final String fileDir, final String suffix,
            final DownloadCallback callback) {
        Request request = new Request.Builder().url(url).build();
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                sendFailedStringCallback(callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = null;
                byte[] buf = new byte[2048];
                int len = 0;
                FileOutputStream fos = null;
                try {
                    is = response.body().byteStream();
                    File file = FileUtils.getFileByPath(getFileName(fileDir, suffix));
                    fos = new FileOutputStream(file);
                    while ((len = is.read(buf)) != -1) {
                        fos.write(buf, 0, len);
                    }
                    fos.flush();
                    // 如果下载文件成功，第一个参数为文件的绝对路径
                    if (callback != null) {
                        callback.postSuccess(file);
                    }
                } catch (IOException e) {
                    XXLog.log_e(TAG, e.getMessage());
                    sendFailedStringCallback(callback);
                } finally {
                    try {
                        if (is != null)
                            is.close();
                    } catch (IOException e) {
                    }
                    try {
                        if (fos != null)
                            fos.close();
                    } catch (IOException e) {
                        XXLog.log_e(TAG, e.getMessage());
                    }
                }
            }
        });
    }

    private void sendFailedStringCallback(DownloadCallback callback) {
        if (callback != null)
            callback.postFail(null, -1);
    }

    private void sendSuccessStringCallback(File file, DownloadCallback callback) {
        if (callback != null)
            callback.postSuccess(file);
    }

    private String getFileName(String fileDir, String suffix) {
        String fileName = fileDir + "/" + System.currentTimeMillis() + suffix;
        return fileName;
    }
}
