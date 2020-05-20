package com.easy.framework.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.fragment.app.Fragment;

import com.easy.framework.skin.inter.IDynamicNewView;
import com.easy.framework.skin.view_attr.BaseAttr;

import java.util.List;

public class SkinFragment extends Fragment implements IDynamicNewView {

    private IDynamicNewView mIDynamicNewView;

    public SkinFragment() {
        super();
    }

    @ContentView
    public SkinFragment(@LayoutRes int contentLayoutId) {
        super(contentLayoutId);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mIDynamicNewView = (IDynamicNewView)context;
        }catch(ClassCastException e){
            mIDynamicNewView = null;
        }
    }

    @Override
    public void dynamicAddView(View view, List<BaseAttr> pDAttrs) {
        if(mIDynamicNewView == null){
            throw new RuntimeException("IDynamicNewView should be implements !");
        }else{
            mIDynamicNewView.dynamicAddView(view, pDAttrs);
        }
    }

    public LayoutInflater onGetLayoutInflater(Bundle savedInstanceState) {
        LayoutInflater result = getActivity().getLayoutInflater();
        return result;
    }
}
