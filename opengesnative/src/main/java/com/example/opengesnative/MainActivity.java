package com.example.opengesnative;

import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class MainActivity extends AppCompatActivity {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    private GLSurfaceView mGLSurface;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGLSurface = (GLSurfaceView) findViewById(R.id.gl_view);
        mGLSurface.setEGLContextClientVersion(3);
        mGLSurface.setRenderer(new GLRenderer());
    }



    class GLRenderer implements GLSurfaceView.Renderer {

        @Override
        public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
            MainActivity.this.onSurfaceCreated();
        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int i, int i1) {
            MainActivity.this.onSurfaceChanged(i, i1);
        }

        @Override
        public void onDrawFrame(GL10 gl10) {
            MainActivity.this.onDrawFrame();
        }
    }

    private native void onSurfaceCreated();
    private native void onSurfaceChanged(int width, int height);
    private native void onDrawFrame();


}
