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
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * A view container where OpenGL ES graphics can be drawn on screen.
 * This view can also be used to capture touch events, such as a user
 * interacting with drawn objects.
 *
 * - Manages an EGL display, which enables OpenGL to render into a surface.
 * - Accepts a user-provided Renderer object that does the actual rendering.
 * - Renders on a dedicated thread to decouple rendering performance from the UI thread.
 * - Supports both on-demand and continuous rendering.
 * - Optionally wraps, traces, and/or error-checks the renderer's OpenGL calls.
 *
 */
// NOTE: You typically use GLSurfaceView through sub-classing it, and overriding event methods.
public class ShaderGadgetGLSurfaceView extends GLSurfaceView {

	public static final String NAMESPACE = "http://schemas.android.com/apk/res-auto";

	// NOTE: Unlike a regular View, work is delegated to an instance of the Renderer class.
	private ShaderGadgetRenderer renderer;


	public ShaderGadgetGLSurfaceView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// AttributeSet is provided from xml, see activity_fullscreen.xml
		int intValue = attrs.getAttributeIntValue(NAMESPACE,"type", 1);
		init(context, intValue);
	}

	public ShaderGadgetGLSurfaceView(Context context) {
        super(context);

        // Create an OpenGL ES 2.0 context.
		init(context, 1);
    }

	/**
	 * @param intValue a simple example of optional parameters parsed from XML layouts.
	 */
	private void init(Context context, int intValue) {
		// NOTE: GLSurfaceView behavior is controlled through these setters.

		// Create an OpenGL ES 2.0 context.
		setEGLContextClientVersion(2);

		// Enable debug mode
		setDebugFlags(DEBUG_CHECK_GL_ERROR|DEBUG_LOG_GL_CALLS);

		switch(intValue) {
			case 2 :
				renderer = new ShaderGadgetRenderer(context,"Plasma.fsh.glsl");
				break;
			case 1 :
			default :
				renderer = new ShaderGadgetRenderer(context,"TriangleFractal.fsh.glsl");
				break;
		}

		// Set the Renderer for drawing on the GLSurfaceView
		setRenderer(renderer);

		// NOTE: You could also decide to only render when surface is dirty.
		// NOTE: Can only be called after setRenderer()
		// Render the view only when there is a change in the drawing data
		setRenderMode(RENDERMODE_CONTINUOUSLY);

	}

	// NOTE: Not really used, here as a good example of getting value from GLSurfaceView

	private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
	private float previousX;
	private float previousY;

	/**
	 * This example method maps MotionEvents to a rotation angle.
	 */
	@Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - previousX;
                float dy = y - previousY;

                // reverse direction of rotation above the mid-line
                if (y > getHeight() / 2) {
                    dx = dx * -1 ;
                }

                // reverse direction of rotation to left of the mid-line
                if (x < getWidth() / 2) {
                    dy = dy * -1 ;
                }

				// NOTE: A nice bonus of GLSurfaceView: easy cross-thread communication:
				final float finalDx = dx;
				final float finalDy = dy;
				queueEvent(new Runnable() {
						@Override
						public void run() {
							// Set the angle.
							renderer.setAngle(renderer.getAngle() + ((finalDx + finalDy) * TOUCH_SCALE_FACTOR));
						}
					}
				);

				// If your RenderMode was set to RENDERMODE_DIRTY, this would be the way to go:
                // requestRender();
        }

        previousX = x;
        previousY = y;
        return true;
    }

}
