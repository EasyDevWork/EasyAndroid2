package com.easy.framework.base;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;

import com.easy.apt.lib.InjectFragment;
import com.easy.framework.base.common.CommonFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.AutoDisposeConverter;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import javax.inject.Inject;

public abstract class BaseFragment<P extends BasePresenter, V extends ViewDataBinding> extends CommonFragment implements BaseView {
    public V viewBind;
    View rootView;
    @Inject
    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectFragment.inject(this);
        if (presenter != null)
            presenter.attachView(context, this,this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBind = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        rootView = viewBind.getRoot();
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isCreateView = true;
        isLazyLoaded = false;
        handleLazyLoad();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        handleLazyLoad();
    }

    protected void handleLazyLoad() {
        if (isCreateView && isShow) {
            if (!isLazyLoaded) {
                isLazyLoaded = true;
                Log.d("LifecycleFragment", tag + "===>lazyInitData 执行");
                initView(rootView);
            } else {
                Log.d("LifecycleFragment", tag + "===>lazyInitData 不执行");
            }
        }
    }

    public abstract int getLayoutId();

    public abstract void initView(View view);

    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDetach();
    }

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
