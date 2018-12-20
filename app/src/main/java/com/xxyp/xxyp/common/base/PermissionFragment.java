
package com.xxyp.xxyp.common.base;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

import java.util.List;

/**
 * Description : 基础权限fragment Created by sunpengfei on 2017/7/27. Person in
 * charge : sunpengfei
 */
public class PermissionFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    public boolean hasPermission(String... permission) {
        return false;
    }

    public void requestPermissions(String... permission) {
    }

    public void onPermissionDenied(List<String> permissions) {
    }

    public void onPermissionGranted(List<String> permissions) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
