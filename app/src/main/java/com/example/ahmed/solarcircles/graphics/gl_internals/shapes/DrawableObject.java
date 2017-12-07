package com.example.ahmed.solarcircles.graphics.gl_internals.shapes;

import com.example.ahmed.solarcircles.graphics.gl_internals.Painter;
import com.example.ahmed.solarcircles.graphics.gl_internals.memory.VertexBufferObject;

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
