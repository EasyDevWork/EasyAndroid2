package com.easy.framework.base.web;

import android.view.MotionEvent;

public abstract class GestureDetectorListenerImp extends IGestureDetectorListener {

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float x = e2.getX() - e1.getX();
        float y = e2.getY() - e1.getY();
        boolean isYInControlled = Math.abs(y) <= 100;
        if (isYInControlled) {
            if (x > 25) {
                slideToLeft();
            } else if (x < -25) {
                slideToRight();
            }
        }
        return false;
    }

    public abstract void slideToLeft();

    public abstract void slideToRight();
}
