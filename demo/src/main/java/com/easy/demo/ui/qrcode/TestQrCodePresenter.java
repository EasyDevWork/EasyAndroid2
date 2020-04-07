package com.easy.demo.ui.qrcode;

import android.graphics.Bitmap;

import com.easy.framework.base.BasePresenter;
import com.easy.framework.observable.DataObserver;
import com.easy.qrcode.ui.qr_code.qrcode.BarCodeCreate;
import com.easy.qrcode.ui.qr_code.qrcode.QRCodeCreate;
import com.easy.utils.DimensUtils;
import com.trello.rxlifecycle3.android.ActivityEvent;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class TestQrCodePresenter extends BasePresenter<TestQrCodeView> {
    @Inject
    public TestQrCodePresenter() {

    }

    public void createQrCode(final String content, int size, int type) {
        bindObservable(Observable.create((ObservableOnSubscribe<Bitmap>) e -> {
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
        })).lifecycleProvider(getRxLifecycle())
                .activityEvent(ActivityEvent.DESTROY)
                .observe(new DataObserver<Bitmap>() {
                    @Override
                    protected void onSuccess(Bitmap bitmap) {
                        mvpView.qRCodeCallback(bitmap);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mvpView.qRCodeCallback(null);
                    }
                });
    }
}