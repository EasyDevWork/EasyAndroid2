package com.easy.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;


public class RectangleImageView extends AppCompatImageView {
    private Paint paint;

    int roundPx = 0;

    public RectangleImageView(Context context) {
        this(context, null);
        initData(context);
    }

    public RectangleImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        initData(context);
    }

    public RectangleImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    private void initData(Context context) {
        paint = new Paint();
        roundPx = dp2px(context, context.getResources().getDimension(R.dimen.dp15));
    }

    /**
     * 设置圆角
     */
    public void setRound(int dp) {
        roundPx = dp2px(getContext(), dp);
        invalidate();
    }

    public int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 绘制圆角矩形图片
     *
     * @author caizhiming
     */
    @Override
    protected void onDraw(Canvas canvas) {

        Drawable drawable = getDrawable();
        if (null != drawable) {
            try {
                Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
                Bitmap b = getRoundBitmap(bitmap, roundPx);
                final Rect rectSrc = new Rect(0, 0, b.getWidth(), b.getHeight());
                final Rect rectDest = new Rect(0, 0, getWidth(), getHeight());
                paint.reset();
                canvas.drawBitmap(b, rectSrc, rectDest, paint);
            } catch (ClassCastException e) {

            }
        } else {
            super.onDraw(canvas);
        }
    }

    /**
     * 获取圆角矩形图片方法
     *
     * @param bitmap
     * @param roundPx,一般设置成14
     * @return Bitmap
     * @author caizhiming
     */
    private Bitmap getRoundBitmap(Bitmap bitmap, int roundPx) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;

        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        int x = bitmap.getWidth();

        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;


    }
}
