package com.randy.fbsample.images;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

public class WebImage implements SmartImage {
	private static final int CONNECT_TIMEOUT = 5000;
	private static final int READ_TIMEOUT = 10000;

	private static WebImageCache webImageCache;

	private String url;
	
	private int reqWidth = 0;
	private int reqHeight = 0;

	public WebImage(String url) {
		this.url = url;
	}
	
	public WebImage(String url, int w, int h) {
		this.url = url;
		this.reqWidth = w;
		this.reqHeight = h;
	}

	public Bitmap getBitmapFromCache(Context context) {
		// Don't leak context
		if (webImageCache == null) {
			webImageCache = new WebImageCache(context);
		}

		// Try getting bitmap from cache first
		Bitmap bitmap = null;
		if (url != null) {
			bitmap = webImageCache.get(url);
		}
		return bitmap;
	}

	public Bitmap getBitmap(Context context) {
		Bitmap bitmap = getBitmapFromUrl(url);
		if (bitmap != null) {
			webImageCache.put(url, bitmap);
		}

		return bitmap;
	}

	private Bitmap getBitmapFromUrl(String url) {
		Bitmap bitmap = null;

		try {
			URLConnection conn = new URL(url).openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			if (reqWidth == 0 && reqHeight == 0) {
				bitmap = BitmapFactory
						.decodeStream((InputStream) conn.getContent());	
			} else {
				
				bitmap = decodeSampledBitmapFromResource(url, reqWidth, reqHeight);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return bitmap;
	}

	public static void removeFromCache(String url) {
		if (webImageCache != null) {
			webImageCache.remove(url);
		}
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {
			if (width > height) {
				inSampleSize = Math.round((float) height / (float) reqHeight);
			} else {
				inSampleSize = Math.round((float) width / (float) reqWidth);
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromResource(String res, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		
		try {
			URLConnection conn = new URL(res).openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			BitmapFactory.decodeStream((InputStream) conn.getContent(), null, options);
		} catch (Exception e) {
			
		}
		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		try {
			URLConnection conn = new URL(res).openConnection();
			conn.setConnectTimeout(CONNECT_TIMEOUT);
			conn.setReadTimeout(READ_TIMEOUT);
			 return BitmapFactory.decodeStream((InputStream) conn.getContent(), null, options);
		} catch (Exception e) {
			return null;
		}
	}
}