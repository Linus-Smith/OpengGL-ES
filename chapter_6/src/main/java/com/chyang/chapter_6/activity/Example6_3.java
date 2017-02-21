package com.chyang.chapter_6.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.chyang.chapter_6.R;

import javax.microedition.khronos.egl.EGLConfig;
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
            mGLGlSurfaceView.setRenderer(new Example6_3Renderer());
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

        public Example6_3Renderer(Context context) {
            
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {

        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {

        }

        @Override
        public void onDrawFrame(GL10 gl) {

        }
    }
}
