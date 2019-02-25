package com.xxyp.xxyp.common.view.dialog.bean;

import android.util.SparseArray;

import java.util.Observable;
import java.util.Observer;

/**
 * Description : 操作类弹窗，外部实时设置类
 * Created by wangtao on 2018/2/28.
 * Job number：600448
 * Email：wangtao@syswin.com
 * Person in charge : wangtao
 * Leader：wangyue
 */

public class OperateDialogConfig {
    private InnerObservable mObservable = new InnerObservable();
    private SparseArray<DialogItem> mItems = new SparseArray<>();

    public void addObserver(Observer o) {mObservable.addObserver(o);}

    public void deleteObservers() {mObservable.deleteObservers();}

    public void notifyObservers() {mObservable.notifyObservers(mItems);}

    public OperateDialogConfig configItem(int position, String name, boolean isEnable) {
        DialogItem item = mItems.get(position);
        if (item == null) {
            mItems.append(position, new DialogItem(name, isEnable));
        } else {
            item.name = name;
            item.isEnable = isEnable;
        }
        mObservable.setChanged();
        return this;
    }

    public static class DialogItem {
        public String name;
        public boolean isEnable;

        public DialogItem(String name, boolean isEnable) {
            this.name = name;
            this.isEnable = isEnable;
        }
    }

    static class InnerObservable extends Observable {
        @Override
        protected synchronized void setChanged() {
            super.setChanged();
        }
    }
}