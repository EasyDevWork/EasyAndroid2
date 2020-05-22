package com.easy.demo.ui.rxjava;

import android.annotation.SuppressLint;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.EmptyBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleObserver;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


@ActivityInject
@Route(path = "/demo/TestRxJavaActivity", name = "Rxjava")
public class TestRxJavaActivity extends BaseActivity<EmptyPresenter, EmptyBinding> implements EmptyView {
    Disposable disposable;

    @Override
    public int getLayoutId() {
        return R.layout.empty;
    }


    @Override
    @SuppressLint("AutoDispose")
    public void initView() {

        Single.create((SingleOnSubscribe<String>) emitter -> {
            Log.d("initView", "subscribe thread:" + Thread.currentThread());
            emitter.onSuccess("ddd");
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<String>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable = d;
                        Log.d("initView", "onSubscribe thread:" + Thread.currentThread());
                    }

                    @Override
                    public void onSuccess(String s) {
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                        Log.d("initView", "onSuccess thread:" + Thread.currentThread());
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (disposable != null && !disposable.isDisposed()) {
                            disposable.dispose();
                        }
                        Log.d("initView", "onError thread:" + Thread.currentThread());
                    }
                });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();
        }
    }
}
