
package com.xxyp.xxyp.publish.model;

import com.xxyp.xxyp.main.bean.ShotBean;
import com.xxyp.xxyp.main.bean.ShotPhotoBean;
import com.xxyp.xxyp.main.bean.WorkBean;
import com.xxyp.xxyp.main.bean.WorkPhotoBean;
import com.xxyp.xxyp.publish.contract.PublishContract;
import com.xxyp.xxyp.publish.db.PublishDBManager;
import com.xxyp.xxyp.publish.service.PublishServiceManager;

import rx.Observable;
import rx.functions.Func1;


/**
 * Description : 发布model Created by sunpengfei on 2017/8/2. Person in charge :
 * sunpengfei
 */
public class PublishModel implements PublishContract.Model {

    @Override
    public Observable<String> publishWorks(final WorkBean workBean) {
        return PublishServiceManager.createWork(workBean).map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                if (workBean != null) {
                    // 发布成功后插入数据库
                    // 返回作品id 设置
                    workBean.setWorksId(s);
                    if (workBean.getList() != null) {
                        for (WorkPhotoBean workPhotoBean : workBean.getList()) {
                            workPhotoBean.setWorksId(s);
                        }
                    }
//                    PublishDBManager.getInstance().addShotInfo(null, shotBean);
                }
                return s;
            }
        });
    }

    @Override
    public Observable<ShotBean> publishDatingShot(final ShotBean shotBean) {
        return PublishServiceManager.createDatingShot(shotBean).map(new Func1<String, ShotBean>() {
            @Override
            public ShotBean call(String s) {
                if (shotBean != null) {
                    // 发布成功后插入数据库
                    // 返回约拍id 设置
                    shotBean.setDatingShotId(s);
                    if (shotBean.getDatingShotImages() != null) {
                        for (ShotPhotoBean photoBean : shotBean.getDatingShotImages()) {
                            photoBean.setDatingShotId(s);
                        }
                    }
                    PublishDBManager.getInstance().addShotInfo(null, shotBean);
                }
                return shotBean;
            }
        });
    }
}
