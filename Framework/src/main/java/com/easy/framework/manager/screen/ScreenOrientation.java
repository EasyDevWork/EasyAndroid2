package com.easy.framework.manager.screen;

/**
 * 屏幕方向
 */
public class ScreenOrientation {
    public ScreenOrientationLiveData.Orientation orientation;//方向改变
    public int rotateAngle;//旋转角度
    public boolean isChange;//方向是否发生改变

    public ScreenOrientation() {
    }

    @Override
    public String toString() {
        return "ScreenOrientation{" +
                "orientation=" + orientation +
                ", rotateAngle=" + rotateAngle +
                ", isChange=" + isChange +
                '}';
    }
}
