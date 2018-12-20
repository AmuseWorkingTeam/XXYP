
package com.xxyp.xxyp.common.net;

import com.xxyp.xxyp.common.bean.BaseBean;
import com.xxyp.xxyp.common.bean.MetaBean;

/**
 * Description : 网络回调返回 Created by sunpengfei on 2017/7/27. Person in charge :
 * sunpengfei
 */
public class NetResponseBean extends BaseBean {

    private Object data;

    private MetaBean meta;

    public Object getData() {
        return this.data;
    }

    public MetaBean getMeta() {
        return this.meta;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public void setMeta(MetaBean meta) {
        this.meta = meta;
    }
}
