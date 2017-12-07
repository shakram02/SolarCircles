package com.example.ahmed.solarcircles.graphics.gl_internals.shapes;

import com.example.ahmed.solarcircles.graphics.gl_internals.Painter;
import com.example.ahmed.solarcircles.graphics.gl_internals.memory.VertexBufferObject;

/**
 * Created by ahmed on 11/23/17.
 */

public abstract class DrawableObject extends Transform {


    final int mvpHandle;
    final VertexBufferObject vertices;
    final Painter painter;

    public DrawableObject(float[] viewMatrix,
                          float[] projectionMatrix,
                          int mvpHandle,
                          VertexBufferObject vertices,
                          Painter painter) {
        super(viewMatrix, projectionMatrix);
        this.mvpHandle = mvpHandle;

        this.vertices = vertices;
        this.painter = painter;
    }

    public abstract void draw();
}
