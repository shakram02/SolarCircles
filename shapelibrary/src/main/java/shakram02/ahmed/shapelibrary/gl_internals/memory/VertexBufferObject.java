package shakram02.ahmed.shapelibrary.gl_internals.memory;

import android.opengl.GLES20;

import shakram02.ahmed.shapelibrary.gl_internals.ErrorChecker;

public class VertexBufferObject {
    private int bufferId;
    private FloatBufferBasedArray dataBuffer;

    public VertexBufferObject(float[] dataArray, int glHandle, int floatsPerItem) {
        this(new FloatBufferBasedArray(dataArray, glHandle, floatsPerItem, true));
    }

    public VertexBufferObject(FloatBufferBasedArray dataBuffer) {
        this.dataBuffer = dataBuffer;
        int[] bufferIds = new int[1];

        GLES20.glGenBuffers(1, bufferIds, 0);
        ErrorChecker.checkGlError("VERTEX_BUFFER_OBJECT:glGenBuffers");
        this.bufferId = bufferIds[0];

        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, this.bufferId);
        ErrorChecker.checkGlError("VERTEX_BUFFER_OBJECT:glBindBuffer");

        // Transfer dataBuffer from client memory to the buffer.
        // We can release the client memory after this call.
        GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, dataBuffer.getSizeInBytes(),
                dataBuffer.getBuffer(), GLES20.GL_STATIC_DRAW);
        ErrorChecker.checkGlError("VERTEX_BUFFER_OBJECT:glBufferData");

        // IMPORTANT: Unbind from the buffer when we're done with it.
        GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
        ErrorChecker.checkGlError("VERTEX_BUFFER_OBJECT:glBindBuffer");
    }

    public void startDraw() {
        GLES20.glEnableVertexAttribArray(dataBuffer.getGlHandle());
        ErrorChecker.checkGlError("START_DRAW:glEnableVertexAttribArray");

        GLES20.glVertexAttribPointer(dataBuffer.getGlHandle(), dataBuffer.getItemLength(),
                dataBuffer.getType(), dataBuffer.isNormalized(), dataBuffer.getStride(), dataBuffer.getBuffer());
        ErrorChecker.checkGlError("START_DRAW:glVertexAttribPointer");
    }

    public void endDraw() {
        GLES20.glDisableVertexAttribArray(dataBuffer.getGlHandle());
    }

    public void deleteBuffer() {
        GLES20.glDeleteBuffers(1, new int[]{bufferId}, 0);
    }

    public int getItemCount() {
        return dataBuffer.getItemCount();
    }

    public int getSizeInBytes() {
        return dataBuffer.getSizeInBytes();
    }
}
