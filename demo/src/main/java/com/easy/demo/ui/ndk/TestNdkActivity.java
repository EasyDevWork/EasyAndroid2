package com.easy.demo.ui.ndk;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.EmptyBinding;
import com.easy.demo.databinding.TestNdkBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;
import com.easy.ndk.NDKTools;

@ActivityInject
@Route(path = "/demo/TestNdkActivity", name = "NDK测试")
public class TestNdkActivity extends BaseActivity<EmptyPresenter, TestNdkBinding> implements EmptyView {

    @Override
    public int getLayoutId() {
        return R.layout.test_ndk;
    }

    @Override
    public void initView() {
        NDKTools.context = this;
        NDKTools.callStaticMethod();
        viewBind.tvNdk.setText("native 计算3+5=" + NDKTools.addNative(3, 5));
    }
}
