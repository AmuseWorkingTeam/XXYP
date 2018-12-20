
package com.xxyp.xxyp.message.customsviews.panel;

/**
 * Description : 输入面板
 * Created by sunpengfei on 2017/8/25.
 * Person in charge : sunpengfei
 */
public interface OnPanelItemListener {

    /**
     * 输入面板点击
     * @param type 当前点击来自于功能展板
     * @param item 所点击的item
     */
    void onPanelItemClick(int type, Object item);
}
