package com.easy.framework.skin.view_attr;

import android.graphics.Typeface;
import android.view.View;
import android.widget.TextView;

import com.easy.framework.skin.SkinResourcesHelp;

public class TypeFaceAttr extends SkinAttr {

    public TypeFaceAttr(AttrType attrName, int id) {
        super(attrName, id);
    }

    @Override
    public void apply(View view) {
        Typeface typeface = SkinResourcesHelp.getInstance().getTypeface(id);
        ((TextView)view).setTypeface(typeface);
    }
}
