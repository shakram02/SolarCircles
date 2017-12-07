package shakram02.ahmed.shapelibrary.gl_internals;

import android.opengl.GLES20;

/**
 * Created by ahmed on 11/19/17.
 */

public class ShaderManager {
    public static int compileVertexShader(String shaderCode) {
        return createShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return createShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    public static int createProgram(int vertexShaderHandle, int fragmentShaderHandle) {
        // Create a program object and store the handle to it.
        int programHandle = GLES20.glCreateProgram();

        if (programHandle == 0) {
            throw new RuntimeException("Error creating program.");
        }

        GLES20.glAttachShader(programHandle, vertexShaderHandle);
        GLES20.glAttachShader(programHandle, fragmentShaderHandle);

        // Bind attributes
        GLES20.glBindAttribLocation(programHandle, 0, "a_Position");
        GLES20.glBindAttribLocation(programHandle, 1, "a_Color");

        // Link the two shaders together into a program.
        GLES20.glLinkProgram(programHandle);
        // Get the link status.
        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programHandle, GLES20.GL_LINK_STATUS, linkStatus, 0);

        // If the link failed, delete the program.
        if (linkStatus[0] == 0) {
            GLES20.glDeleteProgram(programHandle);
            String error = GLES20.glGetProgramInfoLog(programHandle);
            throw new RuntimeException("Failed to create program:" + error);
        }

        return programHandle;
    }

    private static int createShader(int shaderType, String code) throws RuntimeException {
        int vertexShaderHandle = GLES20.glCreateShader(shaderType);

        if (vertexShaderHandle == 0) {
            ErrorChecker.checkGlError("createShader");
            throw new RuntimeException("Error creating vertex shader.");
        }

        GLES20.glShaderSource(vertexShaderHandle, code);
        GLES20.glCompileShader(vertexShaderHandle);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(vertexShaderHandle, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            GLES20.glDeleteShader(vertexShaderHandle);
            String error = GLES20.glGetShaderInfoLog(vertexShaderHandle);
            throw new RuntimeException("Failed to compile shader:" + error);
        }

        return vertexShaderHandle;
    }
}
