package com.easy.demo.ui.launcher;

import android.content.Intent;
import android.provider.Settings;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.base.TvBaseActivity;
import com.easy.demo.databinding.TestLauncherBinding;
import com.easy.net.event.ActivityEvent;
import com.easy.utils.SystemUtils;
import com.easy.utils.ToastUtils;

@ActivityInject
@Route(path = "/demo/LauncherActivity", name = "桌面页面")
public class LauncherActivity extends TvBaseActivity<LauncherPresenter, TestLauncherBinding> implements LauncherView<ActivityEvent> {

    @Override
    public int getLayoutId() {
        return R.layout.test_launcher;
    }

    @Override
    public void initView() {
        super.initView();
        viewBind.rlVideo.setOnFocusChangeListener((v, hasFocus) -> onMoveFocusBorder(v, 1.1f));
        viewBind.rlAlbum.setOnFocusChangeListener((v, hasFocus) -> onMoveFocusBorder(v, 1.1f));
        viewBind.rlApp.setOnFocusChangeListener((v, hasFocus) -> onMoveFocusBorder(v, 1.1f));
        viewBind.rlSet.setOnFocusChangeListener((v, hasFocus) -> onMoveFocusBorder(v, 1.1f));

        viewBind.rlVideo.setOnClickListener(v -> {
            ARouter.getInstance().build("/demo/VideoActivity").navigation();
        });

        viewBind.rlAlbum.setOnClickListener(v -> {
            ARouter.getInstance().build("/demo/AlbumActivity").navigation();
        });

        viewBind.rlApp.setOnClickListener(v -> {
            String packageName = "com.cibn.tv";
            boolean hasIn = SystemUtils.isAppExist(LauncherActivity.this, packageName);
            if (hasIn) {
                SystemUtils.openApp(LauncherActivity.this, packageName);
            } else {
                ToastUtils.showShort("你未安装CIBN酷喵影视，请先安装应用");
            }
        });

        viewBind.rlSet.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        });

        viewBind.rlVideo.requestFocus();
    }
}
