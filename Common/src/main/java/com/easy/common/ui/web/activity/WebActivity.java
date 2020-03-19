package com.easy.common.ui.web.activity;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.common.R;
import com.easy.common.databinding.WebActivityBinding;
import com.easy.common.ui.web.base.IWebCallback;
import com.easy.common.ui.web.base.JsToAndroid;
import com.easy.common.ui.web.fragment.WebViewFragment;
import com.easy.framework.rxlifecycle.ActivityEvent;
import com.easy.framework.utils.ToastUtils;
import com.easy.framework.utils.Utils;

@ActivityInject
@Route(path = "/common/WebActivity", name = "web页面")
public class WebActivity extends WebBaseActivity<WebPresenter, WebActivityBinding> implements WebView<ActivityEvent>, IWebCallback {

    @Autowired(name = "url")
    String url;
    @Autowired(name = "title")
    String title;//有值显示，没值显示webview里的标题
    @Autowired(name = "openGesture")
    boolean openGesture = false;
    @Autowired(name = "htmlData")
    String htmlData;

    WebViewFragment webViewFragment;

    @Override
    public int getLayoutId() {
        return R.layout.web_activity;
    }

    @Override
    public void initView() {
        ARouter.getInstance().inject(this);
        closeSwipeBackLayout();
        initHead();
        initData();
        initFragment();
    }

    @Override
    public void onBackPressed() {
        goBack();
    }

    @Override
    public void loadWebFinish() {

    }

    @Override
    public void receivedWebTitle(String webTitle) {
        if (Utils.isEmpty(title) && Utils.isNotEmpty(webTitle)) {
            viewBind.tvTitle.setText(webTitle);
        }
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
        return null;
    }

    private void initData() {
        viewBind.tvTitle.setText(title);
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
            ToastUtils.showLong("more");
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
        bundle.putString(WebViewFragment.KEY_RUL, url);
        bundle.putString(WebViewFragment.HTML_DATA, htmlData);
        bundle.putBoolean(WebViewFragment.OPEN_GESTURE, openGesture);
        webViewFragment = (WebViewFragment) Fragment.instantiate(this, WebViewFragment.class.getName(), bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.containLayout, webViewFragment);
        fragmentTransaction.commit();
    }

}
