package com.easy.framework.base;

import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel<D extends ViewDataBinding> extends ViewModel {

    public LifecycleOwner owner;
    public D viewBind;

    public void attach(D viewDataBinding, LifecycleOwner owner) {
        this.viewBind = viewDataBinding;
        this.owner = owner;
    }

    public void detach() {
        if (viewBind != null) {
            viewBind.unbind();
            viewBind = null;
        }
        if (owner != null) {
            owner = null;
        }
    }
}
