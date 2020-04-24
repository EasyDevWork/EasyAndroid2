package com.easy.aidl;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MessengerService extends Service {

    private static final int CODE_MESSAGE = 1;

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_MESSAGE: {
                    String receiverMsg = ((Bundle) msg.obj).getString("msg");
                    Log.d("TestAidlService", "服务端收到了消息：" + receiverMsg);
                    Message message = new Message();
                    message.what = CODE_MESSAGE;
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", "服务端处理后消息【" + receiverMsg + "】");
                    message.obj = bundle;
                    message.arg1 = msg.arg1 * 2;
                    try {
                        //取得客户端的 Messenger 对象
                        Messenger messenger = msg.replyTo;
                        messenger.send(message);
                    } catch (RemoteException e) {
                        e.printStackTrace();
                    }
                    break;
                }
            }
        }
    }

    private Messenger messenger = new Messenger(new MessengerHandler());

    public MessengerService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return messenger.getBinder();
    }

}
