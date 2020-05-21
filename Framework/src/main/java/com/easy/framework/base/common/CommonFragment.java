package com.easy.framework.base.common;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.BasePopupView;

public class CommonFragment extends Fragment {
    public Context context;
    public boolean isShow;
    protected boolean isCreateView, isLazyLoaded;
    public String tag = "LifecycleFragment";
    BasePopupView loadingDialog;

    public CommonFragment() {
        super();
    }

    @ContentView
    public CommonFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShow()) {
            loadingDialog.dismiss();
        }
    }

    public synchronized void showLoading() {
        if (context == null) {
            return;
        }
        if (loadingDialog == null) {
            loadingDialog = new XPopup.Builder(context).autoDismiss(false)
                    .asLoading("正在加载中")
                    .show();
        } else if (!loadingDialog.isShow()) {
            loadingDialog.show();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        isShow = hidden;
        Log.d("LifecycleFragment", tag + "===>onHiddenChanged==>" + isShow);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isShow = isVisibleToUser;
        Log.d("LifecycleFragment", tag + "===>setUserVisibleHint==>" + isShow);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        Log.d("LifecycleFragment", tag + "===>onAttach==>" + isShow);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("LifecycleFragment", tag + "===>onCreate==>" + isShow);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isShow = getUserVisibleHint();
        Log.d("LifecycleFragment", tag + "===>onViewCreated==>" + isShow);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("LifecycleFragment", tag + "===>onStart==>" + isShow);
    }

    @Override
    public void onResume() {
        super.onResume();
        isShow = getUserVisibleHint();
        Log.d("LifecycleFragment", tag + "===>onResume==>" + isShow);
    }

    @Override
    public void onPause() {
        super.onPause();
        isShow = false;
        Log.d("LifecycleFragment", tag + "===>onPause==>" + isShow);
    }

    @Override
    public void onStop() {
        super.onStop();
        isShow = false;
        Log.d("LifecycleFragment", tag + "===>onStop==>" + isShow);
    }

    @Override
    public void onDestroyView() {
        hideLoading();
        super.onDestroyView();
        isShow = false;
        isCreateView = false;
        isLazyLoaded = false;
        Log.d("LifecycleFragment", tag + "===>onDestroyView==>" + isShow);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isShow = false;
        Log.d("LifecycleFragment", tag + "===>onDestroy==>" + isShow);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        isShow = false;
        Log.d("LifecycleFragment", tag + "===>onDetach==>" + isShow);
    }
}
