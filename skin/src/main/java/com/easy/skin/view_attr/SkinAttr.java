package com.easy.skin.view_attr;

public abstract class SkinAttr implements  IApply{
    public AttrType attrName;
    public int id;

    public SkinAttr(AttrType attrName, int id) {
        this.attrName = attrName;
        this.id = id;
    }
}
