package com.easy.framework.arouter;

import com.easy.utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由拦截管理
 */
public class RouterInterceptorManager {
    private List<String> interceptorList = new ArrayList<>();//需要拦截的路径
    private Map<String, String> replacePath = new HashMap<>();//重定向路径
    private static RouterInterceptorManager holder = new RouterInterceptorManager();

    private RouterInterceptorManager() {

    }

    public static RouterInterceptorManager getInstance() {
        return holder;
    }

    public void setReplacePath(String oldPath, String newPath) {
        if (Utils.isEmpty(oldPath) || Utils.isEmpty(newPath)) {
            return;
        }
        replacePath.put(oldPath, newPath);
    }

    public void removeReplacePath(String path) {
        if (replacePath.get(path)!=null) {
            replacePath.remove(path);
        }
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

    public static String getReplacePath(String path) {
        return holder.replacePath.get(path);
    }
}
