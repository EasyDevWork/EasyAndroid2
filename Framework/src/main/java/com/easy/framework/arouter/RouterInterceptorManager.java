package com.easy.framework.arouter;

import java.util.ArrayList;
import java.util.List;

/**
 * 路由拦截管理
 */
public class RouterInterceptorManager {
    private List<String> interceptorList = new ArrayList<>();//需要拦截的路径
    private static RouterInterceptorManager holder = new RouterInterceptorManager();

    private RouterInterceptorManager() {

    }

    public static RouterInterceptorManager getInstance() {
        return holder;
    }

    /**
     * 设置要拦截的地址
     *
     * @param path
     */
    public void setInterceptor(String path) {
        if (!interceptorList.contains(path)) {
            interceptorList.add(path);
        }
    }

    public void removeInterceptor(String path) {
        if (interceptorList.contains(path)) {
            interceptorList.remove(path);
        }
    }

    /**
     * 判断是否要拦截
     */
    public static boolean isInterceptor(String path) {
        if (holder.interceptorList.contains(path)) {
            return true;
        }
        return false;
    }

}
