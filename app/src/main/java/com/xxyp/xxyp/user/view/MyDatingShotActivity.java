
package com.xxyp.xxyp.user.view;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.find.provider.FindProvider;
import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.user.adapter.MyDatingShotAdapter;
import com.xxyp.xxyp.user.contract.MyDatingShotContract;
import com.xxyp.xxyp.user.presenter.MyDatingShotPresenter;

import java.util.List;

/**
 * Description : 我的约拍页面 Created by sunpengfei on 2017/8/10. Person in charge :
 * sunpengfei
 */
public class MyDatingShotActivity extends BaseTitleActivity implements MyDatingShotContract.View {

    private RecyclerView mRecyclerView;

    /* 约拍adapter */
    private MyDatingShotAdapter mAdapter;

    private boolean mIsChoose;

    public final static String IS_CHOOSE = "isChoose";

    public final static String MY_SHOT = "myShot";

    private MyDatingShotContract.Presenter mPresenter;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        builder.setTitle(R.string.my_appointment);
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_my_dating_shot, null);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.dating_shot_recycler);
        mAdapter = new MyDatingShotAdapter(this);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(mAdapter);
        mPresenter = new MyDatingShotPresenter(this);
        return view;
    }

    @Override
    protected void initDataFromFront(Intent intent) {
        super.initDataFromFront(intent);
        if (intent != null) {
            mIsChoose = intent.getBooleanExtra(IS_CHOOSE, false);
        }
    }

    @Override
    protected void setViewListener() {
        mAdapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ShotBean bean = mAdapter.getItem(position);
                if (bean == null) {
                    return;
                }
                if (mIsChoose) {
                    // 选择
                    Intent intent = new Intent();
                    intent.putExtra(MY_SHOT, bean);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    String userId = SharePreferenceUtils.getInstance().getUserId();
                    FindProvider.openShot(MyDatingShotActivity.this, userId,
                            bean.getDatingShotId());
                }
            }
        });
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.getMyDatingShot();
    }

    @Override
    public void showMyShot(List<ShotBean> shotBeans) {
        if (shotBeans != null && shotBeans.size() > 0) {
            mAdapter.replaceList(shotBeans);
        }
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(MyDatingShotContract.Presenter presenter) {

    }
}
