package com.easy.demo.ui.testweb;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestWebBinding;
import com.easy.framework.base.WebBaseActivity;
import com.easy.framework.base.WebBaseFragment;
import com.easy.framework.base.web.IWebCallback;
import com.easy.framework.base.web.JsToAndroid;
import com.easy.utils.EmptyUtils;
import com.easy.utils.SystemUtils;

@ActivityInject
@Route(path = "/demo/TestWebActivity", name = "测试web")
public class TestWebActivity extends WebBaseActivity<TestWebPresenter, TestWebBinding> implements TestWebView, IWebCallback {

    @Autowired(name = "url")
    public String url;
    @Autowired(name = "title")
    public String title;//有值显示，没值显示webview里的标题
    @Autowired(name = "openGesture")
    boolean openGesture = false;
    @Autowired(name = "htmlData")
    public String htmlData;

    @Override
    public int getLayoutId() {
        return R.layout.test_web;
    }

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
        closeSwipeBackLayout();
        initHead();
        initFragment(url, htmlData, openGesture);
    }

    public void initFragment(String url, String htmlData, boolean openGesture) {
        Bundle bundle = new Bundle();
        bundle.putString(WebBaseFragment.KEY_RUL, url);
        bundle.putString(WebBaseFragment.HTML_DATA, htmlData);
        bundle.putBoolean(WebBaseFragment.OPEN_GESTURE, openGesture);
        webViewFragment = (WebBaseFragment) Fragment.instantiate(this, WebBaseFragment.class.getName(), bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containLayout, webViewFragment);
        fragmentTransaction.commit();
    }

    @Override
    public void loadWebFinish() {
        //可手动添加JS 但是必须在web加载完成后加载
        String js = SystemUtils.getAssetFile(getAssets(), "testjs.js");
        if (js != null && webViewFragment != null) {
            webViewFragment.loadJavaScript(js);
        }
    }

    /**
     * 调用JS无参
     * @param view
     */
    public void callBackJs1(View view) {
        if (webViewFragment != null) {
            //2种都可以
            webViewFragment.loadJavaScript("javaCallJs()", s -> {

            });
//        webViewFragment.loadJavaScript("javaCallJs()");
        }
    }

    public void callBackJs2(View view) {
        if (webViewFragment != null) {
            //2种都可以
//        webViewFragment.loadJavaScript("javaCallJsWith('22s')", new ValueCallback<String>() {
//            @Override
//            public void onReceiveValue(String s) {
//
//            }
//        });
            webViewFragment.loadJavaScript("javaCallJsWith('java传来的参数')");
        }
    }

    public void callBackJs3(View view) {
        if (webViewFragment != null) {
            webViewFragment.loadJavaScript("javaCallJs2()");
        }
    }

    @Override
    public void receivedWebTitle(String webTitle) {
        if (EmptyUtils.isNotEmpty(webTitle)) {
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

}
