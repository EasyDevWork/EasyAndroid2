package com.easy.demo.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

public class TestDo implements MultiItemEntity {
    private String title;
    private String name;
    private String image;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public TestDo(String name) {
        this.name = name;
    }

    public TestDo() {
    }

    public boolean equals(Object var1) {
        if (var1 instanceof TestDo) {
            return this.name.equals(((TestDo) var1).getName());
        }
        return false;
    }

    @Override
    public int getItemType() {
        return type;
    }
}
