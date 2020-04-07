package com.easy.framework.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.easy.apt.lib.InjectFragment;
import com.easy.framework.base.lifecyle.BaseLifecycleDialogFragment;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import javax.inject.Inject;

public abstract class BaseDialogFragment<P extends BasePresenter,V extends ViewDataBinding> extends BaseLifecycleDialogFragment implements BaseView {
    public V viewBind;
    public Context context;
    View rootView;
    @Inject
    protected P presenter;
    BasePopupView loadingDialog;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        InjectFragment.inject(this);
        if (presenter != null)
            presenter.attachView(this, context);
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

    private void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShow()) {
            loadingDialog.dismiss();
        }
    }

    private void showLoading() {
        if (loadingDialog == null) {
            loadingDialog = new XPopup.Builder(context).autoDismiss(false)
                    .asLoading("正在加载中")
                    .show();
        } else if (!loadingDialog.isShow()) {
            loadingDialog.show();
        }
    }

    @Override
    public void onDestroy() {
        hideLoading();
        super.onDestroy();
        if (presenter != null) {
            presenter.detachView();
        }
    }
}
