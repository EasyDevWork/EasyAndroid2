package com.easy.demo.ui.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.EmptyBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;
import com.easy.utils.ToastUtils;

@ActivityInject
@Route(path = "/demo/TestNotificationActivity", name = "通知测试")
public class TestNotificationActivity extends BaseActivity<EmptyPresenter, EmptyBinding> implements EmptyView {

    int i = 1;

    @Override
    public int getLayoutId() {
        return R.layout.test_notification;
    }

    @Override
    public void initView() {

    }

    public void sendImportNotify(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Notification notification = NotificationHelp.sendNotification(this, NotificationHelp.CHAT_ID, NotificationHelp.CHAT_NAME, NotificationManager.IMPORTANCE_HIGH);
            NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (nm != null) {
                nm.notify(++i, notification);
            }
        }
    }

    public void sendCommonNotify(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Notification notification = NotificationHelp.sendNotification(this, NotificationHelp.SUB_ID, NotificationHelp.SUB_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager nm = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
            if (nm != null) {
                nm.notify(++i, notification);
            }
        }
    }

    public void goSetting(View v) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = mNotificationManager.getNotificationChannel("悬浮");//CHANNEL_ID是自己定义的渠道ID
            if (channel.getImportance() == NotificationManager.IMPORTANCE_DEFAULT) {//未开启
                // 跳转到设置页面
                ToastUtils.showShort("未开启悬浮");
            } else if (channel.getImportance() == NotificationManager.IMPORTANCE_HIGH) {
                ToastUtils.showShort("已开启悬浮");
            }
            Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
            intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
            intent.putExtra(Settings.EXTRA_CHANNEL_ID, channel.getId());
            startActivity(intent);
        }
    }
}
