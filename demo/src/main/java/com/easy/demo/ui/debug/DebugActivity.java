package com.easy.demo.ui.debug;

import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.apt.lib.SharePreference;
import com.easy.demo.R;
import com.easy.demo.base.DemoSharePreferences;
import com.easy.demo.databinding.DebugBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.net.event.ActivityEvent;
import com.easy.utils.LanguageUtil;
import com.easy.utils.WebCacheUtils;
import com.easy.widget.TitleView;

@ActivityInject
@Route(path = "/demo/DebugActivity", name = "测试页面")
public class DebugActivity extends BaseActivity<DebugPresenter, DebugBinding> implements DebugView<ActivityEvent> {

    int i = 0;
    TitleView titleView;

    @Override
    public int getLayoutId() {
        return R.layout.debug;
    }

    @Override
    public void initView() {
        titleView = addTitleView();
        if (titleView != null) {
            titleView.setTitleText(getString(R.string.debug_title));
        }
    }

    public void testLottie(View view) {
        ARouter.getInstance().build("/demo/TestLottieActivity").navigation();
    }

    public void testActivity(View view) {
        ARouter.getInstance().build("/demo/TestFragmentActivity").navigation();
//        FragmentManager manager = getSupportFragmentManager();
//        Bundle bundle = new Bundle();
//        bundle.putString("type", "CCCCC");
//        Fragment testFragment = Fragment.instantiate(this, TestFragment.class.getName(), bundle);
//        manager.beginTransaction().replace(R.id.flScreen, testFragment).commit();
    }

    public void tesSharePreference(View view) {
        i++;
        SharePreference.get(this, DemoSharePreferences.class).setNum(i + "");
        show(SharePreference.get(this, DemoSharePreferences.class).getNum());
    }

    public void testLoadImage(View view) {
        ARouter.getInstance().build("/demo/TestLoadImageActivity").navigation();
    }

    public void goTransparent(View view) {
        ARouter.getInstance().build("/demo/TransparentActivity").navigation();
    }

    public void goEosChain(View view) {
        ARouter.getInstance().build("/demo/EosChainActivity").navigation();
    }

    public void testDialog(View view) {
        ARouter.getInstance().build("/demo/DialogActivity").navigation();
    }

    public void goWeChatTest(View view) {
        ARouter.getInstance().build("/demo/WeChatActivity").navigation();
    }

    public void testQrCode(View view) {
        ARouter.getInstance().build("/demo/TestQrCodeActivity").navigation();
    }

    public void goTestRecycleView(View view) {
        ARouter.getInstance().build("/demo/TestRecycleViewActivity").navigation();
    }

    public void testWeb(View view) {
        ARouter.getInstance().build("/demo/TestWebActivity")
                .withString("url", "file:///android_asset/testjs.html").navigation();
    }

    public void goBtcChain(View view) {
        ARouter.getInstance().build("/demo/BtcChainActivity").navigation();
    }

    public void goPlayer(View view) {
        ARouter.getInstance().build("/demo/PlayerActivity").navigation();
    }

    public void cleanWebCache(View view) {
        String[] size = WebCacheUtils.getWebCacheSize(this);
        show(size[0] + size[1]);
        WebCacheUtils.clearWebCache(this);
    }


    public void testLanguage(View view) {
        i++;
        if (i % 2 == 0) {
            LanguageUtil.changeLanguage(this, "zh");
        } else {
            LanguageUtil.changeLanguage(this, "en");
        }
        titleView.setTitleText(getString(R.string.debug_title));
        Log.d("Language", "current1:" + LanguageUtil.getSystemLanguage());
    }


    public void testCache(View view) {
        i++;
        presenter.setTestCache("cache_" + i);
        show(presenter.getTestCache());
    }

    public void show(String content) {
        viewBind.tvScreen.setVisibility(View.VISIBLE);
        viewBind.tvScreen.setText(content);
    }

}
