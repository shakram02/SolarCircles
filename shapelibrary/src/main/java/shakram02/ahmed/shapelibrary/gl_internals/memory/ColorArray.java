package shakram02.ahmed.shapelibrary.gl_internals.memory;

import java.nio.FloatBuffer;

/**
 * Provides an abstraction layer on top of shader objects
 */

public class ColorArray extends FloatBufferBasedArray {
    private static final int FLOATS_PER_ITEM = 4;


    public ColorArray(float[] items, int glHandle, boolean normalized) {
        super(items, glHandle, FLOATS_PER_ITEM, normalized);
    }

    public ColorArray(float[] items, boolean normalized) {
        super(items, FLOATS_PER_ITEM, normalized);
    }

    ColorArray(float[] items) {
        super(items, FLOATS_PER_ITEM, true);
    }


    public FloatBuffer getBuffer() {
        return super.getBuffer();
    }
}
