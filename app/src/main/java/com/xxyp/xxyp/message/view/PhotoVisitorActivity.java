
package com.xxyp.xxyp.message.view;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.bean.PhotoViewBean;
import com.xxyp.xxyp.common.bean.PhotoViewListBean;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ViewPagerFixed;
import com.xxyp.xxyp.common.view.photoview.PhotoVisitorView;

import java.util.List;

/**
 * Description : 查看大图页面
 * Created by sunpengfei on 2017/9/4.
 * Person in charge : sunpengfei
 */
public class PhotoVisitorActivity extends BaseTitleActivity {

    public static final String PHOTO_DATA = "photoData";

    public static final String PHOTO_INDEX = "photoIndex";

    private List<PhotoViewBean> mPhotoBeans;

    private ViewPagerFixed mViewPager;

    private PhotoPreviewAdapter mAdapter;

    private int mIndex;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        return new Header.Builder(this, headerContainer).build();
    }

    @Override
    protected View onCreateView() {
        hideStatusBar();
        View view = View.inflate(this, R.layout.photo_pre_activity, null);
        mViewPager = (ViewPagerFixed) view.findViewById(R.id.photo_view_pager);
        mViewPager.setAdapter(mAdapter = new PhotoPreviewAdapter(this));
        return view;
    }

    /**
     * 设置全屏展示 隐藏状态栏 底部导航栏
     */
    private void hideStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            final View decorView = getWindow().getDecorView();
            doFullScreen(decorView);
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    if ((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0) {
                        decorView.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                doFullScreen(decorView);
                            }
                        }, 500);
                    }
                }
            });
        }
        hideHeader();
    }

    /**
     * 设置全屏
     * @param decorView
     */
    private void doFullScreen(View decorView){
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        if(intent != null){
            PhotoViewListBean bean = (PhotoViewListBean)intent.getSerializableExtra(PHOTO_DATA);
            mPhotoBeans = bean != null ? bean.getPhotoViewBeans() : null;
            mIndex = intent.getIntExtra(PHOTO_INDEX, 0);
        }
    }

    @Override
    protected void initDataForActivity() {
        mViewPager.setCurrentItem(mIndex);
    }

    private class PhotoPreviewAdapter extends PagerAdapter {

        private Context mContext;

        public PhotoPreviewAdapter(Context context) {
            super();
            mContext = context;
        }

        @Override
        public int getCount() {
            return mPhotoBeans != null ? mPhotoBeans.size() : 0;
        }

        @Override
        public int getItemPosition(Object object) {
            View view = (View) object;
            int index = mViewPager.getCurrentItem();
            if (index == (Integer) view.getTag()) {
                return POSITION_NONE;
            } else {
                return POSITION_UNCHANGED;
            }
        }

        @Override
        public View instantiateItem(ViewGroup container, final int position) {
            RelativeLayout rl = (RelativeLayout) LayoutInflater.from(mContext)
                    .inflate(R.layout.adapter_photo_preview_item, null);
            PhotoVisitorView photoVisitorView = new PhotoVisitorView(mContext);
            rl.removeAllViews();
            rl.addView(photoVisitorView);
            rl.setTag(position);
            container.addView(rl);
            photoVisitorView.setPhotoViewBean(mPhotoBeans.get(position), position);
            return rl;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}
