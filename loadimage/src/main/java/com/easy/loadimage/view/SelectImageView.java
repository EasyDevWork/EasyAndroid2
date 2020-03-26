package com.easy.loadimage.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * 选择时会变成半透明的ImageView
 */
public class SelectImageView extends AppCompatImageView {
    private boolean enableState = true;
    private float pressedAlpha = 0.4f;
    private float unableAlpha = 0.3f;

    public SelectImageView(Context context) {
        super(context);
    }

    public SelectImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SelectImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (enableState) {
            if (isPressed()) {
                setAlpha(pressedAlpha);
            } else if (!isEnabled()) {
                setAlpha(unableAlpha);
            } else {
                setAlpha(1.0f);
            }
        }
    }

    public SelectImageView enableState(boolean enableState) {
        this.enableState = enableState;
        return this;
    }

    public SelectImageView pressedAlpha(float pressedAlpha) {
        this.pressedAlpha = pressedAlpha;
        return this;
    }

    public SelectImageView unableAlpha(float unableAlpha) {
        this.unableAlpha = unableAlpha;
        return this;
    }
}
