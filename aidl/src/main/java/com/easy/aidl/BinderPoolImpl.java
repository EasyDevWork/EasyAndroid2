package com.easy.aidl;

import android.content.ComponentName;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class BinderPoolImpl extends IBinderPool.Stub {

    private static final int CODE_MESSAGE = 1;
    private Messenger serviceMsg = new Messenger(new MessengerHandler());
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

    @Override
    public IBinder queryBinder( ComponentName name, IBinder service, int binderCode) throws RemoteException {
        switch (binderCode) {
            case 100:
                return new BookControllerImpl();
            case 200:
                return new FoodControllerImpl();
            case 300:
                return serviceMsg.getBinder();
            default:
        }
        return null;
    }
}
