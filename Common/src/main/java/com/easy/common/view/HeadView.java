package com.easy.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.easy.common.R;


/**
 * 列表头部
 */
public class HeadView extends RelativeLayout {

    public HeadView(Context context) {
        super(context);
        init();
    }

    public HeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.head_view, this);
        this.setBackgroundResource(R.color.color_ffffff);
    }
}
