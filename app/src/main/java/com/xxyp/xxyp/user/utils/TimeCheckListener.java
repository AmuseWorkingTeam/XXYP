
package com.xxyp.xxyp.user.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.xxyp.xxyp.user.view.wheel.CardSelectPopWindow;
import com.xxyp.xxyp.user.view.wheel.OnWheelFinishListener;
import com.xxyp.xxyp.user.view.wheel.TimeWheel;
import com.xxyp.xxyp.user.view.wheel.bean.DialogWheelTimeBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Description : 日期选择处理 Created by 139356 on 2015/10/27. Job number：139356 Phone
 * ：13070194942 Email：139356@syswin.com Person in charge : 刘一博 Leader：刘一博
 */
public class TimeCheckListener {

    private Calendar defaultCalendar;

    private int color;

    private Context mContext;

    private String timeFormat = "HH:mm";

    public TimeCheckListener(Context context) {
        mContext = context;
        defaultCalendar = Calendar.getInstance();
    }

    public void handleTimeCheck(View parentView, TextView showView, String checkDate,
                                IWheelDataChangeCallback iWheelDataChangeCallback) {
        if (parentView == null || showView == null) {
            return;
        }
        if (showView.getText() != null && !TextUtils.isEmpty(showView.getText().toString())
                && !TextUtils.equals("时间", showView.getText().toString())) {
            try {
                defaultCalendar.setTime(new SimpleDateFormat(timeFormat).parse(checkDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        DialogWheelTimeBean dialogWheelDateBean = new DialogWheelTimeBean();
        dialogWheelDateBean.setCanLessCurrent(true);
        dialogWheelDateBean.setTimeFormat(timeFormat);
        dialogWheelDateBean.setCalendar(defaultCalendar);
        TimeWheel dateWheel = new TimeWheel(mContext, null, dialogWheelDateBean);
        CardSelectPopWindow pw = new CardSelectPopWindow((Activity) mContext, parentView, dateWheel.getTimeView());
        pw.changeButtonColor(color);
        MyClickListener regionClickListener = new MyClickListener(pw, showView,
                iWheelDataChangeCallback);
        dateWheel.setWheelFinishListener(regionClickListener);
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
