package com.easy.demo.ui.cordinator.behavior;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.easy.demo.R;


public class SimpleContentBehavior extends CoordinatorLayout.Behavior {


    public SimpleContentBehavior() {
    }

    public SimpleContentBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onLayoutChild(@NonNull CoordinatorLayout parent, View child, int layoutDirection) {
        parent.onLayoutChild(child, layoutDirection);
        View headerView = parent.findViewById(R.id.llHeard);
        child.offsetTopAndBottom(headerView.getMeasuredHeight());
        return true;
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        return dependency instanceof ConstraintLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        //计算列表y坐标，最小为0
        float y = dependency.getTranslationY();
        child.setTranslationY(y);
        return true;
    }
}
