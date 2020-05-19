package com.easy.demo.ui.debug;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.apt.lib.SharePreference;
import com.easy.demo.R;
import com.easy.demo.base.DemoSharePreferences;
import com.easy.demo.bean.DebugDo;
import com.easy.demo.databinding.DebugBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.store.bean.Accounts;
import com.easy.utils.LanguageUtil;
import com.easy.utils.ToastUtils;
import com.easy.utils.WebCacheUtils;
import com.easy.widget.TitleView;

import java.util.ArrayList;
import java.util.List;

@ActivityInject
@Route(path = "/demo/DebugActivity", name = "测试页面")
public class DebugActivity extends BaseActivity<DebugPresenter, DebugBinding> implements DebugView {

    int i = 0;
    TitleView titleView;
    List<DebugDo> debugDos = new ArrayList<>();

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
        addData();
        ArrayAdapter<DebugDo> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, debugDos);
        viewBind.listView.setAdapter(adapter);
        viewBind.listView.setOnItemClickListener((parent, view, position, id) -> {
            DebugDo debugDo = adapter.getItem(position);
            debugDo.doAction();
        });
    }

    private void addData() {
        DebugDo mmkv = new DebugDo("mmkv", () -> ARouter.getInstance().build("/demo/TestMmmkvActivity").navigation());
        debugDos.add(mmkv);

//        DebugDo rxjava = new DebugDo("空", () -> ARouter.getInstance().build("/demo/TestRxJavaActivity").navigation());
//        debugDos.add(rxjava);

        DebugDo http = new DebugDo("http", () -> ARouter.getInstance().build("/demo/TestHttpActivity").navigation());
        debugDos.add(http);

        DebugDo socket = new DebugDo("socket", () -> ARouter.getInstance().build("/demo/TestSocketActivity").navigation());
        debugDos.add(socket);

        DebugDo ndk = new DebugDo("NDK", () -> ARouter.getInstance().build("/demo/TestNdkActivity").navigation());
        debugDos.add(ndk);

        DebugDo aidl = new DebugDo("ipc进程间通信", () -> ARouter.getInstance().build("/demo/TestAidlActivity").navigation());
        debugDos.add(aidl);

        DebugDo download = new DebugDo("下载", () -> ARouter.getInstance().build("/demo/DownloadActivity").navigation());
        debugDos.add(download);

        DebugDo testActivity = new DebugDo("页面测试", () -> ARouter.getInstance().build("/demo/TestFragmentActivity").navigation());
        debugDos.add(testActivity);

        DebugDo lifeCycle = new DebugDo("生命周期", () -> ARouter.getInstance().build("/demo/TestLifeCycleActivity").navigation());
        debugDos.add(lifeCycle);

        DebugDo lottie = new DebugDo("Lottie", () -> ARouter.getInstance().build("/demo/TestLottieActivity").navigation());
        debugDos.add(lottie);

        DebugDo router = new DebugDo("页面路由", () -> ARouter.getInstance().build("/demo/TestArouterActivity").navigation());
        debugDos.add(router);

        DebugDo imageZoom = new DebugDo("图片缩放", () -> {
            ArrayList<String> imageUrl = new ArrayList<>();
            imageUrl.add("http://t9.baidu.com/it/u=2268908537,2815455140&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1586156504&t=f2bdac1c78a13b038896170ee6ce4694");
            imageUrl.add("http://t9.baidu.com/it/u=1761131378,1355750940&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1586156504&t=bd8de5251ebb6167e29391b9a92da860");
            imageUrl.add("http://t9.baidu.com/it/u=4169540006,4220376401&fm=79&app=86&size=h300&n=0&g=4n&f=jpeg?sec=1586156504&t=5641e3d175af91a852433d57afb75be9");

            ARouter.getInstance().build("/imagezoom/ImageZoomActivity")
                    .withObject("images", imageUrl)
                    .withInt("position", 1).navigation();
        });
        debugDos.add(imageZoom);

        DebugDo flutter = new DebugDo("flutter", () -> ARouter.getInstance().build("/demo/TestFlutterActivity").navigation());
        debugDos.add(flutter);

        DebugDo aop = new DebugDo("AOP", () -> ARouter.getInstance().build("/demo/TestAopActivity").navigation());
        debugDos.add(aop);

        DebugDo loadImage = new DebugDo("加载图片", () -> ARouter.getInstance().build("/demo/TestLoadImageActivity").navigation());
        debugDos.add(loadImage);

        DebugDo dialog = new DebugDo("对话框", () -> ARouter.getInstance().build("/demo/DialogActivity").navigation());
        debugDos.add(dialog);

        DebugDo WeChat = new DebugDo("微信", () -> ARouter.getInstance().build("/demo/WeChatActivity").navigation());
        debugDos.add(WeChat);

        DebugDo QrCode = new DebugDo("二维码", () -> ARouter.getInstance().build("/demo/TestQrCodeActivity").navigation());
        debugDos.add(QrCode);

        DebugDo RecycleView = new DebugDo("RecycleView", () -> ARouter.getInstance().build("/demo/TestRecycleViewActivity").navigation());
        debugDos.add(RecycleView);

        DebugDo Player = new DebugDo("播放器", () -> ARouter.getInstance().build("/demo/PlayerActivity").navigation());
        debugDos.add(Player);

        DebugDo Coordinator = new DebugDo("控件联动", () -> ARouter.getInstance().build("/demo/TestCoordinatorActivity").navigation());
        debugDos.add(Coordinator);

        DebugDo WebView = new DebugDo("WebView", () -> ARouter.getInstance().build("/demo/TestWebActivity").withString("url", "file:///android_asset/testjs.html").navigation());
        debugDos.add(WebView);

        DebugDo WebCache = new DebugDo("清除web缓存", () -> {
            String[] size = WebCacheUtils.getWebCacheSize(this);
            show(size[0] + size[1]);
            WebCacheUtils.clearWebCache(this);
        });
        debugDos.add(WebCache);

        DebugDo Language = new DebugDo("语言设置", () -> {
            i++;
            if (i % 2 == 0) {
                LanguageUtil.changeLanguage(this, "zh");
            } else {
                LanguageUtil.changeLanguage(this, "en");
            }
            titleView.setTitleText(getString(R.string.debug_title));
            Log.d("Language", "current1:" + LanguageUtil.getSystemLanguage());
        });
        debugDos.add(Language);

        DebugDo tv = new DebugDo("TV", () -> ARouter.getInstance().build("/demo/LauncherActivity").navigation());
        debugDos.add(tv);

        DebugDo mvpvm = new DebugDo("MVPVM", () -> ARouter.getInstance().build("/demo/TestMvvmActivity").navigation());
        debugDos.add(mvpvm);

        DebugDo testCache = new DebugDo("testCache", () -> {
            i++;
            presenter.setTestCache("cache_" + i);
            show(presenter.getTestCache());
        });
        debugDos.add(testCache);

        DebugDo tesSharePreference = new DebugDo("SharePreference", () -> {
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
        });
        debugDos.add(tesSharePreference);

        DebugDo eos = new DebugDo("eos", () -> ARouter.getInstance().build("/demo/EosChainActivity").navigation());
        debugDos.add(eos);

        DebugDo btc = new DebugDo("btc", () -> ToastUtils.showShort("未补充"));
        debugDos.add(btc);

        DebugDo bnb = new DebugDo("bnb", () -> ToastUtils.showShort("未补充"));
        debugDos.add(bnb);

        DebugDo eth = new DebugDo("eth", () -> ToastUtils.showShort("未补充"));
        debugDos.add(eth);

        DebugDo Cosmos = new DebugDo("Cosmos", () -> ToastUtils.showShort("未补充"));
        debugDos.add(Cosmos);

        DebugDo Uuos = new DebugDo("Uuos", () -> ToastUtils.showShort("未补充"));
        debugDos.add(Uuos);

        DebugDo Telos = new DebugDo("Telos", () -> ToastUtils.showShort("未补充"));
        debugDos.add(Telos);
    }

    public void show(String content) {
        viewBind.tvScreen.setVisibility(View.VISIBLE);
        viewBind.tvScreen.setText(content);
    }
}
