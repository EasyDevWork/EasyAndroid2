package com.easy.app.ui.welcome;

import android.util.Log;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.app.BuildConfig;
import com.easy.app.R;
import com.easy.app.base.AppSharePreferences;
import com.easy.app.databinding.WelcomeBinding;
import com.easy.apt.annotation.ActivityInject;
import com.easy.apt.lib.SharePreference;
import com.easy.framework.base.BaseActivity;
import com.easy.utils.ToastUtils;

@ActivityInject
@Route(path = "/app/WelcomeActivity", name = "闪屏页")
public class WelcomeActivity extends BaseActivity<WelcomePresenter, WelcomeBinding> implements WelcomeView {

    @Override
    public int getLayoutId() {
        return R.layout.welcome;
    }

    @Override
    public void initView() {
        setBackground(R.color.colorPrimary);
        closeSwipeBackLayout();
        if (BuildConfig.DEBUG) {
            AppSharePreferences appShare = SharePreference.get(this, AppSharePreferences.class);
            appShare.setGoGuide(false);
            ARouter.getInstance().build("/demo/DebugActivity").navigation();
            finish();
        } else {
            presenter.countDown(1);
        }
    }

    @Override
    public void countDownCallback(long time) {
        Log.d("countDownCallback", "time:" + time);
        if (time == 0) {
            AppSharePreferences appShare = SharePreference.get(this, AppSharePreferences.class);
            ToastUtils.showShort("appShare:" + (appShare != null));
            boolean isGo = appShare != null && appShare.isGoGuide();
            if (isGo) {
                ARouter.getInstance().build("/app/GuideActivity")
                        .navigation(this, new NavCallback() {
                            @Override
                            public void onArrival(Postcard postcard) {
                                finish();
                            }
                        });
            } else {
                ARouter.getInstance().build("/app/MainActivity")
                        .navigation(this, new NavCallback() {
                            @Override
                            public void onArrival(Postcard postcard) {
                                finish();
                            }
                        });
            }
        } else if (time == -1) {
            finish();
        } else {
            viewBind.tvCountDown.setText(time + "s");
        }
    }

    @Override
    public void permissionCallback(Boolean granted, Throwable e) {
        ToastUtils.showShort("权限申请" + granted);
    }
}
