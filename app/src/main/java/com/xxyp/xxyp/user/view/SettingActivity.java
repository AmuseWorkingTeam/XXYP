
package com.xxyp.xxyp.user.view;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleActivity;
import com.xxyp.xxyp.common.utils.SharePreferenceUtils;
import com.xxyp.xxyp.common.utils.ToastUtil;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.find.provider.FindProvider;
import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.user.adapter.MyDatingShotAdapter;
import com.xxyp.xxyp.user.contract.MyDatingShotContract;
import com.xxyp.xxyp.user.contract.SettingContract;
import com.xxyp.xxyp.user.presenter.MyDatingShotPresenter;
import com.xxyp.xxyp.user.presenter.SettingPresenter;

import java.util.List;

/**
 * Description : 设置页面
 * Created by sunpengfei on 2017/8/10.
 * Person in charge : sunpengfei
 */
public class SettingActivity extends BaseTitleActivity implements SettingContract.View, View.OnClickListener {

    private SettingContract.Presenter mPresenter;
    private View tvAbout;
    private View tvSuggestions;
    private View tvClear;
    private View tvLogout;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        Header.Builder builder = new Header.Builder(this, headerContainer);
        builder.setBackIcon(R.drawable.header_back_icon, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        builder.setTitle(R.string.setting);
        return builder.build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(this, R.layout.activity_setting, null);
        tvAbout = view.findViewById(R.id.tv_about);
        tvSuggestions = view.findViewById(R.id.tv_suggestions);
        tvClear = view.findViewById(R.id.tv_clear);
        tvLogout = view.findViewById(R.id.tv_logout);
        mPresenter = new SettingPresenter(this);
        return view;
    }

    @Override
    protected void setViewListener() {
        tvAbout.setOnClickListener(this);
        tvSuggestions.setOnClickListener(this);
        tvClear.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
    }

    @Override
    protected void initDataForActivity() {
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void setPresenter(SettingContract.Presenter presenter) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_about:
                ToastUtil.showTextViewPrompt(this, "敬请期待");
                break;
            case R.id.tv_suggestions:
                ToastUtil.showTextViewPrompt(this, "敬请期待");
                break;
            case R.id.tv_clear:
                mPresenter.clearData();
                break;
            case R.id.tv_logout:
                mPresenter.logout();
                break;
            default:
                break;
        }
    }
}
