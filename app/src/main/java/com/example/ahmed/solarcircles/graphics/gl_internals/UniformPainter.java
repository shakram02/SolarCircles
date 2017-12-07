package com.example.ahmed.solarcircles.graphics.gl_internals;

import android.opengl.GLES20;

/**
 * Created by ahmed on 12/7/17.
 */

public class UniformPainter implements Painter {
    private final Integer uniformColorHandle;
    private final float[] colorArray;

    public UniformPainter(Integer uniformColorHandle, float[] colorArray) {
        this.uniformColorHandle = uniformColorHandle;

        this.colorArray = colorArray;
    }

    @Override
    public void paint() {
        GLES20.glUniform4fv(uniformColorHandle, 1, colorArray, 0);
    }
}
