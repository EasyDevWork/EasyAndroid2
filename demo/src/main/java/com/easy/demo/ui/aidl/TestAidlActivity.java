package com.easy.demo.ui.aidl;

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
import com.easy.aidl.BookController;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestAidlBinding;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestAidlActivity", name = "Aidl")
public class TestAidlActivity extends BaseActivity<TestAidlPresenter, TestAidlBinding> implements TestAidlView {
    BookController bookController;
    private boolean connected;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookController = BookController.Stub.asInterface(service);
            Log.d("TestAidlService", "Connected " + name);
            viewBind.tvScreen.setText("Connected 成功" + name);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
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

    }

    public void bindService(View view) {
        Intent intent = new Intent(TestAidlActivity.this, AIDLService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void unBindService(View view) {
        if (connected) {
            unbindService(conn);
        }
    }

    public void openClient(View view) {
        ARouter.getInstance().build("/demo/TestAidlClientActivity").navigation();
    }
}
