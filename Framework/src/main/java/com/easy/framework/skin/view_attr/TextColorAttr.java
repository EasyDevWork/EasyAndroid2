package com.easy.framework.skin.view_attr;

import android.view.View;
import android.widget.TextView;

import com.easy.framework.skin.SkinResourcesHelp;

public class TextColorAttr extends SkinAttr {

    public TextColorAttr(AttrType attrName, int id) {
        super(attrName, id);
    }

    @Override
    public void apply(View view) {
        ((TextView) view).setTextColor(SkinResourcesHelp.getInstance().getColorStateList(id));
    }
}
