
package com.xxyp.xxyp.user.view.wheel;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;


import com.xxyp.xxyp.R;
import com.xxyp.xxyp.user.view.wheel.bean.DialogWheelDateBean;
import com.xxyp.xxyp.user.view.wheel.wheelAdapter.NumericWheelAdapter;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * Description : 显示日期的滚轮
 */
public class DateWheel implements OnWheelChangedListener, OnWheelScrollListener {
    /**
     * 可见item个数
     */
    public static final int VISIBLE_ITEM = 7;

    private int currentYear, currentMonth, currentDay;

    /**
     * 下面三个变量用于当年份大于最大值或小于最小值时，调整年月日的显示值
     */
    private boolean isYearReset = false;

    private boolean isLessYearReset = false;

    private boolean isReset = false;

    /**
     * 下面三个用于当年份小于等于最小值、月份小于等于最小值，或者年份大于等于最大值、月份大于等于最大值
     * 调整月日的显示值
     */
    private boolean isMonthLess = false;

    private boolean isMonthReset = false;

    private boolean isMonthMuch = false;

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
    private OnWheelFinishListener wheelFinishListener;

    private List<String> list_big;

    private List<String> list_little;

    //选中日期、起始日期、截止日期
    private Calendar chooseCalendar;
    private Calendar startCalendar;
    private Calendar endCalendar;

    //最大年/月/日
    private int maxYear, maxMonth, maxDay;
    //最小年/月/日
    private int minYear, minMonth, minDay;

    //用于当maxMonth与minMonth相同时，控制日的显示
    private boolean useLargeDay = false;

    //默认起始日期
    private static final int DEFAULT_START_YEAR = 1900;
    private static final int DEFAULT_START_MONTH = 1;
    private static final int DEFAULT_START_DAY = 1;

    //默认选中日期
    private static final int DEFAULT_CHOOSE_YEAR = 1990;
    private static final int DEFAULT_CHOOSE_MONTH = 6;
    private static final int DEFAULT_CHOOSE_DAY = 6;


    private DialogWheelDateBean wheelDateBean;

    public DateWheel(Context context) {
        this(context, null);
    }

    public DateWheel(Context context, View timeView) {
        this(context, timeView, null);
    }

    public DateWheel(Context context, View timeView, int year, int month, int day) {
        this.context = context;
        this.timeView = timeView;
        initViewData();
    }

    public DateWheel(Context context, View timeView, DialogWheelDateBean bean) {
        this.context = context;
        this.timeView = timeView;
        this.wheelDateBean = bean;
        initViewData();
    }

    private void initViewData() {
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        chooseCalendar = Calendar.getInstance();
        if (wheelDateBean == null) {
            dealNUllType();
        } else if (wheelDateBean.isTimeType()) {
            dealTimeType();
        } else if (wheelDateBean.isStrType()) {
            dealStrType();
        } else {
            dealDirType();
        }

        if (endCalendar.compareTo(startCalendar) < 0) {
            startCalendar.set(1900, 0, 1);
            endCalendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        }

        if (chooseCalendar.compareTo(startCalendar) < 0) {
            chooseCalendar = startCalendar;
        }

        if (chooseCalendar.compareTo(endCalendar) > 0) {
            chooseCalendar = endCalendar;
        }

        minYear = startCalendar.get(Calendar.YEAR);
        minMonth = startCalendar.get(Calendar.MONTH) + 1;
        minDay = startCalendar.get(Calendar.DAY_OF_MONTH);
        maxYear = endCalendar.get(Calendar.YEAR);
        maxMonth = endCalendar.get(Calendar.MONTH) + 1;
        maxDay = endCalendar.get(Calendar.DAY_OF_MONTH);

        initView(chooseCalendar.get(Calendar.YEAR), chooseCalendar.get(Calendar.MONTH) + 1,
                chooseCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void dealNUllType() {
        startCalendar.set(DEFAULT_START_YEAR, DEFAULT_START_MONTH - 1, DEFAULT_START_DAY);
        endCalendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        chooseCalendar.set(DEFAULT_CHOOSE_YEAR, DEFAULT_CHOOSE_MONTH - 1, DEFAULT_CHOOSE_DAY);
    }

    private void dealTimeType() {
        if (wheelDateBean.getTimeStartDate() != 0) {
            startCalendar.setTimeInMillis(wheelDateBean.getTimeStartDate());
        } else {
            startCalendar.set(DEFAULT_START_YEAR, DEFAULT_START_MONTH - 1, DEFAULT_START_DAY);
        }
        if (wheelDateBean.isEndDateNoLimit()) {
            //最大时间为10000-12-31
            endCalendar.set(10000, 12 - 1, 31);
        } else if (wheelDateBean.getTimeEndDate() != 0) {
            endCalendar.setTimeInMillis(wheelDateBean.getTimeEndDate());
        } else {
            endCalendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        }
        if (wheelDateBean.getTimeChooseDate() != 0) {
            chooseCalendar.setTimeInMillis(wheelDateBean.getTimeChooseDate());
        } else {
            chooseCalendar.set(DEFAULT_CHOOSE_YEAR, DEFAULT_CHOOSE_MONTH - 1, DEFAULT_CHOOSE_DAY);
        }
    }

    private void dealStrType() {
        try {
            if (!TextUtils.isEmpty(wheelDateBean.getStrStartDate())) {
                startCalendar.setTime(wheelDateBean.getStrDateFormat().parse(wheelDateBean.getStrStartDate()));
            } else {
                startCalendar.set(DEFAULT_START_YEAR, DEFAULT_START_MONTH - 1, DEFAULT_START_DAY);
            }
            if (wheelDateBean.isEndDateNoLimit()) {
                //最大时间为10000-12-31
                endCalendar.set(10000, 12 - 1, 31);
            } else if (!TextUtils.isEmpty(wheelDateBean.getStrEndDate())) {
                endCalendar.setTime(wheelDateBean.getStrDateFormat().parse(wheelDateBean.getStrEndDate()));
            } else {
                endCalendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
            }
            if (!TextUtils.isEmpty(wheelDateBean.getStrChooseDate())) {
                chooseCalendar.setTime(wheelDateBean.getStrDateFormat().parse(wheelDateBean.getStrChooseDate()));
            }
        } catch (ParseException e) {
            Log.e(DateWheel.class.getSimpleName(), "dealStrType: error");
            dealNUllType();
        }
    }

    private void dealDirType() {
        startCalendar.set(DEFAULT_START_YEAR, DEFAULT_START_MONTH - 1, DEFAULT_START_DAY);
        endCalendar.setTimeInMillis(Calendar.getInstance().getTimeInMillis());
        if (wheelDateBean.getYear() != 0 && wheelDateBean.getMonth() != 0 && wheelDateBean.getDay() != 0) {
            chooseCalendar.set(wheelDateBean.getYear(), wheelDateBean.getMonth(), wheelDateBean.getDay());
        } else {
            chooseCalendar.set(DEFAULT_CHOOSE_YEAR, DEFAULT_CHOOSE_MONTH - 1, DEFAULT_CHOOSE_DAY);
        }
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
     * 初始化组件
     *
     * @param year  要显示年份
     * @param month 要显示月
     * @param day   要显示日
     */
    private void initView(int year, int month, int day) {
        int END_YEAR;
        // 如果没有设置过日期，显示当前日期
        if (day == 0 || month == 0 || year == 0) {
            //默认时间为1990年 6月 6号
            year = 1990;
            month = 6;
            day = 6;
        }
        END_YEAR = 10000;
        // END_YEAR = calendar.get(Calendar.YEAR);
        if (timeView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            timeView = inflater.inflate(R.layout.tc_wheel_timepicker_layout, null);
        }
        wv_year = (WheelView) timeView.findViewById(R.id.year);
        wv_month = (WheelView) timeView.findViewById(R.id.month);
        wv_day = (WheelView) timeView.findViewById(R.id.day);

        wv_year.setExtra(0);
        wv_month.setExtra(1);
        wv_day.setExtra(2);

        wv_year.setDrawShadows(false);
        wv_month.setDrawShadows(false);
        wv_day.setDrawShadows(false);

        // 年
        NumericWheelAdapter yearAdapter = new NumericWheelAdapter(context, START_YEAR, END_YEAR, context.getString(R.string.view_date_year));
        wv_year.setAdapter(yearAdapter);// 设置"年"的显示数据
        wv_year.setCyclic(false);// 可循环滚动
        wv_year.setVisibleItems(VISIBLE_ITEM);
        wv_year.setCurrentItem(year - START_YEAR);// 初始化时显示的数据
        if (yearVisible && monthVisible && dayVisible) {
            initData(year, month, day);
        }
        if (yearVisible && monthVisible && !dayVisible) {
            initData(0, month, 0);
        }
        initListener();
    }

    /**
     * 初始化数据
     */
    private void initData(int year, int month, int day) {
        // 添加大小月月份并将其转换为list,方便之后的判断
        String[] months_big = {"1", "3", "5", "7", "8", "10", "12"};
        String[] months_little = {"4", "6", "9", "11"};
        list_big = Arrays.asList(months_big);
        list_little = Arrays.asList(months_little);
        initMonth(month);
        if (year > 0 && day > 0) {
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
                else wv_day.setAdapter(dayAdapter_28);
            }
            wv_day.setCurrentItem(day - 1);
        }
    }

    /**
     * 初始化月份
     *
     * @param month 月
     */
    private void initMonth(int month) {
        // 月
        NumericWheelAdapter monthAdapter = new NumericWheelAdapter(context, 1, 12, context.getString(R.string.view_date_month));
        wv_month.setAdapter(monthAdapter);
        wv_month.setCyclic(true);
        wv_month.setVisibleItems(VISIBLE_ITEM);
        wv_month.setCurrentItem(month - 1);
    }

    /**
     * 初始化日期
     */
    private void initDay() {
        // 日
        String str = context.getString(R.string.view_date_day);
        dayAdapter_31 = new NumericWheelAdapter(context, 1, 31, str);
        dayAdapter_30 = new NumericWheelAdapter(context, 1, 30, str);
        dayAdapter_29 = new NumericWheelAdapter(context, 1, 29, str);
        dayAdapter_28 = new NumericWheelAdapter(context, 1, 28, str);
        wv_day.setCyclic(true);
        wv_day.setVisibleItems(VISIBLE_ITEM);
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

    /**
     * @param wheel    the wheel view whose state has changed
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
                    if (wv_day.getCurrentItem() >= 30) {
                        wv_day.setCurrentItem(29);
                    }
                    wv_day.setAdapter(dayAdapter_30);
                } else {
                    if ((year_num % 4 == 0 && year_num % 100 != 0) || year_num % 400 == 0) {
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
        if (wheelFinishListener != null && yearVisible && !monthVisible && !dayVisible) {
            wheelFinishListener.onChange(getYear());
        }
        if (wheelFinishListener != null && yearVisible && monthVisible && !dayVisible) {
            wheelFinishListener.onChange(getYear() + "-" + getMonth());
        }
        if (wheelFinishListener != null && yearVisible && monthVisible && dayVisible) {
            wheelFinishListener.onChange(getYear() + "-" + getMonth() + "-" + getDay());
        }
    }

    @Override
    public void onScrollingStarted(WheelView wheel) {

    }

    @Override
    public void onScrollingJustify(WheelView wheel) {
        if (wheel.getId() == R.id.year) {
            isReset = false;
            isLessYearReset = false;
            isYearReset = false;
            if (wheel.getCurrentItem() < minYear) {
                isLessYearReset = true;
                isYearReset = true;
                wheel.setCurrentItem(minYear, true);
            } else if (wheel.getCurrentItem() + START_YEAR >= maxYear) {
                isYearReset = true;
                wheel.setCurrentItem(maxYear - START_YEAR, true);
            }
            onScrollingJustify(wv_month);
        } else if (wheel.getId() == R.id.month) {
            isMonthReset = false;
            isMonthLess = false;
            isMonthMuch = false;
            if (isLessYearReset && !isReset) {
                wv_month.setCurrentItem(minMonth - 1, true);
            } else if (isYearReset && !isReset) {
                wv_month.setCurrentItem(maxMonth - 1, true);
            } else if (wv_year.getCurrentItem() + START_YEAR <= minYear && (wheel.getCurrentItem() + 1) <= minMonth) {
                if (useLargeDay) {
                    isMonthMuch = true;
                    wheel.setCurrentItem(maxMonth - 1, true);
                } else {
                    isMonthLess = true;
                    wheel.setCurrentItem(minMonth - 1, true);
                }
            } else if (wv_year.getCurrentItem() + START_YEAR >= maxYear && (wheel.getCurrentItem() + 1) >= maxMonth) {
                isMonthMuch = true;
                useLargeDay = maxMonth == minMonth ? true : false;
                ;
                wheel.setCurrentItem(maxMonth - 1, true);
            }
            onScrollingJustify(wv_day);
        } else if (wheel.getId() == R.id.day) {
            if (isLessYearReset) {
                wv_day.setCurrentItem(minDay - 1, true);
            } else if (isYearReset && !isReset) {
                wv_day.setCurrentItem(maxDay - 1, true);
            } else if (isMonthLess && !isMonthReset && wv_day.getCurrentItem() + 1 < minDay) {
                wv_day.setCurrentItem(minDay - 1, true);
            } else if (isMonthMuch && !isMonthReset && wv_day.getCurrentItem() + 1 > maxDay) {
                wv_day.setCurrentItem(maxDay - 1, true);
            }
        }
    }

    @Override
    public void onScrollingFinished(WheelView wheel) {
        // 有时位移有偏差，导致首次定位不准确
        if (wheel.getId() == R.id.year) {
            Log.d("maolin", "onScrollingFinished: " + System.currentTimeMillis());
            if (isLessYearReset) {
                if (wheel.getCurrentItem() != minYear) {
                    wheel.setCurrentItem(minYear, true);
                } else {
                    isLessYearReset = false;
                    isYearReset = false;
                }
            } else {
                if (isYearReset) {
                    if (wheel.getCurrentItem() + START_YEAR != maxYear) {
                        wheel.setCurrentItem(maxYear - START_YEAR, true);
                    }
                }
            }
            isReset = true;
        } else if (wheel.getId() == R.id.month) {
            if (isMonthLess && wheel.getCurrentItem() + 1 != minMonth) {
                wheel.setCurrentItem(minMonth - 1, true);
            } else if (isMonthMuch && wheel.getCurrentItem() + 1 != maxMonth) {
                wheel.setCurrentItem(maxMonth - 1, true);
            } else {
                useLargeDay = false;
            }
            isMonthReset = true;
        } else if (wheel.getId() == R.id.day) {
            if (wv_year.getCurrentItem() + START_YEAR >= maxYear
                    && wv_month.getCurrentItem() + 1 >= maxMonth
                    && wv_day.getCurrentItem() + 1 > maxDay) {
                wheel.setCurrentItem(maxDay - 1, true);
            } else if (wv_year.getCurrentItem() + START_YEAR <= minYear
                    && wv_month.getCurrentItem() + 1 <= minMonth
                    && wv_day.getCurrentItem() + 1 < minDay) {
                wheel.setCurrentItem(minDay - 1, true);
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

    public String getCurrentTime() {
        return "" + maxYear + "-" + (maxMonth < 10 ? "0" + maxMonth : maxMonth) + "-" +
                (maxDay < 10 ? "0" + maxDay : maxDay);
    }

    public String getMinTime() {
        return "" + minYear + "-" + (minMonth < 10 ? "0" + minMonth : minMonth) + "-" +
                (minDay < 10 ? "0" + minDay : minDay);
    }
}
