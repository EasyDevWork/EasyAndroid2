package com.easy.demo.ui.recycleview;

import com.easy.common.base.CommonView;
import com.easy.demo.bean.TestDo;
import com.easy.net.beans.Response;
import com.easy.net.rxlifecycle.LifecycleEvent;

import java.util.List;

public interface TestRecycleView<E extends LifecycleEvent> extends CommonView<E> {
    void dataCallback(Response<List<TestDo>> respond);
}
