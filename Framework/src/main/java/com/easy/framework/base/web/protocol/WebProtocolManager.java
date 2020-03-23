package com.easy.framework.base.web.protocol;

import android.content.Context;
import android.net.Uri;

import com.easy.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class WebProtocolManager {
    List<String> schemeList = new ArrayList<>();

    private WebProtocolManager() {

    }

    public void addScheme(String scheme) {
        schemeList.add(scheme);
    }

    public static final class Holder {
        public static final WebProtocolManager protocolManager = new WebProtocolManager();
    }

    public static WebProtocolManager getInstall() {
        return Holder.protocolManager;
    }

    public void handleProtocol(Context context, String message, IProtocolCallback callback) {
        if (Utils.isNotEmpty(message)) {
            try {
                Uri uri = Uri.parse(message);
                handleProtocol(context, uri, callback);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 自定义协议处理
     *
     * @param context
     * @param uri
     * @param callback
     * @return
     */
    public boolean handleProtocol(Context context, Uri uri, IProtocolCallback callback) {
        if (uri == null || Utils.isEmpty(uri.getHost())) {
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
