
package com.xxyp.xxyp.user.view.wheel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


import com.xxyp.xxyp.R;
import com.xxyp.xxyp.user.view.wheel.wheelAdapter.TimeWheelAdapter;

import java.util.Calendar;

/**
 * Description : 时间滚动视图
 * Created by wxh on 2016/6/20 10:53.
 * Job number：138746
 * Phone ：15233620521
 * Email：wangxiaohui@syswin.com
 * Person in charge : 王晓辉
 * Leader：王晓辉
 */
public class TimeWheel implements OnWheelScrollListener {
    /**
     * 可见item个数
     */
    public static final int VISIBLE_ITEM = 7;

    private Context context;

    private View timeView;

    // 时view
    private WheelView wv_hour;

    // 分view
    private WheelView wv_minute;

    /**
     * 滑动结束监听
     */
    private OnWheelFinishListener wheelFinishListener;

    public TimeWheel(Context context, View timeView, int hour, int minute) {
        this.context = context;
        this.timeView = timeView;
        initView(hour, minute);
    }

    public TimeWheel(Context context) {
        this(context, null);
    }

    public TimeWheel(Context context, View timeView) {
        this(context, timeView, 0, 0);
    }

    public View getTimeView() {
        return timeView;
    }

    /**
     * 初始化组件
     */
    private void initView(int hour, int minute) {
        Calendar calendar = Calendar.getInstance();
        // 如果没有设置过日期，显示当前日期
        if (hour == 0 && minute == 0 ) {
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
        }
        if (timeView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            timeView = inflater.inflate(R.layout.tc_wheel_time_layout, null);
        }
        wv_hour = (WheelView) timeView.findViewById(R.id.hour);
        wv_hour.setBackgroundColor(context.getResources().getColor(R.color.c20));
        wv_minute = (WheelView) timeView.findViewById(R.id.minute);
        wv_minute.setBackgroundColor(context.getResources().getColor(R.color.c20));
        // 时
        TimeWheelAdapter hourAdapter = new TimeWheelAdapter(context, 0, 23);
        wv_hour.setAdapter(hourAdapter);// 设置"时"的显示数据
        wv_hour.setCyclic(false);// 可循环滚动
        wv_hour.setVisibleItems(VISIBLE_ITEM);
        wv_hour.setCurrentItem(hour);// 初始化时显示的数据

        // 分
        TimeWheelAdapter minuteAdapter = new TimeWheelAdapter(context, 0, 59);
        wv_minute.setAdapter(minuteAdapter);// 设置"分"的显示数据
        wv_minute.setCyclic(false);// 可循环滚动
        wv_minute.setVisibleItems(VISIBLE_ITEM);
        wv_minute.setCurrentItem(minute);// 初始化时显示的数据
        initListener();
    }

    private void initListener() {
        wv_hour.addScrollingListener(this);
        wv_minute.addScrollingListener(this);
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        if (wheelFinishListener != null) {
            wheelFinishListener.onChange(getHour() + ": " + getMinute());
        }
    }

    @Override
    public void onScrollingJustify(WheelView wheel) {

    }

    public String getHour() {
        return wv_hour.getCurrentItem() < 10 ? "0" + (wv_hour.getCurrentItem())
                : wv_hour.getCurrentItem()  + "";
    }

    public String getMinute() {
        return wv_minute.getCurrentItem() < 10 ? "0" + (wv_minute.getCurrentItem())
                : wv_minute.getCurrentItem() + "";
    }

    public interface OnWheelFinishListener {
        /**
         * 滑动停止后，当前时间
         *
         * @param currentValue 当前时间
         */
        void onChange(String currentValue);
    }

    /**
     * 设置监听
     *
     * @param wheelFinishListener 滑动结束监听
     */
    public void setWheelFinishListener(OnWheelFinishListener wheelFinishListener) {
        if (this.wheelFinishListener == null) {
            this.wheelFinishListener = wheelFinishListener;
        }
    }
}
