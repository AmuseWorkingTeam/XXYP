
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
            for (int i = 0; i < EMOJI.TOON_EMOJI_FACE.length; i++) {
                emojiMap.put(EMOJI.TOON_EMOJI_CODE[i], EMOJI.TOON_EMOJI_FACE[i]);
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
    public static final int[] TOON_EMOJI_FACE = {
            R.drawable.toon_emoji_001, R.drawable.toon_emoji_002, R.drawable.toon_emoji_003,
            R.drawable.toon_emoji_004, R.drawable.toon_emoji_005, R.drawable.toon_emoji_006,
            R.drawable.toon_emoji_007, R.drawable.toon_emoji_008, R.drawable.toon_emoji_009,
            R.drawable.toon_emoji_010, R.drawable.toon_emoji_011, R.drawable.toon_emoji_012,
            R.drawable.toon_emoji_013, R.drawable.toon_emoji_014, R.drawable.toon_emoji_015,
            R.drawable.toon_emoji_016, R.drawable.toon_emoji_017, R.drawable.toon_emoji_018,
            R.drawable.toon_emoji_019, R.drawable.toon_emoji_020, R.drawable.toon_emoji_021,
            R.drawable.toon_emoji_022, R.drawable.toon_emoji_023, R.drawable.toon_emoji_024,
            R.drawable.toon_emoji_025, R.drawable.toon_emoji_026, R.drawable.toon_emoji_027,
            R.drawable.toon_emoji_028, R.drawable.toon_emoji_029, R.drawable.toon_emoji_030,
            R.drawable.toon_emoji_031, R.drawable.toon_emoji_032, R.drawable.toon_emoji_033,
            R.drawable.toon_emoji_034, R.drawable.toon_emoji_035, R.drawable.toon_emoji_036,
            R.drawable.toon_emoji_037, R.drawable.toon_emoji_038, R.drawable.toon_emoji_039,
            R.drawable.toon_emoji_040, R.drawable.toon_emoji_041, R.drawable.toon_emoji_042,
            R.drawable.toon_emoji_043, R.drawable.toon_emoji_044, R.drawable.toon_emoji_045,
            R.drawable.toon_emoji_047, R.drawable.toon_emoji_047, R.drawable.toon_emoji_048,
            R.drawable.toon_emoji_049, R.drawable.toon_emoji_050, R.drawable.toon_emoji_051,
            R.drawable.toon_emoji_052, R.drawable.toon_emoji_053, R.drawable.toon_emoji_054,
            R.drawable.toon_emoji_055, R.drawable.toon_emoji_056, R.drawable.toon_emoji_057,
            R.drawable.toon_emoji_058, R.drawable.toon_emoji_059, R.drawable.toon_emoji_060,
            R.drawable.toon_emoji_061, R.drawable.toon_emoji_062, R.drawable.toon_emoji_063,
            R.drawable.toon_emoji_064, R.drawable.toon_emoji_065, R.drawable.toon_emoji_066,
            R.drawable.toon_emoji_067, R.drawable.toon_emoji_068, R.drawable.toon_emoji_069,
            R.drawable.toon_emoji_070, R.drawable.toon_emoji_071, R.drawable.toon_emoji_072,
            R.drawable.toon_emoji_073, R.drawable.toon_emoji_074, R.drawable.toon_emoji_075,
            R.drawable.toon_emoji_076, R.drawable.toon_emoji_077, R.drawable.toon_emoji_078,
            R.drawable.toon_emoji_079, R.drawable.toon_emoji_080,
    };

    public static final String[] TOON_EMOJI_CODE = AppContextUtils.getAppContext().getResources()
            .getStringArray(R.array.emoji_code);
}
