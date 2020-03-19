package com.easy.demo.ui.debug;

import android.graphics.Bitmap;

import com.easy.common.base.CommonPresenter;
import com.easy.common.ui.qr_code.qrcode.BarCodeCreate;
import com.easy.common.ui.qr_code.qrcode.QRCodeCreate;
import com.easy.framework.base.DataObservable;
import com.easy.framework.base.DataObserver;
import com.easy.framework.rxlifecycle.ActivityEvent;
import com.easy.framework.utils.DimensUtils;
import com.easy.store.dao.CacheDao;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class DebugPresenter extends CommonPresenter<DebugView> {
    @Inject
    public DebugPresenter() {

    }

    @Inject
    Lazy<CacheDao> cacheDao;

    public void setTestCache(String test) {
        cacheDao.get().write("test", test);
    }

    public String getTestCache() {
        return cacheDao.get().read("test", "default");
    }


    public void createQrCode(final String content, int size, int type) {
        DataObservable.builder(Observable.create((ObservableOnSubscribe<Bitmap>) e -> {
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
        })).lifecycleProvider(mvpView.getRxLifecycle())
                .activityEvent(ActivityEvent.DESTROY)
                .dataObserver(new DataObserver<Bitmap>() {
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