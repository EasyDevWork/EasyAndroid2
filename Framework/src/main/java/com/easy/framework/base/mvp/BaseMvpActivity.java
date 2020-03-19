package com.easy.framework.base.mvp;

import android.os.Bundle;

import androidx.databinding.ViewDataBinding;

import com.easy.apt.lib.InjectActivity;
import com.easy.framework.base.BaseActivity;
import com.easy.framework.rxlifecycle.ActivityEvent;

import javax.inject.Inject;


public abstract class BaseMvpActivity<P extends BaseMvpPresenter, V extends ViewDataBinding> extends BaseActivity<V> implements BaseMvpView<ActivityEvent> {

    @Inject
    public P presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        supportMvp = true;
        super.onCreate(savedInstanceState);
        InjectActivity.inject(this);
        if (presenter != null) {
            presenter.attachView(this,context);
        }
        initView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
