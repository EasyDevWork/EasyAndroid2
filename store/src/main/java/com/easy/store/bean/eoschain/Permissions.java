package com.easy.store.bean.eoschain;

public class Permissions {
    /**
     * perm_name : active
     * parent : owner
     * required_auth : {"threshold":1,"keys":[{"key":"EOS7ukaArHrh4uLVXuauRC1Mjr2zbu1QTv7WQrFEV8rzbaMvv5aPd","weight":1}],"accounts":[],"waits":[]}
     */

    private String perm_name;
    private String parent;
    private RequiredAuth required_auth;

    public String getPerm_name() {
        return perm_name;
    }

    public void setPerm_name(String perm_name) {
        this.perm_name = perm_name;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public RequiredAuth getRequired_auth() {
        return required_auth;
    }

    public void setRequired_auth(RequiredAuth required_auth) {
        this.required_auth = required_auth;
    }
}
