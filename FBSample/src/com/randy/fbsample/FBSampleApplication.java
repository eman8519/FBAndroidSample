package com.randy.fbsample;

import android.app.Application;

public class FBSampleApplication extends Application {

	private static FBSampleActivity mFBSampleActivity;
	
	
	public static void setFBSampleActivity(FBSampleActivity fbSampleActivity) {
		mFBSampleActivity = fbSampleActivity;
	}
	
	public static FBSampleActivity getFBSampleActivity() {
		return mFBSampleActivity;
	}
}
