
package com.xxyp.xxyp.user.model;

import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.publish.db.PublishDBManager;
import com.xxyp.xxyp.user.contract.MyDatingShotContract;

import java.util.List;

/**
 * Description : 我的约拍model
 * Created by sunpengfei on 2017/8/11.
 * Person in charge : sunpengfei
 */
public class MyDatingShotModel implements MyDatingShotContract.Model {

    @Override
    public List<ShotBean> getMyShotInfo() {
        return PublishDBManager.getInstance().getShotInfo();
    }
}
