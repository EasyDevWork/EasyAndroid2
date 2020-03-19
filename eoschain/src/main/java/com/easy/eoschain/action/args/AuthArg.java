package com.easy.eoschain.action.args;

import com.easy.store.bean.eoschain.RequiredAuth;

public class AuthArg {
    private String account;
    private String permission;
    private String parent;
    private RequiredAuth auth;

    public AuthArg() {
    }

    public AuthArg(String account, String permission, String parent, RequiredAuth auth) {
        this.account = account;
        this.permission = permission;
        this.parent = parent;
        this.auth = auth;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public RequiredAuth getAuth() {
        return auth;
    }

    public void setAuth(RequiredAuth auth) {
        this.auth = auth;
    }
}
