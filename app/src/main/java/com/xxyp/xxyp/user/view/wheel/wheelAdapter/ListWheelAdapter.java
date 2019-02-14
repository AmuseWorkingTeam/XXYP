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

import com.xxyp.xxyp.user.view.wheel.area.Area;

import java.util.List;

/**
 * Description : The simple Array wheel adapter
 */
public class ListWheelAdapter<T> extends AbstractWheelTextAdapter {

    // items
    private List<T> items;

    /**
     * Constructor
     * 
     * @param context the current context
     * @param items the items
     */
    public ListWheelAdapter(Context context, List<T> items) {
        super(context);
        this.items = items;
    }

    public void setItems(List<T> items) {
        this.items = items;
        notifyDataInvalidated();
    }

    @Override
    public CharSequence getItemText(int index) {
        if (index >= 0 && index < items.size()) {
            T item = items.get(index);
            if (item instanceof CharSequence) {
                return (CharSequence)item;
            }
            if (item instanceof Area) {
                return ((Area)item).getName();
            }
            return item.toString();
        }
        return null;
    }

    @Override
    public int getItemsCount() {
        return items == null ? 0 : items.size();
    }

}
