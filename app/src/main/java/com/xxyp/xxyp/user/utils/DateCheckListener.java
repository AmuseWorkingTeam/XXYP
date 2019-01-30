
package com.xxyp.xxyp.user.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;


import com.xxyp.xxyp.user.view.wheel.CardSelectPopWindow;
import com.xxyp.xxyp.user.view.wheel.DateWheel;
import com.xxyp.xxyp.user.view.wheel.OnWheelFinishListener;
import com.xxyp.xxyp.user.view.wheel.bean.DialogWheelDateBean;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Description : 日期选择处理 Created by 139356 on 2015/10/27. Job number：139356 Phone
 * ：13070194942 Email：139356@syswin.com Person in charge : 刘一博 Leader：刘一博
 */
public class DateCheckListener {

    private static final int INDEX_YEAR = 0;

    private static final int INDEX_MONTH = 1;

    private static final int INDEX_DAY = 2;

    private String DEFAULT_DATE;

    private int color;

    private Context mContext;

    public DateCheckListener(Context context) {
        mContext = context;
        DEFAULT_DATE = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
    }

    public void handleDateCheck(View parentView, TextView showView, String checkDate,
                                IWheelDataChangeCallback iWheelDataChangeCallback) {
        if (parentView == null || showView == null) {
            return;
        }
        String showDate;
        if (showView.getText() != null && !TextUtils.isEmpty(showView.getText().toString())
                && !TextUtils.equals("日期", showView.getText().toString())) {
            showDate = checkDate;
        } else {
            showDate = DEFAULT_DATE;
        }
        DialogWheelDateBean dialogWheelDateBean = new DialogWheelDateBean();
        dialogWheelDateBean.setStrType(true);
        dialogWheelDateBean.setStrStartDate(DEFAULT_DATE);
        dialogWheelDateBean.setStrChooseDate(showDate);
        dialogWheelDateBean.setEndDateNoLimit(true);
        DateWheel dwView = new DateWheel(mContext, null, dialogWheelDateBean);
        CardSelectPopWindow pw = new CardSelectPopWindow((Activity) mContext, parentView, dwView.getTimeView());
        pw.changeButtonColor(color);
        MyClickListener regionClickListener = new MyClickListener(pw, showView,
                iWheelDataChangeCallback);
        dwView.setWheelFinishListener(regionClickListener);
        pw.setConfirmListener(regionClickListener);
    }

    public void setButtonTextColor(int color) {
        this.color = color;
    }

    class MyClickListener implements OnWheelFinishListener, View.OnClickListener {

        private final TextView showView;

        private final CardSelectPopWindow pw;

        private String date;

        private final IWheelDataChangeCallback iWheelDataChangeCallback;

        public MyClickListener(CardSelectPopWindow pw, TextView showView,
                               IWheelDataChangeCallback iWheelDataChangeCallback) {
            this.pw = pw;
            this.showView = showView;
            this.iWheelDataChangeCallback = iWheelDataChangeCallback;
        }

        @Override
        public void onClick(View v) {
            if (!TextUtils.isEmpty(date)) {
                if (showView != null) {
                    showView.setText(date);
                }
                if (iWheelDataChangeCallback != null) {
                    iWheelDataChangeCallback.wheelDataChangeCallback(date);
                }
            }
            pw.dismiss();
        }


        @Override
        public void onChange(String currentValue) {
            date = currentValue;
        }
    }

}
