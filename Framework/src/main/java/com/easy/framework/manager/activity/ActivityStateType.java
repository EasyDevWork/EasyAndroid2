package com.easy.framework.manager.activity;

public enum ActivityStateType {
    foreground("前台"),
    BACKGROUND("后台");

    private String desc;

    ActivityStateType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
