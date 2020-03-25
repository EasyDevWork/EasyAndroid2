package com.easy.wechat.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.easy.wechat.bean.WeChatUser;
import com.easy.wechat.utils.WxNetworkUtil;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX;
import com.tencent.mm.opensdk.modelmsg.WXImageObject;
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage;
import com.tencent.mm.opensdk.modelmsg.WXMusicObject;
import com.tencent.mm.opensdk.modelmsg.WXTextObject;
import com.tencent.mm.opensdk.modelmsg.WXVideoObject;
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import java.io.File;

public class EasyWeChat {

    private IWXAPI api;
    //分享到对话:
    //SendMessageToWX.Req.WXSceneSession
    //分享到朋友圈:
    //SendMessageToWX.Req.WXSceneTimeline
    //分享到收藏:
    //SendMessageToWX.Req.WXSceneFavorite
    private int mTargetScene = SendMessageToWX.Req.WXSceneSession;
    private static final int THUMB_SIZE = 150;
    IWeChatCallback callback;
    WeChatUser weChatUser;
    String appId, appSecret;

    private static class Holder {
        private static EasyWeChat instance = new EasyWeChat();
    }

    private EasyWeChat() {
    }

    public static EasyWeChat getInstance() {
        return Holder.instance;
    }

    public void init(Context context, String appId, String appSecret) {
        this.appId = appId;
        this.appSecret = appSecret;
        api = WXAPIFactory.createWXAPI(context, appId, false);
    }

    /**
     * 启动微信应用
     */
    public void openWeChat() {
        api.openWXApp();
    }

    public void unregisterApp() {
        if (api != null) {
            api.unregisterApp();
        }
    }

    public void setTargetScene(int targetScene) {
        mTargetScene = targetScene;
    }

    public void sendTextMsg(String content) {
        WXTextObject textObj = new WXTextObject();
        textObj.text = content;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = textObj;
        msg.description = content;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("text");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    public void sendImageMsg(Bitmap bmp) {
        WXImageObject imgObj = new WXImageObject(bmp);
        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = WxNetworkUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    public void sendImageMsg(File file) {
        if (!file.exists()) {
            return;
        }

        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(file.getAbsolutePath());

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = imgObj;

        Bitmap bmp = BitmapFactory.decodeFile(file.getAbsolutePath());
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
        bmp.recycle();
        msg.thumbData = WxNetworkUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("img");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    public void sendMusic(String musicUrl, String title, String description, Bitmap thumbBmp) {
        WXMusicObject music = new WXMusicObject();
        music.musicUrl = musicUrl;

        WXMediaMessage msg = new WXMediaMessage();
        msg.mediaObject = music;
        msg.title = title;
        msg.description = description;
        msg.thumbData = WxNetworkUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("music");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    public void sendVideo(String videoUrl, String title, String description, Bitmap thumbBmp) {
        WXVideoObject video = new WXVideoObject();
        video.videoUrl = videoUrl;

        WXMediaMessage msg = new WXMediaMessage(video);

        msg.title = title;
        msg.description = description;
        msg.thumbData = WxNetworkUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("video");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    public void sendWabPage(String webpageUrl, String title, String description, Bitmap thumbBmp) {
        WXWebpageObject webPage = new WXWebpageObject();
        webPage.webpageUrl = webpageUrl;

        WXMediaMessage msg = new WXMediaMessage(webPage);
        msg.title = title;
        msg.description = description;
        msg.thumbData = WxNetworkUtil.bmpToByteArray(thumbBmp, true);

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = buildTransaction("webpage");
        req.message = msg;
        req.scene = mTargetScene;
        api.sendReq(req);
    }

    public void parseData(String result, int msgTag) {
        if (WxNetworkUtil.CHECK_TOKEN == msgTag) {
            JSONObject json = JSON.parseObject(result);
            int errcode = json.getInteger("errcode");
            if (errcode == 0) {
                requestUserInfo();
            } else {
                WxNetworkUtil.sendWxAPI(String.format("https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s",
                        appId, weChatUser.getRefresh_token()), WxNetworkUtil.REFRESH_TOKEN);
            }
        } else if (WxNetworkUtil.REFRESH_TOKEN == msgTag) {
            if (weChatUser == null) {
                weChatUser = new WeChatUser();
            }
            JSONObject json = JSON.parseObject(result);
            weChatUser.setOpenid(json.getString("openid"));
            weChatUser.setAccess_token(json.getString("access_token"));
            weChatUser.setRefresh_token(json.getString("refresh_token"));
            weChatUser.setScope(json.getString("scope"));
            requestUserInfo();
        } else if (WxNetworkUtil.GET_INFO == msgTag) {
            JSONObject json = JSON.parseObject(result);
            weChatUser.setNickname(json.getString("nickname"));
            weChatUser.setSex(json.getString("sex"));
            weChatUser.setProvince(json.getString("province"));
            weChatUser.setCity(json.getString("city"));
            weChatUser.setCountry(json.getString("country"));
            weChatUser.setHeadimgurl(json.getString("headimgurl"));
            if (callback != null) {
                callback.callback(weChatUser);
            }
        } else if (WxNetworkUtil.GET_TOKEN == msgTag) {
            JSONObject json = JSON.parseObject(result);
            if (weChatUser == null) {
                weChatUser = new WeChatUser();
            }
            weChatUser.setAccess_token(json.getString("access_token"));
            weChatUser.setRefresh_token(json.getString("refresh_token"));
            weChatUser.setOpenid(json.getString("openid"));
            weChatUser.setScope(json.getString("scope"));
            requestUserInfo();
        }
    }

    private void requestUserInfo() {
        WxNetworkUtil.sendWxAPI(String.format("https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s",
                weChatUser.getAccess_token(), weChatUser.getOpenid()), WxNetworkUtil.GET_INFO);
    }

    public void sendAuth(String code) {
        WxNetworkUtil.sendWxAPI(String.format("https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code",
                appId, appSecret, code), WxNetworkUtil.GET_TOKEN);
    }

    public void getUserInfo(IWeChatCallback callback) {
        this.callback = callback;
        SendAuth.Req req = new SendAuth.Req();
        req.scope = "snsapi_userinfo,snsapi_friend,snsapi_message,snsapi_contact";
        req.state = "none";
        api.sendReq(req);
    }

    private String buildTransaction(final String type) {
        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
    }
}
