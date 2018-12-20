
package com.xxyp.xxyp.user.bean;

import com.xxyp.xxyp.common.bean.BaseBean;

import java.util.List;

/**
 * Description : 全部粉丝关注 Created by sunpengfei on 2017/10/19. Person in charge :
 * sunpengfei
 */
public class FansFocusListBean extends BaseBean {

    /* 全部粉丝关注返回 */
    private List<FansFocusBean> fans;

    public List<FansFocusBean> getFans() {
        return fans;
    }

    public void setFans(List<FansFocusBean> fans) {
        this.fans = fans;
    }
}
