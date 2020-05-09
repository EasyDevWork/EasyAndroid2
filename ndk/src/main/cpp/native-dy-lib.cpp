#include <jni.h>
#include <string>
#include "LogUtils.h"
#include "TimeUtils.h" // 包含这个头文件

extern "C" {
jstring stringFromJNIByDy(JNIEnv *env, jclass instance) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

jint addByDy(JNIEnv *env, jclass clazz, jint a, jint b) {
    return a + b;
}

jint RegisterNatives(JNIEnv *env) {
    jclass clazz = env->FindClass("com/easy/ndk/NDKTools");
    if (clazz == NULL) {
        return JNI_ERR;
    }
    JNINativeMethod methods_MainActivity[] = {
            {"stringFromJNIByDy", "()Ljava/lang/String;", (void *) stringFromJNIByDy},
            {"addByDy",           "(II)I",                (void *) addByDy}
    };
    return env->RegisterNatives(clazz, methods_MainActivity,
                                sizeof(methods_MainActivity) / sizeof(methods_MainActivity[0]));
}

jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    __TIC1__(fwrite); // 耗时统计起始处
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    jint result = RegisterNatives(env);
    LOGD("RegisterNatives result: %d", result);
    __TOC1__(fwrite); // 耗时统计结束
    return JNI_VERSION_1_6;
}
}