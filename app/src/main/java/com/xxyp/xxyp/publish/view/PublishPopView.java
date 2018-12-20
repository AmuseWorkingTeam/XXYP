
package com.xxyp.xxyp.publish.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.view.CommonPopView;
import com.xxyp.xxyp.publish.utils.PublishConfig;

/**
 * Description : 发布view
 * Created by sunpengfei on 2017/8/14.
 * Person in charge : sunpengfei
 */
public class PublishPopView extends RelativeLayout {

    private Context mContext;

    private TextView mUploadWork, mPublishShot;

    private CommonPopView mCommonPopView;

    private OnPopDismissListener mOnDismissListener;

    public PublishPopView(Context context) {
        this(context, null);
    }

    public PublishPopView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PublishPopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init(context);
    }

    public void setOnDismissListener(OnPopDismissListener onDismissListener){
        mOnDismissListener = onDismissListener;
    }

    private void init(Context context) {
        View view = inflate(context, R.layout.publish_pop_view, this);
        mUploadWork = (TextView)view.findViewById(R.id.tv_upload_work);
        mPublishShot = (TextView)view.findViewById(R.id.tv_publish_shot);
        mCommonPopView = new CommonPopView(context, view);
        setViewListener();
    }

    public void setSize(int width, int height){
        mCommonPopView.setWidth(width);
        mCommonPopView.setHeight(height);
    }

    private void setViewListener(){
        //跳转发布作品
        mUploadWork.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(mContext, PublishActivity.class);
                intent.putExtra(PublishConfig.PUBLISH_TYPE, PublishConfig.PublishType.PUBLISH_WORK);
                mContext.startActivity(intent);
            }
        });
        //跳转发布约拍
        mPublishShot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                Intent intent = new Intent(mContext, PublishActivity.class);
                intent.putExtra(PublishConfig.PUBLISH_TYPE, PublishConfig.PublishType.PUBLISH_SHOT);
                mContext.startActivity(intent);
            }
        });

        mCommonPopView.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if(mOnDismissListener != null){
                    mOnDismissListener.onDismiss();
                }
            }
        });
    }

    public void showPop(View parent) {
        int[] a = new int[2];
        parent.getLocationOnScreen(a);

        //获取屏幕的底部高度  获取window下的是不变的  防止有底部导航栏时候高度错误
        int screenBottom = ((Activity)mContext).getWindow().getDecorView().getBottom();
        mCommonPopView.showAtLocation(parent, Gravity.BOTTOM, a[0], screenBottom-a[1]);
    }

    public void dismiss(){
        mCommonPopView.dismiss();
    }

    public interface OnPopDismissListener{
        void onDismiss();
    }
    
}
