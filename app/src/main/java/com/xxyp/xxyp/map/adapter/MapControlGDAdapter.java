
package com.xxyp.xxyp.map.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.services.core.PoiItem;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.map.utils.ViewHolder;

import java.util.HashMap;
import java.util.List;

/**
 * Description : 高德地图定位附近地址列表的adapter
 */
public class MapControlGDAdapter extends BaseAdapter {

    private Context mContext;

    private List<PoiItem> mPoiItems;

    private LayoutInflater mInflater;

    private HashMap<String, String> mCityMap = new HashMap<>();

    private PoiItem currentItem;

    private int currentPosition;

    private boolean isSetDefault;

    private boolean seachState;

    public MapControlGDAdapter(Context context, List<PoiItem> poiInfos) {

        this.mContext = context;
        this.mPoiItems = poiInfos;
        this.mInflater = LayoutInflater.from(context);
        mCityMap.put(context.getString(R.string.beijing), context.getString(R.string.beijing));// 110000
        mCityMap.put(context.getString(R.string.tianjin), context.getString(R.string.tianjin));// 120000
        mCityMap.put(context.getString(R.string.shanghai), context.getString(R.string.shanghai));// 310000
        mCityMap.put(context.getString(R.string.chongqing), context.getString(R.string.chongqing));// 500000
    }

    @Override
    public int getCount() {

        return mPoiItems == null ? 0 : mPoiItems.size();
    }

    @Override
    public PoiItem getItem(int position) {
        int size = getCount();
        if (size == 0) {
            return null;
        }
        // 防止数组下标越界
        return position >= size ? mPoiItems.get(size - 1) : mPoiItems.get(position);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_plugin_map_listview, null);
        }
        PoiItem item = mPoiItems.get(position);
        ImageView ivChoose = ViewHolder.get(convertView, R.id.iv_item_plugin_map_listview_choose);
        TextView tvDistrict = ViewHolder.get(convertView,
                R.id.tv_item_plugin_map_listview_district);
        TextView tvAddr = ViewHolder.get(convertView, R.id.tv_item_plugin_map_listview_addr);
        ivChoose.setBackgroundResource(R.drawable.common_selected_blue);
        ivChoose.setVisibility(View.GONE);
        if (item != null) {
            String title = item.getTitle();
            if (title != null && !title.equals(mContext.getString(R.string.common_location))) {
                StringBuffer addr = new StringBuffer();
                String province = mPoiItems.get(position).getProvinceName();
                String city = mPoiItems.get(position).getCityName();
                String ad = mPoiItems.get(position).getAdName();
                String snippet = mPoiItems.get(position).getSnippet();
                if (province != null && !mCityMap.containsValue(province)) {
                    addr.append(province);
                }
                if (city != null) {
                    addr.append(city);
                }
                if (ad != null) {
                    addr.append(ad);
                }
                if (snippet != null) {
                    addr.append(snippet);
                }
                tvAddr.setText(addr);
            } else {
                tvAddr.setText("");
            }
            if (title != null) {
                tvDistrict.setText(title);
            }
            if (seachState) {
                ivChoose.setVisibility(View.GONE);
            } else {
                if (position == 0 && currentItem == null) {
                    ivChoose.setVisibility(View.VISIBLE);
                } else {
                    if (currentItem != null
                            && currentItem.getLatLonPoint().equals(item.getLatLonPoint())
                            && currentItem.getTitle().equals(title)
                            && currentItem.getPoiId().equals(item.getPoiId())
                            && position == currentPosition) {
                        ivChoose.setVisibility(View.VISIBLE);
                    } else {
                        ivChoose.setVisibility(View.GONE);
                    }
                }
            }
        }

        return convertView;
    }

    /**
     * 添加数据
     */
    public synchronized void addData(List<PoiItem> poiInfo, boolean isClear) {

        if (mPoiItems != null && poiInfo != null) {
            if (isClear) {
                mPoiItems.clear();
                if (!poiInfo.isEmpty()) {
                    this.currentItem = poiInfo.get(0);
                }
                this.currentPosition = 0;
            }
            this.mPoiItems.addAll(poiInfo);
        }
        notifyDataSetChanged();
    }

    /**
     * 清除多余数据
     */
    public synchronized void clearData() {

        if (mPoiItems != null) {
            mPoiItems.clear();
        }
        notifyDataSetChanged();
    }

    public void initCurrentItem() {
        this.currentItem = null;
    }

    public void setCurrentItem(PoiItem currentItem, int currentPosition) {

        this.currentItem = currentItem;
        this.currentPosition = currentPosition;
        notifyDataSetChanged();
    }

    public void add(int location, PoiItem object) {
        mPoiItems.add(location, object);
    }


    public void setDefault(boolean isSetDefault) {

        this.isSetDefault = isSetDefault;
    }

    public void setSearchState(boolean searchState) {

        this.seachState = searchState;
    }
}
