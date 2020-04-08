package com.easy.demo.ui.fragment;

import android.Manifest;
import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestFragmentActivityBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.statusbar.StatusBarUtil;
import com.easy.utils.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

@ActivityInject
@Route(path = "/demo/TestFragmentActivity", name = "测试fragment的页面")
public class TestActivity extends BaseActivity<TestActivityPresenter, TestFragmentActivityBinding> implements TestActivityView {
    int i = 0;

    @Override
    public int getLayoutId() {
        return R.layout.test_fragment_activity;
    }

    @Override
    public void initStateBar() {
        //沉浸式重新该方法
        StatusBarUtil.setTranslucentStatus(this);
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
    }

    public void btn1(View view) {
        i++;
        if (i % 2 == 0) {
            StatusBarUtil.setTranslucentStatus(this);
            StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        } else {
            StatusBarUtil.setRootViewFitsSystemWindows(this, true);
            StatusBarUtil.setStatusBarColor(this, getResources().getColor(R.color.color_c83c3c));
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
                .doOnDispose(new Action() {
                    @Override
                    public void run() throws Exception {
                        ToastUtils.showShort("被取消：");
                    }
                })
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(aBoolean -> {
                    ToastUtils.showShort("是否允许：" + aBoolean);
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        ToastUtils.showShort("异常：");
                    }
                });
    }

    @Override
    public void initView() {
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
}
