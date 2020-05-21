package com.easy.framework.skin.view_attr;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.easy.framework.skin.SkinResourcesHelp;

public class DrawableAttr implements IApply {
    public int lef, right, top, bottom;

    public DrawableAttr() {

    }

    public void setLef(int lef) {
        this.lef = lef;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public void setTop(int top) {
        this.top = top;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    @Override
    public void apply(View view) {
        Drawable dLeft = SkinResourcesHelp.getInstance().getDrawable(lef);
        Drawable dTop = SkinResourcesHelp.getInstance().getDrawable(top);
        Drawable dRight = SkinResourcesHelp.getInstance().getDrawable(right);
        Drawable dBottom = SkinResourcesHelp.getInstance().getDrawable(bottom);

        if (null != dLeft || null != dTop || null != dRight || null != dBottom) {
            ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(dLeft, dTop, dRight, dBottom);
        }
    }
}
