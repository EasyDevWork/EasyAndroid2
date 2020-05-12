package com.easy.demo.ui.mvvm;

import com.easy.framework.base.BaseMvvmPresenter;

import javax.inject.Inject;

public class TestMvvmPresenter extends BaseMvvmPresenter<TestViewModel,TestMvvmView> {
    @Inject
    public TestMvvmPresenter() {

    }

    @Override
    public void attachViewModel(TestViewModel viewModel) {
        super.attachViewModel(viewModel);
    }
}