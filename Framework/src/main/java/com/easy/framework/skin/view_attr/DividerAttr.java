package com.easy.framework.skin.view_attr;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.ListView;

import com.easy.framework.skin.SkinManager;

public class DividerAttr extends SkinAttr {

    public int dividerHeight = 1;

    public DividerAttr(BaseAttr baseAttr) {
        super(baseAttr);
    }

    @Override
    public void apply(View view) {
        if (view instanceof ListView) {
            ListView tv = (ListView) view;
            if (AttrResType.COLOR.equals(baseAttr.attrResType)) {
                int color = SkinManager.getInstance().getColor(baseAttr.attrResRef);
                ColorDrawable sage = new ColorDrawable(color);
                tv.setDivider(sage);
                tv.setDividerHeight(dividerHeight);
            } else if (AttrResType.DRAWABLE.equals(baseAttr.attrResType)) {
                tv.setDivider(SkinManager.getInstance().getDrawable(baseAttr.attrResRef));
            }
        }
    }
}
