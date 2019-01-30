/*
 *  Copyright 2010 Yuri Kanivets
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.xxyp.xxyp.user.view.wheel.wheelAdapter;

import android.content.Context;
import android.text.TextUtils;

/**
 * Description : Numeric Wheel adapter.
 * Created by mc on 2016/6/27 10:42.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge : mc
 * Leader：mc
 */
public class NumericWheelAdapter extends AbstractWheelTextAdapter {

    /** The default min value */
    private static final int DEFAULT_MAX_VALUE = 9;

    /** The default max value */
    private static final int DEFAULT_MIN_VALUE = 0;

    // Values
    private int minValue;

    private int maxValue;

    // format
    private String label;

    /**
     * Constructor
     *
     * @param context the current context
     */
    public NumericWheelAdapter(Context context) {
        this(context, DEFAULT_MIN_VALUE, DEFAULT_MAX_VALUE);
    }

    /**
     * Constructor
     *
     * @param context the current context
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     */
    public NumericWheelAdapter(Context context, int minValue, int maxValue) {
        this(context, minValue, maxValue, null);
    }

    /**
     * Constructor
     *
     * @param context the current context
     * @param minValue the wheel min value
     * @param maxValue the wheel max value
     * @param label the format string
     */
    public NumericWheelAdapter(Context context, int minValue, int maxValue, String label) {
        super(context);

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.label = label;
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0) {
            int temp = getItemsCount() - index;
            while (temp < 0) {
                index--;
                temp++;
            }
            int value = minValue + index;
            return TextUtils.isEmpty(label) ? String.valueOf(value) : String.valueOf(value) + label;
        }
        return "";
    }

    @Override
    public int getItemsCount() {
        return maxValue - minValue + 1;
    }
}
