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
import com.easy.store.bean.Accounts;
import com.easy.utils.LanguageUtil;
import com.easy.utils.ToastUtils;
import com.easy.utils.WebCacheUtils;
import com.easy.widget.TitleView;

import java.util.ArrayList;

@ActivityInject
@Route(path = "/demo/DebugActivity", name = "测试页面")
public class DebugActivity extends BaseActivity<DebugPresenter, DebugBinding> implements DebugView {

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

    public void testDownload(View view) {
        ARouter.getInstance().build("/demo/DownloadActivity").navigation();
    }

    public void goLifeCycle(View view) {
        ARouter.getInstance().build("/demo/TestLifeCycleActivity").navigation();
    }

    public void testLottie(View view) {
        ARouter.getInstance().build("/demo/TestLottieActivity").navigation();
    }

    public void testRouter(View view) {
        ARouter.getInstance().build("/demo/TestArouterActivity").navigation();
    }

    public void testActivity(View view) {
        ARouter.getInstance().build("/demo/TestFragmentActivity").navigation();
    }

    public void imageZoom(View view) {
        ArrayList<String> imageUrl = new ArrayList<>();
        imageUrl.add("http://t9.baidu.com/it/u=2268908537,2815455140&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1586156504&t=f2bdac1c78a13b038896170ee6ce4694");
        imageUrl.add("http://t9.baidu.com/it/u=1761131378,1355750940&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1586156504&t=bd8de5251ebb6167e29391b9a92da860");
        imageUrl.add("http://t9.baidu.com/it/u=4169540006,4220376401&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1586156504&t=5641e3d175af91a852433d57afb75be9");

        ARouter.getInstance().build("/imagezoom/ImageZoomActivity")
                .withObject("images", imageUrl)
                .withInt("position", 1).navigation();
    }

    public void tesSharePreference(View view) {
        i++;
        DemoSharePreferences sharePreferences = SharePreference.get(this, DemoSharePreferences.class);
        sharePreferences.setNum(i + "");
        Accounts account = new Accounts();
        account.setAge(11);
        account.setName("sss");
        sharePreferences.setUserInfo(account);
        StringBuilder builder = new StringBuilder();
        builder.append("num:").append(sharePreferences.getNum());
        builder.append("guide:").append(sharePreferences.isGoGuide());
        builder.append(" account:").append(sharePreferences.getUserInfo());
        show(builder.toString());
    }

    public void testLoadImage(View view) {
        ARouter.getInstance().build("/demo/TestLoadImageActivity").navigation();
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

    public void goTv(View view) {
        ARouter.getInstance().build("/demo/LauncherActivity").navigation();
    }

    public void goMvvm(View view) {
        ARouter.getInstance().build("/demo/TestMvvmActivity").navigation();
    }

    public void goEosChain(View view) {
        ARouter.getInstance().build("/demo/EosChainActivity").navigation();
    }

    public void goBtcChain(View view) {
        ToastUtils.showShort("未补充");
    }

    public void goBnbChain(View view) {
        ToastUtils.showShort("未补充");
    }

    public void goEthChain(View view) {
        ToastUtils.showShort("未补充");
    }

    public void goCosmosChain(View view) {
        ToastUtils.showShort("未补充");
    }

    public void goUuosChain(View view) {
        ToastUtils.showShort("未补充");
    }

    public void goTelosChain(View view) {
        ToastUtils.showShort("未补充");
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
