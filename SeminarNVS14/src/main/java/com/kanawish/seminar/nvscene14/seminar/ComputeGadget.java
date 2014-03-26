package com.kanawish.seminar.nvscene14.seminar;

import android.content.Context;
import android.opengl.GLES20;
import com.kanawish.seminar.nvscene14.program.SizeSensitive;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

/**
 *
 * TODO: Make this into a texture in/out pipe, the shaders should be bringing the behavior.
 *
 * Right now this class is a simple PoC.
 *
 * Originally based on an iOS tutorial found here:
 *
 * http://blog.angusforbes.com/openglglsl-render-to-texture/
 *
 */
public class ComputeGadget implements SizeSensitive {

	private static final String TAG = ComputeGadget.class.getSimpleName();
	private final Context context;

	// NOTE: Grouped the program / uniforms / samplers references.
	private class ComputeProgram {
		int program ;

		// Attributes
		int a_position;
		int a_texCoord;

		// Sampler2D
		int tex ;
		// Uniforms
		int du ;
		int dv ;
	}
	private ComputeProgram computeProgram;


	private class TextureProgram {
		int program ;

		// Attributes
		int a_position;
		int a_texCoord;

		// Sampler2D
		int tex ;
	}
	private TextureProgram textureProgram;


	// NOTE: Use this counter so we know if we are "pinging" or "ponging"
	//(i.e. if counter is even then FBO A is current, if odd, FBO B is current.)
	int pingPongCounter = 0;

	// NOTE: The texture and framebuffers being ping-ponged.
	private final int textureA;
	private final int textureB;
	private final int fboA;
	private final int fboB;

	// TODO: Arbitrary for now, for easy viewing. Make this configurable.
	private int gridWidth = 256 ;
	private int gridHeight = 256 ;

	float gridResolutionVec2[] = { gridWidth,gridHeight };

	// NOTE: Various buffers used to pass our simple geometry/texel coords to OpenGL.
	private final FloatBuffer vertexBuffer;
	private final FloatBuffer texelBuffer;
	private final ShortBuffer drawListBuffer;

	private final ByteBuffer textureOutputBuffer;

	static final int BYTES_PER_FLOAT = 4 ;


	// number of coordinates per vertex in this array
	static final int COORDS_PER_VERTEX = 3; // x,y,z
	static float squareCoords[] = {
			-1.0f,  1.0f, 0.0f, // top left
			-1.0f, -1.0f, 0.0f, // bottom left
			1.0f, -1.0f, 0.0f,  // bottom right
			1.0f,  1.0f, 0.0f }; // top right

	private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per coord.

	// coordinates per texel.
	static final int COORDS_PER_TEXEL = 2; // u,v
	static float texelCoords[] = {
			0.0f, 1.0f,   // top left
			0.0f, 0.0f,  // bottom left
			1.0f, 0.0f,  // bottom right
			1.0f, 1.0f }; // top right

	private final int texelStride = COORDS_PER_TEXEL * 4 ; // 4 bytes per coord.

	private final short drawOrder[] = { 0, 1, 2, 0, 2, 3 }; // order to draw vertices


	private ByteBuffer randomByteBuffer;

	// These values are provided to us.
	private int realWidth;
	private int realHeight;


	public ComputeGadget(Context context) {
		this.context = context;

		// Initialize vertex byte buffer for shape coordinates
		ByteBuffer bb = ByteBuffer.allocateDirect(squareCoords.length *BYTES_PER_FLOAT);
		bb.order(ByteOrder.nativeOrder());
		vertexBuffer = bb.asFloatBuffer();
		vertexBuffer.put(squareCoords);
		vertexBuffer.position(0);

		// Init texel byte buffer.
		ByteBuffer tbb = ByteBuffer.allocateDirect(texelCoords.length*BYTES_PER_FLOAT);
		tbb.order(ByteOrder.nativeOrder());
		texelBuffer = tbb.asFloatBuffer();
		texelBuffer.put(texelCoords);
		texelBuffer.position(0);

		// Initialize byte buffer for the draw list
		ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
		dlb.order(ByteOrder.nativeOrder());
		drawListBuffer = dlb.asShortBuffer();
		drawListBuffer.put(drawOrder);
		drawListBuffer.position(0);

		// Can be used to monitor the content of the texture, for debugging or getting results from openGL.
		textureOutputBuffer = ByteBuffer.allocateDirect(gridWidth * gridHeight * BYTES_PER_FLOAT).order(ByteOrder.nativeOrder());

		try {
			computeProgram = createAutomataProgram();
			textureProgram = createTextureProgram();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		// Game-of-life PoC specific, Random cell start position buffer.
		byte[] data = new byte[gridWidth*gridHeight*4];
		byte val = 0 ;
		for( int i = 0; i < gridWidth*gridHeight*4; i+= 4) {
			if (Math.random() > 0.5) { val = 0; } else { val = (byte) 255; }
			data[i] = data[i+1] = data[i+2] = val;
			data[i+3] = (byte) 255;
		}
		randomByteBuffer = ByteBuffer.allocateDirect(gridWidth * gridHeight * 4);
		randomByteBuffer.put(data);
		randomByteBuffer.position(0);

		GLES20.glEnable(GLES20.GL_TEXTURE_2D);

		// Create/assign textureA
		int[] temp = {-1}; GLES20.glGenTextures(1, temp, 0);
		textureA = temp[0];

		// Create textureB
		temp[0] = -1; GLES20.glGenTextures(1, temp, 0);
		textureB = temp[0];

		// Create fboA
		temp[0] = -1 ; GLES20.glGenFramebuffers(1, temp, 0);
		fboA = temp[0];

		// Create fboB
		temp[0] = -1 ; GLES20.glGenFramebuffers(1, temp, 0);
		fboB = temp[0];

	}

	private void initTexture( int textureId, ByteBuffer buffer ) {
		// Init
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0); //make texture register 0 active
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
		GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, gridWidth, gridHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, buffer);
	}

	private void initFboA() {
		// Attach texture A to fboA
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboA);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureA, 0);
	}

	private void initFboB() {
		// Attach texture B to fboB
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboB);
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, textureB, 0);
	}

	private ComputeProgram createAutomataProgram() throws IOException {
		ComputeProgram ap = new ComputeProgram();
		// Load the shaders for automata program
		int passthroughVsh = ComputeGadgetRenderer.loadShaderAssetFile(context, GLES20.GL_VERTEX_SHADER, "tp.passthrough.vsh.glsl");
		int automataFsh = ComputeGadgetRenderer.loadShaderAssetFile(context, GLES20.GL_FRAGMENT_SHADER, "tp.automata.fsh.glsl");

		ap.program = GLES20.glCreateProgram();             // create empty OpenGL Program
		GLES20.glAttachShader(ap.program, passthroughVsh);   // add the vertex shader to program
		GLES20.glAttachShader(ap.program, automataFsh); // add the fragment shader to program
		GLES20.glLinkProgram(ap.program);                  // create OpenGL program executables

		// Get handles to uniforms.
		ap.a_position = GLES20.glGetAttribLocation(ap.program, "a_position");
		ap.a_texCoord = GLES20.glGetAttribLocation(ap.program, "a_texCoord");

		ap.tex = GLES20.glGetUniformLocation(ap.program, "tex");
		ap.du = GLES20.glGetUniformLocation(ap.program, "du");
		ap.dv = GLES20.glGetUniformLocation(ap.program, "dv");

		return ap ;
	}

	private TextureProgram createTextureProgram() throws IOException {
		TextureProgram tp = new TextureProgram();

		// Load the shaders for texture program
		// Note the reload, I'm assuming (perhaps wrongly) that we can't share shader instances.
		int passthroughVsh = ComputeGadgetRenderer.loadShaderAssetFile(context, GLES20.GL_VERTEX_SHADER, "tp.passthrough.vsh.glsl");
		int singleTextureFsh = ComputeGadgetRenderer.loadShaderAssetFile(context, GLES20.GL_FRAGMENT_SHADER, "tp.singleTexture.fsh.glsl");

		tp.program = GLES20.glCreateProgram();             // create empty OpenGL Program
		GLES20.glAttachShader(tp.program, passthroughVsh);   // add the vertex shader to program
		GLES20.glAttachShader(tp.program, singleTextureFsh);
		GLES20.glLinkProgram(tp.program);                  // create OpenGL program executables

		// Get handles to uniforms.
		tp.a_position = GLES20.glGetAttribLocation(tp.program, "a_position");
		tp.a_texCoord = GLES20.glGetAttribLocation(tp.program, "a_texCoord");

		tp.tex = GLES20.glGetUniformLocation(tp.program, "tex");

		return tp ;
	}

	// Probably won't need matrix here...
	public void draw(float[] mvpMatrix) {
		// TODO: Fix this hack, init wasn't working from MyGLRenderer.
		if( pingPongCounter == 0 ) {
			initTexture(textureA,null);
			initFboA();
			initTexture(textureB,randomByteBuffer);
			initFboB();
		}

		// Drawing to framebuffer
		if (pingPongCounter % 2 == 0) {
			initFboA();
			readTexture(textureB);
			framebufferDraw(textureB, fboA);
			readTexture(textureB);
			drawTextureToScreen(textureProgram, textureB);
		} else {
			initFboB();
			readTexture(textureA);
			framebufferDraw(textureA, fboB);
			readTexture(textureA);
			drawTextureToScreen(textureProgram, textureA);
		}

		// Increment the ping-pong counter.
		pingPongCounter++;
	}


	/**
	 *
	 * @param textureInput
	 * @param fboOutput
	 */
	private void framebufferDraw( int textureInput, int fboOutput ) {
		// bind FBO X to set textureX as the output texture.
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, fboOutput);

		// set the viewport to be the size of the texture [taken care of by our renderer.]
		GLES20.glViewport(0, 0, (int)gridResolutionVec2[0], (int)gridResolutionVec2[1]);

		// Clear the bound ouput texture
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		// Bind our automata shader
		GLES20.glUseProgram(computeProgram.program);

		//make texture register 0 active
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
		// bind textureX as our 'input' texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureInput);

		GLES20.glUniform1i(computeProgram.tex, 0); //pass textureInput as a sampler to the shader
		float du = 1.0f / gridResolutionVec2[0];
		GLES20.glUniform1f(computeProgram.du, du); //pass in the width of the cells
		float dv = 1.0f / gridResolutionVec2[1];
		GLES20.glUniform1f(computeProgram.dv, dv); //pass in the height of the cells

		// Load the vertex position
		GLES20.glVertexAttribPointer(computeProgram.a_position, 3, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
		// Load the texture coordinate
		GLES20.glVertexAttribPointer(computeProgram.a_texCoord, 2, GLES20.GL_FLOAT, false, texelStride, texelBuffer);

		GLES20.glEnableVertexAttribArray(computeProgram.a_position);
		GLES20.glEnableVertexAttribArray(computeProgram.a_texCoord);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		GLES20.glDisableVertexAttribArray(computeProgram.a_position);
		GLES20.glDisableVertexAttribArray(computeProgram.a_texCoord);

		GLES20.glUseProgram(0); // unbind the shader
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0); // unbind the FBO
	}

	private void drawTextureToScreen(TextureProgram tp, int inputTextureId) {
		GLES20.glViewport(0,0,realWidth,realHeight);

		// Clear done by MyGLRenderer. Check if it still makes sense. (cumulative ops?)
		GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

		GLES20.glUseProgram(tp.program);

		// Make texture register 0 active
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0);

		// Bind texture
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureId);

		// Pass bound texture as a sampler to the shader.
		GLES20.glUniform1i(tp.tex, 0);

		// Assumes you have the dataArray of vertices and texCoords set up...
		// Provide vertex positions
		GLES20.glVertexAttribPointer(tp.a_position, 3, GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
		// Provide texture coordinates
		GLES20.glVertexAttribPointer(tp.a_texCoord, 2, GLES20.GL_FLOAT, false, texelStride, texelBuffer);

		GLES20.glEnableVertexAttribArray(tp.a_position);
		GLES20.glEnableVertexAttribArray(tp.a_texCoord);

		GLES20.glDrawElements(GLES20.GL_TRIANGLES, drawOrder.length, GLES20.GL_UNSIGNED_SHORT, drawListBuffer);

		// Disable vertex array
		GLES20.glDisableVertexAttribArray(tp.a_position);
		GLES20.glDisableVertexAttribArray(tp.a_texCoord);

		GLES20.glUseProgram(0);
	}


	// For debugging purposes
	private void readBoundTexture() {
		GLES20.glReadPixels(0, 0, gridWidth, gridHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, this.textureOutputBuffer);

		int glGetError = GLES20.glGetError();
	}

	private void readTexture(int inputTextureId) {
		GLES20.glActiveTexture(GLES20.GL_TEXTURE0); //make texture register 0 active
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, inputTextureId);
		GLES20.glReadPixels(0, 0, gridWidth, gridHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, this.textureOutputBuffer);

		int glGetError = GLES20.glGetError();
	}


	@Override
	public void onSurfaceSizeChanged(int width, int height) {
		this.realWidth = width;
		this.realHeight = height;
	}
}
