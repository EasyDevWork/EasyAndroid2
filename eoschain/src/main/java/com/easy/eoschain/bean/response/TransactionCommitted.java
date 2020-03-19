package com.easy.eoschain.bean.response;

public class TransactionCommitted {

   public String transaction_id;

   public TransactionProcessed processed;

    public TransactionCommitted() {
    }

    public TransactionCommitted(String transaction_id, TransactionProcessed processed) {
        this.transaction_id = transaction_id;
        this.processed = processed;
    }

    public String getTransaction_id() {
        return transaction_id;
    }

    public void setTransaction_id(String transaction_id) {
        this.transaction_id = transaction_id;
    }

    public TransactionProcessed getProcessed() {
        return processed;
    }

    public void setProcessed(TransactionProcessed processed) {
        this.processed = processed;
    }
}
