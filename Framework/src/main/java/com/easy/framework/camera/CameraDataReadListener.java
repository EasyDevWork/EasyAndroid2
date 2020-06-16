package com.easy.framework.camera;

import android.graphics.PointF;

public interface CameraDataReadListener {

    void onDataRead(String text, PointF[] points);
}
