
package com.xxyp.xxyp.user.view.wheel;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.xxyp.xxyp.user.view.wheel.wheelAdapter.ListWheelAdapter;

import java.util.List;

/**
 * Description : 单个选择
 * Created by wxh on 2016/6/20 10:53.
 * Job number：138746
 * Phone ：15233620521
 * Email：wangxiaohui@syswin.com
 * Person in charge : 王晓辉
 * Leader：王晓辉
 */
public class SingleSelectView implements OnWheelChangedListener {
    /**
     * 可见item个数
     */
    public static final int VISIBLE_ITEM = 7;

    private int currentPosition = 0;

    private List<String> items;

    private WheelView wheelView;

    private ListWheelAdapter adapter;

    /**
     * 滑动停止时监听
     */
    private OnWheelFinisheListener wheelFinisheListener;

    public SingleSelectView(Context context, List<String> items) {
        this(context, null, items, 0);
    }

    public SingleSelectView(Context context, List<String> items, int position) {
        this(context, null, items, position);
    }

    public SingleSelectView(Context context, View view, List<String> items, int position) {
        this.items = items;
        this.currentPosition = position;
        initView(context, view);
        initListener();
    }

    private void initView(Context context, View view) {
        if (this.items != null && currentPosition >= this.items.size()) {
            currentPosition = 0;
        }
        if (currentPosition <= 0) {
            currentPosition = 0;
        }
        if (items == null || items.size() == 0)
            throw new IllegalArgumentException("items Can not be empty !");
        if (view == null) {
            wheelView = new WheelView(context);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            wheelView.setLayoutParams(params);
        }
        if (adapter == null) {
            adapter = new ListWheelAdapter(context, items);
        } else {
            adapter.setItems(items);
        }
        wheelView.setAdapter(adapter);
        wheelView.setVisibleItems(VISIBLE_ITEM);
        wheelView.setCyclic(false);// 不可循环滚动
        wheelView.setCurrentItem(currentPosition);
    }

    public View getView() {
        return wheelView;
    }

    /**
     * 设置滑动结束监听
     *
     * @param wheelFinisheListener 监听
     */
    public void setWheelFinisheListener(OnWheelFinisheListener wheelFinisheListener) {
        if (this.wheelFinisheListener == null) {
            this.wheelFinisheListener = wheelFinisheListener;
        }
        wheelFinisheListener.OnChange(items.get(currentPosition), currentPosition);
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        wheelView.addChangingListener(this);
    }

    public interface OnWheelFinisheListener {
        /**
         * 滑动停止后，当前区域
         *
         * @param currentValue 当前区域
         */
        void OnChange(String currentValue, int currentIndex);
    }

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheelFinisheListener != null) {
            wheelFinisheListener.OnChange(items.get(newValue), newValue);
        }
    }
}
