package com.easy.net.interceptor;

import android.text.TextUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class HostSwitchInterceptorImp implements Interceptor {

    Map<String, String> baseUrlMap = new HashMap<>();

    private static class Holder {
        private static HostSwitchInterceptorImp interceptor = new HostSwitchInterceptorImp();
    }

    private HostSwitchInterceptorImp() {
    }

    public static HostSwitchInterceptorImp getInstance() {
        return Holder.interceptor;
    }

    public void puts(Map<String, String> hostMap) {
        baseUrlMap.putAll(hostMap);
    }

    public void put(String group, String host) {
        if (!baseUrlMap.containsKey(group) && !TextUtils.isEmpty(host)) {
            baseUrlMap.put(group, host);
        }
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Request newRequest = customRequest(request);
        if (request != newRequest) {
            request = newRequest;
        }
        return chain.proceed(request);
    }


    public Request customRequest(Request request) {
        List<String> headerValues = request.headers("host_group");
        if (headerValues.size() > 0) {
            String defaultBaseUrl = baseUrlMap.get("default");
            Request.Builder builder = request.newBuilder();
            builder.removeHeader("host_group");
            String head = headerValues.get(0);
            if (!TextUtils.isEmpty(head)) {
                if ("remove".equals(head)) {
                    defaultBaseUrl = null;
                }
                String baseUrl = baseUrlMap.get(head);
                if (!TextUtils.isEmpty(baseUrl)) {
                    defaultBaseUrl = baseUrl;
                }
            }
            if (!TextUtils.isEmpty(defaultBaseUrl)) {
                HttpUrl newHttpUrl = HttpUrl.parse(defaultBaseUrl);
                HttpUrl oldHttpUrl = request.url();
                HttpUrl newFullHttpUrl = oldHttpUrl.newBuilder()
                        .scheme(newHttpUrl.scheme())
                        .host(newHttpUrl.host())
                        .port(newHttpUrl.port())
                        .build();
                return builder.url(newFullHttpUrl).build();
            }
        }
        return request;
    }
}
