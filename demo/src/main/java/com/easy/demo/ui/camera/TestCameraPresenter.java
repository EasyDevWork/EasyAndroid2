package com.easy.demo.ui.camera;

import android.graphics.Point;

import androidx.lifecycle.Lifecycle;

import com.easy.framework.base.BasePresenter;
import com.easy.utils.BitmapUtil;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class TestCameraPresenter extends BasePresenter<TestCameraView> {
    @Inject
    public TestCameraPresenter() {

    }

    void saveBitmap(byte[] datas, Point size, int top) {
        Observable.just(datas)
                .map(bytes -> BitmapUtil.saveBitmap(getContext(), bytes, size, top, "bankcard"))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .as(getAutoDispose(Lifecycle.Event.ON_DESTROY))
                .subscribe(file -> mvpView.photoAnalyse(file),
                        throwable -> {
                            mvpView.photoAnalyse(null);
                        });
    }
}