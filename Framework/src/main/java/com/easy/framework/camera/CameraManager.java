package com.easy.framework.camera;

import android.content.Context;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;

import java.io.IOException;

public class CameraManager {
    private static final String TAG = CameraManager.class.getSimpleName();

    private final Context context;
    private final CameraConfigurationManager configManager;
    private OpenCamera openCamera;
    private AutoFocusManager autoFocusManager;
    private boolean initialized;
    private boolean previewing;
    private Camera.PreviewCallback previewCallback;
    private int displayOrientation = 90;
    Point bestSize;

    private int requestedCameraId = OpenCameraInterface.NO_REQUESTED_CAMERA;
    private long autoFocusIntervalInMs = AutoFocusManager.DEFAULT_AUTO_FOCUS_INTERVAL_MS;

    public CameraManager(Context context) {
        this.context = context;
        this.configManager = new CameraConfigurationManager(context);
    }

    /**
     * 设置最佳的宽高比例
     * @param bestSize
     */
    public void setBestSize(Point bestSize) {
        this.bestSize = bestSize;
    }

    public void setPreviewCallback(Camera.PreviewCallback previewCallback) {
        this.previewCallback = previewCallback;
        if (isOpen()) {
            openCamera.getCamera().setPreviewCallback(previewCallback);
        }
    }

    public void setDisplayOrientation(int degrees) {
        this.displayOrientation = degrees;
        if (isOpen()) {
            openCamera.getCamera().setDisplayOrientation(degrees);
        }
    }

    public void setAutoFocusInterval(long autoFocusIntervalInMs) {
        this.autoFocusIntervalInMs = autoFocusIntervalInMs;
        if (autoFocusManager != null) {
            autoFocusManager.setAutofocusInterval(autoFocusIntervalInMs);
        }
    }

    public void forceAutoFocus() {
        if (autoFocusManager != null) {
            autoFocusManager.start();
        }
    }

    public Camera getCamera() {
        if (openCamera != null) {
            return openCamera.getCamera();
        }
        return null;
    }

    public synchronized void openDriver(SurfaceHolder holder) throws IOException {
        OpenCamera theCamera = openCamera;
        if (!isOpen()) {
            theCamera = OpenCameraInterface.open(requestedCameraId);
            if (theCamera == null || theCamera.getCamera() == null) {
                throw new IOException("Camera.open() failed to return object from driver");
            }
            openCamera = theCamera;
        }
        theCamera.getCamera().setPreviewDisplay(holder);
        theCamera.getCamera().setPreviewCallback(previewCallback);
        theCamera.getCamera().setDisplayOrientation(displayOrientation);

        if (!initialized) {
            initialized = true;
            configManager.initFromCameraParameters(theCamera,bestSize);
        }

        Camera cameraObject = theCamera.getCamera();
        Camera.Parameters parameters = cameraObject.getParameters();
        String parametersFlattened = parameters == null ? null : parameters.flatten(); // Save these, temporarily
        try {
            configManager.setDesiredCameraParameters(theCamera, false);
        } catch (RuntimeException re) {
            // Driver failed
            Log.w(TAG, "Camera rejected parameters. Setting only minimal safe-mode parameters");
            Log.i(TAG, "Resetting to saved camera params: " + parametersFlattened);
            // Reset:
            if (parametersFlattened != null) {
                parameters = cameraObject.getParameters();
                parameters.unflatten(parametersFlattened);
                try {
                    cameraObject.setParameters(parameters);
                    configManager.setDesiredCameraParameters(theCamera, true);
                } catch (RuntimeException re2) {
                    // Well, darn. Give up
                    Log.w(TAG, "Camera rejected even safe-mode parameters! No configuration");
                }
            }
        }
        cameraObject.setPreviewDisplay(holder);
    }

    /**
     * Allows third party apps to specify the camera ID, rather than determine
     * it automatically based on available cameras and their orientation.
     *
     * @param cameraId camera ID of the camera to use. A negative value means "no preference".
     */
    public synchronized void setPreviewCameraId(int cameraId) {
        requestedCameraId = cameraId;
    }

    public int getPreviewCameraId() {
        return requestedCameraId;
    }

    /**
     * @param enabled if {@code true}, light should be turned on if currently off. And vice versa.
     */
    public synchronized void setFlashEnabled(boolean enabled) {
        OpenCamera theCamera = openCamera;
        if (theCamera != null && enabled != configManager.getFlashState(theCamera.getCamera())) {
            boolean wasAutoFocusManager = autoFocusManager != null;
            if (wasAutoFocusManager) {
                autoFocusManager.stop();
                autoFocusManager = null;
            }
            configManager.setFlashEnabled(theCamera.getCamera(), enabled);
            if (wasAutoFocusManager) {
                autoFocusManager = new AutoFocusManager(theCamera.getCamera());
                autoFocusManager.start();
            }
        }
    }

    public synchronized boolean isOpen() {
        return openCamera != null && openCamera.getCamera() != null;
    }

    /**
     * Closes the camera driver if still in use.
     */
    public synchronized void closeDriver() {
        if (isOpen()) {
            openCamera.getCamera().release();
            openCamera = null;
            // Make sure to clear these each time we close the camera, so that any scanning rect
            // requested by intent is forgotten.
            // framingRect = null;
            // framingRectInPreview = null;
        }
    }

    /**
     * Asks the camera hardware to begin drawing preview frames to the screen.
     */
    public synchronized void startPreview() {
        OpenCamera theCamera = openCamera;
        if (theCamera != null && !previewing) {
            theCamera.getCamera().startPreview();
            previewing = true;
            autoFocusManager = new AutoFocusManager(theCamera.getCamera());
            autoFocusManager.setAutofocusInterval(autoFocusIntervalInMs);
        }
    }

    /**
     * Tells the camera to stop drawing preview frames.
     */
    public synchronized void stopPreview() {
        if (autoFocusManager != null) {
            autoFocusManager.stop();
            autoFocusManager = null;
        }
        if (openCamera != null && previewing) {
            openCamera.getCamera().stopPreview();
            previewing = false;
        }
    }
}
