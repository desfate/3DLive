package com.futrtch.live.base;

import android.app.Activity;
import android.content.Context;

import com.futrtch.live.utils.ToastUtil;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class PermissionTools {
    /**
     * 请求权限
     * @param context 上下文
     * @param interfaces 权限请求执行成功后可执行方法
     * @param permissions 权限列表
     */
    public static void requestPermission(Context context, PermissionSuccess interfaces, String... permissions){
        XXPermissions.with((Activity) context)
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermission() {
                    @Override
                    public void hasPermission(List<String> granted, boolean all) {
                        if (all) {
                            Optional.ofNullable(interfaces).ifPresent(PermissionSuccess::onSuccess);
                        } else {
                            ToastUtil.showToast(context, "获取权限成功，部分权限未正常授予");
                        }
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean never) {
                        if (never) {
                            XXPermissions.startPermissionActivity(context, denied);
                        } else {
                            ToastUtil.showToast(context, "获取权限失败");
                        }
                    }
                });
    }

    public interface PermissionSuccess{
        void onSuccess();
    }
}
