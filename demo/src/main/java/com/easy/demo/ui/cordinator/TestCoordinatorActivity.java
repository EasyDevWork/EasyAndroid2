package com.easy.demo.ui.cordinator;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.bean.TestDo;
import com.easy.demo.databinding.TestCoordinatorBinding;
import com.easy.demo.ui.recycleview.TestRecycleViewMultipleAdapter;
import com.easy.framework.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

@ActivityInject
@Route(path = "/demo/TestCoordinatorActivity", name = "空页面")
public class TestCoordinatorActivity extends BaseActivity<TestCoordinatorPresenter, TestCoordinatorBinding> implements TestCoordinatorView {
    TestRecycleViewMultipleAdapter adapter;

    @Override
    public int getLayoutId() {
        return R.layout.test_coordinator;
    }


    @Override
    public void initView() {

        adapter = new TestRecycleViewMultipleAdapter(new ArrayList<>(),this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        viewBind.recycleView.setLayoutManager(layoutManager);
        viewBind.recycleView.setAdapter(adapter);

        List<TestDo> testDos = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            TestDo testDo = new TestDo();
            testDo.setTitle("title_" + i);
            testDo.setName("name_" + i);
            testDo.setImage("http://img3.imgtn.bdimg.com/it/u=3506060478,2817860815&fm=26&gp=0.jpg");
            testDos.add(testDo);
        }
        adapter.setNewData(testDos);
    }
}
