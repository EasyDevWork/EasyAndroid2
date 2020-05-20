package com.easy.framework.skin;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.LayoutInflater.Factory;

import com.easy.framework.skin.view_attr.BaseAttr;
import com.easy.framework.skin.view_attr.SkinAttr;
import com.easy.utils.EmptyUtils;

import java.util.ArrayList;
import java.util.List;

public class SkinInflaterFactory implements Factory {
    /**
     * Store the view item that need skin changing in the activity
     */
    private List<SkinItem> mSkinItems = new ArrayList<>();

    @Override
    public View onCreateView(String name, Context context, AttributeSet attrs) {
        boolean isSkinEnable = attrs.getAttributeBooleanValue(SkinConfig.NAMESPACE, SkinConfig.ATTR_SKIN_ENABLE, false);
        if (!isSkinEnable) {
            return null;
        }

        View view = createView(context, name, attrs);
        if (view == null) {
            return null;
        }
        parseSkinAttr(context, attrs, view);
        return view;
    }

    /**
     * Invoke low-level function for instantiating a view by name. This attempts to
     * instantiate a view class of the given <var>name</var> found in this
     * LayoutInflater's ClassLoader.
     *
     * @param context
     * @param name    The full name of the class to be instantiated.
     * @param attrs   The XML attributes supplied for this instance.
     * @return View The newly instantiated view, or null.
     */
    private View createView(Context context, String name, AttributeSet attrs) {
        View view = null;
        try {
            if (-1 == name.indexOf('.')) {
                if ("View".equals(name)) {
                    view = LayoutInflater.from(context).createView(name, "android.view.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.widget.", attrs);
                }
                if (view == null) {
                    view = LayoutInflater.from(context).createView(name, "android.webkit.", attrs);
                }
            } else {
                view = LayoutInflater.from(context).createView(name, null, attrs);
            }

            Log.d("createView", "about to create " + name);

        } catch (Exception e) {
            Log.e("createView", "error while create 【" + name + "】 : " + e.getMessage());
            view = null;
        }
        return view;
    }

    /**
     * Collect skin able tag such as background , textColor and so on
     *
     * @param context
     * @param attrs
     * @param view
     */
    private void parseSkinAttr(Context context, AttributeSet attrs, View view) {
        List<SkinAttr> viewAttrs = new ArrayList<>();
        for (int i = 0; i < attrs.getAttributeCount(); i++) {
            String attrName = attrs.getAttributeName(i);
            String attrValue = attrs.getAttributeValue(i);

            if (!AttrFactory.isSupportedAttr(attrName)) {
                continue;
            }
            if (attrValue.startsWith("@")) {
//                try {
//                    int id = Integer.parseInt(attrValue.substring(1));
//                    String typeName = context.getResources().getResourceTypeName(id);
//                    SkinAttr mSkinAttr = AttrFactory.get(attrName, id, typeName);
//                    if (mSkinAttr != null) {
//                        viewAttrs.add(mSkinAttr);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }

        if (!EmptyUtils.isEmpty(viewAttrs)) {
            SkinItem skinItem = new SkinItem();
            skinItem.view = view;
            skinItem.attrs = viewAttrs;

            mSkinItems.add(skinItem);

            if (SkinManager.getInstance().isExternalSkin()) {
                skinItem.apply();
            }
        }
    }

    public void applySkin() {
        if (EmptyUtils.isEmpty(mSkinItems)) {
            return;
        }
        for (SkinItem si : mSkinItems) {
            if (si.view == null) {
                continue;
            }
            si.apply();
        }
    }

    public void dynamicAddSkinEnableView(Context context, View view, List<BaseAttr> pDAttrs) {
        List<SkinAttr> viewAttrs = new ArrayList<>();
        SkinItem skinItem = new SkinItem();
        skinItem.view = view;
        for (BaseAttr baseAttr : pDAttrs) {
            int id = baseAttr.attrResRef;
            String entryName = context.getResources().getResourceEntryName(id);
            String typeName = context.getResources().getResourceTypeName(id);
            SkinAttr mSkinAttr = AttrFactory.get(baseAttr);
            viewAttrs.add(mSkinAttr);
        }
        skinItem.attrs = viewAttrs;
        addSkinView(skinItem);
    }

    public void addSkinView(SkinItem item) {
        mSkinItems.add(item);
    }

    public void clean() {
        if (EmptyUtils.isEmpty(mSkinItems)) {
            return;
        }

        for (SkinItem si : mSkinItems) {
            if (si.view == null) {
                continue;
            }
            si.clean();
        }
    }
}