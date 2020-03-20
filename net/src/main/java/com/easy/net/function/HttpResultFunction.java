package com.easy.net.function;

import androidx.annotation.NonNull;

import com.easy.net.exception.ExceptionEngine;

import io.reactivex.Observable;
import io.reactivex.functions.Function;

public class HttpResultFunction<T> implements Function<Throwable, Observable<T>> {
    @Override
    public Observable<T> apply(@NonNull Throwable throwable)  {
        return Observable.error(ExceptionEngine.handleException(throwable));
    }
}
