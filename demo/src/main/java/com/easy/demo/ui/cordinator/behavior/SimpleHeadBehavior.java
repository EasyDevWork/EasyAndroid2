package com.easy.demo.ui.cordinator.behavior;

import android.content.Context;

import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.ViewCompat;

import com.easy.demo.R;

public class SimpleHeadBehavior extends CoordinatorLayout.Behavior {
    float removeY;
    View llContent;

    public SimpleHeadBehavior() {
    }

    public SimpleHeadBehavior(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, @NonNull View child, int layoutDirection) {
        llContent = parent.findViewById(R.id.llContent);
        return super.onLayoutChild(parent, child, layoutDirection);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        //只关注竖直方向滑动
        return (axes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;
    }

    /**
     * 上滑 dy>，removeY 向下移动的距离
     *
     * @param coordinatorLayout
     * @param child             heard layout
     * @param target            swipeLayout
     * @param dx
     * @param dy                +向上 -向下
     * @param consumed
     * @param type
     */
    @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        float scrollY = dy;
        float fistIndex = llContent.getTranslationY();
        if (dy < 0 && fistIndex >= 0) {
            return;
        }
        if (-removeY + dy > child.getHeight()) {
            Log.d("NestedPre", "大于===》 dy = " + dy + " removeY = " + removeY);
        }
        //向下移动的最大距离不能大于0，否则heard不能在顶部
        /**向上移动的最大距离不能大于header的大小**/
        if (removeY - scrollY <= 0 && -removeY + scrollY <= child.getHeight()) {
            removeY -= scrollY;
            //heard能向下滑动的最大距离
            if (removeY > 0) {
                removeY = 0;
            }
            //heard能向上滑动的最大距离
            if (-removeY >= child.getHeight()) {
                removeY = -child.getHeight();
            }
            if (removeY + child.getHeight() > 0) {
                child.setTranslationY(removeY);
                consumed[1] = dy;
            }
        }
    }
}
