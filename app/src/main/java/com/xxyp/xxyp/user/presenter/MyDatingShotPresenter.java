package com.xxyp.xxyp.user.presenter;

import com.xxyp.xxyp.user.contract.MyDatingShotContract;
import com.xxyp.xxyp.user.model.MyDatingShotModel;

/**
 * Description : 我的约拍presenter
 * Created by sunpengfei on 2017/8/11.
 * Job number：135182
 * Phone ：18510428121
 * Email：sunpengfei@syswin.com
 * Person in charge : sunpengfei
 * Leader：wangxiaohui
 */
public class MyDatingShotPresenter implements MyDatingShotContract.Presenter {

    private MyDatingShotContract.View mView;
    
    private MyDatingShotContract.Model mModel;
    
    public MyDatingShotPresenter(MyDatingShotContract.View view){
        mView = view;
        mModel = new MyDatingShotModel();
    }
    
    @Override
    public void getMyDatingShot() {
        mView.showMyShot(mModel.getMyShotInfo());
    }

    @Override
    public void onDestroyPresenter() {
        mView = null;
        mModel = null;
    }
}
