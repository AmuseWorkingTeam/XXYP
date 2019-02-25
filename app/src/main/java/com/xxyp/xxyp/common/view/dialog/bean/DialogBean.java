package com.xxyp.xxyp.common.view.dialog.bean;

public class DialogBean {
    private String title;               //标题
    private String message;             //内容
    private String btnLeftContent;      //左按钮文本
    private String btnRightContent;     //右按钮文本
    private int btnRightTextColor;      //右按钮颜色
    private int btnLeftTextColor;       //左按钮颜色
    private boolean isNotCancel;        //点击弹框外部不能取消弹框
    private String hintContent;         //编辑框提示内容(不建议使用，暂时保留)
    private boolean isVersionUpdate;    //版本更新(不建议使用，暂时保留)

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBtnLeftContent() {
        return btnLeftContent;
    }

    public void setBtnLeftContent(String btnLeftContent) {
        this.btnLeftContent = btnLeftContent;
    }

    public String getBtnRightContent() {
        return btnRightContent;
    }

    public void setBtnRightContent(String btnRightContent) {
        this.btnRightContent = btnRightContent;
    }

    public int getBtnRightTextColor() {
        return btnRightTextColor;
    }

    public void setBtnRightTextColor(int btnRightTextColor) {
        this.btnRightTextColor = btnRightTextColor;
    }

    public int getBtnLeftTextColor() {
        return btnLeftTextColor;
    }

    public void setBtnLeftTextColor(int btnLeftTextColor) {
        this.btnLeftTextColor = btnLeftTextColor;
    }

    public boolean isNotCancel() {
        return isNotCancel;
    }

    public void setNotCancel(boolean notCancel) {
        isNotCancel = notCancel;
    }

    public String getHintContent() {
        return hintContent;
    }

    public void setHintContent(String hintContent) {
        this.hintContent = hintContent;
    }

    public boolean isVersionUpdate() {
        return isVersionUpdate;
    }

    public void setVersionUpdate(boolean versionUpdate) {
        isVersionUpdate = versionUpdate;
    }
}
