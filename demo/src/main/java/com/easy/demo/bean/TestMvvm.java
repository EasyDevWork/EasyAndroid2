package com.easy.demo.bean;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import com.easy.demo.BR;

public class TestMvvm extends BaseObservable {
    private String name;
    private int size;

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
        notifyPropertyChanged(BR.size);
    }
}
