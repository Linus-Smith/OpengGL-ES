package com.chyang.activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class Example6_6 extends AppCompatActivity {

    private final int CONTEXT_CLIENT_VERSION = 3;
    private GLSurfaceView mSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSurfaceView = new GLSurfaceView(this);

        if (detectOpenGLES30()) {
            mSurfaceView.setEGLContextClientVersion(CONTEXT_CLIENT_VERSION);
            mSurfaceView.setRenderer(new Example6_3Renderer(this));
        }

        setContentView(mSurfaceView);
    }

    private boolean detectOpenGLES30() {
        ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        ConfigurationInfo info = am.getDeviceConfigurationInfo();
        return info.reqGlEsVersion >= 0x3000;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSurfaceView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSurfaceView.onPause();
    }

    private class Example6_3Renderer implements GLSurfaceView.Renderer {

        ///
        // Constructor
        //
        public Example6_3Renderer ( Context context )
        {
            mVertices = ByteBuffer.allocateDirect ( mVerticesData.length * 4 )
                    .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
            mVertices.put ( mVerticesData ).position ( 0 );

            mColors = ByteBuffer.allocateDirect ( mColorData.length * 4 )
                    .order ( ByteOrder.nativeOrder() ).asFloatBuffer();
            mColors.put ( mColorData ).position ( 0 );

            mIndices = ByteBuffer.allocateDirect ( mIndicesData.length * 2 )
                    .order ( ByteOrder.nativeOrder() ).asShortBuffer();
            mIndices.put ( mIndicesData ).position ( 0 );
        }

        ///
        // Initialize the shader and program object
        //
        public void onSurfaceCreated ( GL10 glUnused, EGLConfig config )
        {
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
                            "}" ;

            // Load the shaders and get a linked program object
            mProgramObject = loadProgram ( vShaderStr, fShaderStr );

            mVBOIds[0] = 0;
            mVBOIds[1] = 0;
            mVBOIds[2] = 0;

            GLES30.glClearColor ( 1.0f, 1.0f, 1.0f, 0.0f );
        }

        // /
        // Draw a triangle using the shader pair created in onSurfaceCreated()
        //
        public void onDrawFrame ( GL10 glUnused )
        {
            // Set the viewport
            GLES30.glViewport ( 0, 0, mWidth, mHeight );

            // Clear the color buffer
            GLES30.glClear ( GLES30.GL_COLOR_BUFFER_BIT );

            // Use the program object
            GLES30.glUseProgram ( mProgramObject );

            drawPrimitiveWithVBOs();
        }

        private void drawPrimitiveWithVBOs()
        {
            int numVertices = 3;
            int numIndices = 3;

            // mVBOIds[0] - used to store vertex position
            // mVBOIds[1] - used to store vertex color
            // mVBOIds[2] - used to store element indices
            if ( mVBOIds[0] == 0 && mVBOIds[1] == 0 && mVBOIds[2] == 0 )
            {
                // Only allocate on the first draw
                GLES30.glGenBuffers ( 3, mVBOIds, 0 );

                mVertices.position ( 0 );
                GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOIds[0] );
                GLES30.glBufferData ( GLES30.GL_ARRAY_BUFFER, vtxStrides[0] * numVertices,
                        mVertices, GLES30.GL_STATIC_DRAW );

                mColors.position ( 0 );
                GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOIds[1] );
                GLES30.glBufferData ( GLES30.GL_ARRAY_BUFFER, vtxStrides[1] * numVertices,
                        mColors, GLES30.GL_STATIC_DRAW );

                mIndices.position ( 0 );
                GLES30.glBindBuffer ( GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[2] );
                GLES30.glBufferData ( GLES30.GL_ELEMENT_ARRAY_BUFFER, 2 * numIndices,
                        mIndices, GLES30.GL_STATIC_DRAW );
            }

            GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOIds[0] );

            GLES30.glEnableVertexAttribArray ( VERTEX_POS_INDX );
            GLES30.glVertexAttribPointer ( VERTEX_POS_INDX, VERTEX_POS_SIZE,
                    GLES30.GL_FLOAT, false, vtxStrides[0], 0 );

            GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, mVBOIds[1] );

            GLES30.glEnableVertexAttribArray ( VERTEX_COLOR_INDX );
            GLES30.glVertexAttribPointer ( VERTEX_COLOR_INDX, VERTEX_COLOR_SIZE,
                    GLES30.GL_FLOAT, false, vtxStrides[1], 0 );

            GLES30.glBindBuffer ( GLES30.GL_ELEMENT_ARRAY_BUFFER, mVBOIds[2] );

            GLES30.glDrawElements ( GLES30.GL_TRIANGLES, numIndices,
                    GLES30.GL_UNSIGNED_SHORT, 0 );

            GLES30.glDisableVertexAttribArray ( VERTEX_POS_INDX );
            GLES30.glDisableVertexAttribArray ( VERTEX_COLOR_INDX );

            GLES30.glBindBuffer ( GLES30.GL_ARRAY_BUFFER, 0 );
            GLES30.glBindBuffer ( GLES30.GL_ELEMENT_ARRAY_BUFFER, 0 );
        }

        ///
        // Handle surface changes
        //
        public void onSurfaceChanged ( GL10 glUnused, int width, int height )
        {
            mWidth = width;
            mHeight = height;
        }

        // Handle to a program object
        private int mProgramObject;

        // Additional member variables
        private int mWidth;
        private int mHeight;
        private FloatBuffer mVertices;
        private FloatBuffer mColors;
        private ShortBuffer mIndices;

        // VertexBufferObject Ids
        private int [] mVBOIds = new int[3];

        // 3 vertices, with (x,y,z) ,(r, g, b, a) per-vertex
        private final float[] mVerticesData =
                {
                        0.0f,  0.5f, 0.0f, // v0
                        -0.5f, -0.5f, 0.0f, // v1
                        0.5f, -0.5f, 0.0f  // v2
                };

        private final short[] mIndicesData =
                {
                        0, 1, 2
                };

        private final float [] mColorData =
                {
                        1.0f, 0.0f, 0.0f, 1.0f,   // c0
                        0.0f, 1.0f, 0.0f, 1.0f,   // c1
                        0.0f, 0.0f, 1.0f, 1.0f    // c2
                };

        final int VERTEX_POS_SIZE   = 3; // x, y and z
        final int VERTEX_COLOR_SIZE = 4; // r, g, b, and a

        final int VERTEX_POS_INDX   = 0;
        final int VERTEX_COLOR_INDX = 1;

        private int vtxStrides[] =
                {
                        VERTEX_POS_SIZE * 4,
                        VERTEX_COLOR_SIZE * 4
                };
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
