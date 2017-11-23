package com.example.ahmed.solarcircles.graphics.gl_internals.memory;

import java.nio.FloatBuffer;

/**
 * Provides an abstraction layer on top of shader objects
 * <p>
 * Takes in an array of floating point numbers, each of which representing
 * a point in 3D space
 */

public class VertexArray extends FloatBufferBasedArray {
    private static final int FLOATS_PER_ITEM = 3;

    public VertexArray(float[] items, int glHandle, boolean normalized) {
        super(items, glHandle, FLOATS_PER_ITEM, normalized);
    }

    public VertexArray(float[] items, boolean normalized) {
        super(items, FLOATS_PER_ITEM, normalized);
    }

    VertexArray(float[] items) {
        super(items, FLOATS_PER_ITEM, true);
    }

    public FloatBuffer getBuffer() {
        return super.getBuffer();
    }
}
