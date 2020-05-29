package com.easy.net.function;

import com.alibaba.fastjson.JSON;
import com.easy.net.beans.Response;

import java.io.IOException;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class DataSwitchFunction<T> implements Function<ResponseBody, Response<T>> {

    Class<T> tClass;

    public DataSwitchFunction(Class<T> t) {
        this.tClass = t;
    }

    @Override
    public Response<T> apply(ResponseBody responseBody) {
        Response<T> response = new Response<>();
        try {
            String result = responseBody.string();
            response.setOriData(result);
            response.setResultObj(onConvert(response));
            response.setCode(Response.SUCCESS_STATE);
        } catch (Exception e) {
            response.setCode(Response.ERROR_STATE);
            response.setMsg(e.getMessage());
        }
        return response;
    }

    public  T onConvert(Response response) {
        if (tClass == String.class) {
            return (T) response.getOriData();
        } else return JSON.parseObject(response.getOriData(), (Type) tClass);
    }
}

