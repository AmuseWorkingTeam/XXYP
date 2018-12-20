
package com.xxyp.xxyp.common.bean;

import java.io.Serializable;

/**
 * Description : 表情信息
 */
public class ItemEmoji implements Serializable {

    // 表情名字
    private String emojiName;

    // 默认表情时图标
    private int emojiResId;

    public String getEmojiName() {
        return emojiName;
    }

    public void setEmojiName(String emojiName) {
        this.emojiName = emojiName;
    }

    public int getEmojiResId() {
        return emojiResId;
    }

    public void setEmojiResId(int emojiResId) {
        this.emojiResId = emojiResId;
    }
}
