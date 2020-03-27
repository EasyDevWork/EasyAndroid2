package com.easy.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.MimeTypeMap;

import com.easy.utils.base.FileConstant;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public class FileUtils {

    /**
     * 按分类自定义文件路径
     *
     * @param @param  type 图片为 pic 应用为 app
     * @param @return 设定文件
     * @return String    返回类型
     * @throws
     * @Title: getSaveFilePath
     * @Description:
     */
    public static String getFilePath(int saveType, Context context) {
        String filePath;
        switch (saveType) {
            case FileConstant.TYPE_APP:
                filePath = getRootFilePath(context) + FileConstant.SAVE_APP_PATH;
                break;
            case FileConstant.TYPE_AUDIO:
                filePath = getRootFilePath(context) + FileConstant.SAVE_AUDIO_PATH;
                break;
            case FileConstant.TYPE_PHOTO:
                filePath = getRootFilePath(context) + FileConstant.SAVE_PHOTO_PATH;
                break;
            default:
                filePath = getRootFilePath(context) + FileConstant.SAVE_OTHER_PATH;
                break;
        }
        createDirectory(filePath);
        return filePath;
    }

    /**
     * 获取系统自带文件路径
     *
     * @return
     */
    public static String getSystemFilePath(Context context, String type) {
        //Environment.DIRECTORY_PICTURES : /mnt/sdcard/pictures
        File file = context.getExternalFilesDir(type);
        return file.getAbsolutePath();
    }

    /**
     * 从URL中获取扩张名
     *
     * @param url
     * @return
     */
    public static String getFileExtension(String url, String defaultStr) {
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (Utils.isEmpty(extension)) {
            extension = defaultStr;
        }
        return extension;
    }

    /**
     * 创建文件目录
     *
     * @param filePath
     * @return
     */
    public static boolean createDirectory(String filePath) {
        if (fileIsExist(filePath)) {
            return true;
        } else {
            File file = new File(filePath);
            return file.mkdirs();
        }
    }

    /**
     * 判断文件是否存在---不存在就创建
     *
     * @param filePath
     * @return
     */
    public static boolean createOrExistsFile(String filePath) {
        File file = new File(filePath);
        if (file.exists()) return file.isFile();
        if (!createOrExistsDir(file.getParentFile())) return false;
        try {
            return file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断是否是文件目录--不存在就创建
     *
     * @param file
     * @return
     */
    public static boolean createOrExistsDir(final File file) {
        return file != null && (file.exists() ? file.isDirectory() : file.mkdirs());
    }

    /**
     * 文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean fileIsExist(String filePath) {
        if (Utils.isEmpty(filePath)) {
            return false;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        }
        return true;
    }

    /**
     * 获取文件保存根目录
     *
     * @return 如果有sdcard 则返回sdcard根路径，
     * 如果没有则返回应用包下file目录/data/data/包名/file/
     */
    public static String getRootFilePath(Context context) {
        String strPathHead;
        if (isCanUseSD()) {
            strPathHead = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
        } else {
            strPathHead = context.getFilesDir().getPath() + File.separator;
        }
        return strPathHead;
    }

    public static boolean isCanUseSD() {
        try {
            return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
        } catch (Exception e) {
            Log.e("isCanUseSD", "sdcard不可用");
        }
        return false;
    }

    // 获取文件
    //Context.getExternalFilesDir() --> SDCard/Android/data/你的应用的包名/files/ 目录，一般放一些长时间保存的数据
    //Context.getExternalCacheDir() --> SDCard/Android/data/你的应用包名/cache/目录，一般存放临时缓存数据
    public static long getFolderSize(File file) {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                // 如果下面还有文件
                if (fileList[i].isDirectory()) {
                    size = size + getFolderSize(fileList[i]);
                } else {
                    size = size + fileList[i].length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 删除文件夹/文件
     * @param dir
     * @return
     */
    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            if (children != null) {
                for (int i = 0; i < children.length; i++) {
                    boolean success = deleteDir(new File(dir, children[i]));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return dir.delete();
    }
    /**
     * 格式化单位
     *
     * @param size
     */
    /**
     * 格式化单位
     *
     * @param size
     * @return
     */
    public static String[] getFormatSize(double size) {
        String[] sizeStr = new String[]{"0", "K"};
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return sizeStr;
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            sizeStr[0] = result1.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
            sizeStr[1] = "KB";
            return sizeStr;
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            sizeStr[0] = result2.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
            sizeStr[1] = "MB";
            return sizeStr;
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            sizeStr[0] = result3.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
            sizeStr[1] = "GB";
            return sizeStr;
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        sizeStr[0] = result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString();
        sizeStr[1] = "TB";
        return sizeStr;
    }

    public static String getFromRaw(Context context, int id) {
        try {
            //获取文件中的内容
            InputStream inputStream = context.getResources().openRawResource(id);
            //将文件中的字节转换为字符
            InputStreamReader isReader = new InputStreamReader(inputStream, "UTF-8");
            //使用bufferReader去读取字符
            BufferedReader reader = new BufferedReader(isReader);
            StringBuilder builder = new StringBuilder();
            try {
                String out;
                while ((out = reader.readLine()) != null) {
                    builder.append(out);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return builder.toString();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 写文件
     *
     * @param saveFile
     * @param content
     * @return
     */
    public static boolean writeStringToFile(String saveFile, String content) {
        try {
            boolean createFile = createOrExistsFile(saveFile);
            if (createFile) {
                File file = new File(saveFile);
                FileOutputStream output = new FileOutputStream(file);
                output.write(content.getBytes());
                output.flush();
                output.close();
            } else {
                return false;
            }
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
