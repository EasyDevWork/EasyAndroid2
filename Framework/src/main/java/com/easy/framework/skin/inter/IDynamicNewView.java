package com.easy.framework.skin.inter;

import android.view.View;

import com.easy.framework.skin.view_attr.DynamicAttr;

import java.util.List;

public interface IDynamicNewView {
    void dynamicAddView(View view, List<DynamicAttr> pDAttrs);
}
