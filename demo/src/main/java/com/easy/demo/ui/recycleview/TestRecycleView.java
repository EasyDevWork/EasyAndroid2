package com.easy.demo.ui.recycleview;

import com.easy.demo.bean.TestDo;
import com.easy.framework.base.BaseView;
import com.easy.net.beans.Response;
import com.easy.net.event.LifecycleEvent;

import java.util.List;

public interface TestRecycleView<E extends LifecycleEvent> extends BaseView<E> {
    void dataCallback(Response<List<TestDo>> respond);
}
