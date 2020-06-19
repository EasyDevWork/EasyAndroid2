package com.easy.demo.ui.camera;

import android.Manifest;
import android.graphics.Point;
import android.hardware.Camera;
import android.view.View;
import android.widget.FrameLayout;

import androidx.lifecycle.Lifecycle;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.Glide;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestCameraBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.utils.DimensUtils;
import com.easy.utils.ToastUtils;

import java.io.File;

@ActivityInject
@Route(path = "/demo/TestCameraActivity", name = "相机测试")
public class TestCameraActivity extends BaseActivity<TestCameraPresenter, TestCameraBinding> implements TestCameraView {

    @Override
    public int getLayoutId() {
        return R.layout.test_camera;
    }

    boolean openFlash;

    @Override
    public void initView() {
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) viewBind.cameraView.getLayoutParams();
        layoutParams.width = DimensUtils.getWidth(this);
        layoutParams.height = layoutParams.width * 4 / 3;
        viewBind.cameraView.setLayoutParams(layoutParams);
        viewBind.cameraView.setBestSize(new Point(layoutParams.width, layoutParams.height));

        getRxPermissions()
                .request(Manifest.permission.CAMERA)
                .doOnDispose(() -> ToastUtils.showShort("被取消："))
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(aBoolean -> {
                    viewBind.cameraView.surfaceCreated(viewBind.cameraView.getHolder());//这个必须调用，否则给予权限后无法显示预览
                    viewBind.cameraView.setAutoFocusInterval(1500);
                    viewBind.cameraView.startCamera();
                }, throwable -> ToastUtils.showShort("异常："));
    }

    public void switchFlash(View v) {
        viewBind.cameraView.setFlashEnabled(!openFlash);
    }

    public void takePhoto(View v) {
        getRxPermissions().request(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)
                .doOnDispose(() -> ToastUtils.showShort("被取消："))
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(aBoolean -> {
                    Camera camera = viewBind.cameraView.getCamera();
                    if (camera != null) {
                        camera.takePicture(null, null, (bytes, jpeg) -> {
                            viewBind.cameraView.stopCamera();
                            Point size = new Point(viewBind.cameraView.getWidth(), viewBind.cameraView.getHeight());
                            presenter.saveBitmap(bytes, size, 0);
                        });
                    }
                }, throwable -> ToastUtils.showShort("异常："));
    }

    @Override
    public void photoAnalyse(File file) {
        if (file != null) {
            Glide.with(this).load("file://" + file).into(viewBind.image);
        }
    }
}
