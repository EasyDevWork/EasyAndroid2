package com.easy.demo.ui.testweb;

import android.os.Bundle;
import android.view.View;
import android.webkit.ValueCallback;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.common.ui.web.activity.WebBaseActivity;
import com.easy.common.ui.web.base.IWebCallback;
import com.easy.common.ui.web.base.JsToAndroid;
import com.easy.common.ui.web.fragment.WebViewFragment;
import com.easy.demo.R;
import com.easy.demo.databinding.TestWebBinding;
import com.easy.net.rxlifecycle.ActivityEvent;
import com.easy.framework.utils.SystemUtils;
import com.easy.framework.utils.Utils;

@ActivityInject
@Route(path = "/demo/TestWebActivity", name = "测试web")
public class TestWebActivity extends WebBaseActivity<TestWebPresenter, TestWebBinding> implements TestWebView<ActivityEvent>, IWebCallback {
    WebViewFragment webViewFragment;

    @Override
    public int getLayoutId() {
        return R.layout.test_web;
    }

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
        closeSwipeBackLayout();
        initHead();
        initFragment();
    }

    @Override
    public void loadWebFinish() {
        //可手动添加JS 但是必须在web加载完成后加载
        String js = SystemUtils.getAssetFile(getAssets(), "testjs.js");
        if (js != null) {
            webViewFragment.loadJavaScript(js);
        }
    }

    public void callBackJs1(View view) {
        //2种都可以
        webViewFragment.loadJavaScript("javaCallJs()", new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String s) {

            }
        });
//        webViewFragment.loadJavaScript("javaCallJs()");
    }

    public void callBackJs2(View view) {
        //2种都可以
//        webViewFragment.loadJavaScript("javaCallJsWith('22s')", new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String s) {
//
//            }
//        });
        webViewFragment.loadJavaScript("javaCallJsWith('22s')");
    }

    public void callBackJs3(View view) {
        webViewFragment.loadJavaScript("javaCallJs2()");
    }

    @Override
    public void receivedWebTitle(String webTitle) {
        if (Utils.isNotEmpty(webTitle)) {
            viewBind.tvTitle.setText(webTitle);
        }
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public void showCloseBtn(boolean show) {
        if (show) {
            viewBind.ivClose.setVisibility(View.VISIBLE);
        } else {
            viewBind.ivClose.setVisibility(View.GONE);
        }
    }

    @Override
    public JsToAndroid getJsToAndroid() {
        return new JsToAndroidImp(this);
    }

    private void initHead() {
        viewBind.ivBack.setOnClickListener(v -> goBack());
        viewBind.ivClose.setOnClickListener(v -> finish());
        handleMore();
    }

    /**
     * 右上角更多
     */
    private void handleMore() {
        viewBind.ivMore.setVisibility(View.VISIBLE);
        viewBind.ivMore.setOnClickListener(v -> {
            if (webViewFragment != null) {
                webViewFragment.reload();
            }
        });
    }

    private void goBack() {
        if (webViewFragment != null && webViewFragment.goBack()) {
            return;
        }
        finish();
    }

    private void initFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(WebViewFragment.KEY_RUL, "file:///android_asset/testjs.html");
        webViewFragment = (WebViewFragment) Fragment.instantiate(this, WebViewFragment.class.getName(), bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(com.easy.common.R.id.containLayout, webViewFragment);
        fragmentTransaction.commit();
    }

}
