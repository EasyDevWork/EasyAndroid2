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

import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
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
        disposable = Single.create((SingleOnSubscribe<String>) emitter -> emitter.onSuccess(null))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(integer -> Log.d("TestRxJavaActivity", "i=" + integer), throwable -> {
                    Log.d("TestRxJavaActivity", "throwable==>" + throwable.getMessage());
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
