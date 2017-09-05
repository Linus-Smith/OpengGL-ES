//
// Created by a4004923 on 17-9-4.
//

#include <jni.h>
#include <string>
#include <android/log.h>
#include <GLES/gl.h>
#include "common/esUtil.h"

#ifndef OPENGGL_ES_NATIVEDRAW_H
#define OPENGGL_ES_NATIVEDRAW_H

#endif //OPENGGL_ES_NATIVEDRAW_H


static const char *kClassName = "com/example/opengesnative/MainActivity";

#define  LOGD(...) __android_log_print(ANDROID_LOG_DEBUG, "linus" , __VA_ARGS__);


GLint mProgram;

void onSurfaceCreated();

void onSurfaceChanged(JNIEnv env, jobject obj, int width, int height);

void onDrawFrame();

static const JNINativeMethod gMethod[] = {
        {"onSurfaceCreated", "()V",   (void *) onSurfaceCreated},
        {"onSurfaceChanged", "(II)V", (void *) onSurfaceChanged},
        {"onDrawFrame",      "()V",   (void *) onDrawFrame}
};



static const char * vShader = {
        "#version 300 es                  \n"
                "uniform mat4 uMVPMatrix;"
                "layout (location = 0) in vec3  aPosition;    \n"
                "layout (location = 1) in vec4 aColor;       \n"
                "out vec4 vColor;"
                "void main() {                              \n"
                "   gl_Position =  uMVPMatrix * vec4(aPosition, 1); \n"
                "   vColor = aColor;                                \n"
                "}    \n"
};


static const char * fShader = {
        "#version 300 es                                            \n"
                "precision mediump float;                           \n"
                "in vec4 vColor;                                     \n"
                "out vec4 fragColor;                                  \n"
                "void main(){                                          \n"
                "    fragColor = vColor;                               \n"
                " }"
};

struct ShdaderIndex {
    GLint mvpMatrix;
    GLint position;
    GLint color;
} shIndex;

struct VertexData {
    GLfloat  x;
    GLfloat  y;
    GLfloat  z;
};

struct ShaderData {
    VertexData vertexData;
};


struct ShaderData shaderData[] = {
        {-0.5f, 0.5f, 0.0f},
        {0.5f, 0.5f, 0.0f},
        {-0.5f, -0.5f, 0.0f},
        {0.5f, -0.5f, 0.0f}
};

ESMatrix * mModeMatrix;
ESMatrix * mPMatrox;


GLuint * bufferId;
static const int BUFFER_COUNT = 1;

GLuint * vaoId;
static const int VAO_COUNT = 1;

JNIEXPORT jint JNI_OnLoad(JavaVM *vm, void *reserved) {
    JNIEnv *env = NULL;

    if (((*vm).GetEnv((void **) &env, JNI_VERSION_1_4)) != JNI_OK) {
        return -1;
    }

    jclass clazz = (*env).FindClass(kClassName);

    if (clazz == NULL) {
        LOGD("clazz == NULL")
        return -1;
    }

    if ((*env).RegisterNatives(clazz, gMethod, sizeof(gMethod) / sizeof(gMethod[0])) != JNI_OK) {
        LOGD("register natives error");
        return -1;
    }
    return JNI_VERSION_1_4;
}
