package com.example.ahmed.solarcircles.graphics.gl_internals.shapes;

import android.opengl.Matrix;

/**
 * An objects transform, wraps the MVP matrix
 * <p>
 * Shapes must inherit from transform to give them the ability
 * to be scaled, rotated and translated
 */

public abstract class Transform {
    // We're using homogeneous coordinates 4-d
    private static final int TRANSFORM_MATRIX_DIMENSIONS = 16;
    private float[] modelMatrix;
    private float[] mvpMatrix = new float[TRANSFORM_MATRIX_DIMENSIONS];
    private float[] viewMatrix;
    private float[] projectionMatrix;


    Transform(final float[] viewMatrix) {

        // Setters are called to check for errors
        this.setViewMatrix(viewMatrix);

        modelMatrix = new float[TRANSFORM_MATRIX_DIMENSIONS];
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

    /**
     * Calculates the MVP matrix based on the current model, view and projection matrices
     * this is called only when querying the value of the MVP matrix to save calculations
     */
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
        throwIfAllZeros(projectionMatrix);
        if (!isValidLength(projectionMatrix, TRANSFORM_MATRIX_DIMENSIONS)) {
            throw new IllegalArgumentException("Invalid projectionMatrix size, expected size:"
                    + TRANSFORM_MATRIX_DIMENSIONS);
        }

        this.projectionMatrix = projectionMatrix;
    }

    public void resetModelMatrix() {
        Matrix.setIdentityM(modelMatrix, 0);
    }

    /**
     * Sets the model matrix, sometimes this is desirable for relative
     * motion or whatever
     *
     * @param modelMatrix New values of the model matrix
     */
    public void setModelMatrix(final float[] modelMatrix) {
        throwIfAllZeros(modelMatrix);
        if (viewMatrix.length != TRANSFORM_MATRIX_DIMENSIONS) {
            throw new IllegalArgumentException("Invalid modelMatrix size, expected size:"
                    + TRANSFORM_MATRIX_DIMENSIONS);
        }

        this.modelMatrix = modelMatrix;
    }

    public final float[] getModelMatrix() {
        return this.modelMatrix;
    }

    public void setViewMatrix(final float[] viewMatrix) {
        if (viewMatrix.length != TRANSFORM_MATRIX_DIMENSIONS) {
            throw new IllegalArgumentException("Invalid viewMatrix size, expected size:"
                    + TRANSFORM_MATRIX_DIMENSIONS);
        }

        this.viewMatrix = viewMatrix;
    }

    private static boolean isValidLength(float[] array, int required) {
        return array.length == required;
    }

    private static boolean throwIfAllZeros(float[] array) {
        for (float f : array) {
            if (f != 0) {
                return false;
            }
        }
        throw new IllegalArgumentException("All values are 0");
    }
}
