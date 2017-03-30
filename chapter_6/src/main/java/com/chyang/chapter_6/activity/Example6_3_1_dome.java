package com.chyang.chapter_6.activity;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.chyang.chapter_6.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Random;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Example6_3_1_dome extends AppCompatActivity {

    GLSurfaceView mGlSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGlSurfaceView = new GLSurfaceView(this);
        mGlSurfaceView.setEGLContextClientVersion(3);
        mGlSurfaceView.setRenderer(new MyRender(this));
        setContentView(mGlSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mGlSurfaceView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mGlSurfaceView.onResume();
    }

    class MyRender implements GLSurfaceView.Renderer {

        private FloatBuffer floatBuffer;
        private int mProgramValue;
        private int mWidth;
        private int mheight;
        private float[] verticesData = new float[] {
                0.0f, 1.0f, -1.0f,
                0.1f,  -1.0f, -1.0f,
                -1.0f, -1.0f,  0.0f,
        };

        public MyRender(Context context) {
            floatBuffer =  ByteBuffer.allocateDirect( verticesData.length * 4 )
                    .order(ByteOrder.nativeOrder())
                    .asFloatBuffer();
            floatBuffer.put(verticesData).position(0);
        }

        private int loaderShader(String shaderSrc, int type) {

            //创建着色器引用
            int shader = GLES30.glCreateShader(type);
            if(shader == 0) {
                return 0;
            }

            GLES30.glShaderSource(shader, shaderSrc);
            GLES30.glCompileShader(shader);

            int[] compileState = new int[1];
            GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileState, 0);
            if(compileState[0] == 0) {
                GLES30.glDeleteShader(shader);
                Log.d("linus", GLES30.glGetShaderInfoLog(shader));
                return 0;
            }

            return shader;
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

            String vShaderStr =
                    "#version 300 es 			  \n"
                            +" in vec4 aColor;            \n"
                            + "in vec4 vPosition;           \n"
                            + "out vec4 vColor;             \n"
                            + "void main()                  \n"
                            + "{                            \n"
                            + "    vColor = aColor;         \n"
                            + "   gl_Position = vPosition;  \n"
                            + "   gl_PointSize = 30.0;       \n"
                            + "}                            \n";

            String fShaderStr =
                    "#version 300 es		 			          	\n"
                            + "precision mediump float;					  	\n"
                            + "in vec4 vColor;                              \n "
                            + "out vec4 fragColor;	 			 		  	\n"
                            + "void main()                                  \n"
                            + "{                                            \n"
                            + "  fragColor = vColor;                    	\n"
                            + "}                                            \n";

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

            mProgramValue = shaderProgram;

            GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);

            //查询支持的最大顶点数
            int[] params = new int[1];
            GLES30.glGetIntegerv(GLES30.GL_MAX_VERTEX_ATTRIBS, params, 0);

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            mWidth = width;
            mheight = height;
        }

        @Override
        public void onDrawFrame(GL10 gl) {
            GLES30.glViewport(0, 0, mWidth, mheight);
            GLES30.glDepthRangef(1.0f, 1.0f );
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);
            GLES30.glUseProgram(mProgramValue);
            GLES30.glVertexAttrib4f(0, new Random().nextFloat(), 0.4f, 0.5f, 0.5f);
            GLES30.glVertexAttribPointer(1, 3, GLES30.GL_FLOAT, false, 0, floatBuffer);
            GLES30.glEnableVertexAttribArray(1);
            GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, 3);
            GLES30.glDisableVertexAttribArray(1);
            int e = GLES30.glGetError();
            System.out.println((e == GLES30.GL_NO_ERROR)+"=====");
        }
    }
}
