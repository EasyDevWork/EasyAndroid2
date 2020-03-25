package com.easy.wechat.utils;

import android.graphics.Bitmap;

import com.easy.wechat.manager.EasyWeChat;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import javax.net.ssl.HttpsURLConnection;

public class WxNetworkUtil {

    public static final int GET_TOKEN = 1;
    public static final int CHECK_TOKEN = 2;
    public static final int REFRESH_TOKEN = 3;
    public static final int GET_INFO = 4;

    public static void sendWxAPI(String url, int msgTag) {
        HttpsThread httpsThread = new HttpsThread(url, msgTag);
        httpsThread.start();
    }

    static class HttpsThread extends Thread {

        private String httpsUrl;
        private int msgTag;

        public HttpsThread(String url, int msgTag) {
            this.httpsUrl = url;
            this.msgTag = msgTag;
        }

        @Override
        public void run() {
            int resCode;
            InputStream in;
            try {
                URL url = new URL(httpsUrl);
                URLConnection urlConnection = url.openConnection();
                HttpsURLConnection httpsConn = (HttpsURLConnection) urlConnection;
                httpsConn.setAllowUserInteraction(false);
                httpsConn.setInstanceFollowRedirects(true);
                httpsConn.setRequestMethod("GET");
                httpsConn.connect();
                resCode = httpsConn.getResponseCode();

                if (resCode == HttpURLConnection.HTTP_OK) {
                    in = httpsConn.getInputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                    in.close();
                    String httpResult = sb.toString();

                    EasyWeChat.getInstance().parseData(httpResult, msgTag);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }
}
