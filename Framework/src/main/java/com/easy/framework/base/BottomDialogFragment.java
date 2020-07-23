package com.easy.framework.base;

import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import androidx.fragment.app.FragmentManager;

public abstract class BottomDialogFragment extends BaseDialogFragment {
    private static final float DEFAULT_DIM = 0.2f;
    private static final String TAG = "base_bottom_dialog";

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.BOTTOM;
        params.dimAmount = DEFAULT_DIM;
        window.setAttributes(params);
        //设置背景半透明
//        DisplayMetrics dm = new DisplayMetrics();
//        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
//        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    public String getFragmentTag() {
        return TAG;
    }

    public void show(FragmentManager fragmentManager) {
        try {
            fragmentManager.beginTransaction().remove(this).commit();
            show(fragmentManager, getFragmentTag());
        } catch (Exception e) {
            //同一实例使用不同的tag会异常,这里捕获一下
            e.printStackTrace();
        }
    }
}
