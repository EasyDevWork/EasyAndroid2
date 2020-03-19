package com.easy.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.easy.common.R;

/**
 * 列表底部
 */
public class FootView extends RelativeLayout {

    public FootView(Context context) {
        super(context);
        init();
    }

    public FootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public FootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.foot_view, this);
        this.setBackgroundResource(R.color.color_ffffff);
    }
}
