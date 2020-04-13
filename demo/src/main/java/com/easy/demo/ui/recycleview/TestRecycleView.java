package com.easy.demo.ui.recycleview;

import android.view.View;

import com.easy.demo.bean.TestDo;
import com.easy.framework.base.BaseView;
import com.easy.net.beans.Response;

import java.util.List;

public interface TestRecycleView extends BaseView {
    void dataCallback(Response<List<TestDo>> respond);

    View getView();
}
