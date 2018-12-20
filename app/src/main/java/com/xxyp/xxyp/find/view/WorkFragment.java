
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
import com.xxyp.xxyp.find.adapter.FindWorkAdapter;
import com.xxyp.xxyp.find.contract.WorkContract;
import com.xxyp.xxyp.find.presenter.WorkPresenter;
import com.xxyp.xxyp.main.bean.WorkBean;

import java.util.List;

/**
 * Description : 作品页面 Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class WorkFragment extends BaseTitleFragment implements WorkContract.View {

    private FindWorkAdapter mAdapter;

    private RecyclerView mRecyclerView;

    private WorkContract.Presenter mPresenter;

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
        mAdapter = new FindWorkAdapter(getActivity());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), R.color.guide_divider));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new WorkPresenter(this);
        return view;
    }

    @Override
    protected void setViewListener() {
        mSwipeToLoadLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                //下拉刷新 直接获取最新的数据
                mPresenter.obtainWorkList();
            }
        });

        mSwipeToLoadLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                //上拉刷新
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        });
        mAdapter.setOnFindWorkListener(new FindWorkAdapter.OnFindWorkListener() {
            @Override
            public void onWorkListener(String userId, String workId) {
                mPresenter.openProduct(userId, workId);
            }

            @Override
            public void onOpenFrame(String userId) {
                mPresenter.onOpenFrame(userId);
            }
        });
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.obtainWorkList();
    }

    @Override
    public void setPresenter(WorkContract.Presenter presenter) {

    }

    @Override
    public void showWorkList(List<WorkBean> workBeans) {
        if(workBeans != null && workBeans.size() > 0){
            mAdapter.replaceList(workBeans);
        }
    }

    @Override
    public void showWorkLoading(boolean cancelable) {
        showLoadingDialog(cancelable);
    }

    @Override
    public void cancelWorkLoading() {
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
