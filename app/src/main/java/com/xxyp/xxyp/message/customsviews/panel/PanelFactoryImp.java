
package com.xxyp.xxyp.message.customsviews.panel;

import android.app.Activity;
import android.util.SparseArray;

/**
 * Description : 输入面板 Created by sunpengfei on 2017/8/1. Person in charge :
 * sunpengfei
 */
public class PanelFactoryImp implements IPanelFactory {

    public static final int TYPE_EMOJI = 1;

    public static final int TYPE_FUNCTION = 2;

    public static final int TYPE_PHOTO = 3;

    public static final int TYPE_VOICE = 4;

    private Activity mActivity;

    private SparseArray<IPanel> mCaches;

    public PanelFactoryImp(Activity activity) {
        mActivity = activity;
        mCaches = new SparseArray();
    }

    @Override
    public IPanel obtainPanel(int type) {
        IPanel panel = mCaches.get(type);
        if (panel == null) {
            switch (type){
                case TYPE_FUNCTION:
                    panel = new PanelFunction(mActivity);
                    break;
                case TYPE_EMOJI:
                    panel = new PanelEmoji(mActivity);
                    break;
                default:
                    break;
            }
            if(panel != null){
                mCaches.put(type, panel);
            }
        }
        return panel;
    }
}
