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
        Drawable dLeft = null;
        if (lef != 0) {
            dLeft = SkinResourcesHelp.getInstance().getDrawable(lef);
        }
        Drawable dTop = null;
        if (top != 0) {
            dTop = SkinResourcesHelp.getInstance().getDrawable(top);
        }

        Drawable dRight = null;
        if (right != 0) {
            dRight = SkinResourcesHelp.getInstance().getDrawable(right);
        }

        Drawable dBottom = null;
        if (bottom != 0) {
            dBottom = SkinResourcesHelp.getInstance().getDrawable(bottom);
        }

        ((TextView) view).setCompoundDrawablesWithIntrinsicBounds(dLeft, dTop, dRight, dBottom);
    }
}
