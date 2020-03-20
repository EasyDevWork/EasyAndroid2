package com.easy.demo.ui.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.common.base.CommonActivity;
import com.easy.demo.R;
import com.easy.demo.databinding.TestFragmentActivityBinding;
import com.easy.net.rxlifecycle.ActivityEvent;

import java.util.ArrayList;
import java.util.List;

@ActivityInject
@Route(path = "/demo/TestFragmentActivity", name = "测试fragment的页面")
public class TestFragmentActivity extends CommonActivity<TestFragmentActivityPresenter, TestFragmentActivityBinding> implements TestFragmentActivityView<ActivityEvent> {

    @Override
    public int getLayoutId() {
        return R.layout.test_fragment_activity;
    }

    @Override
    public void initView() {

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

        TestFragmentActivityAdapter activityAdapter = new TestFragmentActivityAdapter(getSupportFragmentManager(), fragments);
        viewBind.pagerView.setAdapter(activityAdapter);
        viewBind.pagerView.setOffscreenPageLimit(fragments.size());
    }
}
