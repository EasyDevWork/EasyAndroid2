package com.easy.eoschain.bean;

public class AbiContract {
    private String account_name;
    private AbiInfo abi;

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public AbiInfo getAbi() {
        return abi;
    }

    public void setAbi(AbiInfo abi) {
        this.abi = abi;
    }
}
