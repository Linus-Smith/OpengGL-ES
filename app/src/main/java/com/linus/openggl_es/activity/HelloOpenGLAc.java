package com.linus.openggl_es.activity;

import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telecom.Log;

import com.linus.openggl_es.R;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class HelloOpenGLAc extends AppCompatActivity implements GLSurfaceView.Renderer{

    private GLSurfaceView glSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_opemgl);
        glSurfaceView = (GLSurfaceView) findViewById(R.id.gf_view);
        glSurfaceView.setRenderer(this);
    }


    //这个方法中主要用来设置一些绘制时不常变化的参数，　比如背景色，是否打开z-buffer等
    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        Log.d("Linus.smith", "onSurfaceCreate");
        //设置背景颜色
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.5f);
        //启动平滑的shading,默认
        gl.glShadeModel(GL10.GL_SMOOTH);
        //Depth buffer setup
        gl.glClearDepthf(1.0f); // openGL docs
        //Enables depth testing
        gl.glDepthFunc(GL10.GL_LEQUAL);
        //Really nice perspective calculations
        gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_NICEST);
    }

    //如果设备支持屏幕横向和纵向，这个方法将发生在横向和纵向切换时，此时可以重新设置绘制比率
    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        Log.d("Linus.smith", "onSurfaceChange");
        //sets the current view port to the new size
        gl.glViewport(0,0, width, height);
        //select the project matrix
        gl.glMatrixMode(GL10.GL_PROJECTION);
        //reset the projection matrix
        gl.glLoadIdentity();
        //Calculate the aspect ratio of the window
        GLU.gluPerspective(gl, 45.0f, (float)width / (float) height, 0.1f, 100.0f );
        gl.glMatrixMode(GL10.GL_MODELVIEW);
        gl.glLoadIdentity();
    }

    //定义实际的绘图操作
    @Override
    public void onDrawFrame(GL10 gl) {
        Log.d("Linus.smith", "onDrawFrame");
        //clears the screen and depth buffer
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
    }
}
