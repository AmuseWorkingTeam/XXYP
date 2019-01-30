package com.xxyp.xxyp.user.view.wheel.bean;

import java.util.Calendar;

/**
 * Description ：
 * Created by jingmaolin on 2018/5/18.
 * Job number：600029
 * Phone ：13342446520
 * Email：jingmaolin@syswin.com
 * Person in charge ： jingmaolin
 * Leader：wangyue
 */

public class DialogWheelTimeBean {
    private Integer hour;               //显示小时
    private Integer minute;              //显示分钟
    private boolean isCanLessCurrent ;    //可以选择的时间比设置的时间小
    private boolean isNotCancel;    //是否可以取消

    private Integer minHour;//设置最小时间
    private Integer minMinute ;//设置最小分钟

    private Calendar calendar ;

    private String timeFormat ; //时间格式 "HH:mm"

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public Integer getMinute() {
        return minute;
    }

    public void setMinute(Integer minute) {
        this.minute = minute;
    }


    public boolean isCanLessCurrent() {
        return isCanLessCurrent;
    }

    public void setCanLessCurrent(boolean canLessCurrent) {
        isCanLessCurrent = canLessCurrent;
    }

    public boolean isNotCancel() {
        return isNotCancel;
    }

    public void setNotCancel(boolean notCancel) {
        isNotCancel = notCancel;
    }

    public Integer getMinHour() {
        return minHour;
    }

    public void setMinHour(Integer minHour) {
        this.minHour = minHour;
    }

    public Integer getMinMinute() {
        return minMinute;
    }

    public void setMinMinute(Integer minMinute) {
        this.minMinute = minMinute;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public String getTimeFormat() {
        return timeFormat;
    }

    public void setTimeFormat(String timeFormat) {
        this.timeFormat = timeFormat;
    }
}
