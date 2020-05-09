#include <jni.h>
#include <string>

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
    JNIEnv *env = NULL;
    if (vm->GetEnv((void **) &env, JNI_VERSION_1_6) != JNI_OK) {
        return JNI_ERR;
    }
    jint result = RegisterNatives(env);
//    LOGD("RegisterNatives result: %d", result);
    return JNI_VERSION_1_6;
}
}