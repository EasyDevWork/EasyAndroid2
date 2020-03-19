package com.easy.app.ui.main;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.app.R;
import com.easy.app.databinding.MainBinding;
import com.easy.apt.annotation.ActivityInject;
import com.easy.common.base.CommonActivity;
import com.easy.common.bean.AppVersion;
import com.easy.common.manager.AppUpdateManager;
import com.easy.framework.bean.Response;
import com.easy.framework.manager.AppQuitManager;
import com.easy.framework.rxlifecycle.ActivityEvent;
import com.easy.framework.utils.ToastUtils;


@ActivityInject
@Route(path = "/app/MainActivity", name = "主页面")
public class MainActivity extends CommonActivity<MainPresenter, MainBinding> implements MainView<ActivityEvent> {

    AppQuitManager manager;
    boolean isAllow;
    AppVersion appVersion;

    @Override
    public int getLayoutId() {
        return R.layout.main;
    }

    @Override
    public void initView() {
        manager = new AppQuitManager();
        closeSwipeBackLayout();
        presenter.requestPermission(getRxPermissions());
    }

    public void updateVersion(View view) {
        presenter.requestAppVersion();
    }

    public void goDebug(View view) {
        ARouter.getInstance().build("/demo/DebugActivity").navigation();
    }

    @Override
    public void permissionCallback(Boolean granted, Throwable e) {
        if (e != null) {
            ToastUtils.showShort("异常：" + e.getMessage());
        } else {
            if (granted) {
                isAllow = true;
                if (appVersion != null) {
                    showUpdateDialog(appVersion);
                }
            }
        }
    }

    @Override
    public void appVersionCallback(Response<AppVersion> response) {
        if (response.isSuccess()) {
            showUpdateDialog(response.getResultObj());
        } else {
            ToastUtils.showShort(response.getMsg());
        }
    }

    public void showUpdateDialog(AppVersion version) {
        AppUpdateManager updateManager = new AppUpdateManager(this);
        updateManager.showUpdateDialog(version, new AppUpdateManager.AppUpdateCallback() {
            @Override
            public boolean permission() {
                return isAllow;
            }

            @Override
            public void permissionCallback() {
                ToastUtils.showLong(getString(R.string.need_premission));
                appVersion = version;
                presenter.requestPermission(getRxPermissions());
            }
        });
    }

    @Override
    public void onBackPressed() {
        manager.onBackPressed(this);
    }
}
