package com.easy.framework.base.mvp;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.ViewDataBinding;

import com.easy.apt.lib.InjectFragment;
import com.easy.framework.base.BaseFragment;
import com.easy.framework.rxlifecycle.FragmentEvent;

import javax.inject.Inject;

public abstract class BaseMvpFragment<P extends BaseMvpPresenter, V extends ViewDataBinding> extends BaseFragment<V> implements BaseMvpView<FragmentEvent> {
    @Inject
    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectFragment.inject(this);
        if (presenter != null)
            presenter.attachView(this, context);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
