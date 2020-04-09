package com.easy.demo.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestFragmentActivityBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.manager.network.NetworkManager;
import com.easy.framework.manager.network.NetworkType;
import com.easy.framework.manager.screen.ScreenStateType;
import com.easy.framework.statusbar.StatusBarUtil;
import com.easy.utils.SystemUtils;
import com.easy.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

@ActivityInject
@Route(path = "/demo/TestFragmentActivity", name = "测试fragment的页面")
public class TestActivity extends BaseActivity<TestActivityPresenter, TestFragmentActivityBinding> implements TestActivityView {
    int i = 0;

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

    public void btn4(View view) {
        getRxPermissions().request(Manifest.permission.CAMERA)
                .doOnDispose(() -> ToastUtils.showShort("被取消："))
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(aBoolean -> {
                    ToastUtils.showShort("是否允许：" + aBoolean);
                }, throwable -> ToastUtils.showShort("异常："));
    }

    public void btn5(View view) {
        ToastUtils.showShort(NetworkManager.getNetworkType().name());
    }

    public void btn6(View view) {
        ToastUtils.showShort("屏幕亮度：" + SystemUtils.getScreenBrightness(this));
    }

    public void btn7(View view) {
        i++;
        SystemUtils.setScreenBrightness(this, i % 2 == 0 ? 0 : 255);
    }

    @Override
    public void initView() {

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
    public void onNetDisconnected() {
        ToastUtils.showShort("无网络");
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        super.onNetConnected(networkType);
        ToastUtils.showShort("有网络：" + networkType.name());
    }


    @Override
    public void onScreenState(ScreenStateType type) {
        super.onScreenState(type);
        ToastUtils.showShort("屏幕状态：" + type.name());
    }
}
