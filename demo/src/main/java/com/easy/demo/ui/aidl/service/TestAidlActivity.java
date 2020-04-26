package com.easy.demo.ui.aidl.service;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.aidl.AIDLService;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestAidlBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestAidlActivity", name = "Aidl")
public class TestAidlActivity extends BaseActivity<EmptyPresenter, TestAidlBinding> implements EmptyView {

    boolean isConnect;
    ServiceConnection aidlConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            isConnect = true;
            Log.d("TestAidlService", "Connected " + name);
            viewBind.tvScreen.setText("Connected 成功" + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isConnect = false;
            Log.d("TestAidlService", "Disconnected " + name);
            viewBind.tvScreen.setText("Disconnected 成功" + name);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.test_aidl;
    }

    @Override
    public void initView() {
        viewBind.bindAidlService.setOnClickListener(v -> bindAidlService());
        viewBind.unbindAidlService.setOnClickListener(v -> unbindAidlService());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbindAidlService();
    }

    public void bindAidlService() {
        Intent intent = new Intent(TestAidlActivity.this, AIDLService.class);
        bindService(intent, aidlConnect, Context.BIND_AUTO_CREATE);
    }

    public void unbindAidlService() {
        if (isConnect) {
            unbindService(aidlConnect);
        }
    }

    public void openClient(View view) {
        ARouter.getInstance().build("/demo/TestAidlBookClientActivity").navigation();
    }
}
