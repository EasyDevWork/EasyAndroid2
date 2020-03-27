package com.easy.demo.ui.loadimage;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

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
import com.easy.loadimage.transform.BlurTransformation;
import com.easy.loadimage.transform.ColorFilterTransformation;
import com.easy.loadimage.transform.ContrastFilterTransformation;
import com.easy.loadimage.transform.CropSquareTransformation;
import com.easy.loadimage.transform.CropTransformation;
import com.easy.loadimage.transform.GrayscaleTransformation;
import com.easy.loadimage.transform.InvertFilterTransformation;
import com.easy.loadimage.transform.KuwaharaFilterTransformation;
import com.easy.loadimage.transform.MaskTransformation;
import com.easy.loadimage.transform.PixelationFilterTransformation;
import com.easy.loadimage.transform.RoundedCornersTransform;
import com.easy.loadimage.transform.SepiaFilterTransformation;
import com.easy.loadimage.transform.SketchFilterTransformation;
import com.easy.loadimage.transform.SwirlFilterTransformation;
import com.easy.loadimage.transform.ToonFilterTransformation;
import com.easy.loadimage.transform.VignetteFilterTransformation;
import com.easy.net.event.ActivityEvent;
import com.easy.utils.DimensUtils;
import com.easy.utils.ToastUtils;

import java.io.File;

import jp.wasabeef.glide.transformations.gpu.BrightnessFilterTransformation;
import jp.wasabeef.glide.transformations.gpu.GPUFilterTransformation;

import static com.easy.loadimage.transform.RoundedCornersTransform.CornerType.TOP;

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

        int width = DimensUtils.dp2px(this, 200);
        viewBind.loadingImageView.loadImage(width, width, imageUrl);

        ValueAnimator animator = ValueAnimator.ofInt(0, 100);
        animator.setDuration(2000);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(animation -> viewBind.progressView2.setProgress((Integer) animator.getAnimatedValue()));
        animator.start();

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv1);
        viewBind.iv1.setOnClickListener(v ->
                presenter.requestPermission(getRxPermissions())
        );

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv2, new RoundedCornersTransform(40, 0, TOP));

        EasyLoadImage.loadBlurImage(this, imageUrl, viewBind.iv3, 10);

        EasyLoadImage.loadCircleImage(this, imageUrl, viewBind.iv4);

        EasyLoadImage.loadRoundCornerImage(this, imageUrl, viewBind.iv5, 40, 0);

        EasyLoadImage.loadGrayImage(this, imageUrl, viewBind.iv6);

        EasyLoadImage.loadResizeXYImage(this, imageUrl, 800, 200, viewBind.iv7);
        viewBind.iv7.setOnClickListener(view -> ToastUtils.showShort("点击了"));

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv8, (isComplete, percentage, bytesRead, totalBytes) -> {

        }, new RequestListener() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        });

        EasyLoadImage.loadCircleWithBorderImage(this, imageUrl, viewBind.iv9, 5, Color.parseColor("#ACACAC"));

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv10, new BlurTransformation(20), new GrayscaleTransformation(), new CircleCrop());

        EasyLoadImage.loadImage(this, R.drawable.test_imag, viewBind.iv11);

        EasyLoadImage.loadImage(this, "", viewBind.iv12);

        EasyLoadImage.loadBorderImage(this, imageUrl, viewBind.iv13, 5, Color.parseColor("#ACACAC"));

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv14, new ColorFilterTransformation(0x7900CCCC));

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv15, new ToonFilterTransformation(0.2F, 10F));

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv16, new CropSquareTransformation());

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv17, new CropTransformation(200, 200, CropTransformation.CropType.TOP));

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv18, new MaskTransformation(R.drawable.alivc_screen_unlock));

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv19, new BrightnessFilterTransformation(0.5f));

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv20, new VignetteFilterTransformation());

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv21, new ContrastFilterTransformation(2.0f));

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv22, new InvertFilterTransformation());

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv23, new KuwaharaFilterTransformation());

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv24, new PixelationFilterTransformation());

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv25, new SepiaFilterTransformation());

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv26, new SketchFilterTransformation());

        EasyLoadImage.loadImage(this, imageUrl, viewBind.iv27, new SwirlFilterTransformation());

    }

    @Override
    public void permissionCallback(Boolean granted, Object o) {
        if (granted) {
            presenter.downloadImage(imageUrl);
        } else {
            ToastUtils.showShort("权限不允许");
        }
    }

    @Override
    public void downloadCallback(File file, Throwable e) {
        if (e != null) {
            ToastUtils.showShort(e.getMessage());
        } else {
            ToastUtils.showShort(R.string.easy_glide_save_succss);
        }
    }
}
