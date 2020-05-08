#include <jni.h>
#include <string>

extern "C" JNIEXPORT jstring JNICALL
Java_com_easy_ndk_NDKTools_stringFromJNI(JNIEnv *env, jobject) {
    std::string hello = "我来自C++";
    return env->NewStringUTF(hello.c_str());
}
