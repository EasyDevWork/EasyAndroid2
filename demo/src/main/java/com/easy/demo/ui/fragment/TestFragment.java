package com.easy.demo.ui.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.easy.apt.annotation.FragmentInject;
import com.easy.common.base.CommonFragment;
import com.easy.demo.R;
import com.easy.demo.databinding.TestFragmentBinding;
import com.easy.net.rxlifecycle.FragmentEvent;

@FragmentInject
public class TestFragment extends CommonFragment<TestFragmentPresenter, TestFragmentBinding> implements TestFragmentView<FragmentEvent> {

    String type;

    @Override
    public int getLayoutId() {
        return R.layout.test_fragment;
    }

    @Override
    public void initView(View view) {
        viewBind.tvName.setText(type + "====>");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getString("type");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TestFragment", type + "===>onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        Log.d("TestFragment", type + "==>onHiddenChanged==>" + hidden);
        super.onHiddenChanged(hidden);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        Log.d("TestFragment", type + "==>setUserVisibleHint==>" + isVisibleToUser);
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onResume() {
        Log.d("TestFragment", type + "==>onResume");
        super.onResume();
    }

    @Override
    public void onPause() {
        Log.d("TestFragment", type + "==>onPause");
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        Log.d("TestFragment", type + "==>onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onStop() {
        Log.d("TestFragment", type + "==>onStop");
        super.onStop();
    }

    @Override
    public void onDestroy() {
        Log.d("TestFragment", type + "==>onDestroy");
        super.onDestroy();
    }
}
