
package com.xxyp.xxyp.user.utils;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.xxyp.xxyp.user.view.wheel.CardSelectPopWindow;
import com.xxyp.xxyp.user.view.wheel.SingleSelectView;

import java.util.Arrays;
import java.util.List;

/**
 * Description : 处理单选事件，内包含底部弹出选择控件层
 */
public class SingleCheckListener extends CheckListener {

    private int color;

    public SingleCheckListener(Activity context) {
        super(context);
    }

    @Override
    public void handleCheck(View parentView, TextView showView,
                            IWheelDataChangeCallback iWheelDataChangeCallback, String... valueDatas) {
        handleCheck(parentView, showView, valueDatas != null ? Arrays.asList(valueDatas) : null, iWheelDataChangeCallback);
    }


    /**
     * 处理单选
     *
     * @param parentView               父布局
     * @param showView                 获取的值显示在哪个textView上
     * @param dataList                 数据集
     * @param iWheelDataChangeCallback 回调
     */
    public void handleCheck(View parentView, TextView showView, List<String> dataList,
                            IWheelDataChangeCallback iWheelDataChangeCallback) {
        if (parentView == null || dataList == null) {
            return;
        }
        int position = 0;
        if (showView.getText() != null && !TextUtils.isEmpty(showView.getText().toString())) {
            position = dataList.indexOf(showView.getText().toString());
        }
        SingleSelectView ssView = new SingleSelectView(mContext, dataList, position);
        CardSelectPopWindow pw = new CardSelectPopWindow((Activity) mContext, parentView, ssView.getView());
        pw.changeButtonColor(color);
        MyClickListener myClickListener = new MyClickListener(pw, showView, dataList, position,
                iWheelDataChangeCallback);
        ssView.setWheelFinisheListener(myClickListener);
        pw.setConfirmListener(myClickListener);
    }

    public void setButtonTextColor(int color) {
        this.color = color;
    }

    /**
     * 处理单选
     *
     * @param parentView 父布局
     * @param showView   获取的值显示在哪个textView上
     * @param dataList   数据集
     */
    public void handleCheck(View parentView, TextView showView, List<String> dataList) {
        handleCheck(parentView, showView, dataList, null);
    }

    class MyClickListener implements SingleSelectView.OnWheelFinisheListener, View.OnClickListener {

        private final List<String> dataList;

        private int position;

        private final TextView showView;

        private final CardSelectPopWindow pw;

        private final IWheelDataChangeCallback iWheelDataChangeCallback;

        public MyClickListener(CardSelectPopWindow pw, TextView showView, List<String> dataList,
                               int position, IWheelDataChangeCallback iWheelDataChangeCallback) {
            this.pw = pw;
            this.showView = showView;
            this.dataList = dataList;
            this.position = position;
            this.iWheelDataChangeCallback = iWheelDataChangeCallback;
        }

        @Override
        public void onClick(View v) {
            if (position != -1) {
                if (showView != null) {
                    showView.setText(dataList.get(position));
                }
                if (iWheelDataChangeCallback != null) {
                    iWheelDataChangeCallback.wheelDataChangeCallback(dataList.get(position));
                }
            }
            pw.dismiss();
        }

        @Override
        public void OnChange(String currentValue, int currentIndex) {
            position = currentIndex;
        }
    }
}
