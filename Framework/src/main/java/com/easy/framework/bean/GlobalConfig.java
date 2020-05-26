package com.easy.framework.bean;

public class GlobalConfig {
    private String language;//应用默认语言
    private boolean skinAble = true;//可换肤吗

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isSkinAble() {
        return skinAble;
    }

    public void setSkinAble(boolean skinAble) {
        this.skinAble = skinAble;
    }
}
