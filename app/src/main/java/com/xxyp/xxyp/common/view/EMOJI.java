
package com.xxyp.xxyp.common.view;

import android.text.TextUtils;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.AppContextUtils;

import java.util.HashMap;

public class EMOJI {

    private static volatile EMOJI mInstance;

    private HashMap<String, Integer> emojiMap;

    private EMOJI() {
        emojiMap();
    }

    public static EMOJI getInstance() {
        if (mInstance == null) {
            synchronized (EMOJI.class) {
                if (mInstance == null) {
                    mInstance = new EMOJI();
                }
            }
        }
        return mInstance;
    }

    private void emojiMap() {
        if (emojiMap == null) {
            emojiMap = new HashMap<>();
            for (int i = 0; i < EMOJI.XXYP_EMOJI_FACE.length; i++) {
                emojiMap.put(EMOJI.XXYP_EMOJI_CODE[i], EMOJI.XXYP_EMOJI_FACE[i]);
            }
        }
    }

    /**
     * 获取对应的emoji资源id
     * @param key emoji描述
     * @return int
     */
    public int getEmojiRes(String key) {

        if(!TextUtils.isEmpty(key) && emojiMap.containsKey(key)){
            return emojiMap.get(key);
        }
        return -1;
    }

    /**
     * 表情资源ID
     */
    public static final int[] XXYP_EMOJI_FACE = {
            R.drawable.xxyp_emoji_001, R.drawable.xxyp_emoji_002, R.drawable.xxyp_emoji_003,
            R.drawable.xxyp_emoji_004, R.drawable.xxyp_emoji_005, R.drawable.xxyp_emoji_006,
            R.drawable.xxyp_emoji_007, R.drawable.xxyp_emoji_008, R.drawable.xxyp_emoji_009,
            R.drawable.xxyp_emoji_010, R.drawable.xxyp_emoji_011, R.drawable.xxyp_emoji_012,
            R.drawable.xxyp_emoji_013, R.drawable.xxyp_emoji_014, R.drawable.xxyp_emoji_015,
            R.drawable.xxyp_emoji_016, R.drawable.xxyp_emoji_017, R.drawable.xxyp_emoji_018,
            R.drawable.xxyp_emoji_019, R.drawable.xxyp_emoji_020, R.drawable.xxyp_emoji_021,
            R.drawable.xxyp_emoji_022, R.drawable.xxyp_emoji_023, R.drawable.xxyp_emoji_024,
            R.drawable.xxyp_emoji_025, R.drawable.xxyp_emoji_026, R.drawable.xxyp_emoji_027,
            R.drawable.xxyp_emoji_028, R.drawable.xxyp_emoji_029, R.drawable.xxyp_emoji_030,
            R.drawable.xxyp_emoji_031, R.drawable.xxyp_emoji_032, R.drawable.xxyp_emoji_033,
            R.drawable.xxyp_emoji_034, R.drawable.xxyp_emoji_035, R.drawable.xxyp_emoji_036,
            R.drawable.xxyp_emoji_037, R.drawable.xxyp_emoji_038, R.drawable.xxyp_emoji_039,
            R.drawable.xxyp_emoji_040, R.drawable.xxyp_emoji_041, R.drawable.xxyp_emoji_042,
            R.drawable.xxyp_emoji_043, R.drawable.xxyp_emoji_044, R.drawable.xxyp_emoji_045,
            R.drawable.xxyp_emoji_047, R.drawable.xxyp_emoji_047, R.drawable.xxyp_emoji_048,
            R.drawable.xxyp_emoji_049, R.drawable.xxyp_emoji_050, R.drawable.xxyp_emoji_051,
            R.drawable.xxyp_emoji_052, R.drawable.xxyp_emoji_053, R.drawable.xxyp_emoji_054,
            R.drawable.xxyp_emoji_055, R.drawable.xxyp_emoji_056, R.drawable.xxyp_emoji_057,
            R.drawable.xxyp_emoji_058, R.drawable.xxyp_emoji_059, R.drawable.xxyp_emoji_060,
            R.drawable.xxyp_emoji_061, R.drawable.xxyp_emoji_062, R.drawable.xxyp_emoji_063,
            R.drawable.xxyp_emoji_064, R.drawable.xxyp_emoji_065, R.drawable.xxyp_emoji_066,
            R.drawable.xxyp_emoji_067, R.drawable.xxyp_emoji_068, R.drawable.xxyp_emoji_069,
            R.drawable.xxyp_emoji_070, R.drawable.xxyp_emoji_071, R.drawable.xxyp_emoji_072,
            R.drawable.xxyp_emoji_073, R.drawable.xxyp_emoji_074, R.drawable.xxyp_emoji_075,
            R.drawable.xxyp_emoji_076, R.drawable.xxyp_emoji_077, R.drawable.xxyp_emoji_078,
            R.drawable.xxyp_emoji_079, R.drawable.xxyp_emoji_080,
    };

    public static final String[] XXYP_EMOJI_CODE = AppContextUtils.getAppContext().getResources()
            .getStringArray(R.array.emoji_code);
}
