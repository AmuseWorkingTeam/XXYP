package com.xxyp.xxyp.common.view.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.AppContextUtils;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.dialog.adapter.DialogBaseAdapter;
import com.xxyp.xxyp.common.view.dialog.bean.DialogBean;
import com.xxyp.xxyp.common.view.dialog.bean.DialogOperateBean;
import com.xxyp.xxyp.user.view.wheel.DateWheel;
import com.xxyp.xxyp.user.view.wheel.OnWheelFinishListener;
import com.xxyp.xxyp.user.view.wheel.bean.DialogWheelDateBean;

public class CommonDialogView extends Dialog {
    /**
     * 普通Dialog弹窗样式
     */
    private TextView mTitle, mContent;   // 编辑标题，内容
    private EditText input;              // 输入框
    private TextView confirm;             // 确定
    private TextView cancel;              // 取消

    /**
     * 操作的Dialog弹窗样式
     */
    private ListView mListView;

    /**
     * Dialog样式
     */
    private int mType; //0 普通 ；1 操作 ；2 仿IOS弹框

    /**
     * 监听方法
     */
    private DialogViews_ask mAction_ask;

    private Context mContext;
    private boolean isNoDismissCallBack;  //dismiss方法是否产生回调
    private final int margin = (int) AppContextUtils.getAppContext().getResources().getDisplayMetrics().density * 20;   //距离边界20dp

    private String dateValue = ""; //日期值，格式类似 2018-5-18

    private String mCurrentTime = "";

    private String minTimeDate = "1900-01-01";

    /**
     * dialog普通弹框
     *
     * @param context  上下文
     * @param bean     DialogBean
     * @param listener 监听接口
     */
    public CommonDialogView(Context context, DialogBean bean, DialogViews_ask listener) {
        super(context, R.style.dialog_normal);
        createDialog(context, bean, listener);
    }

    /**
     * dialog操作弹窗（适用于显示在中间的弹框）
     *
     * @param context     上下文
     * @param operateBean DialogOperateBean
     * @param listener    监听接口
     */
    public CommonDialogView(Context context, DialogOperateBean operateBean, DialogViews_ask.DialogViews_askImpl listener) {
        super(context, R.style.dialog_normal);
        createOperateDialog(context, operateBean, listener);
    }

    /**
     * 初始化view
     */
    private void initView() {
        if (mType == 0) {
            // 编辑标题
            mTitle = (TextView) findViewById(R.id.tv_dialog_title);
            mContent = (TextView) findViewById(R.id.tv_dialog_content);
            confirm = (TextView) findViewById(R.id.tv_dialog_confirm);
            cancel = (TextView) findViewById(R.id.tv_dialog_cancel);
            input = (EditText) findViewById(R.id.et_dialog_input);
        } else if (mType == 1) {
            mListView = (ListView) findViewById(R.id.dialog_operate_listView);
        } else if (mType == 2) {
            confirm = (TextView) findViewById(R.id.wheel_confirm);
            cancel = (TextView) findViewById(R.id.wheel_cancel);
        }
    }

    /**
     * 显示普通Dialog弹框
     *
     * @param context          上下文
     * @param commonDialogBean DialogBean
     * @param listener         监听接口
     */
    private void createDialog(Context context, DialogBean commonDialogBean, DialogViews_ask listener) {
        if (commonDialogBean == null) {
            return;
        }

        mType = 0;
        this.mContext = context;
        setContentView(R.layout.common_dialog);
        this.mAction_ask = listener;
        initView();

        if (!TextUtils.isEmpty(commonDialogBean.getTitle())) {
            mTitle.setVisibility(View.VISIBLE);
            mTitle.setText(commonDialogBean.getTitle());
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mContent.getLayoutParams();
            params.topMargin = ScreenUtils.dp2px(20);
            mContent.setLayoutParams(params);
        }

        if (!TextUtils.isEmpty(commonDialogBean.getMessage())) {
            mContent.setVisibility(View.VISIBLE);
            mContent.setText(commonDialogBean.getMessage());
        } else {
            mContent.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(commonDialogBean.getHintContent())) {
            input.setVisibility(View.VISIBLE);
            input.setHint(commonDialogBean.getHintContent());
            input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        } else {
            input.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(commonDialogBean.getBtnRightContent())) {
            confirm.setVisibility(View.VISIBLE);
            confirm.setText(commonDialogBean.getBtnRightContent());
        } else {
            confirm.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(commonDialogBean.getBtnLeftContent())) {
            cancel.setVisibility(View.VISIBLE);
            cancel.setText(commonDialogBean.getBtnLeftContent());
        } else {
            cancel.setVisibility(View.GONE);
        }

        if (commonDialogBean.getBtnRightTextColor() != 0) {
            confirm.setTextColor(commonDialogBean.getBtnRightTextColor());
        }

        if (commonDialogBean.getBtnLeftTextColor() != 0) {
            cancel.setTextColor(commonDialogBean.getBtnLeftTextColor());
        }

        if (commonDialogBean.isNotCancel()) {
            setCanceledOnTouchOutside(false);
            setCancelable(false);
        }
        // 数据填充
        setDataAndListener();
    }

    /**
     * 显示操作的 Dialog样式
     */
    private void createOperateDialog(Context context, final DialogOperateBean operateBean, final DialogViews_ask.DialogViews_askImpl listener) {
        if (operateBean == null) {
            return;
        }

        mType = 1;
        setContentView(R.layout.dialog_operate);
        initView();
        this.mAction_ask = listener;
        final DialogBaseAdapter adapter = new DialogBaseAdapter(context, operateBean.getList(), operateBean.getDecMap(), operateBean.getColorMap(), operateBean.getPosition());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                isNoDismissCallBack = true;
                dismiss();
                if (listener != null) {
                    listener.doOk(adapter.getItem(i));
                }
            }
        });

        if (operateBean.getPosition() == 0) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mListView.getLayoutParams();
            params.setMargins(margin, margin, margin, margin);
            mListView.setLayoutParams(params);
            mListView.setBackgroundResource(R.drawable.view_org_pop_bg);
        }
    }

    /**
     * 数据填充和添加监听
     */
    private void setDataAndListener() {
        // 确定
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoDismissCallBack = true;
                close();
                if (mAction_ask != null) {
                    if (mAction_ask instanceof DialogViews_ask.DialogViews_askImpl) {
                        if (mType == 0) {
                            ((DialogViews_ask.DialogViews_askImpl) mAction_ask).doOk(getEditText());
                        } else if (mType == 2) {
                            if(dateValue.compareTo(minTimeDate) < 0){
                                ((DialogViews_ask.DialogViews_askImpl) mAction_ask).doOk(minTimeDate);
                            } else if(dateValue.compareTo(mCurrentTime) > 0){
                                ((DialogViews_ask.DialogViews_askImpl) mAction_ask).doOk(mCurrentTime);
                            } else{
                                ((DialogViews_ask.DialogViews_askImpl) mAction_ask).doOk(dateValue);
                            }
                        }
                    } else {
                        mAction_ask.doOk();
                    }
                }
            }
        });
        // 取消
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isNoDismissCallBack = true;
                close();
                if (mAction_ask != null) {
                    mAction_ask.doCancel();
                }
            }
        });
    }

    /**
     * 关闭dialog
     */
    public void close() {
        if (!((Activity) mContext).isFinishing()) {
            ((Activity) mContext).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isShowing()) {
                        dismiss();
                    }
                }
            });
        }
    }

    public String getEditText() {
        return input.getText().toString().trim();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (mAction_ask != null && !isNoDismissCallBack) {
            mAction_ask.doDismiss();
        }
    }

    public interface DialogViews_ask {
        void doOk();            //回传值1

        void doCancel();        //回传值2

        void doDismiss();       //回传值-1，弹窗消失时在doOk与doCancel都未回调的条件下才调用此方法

        class DialogViews_askImpl implements DialogViews_ask {

            @Override
            public void doOk() {

            }

            public void doOk(String text) {

            }

            @Override
            public void doCancel() {

            }

            @Override
            public void doDismiss() {

            }
        }
    }

    /**
     * 仿IOS日期展示
     *
     * @param context
     * @param wheelDateBean
     */
    public CommonDialogView(Context context, DialogWheelDateBean wheelDateBean, DialogViews_ask.DialogViews_askImpl listener) {
        super(context, R.style.dialog_normal);
        createDateDialogView(context, wheelDateBean, listener);
    }

    private void createDateDialogView(Context context, DialogWheelDateBean wheelDateBean, DialogViews_ask.DialogViews_askImpl listener) {
        mType = 2;
        this.mAction_ask = listener;
        this.mContext = context;
        setContentView(R.layout.dialog_wheel_date);
        initView();

        LinearLayout wheelLinearLayout = (LinearLayout) findViewById(R.id.wheel_linearLayout);

        DateWheel dateWheel;
        if (wheelDateBean != null) {
            dateWheel = new DateWheel(context, null, wheelDateBean.getYear(), wheelDateBean.getMonth(), wheelDateBean.getDay());
        } else {
            dateWheel = new DateWheel(context);
        }
        mCurrentTime = dateWheel.getCurrentTime();
        dateWheel.setWheelFinishListener(new OnWheelFinishListener() {
            @Override
            public void onChange(String currentValue) {
                dateValue = currentValue;
            }
        });

        wheelLinearLayout.addView(dateWheel.getTimeView());
        setDataAndListener();
    }
}
