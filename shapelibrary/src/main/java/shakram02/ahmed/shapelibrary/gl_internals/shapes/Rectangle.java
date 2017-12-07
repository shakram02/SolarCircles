package shakram02.ahmed.shapelibrary.gl_internals.shapes;

import android.opengl.GLES20;

import shakram02.ahmed.shapelibrary.gl_internals.Painter;
import shakram02.ahmed.shapelibrary.gl_internals.memory.VertexBufferObject;

/**
 * Created by ahmed on 12/7/17.
 */

public class Rectangle extends DrawableObject {
    public Rectangle(float[] viewMatrix, int mvpHandle, VertexBufferObject vertices, Painter painter) {
        super(viewMatrix, mvpHandle, vertices, painter);
    }

    @Override
    public void draw() {
        this.painter.paint();
        this.vertices.startDraw();

        GLES20.glUniformMatrix4fv(super.mvpHandle, 1, false, super.getMvpMatrix(), 0);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, this.vertices.getItemCount());

        this.vertices.endDraw();
    }
}
