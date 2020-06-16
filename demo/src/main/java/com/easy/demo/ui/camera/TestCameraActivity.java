package com.easy.demo.ui.camera;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestCameraBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestCameraActivity", name = "相机测试")
public class TestCameraActivity extends BaseActivity<EmptyPresenter, TestCameraBinding> implements EmptyView {

    @Override
    public int getLayoutId() {
        return R.layout.test_camera;
    }

    @Override
    public void initView() {


    }

    public void openCamera(View v) {
        viewBind.cameraView.setAutofocusInterval(2000);
        viewBind.cameraView.startCamera();
    }
}
