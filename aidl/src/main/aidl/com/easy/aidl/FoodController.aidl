package com.easy.aidl;

import com.easy.aidl.Food;
import com.easy.aidl.IAdilListener;

interface FoodController {
    List<Food> getFoodList();

    void addFood(in Food book1,in Food book2);

    void registerListener(in IAdilListener listener);

    void unregisterListener(in IAdilListener listener);
}
