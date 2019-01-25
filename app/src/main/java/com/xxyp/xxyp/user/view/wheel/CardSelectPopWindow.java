
package com.xxyp.xxyp.user.view.wheel;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;


/**
 * Description : 底部弹窗
 * Created by 139356 on 2016/2/8.
 * Job number：139356
 * Phone ：13070194942
 * Email：139356@syswin.com
 * Person in charge : 刘一博
 * Leader：刘一博
 */
public class CardSelectPopWindow extends PopupWindow {
    //动画持续时间
    private final static int DURATION = 300;

    private PopupWindow pw;

    private RelativeLayout rlOut;
    private LinearLayout llCenter;

    private int measureHeight;

    private TextView confirm, cancle;

    private View pwView;

    public CardSelectPopWindow(final Activity context, View parentView, View addView) {
        super(context);
        pw = this;
        pwView = View.inflate(context, R.layout.tcard_activity_show_record, null);
        rlOut = (RelativeLayout) pwView.findViewById(R.id.rl_main_app_subset_out);

        cancle = (TextView) pwView.findViewById(R.id.show_recore_cancle);
//        cancle.setTextColor(ThemeUtil.getColor("title_bar_txt_color"));

        confirm = (TextView) pwView.findViewById(R.id.show_recore_confirm);
//        confirm.setTextColor(ThemeUtil.getColor("title_bar_txt_color"));

        llCenter = (LinearLayout) pwView.findViewById(R.id.ll_center);
        //测量子view的可伸展高度
        int mExpandSpec = View.MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, View.MeasureSpec.AT_MOST);
        addView.measure(mExpandSpec, mExpandSpec);
        measureHeight = addView.getMeasuredHeight();

        llCenter.addView(addView);
        // 设置SelectPicPopupWindow的View
        pw.setContentView(pwView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        pw.setWidth(ActionBar.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        pw.setHeight(ActionBar.LayoutParams.MATCH_PARENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        pw.setFocusable(true);
        // 设置之后点击返回键关闭popuwindow
        pw.setBackgroundDrawable(new ColorDrawable(0x00000000));
        pw.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        pw.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        pw.showAtLocation(parentView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
//        darkenBackground(context, 0.4f);
        rlOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
//                darkenBackground(context, 1.0F, 0.0F);
                rlOut.clearAnimation();
                hideAnimation();
            }
        });
        showAnimation();
    }

    public void setConfirmListener(View.OnClickListener listener) {
        if (confirm != null) {
            confirm.setOnClickListener(listener);
        }
    }

    /**
     * 改变popwindow按钮的颜色
     *
     * @param color 颜色id
     */
    public void changeButtonColor(int color) {
        if (color != 0) {
            confirm.setTextColor(color);
            cancle.setTextColor(color);
        }
    }

    /**
     * 调节popwindow背景透明度
     *
     * @param context 上下文
     * @param alpha   透明度
     */
    private void darkenBackground(Activity context, Float alpha) {
        darkenBackground(context, alpha, -1f);
    }

    /**
     * 调节popwindow背景透明度
     *
     * @param context 上下文
     * @param alpha   透明度
     * @param color   颜色
     */
    private void darkenBackground(Activity context, Float alpha, Float color) {
        WindowManager.LayoutParams lp = context.getWindow().getAttributes();
        if (color != -1f) {
            lp.dimAmount = color;
        }
        lp.alpha = alpha;
        if (alpha == 1.0F) {
            context.getWindow().clearFlags(WindowManager.LayoutParams.TYPE_APPLICATION);
        } else {
            context.getWindow().addFlags(WindowManager.LayoutParams.TYPE_APPLICATION);
        }
        context.getWindow().setAttributes(lp);
    }

    private void showAnimation() {
        llCenter.setTranslationY(measureHeight);
        AnimatorSet set = new AnimatorSet();
        set.setDuration(DURATION);
        set.playTogether(
                ObjectAnimator.ofFloat(llCenter, "alpha", 0, 1.0f),
                ObjectAnimator.ofFloat(llCenter, "translationY", measureHeight, 0)
        );
        set.start();
    }

    private void hideAnimation() {
        AnimatorSet set = new AnimatorSet();
        set.setDuration(DURATION);
        set.playTogether(
                ObjectAnimator.ofFloat(llCenter, "alpha", 1, 0),
                ObjectAnimator.ofFloat(llCenter, "translationY", 0, measureHeight)
        );
        set.start();
    }
}
