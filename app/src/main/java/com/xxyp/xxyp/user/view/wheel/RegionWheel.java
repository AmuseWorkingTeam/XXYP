
package com.xxyp.xxyp.user.view.wheel;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;


import com.xxyp.xxyp.R;
import com.xxyp.xxyp.user.view.wheel.area.Area;
import com.xxyp.xxyp.user.view.wheel.area.AreaDBMgr;
import com.xxyp.xxyp.user.view.wheel.wheelAdapter.ListWheelAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Description : 地区选择滚轮
 * Created by wxh on 2016/6/20 10:53.
 * Job number：138746
 * Phone ：15233620521
 * Email：wangxiaohui@syswin.com
 * Person in charge : 王晓辉
 * Leader：王晓辉
 */
public class RegionWheel implements OnWheelChangedListener {
    /**
     * 可见item个数
     */
    public static final int VISIBLE_ITEM = 7;

    /**
     * 当前item
     */
    public static final int CURRENT_ITEM = 0;

    private Context context;

    private View areaView;

    private boolean model = false;

    private WheelView wv_First;

    private WheelView wv_Second;

    private WheelView wv_Third;

    private List<Area> firstList;

    private List<Area> secondList;

    private List<Area> thirdList;

    private ListWheelAdapter FirstAdapter;

    private ListWheelAdapter secondAdapter;

    private ListWheelAdapter thirdAdapter;

    /**
     * 滑动停止时监听()返回字符串）
     */
    private OnWheelFinishListener onWheelFinishListener;

    /**
     * 滑动停止时监听（返回对象）
     */
    private OnWheelFinishNewListener onWheelFinishNewListener;

    public RegionWheel(Context context) {
        this(context, null);
    }

    public RegionWheel(Context context, View areaView) {
        this(context, areaView, null, null, null);
    }

    public RegionWheel(Context context, View areaView, String first, String second, String third) {
        this.context = context;
        this.areaView = areaView;
        this.model = true;
        initView(first, second, third);
        initListener();
    }

    public View getAreaView() {
        return areaView;
    }

    /**
     * 初始化view
     */
    private void initView(String first, String second, String third) {
        if (areaView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            areaView = inflater.inflate(R.layout.tc_wheel_area_layout, null);
        }
        initFirstDatas();
        int provinceIndex = getIndexByName(first, firstList);
        // 省
        wv_First = (WheelView)areaView.findViewById(R.id.first);
        wv_First.setAdapter(FirstAdapter);// 设置省的显示数据
        wv_First.setCyclic(false);// 不可循环滚动
        wv_First.setVisibleItems(VISIBLE_ITEM);
        wv_First.setCurrentItem(provinceIndex);
        // wv_First.setRadian(WheelView.DEFAULT_LEFT_RADIAN);
        updateSecondDatas(provinceIndex);
        int cityIndex = getIndexByName(second, secondList);
        // 市
        wv_Second = (WheelView)areaView.findViewById(R.id.second);
        wv_Second.setAdapter(secondAdapter);// 设置城市的显示数据
        wv_Second.setCyclic(false);// 不可循环滚动
        wv_Second.setVisibleItems(VISIBLE_ITEM);
        wv_Second.setCurrentItem(cityIndex);
        // wv_Second.setRadian(WheelView.DEFAULT_RADIAN);
        wv_Third = (WheelView)areaView.findViewById(R.id.third);
        if (model) {
            wv_Third.setVisibility(View.VISIBLE);
            updateThirdDatas(cityIndex);
            // 地区
            wv_Third.setAdapter(thirdAdapter);// 设置区域的显示数据
            wv_Third.setCyclic(false);// 不可循环滚动
            wv_Third.setVisibleItems(VISIBLE_ITEM);
            wv_Third.setCurrentItem(getIndexByName(third, thirdList));
            // wv_Third.setRadian(WheelView.DEFAULT_RIGHT_RADIAN);
        } else {
            wv_Third.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化监听
     */
    private void initListener() {
        wv_First.addChangingListener(this);
        wv_Second.addChangingListener(this);
        if (model) {
            wv_Third.addChangingListener(this);
        }
    }

    // 存放返回字符串
    StringBuilder result = new StringBuilder();

    // 存放返回对象
    List<Area> areas = new ArrayList<>(2);

    @Override
    public void onChanged(WheelView wheel, int oldValue, int newValue) {
        if (wheel == wv_First) {
            updateSecondDatas(newValue);
        }
        if (wheel == wv_Second) {
            if (model) {
                updateThirdDatas(newValue);
            } else {
                if (wv_Second != null) {
                    wv_Second.setCurrentItem(newValue);
                }
            }
        }
        if (wheel == wv_Third && model) {
            if (wv_Third != null) {
                wv_Third.setCurrentItem(newValue);
            }
        }
        if (model) {
            result.delete(0, result.length());
            result.append(firstList.get(wv_First.getCurrentItem()).getName());
            if (secondList != null && secondList.size() > 0) {
                result.append(" ").append(secondList.get(wv_Second.getCurrentItem()).getName());
            }
            if (thirdList != null && thirdList.size() > 0) {
                result.append(" ").append(thirdList.get(wv_Third.getCurrentItem()).getName());
            }
            onWheelFinishListener.OnChange(result.toString());
        } else {
            if (areas != null) {
                if (areas.size() > 0) {
                    areas.clear();
                }
                if (firstList != null && firstList.size() > 0) {
                    areas.add(0, firstList.get(wv_First.getCurrentItem()));
                }
                if (secondList != null && secondList.size() > 0) {
                    areas.add(1, secondList.get(wv_Second.getCurrentItem()));
                } else {
                    areas.add(1, null);
                }
            }
            onWheelFinishNewListener.OnChange(areas);
        }
    }

    /**
     * 更新区域
     */
    public void updateThirdDatas(int index) {
        if (secondList != null && secondList.size() > 0) {
            String code = secondList.get(index).getCode();
            thirdList = AreaDBMgr.getInstance(context).getDistrictsByCode(code);
        } else {
            thirdList = null;
        }
        if (thirdAdapter == null) {
            thirdAdapter = new ListWheelAdapter(context, thirdList);
        } else {
            thirdAdapter.setItems(thirdList);
        }
        if (wv_Second != null) {
            wv_Second.setCurrentItem(index);
        }
        if (wv_Third != null) {
            wv_Third.setCurrentItem(CURRENT_ITEM);
        }
    }

    /**
     * 更新城市数据
     */
    public void updateSecondDatas(int index) {
        if (firstList != null && firstList.size() > 0) {
            String code = firstList.get(index).getCode();
            if (model) {
                secondList = AreaDBMgr.getInstance(context).getCityByCode(code);
            }
//            else {
//                secondList = RegionAreaService.getInstance().getCityByCode(code);
//            }
        }
        if (secondAdapter == null) {
            secondAdapter = new ListWheelAdapter(context, secondList);
        } else {
            secondAdapter.setItems(secondList);
        }
        if (wv_First != null) {
            wv_First.setCurrentItem(index);
        }
        if (wv_Second != null) {
            wv_Second.setCurrentItem(CURRENT_ITEM);
        }
        if (wv_Third != null && model) {
            wv_Third.setCurrentItem(CURRENT_ITEM);
        }
        if (model) {
            updateThirdDatas(CURRENT_ITEM);
        }
    }

    /**
     * 初始化省数据
     */
    private void initFirstDatas() {
        if (model) {
            firstList = AreaDBMgr.getInstance(context).getProvinces();
        }
//        else {
//            firstList = RegionAreaService.getInstance().getPrivinces();
//        }
        if (FirstAdapter == null) {
            FirstAdapter = new ListWheelAdapter(context, firstList);
        } else {
            FirstAdapter.setItems(firstList);
        }
    }

    public interface OnWheelFinishListener {
        /**
         * 滑动停止后，当前区域
         *
         * @param currentValue 当前区域
         */
        void OnChange(String currentValue);
    }

    public interface OnWheelFinishNewListener {
        /**
         * 滑动停止后，当前区域
         *
         * @param values 当前对象
         */
        void OnChange(List<Area> values);
    }

    /**
     * 设置滑动结束监听(字符串)
     * 
     * @param wheelFinishListener 监听
     */
    public void setWheelFinishListener(OnWheelFinishListener wheelFinishListener) {
        if (this.onWheelFinishListener == null) {
            this.onWheelFinishListener = wheelFinishListener;
        }
        if (firstList != null && firstList.size() > 0) {
            result.append(firstList.get(wv_First.getCurrentItem()).getName());
        }
        if (secondList != null && secondList.size() > 0) {
            result.append(" ").append(secondList.get(wv_Second.getCurrentItem()).getName());
        }
        if (thirdList != null && thirdList.size() > 0) {
            result.append(" ").append(thirdList.get(wv_Third.getCurrentItem()).getName());
        }
        wheelFinishListener.OnChange(result.toString());
    }

    /**
     * 设置滑动结束监听（对象）
     *
     * @param wheelFinishNewListener 监听
     */
    public void setWheelFinishNewListener(OnWheelFinishNewListener wheelFinishNewListener) {
        if (this.onWheelFinishNewListener == null) {
            this.onWheelFinishNewListener = wheelFinishNewListener;
        }
        if (areas != null) {
            if (firstList != null && firstList.size() > 0) {
                areas.add(0, firstList.get(wv_First.getCurrentItem()));
            }
            if (secondList != null && secondList.size() > 0) {
                areas.add(1, secondList.get(wv_Second.getCurrentItem()));
            }
            wheelFinishNewListener.OnChange(areas);
        }
    }

    /**
     * 根据名字获取index
     * 
     * @param name 显示名字
     * @param pointList 目标列表
     * @return int
     */
    private int getIndexByName(String name, List<Area> pointList) {
        if (pointList == null || pointList.size() == 0 || TextUtils.isEmpty(name))
            return CURRENT_ITEM;
        int size = pointList.size();
        for (int i = 0; i < size; i++) {
            Area area = pointList.get(i);
            if (TextUtils.equals(area.getName(), name)) {
                return area.getIndex();
            }
        }
        return CURRENT_ITEM;
    }

    /**
     * 根据index获取地区名字
     * 
     * @param index 位置
     * @param pointList 目标列表
     * @return int
     */
    private String getNameByIndex(int index, List<Area> pointList) {
        if (pointList == null || pointList.size() == 0)
            return "";
        int size = pointList.size();
        for (int i = 0; i < size; i++) {
            Area area = pointList.get(i);
            if (area.getIndex() == index) {
                return area.getName();
            }
        }
        return "";
    }
}
