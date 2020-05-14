package com.easy.demo.ui.rxjava;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.EmptyBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


@ActivityInject
@Route(path = "/demo/TestRxJavaActivity", name = "Rxjava")
public class TestRxJavaActivity extends BaseActivity<EmptyPresenter, EmptyBinding> implements EmptyView {

    @Override
    public int getLayoutId() {
        return R.layout.empty;
    }


    @Override
    @SuppressLint("AutoDispose")
    public void initView() {
        Disposable disposable = Observable.just(1, 2, 3)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> Log.d("TestRxJavaActivity", "i=" + integer), throwable -> {

                });

    }
}
