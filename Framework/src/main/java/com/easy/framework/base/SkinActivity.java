package com.easy.framework.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.easy.framework.skin.SkinInflaterFactory;
import com.easy.framework.skin.SkinManager;
import com.easy.framework.skin.inter.IDynamicNewView;
import com.easy.framework.skin.inter.ISkinUpdate;
import com.easy.framework.skin.view_attr.DynamicAttr;

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
        mSkinInflaterFactory.clean();
    }

    /**
     * dynamic add a skin view
     *
     * @param view
     * @param attrName
     * @param attrValueResId
     */
    protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
    }

    @Override
    public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
        mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }

    @Override
    public void onThemeUpdate() {
        mSkinInflaterFactory.applySkin();
    }
}
