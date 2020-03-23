package com.easy.framework.base.web.protocol;

import android.content.Context;
import android.net.Uri;

public interface IProtocolCallback {

    void callBack(Context context, Uri uri);
}
