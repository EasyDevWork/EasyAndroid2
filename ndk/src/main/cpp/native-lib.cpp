#include <jni.h>
#include <string>

extern "C" {
JNIEXPORT void JNICALL Java_com_easy_ndk_NDKTools_callStaticMethod(JNIEnv *env, jobject) {
    jclass NDKToolsClass = env->FindClass("com/easy/ndk/NDKTools");// 找到对应的类
    if (NDKToolsClass == NULL) return;
    // 获取methodId
    jmethodID NDKToolsMethod = env->GetStaticMethodID(NDKToolsClass, "logMessage","(Ljava/lang/String;)V");
    if (NDKToolsMethod == NULL) return;

    jstring params = env->NewStringUTF("这是从JNI调用的Log");
    // 调用static方法
    env->CallStaticVoidMethod(NDKToolsClass, NDKToolsMethod, params);
    // 释放内存
    env->DeleteLocalRef(NDKToolsClass);
    env->DeleteLocalRef(params);
}

JNIEXPORT jint JNICALL
Java_com_easy_ndk_NDKTools_addNative(JNIEnv *env, jobject instance, jint arg1, jint arg2) {
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
// 返回结果
    return result;
}
}