package com.easy.demo.ui.lottie;

import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestLottieBinding;
import com.easy.framework.base.BaseActivity;

@ActivityInject
@Route(path = "/demo/TestLottieActivity", name = "Lottie动画")
public class TestLottieActivity extends BaseActivity<TestLottiePresenter, TestLottieBinding> implements TestLottieView {

    @Override
    public int getLayoutId() {
        return R.layout.test_lottie;
    }

    @Override
    public void initView() {
//        viewBind.lottie.setAnimationFromUrl("https://assets2.lottiefiles.com/packages/lf20_bGW6Oe.json");

//        viewBind.lottie2.setImageAssetDelegate(asset -> {
//            Bitmap bitmap = BitmapFactory.decodeFile("/sdcard/img_0.jpg");
//            Log.d("lottie2", bitmap == null ? "null" : "!null");
//            return bitmap;
//        });

        viewBind.lottie2.setImageAssetsFolder("images/");
        viewBind.lottie2.setAnimation("lottie_ceshi.json");
    }

    public void testLottieAnimation(View view) {
        viewBind.lottie.playAnimation();
        viewBind.lottie2.playAnimation();
    }

}
