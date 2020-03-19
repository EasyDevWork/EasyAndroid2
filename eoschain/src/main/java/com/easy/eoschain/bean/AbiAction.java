package com.easy.eoschain.bean;

public class AbiAction {
    private String name;
    private String type;
    private String ricardian_contract;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRicardian_contract() {
        return ricardian_contract;
    }

    public void setRicardian_contract(String ricardian_contract) {
        this.ricardian_contract = ricardian_contract;
    }
}
