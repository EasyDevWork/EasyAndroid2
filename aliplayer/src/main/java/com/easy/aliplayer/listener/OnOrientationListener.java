package com.easy.aliplayer.listener;

/**
 * 屏幕方向变化事件
 */
public interface OnOrientationListener {
    /**
     * 变为Land_Forward
     *
     * @param fromPort 是否是从竖屏变过来的
     */
    void changedToLandScape(boolean fromPort);

    /**
     * 变为Land_Reverse
     *
     * @param fromPort 是否是从竖屏变过来的
     */
    void changedToLandReverseScape(boolean fromPort);

    /**
     * 变为Port
     *
     * @param fromLand 是否是从横屏变过来的
     */
    void changedToPortrait(boolean fromLand);
}
