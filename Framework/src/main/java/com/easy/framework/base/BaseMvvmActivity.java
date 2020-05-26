package com.easy.framework.base;

import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;

import com.easy.framework.base.common.CommonActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import javax.inject.Inject;

/**
 * 添加通用的功能
 */
public abstract class BaseMvvmActivity<Vm extends BaseViewModel, D extends ViewDataBinding> extends CommonActivity implements BaseView {
    public D viewBind;
    @Inject
    public Vm viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewBind = DataBindingUtil.setContentView(this, getLayoutId());
        viewBind.setLifecycleOwner(this);
        initViewModel();
        initStateBar();
        initView();
    }

    private void initViewModel() {
        Class modelClass;
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            throw new NullPointerException("viewModel is null");
        }
        viewModel = (Vm) ViewModelProviders.of(this).get(modelClass);
        viewModel.attach(this);
        viewBind.setVariable(initVariableId(), viewModel);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (viewBind != null) {
            viewBind.unbind();
        }
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract int initVariableId();

    public RxPermissions getRxPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);
        return rxPermissions;
    }

    public <T> AutoDisposeConverter<T> getAutoDispose() {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this));
    }

    public <T> AutoDisposeConverter<T> getAutoDispose(Lifecycle.Event untilEvent) {
        return AutoDispose.autoDisposable(AndroidLifecycleScopeProvider.from(this, untilEvent));
    }

}
