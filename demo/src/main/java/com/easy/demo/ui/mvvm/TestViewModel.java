package com.easy.demo.ui.mvvm;

import android.content.Context;
import android.view.View;

import com.easy.demo.bean.TestMvvm;
import com.easy.demo.bean.TestMvvm2;
import com.easy.demo.databinding.TestMvvmBinding;
import com.easy.utils.DimensUtils;

public class TestViewModel {
    TestMvvm testMvvm;
    TestMvvm2 testM2;
    int i = 12, j = 0;
    Context context;

    public TestViewModel(TestMvvmBinding viewDataBinding) {
        testMvvm = new TestMvvm();
        viewDataBinding.setTestMvvm(testMvvm);

        testM2 = new TestMvvm2();
        viewDataBinding.setTestM2(testM2);
        viewDataBinding.setTestVm(this);

        context = viewDataBinding.getRoot().getContext();
    }

    /**
     * 直接引用--参数必须是引用要用的参数
     *
     * @param view
     */
    public void clickChangeNameBtn(View view) {
        i++;
        testM2.setName("testM2_" + i);
        testMvvm.setName("testMvvm_" + i);
        testM2.setSize(DimensUtils.dp2px(context, 15));
        testMvvm.setSize(DimensUtils.dp2px(context, 15));
    }

    /**
     * 绑定监听-参数不受影响
     */
    public void clickChangeSizeBtn() {
        j++;
        testM2.setName("testM2");
        testMvvm.setName("testMvvm");
        testM2.setSize(DimensUtils.dp2px(context, 20 + j));
        testMvvm.setSize(DimensUtils.dp2px(context, 40 - j));
    }

    public String imageUrl() {
        return "http://img2.imgtn.bdimg.com/it/u=3137891603,2800618441&fm=26&gp=0.jpg";
    }
}
