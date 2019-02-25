
package com.xxyp.xxyp.common.utils.dialog;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.xxyp.xxyp.common.view.dialog.CommonDialogView;
import com.xxyp.xxyp.common.view.dialog.bean.DialogOperateBean;

import java.util.List;
import java.util.Map;

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
     * @param context  上下文
     * @param title    标题
     * @param content  内容
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
     * @param context  上下文
     * @param title    标题
     * @param content  内容
     * @param confirm  确认文本
     * @param cancel   取消文本
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


    /**
     * 显示操作弹窗
     *
     * @param context     上下文
     * @param list        存放操作的字符串集合
     * @param colors      对应相应list中字符串的颜色值
     * @param position    位置：0 中间 1 底部
     * @param isNotCancel 点击弹框外部不能取消弹框
     * @param decMap
     */
    public void showOperateDialog(Context context, List<String> list, Map<Integer, String> decMap, Map<Integer, Integer> colors,
                                  int position, boolean isNotCancel, CommonDialogView.DialogViews_ask.DialogViews_askImpl listener) {
        DialogOperateBean dialogBean = new DialogOperateBean();
        dialogBean.setColorMap(colors);
        dialogBean.setList(list);
        dialogBean.setNotCancel(isNotCancel);
        dialogBean.setPosition(position);
        dialogBean.setDecMap(decMap);
        CommonDialogView dialog = new CommonDialogView(context, dialogBean,
                listener);
        if (isNotCancel) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
        }
        dialog.show();
    }
}
