package com.easy.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.webkit.WebView;

public class ScrollWebView extends WebView {

    boolean isScroll = true;//是否可滚动

    private OnScrollChangeListener mOnScrollChangeListener;


    public ScrollWebView(Context context) {
        super(context);
    }

    public ScrollWebView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public ScrollWebView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setScroll(boolean scroll) {
        isScroll = scroll;
    }

    //禁止滑动
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return isScroll && super.onTouchEvent(event);
    }

    //2.4.1滑动监听
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        if (mOnScrollChangeListener != null) {
            mOnScrollChangeListener.onScrollChanged(l, t, oldl, oldt);
        }
        super.onScrollChanged(l, t, oldl, oldt);
    }

    public void setOnScrollChangeListener(OnScrollChangeListener listener) {
        this.mOnScrollChangeListener = listener;
    }

    public interface OnScrollChangeListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    @Override
    public boolean performClick() {
        return super.performClick();
    }
}
