package com.example.ahmed.solarcircles.graphics.gl_internals.memory;

import android.opengl.GLES20;

import com.example.ahmed.solarcircles.graphics.gl_internals.ShaderManager;

import java.util.Hashtable;

/**
 * Open GL program, manages the variable assignment
 */

public class GLProgram {
    private final Hashtable<String, Integer> variables;
    private final int programHandle;

    public GLProgram(String vertexShaderCode, String fragmentShaderCode) {
        int vertexShaderHandle = ShaderManager.compileVertexShader(vertexShaderCode);
        int fragmentShaderHandle = ShaderManager.compileFragmentShader(fragmentShaderCode);
        programHandle = ShaderManager.createProgram(vertexShaderHandle, fragmentShaderHandle);
        variables = new Hashtable<>();
    }

    public void declareAttribute(String attributeName) {
        int handle = GLES20.glGetAttribLocation(programHandle, attributeName);
        variables.put(attributeName, handle);
    }

    public void declareUniform(String uniformName) {
        int handle = GLES20.glGetUniformLocation(programHandle, uniformName);
        variables.put(uniformName, handle);
    }

    public void activate() {
        GLES20.glUseProgram(programHandle);
    }

    public Integer getVariableHandle(String variableName) {
        return variables.get(variableName);
    }
}
