
package com.xxyp.xxyp.common.utils;

import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;

import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.view.EMOJI;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Description : 表情操作工具类
 */
public class EmojiUtils {

    private static volatile EmojiUtils mInstance;

    private EmojiUtils() {
    }

    public static EmojiUtils getInstance() {
        if (mInstance == null) {
            synchronized (EmojiUtils.class) {
                if (mInstance == null) {
                    mInstance = new EmojiUtils();
                }
            }
        }
        return mInstance;
    }

    /**
     * 解析文本内容 将文本内容解析为表情
     * 
     * @param str 文本内容
     * @param zhengze 正则表达式
     * @return SpannableString
     */
    public SpannableString getExpressionString(String str, String zhengze) {
        SpannableString spannableString = new SpannableString(str);
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE); // 通过传入的正则表达式来生成一个pattern
        try {
            dealExpression(spannableString, sinaPatten, 0);
        } catch (Exception e) {
            XXLog.log_e("dealExpression", e.getMessage());
        }
        return spannableString;
    }

    /**
     * 解析文本内容 将文本内容解析为表情
     * 备注：用于超链接文本
     *
     * @param spannableString 文本内容
     * @param zhengze 正则表达式
     * @return SpannableString
     */
    public SpannableStringBuilder getExpressionString(SpannableStringBuilder spannableString, String zhengze) {
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE); // 通过传入的正则表达式来生成一个pattern
        try {

            dealExpression(spannableString, sinaPatten, 0);
        } catch (Exception e) {
            XXLog.log_e("dealExpression", e.getMessage());
        }
        return spannableString;
    }

    /**
     * 解析文本内容 将文本内容解析为表情
     *
     * @param spannableString 文本内容
     * @param zhengze 正则表达式
     * @return SpannableString
     */
    public SpannableString getExpressionString(SpannableString spannableString, String zhengze) {
        Pattern sinaPatten = Pattern.compile(zhengze, Pattern.CASE_INSENSITIVE); // 通过传入的正则表达式来生成一个pattern
        try {

            dealExpression(spannableString, sinaPatten, 0);
        } catch (Exception e) {
            XXLog.log_e("dealExpression", e.getMessage());
        }
        return spannableString;
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     * 
     * @param spannableString 传入的文本内容
     * @param patten 正则匹配
     * @param start 初始位置
     * @throws Exception
     */
    private void dealExpression(SpannableString spannableString, Pattern patten, int start) throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        int end = start;
        while (matcher.find() && end < spannableString.length()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            if (matcher.start() > 800) {
                break;
            }
            int resId;
            resId = EMOJI.getInstance().getEmojiRes(key);
            end = matcher.start() + key.length();
            if (resId > 0) {
                Drawable drawable = small(resId);
                ImageSpan imageSpan = new ImageSpan(drawable);
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    /**
     * 对spanableString进行正则判断，如果符合要求，则以表情图片代替
     *  备注： 用于超链接文本
     * @param spannableString 传入的文本内容
     * @param patten 正则匹配
     * @param start 初始位置
     * @throws Exception
     */
    private void dealExpression(SpannableStringBuilder spannableString, Pattern patten, int start) throws Exception {
        Matcher matcher = patten.matcher(spannableString);
        int end = start;
        while (matcher.find() && end < spannableString.length()) {
            String key = matcher.group();
            if (matcher.start() < start) {
                continue;
            }
            if (matcher.start() > 800) {
                break;
            }
            int resId;
            resId = EMOJI.getInstance().getEmojiRes(key);
            end = matcher.start() + key.length();
            if (resId > 0) {
                Drawable drawable = small(resId);
                ImageSpan imageSpan = new ImageSpan(drawable);
                spannableString.setSpan(imageSpan, matcher.start(), end,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public Drawable small(int resId) {
        Drawable drawable = AppContextUtils.getAppContext().getResources().getDrawable(resId);
        if (drawable == null) {
            return null;
        }
        drawable.setBounds(0, 0, ScreenUtils.dp2px(30), ScreenUtils.dp2px(30));
        return drawable;
    }

}
