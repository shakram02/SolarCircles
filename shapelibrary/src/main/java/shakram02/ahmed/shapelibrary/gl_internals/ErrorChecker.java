package shakram02.ahmed.shapelibrary.gl_internals;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by ahmed on 11/19/17.
 */

public class ErrorChecker {
    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * <p>
     * <pre>
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
     * <p>
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        String errorMessage = null;
        int error;

        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {

            switch (error) {
                case GLES20.GL_INVALID_OPERATION:
                    errorMessage = "INVALID_OPERATION";
                    break;
                case GLES20.GL_INVALID_ENUM:
                    errorMessage = "INVALID_ENUM";
                    break;
                case GLES20.GL_INVALID_VALUE:
                    errorMessage = "INVALID_VALUE";
                    break;
                case GLES20.GL_OUT_OF_MEMORY:
                    errorMessage = "OUT_OF_MEMORY";
                    break;
                case GLES20.GL_INVALID_FRAMEBUFFER_OPERATION:
                    errorMessage = "INVALID_FRAMEBUFFER_OPERATION";
                    break;
            }

            Log.e("CUSTOM_GL_ERR", glOperation + ": glError " + errorMessage);
            throw new RuntimeException(glOperation + ": glError " + errorMessage);
        }
    }
}
