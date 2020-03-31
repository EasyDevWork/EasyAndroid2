package com.easy.demo.ui.arouter;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestArouterBinding;
import com.easy.framework.arouter.FrameWork2AppProvider;
import com.easy.framework.arouter.RouterInterceptorManager;
import com.easy.framework.base.BaseActivity;
import com.easy.net.event.ActivityEvent;

@ActivityInject
@Route(path = "/demo/TestArouterActivity", name = "路由测试")
public class TestArouterActivity extends BaseActivity<TestAroterPresenter, TestArouterBinding> implements TestArouterView<ActivityEvent> {

    private String intercepterPath = "/demo/TestLottieActivity2";

    @Autowired(name = "/framework/FrameWork2AppProvider")
    FrameWork2AppProvider provider;

    @Override
    public int getLayoutId() {
        return R.layout.test_arouter;
    }

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
    }

    public void testCancelInterceptor(View view) {
        RouterInterceptorManager.getInstance().removeInterceptor(intercepterPath);
    }

    public void testInterceptor(View view) {
        RouterInterceptorManager.getInstance().setInterceptor(intercepterPath);
    }

    public void testJumper(View view) {
        ARouter.getInstance().build(intercepterPath).navigation();
    }

    public void testProvider1(View view) {
        ((FrameWork2AppProvider) ARouter.getInstance().build("/framework/FrameWork2AppProvider").navigation()).showToast("testProvider1");
    }

    public void testProvider2(View view) {
        provider.showToast("testProvider2");
    }
}
