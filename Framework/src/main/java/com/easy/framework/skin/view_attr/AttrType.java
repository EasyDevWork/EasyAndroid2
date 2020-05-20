package com.easy.framework.skin.view_attr;

public enum AttrType {
    DividerAttr("divider"),
    BackgroundAttr("background"),
    ListSelectorAttr("listSelector"),
    TextColorAttr("textColor");

    public String type;

    AttrType(String type) {
        this.type = type;
    }

    public String value() {
        return type;
    }
}
