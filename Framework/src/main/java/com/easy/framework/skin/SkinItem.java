package com.easy.framework.skin;

import android.view.View;

import com.easy.framework.skin.view_attr.SkinAttr;
import com.easy.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.List;

public class SkinItem {

    public View view;

    public List<SkinAttr> attrs;

    public SkinItem(){
        attrs = new ArrayList<>();
    }

    public void apply(){
        if(EmptyUtils.isEmpty(attrs)){
            return;
        }
        for(SkinAttr at : attrs){
            at.apply(view);
        }
    }

    public void clean(){
        if(EmptyUtils.isEmpty(attrs)){
            return;
        }
        attrs.clear();
    }

    @Override
    public String toString() {
        return "SkinItem [view=" + view.getClass().getSimpleName() + ", attrs=" + attrs + "]";
    }
}