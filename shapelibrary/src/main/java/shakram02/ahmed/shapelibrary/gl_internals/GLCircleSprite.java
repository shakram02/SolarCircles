package shakram02.ahmed.shapelibrary.gl_internals;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

public class GLCircleSprite {

    private float[] mColor;
    private int mProgram;

    private float mCenterX;
    private float mCenterY;
    private float mRadius;

    private static final int COORDS_PER_VERTEX = 4;
    private static float VERTEX_COORDINATES[] = {
            -1f, 1f, 0f, 0f,   // top left
            -1f, -1f, 0f, 0f,   // bottom left
            1f, -1f, 0f, 0f,  // bottom right
            1f, 1f, 0f, 0f,   // top right
    };

    private FloatBuffer mVertexBuffer;
    private ShortBuffer mDrawListBuffer;

    private final short mDrawOrder[] = {0, 1, 2, 0, 2, 3}; // order to draw vertices

    public GLCircleSprite(float[] color) {
        mColor = color;

        String vertexShaderSource =
                "attribute vec4 aPosition; \n" +
                        "void main() \n" +
                        "{ \n" +
                        "   gl_Position = aPosition; \n" +
                        "} \n";

        String fragmentShaderSource =
                "precision highp float;\n" +
                        "uniform vec4 aCirclePosition;\n" +
                        "uniform float aRadius; \n" +
                        "uniform vec4 aColor; \n" +
                        "const float threshold = 0.005;\n" +
                        "void main() \n" +
                        "{ \n" +
                        "   float d, dist;\n" +
                        "   dist = distance(aCirclePosition, gl_FragCoord.xy);\n" +
                        "   if(dist == 0.)\n" +
                        "       dist = 1.;\n" +
                        "   d = aRadius / dist;\n" +
                        "   if(d >= 1.)\n" +
                        "        gl_FragColor = aColor;\n" +
                        "   else if(d >= 1. - threshold) \n" +
                        "   {\n" +
                        "        float a = (d - (1. - threshold)) / threshold;\n" +
                        "        gl_FragColor = vec4(aColor.r, aColor.g, aColor.b, a); \n" +
                        "    }\n" +
                        "    else\n" +
                        "        gl_FragColor = vec4(0., 0., 0., 0.);\n" +
                        "} \n";

        int vertexShader = ShaderManager.compileVertexShader(vertexShaderSource);
        int fragmentShader = ShaderManager.compileFragmentShader(fragmentShaderSource);
        mProgram = ShaderManager.createProgram(vertexShader, fragmentShader);

        mVertexBuffer = ByteBuffer
                .allocateDirect(VERTEX_COORDINATES.length * 4)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();

        mVertexBuffer.put(VERTEX_COORDINATES);
        mVertexBuffer.position(0);

        ByteBuffer drawByteBuffer = ByteBuffer.allocateDirect(mDrawOrder.length * 2);
        drawByteBuffer.order(ByteOrder.nativeOrder());
        mDrawListBuffer = drawByteBuffer.asShortBuffer();
        mDrawListBuffer.put(mDrawOrder);
        mDrawListBuffer.position(0);
    }

    void draw() {
        GLES20.glEnable(GLES20.GL_BLEND);
        GLES20.glBlendFunc(GLES20.GL_SRC_ALPHA, GLES20.GL_ONE_MINUS_SRC_ALPHA);
        GLES20.glUseProgram(mProgram);
        ErrorChecker.checkGlError("glUseProgram");

        int mPositionHandle = GLES20.glGetAttribLocation(mProgram, "aPosition");
        ErrorChecker.checkGlError("glGetAttribLocation");

        GLES20.glEnableVertexAttribArray(mPositionHandle);
        int vertexStride = COORDS_PER_VERTEX * 4;
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES20.GL_FLOAT, false, vertexStride, mVertexBuffer);

        GLES20.glUniform4fv(GLES20.glGetUniformLocation(mProgram, "aColor"), 1, mColor, 0);
        GLES20.glUniform2f(GLES20.glGetUniformLocation(mProgram, "aCirclePosition"), mCenterX, mCenterY);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(mProgram, "aRadius"), mRadius);
        GLES20.glDrawElements(GLES20.GL_TRIANGLES, mDrawOrder.length, GLES20.GL_UNSIGNED_SHORT, mDrawListBuffer);
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

    public float getCenterX() {
        return mCenterX;
    }

    public void setCenterX(float centerX) {
        mCenterX = centerX;
    }

    public float getCenterY() {
        return mCenterY;
    }

    public void setCenterY(float centerY) {
        mCenterY = centerY;
    }

    public float getRadius() {
        return mRadius;
    }

    public void setRadius(float radius) {
        this.mRadius = radius;
    }
}
