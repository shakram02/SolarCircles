package com.example.ahmed.solarcircles.graphics.gl_internals;

import android.opengl.GLES20;

import com.example.ahmed.solarcircles.graphics.gl_internals.memory.FloatBufferBasedArray;

import java.util.Stack;

/**
 * Created by ahmed on 11/19/17.
 * <p>
 * Manages sending data to GPU
 */

public class ShaderDataLoader {
    private Stack<Integer> handleStack;
    private States state;

    /**
     * How many bytes per float.
     */
    private static final int FLOAT_SIZE = 4;

    private enum States {
        Started,
        Closed,
    }

    public ShaderDataLoader() {
        handleStack = new Stack<>();
        state = States.Closed;
    }

    /**
     * Put the machine in the start state
     */
    public void start() {
        assertInState(States.Closed);

        if (handleStack.size() > 0) {
            throw new RuntimeException("Attribute pointer wasn't closed, " +
                    "call disableHandles() first");
        }

        state = States.Started;
    }

    /**
     * Load data to shaders
     *
     * @param dataBuffer Buffer object containing data
     */
    public void loadData(FloatBufferBasedArray dataBuffer) {
        assertInState(States.Started);

        GLES20.glVertexAttribPointer(dataBuffer.getGlHandle(), dataBuffer.getItemLength(),
                dataBuffer.getType(), dataBuffer.isNormalized(), dataBuffer.getStride(), dataBuffer.getBuffer());
        ErrorChecker.checkGlError("LOAD_DATA:glVertexAttribPointer");

        GLES20.glEnableVertexAttribArray(dataBuffer.getGlHandle());
        ErrorChecker.checkGlError("LOAD_DATA:glEnableVertexAttribArray");

        handleStack.push(dataBuffer.getGlHandle());
    }

    /**
     * Handles to data should be disabled after drawing
     */
    public void disableHandles() {
        assertInState(States.Started);

        while (!handleStack.empty()) {
            GLES20.glDisableVertexAttribArray(handleStack.pop());
        }

        state = States.Closed;
    }

    private void assertInState(States expected) {
        if (state != expected) {
            throw new RuntimeException("Invalid state, Expected to be in [" + expected + "]");
        }
    }
}
