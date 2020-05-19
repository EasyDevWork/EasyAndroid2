package com.easy.demo.ui.mmkv;

import android.content.SharedPreferences;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestMmkvBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;
import com.tencent.mmkv.MMKV;

import java.util.Arrays;

@ActivityInject
@Route(path = "/demo/TestMmmkvActivity", name = "mmkv")
public class TestMmmkvActivity extends BaseActivity<EmptyPresenter, TestMmkvBinding> implements EmptyView {

    //使用方法参考：
    // https://github.com/Tencent/MMKV/wiki/android_tutorial

    @Override
    public int getLayoutId() {
        return R.layout.test_mmkv;
    }

    @Override
    public void initView() {
        StringBuilder builder = new StringBuilder();
        builder.append("1.写入速度是SharedPreferences 200倍").append("\n");
        builder.append("2.按模块来区分业务").append("\n");
        builder.append("3.支持跨进程").append("\n");
        builder.append("4.SharedPreferences数据直接转入MMKV").append("\n");
        builder.append("5.支持数据加密").append("\n");
        viewBind.tvDescribe.setText(builder.toString());

        MMKV kv = MMKV.defaultMMKV();
        kv.encode("bool", true);
        System.out.println("bool: " + kv.decodeBool("bool"));

        kv.encode("long", Long.MAX_VALUE);
        System.out.println("long: " + kv.decodeLong("long"));

        byte[] bytes = {'m', 'm', 'k', 'v'};
        kv.encode("bytes", bytes);
        System.out.println("bytes: " + new String(kv.decodeBytes("bytes")));

        kv.removeValueForKey("bool");
        System.out.println("bool: " + kv.decodeBool("bool"));

        kv.removeValuesForKeys(new String[]{"int", "long"});
        System.out.println("allKeys: " + Arrays.toString(kv.allKeys()));

        boolean hasBool = kv.containsKey("bool");
        System.out.println("hasBool: " + hasBool);

        kv.encode("asset", 1);
        System.out.println("asset: " + kv.decodeInt("asset"));

        //按模块来区分
        MMKV mmkvModel = MMKV.mmkvWithID("MyID");
        mmkvModel.encode("asset", 2);
        System.out.println("asset: " + mmkvModel.decodeInt("asset"));

        MMKV mmkv = MMKV.mmkvWithID("InterProcessKV", MMKV.MULTI_PROCESS_MODE);
        mmkv.encode("buyNum", 1);


        SharedPreferences old_man = getSharedPreferences("myData", MODE_PRIVATE);
        old_man.edit().putInt("a", 7).apply();
        kv.importFromSharedPreferences(old_man);
        old_man.edit().clear().apply();
        System.out.println("a: " + kv.decodeInt("a"));
    }
}
