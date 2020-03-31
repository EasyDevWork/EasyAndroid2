package com.easy.apt.lib;

import com.alibaba.fastjson.JSON;

import java.lang.reflect.Type;

public class JsonConverterFactory implements Converter.Factory {

    @Override
    public <F> Converter<F, String> fromType(Type fromType) {
        return new Converter<F, String>() {
            @Override
            public String convert(F value) {
                return JSON.toJSONString(value);
            }
        };
    }

    @Override
    public <T> Converter<String, T> toType(final Type toType) {
        return new Converter<String, T>() {
            @Override
            public T convert(String value) {
                return JSON.parseObject(value, toType);
            }
        };
    }
}
