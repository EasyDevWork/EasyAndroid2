package com.easy.common.ui.web.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.easy.apt.annotation.FragmentInject;
import com.easy.common.R;
import com.easy.common.databinding.WebVewFragmentBinding;
import com.easy.common.manager.protocol.IProtocolCallback;
import com.easy.common.manager.protocol.WebProtocolManager;
import com.easy.common.ui.web.base.GestureDetectorListenerImp;
import com.easy.common.ui.web.base.IWebCallback;
import com.easy.common.ui.web.base.JsToAndroid;
import com.easy.common.ui.web.base.ScrollWebView;
import com.easy.common.ui.web.base.WebChromeClientBase;
import com.easy.common.ui.web.base.WebViewClientBase;
import com.easy.framework.base.BaseFragment;
import com.easy.net.event.FragmentEvent;
import com.easy.utils.SystemUtils;
import com.easy.utils.Utils;

@FragmentInject
public class WebViewFragment extends BaseFragment<WebViewPresenter, WebVewFragmentBinding> implements WebViewView<FragmentEvent>, View.OnTouchListener {

    public static final String KEY_RUL = "URL";
    public static final String HTML_DATA = "htmlData";
    public static final String OPEN_GESTURE = "openGesture";
    private String url;
    private String htmlData;
    private boolean openGesture = false;
    WebSettings webSettings;
    private ScrollWebView.OnScrollChangeListener onScrollChangeListener;
    IWebCallback iWebCallback;
    IProtocolCallback protocolCallback = (context, uri) -> {
        if (uri != null) {
            //todo handle Protocol
        }
    };

    GestureDetector detector = new GestureDetector(getContext(), new GestureDetectorListenerImp() {
        @Override
        public void slideToRight() {
            if (viewBind.webView.canGoForward()) {
                viewBind.webView.goForward();
            }
        }

        @Override
        public void slideToLeft() {
            if (viewBind.webView.canGoBack()) {
                viewBind.webView.goBack();
            } else {
                if (iWebCallback != null) {
                    iWebCallback.finish();
                }
            }
        }
    });

    @Override
    public int getLayoutId() {
        return R.layout.web_vew_fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        iWebCallback = (IWebCallback) context;
        Bundle bundle = getArguments();
        if (bundle != null) {
            url = bundle.getString(KEY_RUL);
            htmlData = bundle.getString(HTML_DATA);
            openGesture = bundle.getBoolean(OPEN_GESTURE, false);
        }
    }

    @Override
    public void initView(View view) {
        initWebView();
        loadUrl();
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onResume() {
        super.onResume();
        if (webSettings != null) {
            webSettings.setJavaScriptEnabled(true);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (webSettings != null) {
            webSettings.setJavaScriptEnabled(false);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iWebCallback = null;
    }

    private void initWebView() {
        initWebSetting();
        WebChromeClientBase mWebChromeClientBase = new WebChromeClientBase(this);
        WebViewClientBase mWebViewClientBase = new WebViewClientBase(this);
        viewBind.webView.setWebViewClient(mWebViewClientBase);
        viewBind.webView.setWebChromeClient(mWebChromeClientBase);

        if (iWebCallback != null) {
            JsToAndroid jsToAndroid = iWebCallback.getJsToAndroid();
            if (jsToAndroid != null) {
                viewBind.webView.addJavascriptInterface(jsToAndroid, jsToAndroid.jsName());
            }
        }

        if (openGesture) {
            viewBind.webView.setOnTouchListener(this);
        }
        viewBind.webView.setDownloadListener((url, userAgent, contentDisposition, mimeType, contentLength) -> {
            try {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.addCategory(Intent.CATEGORY_BROWSABLE);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        viewBind.webView.setOnScrollChangeListener((l, t, oldl, oldt) -> {
            if (onScrollChangeListener != null) {
                onScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
            }
        });
    }

    private void loadUrl() {
        if (Utils.isNotEmpty(url)) {
            viewBind.webView.loadUrl(url);
        } else if (Utils.isNotEmpty(htmlData)) {
            viewBind.webView.loadDataWithBaseURL(null, htmlData, "text/html", "utf-8", null);
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void initWebSetting() {
        webSettings = viewBind.webView.getSettings();
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        // 若加载的 html 里有JS 在执行动画等操作，会造成资源浪费（CPU、电量）
        // 在 onStop 和 onResume 里分别把 setJavaScriptEnabled() 给设置成 false 和 true 即可
        webSettings.setJavaScriptEnabled(true);

        //设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

        //缩放操作
        //webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
//        webSettings.setBuiltInZoomControls(false); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

        webSettings.setDomStorageEnabled(true);  // 开启 DOM storage 功能
        webSettings.setAppCacheMaxSize(1024 * 1024 * 50);
        webSettings.setAppCacheEnabled(true);
        Context context = getContext();
        if (context != null) {
            String appCachePath = context.getCacheDir().getAbsolutePath();
            webSettings.setAppCachePath(appCachePath);
        }

        webSettings.setAllowFileAccess(true);    // 可以读取文件缓存
        //缓存模式如下：
        //LOAD_CACHE_ONLY: 不使用网络，只读取本地缓存数据
        //LOAD_DEFAULT: （默认）根据cache-control决定是否从网络上取数据。
        //LOAD_NO_CACHE: 不使用缓存，只从网络获取数据.
        //LOAD_CACHE_ELSE_NETWORK，只要本地有，无论是否过期，或者no-cache，都使用缓存中的数据。
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //不使用缓存:
        webSettings.setCacheMode(WebSettings.LOAD_DEFAULT);
        webSettings.setDatabaseEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);

        //其他细节操作
        webSettings.setJavaScriptCanOpenWindowsAutomatically(false); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

        // 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //设置UserAgent
        String userAgent = webSettings.getUserAgentString();
        webSettings.setUserAgentString(Utils.buildString(userAgent, "; app/", getContext().getPackageName(), ";"));
    }

    public void setOnScrollChangeListener(ScrollWebView.OnScrollChangeListener listener) {
        this.onScrollChangeListener = listener;
    }

    public boolean goBack() {
        if (viewBind.webView != null) {
            if (viewBind.webView.canGoBack()) {
                viewBind.webView.goBack();
                return true;
            } else if (iWebCallback != null) {
                iWebCallback.finish();
                return true;
            }
        }
        return false;
    }

    public void reload() {
        viewBind.webView.reload();
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (detector != null) {
                return detector.onTouchEvent(motionEvent);
            }
        }
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_UP:
                view.requestFocusFromTouch();
                break;
        }
        return false;
    }

    /**
     * 加载进度
     *
     * @param newProgress
     */
    public void setLoadProgress(int newProgress) {
        viewBind.topProgressbar.setProgress(newProgress);
        if (newProgress == 10) {
            viewBind.topProgressbar.setVisibility(View.VISIBLE);
        }
        if (newProgress == 100) {
            viewBind.topProgressbar.setVisibility(View.GONE);
        }
    }

    /**
     * 接收web里的标题
     *
     * @param title
     */
    public void receivedWebTitle(String title) {
        if (iWebCallback != null) {
            iWebCallback.receivedWebTitle(title);
        }
    }

    /**
     * 处理自定义协议
     *
     * @param webView
     * @param url
     * @return
     */
    public boolean shouldOverrideUrlLoading(WebView webView, String url) {
        if (Utils.isNotEmpty(url)) {
            Uri uri = Uri.parse(url);
            if (uri != null && uri.getScheme() != null) {
                if (uri.getScheme().equalsIgnoreCase("http") || uri.getScheme().equalsIgnoreCase("https")) {
                    webView.loadUrl(url);
                } else if (WebProtocolManager.getInstall().handleProtocol(webView.getContext(), uri, protocolCallback)) {
                    return true;
                }
            } else if (url.contains("alipays://platformapi/startApp")) {
                SystemUtils.openAliPayApp(webView.getContext(), url);
                return true;
            }
        }
        return false;
    }

    public void onPageFinished() {
        if (iWebCallback != null) {
            iWebCallback.loadWebFinish();
        }
    }

    public void doUpdateVisitedHistory(String url) {
        if (iWebCallback != null) {
            iWebCallback.showCloseBtn(viewBind.webView.canGoBack());
        }
    }

    public void loadUrl(String url) {
        viewBind.webView.loadUrl(url);
    }

    public void loadJavaScript(String methodArg) {
        viewBind.webView.loadUrl("javascript:" + methodArg);
    }

    public void loadJavaScript(String methodArg, ValueCallback<String> valueCallback) {
        viewBind.webView.evaluateJavascript("javascript:" + methodArg, valueCallback);
    }
}
