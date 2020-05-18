package com.easy.store.bean.eoschain;

import java.util.List;

public class RequiredAuth {
    /**
     * threshold : 1
     * keys : [{"key":"EOS7ukaArHrh4uLVXuauRC1Mjr2zbu1QTv7WQrFEV8rzbaMvv5aPd","weight":1}]
     * accounts : []
     * waits : []
     */

    private int threshold;
    private List<KeysBean> keys;
    private List<String> accounts;
    private List<String> waits;

    public RequiredAuth() {
    }

    public RequiredAuth(int threshold, List<KeysBean> keys, List<String> accounts, List<String> waits) {
        this.threshold = threshold;
        this.keys = keys;
        this.accounts = accounts;
        this.waits = waits;
    }

    public int getThreshold() {
        return threshold;
    }

    public void setThreshold(int threshold) {
        this.threshold = threshold;
    }

    public List<KeysBean> getKeys() {
        return keys;
    }

    public void setKeys(List<KeysBean> keys) {
        this.keys = keys;
    }

    public List<String> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<String> accounts) {
        this.accounts = accounts;
    }

    public List<String> getWaits() {
        return waits;
    }

    public void setWaits(List<String> waits) {
        this.waits = waits;
    }

}
