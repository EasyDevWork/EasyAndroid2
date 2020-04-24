package com.easy.aidl;

import com.easy.aidl.Book;
import com.easy.aidl.IAdilListener;

interface BookController {
    List<Book> getBookList();
    void addBookInOut(inout Book book);
    void addBookIn(in Book book);
    void addBookOut(out Book book);

    Book addTwoBook(in Book book1,in Book book2);

    void registerListener(in IAdilListener listener);

    void unregisterListener(in IAdilListener listener);
}
