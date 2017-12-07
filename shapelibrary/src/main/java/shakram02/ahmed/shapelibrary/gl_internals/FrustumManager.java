package shakram02.ahmed.shapelibrary.gl_internals;

import android.opengl.GLES20;
import android.opengl.Matrix;

/**
 * Created by ahmed on 11/19/17.
 * <p>
 * Creates a view Frustum
 */

public class FrustumManager {

    public static float[] createFrustum(int x, int y, int width, int height) {

        // Store the projection matrix. This is used to project the scene onto a 2D viewport.
        float[] projectionMatrix = new float[16];
        GLES20.glViewport(x, y, width, height);

        // Create a new perspective projection matrix. The height will stay the same
        // while the width will vary as per aspect ratio.
        final float ratio = (float) width / height;
        final float left = -ratio;
        final float right = ratio;
        final float bottom = -1.0f;
        final float top = 1.0f;
        final float near = 1.0f;
        final float far = 10.0f;

        Matrix.frustumM(projectionMatrix, 0, left, right, bottom, top, near, far);

        return projectionMatrix;
    }
}
