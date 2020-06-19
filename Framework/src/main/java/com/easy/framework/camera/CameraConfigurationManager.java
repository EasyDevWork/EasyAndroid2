package com.easy.framework.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;

import com.easy.utils.DimensUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * 相机配置
 */
public final class CameraConfigurationManager {

    private static final String TAG = "CameraConfiguration";

    private static final float MAX_EXPOSURE_COMPENSATION = 1.5f;
    private static final float MIN_EXPOSURE_COMPENSATION = 0.0f;
    private final Context context;

    private Point bestPictureSize;//最佳的图片大小
    private Point bestPreviewSize;//最佳预览大小

    CameraConfigurationManager(Context context) {
        this.context = context;
    }

    /**
     * 默认寻找最接近屏幕大小比例为最佳
     * 设置需要的预览比例和照片比例---建议比例设置为9：16,因为基本上的硬件都有这个比例
     *
     * @param camera
     */
    public void initFromCameraParameters(OpenCamera camera, Point bestSize) {
        if (bestSize == null) {
            bestSize = DimensUtils.getScreenSize(context);
        }
        Log.i(TAG, "视图大小 " + bestSize);
        Log.i(TAG, "开始查找最佳的图片大小方案: ");
        Camera.Parameters parameters = camera.getCamera().getParameters();
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        bestPictureSize = findBestSizeValue(parameters, supportedPictureSizes, bestSize);
        Log.i(TAG, "最佳的图片大小为: " + bestPictureSize);
        Log.i(TAG, "开始查找最佳的预览大小方案: ");
        List<Camera.Size> supportedPreviewSizes = parameters.getSupportedPreviewSizes();
        bestPreviewSize = findBestSizeValue(parameters, supportedPreviewSizes, bestSize);
        Log.i(TAG, "最佳的预览大小为:" + bestPreviewSize);
    }

    public void setDesiredCameraParameters(OpenCamera camera, boolean safeMode) {
        Camera theCamera = camera.getCamera();
        Camera.Parameters parameters = theCamera.getParameters();
        if (parameters == null) {
            Log.w(TAG, "no camera parameters are null.");
            return;
        }

        if (safeMode) {
            Log.w(TAG, "In camera config safe mode -- most settings will not be honored");
        }
        // Maybe selected auto-focus but not available, so fall through here:
        String focusMode = null;
        if (!safeMode) {
            List<String> supportedFocusModes = parameters.getSupportedFocusModes();
            focusMode = findSettableValue("focus mode", supportedFocusModes, Camera.Parameters.FOCUS_MODE_AUTO);
        }
        if (focusMode != null) {
            parameters.setFocusMode(focusMode);
        }

        parameters.setPictureFormat(ImageFormat.JPEG);
        parameters.setPictureSize(bestPictureSize.x, bestPictureSize.y);
        parameters.setPreviewSize(bestPreviewSize.x, bestPreviewSize.y);
        theCamera.setParameters(parameters);

        Camera.Parameters afterParameters = theCamera.getParameters();
        Camera.Size afterSize = afterParameters.getPreviewSize();
        if (afterSize != null && (bestPreviewSize.x != afterSize.width || bestPreviewSize.y != afterSize.height)) {
            Log.w(TAG, "Camera said it supported preview size "
                    + bestPreviewSize.x
                    + 'x'
                    + bestPreviewSize.y
                    + ", but after setting it, preview size is "
                    + afterSize.width
                    + 'x'
                    + afterSize.height);
            bestPreviewSize.x = afterSize.width;
            bestPreviewSize.y = afterSize.height;
        }
    }

    /**
     * 选择最佳大小方案
     *
     * @param parameters
     * @param screenResolution
     * @return
     */
    public Point findBestSizeValue(Camera.Parameters parameters, List<Camera.Size> supportedSizes, Point screenResolution) {
        if (supportedSizes == null) {
            Log.w(TAG, "Device returned no supported preview sizes; using default");
            Camera.Size defaultSize = parameters.getPreviewSize();
            return new Point(defaultSize.width, defaultSize.height);
        }
        // Sort by size, descending
        List<Camera.Size> supportedPreviewSizes = new ArrayList<>(supportedSizes);
        Collections.sort(supportedPreviewSizes, (a, b) -> {
            int aPixels = a.height * a.width;
            int bPixels = b.height * b.width;
            if (bPixels < aPixels) {
                return -1;
            }
            if (bPixels > aPixels) {
                return 1;
            }
            return 0;
        });

        Point bestSize = null;
        float screenAspectRatio = (float) screenResolution.x / (float) screenResolution.y;

        float diff = Float.POSITIVE_INFINITY;
        StringBuilder previewSizesString = new StringBuilder();
        for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
            int realWidth = supportedPreviewSize.width;
            int realHeight = supportedPreviewSize.height;

            // This code is modified since We're using portrait mode
            boolean isCandidateLandscape = realWidth > realHeight;
            int maybeFlippedWidth = isCandidateLandscape ? realHeight : realWidth;
            int maybeFlippedHeight = isCandidateLandscape ? realWidth : realHeight;

            if (maybeFlippedWidth == screenResolution.x && maybeFlippedHeight == screenResolution.y) {
                Point exactPoint = new Point(realWidth, realHeight);
                Log.i(TAG, "Found preview size exactly matching screen size: " + exactPoint);
                return exactPoint;
            }
            float aspectRatio = (float) maybeFlippedWidth / (float) maybeFlippedHeight;
            previewSizesString.append(maybeFlippedWidth)
                    .append('/')
                    .append(maybeFlippedHeight)
                    .append('=')
                    .append(aspectRatio)
                    .append("\n");

            float newDiff = Math.abs(aspectRatio - screenAspectRatio);
            if (newDiff < diff) {
                bestSize = new Point(realWidth, realHeight);
                diff = newDiff;
            }
            if (newDiff == 0) {
                break;
            }
        }
        Log.i(TAG, "支持的备选大小: " + previewSizesString);

        if (bestSize == null) {
            Camera.Size defaultSize = parameters.getPreviewSize();
            bestSize = new Point(defaultSize.width, defaultSize.height);
            Log.i(TAG, "不支持设置的大小, 使用默认大小: " + bestSize);
        }
        return bestSize;
    }

    private String findSettableValue(String name, Collection<String> supportedValues, String... desiredValues) {
        Log.i(TAG, "Requesting " + name + " value from among: " + Arrays.toString(desiredValues));
        Log.i(TAG, "Supported " + name + " values: " + supportedValues);
        if (supportedValues != null) {
            for (String desiredValue : desiredValues) {
                if (supportedValues.contains(desiredValue)) {
                    Log.i(TAG, "Can set " + name + " to: " + desiredValue);
                    return desiredValue;
                }
            }
        }
        Log.i(TAG, "No supported values match");
        return null;
    }

    /**
     * 闪光灯是否打开
     *
     * @param camera
     * @return
     */
    public boolean getFlashState(Camera camera) {
        if (camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            if (parameters != null) {
                String flashMode = camera.getParameters().getFlashMode();
                return Camera.Parameters.FLASH_MODE_ON.equals(flashMode) || Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode);
            }
        }
        return false;
    }

    /**
     * 闪光灯是否打开
     *
     * @param camera
     * @param enabled
     */
    public void setFlashEnabled(Camera camera, boolean enabled) {
        Camera.Parameters parameters = camera.getParameters();
        setFlashEnabled(parameters, enabled, false);
        camera.setParameters(parameters);
    }

    /**
     * 设置闪光灯
     *
     * @param parameters
     * @param enabled
     * @param safeMode
     */
    public void setFlashEnabled(Camera.Parameters parameters, boolean enabled, boolean safeMode) {
        setFlashEnabled(parameters, enabled);
        if (!safeMode) {
            setBestExposure(parameters, enabled);
        }
    }

    /**
     * 设置闪光灯
     *
     * @param parameters
     * @param enabled
     */
    public void setFlashEnabled(Camera.Parameters parameters, boolean enabled) {
        List<String> supportedFlashModes = parameters.getSupportedFlashModes();
        String flashMode;
        if (enabled) {
            flashMode = findSettableValue("flash mode", supportedFlashModes, Camera.Parameters.FLASH_MODE_TORCH, Camera.Parameters.FLASH_MODE_ON);
        } else {
            flashMode = findSettableValue("flash mode", supportedFlashModes, Camera.Parameters.FLASH_MODE_OFF);
        }
        if (flashMode != null) {
            if (flashMode.equals(parameters.getFlashMode())) {
                Log.i(TAG, "Flash mode already set to " + flashMode);
            } else {
                Log.i(TAG, "Setting flash mode to " + flashMode);
                parameters.setFlashMode(flashMode);
            }
        }
    }

    /**
     * 设置曝光度
     *
     * @param parameters
     * @param lightOn
     */
    public static void setBestExposure(Camera.Parameters parameters, boolean lightOn) {
        int minExposure = parameters.getMinExposureCompensation();
        int maxExposure = parameters.getMaxExposureCompensation();
        float step = parameters.getExposureCompensationStep();
        if ((minExposure != 0 || maxExposure != 0) && step > 0.0f) {
            // Set low when light is on
            float targetCompensation = lightOn ? MIN_EXPOSURE_COMPENSATION : MAX_EXPOSURE_COMPENSATION;
            int compensationSteps = Math.round(targetCompensation / step);
            float actualCompensation = step * compensationSteps;
            // Clamp value:
            compensationSteps = Math.max(Math.min(compensationSteps, maxExposure), minExposure);
            if (parameters.getExposureCompensation() == compensationSteps) {
                Log.i(TAG, "Exposure compensation already set to " + compensationSteps + " / " + actualCompensation);
            } else {
                Log.i(TAG, "Setting exposure compensation to " + compensationSteps + " / " + actualCompensation);
                parameters.setExposureCompensation(compensationSteps);
            }
        } else {
            Log.i(TAG, "Camera does not support exposure compensation");
        }
    }
}
