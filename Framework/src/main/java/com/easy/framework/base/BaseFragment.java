package com.easy.framework.base;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.easy.apt.lib.InjectFragment;
import com.easy.framework.base.lifecyle.BaseLifecycleFragment;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.android.FragmentEvent;

import javax.inject.Inject;

public abstract class BaseFragment<P extends BasePresenter, V extends ViewDataBinding> extends BaseLifecycleFragment implements BaseView {
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
            presenter.attachView(context, this, getRxLifecycle());
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
    public void onDestroyView() {
        hideLoading();
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        if (presenter != null) {
            presenter.detachView();
        }
        super.onDetach();
    }
}
