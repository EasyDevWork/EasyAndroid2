package com.easy.demo.ui.loadimage;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

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
import com.easy.loadimage.transform.GrayscaleTransformation;
import com.easy.net.event.ActivityEvent;
import com.easy.utils.DimensUtils;
import com.easy.utils.ToastUtils;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

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

        EasyLoadImage.loadImage(this, "http://img5.imgtn.bdimg.com/it/u=1706446022,2591052907&fm=26&gp=0.jpg", viewBind.iv1);
        viewBind.iv1.setOnClickListener(v ->
                presenter.requestPermission(getRxPermissions())
        );

        EasyLoadImage.loadBlurImage(this, imageUrl, viewBind.iv3, 10);

        EasyLoadImage.loadCircleImage(this, imageUrl, viewBind.iv4);

        EasyLoadImage.loadRoundCornerImage(this, imageUrl, viewBind.iv5, 40, 0);

        EasyLoadImage.loadGrayImage(this, imageUrl, viewBind.iv6);

        EasyLoadImage.loadResizeXYImage(this, imageUrl, 800, 200, viewBind.iv7);
        viewBind.iv7.setOnClickListener(view -> ToastUtils.showShort("点击了"));

        EasyLoadImage.loadImage(this, "http://img2.imgtn.bdimg.com/it/u=927695547,1051830077&fm=26&gp=0.jpg", viewBind.iv8, (isComplete, percentage, bytesRead, totalBytes) -> {

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

        EasyLoadImage.loadImage(this)
                .url(imageUrl)
                .imageView(viewBind.iv10)
                .placeholder(EasyLoadImage.placeImage)
                .errorPic(EasyLoadImage.errorImage)
                .transformation(new BlurTransformation(this, 20), new GrayscaleTransformation(), new CircleCrop())
                .end();

        EasyLoadImage.loadImage(this, R.drawable.test_imag, viewBind.iv11);

        EasyLoadImage.loadImage(this, "", viewBind.iv12);

        EasyLoadImage.loadBorderImage(this, imageUrl, viewBind.iv13, 5, Color.parseColor("#ACACAC"));
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
