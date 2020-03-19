package com.easy.framework.Http.callback;

import com.alibaba.fastjson.JSON;
import com.easy.framework.bean.Response;
import com.google.gson.Gson;

/**
 * 根据业务进一步封装
 */
public abstract class RHttpCallback<T> extends HttpCallback<T> {

    private Class<T> aClass;

    public RHttpCallback(Class<T> aClass) {
        this.aClass = aClass;
    }

    @Override
    public Response<T> onConvert(String data) {
        Response response;
        try {
            response = JSON.parseObject(data, Response.class);
            response.setOriData(data);
            if (response.getCode() == Response.ERROR_STATE) {//说明外层不是Response格式的JSON
                response = parseObject(data);
            } else if (response.isSuccess() && response.getResult() != null) {
                T t = new Gson().fromJson(response.getResult(), aClass);
                response.setResultObj(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new Response<>();
            response.setOriData(data);
            response.setMsg(e.getMessage());
        }
        return response;
    }

    public Response<T> parseObject(String data) {
        Response<T> response = new Response<>();
        response.setOriData(data);
        try {
            T t = JSON.parseObject(data, aClass);
            response.setResultObj(t);
            response.setCode(Response.SUCCESS_STATE);
        } catch (Exception e2) {
            e2.printStackTrace();
            response.setMsg(e2.getMessage());
        }
        return response;
    }
}
