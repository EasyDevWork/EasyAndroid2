package com.easy.framework.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Interceptor;
import com.alibaba.android.arouter.facade.callback.InterceptorCallback;
import com.alibaba.android.arouter.facade.template.IInterceptor;
import com.easy.utils.MainLooper;
import com.easy.utils.ToastUtils;

/**
 * 路由拦截器
 */
@Interceptor(priority = 7)
public class ArouterInterceptor implements IInterceptor {
    Context mContext;

    /**
     * The operation of this interceptor.
     *
     * @param postcard meta
     * @param callback cb
     */
    @Override
    public void process(final Postcard postcard, final InterceptorCallback callback) {
        if (RouterInterceptorManager.isInterceptor(postcard.getPath())) {
            MainLooper.runOnUiThread(() ->
                    ToastUtils.showShort("你已被拦截：com.easy.framework.arouter.ArouterInterceptor"));
        } else {
            callback.onContinue(postcard);
        }
    }

    /**
     * Do your init work in this method, it well be call when processor has been load.
     *
     * @param context ctx
     */
    @Override
    public void init(Context context) {
        mContext = context;
    }
}
