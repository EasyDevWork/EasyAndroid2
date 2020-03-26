package com.easy.demo.ui.loadimage;

import android.animation.ValueAnimator;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.easy.apt.annotation.ActivityInject;
import com.easy.demo.R;
import com.easy.demo.databinding.TestLoadImageBinding;
import com.easy.framework.base.BaseActivity;
import com.easy.loadimage.EasyLoadImage;
import com.easy.loadimage.progress.ImageLoadProgressListener;
import com.easy.loadimage.transform.BlurTransformation;
import com.easy.loadimage.transform.GrayscaleTransformation;
import com.easy.loadimage.transform.RoundedCornersTransform;
import com.easy.net.event.ActivityEvent;
import com.easy.utils.ToastUtils;

import io.reactivex.annotations.Nullable;

@ActivityInject
@Route(path = "/demo/TestLoadImageActivity", name = "图片加载")
public class TestLoadImageActivity extends BaseActivity<TestLoadImagePresenter, TestLoadImageBinding> implements TestLoadImageView<ActivityEvent> {

    public String imageUrl = "http://img2.imgtn.bdimg.com/it/u=3137891603,2800618441&fm=26&gp=0.jpg";

    @Override
    public int getLayoutId() {
        return R.layout.test_load_image;
    }

    @Override
    public void initView() {
        EasyLoadImage.placeHolderImageView = R.color.alivc_player_theme_green;
        EasyLoadImage.circlePlaceholderImageView = R.color.alivc_player_theme_green;


        viewBind.loadingImageView.setSize(200,200);
        viewBind.loadingImageView.loadImage(imageUrl);

        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> viewBind.progressView2.setProgress((Integer) animator.getAnimatedValue()));
        animator.start();

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv1);
        viewBind.iv1.setOnClickListener(v ->
                EasyLoadImage.downloadImageToGallery(this, imageUrl)
        );
        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv2, new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
//                        Toast.makeText(getApplication(), R.string.load_failed, Toast.LENGTH_LONG).show();
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
//                        Toast.makeText(getApplication(), R.string.load_success, Toast.LENGTH_LONG).show();
                        return false;
                    }
                }
        );

        EasyLoadImage.loadBlurImage(this, imageUrl, viewBind.iv3);

        EasyLoadImage.loadCircleImage(this, imageUrl, viewBind.iv4);

        EasyLoadImage.loadRoundCornerImage(this, imageUrl, viewBind.iv5);

        EasyLoadImage.loadGrayImage(this, imageUrl, viewBind.iv6);

        EasyLoadImage.loadResizeXYImage(this, imageUrl, 800, 200, viewBind.iv7);
        viewBind.iv7.setOnClickListener(view -> ToastUtils.showShort("点击了"));

        EasyLoadImage.loadImageWithTransformation(this, imageUrl, viewBind.iv8, new GrayscaleTransformation(), new RoundedCornersTransform(50, 0));

        EasyLoadImage.loadCircleWithBorderImage(this, imageUrl, viewBind.iv9);

        EasyLoadImage.loadImageWithTransformation(this, imageUrl, viewBind.iv10, new BlurTransformation(this, 20), new GrayscaleTransformation(), new CircleCrop());

        EasyLoadImage.loadImage(this, R.drawable.test_imag, viewBind.iv11);

        EasyLoadImage.loadImage(this, "", viewBind.iv12);

        EasyLoadImage.loadBorderImage(this, imageUrl, viewBind.iv13);
    }
}
