package com.easy.loadimage.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.easy.loadimage.ImageConfig;
import com.easy.loadimage.R;
import com.easy.loadimage.progress.ImageLoadCircleProgressView;

public class LoadingImageView extends RelativeLayout {
    ImageView imageView;
    ImageLoadCircleProgressView progressView;

    public LoadingImageView(Context context) {
        super(context);
        init();
    }

    public LoadingImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public LoadingImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        LayoutInflater.from(getContext()).inflate(R.layout.loading_image_view, this);
        imageView = findViewById(R.id.imageContain);
        progressView = findViewById(R.id.progressView);
    }

//    public void setSize(int width, int height) {
//        RelativeLayout.LayoutParams layoutParams = (LayoutParams) imageView.getLayoutParams();
//        layoutParams.height = height;
//        layoutParams.width = width;
//        imageView.setLayoutParams(layoutParams);
//    }

    public void loadImage(int width, int height,String url) {
        ImageConfig.create(getContext())
                .resizeX(width,height)
                .url(url)
                .imageView(imageView)
                .progressListener((isComplete, percentage, bytesRead, totalBytes) -> {
                    progressView.setVisibility(View.VISIBLE);
                    progressView.setProgress(percentage);
                })
                .requestListener(new RequestListener() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target target, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Object resource, Object model, Target target, DataSource dataSource, boolean isFirstResource) {
                        progressView.setVisibility(View.GONE);
                        return false;
                    }
                }).end();
    }
}
