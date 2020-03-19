package com.easy.common.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.common.R;


/**
 * 无数据页面/错误页面
 */
public class StateView extends RelativeLayout {

    LinearLayout llContainer;
    View.OnClickListener onClickListener;
    TextView tvTips;

    public StateView(Context context) {
        super(context);
        init();
    }

    public StateView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public StateView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.state_view, this);
        tvTips = findViewById(R.id.tvTips);
        llContainer = findViewById(R.id.llContainer);
        llContainer.setOnClickListener(view -> {
            if (onClickListener != null) {
                onClickListener.onClick(view);
            }
        });
        this.setBackgroundResource(R.color.color_ffffff);
    }

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setTips(String msg) {
        tvTips.setText(msg);
    }
}
