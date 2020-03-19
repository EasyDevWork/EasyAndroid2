package com.easy.framework.bean;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Response<T> implements Serializable {

    public static int SUCCESS_STATE = 0;
    public static int ERROR_STATE = -400;
    /**
     * 描述信息
     */
    @SerializedName("message")
    private String msg;

    /**
     * 状态码
     */
    @SerializedName("errorcode")
    private int code = ERROR_STATE;

    /**
     * 数据对象/成功返回对象
     */
    @SerializedName("data")
    private JsonElement result;
    //原始数据
    private String oriData;

    private T resultObj;

    public T getResultObj() {
        return resultObj;
    }

    public void setResultObj(T resultObj) {
        this.resultObj = resultObj;
    }

    public String getOriData() {
        return oriData;
    }

    public void setOriData(String oriData) {
        this.oriData = oriData;
    }

    /**
     * 是否成功
     *
     * @return
     */
    public boolean isSuccess() {
        return code == SUCCESS_STATE;
    }

    @Override
    public String toString() {
        return "Response{" +
                "msg='" + msg + '\'' +
                ", code=" + code +
                ", oriData='" + oriData + '\'' + '}';
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public JsonElement getResult() {
        return result;
    }

    public void setResult(JsonElement result) {
        this.result = result;
    }
}
