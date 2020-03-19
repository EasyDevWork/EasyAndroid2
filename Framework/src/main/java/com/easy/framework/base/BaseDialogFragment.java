package com.easy.framework.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.easy.framework.base.lifecyle.BaseLifecycleDialogFragment;
import com.tbruyelle.rxpermissions2.RxPermissions;

public abstract class BaseDialogFragment<V extends ViewDataBinding> extends BaseLifecycleDialogFragment {
    public V viewBind;
    public Context context;
    View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewBind = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        rootView = viewBind.getRoot();
        initView(rootView);
        return rootView;
    }

    public abstract int getLayoutId();

    public abstract void initView(View view);

    public RxPermissions getRxPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(true);
        return rxPermissions;
    }
}
