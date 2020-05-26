package com.easy.framework.manager.screen;

public enum ScreenStateType {
    SCREEN_ON("开屏"),
    SCREEN_OFF("锁屏"),
    SCREEN_UNLOCK("解锁");

    private String desc;

    ScreenStateType(String desc) {
        this.desc = desc;
    }

    @Override
    public String toString() {
        return desc;
    }
}
