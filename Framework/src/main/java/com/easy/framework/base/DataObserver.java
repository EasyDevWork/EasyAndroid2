package com.easy.framework.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public abstract class DataObserver<T> implements Observer<T> {


    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(T value) {
        onSuccess(value);
    }

    @Override
    public void onSubscribe(Disposable d) {

    }

    protected abstract void onSuccess(T value);
}
