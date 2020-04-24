package com.easy.demo.ui.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.aidl.AIDLService;
import com.easy.aidl.BookController;
import com.easy.aidl.MessengerService;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestAidlBinding;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestAidlActivity", name = "Aidl")
public class TestAidlActivity extends BaseActivity<TestAidlPresenter, TestAidlBinding> implements TestAidlView {
    BookController bookController;
    private boolean connected;
    ServiceConnection aidlConnect = new ServiceConnection() {
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

    //message
    private Messenger messenger;
    private ServiceConnection msgConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            Log.d("TestAidlService", "msg Connected " + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger = null;
            Log.d("TestAidlService", "msg Disconnected " + name);
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
        bindService(intent, aidlConnect, Context.BIND_AUTO_CREATE);
    }

    public void unBindService(View view) {
        if (connected) {
            unbindService(aidlConnect);
        }
    }

    public void bindMsgService(View view) {
        Intent intent = new Intent(TestAidlActivity.this, MessengerService.class);
        bindService(intent, msgConnect, Context.BIND_AUTO_CREATE);
    }

    public void unBindMsgService(View view) {
        if (connected) {
            unbindService(msgConnect);
        }
    }

    public void openClient(View view) {
        ARouter.getInstance().build("/demo/TestAidlClientActivity").navigation();
    }
}
