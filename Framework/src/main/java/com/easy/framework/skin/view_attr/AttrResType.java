package com.easy.framework.skin.view_attr;

public enum AttrResType {
    COLOR("color"),
    DRAWABLE("drawable");
    public String type;

    AttrResType(String type) {
        this.type = type;
    }

    public String getValue() {
        return type;
    }
}
