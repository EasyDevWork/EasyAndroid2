package com.easy.framework.base.web;

import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.util.Log;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.easy.framework.base.WebBaseFragment;
import com.easy.framework.base.web.protocol.WebProtocolManager;
import com.easy.utils.EmptyUtils;
import com.easy.utils.SystemUtils;

import java.lang.ref.WeakReference;

public class WebViewClientBase extends WebViewClient {

    WeakReference<WebBaseFragment> webViewFragment;

    public WebViewClientBase(WebBaseFragment webViewFragment) {
        this.webViewFragment = new WeakReference<>(webViewFragment);
    }

    /**
     * 处理自定义协议
     *
     * @param webView
     * @param url
     * @return
     */
    public boolean overrideUrlLoading(WebView webView, String url) {
        if (EmptyUtils.isNotEmpty(url)) {
            Uri uri = Uri.parse(url);
            if (uri != null && uri.getScheme() != null) {
                if (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")) {
                    webView.loadUrl(url);
                } else
                    return WebProtocolManager.getInstall().handleProtocol(webView.getContext(), uri);
            } else if (url.contains("alipays://platformapi/startApp")) {
                SystemUtils.openAliPayApp(webView.getContext(), url);
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        Log.d("WebViewClientBase", "shouldOverrideUrlLoading1: " + url);
        boolean isDone = overrideUrlLoading(webView, url);
        if (isDone) {
            return true;
        }
        return super.shouldOverrideUrlLoading(webView, url);
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView webView, WebResourceRequest webResourceRequest) {
        String url;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            url = webResourceRequest.getUrl().toString();
        } else {
            url = webView.getUrl();
        }
        Log.d("WebViewClientBase", "shouldOverrideUrlLoading2: " + url);
        boolean isDone = shouldOverrideUrlLoading(webView, url);
        if (isDone) {
            return true;
        }
        return super.shouldOverrideUrlLoading(webView, webResourceRequest);
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        Log.d("WebViewClientBase", "onPageStarted: " + url);
        super.onPageStarted(view, url, favicon);
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        Log.d("WebViewClientBase", "onPageFinished: " + url);
        super.onPageFinished(view, url);
        if (webViewFragment.get() != null) {
            webViewFragment.get().onPageFinished();
        }
    }

    @Override
    public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
        Log.d("WebViewClientBase", "onReceivedError: " + failingUrl);
        super.onReceivedError(view, errorCode, description, failingUrl);
    }

    @Override
    public void doUpdateVisitedHistory(WebView view, String url, boolean isReload) {
        Log.d("WebViewClientBase", "doUpdateVisitedHistory: " + url);
        super.doUpdateVisitedHistory(view, url, isReload);
        if (webViewFragment.get() != null) {
            webViewFragment.get().doUpdateVisitedHistory(url);
        }
    }

    @Override
    public void onReceivedSslError(WebView webView, SslErrorHandler sslErrorHandler, SslError sslError) {
        Log.d("WebViewClientBase", "onReceivedSslError");
        sslErrorHandler.proceed();    //表示等待证书响应
        // handler.cancel();      //表示挂起连接，为默认方式
        // handler.handleMessage(null);    //可做其他处理
    }

}
