package com.easy.framework.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.easy.apt.lib.InjectFragment;

import javax.inject.Inject;

public abstract class BaseDialogFragment<P extends BasePresenter, V extends ViewDataBinding> extends AppCompatDialogFragment implements BaseView {
    public V viewBind;
    public Context context;
    View rootView;
    @Inject
    protected P presenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        InjectFragment.inject(this);
        if (presenter != null)
            presenter.attachView(context, this,this);
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

    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDetach();
    }
}
