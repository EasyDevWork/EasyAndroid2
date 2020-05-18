package com.easy.net.function;

import com.alibaba.fastjson.JSON;
import com.easy.net.beans.Response;

import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import io.reactivex.functions.Function;
import okhttp3.ResponseBody;

public class DataSwitchFunction<T> implements Function<ResponseBody, Response<T>> {

    @Override
    public Response<T> apply(ResponseBody responseBody) {
        Response response = new Response();
        try {
            String result = responseBody.string();
            response.setOriData(result);
            response.setResultObj(onConvert(response));
            response.setCode(Response.SUCCESS_STATE);
        } catch (IOException e) {
            response.setCode(Response.ERROR_STATE);
            response.setMsg(e.getMessage());
        }
        return response;
    }

    public T onConvert(Response response) {
        Type type = getClass().getGenericSuperclass();
        Class modelClass;
        if (type instanceof ParameterizedType) {
            modelClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        } else {
            modelClass = String.class;
        }
        if (modelClass.getCanonicalName().equals(String.class.getCanonicalName())) {
            return (T) response.getOriData();
        } else {
            T result = JSON.parseObject(response.getOriData(), (Type) modelClass);
            return result;
        }
    }
}

