package com.easy.common.ui.qr_code;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Vibrator;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.easy.apt.annotation.ActivityInject;
import com.easy.common.R;
import com.easy.common.base.CommonActivity;
import com.easy.common.databinding.QrScanBinding;
import com.easy.framework.rxlifecycle.ActivityEvent;
import com.easy.framework.utils.IntentUtils;
import com.easy.framework.utils.ToastUtils;

@ActivityInject
@Route(path = "/common/QrScanActivity", name = "二维码扫码")
public class QrScanActivity extends CommonActivity<QrScanPresenter, QrScanBinding> implements QrScanView<ActivityEvent>, QRCodeReaderView.OnQRCodeReadListener {

    private boolean hasPermission;
    Vibrator vibrator;
    long[] pattern;
    public final int CODE_SELECT_IMAGE = 2;

    @Override
    public int getLayoutId() {
        return R.layout.qr_scan;
    }

    @Override
    public void initView() {
        viewBind.ivBack.setOnClickListener(view -> finish());
        viewBind.ivMore.setOnClickListener(view ->
                presenter.requestPermission(getRxPermissions(), 2, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE));
        initVibrator();
        presenter.requestPermission(getRxPermissions(), 1, Manifest.permission.CAMERA, Manifest.permission.VIBRATE);
    }

    /**
     * 震动
     */
    private void initVibrator() {
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        pattern = new long[]{100, 400, 100, 400};   // 停止 开启 停止 开启
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hasPermission) {
            viewBind.qrView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasPermission) {
            viewBind.qrView.stopCamera();
        }
    }

    @Override
    public void permissionCallback(Boolean granted, int type, Throwable e) {
        if (e != null) {
            ToastUtils.showShort("error：" + e.getMessage());
        } else {
            if (granted) {
                hasPermission = true;
                if (type == 1) {
                    initQRCodeReaderView();
                } else if (type == 2) {
                    presenter.selectAlbum(this, CODE_SELECT_IMAGE);
                }
            } else {
                ToastUtils.showShort(getString(R.string.permission_not_allow));
            }
        }
    }

    @Override
    public void scanAlbumCallBack(String result, int code) {
        hideLoading();
        viewBind.qrView.setQRDecodingEnabled(true);
        if (code == 2) { //扫码异常
            ToastUtils.showShort(result);
        } else {
            //todo 扫码结果
            ToastUtils.showShort(result);
        }
    }

    private void initQRCodeReaderView() {
        viewBind.qrView.setAutofocusInterval(2000L);
        viewBind.qrView.setOnQRCodeReadListener(this);
        viewBind.qrView.setQRDecodingEnabled(true);
        viewBind.qrView.setBackCamera();
//        viewBind.qrView.setTorchEnabled(true);
        viewBind.qrView.startCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        vibrator.vibrate(pattern, -1);
        //todo 扫码结果
        ToastUtils.showShort(text);
        viewBind.qrView.setQRDecodingEnabled(false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    viewBind.qrView.setQRDecodingEnabled(false);
                    showLoading();
                    String picturePath = IntentUtils.selectPic(this, data);
                    presenter.scanAlbum(picturePath);
                }
                break;
        }
    }
}
