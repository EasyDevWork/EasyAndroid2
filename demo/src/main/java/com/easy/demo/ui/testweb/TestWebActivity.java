package com.easy.demo.ui.testweb;

import android.view.View;
import android.webkit.ValueCallback;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestWebBinding;
import com.easy.framework.base.WebBaseActivity;
import com.easy.framework.base.web.IWebCallback;
import com.easy.framework.base.web.JsToAndroid;
import com.easy.utils.SystemUtils;
import com.easy.utils.EmptyUtils;

@ActivityInject
@Route(path = "/demo/TestWebActivity", name = "测试web")
public class TestWebActivity extends WebBaseActivity<TestWebPresenter, TestWebBinding> implements TestWebView, IWebCallback {

    @Override
    public int getLayoutId() {
        return R.layout.test_web;
    }

    @Override
    public int getWebViewContainId() {
        return R.id.containLayout;
    }

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
        closeSwipeBackLayout();
        initHead();
    }

    @Override
    public void loadWebFinish() {
        //可手动添加JS 但是必须在web加载完成后加载
        String js = SystemUtils.getAssetFile(getAssets(), "testjs.js");
        if (js != null && webViewFragment != null) {
            webViewFragment.loadJavaScript(js);
        }
    }

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
            webViewFragment.loadJavaScript("javaCallJsWith('22s')");
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
//
//    private void initFragment() {
//        Bundle bundle = new Bundle();
//        bundle.putString(WebBaseFragment.KEY_RUL, "file:///android_asset/testjs.html");
//        webViewFragment = (WebBaseFragment) Fragment.instantiate(this, WebBaseFragment.class.getName(), bundle);
//        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
//        fragmentTransaction.replace(com.easy.common.R.id.containLayout, webViewFragment);
//        fragmentTransaction.commit();
//    }

}
