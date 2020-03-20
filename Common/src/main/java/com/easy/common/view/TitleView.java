package com.easy.common.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.easy.common.R;
import com.easy.utils.Utils;


public class TitleView extends RelativeLayout {

    private ImageView ivBack;
    private TextView tvTitle;
    private TextView tvRight;

    public TitleView(Context context) {
        super(context);
        init();
    }

    public TitleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.title_bar, this);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        tvRight = findViewById(R.id.tvRight);
        setBackground(R.color.color_ffffff);
    }

    public void hideBackBtn() {
        ivBack.setVisibility(GONE);
    }

    public void setLeftClickListener(OnClickListener clickListener) {
        if (clickListener != null) {
            ivBack.setOnClickListener(clickListener);
        }
    }

    public TextView setTitleText(String titleText) {
        if (Utils.isNotEmpty(titleText)) {
            if (tvTitle != null) {
                tvTitle.setText(titleText);
                tvTitle.setVisibility(VISIBLE);
            }
        }
        return tvTitle;

    }

    public void setRightImage(Drawable drawable) {
        if (tvRight != null) {
            tvRight.setVisibility(VISIBLE);
            tvRight.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }
    }

    public void setRightText(String rightText) {
        if (tvRight != null) {
            tvRight.setVisibility(VISIBLE);
            tvRight.setText(rightText);
        }
    }

    public void setRightClickListener(OnClickListener clickListener) {
        if (clickListener != null) {
            tvRight.setOnClickListener(clickListener);
        }
    }

    public void setBackground(int resid) {
        this.setBackgroundResource(resid);
    }
}
