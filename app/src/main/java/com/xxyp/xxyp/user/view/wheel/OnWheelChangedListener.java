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

package com.xxyp.xxyp.user.view.wheel;


/**
 * Description :  Wheel changed listener interfaces.
 * <p>The currentItemChanged() method is called whenever current wheel positions is changed:
 * <li> New Wheel position is set
 * <li> Wheel view is scrolled
 * Created by wxh on 2016/6/20 10:52.
 * Job number：138746
 * Phone ：15233620521
 * Email：wangxiaohui@syswin.com
 * Person in charge : 王晓辉
 * Leader：王晓辉
 */
public interface OnWheelChangedListener {
    /**
     * Callback method to be invoked when current item changed
     *
     * @param wheel    the wheel view whose state has changed
     * @param oldValue the old value of current item
     * @param newValue the new value of current item
     */
    void onChanged(WheelView wheel, int oldValue, int newValue);
}
