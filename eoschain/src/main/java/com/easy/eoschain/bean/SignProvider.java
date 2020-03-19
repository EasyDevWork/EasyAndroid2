package com.easy.eoschain.bean;

import java.util.List;

public class SignProvider {
    private List<Integer> buf;
    private List<AbiBin> transaction;
    private boolean isSignProvider;
    private boolean isSignatureConfirmOpen;

    public List<Integer> getBuf() {
        return buf;
    }

    public void setBuf(List<Integer> buf) {
        this.buf = buf;
    }

    public List<AbiBin> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<AbiBin> transaction) {
        this.transaction = transaction;
    }

    public boolean isSignProvider() {
        return isSignProvider;
    }

    public void setSignProvider(boolean signProvider) {
        isSignProvider = signProvider;
    }

    public boolean isSignatureConfirmOpen() {
        return isSignatureConfirmOpen;
    }

    public void setSignatureConfirmOpen(boolean signatureConfirmOpen) {
        isSignatureConfirmOpen = signatureConfirmOpen;
    }
}
