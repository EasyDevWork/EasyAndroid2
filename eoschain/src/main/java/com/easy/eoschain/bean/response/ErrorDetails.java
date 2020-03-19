package com.easy.eoschain.bean.response;

public class ErrorDetails {
    private String message;
    private String file;
    private String method;
    private long line_number;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public long getLine_number() {
        return line_number;
    }

    public void setLine_number(long line_number) {
        this.line_number = line_number;
    }
}
