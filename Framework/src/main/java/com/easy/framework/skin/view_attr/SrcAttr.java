package com.easy.framework.skin.view_attr;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.easy.framework.skin.SkinResourcesHelp;

public class SrcAttr extends SkinAttr {

    public SrcAttr(AttrType attrName, int id) {
        super(attrName, id);
    }

    @Override
    public void apply(View view) {
        Object background = SkinResourcesHelp.getInstance().getBackground(id);
        if (background instanceof Integer) {
            ((ImageView) view).setImageDrawable(new ColorDrawable((Integer) background));
        } else {
            ((ImageView) view).setImageDrawable((Drawable) background);
        }
    }
}
