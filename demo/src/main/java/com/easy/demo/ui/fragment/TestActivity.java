package com.easy.demo.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestFragmentActivityBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.bean.AppVersion;
import com.easy.framework.manager.activity.ActivityStateLiveData;
import com.easy.framework.manager.activity.ActivityStateType;
import com.easy.framework.manager.network.NetworkStateLiveData;
import com.easy.framework.manager.screen.ScreenOrientation;
import com.easy.framework.manager.screen.ScreenOrientationLiveData;
import com.easy.framework.manager.screen.ScreenStateLiveData;
import com.easy.framework.manager.screen.ScreenStateType;
import com.easy.framework.manager.update.AppUpdateManager;
import com.easy.framework.statusbar.StatusBarUtil;
import com.easy.net.beans.Response;
import com.easy.utils.SystemUtils;
import com.easy.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

@ActivityInject
@Route(path = "/demo/TestFragmentActivity", name = "测试fragment的页面")
public class TestActivity extends BaseActivity<TestActivityPresenter, TestFragmentActivityBinding> implements TestActivityView {
    int i = 0;
    Observer activityStateObserver, screenStateObserver;
    boolean isAllow;

    @Override
    public int getLayoutId() {
        return R.layout.test_fragment_activity;
    }

    /**
     * //沉浸式重新该方法
     */
    @Override
    public void initStateBar() {
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
    }

    @Override
    public void initView() {

        activityStateObserver = (Observer<ActivityStateType>) activityStateType -> Log.d("StateChange", "ActivityState activityStateType=" + activityStateType.toString());
        ActivityStateLiveData.getInstance().observeForever(activityStateObserver);

        NetworkStateLiveData.getInstance(this).observe(this, networkType -> {
            viewBind.tvNetInfo.setText("当前网络：" + networkType.toString());
            Log.d("StateChange", "NetworkState networkInfo=" + networkType.toString());
        });

        ScreenOrientationLiveData screenOrientationLiveData = new ScreenOrientationLiveData(this);
        screenOrientationLiveData.observe(this, screenOrientation -> {
            Log.d("ScreenOrientation", screenOrientation.toString());
            viewBind.tvScreenOrientation.setText("屏幕方向:" + screenOrientation.orientation + " 角度：" + screenOrientation.rotateAngle);
        });

        screenStateObserver = (Observer<ScreenStateType>) type -> {
            viewBind.tvScreenInfo.setText("当前屏幕状态:" + type.toString());
            Log.d("StateChange", "ScreenState screenStateType=" + type.toString());
        };
        ScreenStateLiveData.getInstance(this).observeForever(screenStateObserver);


        viewBind.tvScreenBrightness.setText("屏幕亮度：" + SystemUtils.getScreenBrightness(this));

        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewBind.statusBarSpace.getLayoutParams();
        layoutParams.height = StatusBarUtil.getStatusBarHeight(this);
        viewBind.statusBarSpace.setLayoutParams(layoutParams);

        TestFragment ccccc = new TestFragment();
        Bundle bundlec = new Bundle();
        bundlec.putString("type", "CCCC");
        ccccc.setArguments(bundlec);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flContain, ccccc);
        transaction.commitAllowingStateLoss();

        List<Fragment> fragments = new ArrayList<>();
        TestFragment testFragment = new TestFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "AAAAA");
        testFragment.setArguments(bundle);
        fragments.add(testFragment);

        TestFragment testFragment2 = new TestFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putString("type", "BBBBB");
        testFragment2.setArguments(bundle2);
        fragments.add(testFragment2);

        TestActivityAdapter activityAdapter = new TestActivityAdapter(getSupportFragmentManager(), fragments);
        viewBind.pagerView.setAdapter(activityAdapter);
        viewBind.pagerView.setOffscreenPageLimit(fragments.size());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ActivityStateLiveData.getInstance().removeObserver(activityStateObserver);
        ScreenStateLiveData.getInstance(this).removeObserver(screenStateObserver);
    }

    /**
     * 要控制页面沉浸式与非沉浸式切换，可通过控制占位UI显隐来实现
     *
     * @param view
     */
    public void btn1(View view) {
        i++;
        if (i % 2 == 0) {
            ToastUtils.showShort("沉浸式");
            viewBind.statusBarSpace.setVisibility(View.GONE);
        } else {
            ToastUtils.showShort("非沉浸式");
            viewBind.statusBarSpace.setVisibility(View.VISIBLE);
        }
    }

    public void testException(View view) {
        int s = 1 / 0;
        Log.d("testException", "s=" + s);
    }

    public void btn2(View view) {
        i++;
        if (i % 2 == 0) {
            StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.color_c83c3c));
        } else {
            StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.color_008577));
        }
    }

    public void btn3(View view) {
        showLoading();
    }

    public void btn7(View view) {
        float screenBrightness = SystemUtils.getScreenBrightness(this);
        if (screenBrightness == 1) {
            i = -1;
        } else if (screenBrightness <= 0) {
            i = 1;
        }
        SystemUtils.setScreenBrightness(this, screenBrightness + (i * 0.1f));
        viewBind.tvScreenBrightness.setText("屏幕亮度：" + SystemUtils.getScreenBrightness(this));
        Log.d("StateChange", viewBind.tvScreenBrightness.getText().toString());
    }

    public void btn8(View view) {
        getRxPermissions()
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                .doOnDispose(() -> ToastUtils.showShort("被取消："))
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(aBoolean -> {
                    isAllow = aBoolean;
                    ToastUtils.showShort("是否允许：" + aBoolean);
                }, throwable -> ToastUtils.showShort("异常："));
    }

    public void btn9(View view) {
        presenter.requestAppVersion();
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
            }
        });
    }
}
