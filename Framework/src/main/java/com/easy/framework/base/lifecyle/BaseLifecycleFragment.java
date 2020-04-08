package com.easy.framework.base.lifecyle;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BaseLifecycleFragment extends Fragment {

    public boolean isShow;
    protected boolean isCreateView, isLazyLoaded;
    public String tag = "LifecycleFragment";

    public BaseLifecycleFragment() {
        super();
    }

    @ContentView
    public BaseLifecycleFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
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
