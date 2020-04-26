package com.easy.aidl;

import android.os.RemoteCallbackList;
import android.os.RemoteException;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class FoodControllerImpl extends FoodController.Stub {
    private List<Food> bookList = new ArrayList<>();
    private RemoteCallbackList<IAdilListener> callbackList = new RemoteCallbackList<>();

    @Override
    public List<Food> getFoodList() throws RemoteException {
        return bookList;
    }

    @Override
    public void addFood(Food food1, Food food2) throws RemoteException {
        Food food = new Food(food1.getName() + "+" + food2.getName());
        int count = callbackList.beginBroadcast();
        for (int i = 0; i < count; i++) {
            IAdilListener listener = callbackList.getBroadcastItem(i);
            if (listener != null) {
                listener.addFoodCallback(food);
            }
        }
        callbackList.finishBroadcast();
    }

    @Override
    public void registerListener(IAdilListener listener) throws RemoteException {

    }

    @Override
    public void unregisterListener(IAdilListener listener) throws RemoteException {

    }
}
