package shakram02.ahmed.shapelibrary.gl_internals.shapes;

import shakram02.ahmed.shapelibrary.gl_internals.Painter;
import shakram02.ahmed.shapelibrary.gl_internals.memory.VertexBufferObject;

/**
 * A shape that can be drawn, this class wraps the draw functions
 * of different shapes in a convenient interface
 */

public abstract class DrawableObject extends Transform {


    final int mvpHandle;
    final VertexBufferObject vertices;
    final Painter painter;

    DrawableObject(float[] viewMatrix, int mvpHandle,
                   VertexBufferObject vertices,
                   Painter painter) {
        super(viewMatrix);
        this.mvpHandle = mvpHandle;

        this.vertices = vertices;
        this.painter = painter;
    }

    public abstract void draw();
}
