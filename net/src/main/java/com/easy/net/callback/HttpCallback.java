package com.easy.net.callback;

import com.alibaba.fastjson.JSON;
import com.easy.net.beans.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class HttpCallback<T> extends BaseCallback<T> {

    @Override
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
