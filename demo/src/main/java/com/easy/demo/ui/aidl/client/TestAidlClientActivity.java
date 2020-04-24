package com.easy.demo.ui.aidl.client;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.aidl.Book;
import com.easy.aidl.BookController;
import com.easy.aidl.IAdilListener;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestAidlClientBinding;
import com.easy.framework.base.BaseActivity;

import java.util.List;

@ActivityInject
@Route(path = "/demo/TestAidlClientActivity", name = "Aidl client")
public class TestAidlClientActivity extends BaseActivity<TestAidlClientPresenter, TestAidlClientBinding> implements TestAidClientView {

    private BookController bookController;
    private boolean connected;
    int i;
    private List<Book> bookList;
    //aidl
    private ServiceConnection aidlConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookController = BookController.Stub.asInterface(service);
            connected = true;
            try {
                //添加回调监听
                bookController.registerListener(iAdilListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            Log.d("TestAidlClient", "client_Connected name=" + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
            bookController = null;
            Log.d("TestAidlClient", "client_Disconnected name=" + name);
        }
    };

    private IAdilListener iAdilListener = new IAdilListener.Stub() {
        @Override
        public void onOperationCompleted(final Book result) {
            runOnUiThread(() -> viewBind.tvScreen.setText("two add book=" + result.toString()));
        }
    };
    //message
    private static final int CODE_MESSAGE = 1;
    private Messenger messenger;
    private Messenger replyMessage = new Messenger(new MessengerHandler());
    private ServiceConnection msgConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            Log.d("TestAidlClient", "client_Connected name=" + name);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            messenger = null;
            Log.d("TestAidlClient", "client_Disconnected name=" + name);
        }
    };

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_MESSAGE: {
                    int arg = msg.arg1;
                    String receiverMsg = ((Bundle) msg.obj).getString("msg");
                    Log.d("TestAidlClient", "客户端收到了服务端回复的消息：" + arg + "_" + receiverMsg);
                    break;
                }
            }
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.test_aidl_client;
    }

    @Override
    public void initView() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connected) {
            unbindService(aidlConnect);
            try {
                bookController.unregisterListener(iAdilListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            unbindService(msgConnect);
        }
    }

    public void registerAidl(View view) {
        Intent intent = new Intent();
        intent.setPackage("com.easy");//服务端包名
        intent.setAction("com.easy.aidl.action");//服务端声明的action
        bindService(intent, aidlConnect, Context.BIND_AUTO_CREATE);
    }

    public void registerMessage(View view) {
        Intent intent = new Intent();
        intent.setPackage("com.easy");//服务端包名
        intent.setAction("com.easy.message.action");//服务端声明的action
        bindService(intent, msgConnect, Context.BIND_AUTO_CREATE);
    }

    public void getBook(View view) {
        if (connected) {
            try {
                bookList = bookController.getBookList();
                Log.d("TestAidlClient", bookList.toString());
                viewBind.tvScreen.setText(bookList.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TestAidlClient", "连接失败");
        }
    }

    public void addBookIn(View view) {
        if (connected) {
            i++;
            Book book = new Book("ClientBook_in_" + i);
            try {
                bookController.addBookIn(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addBookOut(View view) {
        if (connected) {
            i++;
            Book book = new Book("ClientBook_out_" + i);
            try {
                bookController.addBookOut(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addTwoBook(View view) {
        if (connected) {
            i++;
            Book book2 = new Book("Client_add_" + i);
            Book book1 = new Book("Client_add_" + i);
            try {
                bookController.addTwoBook(book1, book2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addBook(View view) {
        if (connected) {
            i++;
            Book book = new Book("ClientBook_" + i);
            try {
                bookController.addBookInOut(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TestAidlClient", "连接失败");
        }
    }

    public void testMsg(View v) {
        Message message = new Message();
        message.what = CODE_MESSAGE;
        Bundle bundle = new Bundle();
        bundle.putString("msg", "客户端发送的内容");
        message.obj = bundle;
        message.arg1 = 1;
        message.replyTo = replyMessage;
        try {
            messenger.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
