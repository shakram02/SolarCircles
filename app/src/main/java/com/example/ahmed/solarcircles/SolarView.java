package com.example.ahmed.solarcircles;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.AttributeSet;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ahmed on 10/30/17.
 */

public class SolarView extends GLSurfaceView {

    SolarViewRenderer renderer;

    public SolarView(Context context) {
        super(context);

        this.setEGLContextClientVersion(2);
        renderer = new SolarViewRenderer();
        this.setRenderer(renderer);

        // Render the view only when there is a change in the drawing data
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    private SolarView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public static class SolarViewRenderer implements GLSurfaceView.Renderer {
        private static final String TAG = "Renderer";
        private Triangle mTriangle;
        private Square mSquare;
        // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
        private final float[] mMVPMatrix = new float[16];
        private final float[] mProjectionMatrix = new float[16];
        private final float[] mViewMatrix = new float[16];


        @Override
        public void onSurfaceCreated(GL10 _, EGLConfig eglConfig) {
            // Unless the structure (the original coordinates) of the
            // shapes you use in your program change during the
            // course of execution, you should initialize them in the
            // onSurfaceCreated() method of your renderer for memory and
            // processing efficiency.

            mTriangle = new Triangle();
            mSquare = new Square();


        }

        @Override
        public void onSurfaceChanged(GL10 gl10, int width, int height) {
            GLES20.glViewport(0, 0, width, height);

            float ratio = (float) width / height;

            // this projection matrix is applied to object coordinates
            // in the onDrawFrame() method
            Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
        }

        @Override
        public void onDrawFrame(GL10 _) {
            GLES20.glClearColor(0.15f, 0.15f, 0.15f, 0.15f);
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

            // Set the camera position (View matrix)
            Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3,
                    0f, 0f, 0f, 0f, 1.0f, 0.0f);

            // Calculate the projection and view transformation
            Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

            mTriangle.draw(mMVPMatrix);
        }

        static int loadShader(int type, String shaderCode) {
            // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
            // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
            int shader = GLES20.glCreateShader(type);

            // add the source code to the shader and compile it
            GLES20.glShaderSource(shader, shaderCode);
            GLES20.glCompileShader(shader);

            return shader;
        }

        /**
         * Utility method for debugging OpenGL calls. Provide the name of the call
         * just after making it:
         * <p>
         * <pre>
         * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
         * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
         * <p>
         * If the operation is not successful, the check throws an error.
         *
         * @param glOperation - Name of the OpenGL call to check.
         */
        static void checkGlError(String glOperation) {
            int error;
            while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
                Log.e(TAG, glOperation + ": glError " + error);
                throw new RuntimeException(glOperation + ": glError " + error);
            }
        }
    }
}
