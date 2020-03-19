package com.easy.eoschain.encrypt.info;

public class SignaturesResult {
    private int errorcode;
    private String message;
    private Signatures data;

    public int getErrorcode() {
        return errorcode;
    }

    public void setErrorcode(int errorcode) {
        this.errorcode = errorcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Signatures getData() {
        return data;
    }

    public void setData(Signatures data) {
        this.data = data;
    }
}
