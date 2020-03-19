package com.easy.common.ui.web.base;

import android.net.Uri;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import com.easy.common.event.ChooseFileEvent;
import com.easy.common.ui.web.fragment.WebViewFragment;

import org.greenrobot.eventbus.EventBus;

import java.lang.ref.WeakReference;

public class WebChromeClientBase extends WebChromeClient {
    WeakReference<WebViewFragment> fragment;

    public WebChromeClientBase(WebViewFragment fragment) {
        this.fragment = new WeakReference<>(fragment);
    }

    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (fragment.get() != null) {
            fragment.get().setLoadProgress(newProgress);
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (fragment.get() != null) {
            fragment.get().receivedWebTitle(title);
        }
    }

    //For Android API >= 21 (5.0 OS)
    @Override
    public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> valueCallback, FileChooserParams fileChooserParams) {
        EventBus.getDefault().post(new ChooseFileEvent(null, valueCallback, true));
        return true;
    }

    //
//    @Override
//    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
//        new AlertDialog.Builder(context)
//                .setTitle("Js调用警告对话框")
//                .setMessage(message)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.confirm();
//                    }
//                })
//                .setCancelable(false)
//                .show();
//        return true;
//    }

//    @Override
//    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
//        new AlertDialog.Builder(context)
//                .setTitle("Js调用确认对话框")
//                .setMessage(message)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.confirm();
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.cancel();
//                    }
//                })
//                .setCancelable(false)
//                .show();
//        return true;
//    }
//
//    @Override
//    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
//        final EditText et = new EditText(context);
//        et.setText(defaultValue);
//        new AlertDialog.Builder(context)
//                .setTitle(message)
//                .setView(et)
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.confirm(et.getText().toString());
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        result.cancel();
//                    }
//                })
//                .setCancelable(false)
//                .show();
//
//        return true;
//    }
}
