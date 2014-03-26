package com.kanawish.seminar.nvscene14;

import android.opengl.GLSurfaceView;
import android.widget.ImageView;
import com.kanawish.seminar.nvscene14.usage.simplex.SimplexExample;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

/**
 * Our main activity.
 *
 */
public class MainActivity extends Activity {

	/** Hold a reference to our GLSurfaceView */
	private GLSurfaceView glSurfaceView;

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

		super.onCreate(savedInstanceState);
	}

	@Override
	protected void onResume()
	{
		// The activity must call the GL surface view's onResume() on activity onResume().
		super.onResume();
		glSurfaceView.onResume();

	}

	@Override
	protected void onPause()
	{
		// The activity must call the GL surface view's onPause() on activity onPause().
		super.onPause();
		glSurfaceView.onPause();
	}

}
