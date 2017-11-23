package com.example.ahmed.solarcircles.graphics.gl_internals.memory;

import android.opengl.GLES20;

import com.example.ahmed.solarcircles.BuildConfig;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;

/**
 * floating point GPU data to be sent for rendering purposes, they might be position
 * or color data
 * <p>
 * This class's data is immutable and can't be changed during runtime. The normalization of
 * data items must be known during construction
 * <p>
 * Represents a class that can be sent over to the GPU using a buffer. Maintaining the state
 * of the buffer is handled by the user
 */

public abstract class FloatBufferBasedArray {
    private ArrayList<Float> raw;
    private boolean isDirty = true;
    private FloatBuffer cache;
    private int floatsPerItem = 0;
    private boolean normalized;
    private int glHandle = -1;

    /**
     * How many bytes are there for a float
     */
    private static final int FLOAT_SIZE = 4;

    // This constructor is private to force the knowledge of data normalization
    private FloatBufferBasedArray(float[] items, int floatsPerItem) {
        if (BuildConfig.DEBUG && items.length % floatsPerItem != 0) {
            throw new RuntimeException(String.format("Invalid data array, " +
                    "item count: %s, floats per item: %s", items.length, floatsPerItem));
        }
        this.floatsPerItem = floatsPerItem;
        raw = new ArrayList<>();
        this.addAll(items);
    }

    FloatBufferBasedArray(float[] items, int glHandle, int floatsPerItem, boolean normalized) {
        this(items, floatsPerItem, normalized);
        this.glHandle = glHandle;
    }

    FloatBufferBasedArray(float[] items, int floatsPerItem, boolean normalized) {
        this(items, floatsPerItem);

        this.floatsPerItem = floatsPerItem;
        this.normalized = normalized;
    }

    /**
     * @return The number of logical instances in the array (i.e position)
     */
    int getItemCount() {
        return raw.size() / floatsPerItem;
    }


    /**
     * @return Size of all objects in the array in bytes
     */
    int getSizeInBytes() {
        return getItemCount() * FLOAT_SIZE;
    }


    public FloatBuffer getBuffer() {
        if (isDirty) {
            reallocateBuffer();
        }

        // Reset cache positions
        cache.position(0);

        if (!cache.hasRemaining()) {
            throw new IllegalArgumentException("Array contains no elements");
        }

        return cache;
    }

    private void addItem(float a) {
        isDirty = true;

        this.raw.add(a);
    }

    /**
     * Updates the value of the member variable 'cache' when
     * the state is dirty
     */
    private void reallocateBuffer() {
        isDirty = false;

        FloatBuffer buffer = ByteBuffer
                .allocateDirect(raw.size() * FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        for (float f : raw) {
            buffer.put(f);
        }

        cache = buffer;
    }


    void addAll(float[] items) {
        isDirty = true;

        for (float v : items) {
            this.addItem(v);
        }

        validateLength();
    }

    public int getGlHandle() {
        if (glHandle == -1) {
            throw new RuntimeException("Handle wasn't set");
        }

        return glHandle;
    }

    public void setGlHandle(int glHandle) {
        if (this.glHandle != -1) {
            throw new RuntimeException("Resetting GLHandle");
        }

        this.glHandle = glHandle;
    }

    public int getItemLength() {
        return floatsPerItem;
    }

    public int getType() {
        return GLES20.GL_FLOAT;
    }

    private void validateLength() {
        if (raw.size() % floatsPerItem != 0) {
            throw new RuntimeException("Invalid points were added");
        }
    }

    public boolean isNormalized() {
        return normalized;
    }

    public int getStride() {
        return FLOAT_SIZE * floatsPerItem;
    }

}
