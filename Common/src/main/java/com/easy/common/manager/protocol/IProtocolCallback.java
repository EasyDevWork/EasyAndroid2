package com.easy.common.manager.protocol;

import android.content.Context;
import android.net.Uri;

public interface IProtocolCallback {

    void callBack(Context context, Uri uri);
}
