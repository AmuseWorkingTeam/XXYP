
package com.xxyp.xxyp.user.bean;

import com.xxyp.xxyp.common.bean.BaseBean;
import com.xxyp.xxyp.main.bean.WorkBean;

import java.util.List;

/**
 * Description : 用户作品列表 Created by sunpengfei on 2017/8/2. Person in charge :
 * sunpengfei
 */

public class UserWorkListBean extends BaseBean {

    private List<WorkBean> works;

    public List<WorkBean> getWorks() {
        return works;
    }

    public void setWorks(List<WorkBean> works) {
        this.works = works;
    }
}
