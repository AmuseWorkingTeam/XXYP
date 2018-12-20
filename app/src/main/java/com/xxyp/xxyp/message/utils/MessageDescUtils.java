
package com.xxyp.xxyp.message.utils;

import android.text.TextUtils;

import com.xxyp.xxyp.message.bean.ChatMessageBean;
import com.xxyp.xxyp.message.bean.MessageBodyBean;

/**
 * Description : 消息文案工具类 Created by sunpengfei on 2017/8/24. Person in charge :
 * sunpengfei
 */
public class MessageDescUtils {

    /**
     * 获取消息中摘要信息
     *
     * @param bean 消息对象
     * @return 摘要字段
     */
    public static String getMessageDes(ChatMessageBean bean) {
        if (bean == null) {
            return "";
        }
        switch (bean.getChatType()) {
            // 如果是单聊
            case MessageConfig.MessageCatalog.CHAT_SINGLE:
                return getMessageDes(bean.getMsgType(), bean.getBodyBean());
            default:
                return "[不支持该类型消息]";
        }
    }

    /**
     * 根据类型和聊天的内容获取展示的缩略信息
     *
     * @param msgType 消息类型
     * @return 显示的缩略信息
     */
    private static String getMessageDes(int msgType, MessageBodyBean bodyBean) {
        String msgText = "";
        switch (msgType) {
            case MessageConfig.MessageType.MSG_TEXT:
                //文本
                msgText = bodyBean != null && !TextUtils.isEmpty(bodyBean.getText())
                        ? bodyBean.getText()
                        : "";
                break;
            case MessageConfig.MessageType.MSG_VOICE:
                msgText = "[语音]";
                break;
            case MessageConfig.MessageType.MSG_IMAGE:
                msgText = "[图片]";
                break;
            case MessageConfig.MessageType.MSG_APPOINTMENT:
                msgText = "[约拍]";
                break;
            default:
                msgText = "[未知消息]";
                break;
        }
        return msgText;
    }
}
