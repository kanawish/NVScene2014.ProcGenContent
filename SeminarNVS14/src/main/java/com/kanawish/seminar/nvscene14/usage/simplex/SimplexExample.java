package com.kanawish.seminar.nvscene14.usage.simplex;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.SystemClock;
import android.util.Log;
import com.kanawish.seminar.nvscene14.noise.SimplexNoise;
import com.kanawish.seminar.nvscene14.noise.SimplexNoiseOctave;

/**
 * Source:
 * http://stackoverflow.com/questions/18279456/any-simplex-noise-tutorials-or-resources
 */
public class SimplexExample {

	private static final String TAG = SimplexExample.class.getSimpleName();

	private static Bitmap buildBitmapFromNoiseArray(double[][] data) {
		//this takes and array of doubles between 0 and 1 and generates a grey scale image from them
		Bitmap bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);

		// BufferedImage image = new BufferedImage(data.length,data[0].length, BufferedImage.TYPE_INT_RGB);

		for (int y = 0; y < data[0].length; y++) {
			for (int x = 0; x < data.length; x++) {
				if (data[x][y] > 1) {
					data[x][y] = 1;
				}
				if (data[x][y] < 0) {
					data[x][y] = 0;
				}

				int gray = (int) (255 * data[x][y]);
				int color = Color.argb(255, gray, gray, gray);
				bitmap.setPixel(x, y, color);
			}
		}

		return bitmap;
	}


	/** Build a 512x512 noise bitmap */
	public static Bitmap buildBitmapExample1() {
		Log.v(TAG, "buildBitmapExample1 started.");
		long startTime = SystemClock.elapsedRealtime();

		SimplexNoise simplexNoise = new SimplexNoise(100,0.5,42);

		// This allows you to pick an "area" in the noise grid to be mapped to your bitmap.
		double xStart = 0;
		double XEnd = 512;
		double yStart = 0;
		double yEnd = 512;

		// The bitmap
		int xResolution = 512;
		int yResolution = 512;

		double[][] result = new double[xResolution][yResolution];

		for (int i = 0; i < xResolution; i++) {
			for (int j = 0; j < yResolution; j++) {
				int x = (int) (xStart + i * ((XEnd - xStart) / xResolution));
				int y = (int) (yStart + j * ((yEnd - yStart) / yResolution));
				result[i][j] = 0.5 * (1 + simplexNoise.getNoise(x, y));
			}
		}

		Bitmap bitmap = buildBitmapFromNoiseArray(result);

		long elapsed = SystemClock.elapsedRealtime() - startTime ;
		Log.v(TAG, String.format("buildBitmapExample1() finished after %d ms.", elapsed));

		return bitmap;
	}

}