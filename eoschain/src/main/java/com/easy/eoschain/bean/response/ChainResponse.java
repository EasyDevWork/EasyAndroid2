package com.easy.eoschain.bean.response;

public class ChainResponse<T> {
    private boolean isSuccessful;
    private int statusCode;
    private T body;
    private ChainError errorBody;

    public ChainResponse(boolean isSuccessful, int statusCode, T body, ChainError errorBody) {
        this.isSuccessful = isSuccessful;
        this.statusCode = statusCode;
        this.body = body;
        this.errorBody = errorBody;
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public void setSuccessful(boolean successful) {
        isSuccessful = successful;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    public ChainError getErrorBody() {
        return errorBody;
    }

    public void setErrorBody(ChainError errorBody) {
        this.errorBody = errorBody;
    }
}
