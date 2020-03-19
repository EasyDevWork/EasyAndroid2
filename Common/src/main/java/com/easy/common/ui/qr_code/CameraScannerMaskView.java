package com.easy.common.ui.qr_code;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.FrameLayout;

public class CameraScannerMaskView extends FrameLayout {

    private ScannerBarView scannerBarView;
    private CameraLensView cameraLensView;

    public CameraScannerMaskView(Context context) {
        this(context, null);
    }

    public CameraScannerMaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraScannerMaskView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        cameraLensView = new CameraLensView(context);
        cameraLensView.init(context, attrs, defStyleAttr);
        scannerBarView = new ScannerBarView(context);
        scannerBarView.init(context, attrs, defStyleAttr);
        addView(cameraLensView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        addView(scannerBarView, new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);
        reLocationScannerBarView(cameraLensView.getCameraLensRect());
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void reLocationScannerBarView(Rect rect) {
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) scannerBarView.getLayoutParams();
        params.width = rect.width();
        params.height = rect.height();
        params.leftMargin = rect.left;
        params.topMargin = rect.top;
        scannerBarView.setLayoutParams(params);
    }

    public void start() {
        scannerBarView.start();
    }

    public void pause() {
        scannerBarView.pause();
    }

    public void resume() {
        scannerBarView.resume();
    }

    public void stop() {
        scannerBarView.stop();
    }
}