package com.easy.demo.ui.qrcode;

import android.graphics.Bitmap;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestQrcodeBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.utils.DimensUtils;

@ActivityInject
@Route(path = "/demo/TestQrCodeActivity", name = "二维码测试")
public class TestQrCodeActivity extends BaseActivity<TestQrCodePresenter, TestQrcodeBinding> implements TestQrCodeView {

    @Override
    public int getLayoutId() {
        return R.layout.test_qrcode;
    }

    @Override
    public void initView() {
        addTitleView().setTitleText("二维码测试");
    }

    public void createQrCode(View view) {
        presenter.createQrCode("hello word !!!", DimensUtils.dp2px(this, 200), 1);
    }

    public void createBarCode(View view) {
        presenter.createQrCode("23232323", DimensUtils.dp2px(this, 200), 2);
    }

    public void goQrScan(View view) {
        ARouter.getInstance().build("/qrCode/QrScanActivity").navigation();
    }

    @Override
    public void qRCodeCallback(Bitmap bitmap) {
        if (bitmap != null) {
            viewBind.ivScreen.setVisibility(View.VISIBLE);
            viewBind.ivScreen.setImageBitmap(bitmap);
        }
    }
}
