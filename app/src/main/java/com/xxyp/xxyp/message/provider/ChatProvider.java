
package com.xxyp.xxyp.message.provider;

import android.app.Activity;
import android.content.Intent;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.bean.PhotoViewBean;
import com.xxyp.xxyp.common.bean.PhotoViewListBean;
import com.xxyp.xxyp.message.utils.MessageConfig;
import com.xxyp.xxyp.message.view.ChatSingleActivity;
import com.xxyp.xxyp.message.view.PhotoVisitorActivity;

import java.util.List;

/**
 * Description : 聊天provider
 * Created by sunpengfei on 2017/8/22.
 * Person in charge : sunpengfei
 */
public class ChatProvider {

    /**
     * 跳转聊天
     * @param activity  上下文
     * @param chatType  聊天类型
     * @param chatId    会话id
     */
    public static void openChatActivity(Activity activity, int chatType, String chatId) {
        Intent intent = null;
        switch (chatType) {
            case MessageConfig.MessageCatalog.CHAT_SINGLE:
                intent = new Intent(activity, ChatSingleActivity.class);
                break;
            default:
                break;
        }
        if (intent != null) {
            intent.putExtra(MessageConfig.CHAT_TYPE, chatType);
            intent.putExtra(MessageConfig.CHAT_ID, chatId);
            activity.startActivity(intent);
        }
    }

    /**
     * 打开图片查看
     * @param activity  上下文
     * @param photoBeans  图片数据
     * @param index     索引位置
     */
    public static void openPhotoPreView(Activity activity, int index, List<PhotoViewBean> photoBeans) {
        PhotoViewListBean bean = new PhotoViewListBean();
        bean.setPhotoViewBeans(photoBeans);
        Intent intent = new Intent(activity, PhotoVisitorActivity.class);
        intent.putExtra(PhotoVisitorActivity.PHOTO_INDEX, index);
        intent.putExtra(PhotoVisitorActivity.PHOTO_DATA, bean);
        activity.startActivity(intent);
        activity.overridePendingTransition(R.anim.popwindow_alpha_in, 0);
    }

}
