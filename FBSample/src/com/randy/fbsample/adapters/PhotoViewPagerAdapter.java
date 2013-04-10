package com.randy.fbsample.adapters;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView.ScaleType;

import com.randy.fbsample.images.SmartImageView;

public class PhotoViewPagerAdapter extends PagerAdapter {
	private static final String TAG = "PhotoViewPagerAdapter";
	
	private ArrayList<JSONObject> mData;
	
	public PhotoViewPagerAdapter(ArrayList<JSONObject> data) {
		mData = data;
	}
	
	@Override
	public int getCount() {
		if (mData != null) {
			return mData.size();
		}
		return 0;
	}
	
	@Override
	public Object instantiateItem(ViewGroup viewGroup, int position) {
		SmartImageView toReturn = new SmartImageView(viewGroup.getContext());
		FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		toReturn.setLayoutParams(lp);
		
		toReturn.setScaleType(ScaleType.FIT_CENTER);
		
		JSONObject currentObject = mData.get(position);
		
		try {
			if (currentObject.has("src")) {
				toReturn.setImageUrl(currentObject.getString("src"));
			}
		} catch(Exception e) {
			Log.e(TAG, "Error parsing media item JSON - " + Log.getStackTraceString(e));
		}
		viewGroup.addView(toReturn, 0);
		return toReturn;
	}

	@Override
	public boolean isViewFromObject(View view, Object key) {
		//My views will be my object keys for the adapter.
		return view == key;
	}
	
	@Override
	public void destroyItem(ViewGroup collection, int position, Object view) {
		collection.removeView((View) view);
	}


	@Override
    public Parcelable saveState() {
            return null;
    } 

}
