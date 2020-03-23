package com.easy.tv.base;

import java.io.Serializable;

public class CommonRespond<T> implements Serializable {
    private String message;//错误信息
    private String data;//存原始数据
    private T obj;//用于携带解析后的数据对象
    private int code;//返回码

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getObj() {
        return obj;
    }

    public void setObj(T obj) {
        this.obj = obj;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Respond{" +
                "message='" + message + '\'' +
                ", data='" + data + '\'' +
                ", obj=" + obj +
                ", code=" + code +
                '}';
    }
}
