package com.easy.eoschain.bean;

import com.easy.eoschain.encrypt.abi.TransactionAuthorizationAbi;

import java.util.List;

public class AbiBin {
    private List<TransactionAuthorizationAbi> authorization;
    private Object data;
    private String name;
    private String account;

    public List<TransactionAuthorizationAbi> getAuthorization() {
        return authorization;
    }

    public void setAuthorization(List<TransactionAuthorizationAbi> authorization) {
        this.authorization = authorization;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
}
