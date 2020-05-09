#include <jni.h>
#include <string>
#include <dirent.h>
#include "LogUtils.h"
#include <android/bitmap.h>

extern "C" {
using namespace std;

JNIEXPORT jstring JNICALL Java_com_easy_ndk_NDKTools_callStaticMethod(JNIEnv *env, jclass clazz) {
    jclass NDKToolsClass = env->FindClass("com/easy/ndk/NDKTools");// 找到对应的类
    if (NDKToolsClass == NULL) return NULL;
    // 获取methodId
    jmethodID NDKToolsMethod = env->GetStaticMethodID(NDKToolsClass, "logMessage",
                                                      "(Ljava/lang/String;)Ljava/lang/String;");
    if (NDKToolsMethod == NULL) return NULL;

    jstring params = env->NewStringUTF("我是JNI数据");
    // 调用static方法
    jstring result = static_cast<jstring>(env->CallStaticObjectMethod(NDKToolsClass, NDKToolsMethod,
                                                                      params));
    // 释放内存
    env->DeleteLocalRef(NDKToolsClass);
    env->DeleteLocalRef(params);
    return result;
}

JNIEXPORT jint JNICALL
Java_com_easy_ndk_NDKTools_addNative(JNIEnv *env, jclass instance, jint arg1, jint arg2) {
// 找到对应类
    jclass cls_adder = env->FindClass("com/easy/ndk/Adder");
// 获取构造方法
    jmethodID mth_constructor = env->GetMethodID(cls_adder, "<init>", "(II)V");
// 调用构造方法构建jobject
    jobject adder = env->NewObject(cls_adder, mth_constructor, arg1, arg2);
// 获取add方法
    jmethodID mth_add = env->GetMethodID(cls_adder, "doAdd", "()I");
// 调用add方法获取返回值
    jint result = env->CallIntMethod(adder, mth_add);
    // 回收资源
    env->DeleteLocalRef(cls_adder);
    env->DeleteLocalRef(adder);
    return result;
}

JNIEXPORT jint JNICALL Java_com_easy_ndk_NDKTools_getNativeVersion(JNIEnv *env, jclass clazz) {
    return env->GetVersion();
}

JNIEXPORT jstring JNICALL Java_com_easy_ndk_NDKTools_getJavaVersion(JNIEnv *env, jclass clazz) {
    jclass NDKToolsClass = env->FindClass("com/easy/ndk/NDKTools");
    if (NDKToolsClass == NULL) {
        return NULL;
    }
    jmethodID NDKToolsMethod = env->GetStaticMethodID(NDKToolsClass, "getVersionName",
                                                      "()Ljava/lang/String;");
    if (NDKToolsMethod == NULL) return NULL;
    jstring result = static_cast<jstring>(env->CallStaticObjectMethod(NDKToolsClass,
                                                                      NDKToolsMethod));
    return result;
}

JNIEXPORT void JNICALL Java_com_easy_ndk_NDKTools_handleExcept(JNIEnv *env, jclass clazz) {
    jmethodID methodId = env->GetStaticMethodID(clazz, "callNullPointerException", "()V");
    if (methodId == NULL) return;
    env->CallStaticVoidMethod(clazz, methodId);
    jthrowable exc = env->ExceptionOccurred();
    if (exc) {
        env->ExceptionDescribe(); // 打印异常信息
        env->ExceptionClear(); // 清除掉发生的异常
        jclass newExcCls = env->FindClass("java/lang/IllegalArgumentException");
        env->ThrowNew(newExcCls, "throw from JNI"); // 返回一个新的异常到 Java
    }
}

void showAllFiles(string dir_name) {
    // check the parameter
    if (dir_name.empty()) {
        LOGE("dir_name is null !");
        return;
    }
    DIR *dir = opendir(dir_name.c_str());
    // check is dir ?
    if (NULL == dir) {
        LOGE("Can not open dir. Check path or permission!");
        return;
    }
    struct dirent *file;
    // read all the files in dir
    while ((file = readdir(dir)) != NULL) {
        // skip "." and ".."
        if (strcmp(file->d_name, ".") == 0 || strcmp(file->d_name, "..") == 0) {
            LOGV("ignore . and ..");
            continue;
        }
        if (file->d_type == DT_DIR) {
            string filePath = dir_name + "/" + file->d_name;
            showAllFiles(filePath); // 递归执行
        } else {
            // 如果需要把路径保存到集合中去，就在这里执行 add 的操作
            LOGI("filePath: %s/%s", dir_name.c_str(), file->d_name);
        }
    }
    closedir(dir);
}

JNIEXPORT void JNICALL Java_com_easy_ndk_NDKTools_showAllFiles(JNIEnv *env, jclass instance, jstring dirPath_) {
    const char *dirPath = env->GetStringUTFChars(dirPath_, 0);
    showAllFiles(string(dirPath));
    env->ReleaseStringUTFChars(dirPath_, dirPath);
}

JNIEXPORT void JNICALL Java_com_easy_ndk_NDKTools_parseBitmap(JNIEnv *env, jclass thiz, jobject bitmap) {
    if (NULL == bitmap) {
        LOGE("bitmap is null!");
        return;
    }
    AndroidBitmapInfo info; // create a AndroidBitmapInfo
    int result;
    // 获取图片信息
    result = AndroidBitmap_getInfo(env, bitmap, &info);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_getInfo failed, result: %d", result);
        return;
    }
    LOGD("bitmap width: %d, height: %d, format: %d, stride: %d", info.width, info.height,info.format, info.stride);
    // 获取像素信息
    unsigned char *addrPtr;
    result = AndroidBitmap_lockPixels(env, bitmap, reinterpret_cast<void **>(&addrPtr));
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_lockPixels failed, result: %d", result);
        return;
    }
    // 执行图片操作的逻辑
    int length = info.stride * info.height;
    for (int i = 0; i < length; ++i) {
        LOGD("value: %x", addrPtr[i]);
    }
    // 像素信息不再使用后需要解除锁定
    result = AndroidBitmap_unlockPixels(env, bitmap);
    if (result != ANDROID_BITMAP_RESULT_SUCCESS) {
        LOGE("AndroidBitmap_unlockPixels failed, result: %d", result);
    }
}
}

