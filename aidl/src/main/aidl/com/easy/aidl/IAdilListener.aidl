
package com.easy.aidl;

import com.easy.aidl.Book;
import com.easy.aidl.Food;

interface IAdilListener{
   void addFoodCallback(in Food result);
   void addBookCallback(in Book result);
}
