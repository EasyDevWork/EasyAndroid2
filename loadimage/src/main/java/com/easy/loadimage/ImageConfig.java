package com.easy.loadimage;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory;
import com.easy.loadimage.progress.EasyGlideApp;
import com.easy.loadimage.progress.GlideImageViewTarget;
import com.easy.loadimage.progress.GlideRequest;
import com.easy.loadimage.progress.GlideRequests;
import com.easy.loadimage.progress.ImageLoadProgressListener;
import com.easy.loadimage.progress.ProgressManager;
import com.easy.loadimage.transform.BlurTransformation;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageConfig {
    private Context context;
    private FragmentActivity activity;
    private Fragment fragment;

    private String url;
    private int drawableId;
    private ImageView imageView;
    private int placeholder;
    private int errorPic;

    /**
     * 0 对应DiskCacheStrategy.all
     * 1 对应DiskCacheStrategy.NONE
     * 2 对应DiskCacheStrategy.SOURCE
     * 3 对应DiskCacheStrategy.RESULT
     */
    private int cacheStrategy;
    private int fallback;
    private BitmapTransformation[] transformation;
    private Drawable placeholderDrawable;
    private int resizeX;
    private boolean isCropCenter;
    private boolean isCropCircle;
    private boolean isFitCenter;
    private DecodeFormat formatType;
    private int resizeY;
    private int imageRadius;
    private int blurValue;
    private boolean isCrossFade;
    private ImageLoadProgressListener onProgressListener;
    private RequestListener requestListener;

    protected ImageConfig(Context context, FragmentActivity activity, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        this.activity = activity;
    }

    public GlideRequests getGlideRequests() {
        if (activity != null) {
            return EasyGlideApp.with(activity);
        }
        if (fragment != null) {
            return EasyGlideApp.with(fragment);
        }
        return EasyGlideApp.with(context);
    }

    public static ImageConfig create(Context context) {
        return new ImageConfig(context, null, null);
    }

    public static ImageConfig create(FragmentActivity activity) {
        return new ImageConfig(null, activity, null);
    }

    public static ImageConfig create(Fragment fragment) {
        return new ImageConfig(null, null, fragment);
    }


    public ImageConfig url(String val) {
        url = val;
        return this;
    }

    public ImageConfig drawableId(int val) {
        drawableId = val;
        return this;
    }

    public ImageConfig imageView(ImageView val) {
        imageView = val;
        return this;
    }

    public ImageConfig placeholder(int val) {
        placeholder = val;
        return this;
    }

    public ImageConfig errorPic(int val) {
        errorPic = val;
        return this;
    }

    public ImageConfig cacheStrategy(int val) {
        cacheStrategy = val;
        return this;
    }

    public ImageConfig fallback(int val) {
        fallback = val;
        return this;
    }

    public ImageConfig transformation(BitmapTransformation... val) {
        transformation = val;
        return this;
    }

    public ImageConfig placeholderDrawable(Drawable val) {
        placeholderDrawable = val;
        return this;
    }

    public ImageConfig resizeX(int x, int y) {
        resizeX = x;
        resizeY = y;
        return this;
    }

    public ImageConfig isCropCenter(boolean val) {
        isCropCenter = val;
        return this;
    }

    public ImageConfig isCropCircle(boolean val) {
        isCropCircle = val;
        return this;
    }

    public ImageConfig isFitCenter(boolean val) {
        isFitCenter = val;
        return this;
    }

    public ImageConfig formatType(DecodeFormat val) {
        formatType = val;
        return this;
    }

    public ImageConfig imageRadius(int val) {
        imageRadius = val;
        return this;
    }

    public ImageConfig blurValue(int val) {
        blurValue = val;
        return this;
    }

    public ImageConfig isCrossFade(boolean val) {
        isCrossFade = val;
        return this;
    }

    public ImageConfig progressListener(ImageLoadProgressListener val) {
        onProgressListener = val;
        return this;
    }

    public ImageConfig requestListener(RequestListener val) {
        requestListener = val;
        return this;
    }

    public void end() {
        if (context == null && activity == null && fragment == null) {
            return;
        }
        GlideRequests requests = getGlideRequests();
        GlideRequest<Drawable> glideRequest;
        if (drawableId != 0) {
            glideRequest = requests.load(drawableId);
        } else {
            glideRequest = requests.load(url);
        }
        //缓存策略
        switch (cacheStrategy) {
            case 1:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.NONE);
                break;
            case 2:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.RESOURCE);
                break;
            case 3:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.DATA);
                break;
            case 4:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.AUTOMATIC);
                break;
            default:
                glideRequest.diskCacheStrategy(DiskCacheStrategy.ALL);
                break;
        }

        if (isCrossFade) {
            DrawableCrossFadeFactory factory = new DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build();
            glideRequest.transition(withCrossFade(factory));
        }
        if (imageRadius != 0) {
            glideRequest.transform(new RoundedCorners(imageRadius));
        }

        if (blurValue != 0) {
            glideRequest.transform(new BlurTransformation(context, blurValue));
        }
        //glide用它来改变图形的形状
        if (transformation != null) {
            glideRequest.transform(transformation);
        }

        if (placeholderDrawable != null) {
            glideRequest.placeholder(placeholderDrawable);
        }

        //设置占位符
        if (placeholder != 0) {
            glideRequest.placeholder(placeholder);
        }
        //设置错误的图片
        if (errorPic != 0) {
            glideRequest.error(errorPic);
        }
        //设置请求 url 为空图片
        if (fallback != 0) {
            glideRequest.fallback(fallback);
        }

        if (resizeX != 0 && resizeY != 0) {
            glideRequest.override(resizeX, resizeY);
        }

        if (isCropCenter) {
            glideRequest.centerCrop();
        }

        if (isCropCircle) {
            glideRequest.circleCrop();
        }

        if (formatType != null) {
            glideRequest.format(formatType);
        }

        if (isFitCenter) {
            glideRequest.fitCenter();
        }

        if (requestListener != null) {
            glideRequest.addListener(requestListener);
        }

        if (onProgressListener != null) {
            ProgressManager.addListener(url, onProgressListener);
        }

        glideRequest.into(new GlideImageViewTarget(imageView, url));
    }
}
