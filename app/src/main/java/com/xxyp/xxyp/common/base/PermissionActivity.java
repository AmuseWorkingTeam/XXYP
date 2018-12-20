
package com.xxyp.xxyp.common.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.xxyp.xxyp.common.log.XXLog;
import com.xxyp.xxyp.common.utils.permissions.PermissionsMgr;
import com.xxyp.xxyp.common.utils.permissions.PermissionsResultAction;

import java.util.Arrays;
import java.util.List;

/**
 * Description : 基础权限activity Created by sunpengfei on 2017/7/27. Person in
 * charge : sunpengfei
 */
public class PermissionActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public boolean hasPermission(String... permission) {
        return PermissionsMgr.getInstance().hasAllPermissions(this, permission);
    }

    public void requestPermissions(String... permissions) {
        if (!PermissionsMgr.getInstance().hasAllPermissions(this, permissions)) {
            PermissionsMgr.getInstance().requestPermissionsIfNecessaryForResult(this, permissions,
                    new PermissionsResultAction() {

                        @Override
                        public void onGranted(List<String> perms) {
                            XXLog.log_d(TAG, "Permission is Granted:" + perms);
                            onPermissionGranted(perms);
                        }

                        @Override
                        public void onDenied(List<String> perms) {
                            XXLog.log_d(TAG, "Permission is Denied" + perms);
                            onPermissionDenied(perms);
                        }
                    });
        } else {
            onPermissionGranted(Arrays.asList(permissions));
        }
    }

    public void onPermissionDenied(List<String> permissions) {
    }

    public void onPermissionGranted(List<String> permissions) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        PermissionsMgr.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

}
