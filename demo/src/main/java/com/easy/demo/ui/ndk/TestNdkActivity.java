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
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    public int getLayoutId() {
        return R.layout.test_ndk;
    }

    @Override
    public void initView() {
        String content = NDKTools.stringFromJNI();
        viewBind.tvNdk.setText(content);
    }
}
