package com.easy.imagezoom;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.DrawableImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.easy.apt.annotation.FragmentInject;
import com.easy.framework.base.BaseFragment;
import com.easy.imagezoom.databinding.ImagePreviewBinding;
import com.easy.imagezoom.widget.PhotoViewAttacher;
import com.easy.loadimage.ImageConfig;
import com.easy.net.event.FragmentEvent;

@FragmentInject
public class ImageZoomDetailFragment extends BaseFragment<ImageZoomDetailPresenter, ImagePreviewBinding> implements ImageZoomDetailView<FragmentEvent> {
    private String imageUrl;

    private PhotoViewAttacher photoViewAttacher;
    boolean isLoadOk;

    public static ImageZoomDetailFragment newInstance(String imageUrl) {
        ImageZoomDetailFragment f = new ImageZoomDetailFragment();
        Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);
        return f;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        imageUrl = getArguments() != null ? getArguments().getString("url") : null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.image_preview;
    }

    @Override
    public void initView(View view) {
        photoViewAttacher = new PhotoViewAttacher(viewBind.preImageView);
        //单击退出页面
        photoViewAttacher.setOnPhotoTapListener((arg0, arg1, arg2) -> {
            Activity activity = getActivity();
            if (activity != null) {
                activity.finish();
            }
        });
        ImageConfig.create(getContext())
                .url(imageUrl)
                .progressListener((isComplete, percentage, bytesRead, totalBytes) -> {
                    Log.d("loadImage", "progressListener");
                    if (!isLoadOk) {
                        viewBind.progressView.setVisibility(View.VISIBLE);
                        viewBind.progressView.setProgress(percentage);
                    } else {
                        viewBind.progressView.setVisibility(View.GONE);
                    }
                })
                .target(new DrawableImageViewTarget(viewBind.preImageView) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        viewBind.preImageView.setImageDrawable(resource);
                        photoViewAttacher.update();
                    }
                })
                .requestListener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        viewBind.progressView.setVisibility(View.GONE);
                        isLoadOk = true;
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        viewBind.progressView.setVisibility(View.GONE);
                        isLoadOk = true;
                        Log.d("loadImage", "onResourceReady");
                        return false;
                    }
                }).end();
    }
}
