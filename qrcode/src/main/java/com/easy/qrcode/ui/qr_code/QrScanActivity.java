package com.easy.qrcode.ui.qr_code;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.os.Vibrator;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.easy.apt.annotation.ActivityInject;
import com.easy.framework.base.BaseActivity;
import com.easy.net.event.ActivityEvent;
import com.easy.qrcode.R;
import com.easy.qrcode.databinding.QrScanBinding;
import com.easy.utils.IntentUtils;
import com.easy.utils.ToastUtils;

@ActivityInject
@Route(path = "/qrCode/QrScanActivity", name = "二维码扫码")
public class QrScanActivity extends BaseActivity<QrScanPresenter, QrScanBinding> implements QrScanView<ActivityEvent>, QRCodeReaderView.OnQRCodeReadListener {

    private boolean hasPermission;
    Vibrator vibrator;
    long[] pattern;
    public final int CODE_SELECT_IMAGE = 2;
    QRCodeReaderView qrCodeReaderView;

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
        if (hasPermission && qrCodeReaderView != null) {
            qrCodeReaderView.startCamera();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hasPermission && qrCodeReaderView != null) {
            qrCodeReaderView.stopCamera();
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
        if (qrCodeReaderView != null) {
            qrCodeReaderView.setQRDecodingEnabled(true);
        }
        if (code == 2) { //扫码异常
            ToastUtils.showShort(result);
        } else {
            //todo 扫码结果
            ToastUtils.showShort(result);
        }
    }

    private void initQRCodeReaderView() {
        qrCodeReaderView = new QRCodeReaderView(this);
        qrCodeReaderView.setAutofocusInterval(2000L);
        qrCodeReaderView.setOnQRCodeReadListener(this);
        qrCodeReaderView.setQRDecodingEnabled(true);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewBind.flContain.addView(qrCodeReaderView,0, layoutParams);
//       qrCodeReaderView.setTorchEnabled(true);
        qrCodeReaderView.startCamera();
    }

    @Override
    public void onQRCodeRead(String text, PointF[] points) {
        vibrator.vibrate(pattern, -1);
        //todo 扫码结果
        ToastUtils.showShort(text);
        if (qrCodeReaderView != null) {
            qrCodeReaderView.setQRDecodingEnabled(false);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CODE_SELECT_IMAGE:
                if (resultCode == RESULT_OK) {
                    if (qrCodeReaderView != null) {
                        qrCodeReaderView.setQRDecodingEnabled(false);
                    }
                    showLoading();
                    String picturePath = IntentUtils.selectPic(this, data);
                    presenter.scanAlbum(picturePath);
                }
                break;
        }
    }
}
