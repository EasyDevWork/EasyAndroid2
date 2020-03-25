package com.easy.demo.ui.debug;

import android.graphics.Bitmap;

import com.easy.qrcode.ui.qr_code.qrcode.BarCodeCreate;
import com.easy.qrcode.ui.qr_code.qrcode.QRCodeCreate;
import com.easy.framework.base.BasePresenter;
import com.easy.framework.base.DataObservable;
import com.easy.framework.base.DataObserver;
import com.easy.net.event.ActivityEvent;
import com.easy.store.dao.CacheDao;
import com.easy.utils.DimensUtils;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;

public class DebugPresenter extends BasePresenter<DebugView> {
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



}