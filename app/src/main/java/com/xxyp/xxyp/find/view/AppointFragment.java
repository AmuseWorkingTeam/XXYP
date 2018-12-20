
package com.xxyp.xxyp.find.view;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleFragment;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.recyclerView.DividerItemDecoration;
import com.xxyp.xxyp.find.adapter.AppointAdapter;
import com.xxyp.xxyp.find.contract.AppointContract;
import com.xxyp.xxyp.find.presenter.AppointPresenter;
import com.xxyp.xxyp.main.bean.ShotBean;

import java.util.List;

/**
 * Description : 作品fragment Created by sunpengfei on 2017/7/31. Person in charge
 * : sunpengfei
 */
public class AppointFragment extends BaseTitleFragment implements AppointContract.View {

    private AppointAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private AppointContract.Presenter mPresenter;

    /* 上下拉 */
    private SwipeToLoadLayout mSwipeToLoadLayout;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        return new Header.Builder(getActivity(), headerContainer).build();
    }

    @Override
    protected View onCreateView() {
        hideHeader();
        View view = View.inflate(getActivity(), R.layout.fragment_appoint, null);
        mSwipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.container);
        mRecyclerView = ((RecyclerView)view.findViewById(R.id.swipe_target));
        mAdapter = new AppointAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.color.guide_divider));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new AppointPresenter(this);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.obtainShotList();
    }

    @Override
    protected void setViewListener() {
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新 直接获取最新的数据
                mPresenter.obtainShotList();
            }
        });

        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //上拉刷新
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        });
        mAdapter.setOnFindShotListener(new AppointAdapter.OnFindShotListener() {
            @Override
            public void onShotListener(String userId, String shotId) {
                mPresenter.openShotDetail(userId, shotId);
            }

            @Override
            public void onOpenFrame(String userId) {
                mPresenter.openFrame(userId);
            }
        });
    }

    @Override
    public void setPresenter(AppointContract.Presenter presenter) {

    }

    @Override
    public void showShotList(List<ShotBean> shotBeans) {
        if(shotBeans == null || shotBeans.size() == 0){
            return;
        }
        mAdapter.replaceList(shotBeans);
    }

    @Override
    public void showShotLoading(boolean isCancelable) {
        showLoadingDialog(isCancelable);
    }

    @Override
    public void cancelShotLoading() {
        dismissLoadingDialog();
    }

    @Override
    public void resetRefresh() {
        mSwipeToLoadLayout.setRefreshing(false);
    }

    @Override
    public void onDestroyView() {
        if(mPresenter != null){
            mPresenter.onDestroyPresenter();
        }
        setNull(mPresenter);
        super.onDestroyView();
    }
}
