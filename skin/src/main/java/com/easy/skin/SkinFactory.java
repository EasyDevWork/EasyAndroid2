package com.easy.skin;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Observable;
import java.util.Observer;

public class SkinFactory implements LayoutInflater.Factory2, Observer {
    /**
     * 属性处理类
     */
    private SkinAttribute mSkinAttribute;
    AppCompatViewInflater2 inflater2;
    /**
     * 保存view的构造方法
     */
    private static final HashMap<String, Constructor<? extends View>> sConstructorMap = new HashMap<>();

    private static final Class<?>[] mConstructorSignature = new Class[]{Context.class, AttributeSet.class};

    public final String[] a = new String[]{
            "android.widget.",
            "android.view.",
            "android.webkit."
    };

    public SkinFactory() {
        mSkinAttribute = new SkinAttribute();
        inflater2 = new AppCompatViewInflater2();
    }

    /**
     * AppCompatViewInflater
     *
     * @param parent
     * @param name
     * @param context
     * @param attributeSet
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@Nullable View parent, @NonNull String name, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        View view = inflater2.createView(parent, name, context, attributeSet, false, false, true, false);
        if (view != null) {
            mSkinAttribute.loadView(view, attributeSet);
        }
        return view;
    }

    @Override
    public void update(Observable observable, Object o) {
        //接受到换肤请求
        mSkinAttribute.applySkin();
    }

    public SkinAttribute getSkinAttribute() {
        return mSkinAttribute;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull String s, @NonNull Context context, @NonNull AttributeSet attributeSet) {
        return null;
    }
}
