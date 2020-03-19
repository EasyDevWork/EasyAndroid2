package com.easy.framework.Http.function;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import io.reactivex.functions.Function;

public class ServerResultFunction implements Function<JsonElement, Object> {
    @Override
    public Object apply(@NonNull JsonElement response){
        return new Gson().toJson(response);
    }
}