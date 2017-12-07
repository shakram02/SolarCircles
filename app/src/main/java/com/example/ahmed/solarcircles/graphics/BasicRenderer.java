package com.example.ahmed.solarcircles.graphics;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.ahmed.solarcircles.graphics.gl_internals.CircleMaker;
import com.example.ahmed.solarcircles.graphics.gl_internals.FrustumManager;
import com.example.ahmed.solarcircles.graphics.gl_internals.memory.GLProgram;
import com.example.ahmed.solarcircles.graphics.gl_internals.memory.VertexArray;
import com.example.ahmed.solarcircles.graphics.gl_internals.memory.VertexBufferObject;
import com.example.ahmed.solarcircles.graphics.utils.ValueLimiter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ahmed on 11/19/17.
 * <p>
 * OpenGL lesson 1
 */

public class BasicRenderer implements GLSurfaceView.Renderer {

    private VertexArray circleVertices;
    private String colorVariableName = "v_Color";
    /**
     * Store the view matrix. This can be thought of as our camera. This matrix transforms world space to eye space;
     * it positions things relative to our eye.
     */
    private float[] mViewMatrix = new float[16];

    /**
     * Store the projection matrix. This is used to project the scene onto a 2D viewport.
     */
    private float[] mProjectionMatrix = new float[16];

    /**
     * Store the model matrix. This matrix is used to move models
     * from object space (where each model can be thought
     * of being located at the center of the universe) to world space.
     */
    private float[] mModelMatrix = new float[16];
    private float earthColor[] = {0.13671875f, 0.26953125f, 0.92265625f, 0.0f};
    private float sunColor[] = {0.93671875f, 0.76953125f, 0.12265625f, 0.0f};
    private float moonColor[] = {0.93671875f, 0.73671875f, 0.63671875f, 0.0f};

    private VertexBufferObject colorVbo;

    public BasicRenderer() {

    }

    private GLProgram program;
    private String mvpMatrixVariableName = "u_MVPMatrix";
    private VertexBufferObject verticesVbo;

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        // Set the background clear earthColor to gray.
        GLES20.glClearColor(0.15f, 0.15f, 0.15f, 0.15f);

        // Position the eye behind the origin.
        final float eyeX = 0.0f;
        final float eyeY = 0.0f;
        final float eyeZ = 1.5f;

        // We are looking toward the distance
        final float lookX = 0.0f;
        final float lookY = 0.0f;
        final float lookZ = -5.0f;

        // Set our up vector. This is where our head would be pointing were we holding the camera.
        final float upX = 0.0f;
        final float upY = 1.0f;
        final float upZ = 0.0f;

        Matrix.setLookAtM(mViewMatrix, 0, eyeX, eyeY, eyeZ, lookX,
                lookY, lookZ, upX, upY, upZ);

        String vertexShader = "" +
                "uniform mat4 u_MVPMatrix;      \n"
                + "attribute vec4 a_Position;     \n"

                + "void main()                    \n"
                + "{                              \n"
                + "   gl_Position = u_MVPMatrix * a_Position;   \n"
                + "}";

        String fragmentShader = "precision mediump float;       \n"
                + "uniform vec4 v_Color;          \n"
                + "void main()                    \n"
                + "{                              \n"
                + "   gl_FragColor = v_Color;     \n"
                + "}                              \n";

        program = new GLProgram(vertexShader, fragmentShader);
        String positionVariableName = "a_Position";

        program.declareAttribute(positionVariableName);
        program.declareUniform(colorVariableName);
        program.declareUniform(mvpMatrixVariableName);

        // Tell OpenGL to use this program when rendering.
        program.activate();


        int pointCount = 64;
        float radius = 0.321f;

//        DrawableObject modelDrawableObject = new DrawableObject(pointCount, radius, 0f, 0f);
        float circleVertices[] = CircleMaker.CreateCirclePoints(0, 0, 0.3f, 120);

        this.circleVertices = new VertexArray(circleVertices,
                program.getVariableHandle(positionVariableName), true);

        verticesVbo = new VertexBufferObject(this.circleVertices);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mProjectionMatrix = FrustumManager.createFrustum(0, 0, width, height);
    }

    private ValueLimiter horizontalLimiter = new ValueLimiter(0, -1, 1, 0.08f);
    private ValueLimiter verticalLimiter = new ValueLimiter(0, -1, 1, 0.08f);


    void moveRight() {
        horizontalLimiter.increment();
    }

    void moveLeft() {
        horizontalLimiter.decrement();
    }

    void moveUp() {
        verticalLimiter.increment();
    }

    void moveDown() {
        verticalLimiter.decrement();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);

        // Do a complete rotation every 10 seconds.
        long time = SystemClock.uptimeMillis() % 1000000L;
        float angleInDegrees = (360.0f / 10000.0f) * ((int) time);


        float rotationRatio = time / 2500.0f;
        // Draw the triangle facing straight on.
        float deltaX = (float) (2 * Math.sin(rotationRatio * Math.PI));
        float deltaY = (float) (2 * Math.cos(rotationRatio * Math.PI));

        //EARTH
        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.scaleM(mModelMatrix, 0, 0.5f, 0.5f, 0);
        Matrix.rotateM(mModelMatrix, 0, angleInDegrees, 0.0f, 0.0f, 1.0f);
        Matrix.translateM(mModelMatrix, 0, deltaX, deltaY, 0);
        GLES20.glUniform4fv(program.getVariableHandle(colorVariableName), 1, earthColor, 0);
        drawCircle();

        //MOON
        // Draw the triangle facing straight on.
        float deltaXM = (float) (deltaX / Math.sin(rotationRatio * Math.PI));
        float deltaYM = (float) (deltaY / Math.cos(rotationRatio * Math.PI));
        GLES20.glUniform4fv(program.getVariableHandle(colorVariableName), 1, moonColor, 0);
        Matrix.scaleM(mModelMatrix, 0, 0.3f, 0.3f, 0);
        Matrix.translateM(mModelMatrix, 0, deltaXM / 2f, deltaYM, 0);
//         Matrix.rotateM(mModelMatrix, 0,45, deltaX,deltaX, 0 );
        drawCircle();

        //SUN
        Matrix.setIdentityM(mModelMatrix, 0);
        GLES20.glUniform4fv(program.getVariableHandle(colorVariableName), 1, sunColor, 0);
        drawCircle();
    }

    /**
     * Allocate storage for the final combined matrix.
     * This will be passed into the shader program.
     */
    private float[] mMVPMatrix = new float[16];

    private void drawCircle() {
        verticesVbo.startDraw();

        // This multiplies the view matrix by the model matrix, and stores the
        // result in the MVP matrix (which currently contains model * view).
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);

        // This multiplies the modelview matrix by the projection matrix, and stores the result in the MVP matrix
        // (which now contains model * view * projection).
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);

        // Send the matrix to shaders
        GLES20.glUniformMatrix4fv(program.getVariableHandle(mvpMatrixVariableName),
                1, false, mMVPMatrix, 0);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, verticesVbo.getItemCount());

        verticesVbo.endDraw();
    }
}
