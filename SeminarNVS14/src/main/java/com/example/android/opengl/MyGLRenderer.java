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
package com.example.android.opengl;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;
import com.kanawish.seminar.nvscene14.program.AutomataProcessor;
import com.kanawish.seminar.nvscene14.util.IOUtils;

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
public class MyGLRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "MyGLRenderer";

    private Triangle mTriangle;
	private ImprovedSquare mSquare;
	private AutomataProcessor mAutomata;


    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    private float mAngle;

	// TODO: Inject.
	private final Context context ;
	private final int mode;

	public MyGLRenderer(Context context, int i) {
		this.context = context;
		this.mode = i;
	}

	@Override
    public void onSurfaceCreated(GL10 unused, EGLConfig config) {

        // Set the background frame color
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

/*
        mTriangle = new Triangle();
*/
		String fshFilename ;
		switch(this.mode) {
			case 5 :
				fshFilename = "StaticSineWaves.fsh.glsl"; // Static shader
				initImprovedSquare(fshFilename);
				break;
			case 4 :
				mTriangle = new Triangle();
				break;
			case 3 :
				// Too early for calls to glTexImage2D.
				mAutomata = new AutomataProcessor(context);
				break;
			case 2 :
				fshFilename = "Plasma.fsh.glsl"; // Slow on Nexus 5.
				initImprovedSquare(fshFilename);
				break;
			case 1 :
			default :
				fshFilename = "TriangleFractal.fsh.glsl"; // Buggy on Nexus5.
				initImprovedSquare(fshFilename);
				break;
		}


	}

	private void initImprovedSquare(String fshFilename) {
		try {
			mSquare = new ImprovedSquare(context,ImprovedSquare.DEFAULT_VERTEX_SHADER, fshFilename);
		} catch (IOException e) {
			Log.v(TAG, "Caught an exception with the ImprovedSquare.", e);
		}
	}

	@Override
    public void onDrawFrame(GL10 unused) {
        float[] scratch = new float[16];

        // Draw background color
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        // Set the camera position (View matrix)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3, 0f, 0f, 0f, 0f, 1.0f, 0.0f);

        // Calculate the projection and view transformation
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);

        // Create a rotation for the triangle

        // Use the following code to generate constant rotation.
        // Leave this code out when using TouchEvents.
        // long time = SystemClock.uptimeMillis() % 4000L;
        // float angle = 0.090f * ((int) time);

        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);

        // Combine the rotation matrix with the projection and camera view
        // Note that the mMVPMatrix factor *must be first* in order
        // for the matrix multiplication product to be correct.
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);

		switch(this.mode) {
			case 4:
				// Draw triangle
				mTriangle.draw(scratch);
				break;
			case 3:
				mAutomata.draw(mMVPMatrix);
				break;
			case 5:
			case 2:
			case 1:
				// Draw square on top, for tests.
				mSquare.draw(mMVPMatrix);
				break;
			default :
				// Error worthy..
				break;
		}


	}


    @Override
    public void onSurfaceChanged(GL10 unused, int width, int height) {
        // Adjust the viewport based on geometry changes,
        // such as screen rotation
        GLES20.glViewport(0, 0, width, height);

		switch(this.mode) {
			case 3:
				mAutomata.onSurfaceSizeChanged(width, height);
				break;
			case 2:
			case 1:
			default :
				// Error worthy..
				break;
		}

        float ratio = (float) width / height;

        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
    }

    /**
     * Utility method for compiling a OpenGL shader.
     *
     * @param type - Vertex or fragment shader type.
     * @param shaderCode - String containing the shader code.
     * @return - Returns an id for the shader.
	 *
     */
	static public int loadShader(int type, String shaderCode){

        // create a vertex shader type (GLES20.GL_VERTEX_SHADER)
        // or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
		MyGLRenderer.checkGlError(String.format("glCreateShader %d", type));

        // add the source code to the shader and compile it
        GLES20.glShaderSource(shader, shaderCode);
		MyGLRenderer.checkGlError(String.format("glShaderSource %s", shaderCode));
        GLES20.glCompileShader(shader);
		MyGLRenderer.checkGlError("glCompileShader");

        return shader;
    }

	static public int loadShaderAssetFile(Context context, int type, String filename) throws IOException {
		String shaderCode = IOUtils.loadStringFromAsset(context, filename);
		return MyGLRenderer.loadShader(type, shaderCode);
	}

    /**
    * Utility method for debugging OpenGL calls. Provide the name of the call
    * just after making it:
    *
    * <pre>
    * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
    * MyGLRenderer.checkGlError("glGetUniformLocation");</pre>
    *
    * If the operation is not successful, the check throws an error.
    *
    * @param glOperation - Name of the OpenGL call to check.
    */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			String message = String.format("%s: glError %d (%05X)", glOperation, error, error);
            Log.e(TAG, message);
			throw new RuntimeException(message);
        }
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

}