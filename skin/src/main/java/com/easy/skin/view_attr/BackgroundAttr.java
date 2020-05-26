package com.easy.skin.view_attr;

import android.graphics.drawable.Drawable;
import android.view.View;

import androidx.core.view.ViewCompat;

import com.easy.skin.SkinResourcesHelp;

public class BackgroundAttr extends SkinAttr {

    public BackgroundAttr(AttrType attrName, int id) {
        super(attrName, id);
    }

    @Override
    public void apply(View view) {
        Object background = SkinResourcesHelp.getInstance().getBackground(id);
        if (background instanceof Integer) {
            view.setBackgroundColor((Integer) background);
        } else {
            ViewCompat.setBackground(view, (Drawable) background);
        }
    }
}
