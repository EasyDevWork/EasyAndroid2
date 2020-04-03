package com.easy.demo.bean;

import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

public class TestMvvm2 {

    public final ObservableField<String> name = new ObservableField<>();
    public final ObservableInt size = new ObservableInt();

    public ObservableField<String> getName() {
        return name;
    }

    public ObservableInt getSize() {
        return size;
    }

    public void setName(String name2) {
        name.set(name2);
    }

    public void setSize(int size2) {
        size.set(size2);
    }
}
