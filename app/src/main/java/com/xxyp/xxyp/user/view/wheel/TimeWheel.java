
package com.xxyp.xxyp.user.view.wheel;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;


import com.xxyp.xxyp.R;
import com.xxyp.xxyp.user.view.wheel.bean.DialogWheelTimeBean;
import com.xxyp.xxyp.user.view.wheel.wheelAdapter.NumericWheelAdapter;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Description : 显示日期的滚轮
 * Created by wxh on 2015/10/26 10:51.
 * Job number：138746
 * Phone ：15233620521
 * Email：wangxiaohui@syswin.com
 * Person in charge : 王晓辉
 * Leader：王晓辉
 */
public class TimeWheel implements OnWheelChangedListener, OnWheelScrollListener {
    /**
     * 可见item个数
     */
    public static final int VISIBLE_ITEM = 7;

    private int showHour, showMinute; //显示的小时 设置的分钟

    private int setMinHour, setMinMinute; //设置的可选择的最小小时，分钟

    private Context context;

    private View timeView;

    // 年月日可见性
    private boolean hourVisible = true, minuteVisible = true;

    private boolean isCanLessCurrent = true;

    private boolean isHourOver;

    private boolean isReset;

    // 时间view
    private WheelView wvHour;

    // 分钟view
    private WheelView wvMinute;

    /**
     * 滑动结束监听
     */
    private OnWheelFinishListener wheelFinishListener;

    private String timeFormat;

    private Calendar calendar;

    public TimeWheel(Context context, View timeView, DialogWheelTimeBean beanNew) {
        this.context = context;
        this.timeView = timeView;
        if (beanNew != null) {
            if (!TextUtils.isEmpty(beanNew.getTimeFormat())) {
                this.timeFormat = beanNew.getTimeFormat();
            }
            if (beanNew.getMinHour() == null || beanNew.getMinMinute() == null) {
                calendar = Calendar.getInstance();
                setMinHour = calendar.get(Calendar.HOUR_OF_DAY);
                setMinMinute = calendar.get(Calendar.MINUTE);
            } else {
                setMinHour = beanNew.getMinHour();
                setMinMinute = beanNew.getMinMinute();
            }

            if (beanNew.getCalendar() != null) {
                calendar = beanNew.getCalendar();
                showHour = calendar.get(Calendar.HOUR_OF_DAY);
                showMinute = calendar.get(Calendar.MINUTE);
            } else {
                if (beanNew.getHour() == null || beanNew.getMinute() == null) {
                    //如果不设置当前时间，则默认我系统时间
                    if (calendar == null) {
                        calendar = Calendar.getInstance();
                    }
                    showHour = calendar.get(Calendar.HOUR_OF_DAY);
                    showMinute = calendar.get(Calendar.MINUTE);
                } else {
                    showHour = beanNew.getHour();
                    showMinute = beanNew.getMinute();
                }
            }
        } else {
            //如果传入为空则，显示时间，最小时间全部为 系统当前时间
            Calendar calendar = Calendar.getInstance();
            setMinHour = calendar.get(Calendar.HOUR_OF_DAY);
            setMinMinute = calendar.get(Calendar.MINUTE);
            showHour = setMinHour;
            showMinute = setMinMinute;
        }
        initView();

    }

    public TimeWheel(Context context) {
        this(context, null);
    }

    public TimeWheel(Context context, View timeView) {
        //默认时间为00:00
        this(context, timeView, null);
    }

    public void setYearVisible(boolean yearVisible) {
        this.hourVisible = yearVisible;
        if (!yearVisible && wvHour != null) {
            wvHour.setVisibility(View.GONE);
        }
    }

    public void setMonthVisible(boolean monthVisible) {
        this.minuteVisible = monthVisible;
        if (!monthVisible && wvMinute != null) {
            wvMinute.setVisibility(View.GONE);
        }
    }


    public View getTimeView() {
        return timeView;
    }

    public String getHour() {
        return wvHour.getCurrentItem() >= 10 ? wvHour.getCurrentItem() + "" : "0" + wvHour.getCurrentItem();
    }

    public String getMinute() {
        return wvMinute.getCurrentItem() >= 10 ? wvMinute.getCurrentItem() + "" : "0" + wvMinute.getCurrentItem();
    }


    /**
     * 初始化组件
     */
    private void initView() {
        //可选择的最小时间与显示时间一致
        if (setMinHour - showHour == 0) {
            if (setMinMinute - showMinute > 0) {
                showMinute = setMinMinute;
            }
        } else if (setMinHour - showHour > 0) {//可选择的最小时间大于显示时间，则显示时间为，最小时间
            showHour = setMinHour;
            showMinute = setMinMinute;
        }

        if (timeView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            timeView = inflater.inflate(R.layout.tc_wheel_time_layout, null);
        }
        wvHour = (WheelView) timeView.findViewById(R.id.hour);
        wvMinute = (WheelView) timeView.findViewById(R.id.minute);

        wvHour.setDrawShadows(false);
        wvMinute.setDrawShadows(false);

        // 年
        NumericWheelAdapter hourAdapter = new NumericWheelAdapter(context, 0, 23, "");
        hourAdapter.isFrontAddZero(true);
        wvHour.setAdapter(hourAdapter);// 设置"年"的显示数据
        wvHour.setCyclic(false);// 可循环滚动
        wvHour.setVisibleItems(VISIBLE_ITEM);
        wvHour.setCurrentItem(showHour);// 初始化时显示的数据
        initMinute(showMinute);
        initListener();
    }

    /**
     * 初始分钟
     *
     * @param minute 月
     */
    private void initMinute(int minute) {
        NumericWheelAdapter minuteAdapter = new NumericWheelAdapter(context, 0, 59, "");
        minuteAdapter.isFrontAddZero(true);
        wvMinute.setAdapter(minuteAdapter);
        wvMinute.setCyclic(true);
        wvMinute.setVisibleItems(VISIBLE_ITEM);
        wvMinute.setCurrentItem(minute);
    }


    /**
     * 初始化监听:如果只显示年份，则没有滑动监听和月日监听
     */
    private void initListener() {
        if (hourVisible && minuteVisible) {
            wvHour.addScrollingListener(this);
            wvMinute.addScrollingListener(this);
            wvMinute.addChangingListener(this);
        }
        wvHour.addChangingListener(this);

    }


    public void isCanLessCurrent(boolean isCanLessCurrent) {
        this.isCanLessCurrent = isCanLessCurrent;
    }

    /**
     * @param wheel    the wheel view whose state has changed
     * @param oldValue the old value of current item
     * @param newValue the new value of current item
     */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheelFinishListener != null && hourVisible && !minuteVisible) {
            wheelFinishListener.onChange(getHour());
        }
        if (wheelFinishListener != null && hourVisible && minuteVisible) {
            wheelFinishListener.onChange(getCurrentTime());
        }
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingJustify(WheelView wheel) {
        if (wheel.getId() == R.id.hour) {
            isHourOver = false;
            isReset = false;
            if (wheel.getCurrentItem() < 0) {
                wheel.setCurrentItem(0, true);
            } else if (wheel.getCurrentItem() <= setMinHour && !isCanLessCurrent) {
                isHourOver = true;
                wheel.setCurrentItem(setMinHour, true);
            }
            onScrollingJustify(wvMinute);
        } else if (wheel.getId() == R.id.minute) {
            if (wheel.getCurrentItem() < setMinMinute && !isCanLessCurrent && isHourOver & !isReset) {
                wvMinute.setCurrentItem(setMinMinute, true);
            }
        }
    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        // 有时位移有偏差，导致首次定位不准确
        if (wheel.getId() == R.id.hour) {
            if (wheel.getCurrentItem() < 0) {
                wheel.setCurrentItem(0, true);
            } else if (isHourOver && wheel.getCurrentItem() != setMinHour && !isCanLessCurrent) {
                wheel.setCurrentItem(setMinHour, true);
            }
            isReset = true;
        } else if (wheel.getId() == R.id.minute) {
            if (!isCanLessCurrent && Integer.parseInt(getHour()) <= setMinHour && wheel.getCurrentItem() < setMinMinute) {
                wvMinute.setCurrentItem(setMinMinute, true);
            }
        }
        changeContent();
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
        changeContent();
    }

    private void changeContent() {
        if (wheelFinishListener != null) {
            if (hourVisible && minuteVisible) {
                wheelFinishListener.onChange(getCurrentTime());
            }
            if (hourVisible && !minuteVisible) {
                wheelFinishListener.onChange(getHour());
            }
        }
    }

    public String getCurrentTime() {
        if (!TextUtils.isEmpty(timeFormat)) {
            if (calendar == null) {
                calendar = Calendar.getInstance();
            }
            calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(getHour()));
            calendar.set(Calendar.MINUTE, Integer.parseInt(getMinute()));
            return new SimpleDateFormat(timeFormat).format(calendar.getTime());
        }
        return getHour() + ":" + getMinute();
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public String getTimeFormat() {
        return timeFormat;
    }
}
