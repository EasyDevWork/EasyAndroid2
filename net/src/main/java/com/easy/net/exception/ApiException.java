package com.easy.net.exception;


public class ApiException extends Exception {
    private int code;//错误码
    private String msg;//简短的错误信息，具体消息查看Throwable

    public ApiException(int code, String msg, Throwable throwable) {
        super(throwable);
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return code + " : " + msg;
    }
}
