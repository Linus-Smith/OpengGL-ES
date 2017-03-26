package com.chyang.lookopenglapidome;

import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLDisplay;
import android.opengl.EGLSurface;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import javax.microedition.khronos.egl.EGL;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  setContentView(R.layout.activity_main);
      intEGL14();
    }


    private void intEGL14() {
        //创建显示连接
        EGLDisplay eglDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
        if(eglDisplay == EGL14.EGL_NO_DISPLAY) {
            Log.d(TAG, "显示连接不成功");
        } else {
            Log.d(TAG, "显示链接成功");
        }

        //初始化显示链接

        int[] major = new int[1];
        int majorOffset = 0;
        int[] minor = new int[1];
        int minorOffset = 0;

        boolean isSucceed = EGL14.eglInitialize(eglDisplay, major, majorOffset, minor, minorOffset);
        if(isSucceed) {
            Log.d(TAG, "major:"+major[0] +"    majorOffset:"+ majorOffset +"   minor:"+minor[0] +"    minorOffset:"+minorOffset +" queryString:"+ EGL14.eglQueryString(eglDisplay, EGL14.EGL_VERSION));
        }

        int[] attrid_list = {
          EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_BIT,
                EGL14.EGL_RED_SIZE, 5,
                EGL14.EGL_GREEN_SIZE, 6,
                EGL14.EGL_BLUE_SIZE, 5,
                EGL14.EGL_NONE
        };


        final int maxConfigs = 10;
        EGLConfig[] configs = new EGLConfig[maxConfigs];
        int[] mun = new int[1];
        EGL14.eglChooseConfig(eglDisplay, attrid_list, 0, configs, 0, maxConfigs, mun, 0);


        int[] attribList = {
                EGL14.EGL_RENDER_BUFFER, EGL14.EGL_BACK_BUFFER
        };
       EGLSurface eglSurface = EGL14.eglCreateWindowSurface(eglDisplay,configs[0], null, attribList, 0);
        System.out.println(eglSurface+"======== ");
    }
}
