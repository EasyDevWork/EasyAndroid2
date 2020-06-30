package com.easy.framework.manager.screen;

import android.content.Context;
import android.hardware.SensorManager;
import android.util.Log;
import android.view.OrientationEventListener;

import androidx.lifecycle.LiveData;

public class ScreenOrientationLiveData extends LiveData<ScreenOrientation> {

    private Context mContext;
    //系统的屏幕方向改变监听
    private OrientationEventListener mLandOrientationListener;
    //上次屏幕的方向
    private Orientation mLastOrientation = Orientation.PORT_FORWARD;

    public enum Orientation {
        /**
         * 竖屏,正向
         */
        PORT_FORWARD("竖屏,正向"),
        /**
         * 竖屏,反向
         */
        PORT_REVERSE("竖屏,反向"),
        /**
         * 横屏,正向
         */
        LAND_FORWARD("横屏,正向"),
        /**
         * 横屏,反向
         */
        LAND_REVERSE("横屏,反向");

        private String desc;

        Orientation(String desc) {
            this.desc = desc;
        }

        @Override
        public String toString() {
            return desc;
        }
    }

    public ScreenOrientationLiveData(Context mContext) {
        this.mContext = mContext.getApplicationContext();
    }

    @Override
    protected void onActive() {
        super.onActive();
        Log.d("ScreenOrientation", "onActive");
        startWatch();
    }

    @Override
    protected void onInactive() {
        super.onInactive();
        Log.d("ScreenOrientation", "onInactive");
        stopWatch();
    }

    public void stopWatch() {
        if (mLandOrientationListener != null) {
            mLandOrientationListener.disable();
        }
    }

    /**
     * 开始监听
     */
    public void startWatch() {
        if (mLandOrientationListener == null) {
            mLandOrientationListener = new OrientationEventListener(mContext, SensorManager.SENSOR_DELAY_NORMAL) {
                @Override
                public void onOrientationChanged(int orientation) {
                    if (orientation == -1) {
                        return;
                    }
                    ScreenOrientation screenOrientation = new ScreenOrientation();
                    screenOrientation.rotateAngle = orientation;
                    if (orientation <= 45 || orientation >= 315) {//正向竖屏
                        screenOrientation.orientation = Orientation.PORT_FORWARD;
                        screenOrientation.isChange = mLastOrientation != Orientation.PORT_FORWARD;
                        mLastOrientation = Orientation.PORT_FORWARD;
                    } else if (orientation <= 225 && orientation >= 135) {//反向竖屏
                        screenOrientation.orientation = Orientation.PORT_REVERSE;
                        screenOrientation.isChange = mLastOrientation != Orientation.PORT_REVERSE;
                        mLastOrientation = Orientation.PORT_REVERSE;
                    } else if (orientation > 225) {//正向横屏
                        screenOrientation.orientation = Orientation.LAND_FORWARD;
                        screenOrientation.isChange = mLastOrientation != Orientation.LAND_FORWARD;
                        mLastOrientation = Orientation.LAND_FORWARD;
                    } else {//反向横屏
                        screenOrientation.orientation = Orientation.LAND_REVERSE;
                        screenOrientation.isChange = mLastOrientation != Orientation.LAND_REVERSE;
                        mLastOrientation = Orientation.LAND_REVERSE;
                    }
                    setValue(screenOrientation);
                }
            };
        }
        mLandOrientationListener.enable();
    }
}
