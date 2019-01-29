
package com.xxyp.xxyp.message.customsviews.panel;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.xxyp.xxyp.R;

/**
 * 聊天输入面板功能
 */
public class PanelFunction implements IPanel, View.OnClickListener {

    private Context mContext;

    /* 拍照 相册 创建约拍 选择约拍 */
    private TextView mCameraView, mChoosePicView, mCreateShotView, mChooseShotView;

    private OnPanelItemListener mItemListener;

    public PanelFunction(Context context) {
        mContext = context;
    }

    @Override
    public View obtainView(OnPanelItemListener itemListener) {
        mItemListener = itemListener;
        View view = View.inflate(mContext, R.layout.panel_function, null);
        mCameraView = (TextView)view.findViewById(R.id.function_camera);
        mChoosePicView = (TextView)view.findViewById(R.id.function_image);
        mCreateShotView = (TextView)view.findViewById(R.id.function_create_shot);
        mChooseShotView = (TextView)view.findViewById(R.id.function_choose_shot);
        setViewListener();
        return view;
    }

    /**
     * 点击事件
     */
    private void setViewListener() {
        mCameraView.setOnClickListener(this);
        mChoosePicView.setOnClickListener(this);
        mCreateShotView.setOnClickListener(this);
        mChooseShotView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.function_camera:
                if (mItemListener != null) {
                    mItemListener.onPanelItemClick(PanelFactoryImp.TYPE_FUNCTION,
                            PanelConfig.PANEL_CAMERA);
                }
                break;
            case R.id.function_image:
                if (mItemListener != null) {
                    mItemListener.onPanelItemClick(PanelFactoryImp.TYPE_FUNCTION,
                            PanelConfig.PANEL_IMAGE);
                }
                break;
            case R.id.function_create_shot:
                if (mItemListener != null) {
                    mItemListener.onPanelItemClick(PanelFactoryImp.TYPE_FUNCTION,
                            PanelConfig.PANEL_CREATE_SHOT);
                }
                break;
            case R.id.function_choose_shot:
                if (mItemListener != null) {
                    mItemListener.onPanelItemClick(PanelFactoryImp.TYPE_FUNCTION,
                            PanelConfig.PANEL_CHOOSE_SHOT);
                }
                break;
            default:
                break;
        }
    }

}
