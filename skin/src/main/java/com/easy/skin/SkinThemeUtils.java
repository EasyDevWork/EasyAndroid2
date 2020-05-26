package com.easy.skin;

import android.content.Context;
import android.content.res.TypedArray;

public class SkinThemeUtils {
    public static int[] getThemeResId(Context context, int[] attrs) {
        int[] resIds = new int[]{attrs.length};
        TypedArray typedArray = context.obtainStyledAttributes(attrs);
        for (int i = 0; i < typedArray.length(); i++) {
            int resourceId = typedArray.getResourceId(i, 0);
            resIds[i] = resourceId;
        }
        typedArray.recycle();
        return resIds;
    }
}
