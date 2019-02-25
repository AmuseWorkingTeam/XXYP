package com.xxyp.xxyp.common.view.dialog.bean;

import java.util.List;
import java.util.Map;

public class DialogOperateBean {
    private List<String> list;              //存放操作的字符串集合
    private int position;                   //弹框位置：0 中间 1 底部
    private boolean isNotCancel;            //点击空白弹框是否不能取消
    private Map<Integer, String> decMap;    //描述文本;key对应list位置，value为描述语;(仅对需要的条目进行设置即可)
    private Map<Integer, Integer> colorMap;  //颜色;key对应list位置，value为颜色值;(仅对需要的条目进行设置即可)

    public List<String> getList() {
        return list;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Map<Integer, Integer> getColorMap() {
        return colorMap;
    }

    public void setColorMap(Map<Integer, Integer> colorMap) {
        this.colorMap = colorMap;
    }

    public Map<Integer, String> getDecMap() {
        return decMap;
    }

    public void setDecMap(Map<Integer, String> decMap) {
        this.decMap = decMap;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isNotCancel() {
        return isNotCancel;
    }

    public void setNotCancel(boolean notCancel) {
        isNotCancel = notCancel;
    }
}
