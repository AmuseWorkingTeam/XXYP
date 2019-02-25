
package com.xxyp.xxyp.message.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.interfaces.ChatActionListener;
import com.xxyp.xxyp.message.itemholder.BaseChatHolder;
import com.xxyp.xxyp.message.itemholder.IItemChatPanel;
import com.xxyp.xxyp.message.itemholder.MessageItemBaseView;
import com.xxyp.xxyp.message.itemholder.MessageItemImage;
import com.xxyp.xxyp.message.itemholder.MessageItemNotice;
import com.xxyp.xxyp.message.itemholder.MessageItemShot;
import com.xxyp.xxyp.message.itemholder.MessageItemText;
import com.xxyp.xxyp.message.itemholder.MessageItemVoice;
import com.xxyp.xxyp.message.utils.MessageConfig;

import java.util.List;

/**
 * Description : 聊天页面消息adapter Created by sunpengfei on 2017/8/1. Person in
 * charge : sunpengfei
 */
public class ChatMessageAdapter extends RecyclerView.Adapter<BaseChatHolder> {

    private List<ChatMessageBean> mBeans;

    private Context mContext;

    private ChatActionListener mActionListener;

    public ChatMessageAdapter(Context context) {
        mContext = context;
    }

    public void setChatMessages(List<ChatMessageBean> beans) {
        mBeans = beans;
    }

    public void setActionListener(ChatActionListener actionListener) {
        mActionListener = actionListener;
    }

    @Override
    public int getItemCount() {
        if (mBeans != null) {
            return mBeans.size();
        }
        return 0;
    }

    @Override
    public int getItemViewType(int position) {
        if (mBeans == null || mBeans.size() == 0) {
            return -1;
        }
        return getChatItemType(mBeans.get(position));
    }

    @Override
    public BaseChatHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return getChatItemHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(BaseChatHolder holder, int position) {
        if (holder == null) {
            return;
        }
        holder.setData(mBeans.get(position));
        holder.showChatTime(isShowTime(position));
    }

    /**
     * 根据类型获取holder
     * 
     * @param parent 父布局
     * @param viewType 类型
     * @return BaseChatHolder
     */
    private BaseChatHolder getChatItemHolder(ViewGroup parent, int viewType) {
        IItemChatPanel panel = null;
        switch (viewType) {
            case MessageConfig.ChatListViewItemType.TEXT_LEFT:
                // 左文本
                panel = new MessageItemText(mContext, mActionListener,
                        MessageItemBaseView.ITEM_LEFT);
                break;
            case MessageConfig.ChatListViewItemType.TEXT_RIGHT:
                // 右文本
                panel = new MessageItemText(mContext, mActionListener,
                        MessageItemBaseView.ITEM_RIGHT);
                break;
            case MessageConfig.ChatListViewItemType.VOICE_LEFT:
                // 左语音
                panel = new MessageItemVoice(mContext, mActionListener,
                        MessageItemBaseView.ITEM_LEFT);
                break;
            case MessageConfig.ChatListViewItemType.VOICE_RIGHT:
                // 右语音
                panel = new MessageItemVoice(mContext, mActionListener,
                        MessageItemBaseView.ITEM_RIGHT);
                break;
            case MessageConfig.ChatListViewItemType.IMAGE_LEFT:
                // 左图片
                panel = new MessageItemImage(mContext, mActionListener,
                        MessageItemBaseView.ITEM_LEFT);
                break;
            case MessageConfig.ChatListViewItemType.IMAGE_RIGHT:
                // 右图片
                panel = new MessageItemImage(mContext, mActionListener,
                        MessageItemBaseView.ITEM_RIGHT);
                break;
            case MessageConfig.ChatListViewItemType.APPOINTMENT_LEFT:
                // 左约拍
                panel = new MessageItemShot(mContext, mActionListener,
                        MessageItemBaseView.ITEM_LEFT);
                break;
            case MessageConfig.ChatListViewItemType.APPOINTMENT_RIGHT:
                // 右约拍
                panel = new MessageItemShot(mContext, mActionListener,
                        MessageItemBaseView.ITEM_RIGHT);
                break;
            case MessageConfig.ChatListViewItemType.NOTICE:
                panel = new MessageItemNotice(mContext, mActionListener,
                        MessageItemBaseView.ITEM_CENTER);
                break;
            default:
                break;
        }
        if (panel == null) {
            // 无法识别的类型转为文本
            panel = new MessageItemText(mContext, mActionListener, MessageItemBaseView.ITEM_RIGHT);
        }
        return new BaseChatHolder(panel.obtainView(parent), panel);
    }

    /**
     * 获取消息对应的类型
     * 
     * @param bean 消息体
     * @return int
     */
    private int getChatItemType(ChatMessageBean bean) {
        if (bean == null) {
            return -1;
        }
        int itemType = -1;
        switch (bean.getMsgType()) {
            case MessageConfig.MessageType.MSG_TEXT:
                // 文本
                if (bean.getSender() == MessageConfig.MessageSender.MY_SEND) {
                    // 我发送
                    itemType = MessageConfig.ChatListViewItemType.TEXT_RIGHT;
                } else {
                    itemType = MessageConfig.ChatListViewItemType.TEXT_LEFT;
                }
                break;
            case MessageConfig.MessageType.MSG_VOICE:
                // 语音
                if (bean.getSender() == MessageConfig.MessageSender.MY_SEND) {
                    // 我发送
                    itemType = MessageConfig.ChatListViewItemType.VOICE_RIGHT;
                } else {
                    itemType = MessageConfig.ChatListViewItemType.VOICE_LEFT;
                }
                break;
            case MessageConfig.MessageType.MSG_IMAGE:
                // 图片
                if (bean.getSender() == MessageConfig.MessageSender.MY_SEND) {
                    // 我发送
                    itemType = MessageConfig.ChatListViewItemType.IMAGE_RIGHT;
                } else {
                    itemType = MessageConfig.ChatListViewItemType.IMAGE_LEFT;
                }
                break;
            case MessageConfig.MessageType.MSG_APPOINTMENT:
                // 约拍
                if (bean.getSender() == MessageConfig.MessageSender.MY_SEND) {
                    // 我发送
                    itemType = MessageConfig.ChatListViewItemType.APPOINTMENT_RIGHT;
                } else {
                    itemType = MessageConfig.ChatListViewItemType.APPOINTMENT_LEFT;
                }
                break;
            case MessageConfig.MessageType.MSG_NOTICE:
                itemType = MessageConfig.ChatListViewItemType.NOTICE;
                break;
            default:
                // 无法识别的类型转为文本
                if (bean.getSender() == MessageConfig.MessageSender.MY_SEND) {
                    // 我发送
                    itemType = MessageConfig.ChatListViewItemType.TEXT_RIGHT;
                } else {
                    itemType = MessageConfig.ChatListViewItemType.TEXT_LEFT;
                }
                break;
        }
        return itemType;
    }

    /**
     * 是否展示时间
     * 
     * @param position 当前位置
     * @return boolean
     */
    private boolean isShowTime(int position) {
        // 聊天消息时间戳为毫秒级 目前为10分钟间隔 也就是10 * 60 * 1000
        long timeOut = 10 * 60 * 1000;
        ChatMessageBean current = mBeans.get(position);
        boolean showtime = true;
        if (position > 0) {
            ChatMessageBean pre = mBeans.get(position - 1);
            if (current.getMessageTime() == 0 || pre.getMessageTime() == 0) {
                showtime = false;
            } else {
                long time_cha = 0;
                try {
                    time_cha = (current.getMessageTime() - pre.getMessageTime()) / timeOut;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showtime = time_cha >= 1;
            }
        }
        return showtime;
    }

}
