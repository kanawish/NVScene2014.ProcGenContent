package com.kanawish.seminar.nvscene14;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ConfigurationInfo;
import android.opengl.GLSurfaceView;
import android.widget.ImageView;
import com.kanawish.seminar.nvscene14.usage.opengl.LessonOneRenderer;
import com.kanawish.seminar.nvscene14.usage.simplex.SimplexExample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Our main activity.
 *
 * Note: Won't be using UI hiding example, looked janky.
 *
 *
 */
public class MainActivity extends Activity {

	/** Hold a reference to our GLSurfaceView */
	private GLSurfaceView glSurfaceView;
	private GLSurfaceView glSurfaceView2;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);

		findViewById(R.id.dummy_button).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// Generate a new run of noise.
				ImageView mainImageView = (ImageView) findViewById(R.id.image_view);
				mainImageView.setImageBitmap(SimplexExample.buildBitmapExample1());
			}
		});

		// Note: We filter in the AndroidManifest, so we don't need to double check if ES 2.0 is supported.

		// Get this reference to call the onResume/onPause of the surface.
		glSurfaceView = (GLSurfaceView) findViewById(R.id.gl_surface_view);
		glSurfaceView2 = (GLSurfaceView) findViewById(R.id.gl_surface_view2);

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume()
	{
		// The activity must call the GL surface view's onResume() on activity onResume().
		super.onResume();
		glSurfaceView.onResume();
		glSurfaceView2.onResume();

	}

	@Override
	protected void onPause()
	{
		// The activity must call the GL surface view's onPause() on activity onPause().
		super.onPause();
		glSurfaceView.onPause();
		glSurfaceView2.onPause();
	}

}
