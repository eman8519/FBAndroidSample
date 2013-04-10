package com.randy.fbsample;

import java.lang.ref.WeakReference;

import android.app.Application;

public class FBSampleApplication extends Application {

	private static WeakReference<FBSampleActivity> mFBSampleActivity;
	
	
	public static void setFBSampleActivity(FBSampleActivity fbSampleActivity) {
		mFBSampleActivity = new WeakReference<FBSampleActivity>(fbSampleActivity);
	}
	
	public static FBSampleActivity getFBSampleActivity() {
		return mFBSampleActivity.get();
	}
}
