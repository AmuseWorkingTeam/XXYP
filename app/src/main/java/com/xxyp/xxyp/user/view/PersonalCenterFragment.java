
package com.xxyp.xxyp.user.view;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.base.BaseTitleFragment;
import com.xxyp.xxyp.common.bean.UserInfo;
import com.xxyp.xxyp.common.utils.ImageRequestConfig;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.common.view.Header;
import com.xxyp.xxyp.common.view.ImageLoader;
import com.xxyp.xxyp.login.utils.UserConfig;
import com.xxyp.xxyp.user.contract.PersonalCenterContract;
import com.xxyp.xxyp.user.presenter.PersonalCenterPresenter;

/**
 * Description : 个人中心fragment Created by sunpengfei on 2017/8/3. Person in
 * charge : sunpengfei
 */
public class PersonalCenterFragment extends BaseTitleFragment
        implements PersonalCenterContract.View, View.OnClickListener {

    private ImageRequestConfig mConfig;

    private PersonalCenterContract.Presenter mPresenter;

    private TextView mTvFans;

    private TextView mTvFollow;

    /* 我的约拍 */
    private TextView mTvMyDatingShot;

    /* 个人设置 */
    private TextView mTvSetting;

    private SimpleDraweeView mUserAvatar;

    private TextView mUserIntro;

    private TextView mUserName;

    private ImageView mUserdentity;
    private TextView tvUserInfo;
    private TextView tvMyPhoto;

    @Override
    protected Header onCreateHeader(RelativeLayout headerContainer) {
        return new Header.Builder(getActivity(), headerContainer).setTitle(R.string.personal_center)
                .build();
    }

    @Override
    protected View onCreateView() {
        View view = View.inflate(getActivity(), R.layout.fragment_personal, null);
        mUserAvatar = ((SimpleDraweeView) view.findViewById(R.id.personal_user_avatar));
        mUserdentity = ((ImageView) view.findViewById(R.id.user_identity));
        mUserName = ((TextView) view.findViewById(R.id.user_name));
        mUserIntro = ((TextView) view.findViewById(R.id.user_intro));
        mTvFans = ((TextView) view.findViewById(R.id.tv_fans));
        mTvFollow = ((TextView) view.findViewById(R.id.tv_follow));
        tvUserInfo = (TextView) view.findViewById(R.id.tv_user_info);
        tvMyPhoto = (TextView) view.findViewById(R.id.tv_my_photo);
        mTvMyDatingShot = ((TextView) view.findViewById(R.id.tv_my_dating_shot));
        mTvSetting = ((TextView) view.findViewById(R.id.tv_setting));
        mPresenter = new PersonalCenterPresenter(this);
        return view;
    }

    @Override
    protected void initDataForActivity() {
        mPresenter.getMyUserInfo();
        mPresenter.getFansCount();
    }

    @Override
    protected void setViewListener() {
        mTvMyDatingShot.setOnClickListener(this);
        mTvSetting.setOnClickListener(this);
        tvUserInfo.setOnClickListener(this);
        tvMyPhoto.setOnClickListener(this);
        mTvFans.setOnClickListener(this);
        mTvFollow.setOnClickListener(this);
    }

    @Override
    public void showFansCount(int fansCount, int followCount) {
        mTvFans
                .setText(String.format(getResources().getString(R.string.fans), fansCount + ""));
        mTvFollow
                .setText(String.format(getResources().getString(R.string.focus), followCount + ""));
    }

    @Override
    public void showUserInfo(UserInfo userInfo) {
        if (userInfo == null) {
            return;
        }
        if (mConfig == null) {
            mConfig = new ImageRequestConfig.Builder().setLoadWidth(ScreenUtils.dp2px(104))
                    .setLoadHeight(ScreenUtils.dp2px(104)).build();
        }
        ImageLoader.getInstance().display(mUserAvatar, userInfo.getUserImage(),
                mConfig);
        if (userInfo.getUserIdentity() == UserConfig.UserType.CAMERAMER) {
            mUserdentity.setImageResource(R.drawable.cameraman_icon);
        } else {
            mUserdentity.setImageResource(R.drawable.customer_icon);
        }
        mUserName.setText(userInfo.getUserName());
        mUserIntro.setText(userInfo.getUserIntroduction());
    }

    @Override
    public void onClick(View v) {
        if (mPresenter == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_user_info:
                // 用户信息
                mPresenter.openPersonalInfo();
                break;
            case R.id.tv_my_photo:
                // 我的相册
                break;
            case R.id.tv_setting:
                //跳转个人设置
                mPresenter.openSetting();
                break;
            case R.id.tv_my_dating_shot:
                //我的约拍
                mPresenter.openMyDatingShot();
                break;
            case R.id.tv_fans:
                //我的粉丝
                mPresenter.openMyFans();
                break;
            case R.id.tv_follow:
                //我的关注
                mPresenter.openMyFocus();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mPresenter != null) {
            mPresenter.onDestroyPresenter();
            setNull(mPresenter);
        }
    }

    @Override
    public void setPresenter(PersonalCenterContract.Presenter presenter) {
    }

}
