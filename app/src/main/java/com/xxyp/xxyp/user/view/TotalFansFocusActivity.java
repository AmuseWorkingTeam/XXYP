
package com.xxyp.xxyp.user.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.common.view.ShapeImageView;
import com.xxyp.xxyp.common.view.recyclerView.BaseViewHolder;
import com.xxyp.xxyp.common.view.recyclerView.DividerItemDecoration;
import com.xxyp.xxyp.common.view.recyclerView.RecyclerWithHFAdapter;
import com.xxyp.xxyp.login.utils.UserConfig;
import com.xxyp.xxyp.user.bean.FansFocusBean;
import com.xxyp.xxyp.user.contract.TotalFansFocusContract;
import com.xxyp.xxyp.user.presenter.TotalFansFocusPresenter;
import com.xxyp.xxyp.user.utils.FrameConfig;

import java.util.List;

/**
 * Description : 全部粉丝关注页面 Created by sunpengfei on 2017/10/19. Person in charge
 * : sunpengfei
 */
public class TotalFansFocusActivity extends BaseTitleActivity implements TotalFansFocusContract.View {

    private RecyclerView mRecyclerView;

    private TotalFansFocusAdapter mAdapter;

    private TotalFansFocusContract.Presenter mPresenter;

    /* 被查看者的userId */
    private String mUserId;

    /* 查看粉丝 关注类型 0 粉丝 1 关注*/
    private int mType;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        builder.setTitle("");
        return builder.build();
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        if(intent == null){
            return;
        }
        mUserId = intent.getStringExtra(FrameConfig.USER_ID);
        mType = intent.getIntExtra(UserConfig.USER_FANS_TYPE, 0);
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_total_fans_focus, null);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.total_fans_focus_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, R.color.guide_divider));
        mAdapter = new TotalFansFocusAdapter(this);
        mAdapter.addHeaderView(createHeadView());
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new TotalFansFocusPresenter(this);
        return view;
    }

    /**
     * 初始化头部view
     * @return View
     */
    private View createHeadView(){
        RelativeLayout rl = new RelativeLayout(this);
        rl.setBackgroundResource(R.color.color_EEEEEE);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(20));
        rl.setLayoutParams(params);

        // 上分割线
        View topDiver = new View(this);
        topDiver.setBackgroundResource(R.color.guide_divider);
        RelativeLayout.LayoutParams topParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(0.3f));
        topParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        topDiver.setLayoutParams(topParams);
        rl.addView(topDiver);

        // 文本
        TextView tv = new TextView(this);
        tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        tv.setTextColor(getResources().getColor(R.color.color_949393));
        tv.setPadding(ScreenUtils.dp2px(15), 0, 0,
                0);
        tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        RelativeLayout.LayoutParams textParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textParams.addRule(RelativeLayout.CENTER_VERTICAL);
        tv.setLayoutParams(textParams);
        if (mType == UserConfig.UserFansType.FANS_TYPE) {
            tv.setText(R.string.total_fans);
        } else if (mType == UserConfig.UserFansType.FOCUS_TYPE) {
            tv.setText(R.string.total_focus);
        }
        rl.addView(tv);

//       // 下分割线
        View bottomDiver = new View(this);
        bottomDiver.setBackgroundResource(R.color.color_EEEEEE);
        RelativeLayout.LayoutParams bottomParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT, ScreenUtils.dp2px(0.3f));
        bottomParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        bottomDiver.setLayoutParams(bottomParams);
        rl.addView(bottomDiver);
        return rl;
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (mType){
            case UserConfig.UserFansType.FANS_TYPE:
                mPresenter.obtainTotalFans(mUserId);
                break;
            case UserConfig.UserFansType.FOCUS_TYPE:
                mPresenter.obtainTotalFocus(mUserId);
                break;
            default:
                break;
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(TotalFansFocusContract.Presenter presenter) {

    }

    @Override
    public void showFansFocusDialog(boolean cancelable) {
        showLoadingDialog(cancelable);
    }

    @Override
    public void cancelFansFocusDialog() {
        dismissLoadingDialog();
    }

    @Override
    public void showFansFocus(List<FansFocusBean> beans) {
        if(beans != null){
            mAdapter.setBeans(beans);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPresenter != null){
            mPresenter.onDestroyPresenter();
            setNull(mPresenter);
        }
    }

    /**
     * 全部粉丝关注adapter
     */
    private class TotalFansFocusAdapter extends RecyclerWithHFAdapter{

        private List<FansFocusBean> mBeans;

        private Context mContext;
        
        public TotalFansFocusAdapter(Context context){
            mContext = context;
        }

        public void setBeans(List<FansFocusBean> beans){
            mBeans = beans;
            notifyDataSetChanged();
        }

        @Override
        protected int getAdapterCount() {
            return mBeans != null ? mBeans.size() : 0;
        }

        @Override
        protected BaseViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new BaseViewHolder(
                    LayoutInflater.from(mContext).inflate(R.layout.item_fans_focus_view, parent, false));
        }

        @Override
        protected void onBindHolder(BaseViewHolder holder, int position) {
            RelativeLayout rlContainer = holder.findViewById(R.id.fans_container);
            ShapeImageView iconView = holder.findViewById(R.id.fans_avatar);
            TextView nameView = holder.findViewById(R.id.fans_name);
            TextView descView = holder.findViewById(R.id.fans_describe);
            ImageView relationIconView = holder.findViewById(R.id.fans_relation);
            if(mBeans == null || mBeans.size() == 0){
                return;
            }
            FansFocusBean bean = mBeans.get(position);
            if(bean == null){
                return;
            }
            UserInfo userInfo = null;
            if(mType == UserConfig.UserFansType.FANS_TYPE){
                userInfo = bean.getFromUser();
            }else if(mType == UserConfig.UserFansType.FOCUS_TYPE){
                userInfo = bean.getToUser();
            }
            if(userInfo == null){
                return;
            }
            final String userId = userInfo.getUserId();
            rlContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPresenter.openFrame(userId);
                }
            });
            ImageLoader.getInstance().display(iconView, userInfo.getUserImage());
            nameView.setText(
                    !TextUtils.isEmpty(userInfo.getUserName()) ? userInfo.getUserName() : "");
            descView.setText(!TextUtils.isEmpty(userInfo.getUserIntroduction())
                    ? userInfo.getUserIntroduction()
                    : "");
        }
    }
}
