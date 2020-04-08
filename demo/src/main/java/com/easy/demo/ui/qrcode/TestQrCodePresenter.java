package com.easy.demo.ui.qrcode;

import android.graphics.Bitmap;

import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;
import com.easy.qrcode.ui.qr_code.qrcode.BarCodeCreate;
import com.easy.qrcode.ui.qr_code.qrcode.QRCodeCreate;
import com.easy.utils.DimensUtils;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class TestQrCodePresenter extends BasePresenter<TestQrCodeView> {
    @Inject
    public TestQrCodePresenter() {

    }

    public void createQrCode(final String content, int size, int type) {
        Observable.create((ObservableOnSubscribe<Bitmap>) e -> {
            int width = DimensUtils.dp2px(getContext(), size);
            Bitmap bitmap;
            if (type == 1) {
                bitmap = QRCodeCreate.createQRCodeBitmap(content, width, width);
            } else {
                bitmap = BarCodeCreate.createBarCode(content, width, width);
            }
            if (bitmap == null) {
                e.onError(null);
            } else {
                e.onNext(bitmap);
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(bitmap -> mvpView.qRCodeCallback(bitmap), throwable -> mvpView.qRCodeCallback(null));
    }
}