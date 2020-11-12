package com.easy.demo.ui.imgEdit;

import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestImgEditBinding;
import com.easy.demo.ui.empty.EmptyPresenter;
import com.easy.demo.ui.empty.EmptyView;
import com.easy.framework.base.BaseActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

@ActivityInject
@Route(path = "/demo/TestImgEditActivity", name = "图片处理")
public class TestImgEditActivity extends BaseActivity<EmptyPresenter, TestImgEditBinding> implements EmptyView {

    List<Integer> images = new ArrayList<>();

    @Override
    public int getLayoutId() {
        return R.layout.test_img_edit;
    }

    @Override
    public void initView() {

        images.add(R.drawable.tou);
        images.add(R.drawable.rabit);
        images.add(R.drawable.wenzi2);

        Observable.fromIterable(images).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(integer -> BitmapFactory.decodeResource(getResources(), integer))
                .as(getAutoDispose())
                .subscribe(bitmap -> viewBind.stickerView.addBitImage(bitmap),
                        throwable -> Log.d("TestImgEditActivity", throwable.getMessage()));

    }

    public void crop(View view) {
        ARouter.getInstance().build("/demo/TestCropActivity").navigation();
    }
}
