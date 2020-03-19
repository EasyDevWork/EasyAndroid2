package com.easy.eoschain.bean;

import java.util.List;

public class AbiStruct {
    private String name;
    private String base;
    private List<AbiField> fields;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBase() {
        return base;
    }

    public void setBase(String base) {
        this.base = base;
    }

    public List<AbiField> getFields() {
        return fields;
    }

    public void setFields(List<AbiField> fields) {
        this.fields = fields;
    }
}
