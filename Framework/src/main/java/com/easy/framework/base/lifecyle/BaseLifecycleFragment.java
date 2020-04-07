package com.easy.framework.base.lifecyle;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.CheckResult;
import androidx.annotation.ContentView;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.LifecycleTransformer;
import com.trello.rxlifecycle3.RxLifecycle;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.android.RxLifecycleAndroid;

import io.reactivex.Observable;
import io.reactivex.subjects.BehaviorSubject;

public class BaseLifecycleFragment extends Fragment implements LifecycleProvider<FragmentEvent> {

    public boolean isShow;
    protected boolean isCreateView, isLazyLoaded;
    public String tag = "LifecycleFragment";
    private final BehaviorSubject<FragmentEvent> lifecycleSubject = BehaviorSubject.create();

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
        lifecycleSubject.onNext(FragmentEvent.ATTACH);
        Log.d("LifecycleFragment", tag + "===>onAttach==>" + isShow);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE);
        Log.d("LifecycleFragment", tag + "===>onCreate==>" + isShow);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lifecycleSubject.onNext(FragmentEvent.CREATE_VIEW);
        isShow = getUserVisibleHint();
        Log.d("LifecycleFragment", tag + "===>onViewCreated==>" + isShow);
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleSubject.onNext(FragmentEvent.START);
        Log.d("LifecycleFragment", tag + "===>onStart==>" + isShow);
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleSubject.onNext(FragmentEvent.RESUME);
        isShow = getUserVisibleHint();
        Log.d("LifecycleFragment", tag + "===>onResume==>" + isShow);
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecycleSubject.onNext(FragmentEvent.PAUSE);
        isShow = false;
        Log.d("LifecycleFragment", tag + "===>onPause==>" + isShow);
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleSubject.onNext(FragmentEvent.STOP);
        isShow = false;
        Log.d("LifecycleFragment", tag + "===>onStop==>" + isShow);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        lifecycleSubject.onNext(FragmentEvent.DESTROY_VIEW);
        isShow = false;
        isCreateView = false;
        isLazyLoaded = false;
        Log.d("LifecycleFragment", tag + "===>onDestroyView==>" + isShow);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleSubject.onNext(FragmentEvent.DESTROY);
        isShow = false;
        Log.d("LifecycleFragment", tag + "===>onDestroy==>" + isShow);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        lifecycleSubject.onNext(FragmentEvent.DETACH);
        isShow = false;
        Log.d("LifecycleFragment", tag + "===>onDetach==>" + isShow);
    }

    public LifecycleProvider getRxLifecycle() {
        return this;
    }

    @Override
    @NonNull
    @CheckResult
    public final Observable<FragmentEvent> lifecycle() {
        return lifecycleSubject.hide();
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindUntilEvent(@NonNull FragmentEvent event) {
        return RxLifecycle.bindUntilEvent(lifecycleSubject, event);
    }

    @Override
    @NonNull
    @CheckResult
    public final <T> LifecycleTransformer<T> bindToLifecycle() {
        return RxLifecycleAndroid.bindFragment(lifecycleSubject);
    }
}
