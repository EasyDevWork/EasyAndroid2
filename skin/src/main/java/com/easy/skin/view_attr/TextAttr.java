package com.easy.skin.view_attr;

import android.view.View;
import android.widget.TextView;

import com.easy.skin.SkinResourcesHelp;

//更换文字--可用于无感切换语言
public class TextAttr extends SkinAttr {

    public TextAttr(AttrType attrName, int id) {
        super(attrName, id);
    }

    @Override
    public void apply(View view) {
        String textContent = SkinResourcesHelp.getInstance().getTextContent(id);
        ((TextView) view).setText(textContent);
    }
}
