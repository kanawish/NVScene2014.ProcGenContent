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
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import com.kanawish.seminar.nvscene14.util.IOUtils;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.IOException;

/**
 * Provides drawing instructions for a GLSurfaceView object. This class
 * must override the OpenGL ES drawing lifecycle methods:
 * <ul>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceCreated}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onDrawFrame}</li>
 *   <li>{@link android.opengl.GLSurfaceView.Renderer#onSurfaceChanged}</li>
 * </ul>
 */
public class  ShaderGadgetRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "ShaderGadgetRenderer";

	// TODO: Add comments for people new to OpenGL
    private final float[] mvpMatrix = new float[16];
    private final float[] projectionMatrix = new float[16];
    private final float[] viewMatrix = new float[16];
    private final float[] rotationMatrix = new float[16];

	// TODO: Could introduce Dagger injection here, but it would likely confuse newbies?
	private final Context context ;
	private final String fragmentShaderFilename;

	// NOTE:
	private ShaderGadget square;

	// NOTE: See parent surface view for how to pass values from UI thread to Renderer's thread.
	private float angle;


	public ShaderGadgetRenderer(Context context, String fragmentShaderFilename) {
		this.context = context;
		this.fragmentShaderFilename = fragmentShaderFilename;
	}

	private void initShaderGadget(String fshFilename) {
		try {
			square = new ShaderGadget(context,ShaderGadget.DEFAULT_VERTEX_SHADER, fshFilename);
		} catch (IOException e) {
			Log.v(TAG, String.format("Caught an exception constructing ShaderGadget %s.", fshFilename), e);
		}
	}


	// NOTE: The angle rotation stuff here is not used yet, just here to demonstrate inter-thread value passing.
	@Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(viewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mvpMatrix, 0, projectionMatrix, 0, viewMatrix, 0);

        // Create a rotation for the triangle
        Matrix.setRotateM(rotationMatrix, 0, angle, 0, 0, 1.0f);

        // Combine the rotation matrix with the projection and camera view
        // The mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mvpMatrix, 0, rotationMatrix, 0);

		square.draw(mvpMatrix);

	}


	/**
	 * NOTE: This is called on at least once when starts, and *whenever EGL context is lost*.
	 *
	 * This can happen when an Android device wakes from sleep. So this method is a convenient place
	 * to put pre-rendering resource creation code, and what needs to be recreated on context loss.
	 * (Such as textures.)
	 */
	@Override
	public void onSurfaceCreated(GL10 unused, EGLConfig config) {

		// Set the background frame color
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

		initShaderGadget(fragmentShaderFilename);
	}


	/**
	 * NOTE: Called after the surface is created and whenever the OpenGL ES surface size changes.
	 */
    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(projectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }


    /**
     * Static utility method for compiling a OpenGL shader.
     *
     * <p><strong>Note:</strong> When developing shaders, use the checkGlError()
     * method to debug shader coding errors.</p>
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
     */
	static public int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);

        return shader;
    }


	static public int loadShaderAssetFile(Context context, int type, String filename) throws IOException {
		String shaderCode = IOUtils.loadStringFromAsset(context, filename);
		return ShaderGadgetRenderer.loadShader(type, shaderCode);
	}

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return angle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        this.angle = angle;
    }

}