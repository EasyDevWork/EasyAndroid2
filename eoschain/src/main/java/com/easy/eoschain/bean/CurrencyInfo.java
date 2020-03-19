package com.easy.eoschain.bean;

public class CurrencyInfo {
    private String code;//token publisher
    private String account;//accountname;
    private String symbol;//token name;
    private String balance;//数量

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    @Override
    public String toString() {
        return "CurrencyInfo{" +
                "code='" + code + '\'' +
                ", account='" + account + '\'' +
                ", symbol='" + symbol + '\'' +
                ", balance='" + balance + '\'' +
                '}';
    }
}
