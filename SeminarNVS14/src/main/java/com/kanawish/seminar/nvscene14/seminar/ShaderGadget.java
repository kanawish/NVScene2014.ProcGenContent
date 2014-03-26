/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.kanawish.seminar.nvscene14.seminar;

import android.content.Context;
import android.opengl.GLES20;
import android.os.SystemClock;
import com.kanawish.seminar.nvscene14.util.IOUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 * Very simple canvas for fragment shader prototyping.
 */
public class ShaderGadget {

	private static final String TAG = ShaderGadget.class.getSimpleName();


	public static final String DEFAULT_VERTEX_SHADER = "ImprovedSquare.vsh";
	public static final String DEFAULT_FRAGMENT_SHADER = "ImprovedSquare.fsh";

	private final FloatBuffer vertexBuffer;
    private final ShortBuffer drawListBuffer;
    private final int program;
    private int vPositionHandle;
    private int vColorHandle;
    private int uMVPMatrixHandle;

	// For debugging
	long loopCount = 0 ;

	// In seconds, initialized to 0 on first call to draw.
	float timeF = -1 ;
	// Recorded on first call to draw, used to calculate future values of 'time' above.
	long systemStartTime = SystemClock.elapsedRealtime();

	float resolutionVec2[] = { 256,256 };

	// number of coordinates per vertex in this array
    static final int COORDS_PER_VERTEX = 3;
    static float squareCoords[] = {
            -1.0f,  1.0f, 0.0f,   // top left
            -1.0f, -1.0f, 0.0f,   // bottom left
             1.0f, -1.0f, 0.0f,   // bottom right
             1.0f,  1.0f, 0.0f }; // top right

    private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    float color[] = { 0.2f, 0.709803922f, 0.898039216f, 1.0f };

	/**
	 * Defaults
	 */
	public ShaderGadget(Context context) throws IOException {
		this(context, DEFAULT_VERTEX_SHADER, DEFAULT_FRAGMENT_SHADER);
	}

    /**
     * Sets up the drawing object data for use in an OpenGL ES context.
	 *
	 * @throws java.io.IOException if there was a problem loading shaders from disk.
	 *
	 * TODO: Change schemes to get the shader code instead of asset filenames.
     */
    public ShaderGadget(Context context, String vshFilename, String fshFilename) throws IOException {

		// NOTE: This is where we load shader code from 'disk', see assets folder.
		// NOTE: Mention IntelliJ basic GLSL syntax highlighter.
		String vertexShaderCode = IOUtils.loadStringFromAsset(context, vshFilename);
		String fragmentShaderCode = IOUtils.loadStringFromAsset(context, fshFilename);

		// NOTE: This is how we pass along memory blocks to OpenGL on Android, vs passing pointers in C.
        // initialize vertex byte buffer for shape coordinates
        ByteBuffer bb = ByteBuffer.allocateDirect(
        // (# of coordinate values * 4 bytes per float)
                squareCoords.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareCoords);
        vertexBuffer.position(0);

        // initialize byte buffer for the draw list
        ByteBuffer dlb = ByteBuffer.allocateDirect(
                // (# of coordinate values * 2 bytes per short)
                drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);

		// NOTE: This is where we setup the shaders, attach and link them.
        // Prepare shaders and OpenGL program
        int vertexShader = ShaderGadgetRenderer.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = ShaderGadgetRenderer.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);

        program = GLES20.glCreateProgram();             // create empty OpenGL Program
        GLES20.glAttachShader(program, vertexShader);   // add the vertex shader to program
        GLES20.glAttachShader(program, fragmentShader); // add the fragment shader to program
        GLES20.glLinkProgram(program);                  // create OpenGL program executables
    }

    /**
     * Encapsulates the OpenGL ES instructions for drawing this shape.
     *
     * @param mvpMatrix - The Model View Project matrix in which to draw
     * this shape.
	 *
	 * NOTE: Could be worth asking who in the audience is familiar with shader code, and who is totally new to it.
	 *
     */
    public void draw(float[] mvpMatrix) {
		// Our time reference.
		if( timeF == -1 ) {
			timeF = 0 ;
			systemStartTime = SystemClock.elapsedRealtime();
		} else {
			timeF = (SystemClock.elapsedRealtime() - systemStartTime) / 1000.0f ;
		}

		// Add program to OpenGL environment
        GLES20.glUseProgram(program);

		// NOTE: This section is about assigning custom variable that the shader will be accessing.
		// Get handle to the time uniform.
		int timeHandle = GLES20.glGetUniformLocation(program,"time");
		// Assign time.
		GLES20.glUniform1f(timeHandle, timeF);

		// Resolution
		int resolutionHandle = GLES20.glGetUniformLocation(program,"resolution");
		GLES20.glUniform2fv(resolutionHandle,1, resolutionVec2,0);

        // Get a handle to vertex shader's vPosition member
        vPositionHandle = GLES20.glGetAttribLocation(program, "vPosition");

        // Enable a handle to the triangle vertices
        GLES20.glEnableVertexAttribArray(vPositionHandle);

        // Prepare the triangle coordinate data for our 'shader canvas'
        GLES20.glVertexAttribPointer(vPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false,
                vertexStride, vertexBuffer);

		// NOTE: Not really used here.
        // Get handle to fragment shader's vColor member
        vColorHandle = GLES20.glGetUniformLocation(program, "vColor");
        // Set color for drawing the triangle
        GLES20.glUniform4fv(vColorHandle, 1, color, 0);

        // get handle to shape's transformation matrix
        uMVPMatrixHandle = GLES20.glGetUniformLocation(program, "uMVPMatrix");

        // Apply the projection and view transformation
        GLES20.glUniformMatrix4fv(uMVPMatrixHandle, 1, false, mvpMatrix, 0);

		// NOTE: This is where the fragment shader will be invoked.
		// TODO: Add a slide or two explaining basics of vertex/fragment shaders to less experienced audiences.
        // Draw the square
        GLES20.glDrawElements(
                GLES20.GL_TRIANGLES, drawOrder.length,
                GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

        // Disable vertex array
        GLES20.glDisableVertexAttribArray(vPositionHandle);
    }


}