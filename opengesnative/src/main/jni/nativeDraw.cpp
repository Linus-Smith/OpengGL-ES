
#include "nativeDraw.h"



void onSurfaceCreated() {
   mProgram = esLoadProgram(vShader, fShader);
}


void onSurfaceChanged(JNIEnv env, jobject obj, int width, int height) {
    LOGD("onSurfaceChanged %d  %d", width, height);
}

void onDrawFrame() {
    // LOGD("onDrawFrddame");
}






