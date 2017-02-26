package com.chyang.chapter_6.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.chyang.chapter_6.R;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class VAOActivity extends AppCompatActivity {

    private final int CONTEXT_CLIENT_VERSION = 3;

    private GLSurfaceView mGLSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGLSurfaceView = new GLSurfaceView(this);

        if (detectOpenGLES30()) {
            // Tell the surface view we want to create an OpenGL ES 3.0-compatible
            // context, and set an OpenGL ES 3.0-compatible renderer.
            mGLSurfaceView.setEGLContextClientVersion(CONTEXT_CLIENT_VERSION);
            mGLSurfaceView.setRenderer(new VAORenderer(this));
        } else {
            Log.e("SimpleTexture2D", "OpenGL ES 3.0 not supported on device.  Exiting...");
            finish();

        }

        setContentView(mGLSurfaceView);
    }

    private boolean detectOpenGLES30() {
        ActivityManager am =
                (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return (info.reqGlEsVersion >= 0x30000);
    }

    @Override
    protected void onResume() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onResume();
        mGLSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        // Ideally a game should implement onResume() and onPause()
        // to take appropriate action when the activity looses focus
        super.onPause();
        mGLSurfaceView.onPause();
    }


    private class VAORenderer implements GLSurfaceView.Renderer {

        ///
        // Constructor
        //
        public VAORenderer(Context context) {
            mVertices = ByteBuffer.allocateDirect(mVerticesData.length * 4)
                    .order(ByteOrder.nativeOrder()).asFloatBuffer();
            mVertices.put(mVerticesData).position(0);

            mIndices = ByteBuffer.allocateDirect(mIndicesData.length * 2)
                    .order(ByteOrder.nativeOrder()).asShortBuffer();
            mIndices.put(mIndicesData).position(0);
        }

        ///
        // Initialize the shader and program object
        //
        public void onSurfaceCreated(GL10 glUnused, EGLConfig config) {
            String vShaderStr =
                    "#version 300 es                            \n" +
                            "layout(location = 0) in vec4 a_position;   \n" +
                            "layout(location = 1) in vec4 a_color;      \n" +
                            "out vec4 v_color;                          \n" +
                            "void main()                                \n" +
                            "{                                          \n" +
                            "    v_color = a_color;                     \n" +
                            "    gl_Position = a_position;              \n" +
                            "}";


            String fShaderStr =
                    "#version 300 es            \n" +
                            "precision mediump float;   \n" +
                            "in vec4 v_color;           \n" +
                            "out vec4 o_fragColor;      \n" +
                            "void main()                \n" +
                            "{                          \n" +
                            "    o_fragColor = v_color; \n" +
                            "}";

            // Load the shaders and get a linked program object
            mProgramObject =loadProgram(vShaderStr, fShaderStr);

            // Generate VBO Ids and load the VBOs with data
            GLES30.glGenBuffers(2, mVBOIds, 0);

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOIds[0]);

            mVertices.position(0);
            GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, mVerticesData.length * 4,
                    mVertices, GLES30.GL_STATIC_DRAW);

            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[1]);

            mIndices.position(0);
            GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, 2 * mIndicesData.length,
                    mIndices, GLES30.GL_STATIC_DRAW);

            // Generate VAO Id
            GLES30.glGenVertexArrays(1, mVAOId, 0);

            // Bind the VAO and then setup the vertex
            // attributes
            GLES30.glBindVertexArray(mVAOId[0]);

            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, mVBOIds[0]);
            GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[1]);

            GLES30.glEnableVertexAttribArray(VERTEX_POS_INDX);
            GLES30.glEnableVertexAttribArray(VERTEX_COLOR_INDX);

            GLES30.glVertexAttribPointer(VERTEX_POS_INDX, VERTEX_POS_SIZE,
                    GLES30.GL_FLOAT, false, VERTEX_STRIDE,
                    0);

            GLES30.glVertexAttribPointer(VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                    GLES30.GL_FLOAT, false, VERTEX_STRIDE,
                    (VERTEX_POS_SIZE * 4));

            // Reset to the default VAO
            GLES30.glBindVertexArray(0);

            GLES30.glClearColor(1.0f, 1.0f, 1.0f, 0.0f);
        }

        // /
        // Draw a triangle using the shader pair created in onSurfaceCreated()
        //
        public void onDrawFrame(GL10 glUnused) {
            // Set the viewport
            GLES30.glViewport(0, 0, mWidth, mHeight);

            // Clear the color buffer
            GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT);

            // Use the program object
            GLES30.glUseProgram(mProgramObject);

            // Bind the VAO
            GLES30.glBindVertexArray(mVAOId[0]);

            // Draw with the VAO settings
            GLES30.glDrawElements(GLES30.GL_TRIANGLES, mIndicesData.length, GLES30.GL_UNSIGNED_SHORT, 0);

            // Return to the default VAO
            GLES30.glBindVertexArray(0);
        }

        ///
        // Handle surface changes
        //
        public void onSurfaceChanged(GL10 glUnused, int width, int height) {
            mWidth = width;
            mHeight = height;
        }

        // Handle to a program object
        private int mProgramObject;

        // Additional member variables
        private int mWidth;
        private int mHeight;
        private FloatBuffer mVertices;
        private ShortBuffer mIndices;

        // VertexBufferObject Ids
        private int[] mVBOIds = new int[2];

        // VertexArrayObject Id
        private int[] mVAOId = new int[1];

        // 3 vertices, with (x,y,z) ,(r, g, b, a) per-vertex
        private final float[] mVerticesData =
                {
                        0.0f, 0.5f, 0.0f,        // v0
                        1.0f, 0.0f, 0.0f, 1.0f,  // c0
                        -0.5f, -0.5f, 0.0f,        // v1
                        0.0f, 1.0f, 0.0f, 1.0f,  // c1
                        0.5f, -0.5f, 0.0f,        // v2
                        0.0f, 0.0f, 1.0f, 1.0f,  // c2
                };

        private final short[] mIndicesData =
                {
                        0, 1, 2
                };

        final int VERTEX_POS_SIZE = 3; // x, y and z
        final int VERTEX_COLOR_SIZE = 4; // r, g, b, and a

        final int VERTEX_POS_INDX = 0;
        final int VERTEX_COLOR_INDX = 1;

        final int VERTEX_STRIDE = (4 * (VERTEX_POS_SIZE + VERTEX_COLOR_SIZE));

    }


    //
    ///
    /// \brief Load a shader, check for compile errors, print error messages to
    /// output log
    /// \param type Type of shader (GL_VERTEX_SHADER or GL_FRAGMENT_SHADER)
    /// \param shaderSrc Shader source string
    /// \return A new shader object on success, 0 on failure
    //
    public  int loadShader ( int type, String shaderSrc )
    {
        int shader;
        int[] compiled = new int[1];

        // Create the shader object
        shader = GLES30.glCreateShader ( type );

        if ( shader == 0 )
        {
            return 0;
        }

        // Load the shader source
        GLES30.glShaderSource ( shader, shaderSrc );

        // Compile the shader
        GLES30.glCompileShader ( shader );

        // Check the compile status
        GLES30.glGetShaderiv ( shader, GLES30.GL_COMPILE_STATUS, compiled, 0 );

        if ( compiled[0] == 0 )
        {
            Log.e ( "ESShader", GLES30.glGetShaderInfoLog ( shader ) );
            GLES30.glDeleteShader ( shader );
            return 0;
        }

        return shader;
    }

    //
    ///
    /// \brief Load a vertex and fragment shader, create a program object, link
    ///    program.
    /// Errors output to log.
    /// \param vertShaderSrc Vertex shader source code
    /// \param fragShaderSrc Fragment shader source code
    /// \return A new program object linked with the vertex/fragment shader
    ///    pair, 0 on failure
    //
    public  int loadProgram ( String vertShaderSrc, String fragShaderSrc )
    {
        int vertexShader;
        int fragmentShader;
        int programObject;
        int[] linked = new int[1];

        // Load the vertex/fragment shaders
        vertexShader = loadShader ( GLES30.GL_VERTEX_SHADER, vertShaderSrc );

        if ( vertexShader == 0 )
        {
            return 0;
        }

        fragmentShader = loadShader ( GLES30.GL_FRAGMENT_SHADER, fragShaderSrc );

        if ( fragmentShader == 0 )
        {
            GLES30.glDeleteShader ( vertexShader );
            return 0;
        }

        // Create the program object
        programObject = GLES30.glCreateProgram();

        if ( programObject == 0 )
        {
            return 0;
        }

        GLES30.glAttachShader ( programObject, vertexShader );
        GLES30.glAttachShader ( programObject, fragmentShader );

        // Link the program
        GLES30.glLinkProgram ( programObject );

        // Check the link status
        GLES30.glGetProgramiv ( programObject, GLES30.GL_LINK_STATUS, linked, 0 );

        if ( linked[0] == 0 )
        {
            Log.e ( "ESShader", "Error linking program:" );
            Log.e ( "ESShader", GLES30.glGetProgramInfoLog ( programObject ) );
            GLES30.glDeleteProgram ( programObject );
            return 0;
        }

        // Free up no longer needed shader resources
        GLES30.glDeleteShader ( vertexShader );
        GLES30.glDeleteShader ( fragmentShader );

        return programObject;
    }
}
