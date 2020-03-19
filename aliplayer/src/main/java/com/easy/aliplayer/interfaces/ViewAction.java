package com.easy.aliplayer.interfaces;

import com.easy.aliplayer.base.ScreenMode;
import com.easy.aliplayer.view.ControlView;

/**
 * 定义UI界面通用的操作。
 * 主要实现类有{@link ControlView} ,
 */

public interface ViewAction {

    /**
     * 隐藏类型
     */
     enum HideType {
        /**
         * 正常情况下的隐藏
         */
        Normal,
        /**
         * 播放结束的隐藏，比如出错了
         */
        End
    }

    /**
     * 重置
     */
     void reset();

    /**
     * 显示
     */
     void show();

    /**
     * 隐藏
     *
     * @param hideType 隐藏类型
     */
     void hide(HideType hideType);

    /**
     * 设置屏幕全屏情况
     *
     * @param mode {@link ScreenMode#Small}：小屏. {@link ScreenMode#Full}:全屏
     */
     void setScreenModeStatus(ScreenMode mode);
}
