package com.easy.eoschain.bean;

import java.util.List;

public class AbiTable {
    private String name;
    private String index_type;
    private List<String> key_names;
    private List<String> key_types;
    private String type;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex_type() {
        return index_type;
    }

    public void setIndex_type(String index_type) {
        this.index_type = index_type;
    }

    public List<String> getKey_names() {
        return key_names;
    }

    public void setKey_names(List<String> key_names) {
        this.key_names = key_names;
    }

    public List<String> getKey_types() {
        return key_types;
    }

    public void setKey_types(List<String> key_types) {
        this.key_types = key_types;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
