package com.randy.fbsample.images;

import android.content.Context;
import android.graphics.Bitmap;

public interface SmartImage {
	public Bitmap getBitmapFromCache(Context context);
    public Bitmap getBitmap(Context context);
}