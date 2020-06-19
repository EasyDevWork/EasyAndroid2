package com.easy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Log;
import android.view.View;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class BitmapUtil {

    /**
     * 将图片的四角圆弧化
     *
     * @param roundPixels 弧度
     * @param half        （上/下/左/右）半部分圆角
     * @return
     */
    public static Drawable getRoundCornerImage(Context context, int resId, int roundPixels, HalfType half) {

        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resId); // 先从资源中把背景图获取出来
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Bitmap roundConcerImage = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);//创建一个和原始图片一样大小的位图
        Canvas canvas = new Canvas(roundConcerImage);//创建位图画布
        Paint paint = new Paint();//创建画笔

        Rect rect = new Rect(0, 0, width, height);//创建一个和原始图片一样大小的矩形
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);// 抗锯齿

        canvas.drawRoundRect(rectF, roundPixels, roundPixels, paint);//画一个基于前面创建的矩形大小的圆角矩形
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));//设置相交模式
        canvas.drawBitmap(bitmap, null, rect, paint);//把图片画到矩形去

        switch (half) {
            case LEFT:
                return new BitmapDrawable(context.getResources(), Bitmap.createBitmap(roundConcerImage, 0, 0, width - roundPixels, height));
            case RIGHT:
                return new BitmapDrawable(context.getResources(), Bitmap.createBitmap(roundConcerImage, width - roundPixels, 0, width - roundPixels, height));
            case TOP: // 上半部分圆角化 “- roundPixels”实际上为了保证底部没有圆角，采用截掉一部分的方式，就是截掉和弧度一样大小的长度
                return new BitmapDrawable(context.getResources(), Bitmap.createBitmap(roundConcerImage, 0, 0, width, height - roundPixels));
            case BOTTOM:
                return new BitmapDrawable(context.getResources(), Bitmap.createBitmap(roundConcerImage, 0, height - roundPixels, width, height - roundPixels));
            case ALL:
                return new BitmapDrawable(context.getResources(), roundConcerImage);
            default:
                return new BitmapDrawable(context.getResources(), roundConcerImage);
        }
    }

    /**
     * 图片圆角规则 eg. TOP：上半部分
     */
    public enum HalfType {
        LEFT, // 左上角 + 左下角
        RIGHT, // 右上角 + 右下角
        TOP, // 左上角 + 右上角
        BOTTOM, // 左下角 + 右下角
        ALL // 四角
    }


    public static File saveBitmap(Context context, byte[] datas, Point size, int top, String name) {
        File file = null;
        FileOutputStream fos = null;
        Bitmap bm;
        try {
            bm = toturn(datas, 90);
            Log.d("CameraConfiguration","转换角度后图片 width:"+bm.getWidth()+ "height:"+bm.getHeight());
//            bm = Bitmap.createBitmap(bm, 0, top, bm.getWidth(), size.y, null, false);
            String newFilePath = StringUtils.buildString(context.getFilesDir().getAbsolutePath(), "/", name, "_", System.currentTimeMillis(), ".jpg");
            file = new File(newFilePath);
            boolean isOk = file.createNewFile();
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            bm.recycle();
        } catch (Exception e) {
            Log.d("saveBitmap", e.toString());
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static String saveBitmap(Context context, Bitmap bm, String name) {
        String newFilePath = context.getFilesDir().getAbsolutePath() + "/"
                + name + ".png";
        File file = new File(newFilePath);
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            // width = PAPER_WIDTH;
        } catch (IOException e) {
        } finally {
            // if (bm != null) {
            // bm.recycle();
            // }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return newFilePath;
    }

    public static File saveBitmap(Context context, byte[] datas, String name) {
        String newFilePath = context.getFilesDir().getAbsolutePath() + "/"
                + name + ".png";
        File file = new File(newFilePath);
        FileOutputStream fos = null;
        try {
            Bitmap bm = BitmapFactory.decodeByteArray(datas, 0, datas.length);
            file.createNewFile();
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            // width = PAPER_WIDTH;
        } catch (IOException e) {
        } finally {
            // if (bm != null) {
            // bm.recycle();
            // }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    public static Data saveBitmap(Context context, byte[] datas, float percent, String name) {
        String newFilePath = context.getFilesDir().getAbsolutePath() + "/"
                + name + ".jpg";
        File file = new File(newFilePath);
        FileOutputStream fos = null;
        Bitmap bm = null;
        try {

            //竖屏拍照需要旋转90度
            Bitmap toTransform = toturn(datas, 90);
            BitmapShader bitmapShader = new BitmapShader(toTransform, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            bm = Bitmap.createBitmap(toTransform.getWidth(), (int) (toTransform.getHeight() * percent), Bitmap.Config.RGB_565);

            Canvas canvas = new Canvas(bm);
            Paint paint = new Paint();
            paint.setShader(bitmapShader);
            paint.setAntiAlias(true);
            canvas.drawRect(0, 0, bm.getWidth(), bm.getHeight(), paint);
            file.createNewFile();
            fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            // width = PAPER_WIDTH;
        } catch (IOException e) {
        } finally {

            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new Data(file, bm);
    }

    /**
     * 读取照片exif信息中的旋转角度
     *
     * @param path 照片路径
     * @return角度
     */
    public static int readPictureDegree(String path) {
        //传入图片路径
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.setDrawingCacheEnabled(true);
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;
    }

    //旋将旋转后的图片翻转
    public static Bitmap toturn(byte[] datas, int degree) {
        Bitmap img = BitmapFactory.decodeByteArray(datas, 0, datas.length);
        Log.d("CameraConfiguration","未转换角度图片 width:"+img.getWidth()+ "height:"+img.getHeight());
        Matrix matrix = new Matrix();
        matrix.postRotate(degree); /*翻转90度*/
        int width = img.getWidth();
        int height = img.getHeight();
        img = Bitmap.createBitmap(img, 0, 0, width, height, matrix, true);
        return img;
    }

    public static File loadBitmapFromView(View v, int width, int height, String name) {
        Bitmap b = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(b);
        v.draw(c);
        String newFilePath = v.getContext().getFilesDir().getAbsolutePath() + "/"
                + name + ".png";
        File file = new File(newFilePath);
        FileOutputStream fos = null;
        try {
            file.createNewFile();
            fos = new FileOutputStream(file);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            // width = PAPER_WIDTH;
        } catch (IOException e) {
        } finally {
            // if (bm != null) {
            // bm.recycle();
            // }
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 图片压缩
     *
     * @param image
     * @param size  压缩的图片大小 单位m
     * @return
     */
    public static Bitmap compressImage(Bitmap image, float size) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while (baos.toByteArray().length / 1024 > size * 1024) { //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    public static int getImageSize(Bitmap image) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        return baos.toByteArray().length / 1024;
    }

    public static class Data {
        File file;
        Bitmap bitmap;

        public File getFile() {
            return file;
        }

        public void setFile(File file) {
            this.file = file;
        }

        public Bitmap getBitmap() {
            return bitmap;
        }

        public void setBitmap(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        public Data(File file, Bitmap bitmap) {
            this.file = file;
            this.bitmap = bitmap;
        }
    }

}
