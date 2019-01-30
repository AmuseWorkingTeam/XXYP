package com.xxyp.xxyp.user.view.wheel.bean;

import java.text.SimpleDateFormat;

/**
 * Description ：
 * Created by jingmaolin on 2018/5/18.
 * Job number：600029
 * Phone ：13342446520
 * Email：jingmaolin@syswin.com
 * Person in charge ： jingmaolin
 * Leader：wangyue
 */

public class DialogWheelDateBean {
    /**
     * 日期弹框参数的传递方式有 isTimeType 、isStrType、isDirType三种方式
     */

    /**
     * 方式一：传时间戳
     */
    private boolean isTimeType = false;       // true 使用时间戳形式传值，false不使用
    private long timeChooseDate;              // 选中时间
    private long timeStartDate;               // 滚动最小可选择日期
    private long timeEndDate;                 // 滚动最大可选择日期

    /**
     * 方式二：传日期字符串
     */
    boolean isStrType = false;
    private String strStartDate;             //滚动最小可选择日期
    private String strEndDate;               //滚动最大可选择日期
    private String strChooseDate;            // 选中时间
    //startDate与endDate的日期格式，默认为“yyyy-MM-dd”
    private SimpleDateFormat strDateFormat = new SimpleDateFormat("yyyy-MM-dd");


    /**
     *  方式三：传 年，月 ，日；都不为0才有效，
     *  默认滚动截止日期为当前时间
     *  默认滚动开始日期为1900-1-1
     */
    private boolean isDirType = false;         //true 使用直接传值方式 ， false 不使用
    private int year;                        //选中年，下面以此类推
    private int month;
    private int day;


    private boolean endDateNoLimit;            //滚动最大可选择日期没有限制，默认false

    //点击空白区域弹框是否消失，默认消失
    private boolean isNotCancel;

    public boolean isTimeType() {
        return isTimeType;
    }

    public void setTimeType(boolean timeType) {
        isTimeType = timeType;
    }

    public long getTimeChooseDate() {
        return timeChooseDate;
    }

    public void setTimeChooseDate(long timeChooseDate) {
        this.timeChooseDate = timeChooseDate;
    }

    public long getTimeStartDate() {
        return timeStartDate;
    }

    public void setTimeStartDate(long timeStartDate) {
        this.timeStartDate = timeStartDate;
    }

    public long getTimeEndDate() {
        return timeEndDate;
    }

    public void setTimeEndDate(long timeEndDate) {
        this.timeEndDate = timeEndDate;
    }

    public boolean isStrType() {
        return isStrType;
    }

    public void setStrType(boolean strType) {
        isStrType = strType;
    }

    public String getStrStartDate() {
        return strStartDate;
    }

    public void setStrStartDate(String strStartDate) {
        this.strStartDate = strStartDate;
    }

    public String getStrEndDate() {
        return strEndDate;
    }

    public void setStrEndDate(String strEndDate) {
        this.strEndDate = strEndDate;
    }

    public String getStrChooseDate() {
        return strChooseDate;
    }

    public void setStrChooseDate(String strChooseDate) {
        this.strChooseDate = strChooseDate;
    }

    public boolean isDirType() {
        return isDirType;
    }

    public void setDirType(boolean dirType) {
        isDirType = dirType;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public SimpleDateFormat getStrDateFormat() {
        return strDateFormat;
    }

    public void setStrDateFormat(SimpleDateFormat strDateFormat) {
        this.strDateFormat = strDateFormat;
    }

    public boolean isEndDateNoLimit() {
        return endDateNoLimit;
    }

    public void setEndDateNoLimit(boolean endDateNoLimit) {
        this.endDateNoLimit = endDateNoLimit;
    }

    public boolean isNotCancel() {
        return isNotCancel;
    }

    public void setNotCancel(boolean notCancel) {
        isNotCancel = notCancel;
    }
}
