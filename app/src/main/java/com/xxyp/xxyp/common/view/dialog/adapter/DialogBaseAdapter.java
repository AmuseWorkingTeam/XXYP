package com.xxyp.xxyp.common.view.dialog.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.dialog.bean.OperateDialogConfig;
import com.xxyp.xxyp.map.utils.ViewHolder;

import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import com.xxyp.xxyp.common.view.dialog.bean.OperateDialogConfig.*;

public class DialogBaseAdapter extends BaseAdapter implements Observer {

    private Context mContext;
    private List<String> mList;//操作列表
    private Map<Integer, Integer> mColors;//颜色
    private Map<Integer, String> mDecMap;//描述信息
    private SparseBooleanArray mIsEnables = new SparseBooleanArray();
    private int mPosition;//弹窗放置位置

    //中间弹框的子项距离上下的边距
    private int middle_margin_top = ScreenUtils.dp2px(12);
    private int middle_margin_bottom = ScreenUtils.dp2px(11);

    //底部弹框的子项距离上下的边距
    private int bottom_margin_top = ScreenUtils.dp2px(17);
    private int bottom_margin_bottom = ScreenUtils.dp2px(16);

    public DialogBaseAdapter(Context context, List<String> list, Map<Integer, String> decMap, Map<Integer, Integer> colors, int position) {
        mContext = context;
        this.mList = list;
        this.mColors = colors;
        this.mDecMap = decMap;
        this.mPosition = position;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.item_dialog_operate, viewGroup, false);
        }
        RelativeLayout textLayout = ViewHolder.get(view, R.id.dialog_operate_text_layout);
        TextView mOperateTv = ViewHolder.get(view, R.id.dialog_operateTv);
        TextView dialog_operate_dec = ViewHolder.get(view, R.id.dialog_operate_dec);
        View mDivider = ViewHolder.get(view, R.id.dialog_operator_divider);
        view.setEnabled(isEnabled(i));
        mOperateTv.setText(mList.get(i));

        //给对应的list子项文本设置颜色
        if (mColors != null && mColors.get(i) != null) {
            mOperateTv.setTextColor(mColors.get(i));
        } else {
            mOperateTv.setTextColor(mContext.getResources().getColor(R.color.c12));
        }

        //添加下划线
        if (i == (getCount() - 1)) {
            mDivider.setVisibility(View.GONE);
        } else {
            mDivider.setVisibility(View.VISIBLE);
        }

        //在对应的list子项添加描述信息
        if (mDecMap != null && mDecMap.get(i) != null) {
            dialog_operate_dec.setVisibility(View.VISIBLE);
            dialog_operate_dec.setText(mDecMap.get(i));
        } else {
            dialog_operate_dec.setVisibility(View.GONE);
        }

        //弹框子项距离上下边距
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) textLayout.getLayoutParams();
        if (mPosition == 0) { //弹框位置在中间
            params.topMargin = middle_margin_top;
            textLayout.setPadding(0, 0, 0, middle_margin_bottom); //有某机型设置bottomMargin不起作用故采用padding
            textLayout.setLayoutParams(params);
        } else if (mPosition == 1) {//弹框位置在底部
            params.topMargin = bottom_margin_top;
            textLayout.setLayoutParams(params);
            textLayout.setPadding(0, 0, 0, bottom_margin_bottom);
        }

        return view;
    }

    @Override
    public boolean isEnabled(int position) {
        return mIsEnables.get(position, true);
    }

    public void setList(List<String> list) {
        mList = list;
        notifyDataSetChanged();
    }

    @Override
    @SuppressWarnings("unchecked")
    public void update(Observable o, Object arg) {
        SparseArray<DialogItem> items = (SparseArray<DialogItem>) arg;
        boolean isChanged = false;
        for (int i = 0, size = items.size(); i < size; i++) {
            int key = items.keyAt(i);
            DialogItem value = items.valueAt(i);
            if (!mList.get(key).equals(value.name)) {
                mList.set(key, value.name);
                isChanged = true;
            }
            if (mIsEnables.get(key, true) != value.isEnable) {
                mIsEnables.put(key, value.isEnable);
                isChanged = true;
            }
        }
        if (isChanged) {
            notifyDataSetChanged();
        }
    }
}
