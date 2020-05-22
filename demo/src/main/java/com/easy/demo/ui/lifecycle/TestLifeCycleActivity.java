package com.easy.demo.ui.lifecycle;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestLifecycleBinding;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestLifeCycleActivity", name = "测试业务生命周期")
public class TestLifeCycleActivity extends BaseActivity<TestLifeCyclePresenter, TestLifecycleBinding> implements TestLifeCycleView {

    @Override
    public int getLayoutId() {
        return R.layout.test_lifecycle;
    }

    @Override
    public void initView() {
        StringBuilder builder = new StringBuilder();
        builder.append("测试业务的生命周期绑定");
        viewBind.tvDescribe.setText(builder.toString());
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void callback(String result) {
        viewBind.tvTips.setText(result);
    }
}
