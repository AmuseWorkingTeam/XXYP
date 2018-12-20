
package com.xxyp.xxyp.main.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

import java.util.List;

/**
 * Description : 热门作品列表 Created by sunpengfei on 2017/7/31. Person in charge :
 * sunpengfei
 */
public class WorkHotListBean extends BaseBean {

    private List<WorkBean> hotWorks;

    public List<WorkBean> getHotWorks() {
        return hotWorks;
    }

    public void setHotWorks(List<WorkBean> hotWorks) {
        this.hotWorks = hotWorks;
    }
}
