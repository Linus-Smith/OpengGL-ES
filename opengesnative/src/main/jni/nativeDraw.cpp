
#include "nativeDraw.h"



void onSurfaceCreated() {
   mProgram = esLoadProgram(vShader, fShader);
   shIndex.mvpMatrix = glGetUniformLocation(mProgram, "uMVPMatrix");
   shIndex.position = glGetAttribLocation(mProgram, "aPosition");
   shIndex.color = glGetAttribLocation(mProgram, "aColor");
    bufferId = (GLuint *) malloc(BUFFER_COUNT);
    vaoId = (GLuint *)malloc(VAO_COUNT);
    glGenBuffers(BUFFER_COUNT, bufferId);
    glBindBuffer(GL_ARRAY_BUFFER, bufferId[0]);
    glBufferData(GL_ARRAY_BUFFER, sizeof(shaderData), shaderData, GL_STATIC_DRAW);

}


void onSurfaceChanged(JNIEnv env, jobject obj, int width, int height) {
    LOGD("onSurfaceChanged %d  %d", width, height);
    glViewport(0, 0, width, height);
    glGenVertexArrays(VAO_COUNT, vaoId);
    glBindVertexArray(vaoId[0]);
    glBindBuffer(GL_ARRAY_BUFFER, bufferId[0]);
    glEnableVertexAttribArray(shIndex.position);
    glVertexAttribPointer(shIndex.position, 3, GL_FLOAT, GL_FALSE, sizeof(shaderData[0]), 0);
    glVertexAttrib4f(shIndex.color, 1.0f, 0.4f, 0.6f, 0.4f);
    glBindVertexArray(0);
}


void onDrawFrame() {
    glClear(GL_COLOR_BUFFER_BIT |GL_DEPTH_BUFFER_BIT);

    glUseProgram(mProgram);
    glBindVertexArray(vaoId[0]);
    glDrawArrays(GL_TRIANGLE_STRIP, 0, 4);
    glBindVertexArray(0);
}






