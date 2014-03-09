package com.kanawish.seminar.nvscene14.usage.opengl;

import android.content.Context;
import android.content.res.AssetManager;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * Created by etiennecaron on 2/24/2014.
 *
 * This surface view I'd like to use as my own example. I was quite inspired when I saw
 * the follow post:
 * http://codeflow.org/entries/2010/dec/09/minecraft-like-rendering-experiments-in-opengl-4/
 *
 * And considering
 *
 * Features I'd like to see:
 *
 * Loading shaders from file, with the idea of using external tools to test and debug the shaders, etc.
 *
 *
 *
 */
public class MineGLSurfaceView extends GLSurfaceView {

	public MineGLSurfaceView(Context context) {
		super(context);
	}

	public MineGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	class Renderer implements GLSurfaceView.Renderer {
		// Pass-through example
		private final String vertexShaderCode =
				"attribute vec4 vPosition;" +
						"void main() {" +
						"  gl_Position = vPosition;" +
						"}";

		// Pass-through example
		private final String fragmentShaderCode =
				"precision mediump float;" +
						"uniform vec4 vColor;" +
						"void main() {" +
						"  gl_FragColor = vColor;" +
						"}";

		@Override
		public void onSurfaceCreated(GL10 gl, EGLConfig config) {
			// Initialize shapes
		}

		@Override
		public void onSurfaceChanged(GL10 gl, int width, int height) {

		}

		@Override
		public void onDrawFrame(GL10 gl) {

		}

	}

	public static int loadShader(int type, String shaderCode){

		// create a vertex shader type (GLES20.GL_VERTEX_SHADER)
		// or a fragment shader type (GLES20.GL_FRAGMENT_SHADER)
		int shader = GLES20.glCreateShader(type);

		// add the source code to the shader and compile it
		GLES20.glShaderSource(shader, shaderCode);
		GLES20.glCompileShader(shader);

		return shader;
	}


}
