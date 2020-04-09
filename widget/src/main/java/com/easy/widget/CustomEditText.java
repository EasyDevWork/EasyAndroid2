package com.easy.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.widget.AppCompatEditText;

/**
 * @author gakes
 * 2014-07-14
 */
public class CustomEditText extends AppCompatEditText {

    private Drawable imgDelete, imageSerach;
    boolean isShowDelete;
    boolean isShowSearch;
    boolean isGreyDeleteIcon;
    boolean hasFocus;
    boolean isPassword;
    TextWatcher listener;
    View.OnFocusChangeListener focusChangeListener;
    onDeleteListener onDeleteListener;

    public void setOnDeleteListener(onDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    public interface onDeleteListener {
        void onDelete();
    }


    public CustomEditText(Context context) {
        super(context);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setCustomAttributes(context, attrs);
        init();
    }

    public CustomEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomAttributes(context, attrs);
        init();
    }

    private void setCustomAttributes(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.EditAttrs);
        isShowDelete = array.getBoolean(R.styleable.EditAttrs_showDelete, false);
        isShowSearch = array.getBoolean(R.styleable.EditAttrs_showSearch, false);
        isGreyDeleteIcon = array.getBoolean(R.styleable.EditAttrs_greyDeleteIcon, false);

        array.recycle();
    }

    private void init() {
        if (isShowSearch)
            imageSerach = resourceToDrawable(R.drawable.search);
        if (isShowDelete) {
            imgDelete = resourceToDrawable(isGreyDeleteIcon ? R.drawable.close : R.drawable.close);
            imgDelete.setBounds(0, 0, imgDelete.getIntrinsicWidth(), imgDelete.getIntrinsicHeight());
            setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean focus) {
                    hasFocus = focus;
                    judgeShowDrawable();
                    if (focusChangeListener != null) {
                        focusChangeListener.onFocusChange(view, focus);
                    }
                }
            });
            setListener();
        }
        judgeShowDrawable();

    }

    public Drawable resourceToDrawable(int resource) {
        return getResources().getDrawable(resource);
    }

    public void setFocusChangeListener(View.OnFocusChangeListener focusChangeListener) {
        this.focusChangeListener = focusChangeListener;
    }

    public void onChangedListener(TextWatcher listener) {
        this.listener = listener;
    }

    public void setListener() {
        addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (listener != null) {
                    listener.onTextChanged(s, start, before, count);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (listener != null) {
                    listener.beforeTextChanged(s, start, count, after);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                judgeShowDrawable();
                if (listener != null) {
                    listener.afterTextChanged(s);
                }
            }
        });
    }

    //设置删除图片
    public void judgeShowDrawable() {
        if (isEnabled()) {
            Drawable[] drawables = getCompoundDrawables();
            if (getText().length() < 1) {
                if (isShowSearch) {
                    setCompoundDrawablesWithIntrinsicBounds(imageSerach, null, null, null);
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(drawables != null ? drawables[0] : null, null, null, null);
                }
            } else {
                if (isShowDelete && isShowSearch) {
                    setCompoundDrawablesWithIntrinsicBounds(imageSerach, null, hasFocus ? imgDelete : null, null);
                } else if (isShowDelete && !isShowSearch) {
                    setCompoundDrawablesWithIntrinsicBounds(drawables != null ? drawables[0] : null, null, hasFocus ? imgDelete : null, null);
                } else if (!isShowDelete && isShowSearch) {
                    setCompoundDrawablesWithIntrinsicBounds(imageSerach, null, null, null);
                } else {
                    setCompoundDrawablesWithIntrinsicBounds(drawables != null ? drawables[0] : null, null, null, null);
                }
            }
        } else {
            setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
    }

    /**
     * @return null
     * @Description 是否显示删除键
     * @author chengaobin
     */
    public void setShowDelDrawable() {
        if (isShowDelete && getText().length() > 0) {
            setCompoundDrawablesWithIntrinsicBounds(null, null, imgDelete, null);
        }
        if (isShowSearch && getText().length() < 1) {
            setCompoundDrawablesWithIntrinsicBounds(imageSerach, null, null, null);
        }
    }


    // 处理删除事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (imgDelete != null && event.getAction() == MotionEvent.ACTION_UP) {
            boolean touchable = event.getX() > (getWidth() - getTotalPaddingRight())
                    && (event.getX() < ((getWidth() - getPaddingRight())));
            if (touchable) {
                this.setText("");
                if (onDeleteListener != null) {
                    onDeleteListener.onDelete();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        if(focused){//获取焦点是，移动到文本末尾
            setSelection(getText().length());
        }
    }


}
