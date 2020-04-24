package com.easy.demo.ui.aidl;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.aidl.AIDLService;
import com.easy.aidl.Book;
import com.easy.aidl.BookController;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestAidlBinding;
import com.easy.framework.base.BaseActivity;

import java.util.List;

@ActivityInject
@Route(path = "/demo/TestAidlActivity", name = "Aidl")
public class TestAidlActivity extends BaseActivity<TestAidlPresenter, TestAidlBinding> implements TestAidlView {
    BookController bookController;
    private boolean connected;
    private List<Book> bookList;
    int i;
    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            bookController = BookController.Stub.asInterface(service);
            Log.d("TestAidlService", "Connected " + name);
            connected = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            connected = false;
            Log.d("TestAidlService", "Disconnected " + name);
        }
    };

    @Override
    public int getLayoutId() {
        return R.layout.test_aidl;
    }

    @Override
    public void initView() {
        bindService();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (connected) {
            unbindService(conn);
        }
    }

    private void bindService() {
        Intent intent = new Intent(TestAidlActivity.this, AIDLService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
    }

    public void getBook(View view) {
        if (connected) {
            try {
                bookList = bookController.getBookList();
                Log.d("TestAidlService", bookList.toString());
                viewBind.tvScreen.setText(bookList.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TestAidlService", "连接失败");
        }
    }

    public void addBookIn(View view) {
        if (connected) {
            i++;
            Book book = new Book("ServiceBook_In_" + i);
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
            Book book = new Book("ServiceBook_out_" + i);
            try {
                bookController.addBookOut(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    public void addBook(View view) {
        if (connected) {
            i++;
            Book book = new Book("ServiceBook_" + i);
            try {
                bookController.addBookInOut(book);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        } else {
            Log.d("TestAidlService", "连接失败");
        }
    }
}
