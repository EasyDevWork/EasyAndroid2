package com.easy.demo.ui.camera;

import android.Manifest;
import android.view.View;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestCameraBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;
import com.easy.utils.ToastUtils;

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

        getRxPermissions()
                .request(Manifest.permission.CAMERA)
                .doOnDispose(() -> ToastUtils.showShort("被取消："))
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(aBoolean -> {
                    viewBind.cameraView.surfaceCreated(viewBind.cameraView.getHolder());//这个必须调用，否则给予权限后无法显示预览
                    viewBind.cameraView.setAutofocusInterval(2000);
                    viewBind.cameraView.startCamera();
                }, throwable -> ToastUtils.showShort("异常："));
    }
}
