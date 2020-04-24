
package com.easy.aidl;

import com.easy.aidl.Book;

interface IAdilListener {
  void onOperationCompleted(in Book result);
}
