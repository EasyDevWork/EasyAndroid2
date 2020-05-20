package com.easy.framework.skin.view_attr;

import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.easy.framework.skin.SkinManager;

public class BackgroundAttr extends SkinAttr {

    public BackgroundAttr(BaseAttr baseAttr) {
        super(baseAttr);
    }

    @Override
    public void apply(View view) {
        if (AttrResType.COLOR.equals(baseAttr.attrResType)) {
            view.setBackgroundColor(SkinManager.getInstance().getColor(baseAttr.attrResRef));
            Log.i("attr", "apply as color");
        } else if (AttrResType.DRAWABLE.equals(baseAttr.attrResType)) {
            Drawable bg = SkinManager.getInstance().getDrawable(baseAttr.attrResRef);
            view.setBackground(bg);
            Log.i("attr", "apply as drawable");
            Log.i("attr", "bg.toString()  " + bg.toString());
        }
    }
}
