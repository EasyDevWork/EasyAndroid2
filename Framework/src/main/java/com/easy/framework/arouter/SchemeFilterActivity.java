package com.easy.framework.arouter;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.callback.NavCallback;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.utils.ToastUtils;

/**
 * 网页跳转
 * <a href="arouter://m.aliyun.com/test/activity1?name=tpnet&age=21&sex=true">arouter://m.aliyun.com/test/activity1</a>
 */
public class SchemeFilterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent().getData() == null) {
            ToastUtils.showShort("SchemeFilterActivity data==null");
            finish();
        } else {
            ARouter.getInstance().build(getIntent().getData()).navigation(this, new NavCallback() {
                @Override
                public void onArrival(Postcard postcard) {
                    finish();
                }
            });
        }
    }
}
