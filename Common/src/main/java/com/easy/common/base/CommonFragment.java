package com.easy.common.base;

import android.app.Activity;

import androidx.databinding.ViewDataBinding;

import com.easy.framework.base.mvp.BaseMvpFragment;
import com.easy.framework.rxlifecycle.FragmentEvent;

public abstract class CommonFragment<P extends CommonPresenter, V extends ViewDataBinding> extends BaseMvpFragment<P, V> implements CommonView<FragmentEvent> {

    @Override
    public void hideLoading() {
        Activity activity = getActivity();
        if (activity instanceof CommonActivity) {
            ((CommonActivity) activity).hideLoading();
        }
    }

    @Override
    public void showLoading() {
        Activity activity = getActivity();
        if (activity instanceof CommonActivity) {
            ((CommonActivity) activity).showLoading();
        }
    }
}
