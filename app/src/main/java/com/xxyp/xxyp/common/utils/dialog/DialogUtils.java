
package com.xxyp.xxyp.common.utils.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;

/**
 * Description : 弹出框工具类
 * Created by sunpengfei on 2017/9/1.
 * Person in charge : sunpengfei
 */
public class DialogUtils {

    private static DialogUtils mInstance;

    public static DialogUtils getInstance() {
        if (mInstance == null) {
            synchronized (DialogUtils.class) {
                if (mInstance == null) {
                    mInstance = new DialogUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 带标题 内容
     * 
     * @param context 上下文
     * @param title 标题
     * @param content 内容
     * @param listener 监听
     */
    public void showDialog(Activity context, String title, String content,
            CommonDialog.OnDialogListener listener) {
        CommonDialog dialog = new CommonDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CommonDialog.DIALOG_TITLE, title);
        bundle.putString(CommonDialog.DIALOG_CONTENT, content);
        dialog.setArguments(bundle);
        dialog.setDialogListener(listener);
        dialog.showDialog(context);
    }

    /**
     * 带标题 内容
     * 
     * @param context 上下文
     * @param title 标题
     * @param content 内容
     * @param confirm 确认文本
     * @param cancel 取消文本
     * @param listener 监听
     */
    public void showDialog(Activity context, String title, String content, String confirm,
            String cancel, CommonDialog.OnDialogListener listener) {
        CommonDialog dialog = new CommonDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CommonDialog.DIALOG_TITLE, title);
        bundle.putString(CommonDialog.DIALOG_CONTENT, content);
        bundle.putString(CommonDialog.DIALOG_CONFIRM, confirm);
        bundle.putString(CommonDialog.DIALOG_CANCEL, cancel);
        dialog.setArguments(bundle);
        dialog.setDialogListener(listener);
        dialog.showDialog(context);
    }

}
