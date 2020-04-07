package com.easy.demo.ui.lifecycle;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.EmptyBinding;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestLifeCycleActivity", name = "测试业务生命周期")
public class TestLifeCycleActivity extends BaseActivity<TestLifeCyclePresenter, EmptyBinding> implements TestLifeCycleView {

    @Override
    public int getLayoutId() {
        return R.layout.test_lifecycle;
    }

    @Override
    public void initView() {

    }

    public void bindLifeCycle(View view) {
        presenter.clickBindLifeCycle();
    }

    public void bindUntilEvent(View view) {
        presenter.clickBindUntilEvent();
    }

    public void bindLifeCycle2(View view) {
        presenter.bindLifeCycle2();
    }

    public void goActivity(View view) {
        ARouter.getInstance().build("/demo/TransparentActivity").navigation();
    }
}
