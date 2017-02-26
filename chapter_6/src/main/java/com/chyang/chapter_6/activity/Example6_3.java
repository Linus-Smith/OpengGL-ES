package com.chyang.chapter_6.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.chyang.chapter_6.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL;
import javax.microedition.khronos.opengles.GL10;

public class Example6_3 extends AppCompatActivity {

    private final int CONTEXT_CLIENT_VERSION = 3;
    private GLSurfaceView mGLGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLGlSurfaceView = new GLSurfaceView(this);
        if(detectOpenGLES30()) {
            mGLGlSurfaceView.setEGLContextClientVersion(CONTEXT_CLIENT_VERSION);
            mGLGlSurfaceView.setRenderer(new Example6_3Renderer(this));
        }
        setContentView(mGLGlSurfaceView);

    }

    private boolean detectOpenGLES30() {
        ActivityManager am = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return (info.reqGlEsVersion >= 0x30000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGLGlSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGLGlSurfaceView.onPause();
    }

    private class Example6_3Renderer  implements GLSurfaceView.Renderer {

        private FloatBuffer mVertices;
        private int mProgramValue;
        private int mWidth;
        private int mHeight;

        private final float[] mVerticesData =
                {
                        0.0f, 0.5f, 0.0f,
                        -0.5f,-0.5f, 0.0f,
                        0.5f, -0.5f, 0.0f
                };

        public Example6_3Renderer(Context context) {
              mVertices = ByteBuffer.allocateDirect( mVerticesData.length * 4 )
                      .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mVertices.put((mVerticesData)).position(0);
        }

        private int loaderShader(String shaderSrc, int type) {

            int shader = GLES30.glCreateShader(type);

            if (shader == 0) {
                return 0;
            }

            GLES30.glShaderSource(shader, shaderSrc);
            GLES30.glCompileShader(shader);

            int[] compileState = new int[1];

            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileState, 0 );

            if(compileState[0] == 0) {
                GLES30.glDeleteShader(shader);
                Log.e("linus.yang", GLES30.glGetShaderInfoLog(shader));
                return 0;
            }

            return shader;

        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            String vShaderStr =
                    "#version 300 es              \n" +
                            "layout(location = 0) in vec4 a_color;     \n" +
                            "layout(location = 1) in vec4 a_position;  \n" +
                            "out vec4 v_color;                         \n" +
                            "void main()                               \n" +
                            "{                                         \n" +
                            "        v_color = a_color;                 \n" +
                            "        gl_Position = a_position;          \n" +
                            "}";
            String fShaderStr =
                    "#version 300 es                                 \n" +
                            "precision mediump float;                \n" +
                            "in vec4 v_color;                        \n" +
                            "out vec4 o_fragColor;                   \n" +
                            "void main()                             \n" +
                            "{                                       \n" +
                            "       o_fragColor = v_color;           \n" +
                            "}";

            int vShader = loaderShader(vShaderStr, GLES30.GL_VERTEX_SHADER);
            int fShader = loaderShader(fShaderStr, GLES30.GL_FRAGMENT_SHADER);

            //创建着色程序
           int shaderProgram =  GLES30.glCreateProgram();

            if(shaderProgram == 0) {
                GLES30.glDeleteProgram(shaderProgram);
                Log.e("linus", GLES30.glGetProgramInfoLog(shaderProgram));
                return;
            }


            GLES30.glAttachShader(shaderProgram, vShader);
            GLES30.glAttachShader(shaderProgram, fShader);


            GLES30.glLinkProgram(shaderProgram);

            int[] linkState = new int[1];

            GLES30.glGetProgramiv(shaderProgram, GLES30.GL_LINK_STATUS, linkState, 0);

            if(linkState[0] == 0) {
                GLES30.glDeleteProgram(shaderProgram);
                Log.e("linus", GLES30.glGetProgramInfoLog(shaderProgram));
            }

            int  a = GLES30.glGetAttribLocation(shaderProgram, "a_position");
            System.out.println(a+"=====-");
            mProgramValue = shaderProgram;

            GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        @Override
        public void onDrawFrame(GL10 gl) {

            GLES30.glViewport(0, 0 , mWidth, mHeight);

            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

            GLES30.glUseProgram(mProgramValue);

            //set the vertex color to red
            GLES30.glVertexAttrib4f(0, 1.0f, 0.5f, 0.0f, 1.0f);
            mVertices.position(0);
            GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 0, mVertices);
            GLES30.glEnableVertexAttribArray(1);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
            GLES30.glDisableVertexAttribArray(1);

        }
    }
}
