package com.easy.framework.arouter.impl;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.PathReplaceService;
import com.easy.framework.arouter.RouterInterceptorManager;
import com.easy.utils.Utils;

/**
 * 路由重定向
 */
@Route(path = "/framework/replacePaths")
public class PathReplaceServiceImpl implements PathReplaceService {

    @Override
    public String forString(String path) {
        if (Utils.isNotEmpty(path)) {
            Log.d("PathReplaceServiceImpl", path);
            return RouterInterceptorManager.getReplacePath(path);
        }
        return null;
    }

    @Override
    public Uri forUri(Uri uri) {
        if (uri != null) {
            Log.d("PathReplaceServiceImpl", "forUri:" + uri.getPath());
            forString(uri.getPath());
        }
        return null;
    }

    @Override
    public void init(Context context) {

    }
}
