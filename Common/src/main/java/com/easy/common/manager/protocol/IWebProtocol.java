package com.easy.common.manager.protocol;

import android.net.Uri;

public interface IWebProtocol {
    void handlerProtocol(Uri uri, IProtocolCallback callback);
}
