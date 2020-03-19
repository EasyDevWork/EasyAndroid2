package com.easy.store.bean.eoschain;

public class TokenPrice {


    /**
     * symbol : eosio.token-eos-usdt
     * contract : eosio.token
     * currency : EOS
     * last : 2.668
     * change : 7.0E-4
     * high : 2.71
     * low : 2.6531
     * amount : 3099.276199999999
     * volume : 8276.846700000002
     */

    private String symbol;
    private String contract;
    private String currency;
    private String last;
    private String change;
    private String high;
    private String low;
    private String amount;
    private String volume;

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getChange() {
        return change;
    }

    public void setChange(String change) {
        this.change = change;
    }

    public String getHigh() {
        return high;
    }

    public void setHigh(String high) {
        this.high = high;
    }

    public String getLow() {
        return low;
    }

    public void setLow(String low) {
        this.low = low;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
