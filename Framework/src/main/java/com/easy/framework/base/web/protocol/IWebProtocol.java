package com.easy.framework.base.web.protocol;

import android.net.Uri;

public interface IWebProtocol {
    void handlerProtocol(Uri uri, IProtocolCallback callback);
}
