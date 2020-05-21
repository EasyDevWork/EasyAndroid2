package com.easy.framework.skin;

import android.view.View;

import com.easy.framework.skin.view_attr.IApply;

import java.util.List;

public class SkinView {
    View view;
    List<IApply> params;

    public SkinView(View view, List<IApply> params) {
        this.view = view;
        this.params = params;
    }

    public View getView() {
        return view;
    }

    public void setView(View view) {
        this.view = view;
    }

    /**
     * 加载属性
     */
    public void applySkin() {
        for (IApply param : params) {
            param.apply(view);
        }
    }
}
