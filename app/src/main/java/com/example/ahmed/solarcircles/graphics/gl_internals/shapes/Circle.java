package com.example.ahmed.solarcircles.graphics.gl_internals.shapes;

import android.opengl.GLES20;

import com.example.ahmed.solarcircles.graphics.gl_internals.Painter;
import com.example.ahmed.solarcircles.graphics.gl_internals.memory.VertexBufferObject;

/**
 * This is a circle, it can be drawn. the VBO is supplied from outside so that
 * it's coupled with its location in the GL program
 */

public class Circle extends DrawableObject {
    public Circle(float[] viewMatrix, int mvpHandle,
                  VertexBufferObject vertices, Painter painter) {
        super(viewMatrix, mvpHandle, vertices, painter);
    }

    @Override
    public void draw() {
        super.painter.paint();
        this.vertices.startDraw();

        GLES20.glUniformMatrix4fv(super.mvpHandle, 1, false, super.getMvpMatrix(), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, this.vertices.getItemCount());

        this.vertices.endDraw();
    }
}
