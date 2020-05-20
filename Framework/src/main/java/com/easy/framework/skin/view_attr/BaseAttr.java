package com.easy.framework.skin.view_attr;

public class BaseAttr {
    /**
     * name of the attr, such as background or textSize or textColor
     */
    public AttrType attrType;

    /**
     * type of the value , such as color or drawable
     */
    public AttrResType attrResType;

    /**
     * 资源引用 such as color=attrValueRefId
     */
    public int attrResRef;


    public BaseAttr(AttrType attrType, AttrResType attrResType, int attrResRef) {
        this.attrType = attrType;
        this.attrResType = attrResType;
        this.attrResRef = attrResRef;
    }
}