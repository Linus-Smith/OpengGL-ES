#include <jni.h>
#include <string>
#include <android/log.h>

static const char * kClassName = "com/example/opengesnative/MainActivity";

#define  LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "linus" , __VA_ARGS__);


void onSurfaceCreated();
void onSurfaceChanged();
void onDrawFrame();

static const JNINativeMethod gMethod[] = {
        {"onSurfaceCreated", "()V", (void *)onSurfaceCreated},
        {"onSurfaceChanged",  "()V", (void *)onSurfaceChanged},
        {"onDrawFrame", "()V", (void *)onDrawFrame}
};

void onSurfaceCreated() {
    LOGD("onSurfaceCreated");
}


void onSurfaceChanged() {
    LOGD("onSurfaceChanged");
}

void onDrawFrame() {
    LOGD("onDrawFrddame");
}

JNIEXPORT jint JNI_OnLoad(JavaVM * vm, void * reserved) {
    JNIEnv * env = NULL;

    if(((*vm).GetEnv((void **)&env, JNI_VERSION_1_4)) != JNI_OK) {
        return  -1;
    }

    jclass clazz = (*env).FindClass(kClassName);

    if(clazz == NULL) {
        LOGD("clazz == NULL")
        return -1;
    }

    if((*env).RegisterNatives(clazz, gMethod, sizeof(gMethod) / sizeof(gMethod[0])) != JNI_OK) {
        LOGD("register natives error");
        return -1;
    }

    return JNI_VERSION_1_4;


}





