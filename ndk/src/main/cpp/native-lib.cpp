#include <jni.h>
#include <string>

extern "C" {
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

JNIEXPORT jint JNICALL Java_com_easy_ndk_NDKTools_addNative(JNIEnv *env, jclass instance, jint arg1, jint arg2) {
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
}