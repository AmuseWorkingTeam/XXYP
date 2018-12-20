
package com.xxyp.xxyp.user.bean;

import com.xxyp.xxyp.common.bean.BaseBean;
import com.xxyp.xxyp.main.bean.ShotBean;

import java.util.List;

/**
 * Description : 用户约拍list
 * Created by sunpengfei on 2017/8/21.
 * Person in charge : sunpengfei
 */
public class UserShotListBean extends BaseBean {

    private List<ShotBean> datingShot;

    public List<ShotBean> getDatingShot() {
        return datingShot;
    }

    public void setDatingShot(List<ShotBean> datingShot) {
        this.datingShot = datingShot;
    }
}
