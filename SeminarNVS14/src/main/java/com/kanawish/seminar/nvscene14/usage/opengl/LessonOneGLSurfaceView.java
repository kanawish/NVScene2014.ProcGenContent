package com.kanawish.seminar.nvscene14.usage.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;

/**
 * TODO: Could probably get the renderer, gl version from xml layout, would be cleaner.
 */
public class LessonOneGLSurfaceView extends GLSurfaceView {

	private final LessonOneRenderer renderer ;

	public LessonOneGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);

		setEGLContextClientVersion(2);

		this.renderer = new LessonOneRenderer();
		setRenderer(this.renderer);

		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

	public LessonOneGLSurfaceView(Context context) {
		super(context);

		setEGLContextClientVersion(2);

		this.renderer = new LessonOneRenderer();
		setRenderer(this.renderer);

		setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
	}

}
