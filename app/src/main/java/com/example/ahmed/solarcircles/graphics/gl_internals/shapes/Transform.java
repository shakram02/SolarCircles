package com.example.ahmed.solarcircles.graphics.gl_internals.shapes;

import android.opengl.Matrix;

/**
 * An objects transform, wraps the MVP matrix
 */

public abstract class Transform {
    private float[] modelMatrix;
    private float[] mvpMatrix = new float[16];
    private float[] viewMatrix;
    private float[] projectionMatrix;

    Transform(final float[] viewMatrix,
              final float[] projectionMatrix) {
        this.viewMatrix = viewMatrix;
        this.projectionMatrix = projectionMatrix;

        modelMatrix = new float[16];
        Matrix.setIdentityM(modelMatrix, 0);
    }

    public final void translate(float x, float y) {
        translate(x, y, 0);
    }

    public final void translate(float x, float y, float z) {
        Matrix.translateM(modelMatrix, 0, x, y, z);
    }

    public final void rotate(float angleInDegrees, Axis rotationAxis) {
        switch (rotationAxis) {
            case X:
                Matrix.rotateM(modelMatrix, 0, angleInDegrees, 1, 0, 0);
                break;
            case Y:
                Matrix.rotateM(modelMatrix, 0, angleInDegrees, 0, 1, 0);
                break;
            case Z:
                Matrix.rotateM(modelMatrix, 0, angleInDegrees, 0, 0, 1);
                break;
        }
    }

    public final void scale(float x, float y) {
        scale(x, y, 0);
    }

    public final void scale(float x, float y, float z) {
        Matrix.scaleM(modelMatrix, 0, x, y, z);
    }

    public final float[] getMvpMatrix() {
        updateMvpMatrix();
        return mvpMatrix;
    }

    private void updateMvpMatrix() {
        Matrix.multiplyMM(mvpMatrix, 0, viewMatrix, 0,
                modelMatrix, 0);
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0,
                mvpMatrix, 0);
    }

    /**
     * Projection matrix changes when the surface is updated, or the screen is rotated
     *
     * @param projectionMatrix New values for projection matrix
     */
    public void setProjectionMatrix(final float[] projectionMatrix) {
        this.projectionMatrix = projectionMatrix;
    }

    public void setModelMatrix(final float[] modelMatrix) {
        this.modelMatrix = modelMatrix;
    }

    public void setViewMatrix(final float[] viewMatrix) {
        this.viewMatrix = viewMatrix;
    }
}
