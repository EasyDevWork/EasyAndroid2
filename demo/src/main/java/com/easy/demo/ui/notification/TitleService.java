package com.easy.demo.ui.notification;

import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.TileService;

import androidx.annotation.RequiresApi;

import com.easy.demo.ui.debug.DebugActivity;
import com.easy.utils.ToastUtils;

/**
 * 自定义下拉工具栏里的工具
 */
@RequiresApi(api = Build.VERSION_CODES.N)
public class TitleService extends TileService {
    public TitleService() {
    }

    @Override
    public void onTileAdded() {
        super.onTileAdded();
        ToastUtils.showShort("onTileAdded");
    }

    @Override
    public void onTileRemoved() {
        super.onTileRemoved();
        ToastUtils.showShort("onTileRemoved");
    }

    @Override
    public void onClick() {
        super.onClick();
        Intent intent = new Intent(this, DebugActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
