package com.easy.framework.skin;

import android.util.AttributeSet;
import android.view.View;

import com.easy.framework.skin.view_attr.AttrType;
import com.easy.framework.skin.view_attr.IApply;
import com.easy.framework.skin.view_attr.SkinAttrParam;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SkinAttribute {

    public static final List<AttrType> list = Arrays.asList(AttrType.values());

    private ArrayList<SkinView> skinViews = new ArrayList<>();

    /**
     * 保存view，分解属性，并对view进行换肤处理（当前皮肤可能不是默认时需要更换）
     *
     * @param view
     * @param attrs
     */
    public void loadView(View view, AttributeSet attrs) {
        ArrayList<SkinAttrParam> skinAttrParams = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attributeName = attrs.getAttributeName(i);
            AttrType attrType = AttrType.getType(attributeName);
            if (AttrType.NO_SUPPORT != attrType) {
                String attributeValue = attrs.getAttributeValue(i);
                if (attributeValue.startsWith("#")) {
                    continue;
                }
                int id = 0;
                if (attributeValue.startsWith("?")) {
                    int attrId = Integer.parseInt(attributeValue.substring(1));
                    id = SkinThemeUtils.getThemeResId(view.getContext(), new int[]{attrId})[0];
                } else if (attributeValue.startsWith("@")) {
                    id = Integer.parseInt(attributeValue.substring(1));
                }
                if (id != 0) {
                    SkinAttrParam attrParams = new SkinAttrParam(attrType, id);
                    skinAttrParams.add(attrParams);
                }
            }
        }
        if (!skinAttrParams.isEmpty()) {
            List<IApply> skinAttrs = AttrType.assemble(skinAttrParams);
            SkinView skinView = new SkinView(view, skinAttrs);
            skinView.applySkin();
            skinViews.add(skinView);
        }
    }

    public void addSkinView(View view, SkinAttrParam... skinAttrParams) {
        List<IApply> skinAttrs = AttrType.assemble(Arrays.asList(skinAttrParams));
        SkinView skinView = new SkinView(view, skinAttrs);
        skinView.applySkin();
        skinViews.add(skinView);
    }

    /**
     * 进行换肤
     */
    public void applySkin() {
        for (SkinView skinView : skinViews) {
            skinView.applySkin();
        }
    }
}
