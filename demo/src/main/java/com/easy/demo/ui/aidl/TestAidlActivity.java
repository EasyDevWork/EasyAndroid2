package com.easy.demo.ui.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.aidl.IBaseAidlInterface;
import com.easy.aidl.MyRemoteService;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestAidlBinding;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestAidlActivity", name = "Aidl")
public class TestAidlActivity extends BaseActivity<TestAidlPresenter, TestAidlBinding> implements TestAidlView {
    IBaseAidlInterface iBaseAidlInterface;

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            iBaseAidlInterface = IBaseAidlInterface.Stub.asInterface(service);
            if (iBaseAidlInterface != null) {
                try {
                    iBaseAidlInterface.basicTypes(1024, 2048L, true, 3.14159F, 3.1415926D, "I come from MyRemoteService");
                    Log.d("RemoteValue", "Set value success!");
                } catch (Exception e) {
                    Log.d("RemoteValue", "Set value fail!");
                }
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d("RemoteValue", "onServiceDisconnected " + name);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.test_aidl;
    }

    @Override
    public void initView() {

    }

    public void onBind(View view){
        Intent intent = new Intent(TestAidlActivity.this, MyRemoteService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void onUnBind(View view){
        unbindService(conn);
    }
}
