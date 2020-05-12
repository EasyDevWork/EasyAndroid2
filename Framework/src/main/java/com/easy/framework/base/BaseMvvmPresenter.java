package com.easy.framework.base;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

public class BaseMvvmPresenter<Vm extends BaseViewModel, V extends BaseView> extends BasePresenter<V> {

    protected Vm viewModel;

    public void attachViewModel(Vm viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void detachView() {
        super.detachView();
        if (viewModel != null) {
            viewModel.detach();
        }
    }

    @Inject
    public BaseMvvmPresenter() {

    }
}