package com.easy.framework.skin.view_attr;

import android.view.View;

public abstract class SkinAttr {

    BaseAttr baseAttr;

    public SkinAttr(BaseAttr baseAttr) {
        this.baseAttr = baseAttr;
    }

    public abstract void apply(View view);
}
