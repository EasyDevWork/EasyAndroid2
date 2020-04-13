package com.easy.demo.ui.aop;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.aop.CostTime;
import com.easy.demo.aop.SingleClick;
import com.easy.demo.aop.TestAop;
import com.easy.demo.databinding.TestAopBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.utils.ToastUtils;

@ActivityInject
@Route(path = "/demo/TestAopActivity", name = "Aop测试")
public class TestAopActivity extends BaseActivity<TestAopPresenter, TestAopBinding> implements TestAopView {

    @Override
    public int getLayoutId() {
        return R.layout.test_aop;
    }

    @Override
    public void initView() {

    }

    @CostTime
    @SingleClick
    @TestAop
    public void click1(View view) {
        ToastUtils.showShort("点击了");
    }
}
