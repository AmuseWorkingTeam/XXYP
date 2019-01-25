
package com.xxyp.xxyp.user.view.wheel;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;


import com.xxyp.xxyp.R;
import com.xxyp.xxyp.user.view.wheel.wheelAdapter.NumericWheelAdapter;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Description : 显示日期的滚轮
 * Created by wxh on 2015/10/26 10:51.
 * Job number：138746
 * Phone ：15233620521
 * Email：wangxiaohui@syswin.com
 * Person in charge : 王晓辉
 * Leader：王晓辉
 */
public class DateWheel implements OnWheelChangedListener, OnWheelScrollListener {
    /**
     * 可见item个数
     */
    public static final int VISIBLE_ITEM = 7;

    private int currentYear, currentMonth, currentDay;

    private boolean isYearReset = false;

    private boolean isLessYearReset = false;

    private boolean isReset = false;

    private Context context;

    private View timeView;

    private int START_YEAR = 0;

    private int MIN_YEAR = 1900;

    // 年月日可见性
    private boolean yearVisible = true, monthVisible = true, dayVisible = true;

    // 年view
    private WheelView wv_year;

    // 月view
    private WheelView wv_month;

    // 日view
    private WheelView wv_day;

    private NumericWheelAdapter dayAdapter_31;

    private NumericWheelAdapter dayAdapter_30;

    private NumericWheelAdapter dayAdapter_29;

    private NumericWheelAdapter dayAdapter_28;

    /**
     * 滑动结束监听
     */
    private OnWheelFinishListener wheelFinisheListener;

    private List<String> list_big;

    private List<String> list_little;

    public DateWheel(Context context, View timeView, int year, int month, int day) {
        this.context = context;
        this.timeView = timeView;
        initView(year, month, day);
    }

    public DateWheel(Context context) {
        this(context, null);
    }

    public DateWheel(Context context, View timeView) {
        this(context, timeView, 0, 0, 0);
    }

    public void setYearVisible(boolean yearVisible) {
        this.yearVisible = yearVisible;
        if (!yearVisible && wv_year != null) {
            wv_year.setVisibility(View.GONE);
        }
    }

    public void setMonthVisible(boolean monthVisible) {
        this.monthVisible = monthVisible;
        if (!monthVisible && wv_month != null) {
            wv_month.setVisibility(View.GONE);
        }
    }

    public void setDayVisible(boolean dayVisible) {
        this.dayVisible = dayVisible;
        if (!dayVisible && wv_day != null) {
            wv_day.setVisibility(View.GONE);
        }
    }

    public View getTimeView() {
        return timeView;
    }

    /**
     * 初始化组件
     *
     * @param year 要显示年份
     * @param month 要显示月
     * @param day 要显示日
     */
    private void initView(int year, int month, int day) {
        int END_YEAR;
        Calendar calendar = Calendar.getInstance();
        this.currentYear = calendar.get(Calendar.YEAR);
        this.currentMonth = calendar.get(Calendar.MONTH) + 1;
        this.currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        // 如果没有设置过日期，显示当前日期
        if (day == 0 || month == 0 || year == 0) {
            year = currentYear;
            month = currentMonth;
            day = currentDay;
        }
        END_YEAR = 10000;
        // END_YEAR = calendar.get(Calendar.YEAR);
        if (timeView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            timeView = inflater.inflate(R.layout.tc_wheel_timepicker_layout, null);
        }
        wv_year = (WheelView)timeView.findViewById(R.id.year);
        wv_month = (WheelView)timeView.findViewById(R.id.month);
        wv_day = (WheelView)timeView.findViewById(R.id.day);
        // 年
        NumericWheelAdapter yearAdapter = new NumericWheelAdapter(context, START_YEAR, END_YEAR);
        wv_year.setAdapter(yearAdapter);// 设置"年"的显示数据
        wv_year.setCyclic(false);// 可循环滚动
        wv_year.setVisibleItems(VISIBLE_ITEM);
        wv_year.setLabel("年");
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
        if (yearVisible && monthVisible && dayVisible) {
            initData(year, month, day);
        }
        if (yearVisible && monthVisible && !dayVisible) {
            initData(month);
        }
        initListener();
    }

    /**
     * 初始化月份
     *
     * @param month 月
     */
    private void initMonth(int month) {
        // 月
        NumericWheelAdapter monthAdapter = new NumericWheelAdapter(context, 1, 12);
        wv_month.setAdapter(monthAdapter);
        wv_month.setCyclic(true);
        wv_month.setVisibleItems(VISIBLE_ITEM);
        // wv_month.setRadian(WheelView.DEFAULT_RADIAN);
        wv_month.setLabel("月");
        wv_month.setCurrentItem(month - 1);
    }

    /**
     * 初始化日期
     */
    private void initDay() {
        // 日
        dayAdapter_31 = new NumericWheelAdapter(context, 1, 31);
        dayAdapter_30 = new NumericWheelAdapter(context, 1, 30);
        dayAdapter_29 = new NumericWheelAdapter(context, 1, 29);
        dayAdapter_28 = new NumericWheelAdapter(context, 1, 28);
        wv_day.setCyclic(true);
        wv_day.setVisibleItems(VISIBLE_ITEM);
        wv_day.setLabel("日");
    }

    /**
     * 初始化数据
     */
    private void initData(int month) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {
                "1", "3", "5", "7", "8", "10", "12"
        };
        String[] months_little = {
                "4", "6", "9", "11"
        };
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        initMonth(month);
    }

    /**
     * 初始化数据
     */
    private void initData(int year, int month, int day) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {
                "1", "3", "5", "7", "8", "10", "12"
        };
        String[] months_little = {
                "4", "6", "9", "11"
        };
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        initMonth(month);
        initDay();
        // 判断大小月及是否闰年,用来确定"日"的数据
        if (list_big.contains(String.valueOf(month))) {
            wv_day.setAdapter(dayAdapter_31);
        } else if (list_little.contains(String.valueOf(month))) {
            wv_day.setAdapter(dayAdapter_30);
        } else {
            // 闰年
            if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
                wv_day.setAdapter(dayAdapter_29);
            else
                wv_day.setAdapter(dayAdapter_28);
        }
        wv_day.setCurrentItem(day - 1);
    }

    /**
     * 初始化监听:如果只显示年份，则没有滑动监听和月日监听
     */
    private void initListener() {
        if (yearVisible && monthVisible && dayVisible) {
            wv_year.addScrollingListener(this);
            wv_month.addScrollingListener(this);
            wv_day.addScrollingListener(this);
            wv_month.addChangingListener(this);
            wv_day.addChangingListener(this);
        }
        if (yearVisible && monthVisible && !dayVisible) {
            wv_year.addScrollingListener(this);
            wv_month.addChangingListener(this);
        }
        wv_year.addChangingListener(this);
    }

    public String getYear() {
        return wv_year.getCurrentItem() + START_YEAR + "";
    }

    public String getMonth() {
        return wv_month.getCurrentItem() + 1 < 10 ? "0" + (wv_month.getCurrentItem() + 1)
                : wv_month.getCurrentItem() + 1 + "";
    }

    public String getDay() {
        return wv_day.getCurrentItem() + 1 < 10 ? "0" + (wv_day.getCurrentItem() + 1)
                : wv_day.getCurrentItem() + 1 + "";
    }

    /**
     * @param wheel the wheel view whose state has changed
     * @param oldValue the old value of current item
     * @param newValue the new value of current item
     */
    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (yearVisible && monthVisible && dayVisible) {
            if (wheel == wv_year) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(dayAdapter_31);
                } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(dayAdapter_30);
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                        wv_day.setAdapter(dayAdapter_29);
                    else
                        wv_day.setAdapter(dayAdapter_28);
                }
            } else if (wheel == wv_month) {
                int month_num = newValue + 1;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(month_num))) {
                    wv_day.setAdapter(dayAdapter_31);
                } else if (list_little.contains(String.valueOf(month_num))) {
                    if (wv_day.getCurrentItem() >= 30) {
                        wv_day.setCurrentItem(29);
                    }
                    wv_day.setAdapter(dayAdapter_30);
                } else {
                    if (((wv_year.getCurrentItem() + START_YEAR) % 4 == 0
                            && (wv_year.getCurrentItem() + START_YEAR) % 100 != 0)
                            || (wv_year.getCurrentItem() + START_YEAR) % 400 == 0) {
                        if (wv_day.getCurrentItem() >= 29) {
                            wv_day.setCurrentItem(28);
                        }
                        wv_day.setAdapter(dayAdapter_29);
                    } else {
                        if (wv_day.getCurrentItem() >= 28) {
                            wv_day.setCurrentItem(27);
                        }
                        wv_day.setAdapter(dayAdapter_28);
                    }
                }
            }
        }
        if (yearVisible && monthVisible && !dayVisible) {
            if (wheel == wv_year) {
                int year_num = newValue + START_YEAR;
                // 判断大小月及是否闰年,用来确定"日"的数据
                if (list_big.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(dayAdapter_31);
                } else if (list_little.contains(String.valueOf(wv_month.getCurrentItem() + 1))) {
                    wv_day.setAdapter(dayAdapter_30);
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0)
                        wv_day.setAdapter(dayAdapter_29);
                    else
                        wv_day.setAdapter(dayAdapter_28);
                }
            }
        }
        if (wheelFinisheListener != null && yearVisible && !monthVisible && !dayVisible) {
            wheelFinisheListener.onChange(getYear());
        }
        if (wheelFinisheListener != null && yearVisible && monthVisible && !dayVisible) {
            wheelFinisheListener.onChange(getYear() + "-" + getMonth());
        }
        if (wheelFinisheListener != null && yearVisible && monthVisible && dayVisible) {
            wheelFinisheListener.onChange(getYear() + "-" + getMonth() + "-" + getDay());
        }
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingJustify(WheelView wheel) {
        if(wheel.getId() == R.id.year){
            isReset = false;
            isLessYearReset = false;
            isYearReset = false;
            if (wheel.getCurrentItem() < MIN_YEAR) {
                isLessYearReset = true;
                isYearReset = true;
                wheel.setCurrentItem(MIN_YEAR, true);
            } else if (wheel.getCurrentItem() + START_YEAR >= currentYear) {
                isYearReset = true;
                wheel.setCurrentItem(currentYear - START_YEAR, true);
            }
            onScrollingJustify(wv_month);
        }else if(wheel.getId() == R.id.month){
            if (isLessYearReset) {
                wv_month.setCurrentItem(0, true);
            } else {
                if (isYearReset && !isReset) {
                    wv_month.setCurrentItem(currentMonth - 1, true);
                }
            }
            onScrollingJustify(wv_day);
        }else if(wheel.getId() == R.id.day){
            if (isLessYearReset) {
                wv_day.setCurrentItem(0, true);
            } else {
                if (isYearReset && !isReset) {
                    wv_day.setCurrentItem(currentDay - 1, true);
                }
            }
        }
    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        // 有时位移有偏差，导致首次定位不准确
        if(wheel.getId() == R.id.year){
            if (isLessYearReset) {
                if (wheel.getCurrentItem() != MIN_YEAR) {
                    wheel.setCurrentItem(MIN_YEAR, true);
                } else {
                    isLessYearReset = false;
                    isYearReset = false;
                }
            } else {
                if (isYearReset) {
                    if (wheel.getCurrentItem() + START_YEAR != currentYear) {
                        wheel.setCurrentItem(currentYear - START_YEAR, true);
                    }
                }
            }
            isReset = true;
        }else if(wheel.getId() == R.id.month){
            if (wv_year.getCurrentItem() + START_YEAR >= currentYear
                    && wheel.getCurrentItem() + 1 > currentMonth) {
                wheel.setCurrentItem(currentMonth - 1, true);
            }
        }else if(wheel.getId() == R.id.day){
            if (wv_year.getCurrentItem() + START_YEAR >= currentYear
                    && wv_month.getCurrentItem() + 1 >= currentMonth
                    && wv_day.getCurrentItem() + 1 > currentDay) {
                wheel.setCurrentItem(currentDay - 1, true);
            }
        }


        if (wheelFinisheListener != null) {
            if (yearVisible && monthVisible && dayVisible) {
                wheelFinisheListener.onChange(getYear() + "-" + getMonth() + "-" + getDay());
            }
            if (yearVisible && monthVisible && !dayVisible) {
                wheelFinisheListener.onChange(getYear() + "-" + getMonth());
            }
            if (yearVisible && !monthVisible && !dayVisible) {
                wheelFinisheListener.onChange(getYear());
            }

        }
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
        if (this.wheelFinisheListener == null) {
            this.wheelFinisheListener = wheelFinishListener;
        }
        if (yearVisible && monthVisible && dayVisible) {
            wheelFinishListener.onChange(getYear() + "-" + getMonth() + "-" + getDay());
        }
        if (yearVisible && monthVisible && !dayVisible) {
            wheelFinishListener.onChange(getYear() + "-" + getMonth());
        }
        if (yearVisible && !monthVisible && !dayVisible) {
            wheelFinishListener.onChange(getYear());
        }

    }
}
