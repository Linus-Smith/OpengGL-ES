#include <jni.h>
#include <string>
#include <android/log.h>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_opengesnative_MainActivity_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    std::string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}



extern "C"
JNIEXPORT void JNICALL
Java_com_example_opengesnative_MainActivity_onSurfaceCreated(
        JNIEnv *env,
        jobject /* this */) {
    __android_log_print(ANDROID_LOG_DEBUG, "linus", "onSurfaceCreate");
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_opengesnative_MainActivity_onSurfaceChanged(
        JNIEnv *env,
        jobject /* this */) {
    __android_log_print(ANDROID_LOG_DEBUG, "linus", "onSurfaceChanged");
}


extern "C"
JNIEXPORT void JNICALL
Java_com_example_opengesnative_MainActivity_onDrawFrame(
        JNIEnv *env,
        jobject /* this */) {
    __android_log_print(ANDROID_LOG_DEBUG, "linus", "onDrawFrame");
}