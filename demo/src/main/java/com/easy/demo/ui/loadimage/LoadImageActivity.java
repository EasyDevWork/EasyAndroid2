package com.easy.demo.ui.loadimage;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.easy.apt.annotation.ActivityInject;
import com.easy.common.base.CommonActivity;
import com.easy.demo.R;
import com.easy.demo.databinding.TestLoadImageBinding;
import com.easy.framework.loadimage.ImageLoadFactory;
import com.easy.framework.loadimage.ImageLoaderOptions;
import com.easy.framework.loadimage.ImageLoaderRequestListener;
import com.easy.framework.loadimage.transform.BlurTransformation;
import com.easy.framework.loadimage.transform.GlideRoundedCornersTransform;
import com.easy.framework.rxlifecycle.ActivityEvent;
import com.easy.framework.utils.DimensUtils;

@ActivityInject
@Route(path = "/demo/LoadImageActivity", name = "图片加载")
public class LoadImageActivity extends CommonActivity<LoadImagePresenter, TestLoadImageBinding> implements LoadImageView<ActivityEvent> {

    public String imageUrl = "http://img2.imgtn.bdimg.com/it/u=3137891603,2800618441&fm=26&gp=0.jpg";

    @Override
    public int getLayoutId() {
        return R.layout.test_load_image;
    }

    @Override
    public void initView() {

    }

    public void loadResource(View view) {
        ImageLoadFactory.create()
                .loadImage(this, R.drawable.testimg)
                .into(viewBind.tvImage);
    }

    public void loadUrl(View view) {
        ImageLoadFactory.create()
                .loadImage(this, imageUrl)
                .into(viewBind.tvImage);
    }

    public void loadUrl2(View view) {
        int width = DimensUtils.dp2px(this, 150);
        int radius = DimensUtils.dp2px(this, 10);
        ImageLoadFactory.create()
                .loadImage(this, imageUrl, new ImageLoaderOptions.Builder()
                        .placeholder(R.drawable.default_image)
                        .error(R.drawable.default_error_image)
                        .centerCrop()
                        .override(width, width)
//                        .skipDiskCacheCache()
//                        .skipMemoryCache()
//                        .circle()
//                        .transformation(new GlideCircleTransformWithBoard(this, radius, Color.parseColor("#c83c3c")))
//                        .transformation(new BlurTransformation())
                        .transformation(new GlideRoundedCornersTransform(radius, GlideRoundedCornersTransform.CornerType.TOP_LEFT), new BlurTransformation())
                        .thumbnail(0.3f)
                        .crossFade()
//                       .override(DimensUtils.dp2px(this,50),DimensUtils.dp2px(this,50))
                        .build())
                .listener(new ImageLoaderRequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(String exception, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, boolean isFirstResource) {
                        return false;
                    }
                })
                .into(viewBind.tvImage);
    }
}
