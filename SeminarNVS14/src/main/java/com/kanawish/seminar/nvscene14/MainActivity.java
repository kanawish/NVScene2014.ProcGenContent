package com.kanawish.seminar.nvscene14;

import android.widget.ImageView;
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
    }

}
