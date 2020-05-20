package com.easy.framework.skin.view_attr;

import android.view.View;
import android.widget.AbsListView;

import com.easy.framework.skin.SkinManager;

public class ListSelectorAttr extends SkinAttr {

    public ListSelectorAttr(BaseAttr baseAttr) {
        super(baseAttr);
    }

    @Override
    public void apply(View view) {
        if(view instanceof AbsListView){
            AbsListView tv = (AbsListView)view;
            if(AttrResType.COLOR.equals(baseAttr.attrResType)){
                tv.setSelector(SkinManager.getInstance().getColor(baseAttr.attrResRef));
            }else if(AttrResType.DRAWABLE.equals(baseAttr.attrResType)){
                tv.setSelector(SkinManager.getInstance().getDrawable(baseAttr.attrResRef));
            }
        }
    }
}
