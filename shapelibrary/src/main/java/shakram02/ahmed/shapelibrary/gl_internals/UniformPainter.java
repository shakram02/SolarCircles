package shakram02.ahmed.shapelibrary.gl_internals;

import android.opengl.GLES20;

/**
 * A painter whose backing color is a uniform field in the GL program
 */

public class UniformPainter implements Painter {
    private final Integer uniformColorHandle;
    private final float[] colorArray;

    /**
     * Initializes a new {@link UniformPainter}
     *
     * @param uniformColorHandle Variable handle in {@link shakram02.ahmed.shapelibrary.gl_internals.memory.GLProgram}
     * @param colorArray         An array containing a single color point for the uniform variable
     */
    public UniformPainter(Integer uniformColorHandle, float[] colorArray) {
        this.uniformColorHandle = uniformColorHandle;
        this.colorArray = colorArray;
    }

    @Override
    public void paint() {
        GLES20.glUniform4fv(uniformColorHandle, 1, colorArray, 0);
    }
}
