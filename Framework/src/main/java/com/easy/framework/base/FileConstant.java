package com.easy.framework.base;

import java.io.File;

public class FileConstant {
    /**
     * 文件根路径
     */
    public static final String BASE_FILE_PATH = "localFile" + File.separator;

    /**
     * apk
     */
    public static final int TYPE_APP = 0;

    /**
     *音频
     */
    public static final int TYPE_AUDIO = 2;

    /**
     * 图片
     */
    public static final int TYPE_PHOTO = 3;

    /**
     * 应用文件保存路径
     */
    public static final String SAVE_APP_PATH = BASE_FILE_PATH + "app" + File.separator;
    /**
     * 录音位置
     */
    public static final String SAVE_AUDIO_PATH = BASE_FILE_PATH + "audio" + File.separator;

    /**
     * 图片保存路径
     */
    public static final String SAVE_PHOTO_PATH = BASE_FILE_PATH + "photo" + File.separator;
    /**
     * 其他
     */
    public static final String SAVE_OTHER_PATH = BASE_FILE_PATH + "other" + File.separator;
}
