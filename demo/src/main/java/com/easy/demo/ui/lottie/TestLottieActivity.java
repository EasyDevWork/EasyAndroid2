package com.easy.demo.ui.lottie;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestLottieBinding;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestLottieActivity", name = "Lottie动画")
public class TestLottieActivity extends BaseActivity<TestLottiePresenter, TestLottieBinding> implements TestLottieView {

    @Override
    public int getLayoutId() {
        return R.layout.test_lottie;
    }

    @Override
    public void initView() {
        viewBind.lottie.setAnimationFromUrl("https://assets2.lottiefiles.com/packages/lf20_bGW6Oe.json");
    }

    public void testLottieAnimation(View view) {
        viewBind.lottie.playAnimation();
    }
}
