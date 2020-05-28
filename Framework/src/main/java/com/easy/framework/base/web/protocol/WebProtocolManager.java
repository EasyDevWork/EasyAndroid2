package com.easy.framework.base.web.protocol;

import android.content.Context;
import android.net.Uri;

import com.easy.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.List;

public class WebProtocolManager {
    List<String> schemeList = new ArrayList<>();
    IProtocolCallback callback;

    private WebProtocolManager() {

    }

    public static final class Holder {
        public static final WebProtocolManager protocolManager = new WebProtocolManager();
    }

    public static WebProtocolManager getInstall() {
        return Holder.protocolManager;
    }

    public void addScheme(String scheme,IProtocolCallback callback) {
        schemeList.add(scheme);
        this.callback = callback;
    }

    /**
     * 自定义协议处理
     *
     * @param context
     * @param uri
     * @return
     */
    public boolean handleProtocol(Context context, Uri uri) {
        if (uri == null || EmptyUtils.isEmpty(uri.getHost())) {
            return false;
        }
        String scheme = uri.getScheme();
        if (schemeList.contains(scheme)) {
            if (callback != null) {
                callback.callBack(context, uri);
            }
            return true;
        }
        return false;
    }
}
