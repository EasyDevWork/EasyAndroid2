package com.easy.tv.ui.launcher;

import android.content.ComponentName;
import android.content.Intent;
import android.provider.Settings;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.framework.rxlifecycle.ActivityEvent;
import com.easy.framework.utils.ToastUtils;
import com.easy.tv.R;
import com.easy.tv.base.TvBaseActivity;
import com.easy.tv.databinding.LauncherBinding;

@ActivityInject
@Route(path = "/tv/LauncherActivity", name = "桌面页面")
public class LauncherActivity extends TvBaseActivity<LauncherPresenter, LauncherBinding> implements LauncherView<ActivityEvent> {

    private static final int REQUEST_CODE_APP_INSTALL = 100;

    @Override
    public int getLayoutId() {
        return R.layout.launcher;
    }

//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            boolean hasInstallPermission = isHasInstallPermissionWithO(context);
//            if (!hasInstallPermission) {
//                startInstallPermissionSettingActivity(context);
//                return;
//            }
//        }
//    }

//    /**
//     * 开启设置安装未知来源应用权限界面
//     *
//     * @param context
//     */
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private void startInstallPermissionSettingActivity(Context context) {
//        if (context == null) {
//            return;
//        }
//        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
//        ((Activity) context).startActivityForResult(intent, REQUEST_CODE_APP_INSTALL);
//    }

//    @RequiresApi(api = Build.VERSION_CODES.O)
//    private boolean isHasInstallPermissionWithO(Context context) {
//        if (context == null) {
//            return false;
//        }
//        return context.getPackageManager().canRequestPackageInstalls();
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE_APP_INSTALL) {
//            ToastUtils.showShort("ddd");
//        }
//    }


    @Override
    public void initView() {
        super.initView();
        viewBind.rlVideo.setOnFocusChangeListener((v, hasFocus) -> onMoveFocusBorder(v, 1.1f));
        viewBind.rlAlbum.setOnFocusChangeListener((v, hasFocus) -> onMoveFocusBorder(v, 1.1f));
        viewBind.rlApp.setOnFocusChangeListener((v, hasFocus) -> onMoveFocusBorder(v, 1.1f));
        viewBind.rlSet.setOnFocusChangeListener((v, hasFocus) -> onMoveFocusBorder(v, 1.1f));

        viewBind.rlVideo.setOnClickListener(v -> {
            ARouter.getInstance().build("/tv/VideoActivity").navigation();
        });

        viewBind.rlAlbum.setOnClickListener(v -> {
            ARouter.getInstance().build("/tv/AlbumActivity").navigation();
        });

        viewBind.rlApp.setOnClickListener(v -> {
            try {
                ComponentName componet = new ComponentName("com.cibn.tv", "com.yunos.tv.yingshi.home.HomeActivity");
                Intent intent = new Intent();
                intent.setComponent(componet);
                startActivity(intent);
            } catch (Exception e) {
                e.printStackTrace();
                ToastUtils.showShort("你未安装CIBN酷喵影视");
            }
        });

        viewBind.rlSet.setOnClickListener(v -> {
            Intent intent = new Intent(Settings.ACTION_SETTINGS);
            startActivity(intent);
        });

        onMoveFocusBorder(viewBind.rlVideo, 1.1f);

    }
}
