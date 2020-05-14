package com.easy.framework.base;

import android.os.Bundle;
import android.util.Log;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProviders;

import com.easy.framework.base.common.CommonActivity;
import com.easy.framework.manager.network.INetStateChange;
import com.easy.framework.manager.network.NetworkManager;
import com.easy.framework.manager.network.NetworkType;
import com.easy.framework.manager.screen.IScreenStateChange;
import com.easy.framework.manager.screen.ScreenManager;
import com.easy.framework.manager.screen.ScreenStateType;
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
public abstract class BaseMvvmActivity<Vm extends BaseViewModel, D extends ViewDataBinding> extends CommonActivity implements BaseView, INetStateChange, IScreenStateChange {
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
        NetworkManager.registerObserver(this);
        ScreenManager.registerObserver(this);
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
        NetworkManager.unRegisterObserver(this);
        ScreenManager.unRegisterObserver(this);
        super.onDestroy();
        if (viewBind != null) {
            viewBind.unbind();
        }
    }

    public abstract int getLayoutId();

    public abstract void initView();

    public abstract int initVariableId();

    @Override
    public void onNetDisconnected() {
        Log.d("onNetDisconnected", "无网络");
    }

    @Override
    public void onNetConnected(NetworkType networkType) {
        Log.d("onNetDisconnected", "有网络：" + networkType.name());
    }

    @Override
    public void onScreenState(ScreenStateType type) {
        Log.d("onScreenState", "屏幕状态：" + type.name());
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
