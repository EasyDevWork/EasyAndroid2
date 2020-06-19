package com.easy.framework.camera;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.io.IOException;

public class CameraView extends SurfaceView implements SurfaceHolder.Callback, Camera.PreviewCallback {

    private static final String TAG = CameraView.class.getName();
    private CameraManager mCameraManager;

    public CameraView(Context context) {
        this(context, null);
    }

    public CameraView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (isInEditMode()) {
            return;
        }
        if (checkCameraHardware()) {
            mCameraManager = new CameraManager(getContext());
            mCameraManager.setPreviewCallback(this);
            getHolder().addCallback(this);
            setBackCamera();
        } else {
            throw new RuntimeException("Error: Camera not found");
        }
    }

    /**
     * Starts camera preview and decoding
     */
    public void startCamera() {
        mCameraManager.startPreview();
    }

    /**
     * Stop camera preview and decoding
     */
    public void stopCamera() {
        mCameraManager.stopPreview();
    }

    /**
     * Set Camera autofocus interval value
     * default value is 5000 ms.
     *
     * @param autoFocusIntervalInMs autofocus interval value
     */
    public void setAutoFocusInterval(long autoFocusIntervalInMs) {
        if (mCameraManager != null) {
            mCameraManager.setAutoFocusInterval(autoFocusIntervalInMs);
        }
    }

    /**
     * Trigger an auto focus
     */
    public void forceAutoFocus() {
        if (mCameraManager != null) {
            mCameraManager.forceAutoFocus();
        }
    }

    public Camera getCamera() {
        if (mCameraManager != null) {
            return mCameraManager.getCamera();
        }
        return null;
    }

    /**
     * Set Torch enabled/disabled.
     * default value is false
     *
     * @param enabled torch enabled/disabled.
     */
    public void setFlashEnabled(boolean enabled) {
        if (mCameraManager != null) {
            mCameraManager.setFlashEnabled(enabled);
        }
    }

    /**
     * Allows user to specify the camera ID, rather than determine
     * it automatically based on available cameras and their orientation.
     *
     * @param cameraId camera ID of the camera to use. A negative value means "no preference".
     */
    public void setPreviewCameraId(int cameraId) {
        mCameraManager.setPreviewCameraId(cameraId);
    }

    /**
     * Camera preview from device back camera
     */
    public void setBackCamera() {
        setPreviewCameraId(Camera.CameraInfo.CAMERA_FACING_BACK);
    }

    /**
     * Camera preview from device front camera
     */
    public void setFrontCamera() {
        setPreviewCameraId(Camera.CameraInfo.CAMERA_FACING_FRONT);
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    public void setBestSize(Point bestSize) {
        if (mCameraManager != null) {
            mCameraManager.setBestSize(bestSize);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d(TAG, "surfaceCreated");
        try {
            mCameraManager.openDriver(holder);
        } catch (IOException | RuntimeException e) {
            Log.w(TAG, "Can not openDriver: " + e.getMessage());
            mCameraManager.closeDriver();
        }
        try {
            mCameraManager.startPreview();
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            mCameraManager.closeDriver();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d(TAG, "surfaceChanged");
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "surfaceDestroyed");
        mCameraManager.setPreviewCallback(null);
        mCameraManager.stopPreview();
        mCameraManager.closeDriver();
    }

    // Called when camera take a frame
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {

    }

    /**
     * Check if this device has a camera
     */
    @SuppressLint("UnsupportedChromeOsCameraSystemFeature")
    private boolean checkCameraHardware() {
        if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else if (getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)) {
            // this device has a front camera
            return true;
        } else {
            // this device has any camera
            return getContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY);
        }
    }
}
