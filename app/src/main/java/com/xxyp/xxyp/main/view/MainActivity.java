
package com.xxyp.xxyp.main.view;

import android.view.View;
import android.view.ViewGroup;

import com.xxyp.xxyp.R;
import com.xxyp.xxyp.common.utils.ScreenUtils;
import com.xxyp.xxyp.find.view.FindFragment;
import com.xxyp.xxyp.message.view.MessageFragment;
import com.xxyp.xxyp.publish.view.PublishPopView;
import com.xxyp.xxyp.user.view.PersonalCenterFragment;

import java.util.List;

/**
 * Description : 首页view Created by sunpengfei on 2017/7/26. Person in charge :
 * sunpengfei
 */
public class MainActivity extends BaseSwitchActivity {

    protected TabSwitchView mFindTab;

    protected TabSwitchView mMainTab;

    protected TabSwitchView mMessageTab;

    protected TabSwitchView mMineTab;

    protected TabSwitchView mPublishTab;

    /* 发布pop */
    private PublishPopView mPublishPopView;

    @Override
    protected void initFragment(final List<TabSwitchView> mTabSwitch) {

        mMainTab = new BaseSwitchActivity.TabSwitchView(R.drawable.main_icon_selector, new MainFragment(),
                0);
        mTabSwitch.add(mMainTab);

        mFindTab = new BaseSwitchActivity.TabSwitchView(R.drawable.find_icon_selector, new FindFragment(),
                1);
        mTabSwitch.add(mFindTab);

        mPublishTab = new BaseSwitchActivity.TabSwitchView(R.drawable.publish_red_icon_selector, null, -1);
        mTabSwitch.add(mPublishTab);

        mPublishTab.getSwitchView().setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if(mPublishPopView == null){
                    mPublishPopView = new PublishPopView(MainActivity.this);
                    mPublishPopView.setSize(ViewGroup.LayoutParams.MATCH_PARENT,
                            ScreenUtils.heightPixels - mTabLayout.getHeight()
                                    - ScreenUtils.getStatusBarHeight());
                    mPublishPopView.setOnDismissListener(new PublishPopView.OnPopDismissListener() {
                        @Override
                        public void onDismiss() {
                            mPublishTab.getTabImageView().setSelected(false);
                        }
                    });
                }
                if(mPublishPopView.isShown()){
                    mPublishPopView.dismiss();
                }else{
                    mPublishTab.getTabImageView().setSelected(true);
                    mPublishPopView.showPop(mTabLayout);
                }
            }
        });

        mMessageTab = new BaseSwitchActivity.TabSwitchView(R.drawable.message_icon_selector,
                new MessageFragment(), 3);
        mTabSwitch.add(mMessageTab);

        mMineTab = new BaseSwitchActivity.TabSwitchView(R.drawable.mine_icon_selector,
                new PersonalCenterFragment(), 4);
        mTabSwitch.add(mMineTab);
    }

    @Override
    protected void onTabClick() {
        if(mPublishPopView != null && mPublishPopView.isShown()){
            mPublishPopView.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        if(mPublishPopView != null && mPublishPopView.isShown()){
            mPublishPopView.dismiss();
            return;
        }
        super.onBackPressed();
    }
}
