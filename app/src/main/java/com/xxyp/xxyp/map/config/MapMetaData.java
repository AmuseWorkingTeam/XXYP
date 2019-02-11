
package com.xxyp.xxyp.map.config;


import com.xxyp.xxyp.common.utils.AppContextUtils;

/**
 * Description : Meta Data 数据提供
 * Created by mc on 2017/2/25 15:41.
 * Job number：135033
 * Phone ：18610413765
 * Email：wangyue@syswin.com
 * Person in charge : mc
 * Leader：mc
 */
public class MapMetaData {
    /*
     * 文件夹目录
     */
    public static String TOON_FILE_NAME = "toon";

    // init
    static {
        try {
            String packageName = AppContextUtils.getAppContext().getPackageName();
            String[] str = packageName.split("\\.");
            TOON_FILE_NAME = str[str.length - 1];
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
