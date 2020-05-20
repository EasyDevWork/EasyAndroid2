package com.easy.framework.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.LayoutInflaterCompat;

import com.easy.framework.skin.SkinInflaterFactory;
import com.easy.framework.skin.SkinManager;
import com.easy.framework.skin.inter.IDynamicNewView;
import com.easy.framework.skin.inter.ISkinUpdate;
import com.easy.framework.skin.view_attr.BaseAttr;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 支持换肤
 */
public class SkinActivity extends AppCompatActivity implements ISkinUpdate, IDynamicNewView {

    private SkinInflaterFactory mSkinInflaterFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(getLayoutInflater(), false);
            mSkinInflaterFactory = new SkinInflaterFactory();
            getLayoutInflater().setFactory(mSkinInflaterFactory);
//            LayoutInflaterCompat.setFactory(getLayoutInflater(), mSkinInflaterFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SkinManager.getInstance().attach(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SkinManager.getInstance().detach(this);
        if (mSkinInflaterFactory != null) {
            mSkinInflaterFactory.clean();
        }
    }

    @Override
    public void dynamicAddView(View view, List<BaseAttr> pDAttrs) {
        if (mSkinInflaterFactory != null) {
            mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
        }
    }

    @Override
    public void onThemeUpdate() {
        if (mSkinInflaterFactory != null) {
            mSkinInflaterFactory.applySkin();
        }
    }
}
