package com.easy.framework.base;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.easy.apt.lib.InjectActivity;
import com.easy.framework.base.common.CommonActivity;

import javax.inject.Inject;

public abstract class BaseActivity<P extends BasePresenter, V extends ViewDataBinding> extends CommonActivity implements BaseView {

    public V viewBind;
    @Inject
    public P presenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = DataBindingUtil.setContentView(this, getLayoutId());
        initStateBar();
        InjectActivity.inject(this);
        if (presenter != null) {
            presenter.attachView(this, context);
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

    public abstract int getLayoutId();

    public abstract void initView();





}
