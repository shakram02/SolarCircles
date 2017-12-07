package com.example.ahmed.solarcircles.graphics;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;

import com.example.ahmed.solarcircles.graphics.gl_internals.CircleMaker;
import com.example.ahmed.solarcircles.graphics.gl_internals.FrustumManager;
import com.example.ahmed.solarcircles.graphics.gl_internals.UniformPainter;
import com.example.ahmed.solarcircles.graphics.gl_internals.memory.GLProgram;
import com.example.ahmed.solarcircles.graphics.gl_internals.memory.VertexBufferObject;
import com.example.ahmed.solarcircles.graphics.gl_internals.shapes.Axis;
import com.example.ahmed.solarcircles.graphics.gl_internals.shapes.Circle;
import com.example.ahmed.solarcircles.graphics.utils.ValueLimiter;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by ahmed on 11/19/17.
 * <p>
 * OpenGL lesson 1
 */

public class BasicRenderer implements GLSurfaceView.Renderer {
    private final int XYZ_POINT_LENGTH = 3;
    private Circle moonCircle;
    private Circle earthCircle;
    private Circle sunCircle;

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

        float earthColor[] = {0.13671875f, 0.26953125f, 0.92265625f, 0.0f};
        float sunColor[] = {0.93671875f, 0.76953125f, 0.12265625f, 0.0f};
        float moonColor[] = {0.93671875f, 0.73671875f, 0.63671875f, 0.0f};
        float[] mViewMatrix = new float[16];

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

        GLProgram program = new GLProgram(vertexShader, fragmentShader);
        String positionVariableName = "a_Position";

        program.declareAttribute(positionVariableName);
        String colorVariableName = "v_Color";
        program.declareUniform(colorVariableName);
        String mvpMatrixVariableName = "u_MVPMatrix";
        program.declareUniform(mvpMatrixVariableName);

        // Tell OpenGL to use this program when rendering.
        program.activate();

        float circlePoints[] = CircleMaker.CreateCirclePoints(0, 0, 0.3f, 120);
        Integer colorHandle = program.getVariableHandle(colorVariableName);

        UniformPainter moonPainter = new UniformPainter(colorHandle, moonColor);
        UniformPainter earthPainter = new UniformPainter(colorHandle, earthColor);
        UniformPainter sunPainter = new UniformPainter(colorHandle, sunColor);

        VertexBufferObject circleVertices = new VertexBufferObject(circlePoints,
                program.getVariableHandle(positionVariableName), XYZ_POINT_LENGTH);

        Integer mvpHandle = program.getVariableHandle(mvpMatrixVariableName);
        sunCircle = new Circle(mViewMatrix, mvpHandle, circleVertices, sunPainter);
        moonCircle = new Circle(mViewMatrix, mvpHandle, circleVertices, moonPainter);
        earthCircle = new Circle(mViewMatrix, mvpHandle, circleVertices, earthPainter);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        //Store the projection matrix. This is used to project the scene onto a 2D viewport.
        float[] mProjectionMatrix = FrustumManager.createFrustum(0, 0, width, height);

        moonCircle.setProjectionMatrix(mProjectionMatrix);
        earthCircle.setProjectionMatrix(mProjectionMatrix);
        sunCircle.setProjectionMatrix(mProjectionMatrix);
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

        earthCircle.resetModelMatrix();
        earthCircle.scale(0.5f, 0.5f);
        earthCircle.rotate(angleInDegrees, Axis.Z);
        earthCircle.translate(deltaX, deltaY);
        earthCircle.draw();

        //MOON
        // Draw the triangle facing straight on.
        float deltaXMoon = (float) (deltaX / Math.sin(rotationRatio * Math.PI));
        float deltaYMoon = (float) (deltaY / Math.cos(rotationRatio * Math.PI));

        // Coupling the model matrix is required by the relative motion
        moonCircle.setModelMatrix(earthCircle.getModelMatrix());
        moonCircle.scale(0.3f, 0.3f);
        moonCircle.translate(deltaXMoon / 2f, deltaYMoon);
        moonCircle.draw();

        sunCircle.draw();
    }
}
