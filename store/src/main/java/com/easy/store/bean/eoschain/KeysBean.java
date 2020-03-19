package com.easy.store.bean.eoschain;

public class KeysBean {
    /**
     * key : EOS7ukaArHrh4uLVXuauRC1Mjr2zbu1QTv7WQrFEV8rzbaMvv5aPd
     * weight : 1
     */

    private String key;
    private Short weight;

    public KeysBean(String key, Short weight) {
        this.key = key;
        this.weight = weight;
    }

    public KeysBean() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Short getWeight() {
        return weight;
    }

    public void setWeight(Short weight) {
        this.weight = weight;
    }
}
