package com.easy.wechat.manager;

import com.easy.wechat.bean.WeChatUser;

public interface IWeChatCallback {

    void callback(WeChatUser weChatUser);
}
