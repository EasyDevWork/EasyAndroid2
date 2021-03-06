package com.easy.aidl;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class BookControllerImpl extends BookController.Stub {
    private List<Book> bookList = new ArrayList<>();
    private RemoteCallbackList<IAdilListener> callbackList = new RemoteCallbackList<>();

    @Override
    public List<Book> getBookList() throws RemoteException {
        Log.d("TestAidl", "AIDLService 返回书籍列表");
        return bookList;
    }

    @Override
    public void addBookInOut(Book book) throws RemoteException {
        if (book != null) {
            bookList.add(book);
            Log.d("TestAidl", "AIDLService inout 添加一本书");
        } else {
            Log.d("TestAidl", "AIDLService inout 接收到了一个空对象");
        }
    }

    @Override
    public void addBookIn(Book book) throws RemoteException {
        if (book != null) {
            bookList.add(book);
            Log.d("TestAidl", "AIDLService in 添加一本书");
        } else {
            Log.d("TestAidl", "AIDLService in 接收到了一个空对象");
        }
    }

    @Override
    public void addBookOut(Book book) throws RemoteException {
        if (book != null) {
            bookList.add(book);
            Log.d("TestAidl", "AIDLService out 添加一本书");
        } else {
            Log.d("TestAidl", "AIDLService out 接收到了一个空对象");
        }
    }

    @Override
    public void addBook(Book book1, Book book2) throws RemoteException {
        Book book = new Book(book1.getName() + "|" + book2.getName());
        int count = callbackList.beginBroadcast();
        for (int i = 0; i < count; i++) {
            IAdilListener listener = callbackList.getBroadcastItem(i);
            if (listener != null) {
                listener.addBookCallback(book);
            }
        }
        callbackList.finishBroadcast();
    }

    @Override
    public void registerListener(IAdilListener listener) throws RemoteException {
        callbackList.register(listener);
        Log.e("TestAidl", "注册回调成功");
    }

    @Override
    public void unregisterListener(IAdilListener listener) throws RemoteException {
        callbackList.unregister(listener);
        Log.e("TestAidl", "解除注册回调成功");
    }
}
