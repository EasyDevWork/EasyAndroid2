package com.easy.loadimage;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.RawRes;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.easy.loadimage.progress.EasyGlideApp;
import com.easy.loadimage.progress.ImageLoadProgressListener;
import com.easy.loadimage.transform.BlurTransformation;
import com.easy.loadimage.transform.BorderTransformation;
import com.easy.loadimage.transform.CircleTransformWithBoard;
import com.easy.loadimage.transform.GrayscaleTransformation;
import com.easy.loadimage.transform.RoundedCornersTransform;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.Nullable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class EasyLoadImage {

    public static int placeImage = R.color.color_757575;//占位图
    public static int errorImage = R.color.color_757575;//错误图
    public static int circlePlaceImage = R.color.color_757575;//圆形占位图
    public static int circleErrorImage = R.color.color_757575;//错误图

    public static ImageConfig loadImage(Context context) {
        return ImageConfig.create(context);
    }

    public static ImageConfig loadImage(Activity activity) {
        return ImageConfig.create(activity);
    }

    public static ImageConfig loadImage(Fragment fragment) {
        return ImageConfig.create(fragment);
    }

    public static ImageConfig loadImage(Object object) {
        ImageConfig imageConfig = null;
        if (object instanceof Activity) {
            imageConfig = loadImage((Activity) object);
        } else if (object instanceof Fragment) {
            imageConfig = loadImage((Fragment) object);
        } else if (object instanceof Context) {
            imageConfig = loadImage((Context) object);
        }
        return imageConfig;
    }

    /**
     * 加载网络图片
     */
    public static void loadImage(Object object, String url, ImageView imageView) {
        ImageConfig imageConfig = loadImage(object);
        if (imageConfig != null) {
            imageConfig.url(url)
                    .isCrossFade(true)
                    .fallback(errorImage)
                    .placeholder(placeImage)
                    .errorPic(errorImage)
                    .imageView(imageView)
                    .end();
        }
    }

    /**
     * 加载本地图片
     */
    public static void loadImage(Object object, @RawRes @DrawableRes Integer drawableId, ImageView imageView) {
        ImageConfig imageConfig = loadImage(object);
        if (imageConfig != null) {
            imageConfig.drawableId(drawableId)
                    .isCrossFade(true)
                    .placeholder(placeImage)
                    .errorPic(errorImage)
                    .imageView(imageView)
                    .end();
        }
    }

    /**
     * 圆形图片
     */
    public static void loadCircleImage(Object object, String url, ImageView imageView) {
        ImageConfig imageConfig = loadImage(object);
        if (imageConfig != null) {
            imageConfig.url(url)
                    .isCropCircle(true)
                    .isCrossFade(true)
                    .placeholder(circlePlaceImage)
                    .errorPic(circleErrorImage)
                    .imageView(imageView)
                    .end();
        }
    }

    /**
     * 图片加载监
     */
    public static void loadImage(Object object, String url, ImageView imageView, ImageLoadProgressListener onProgressListener, RequestListener requestListener) {
        ImageConfig imageConfig = loadImage(object);
        if (imageConfig != null) {
            imageConfig.url(url)
                    .isCrossFade(true)
                    .imageView(imageView)
                    .placeholder(placeImage)
                    .errorPic(errorImage)
                    .progressListener(onProgressListener)
                    .requestListener(requestListener)
                    .end();
        }
    }

    /**
     * 重制图片大小
     */
    public static void loadResizeXYImage(Object object, String url, int resizeX, int resizeY, ImageView imageView) {
        ImageConfig imageConfig = loadImage(object);
        if (imageConfig != null) {
            imageConfig.url(url)
                    .isCrossFade(true)
                    .resizeX(resizeX, resizeY)
                    .placeholder(placeImage)
                    .errorPic(errorImage)
                    .imageView(imageView)
                    .end();
        }
    }

    /**
     * 图片变黑白
     */
    public static void loadGrayImage(Object object, String url, ImageView imageView) {
        ImageConfig imageConfig = loadImage(object);
        if (imageConfig != null) {
            imageConfig.url(url)
                    .isCrossFade(true)
                    .transformation(new CenterCrop(), new GrayscaleTransformation())
                    .placeholder(placeImage)
                    .errorPic(errorImage)
                    .imageView(imageView)
                    .end();
        }
    }

    private static Context getContext(Object object) {
        Context context = null;
        if (object instanceof Fragment) {
            context = ((Fragment) object).getContext();
        } else if (object instanceof Activity) {
            context = (Activity) object;
        } else if (object instanceof Context) {
            context = (Context) object;
        }
        return context;
    }

    /**
     * 高斯模糊
     */
    public static void loadBlurImage(Object object, String url, ImageView imageView, int radius) {
        ImageConfig imageConfig = loadImage(object);
        Context context = getContext(object);
        if (imageConfig != null && context != null) {
            imageConfig.url(url)
                    .isCrossFade(true)
                    .transformation(new CenterCrop(), new BlurTransformation(context, radius))
                    .placeholder(placeImage)
                    .errorPic(errorImage)
                    .imageView(imageView)
                    .end();
        }
    }

    /**
     * 距行圆角图
     */
    public static void loadRoundCornerImage(Object object, String url, ImageView imageView, int radius, int margin) {
        ImageConfig imageConfig = loadImage(object);
        if (imageConfig != null) {
            imageConfig.url(url)
                    .isCrossFade(true)
                    .transformation(new CenterCrop(), new RoundedCornersTransform(radius, margin))
                    .placeholder(placeImage)
                    .errorPic(errorImage)
                    .imageView(imageView)
                    .end();
        }
    }

    /**
     * 圆形带边框
     */
    public static void loadCircleWithBorderImage(Object object, String url, ImageView imageView, int borderWidth, @ColorInt int borderColor) {
        ImageConfig imageConfig = loadImage(object);
        Context context = getContext(object);
        if (imageConfig != null && context != null) {
            imageConfig.url(url)
                    .isCrossFade(true)
                    .transformation(new CircleTransformWithBoard(context, borderWidth, borderColor))
                    .placeholder(placeImage)
                    .errorPic(errorImage)
                    .imageView(imageView)
                    .end();
        }
    }

    public static void loadBorderImage(Object object, String url, ImageView imageView, int borderWidth, @ColorInt int borderColor) {
        ImageConfig imageConfig = loadImage(object);
        if (imageConfig != null) {
            imageConfig.url(url)
                    .isCrossFade(true)
                    .transformation(new BorderTransformation(borderWidth, borderColor))
                    .placeholder(placeImage)
                    .errorPic(errorImage)
                    .imageView(imageView)
                    .end();
        }
    }

    /**
     * 预加载
     */
    public static Target preloadImage(Context context, String url) {
        return Glide.with(context).load(url).preload();
    }

    /**
     * 清除本地缓存
     */
    public static Observable clearDiskCache(final Context context) {
        return Observable.just(0)
                .map(integer -> {
                    try {
                        Glide.get(context).clearDiskCache();
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                });
    }

    /**
     * 清除内存缓存
     */
    public static Observable clearMemory(final Context context) {
        return Observable.just(0)
                .map(integer -> {
                    try {
                        Glide.get(context).clearMemory();
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                });
    }

    /**
     * 取消图片加载
     */
    public static void clearImage(final Context context, ImageView imageView) {
        EasyGlideApp.get(context).getRequestManagerRetriever().get(context).clear(imageView);
    }


    /**
     * 下载图片，并在媒体库中显示
     */
    public static void downloadImageToGallery(final Context context, final String imgUrl) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(imgUrl);
        if (TextUtils.isEmpty(extension)) {
            extension = "png";
        }
        String finalExtension = extension;
        Disposable disposable = Observable.create((ObservableOnSubscribe<File>)
                emitter -> {
                    // Glide提供了一个download() 接口来获取缓存的图片文件，
                    // 但是前提必须要设置diskCacheStrategy方法的缓存策略为
                    // DiskCacheStrategy.ALL或者DiskCacheStrategy.SOURCE，
                    // 还有download()方法需要在子线程里进行
                    File file = Glide.with(context).download(imgUrl).submit().get();
                    String fileParentPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/easyGlide/Image";
                    File appDir = new File(fileParentPath);
                    if (!appDir.exists()) {
                        boolean mk = appDir.mkdirs();
                        Log.d("downloadImageToGallery", "mkdirs:" + mk);
                    } //获得原文件流
                    FileInputStream fis = new FileInputStream(file);
                    //保存的文件名
                    String fileName = "easyGlide_" + System.currentTimeMillis() + "." + finalExtension;
                    //目标文件
                    File targetFile = new File(appDir, fileName);
                    //输出文件流
                    FileOutputStream fos = new FileOutputStream(targetFile);
                    // 缓冲数组
                    byte[] b = new byte[1024 * 8];
                    while (fis.read(b) != -1) {
                        fos.write(b);
                    }
                    fos.flush();
                    fis.close();
                    fos.close();
                    //扫描媒体库
                    String mimeTypes = MimeTypeMap.getSingleton().getMimeTypeFromExtension(finalExtension);
                    MediaScannerConnection.scanFile(context, new String[]{targetFile.getAbsolutePath()},
                            new String[]{mimeTypes}, null);
                    emitter.onNext(targetFile);
                }).subscribeOn(Schedulers.io())
                //发送事件在io线程
                .observeOn(AndroidSchedulers.mainThread())
                //最后切换主线程提示结果
                .subscribe(file -> Toast.makeText(context, R.string.easy_glide_save_succss, Toast.LENGTH_SHORT).show(),
                        throwable -> Toast.makeText(context, R.string.easy_glide_save_failed, Toast.LENGTH_SHORT).show());
    }
}
