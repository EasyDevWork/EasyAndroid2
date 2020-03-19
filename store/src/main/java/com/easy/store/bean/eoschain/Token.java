package com.easy.store.bean.eoschain;

public class Token {

    /**
     * contract : hirevibeshvt
     * symbol : HVT
     * balance : 2.7501
     * precision : 4
     * price : 0
     * price_eos : 0
     * total_value : 0
     * total_value_eos : 0
     */

    private String contract;
    private String symbol;
    private String balance;
    private String precision;

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getPrecision() {
        return precision;
    }

    public void setPrecision(String precision) {
        this.precision = precision;
    }
    @Override
    public String toString() {
        return "Token{" +
                "contract='" + contract + '\'' +
                ", symbol='" + symbol + '\'' +
                ", balance=" + balance +
                '}';
    }
}
