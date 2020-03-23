package com.easy.demo.ui.recycleview;

import com.easy.demo.bean.TestDo;
import com.easy.framework.base.BasePresenter;
import com.easy.net.beans.Response;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TestRecycleViewPresenter extends BasePresenter<TestRecycleView> {

    int size = 10;//一次加载条数

    @Inject
    public TestRecycleViewPresenter() {

    }

    public void loadData(int page) {
        List<TestDo> testDos = new ArrayList<>();
        for (int i = page * size; i < (page + 1) * size; i++) {
            TestDo testDo = new TestDo();
            if (i % 2 == 0) {
                testDo.setType(0);
                testDo.setTitle("title_" + i);
            }else{
                testDo.setType(1);
                testDo.setTitle("title_" + (i-1));
            }
            testDo.setName("name_" + i);
            testDo.setImage("http://img3.imgtn.bdimg.com/it/u=3506060478,2817860815&fm=26&gp=0.jpg");
            testDos.add(testDo);
        }
        if (page == 5) {
            loadDataError(page);
            return;
        }
        //success
        Response respond = new Response();
        respond.setResultObj(testDos);
        respond.setCode(Response.SUCCESS_STATE);
        mvpView.dataCallback(respond);
    }

    public void loadDataError(int page) {
        //error
        Response respond = new Response();
        respond.setMsg("null is null");
        respond.setResultObj(null);
        respond.setCode(Response.ERROR_STATE);
        mvpView.dataCallback(respond);
    }
}