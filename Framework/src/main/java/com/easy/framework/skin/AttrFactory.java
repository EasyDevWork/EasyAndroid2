package com.easy.framework.skin;

import com.easy.framework.skin.view_attr.BackgroundAttr;
import com.easy.framework.skin.view_attr.BaseAttr;
import com.easy.framework.skin.view_attr.DividerAttr;
import com.easy.framework.skin.view_attr.ListSelectorAttr;
import com.easy.framework.skin.view_attr.SkinAttr;
import com.easy.framework.skin.view_attr.TextColorAttr;

public class AttrFactory {

    public static SkinAttr get(BaseAttr baseAttr) {
        SkinAttr mSkinAttr = null;
        switch (baseAttr.attrType) {
            case TextColorAttr:
                mSkinAttr = new TextColorAttr(baseAttr);
                break;
            case BackgroundAttr:
                mSkinAttr = new BackgroundAttr(baseAttr);
                break;
            case DividerAttr:
                mSkinAttr = new DividerAttr(baseAttr);
                break;
            case ListSelectorAttr:
                mSkinAttr = new ListSelectorAttr(baseAttr);
                break;
        }
        return mSkinAttr;
    }

    /**
     * Check whether the attribute is supported
     *
     * @param attrName
     * @return true : supported <br>
     * false: not supported
     */
    public static boolean isSupportedAttr(String attrName) {
        return false;
    }
}
