package com.easy.demo.ui.aidl.client;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.net.Uri;
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
import com.easy.aidl.Food;
import com.easy.aidl.FoodController;
import com.easy.aidl.IAdilListener;
import com.easy.aidl.IBinderPool;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestAidlClientBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;
import com.easy.utils.ToastUtils;

import java.util.List;

@ActivityInject
@Route(path = "/demo/TestAidlBookClientActivity", name = "Aidl client")
public class TestAidlBookClientActivity extends BaseActivity<EmptyPresenter, TestAidlClientBinding> implements EmptyView {

    IBinderPool binderPool;
    private BookController bookController;
    private FoodController foodController;
    private Messenger messengerController;
    private boolean connected;
    int i;
    private List<Book> bookList;
    //aidl
    private ServiceConnection aidlConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder binder) {
            binderPool = IBinderPool.Stub.asInterface(binder);
            connected = true;
            viewBind.tvScreen.setText("Connected 成功" + name);
            Log.d("TestAidlClient", "client_Connected name=" + name);
            initBookController(name, binder);
            initFoodController(name, binder);
            initMessageController(name, binder);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
            bookController = null;
            foodController = null;
            messengerController = null;
            Log.d("TestAidlClient", "client_Disconnected name=" + name);
        }
    };

    private IAdilListener iAdilListener = new IAdilListener.Stub() {
        @Override
        public void addFoodCallback(Food result) throws RemoteException {
            runOnUiThread(() -> viewBind.tvScreen.setText("add food=" + result.toString()));
        }

        @Override
        public void addBookCallback(Book result) throws RemoteException {
            runOnUiThread(() -> viewBind.tvScreen.setText("add book=" + result.toString()));
        }
    };
    //message
    private static final int CODE_MESSAGE = 1;//消息通知
    private static final int SQL_UPDATE = 2;
    private Messenger replyMessage = new Messenger(new MessengerHandler());

    private static class MessengerHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case CODE_MESSAGE:
                    int arg = msg.arg1;
                    String receiverMsg = ((Bundle) msg.obj).getString("msg");
                    Log.d("TestAidlClient", arg + "_" + receiverMsg);
                    ToastUtils.showShort(arg + "_" + receiverMsg);
                    break;
                case SQL_UPDATE:
                    break;
            }
        }
    }

    //contentProvider
    private static final String AUTHORITY = "com.easy.studentProvider";
    private static final Uri STUDENT_URI = Uri.parse("content://" + AUTHORITY + "/student");
    ContentResolver contentResolver;

    @Override
    public int getLayoutId() {
        return R.layout.test_aidl_client;
    }

    @Override
    public void initView() {
        contentResolver = getContentResolver();
        contentResolver.registerContentObserver(STUDENT_URI, true, new StudentContentObserver(new MessengerHandler()));
        registerAidl();
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
        }
    }

    public void initBookController(ComponentName name, IBinder binder) {
        if (binderPool != null) {
            try {
                //本客户端的唯一标识是 100
                //获取真实的 Binder 对象
                bookController = BookController.Stub.asInterface(binderPool.queryBinder(name, binder, 100));
                bookController.registerListener(iAdilListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void initFoodController(ComponentName name, IBinder binder) {
        if (binderPool != null) {
            try {
                //本客户端的唯一标识是 100
                //获取真实的 Binder 对象
                foodController = FoodController.Stub.asInterface(binderPool.queryBinder(name, binder, 200));
                foodController.registerListener(iAdilListener);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void initMessageController(ComponentName name, IBinder binder) {
        if (binderPool != null) {
            try {
                messengerController = new Messenger(binderPool.queryBinder(name, binder, 300));
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void registerAidl() {
        Intent intent = new Intent();
        intent.setPackage("com.easy");//服务端包名
        intent.setAction("com.easy.aidl.action");//服务端声明的action
        bindService(intent, aidlConnect, Context.BIND_AUTO_CREATE);
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

    public void addBookCallback(View view) {
        if (connected) {
            i++;
            Book book2 = new Book("Client_add_" + i);
            Book book1 = new Book("Client_add_" + i);
            try {
                bookController.addBook(book1, book2);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addBookInout(View view) {
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
        String msg = "我是客户端的消息";
        bundle.putString("msg", msg);
        viewBind.tvScreen.setText("客户端发送的内容:" + msg);
        message.obj = bundle;
        message.arg1 = 1;
        message.replyTo = replyMessage;
        try {
            messengerController.send(message);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void insertSql(View view) {
        //创建期待匹配的uri
        ContentValues values = new ContentValues();
        values.put("name", "小明");
        //获得ContentResolver对象，调用方法
        contentResolver.insert(STUDENT_URI, values);
    }

    public void deleteSql(View view) {
        contentResolver.delete(STUDENT_URI, "name=?", new String[]{"小明"});
    }

    public void updateSql(View view) {
        ContentValues values1 = new ContentValues();
        values1.put("name", "小李");
        contentResolver.update(STUDENT_URI, values1, "name = ?", new String[]{"小明"});
    }

    public void querySql(View view) {
        Cursor cursor = getContentResolver().query(STUDENT_URI, null, null, null, null);
        cursor.moveToFirst();
        StringBuilder builder = new StringBuilder();
        do {
            String name2 = cursor.getString(cursor.getColumnIndex("name"));
            builder.append("name").append(name2).append("\n");
        } while (cursor.moveToNext());
        cursor.close();
        viewBind.tvScreen.setText(builder.toString());
    }
}
