package com.randy.fbsample.adapters;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.randy.fbsample.R;
import com.viewpagerindicator.LinePageIndicator;


public class PhotoNewsListViewAdapter extends ArrayAdapter<JSONObject> {
	private static final String TAG = "PhotoNewsListViewAdapter";
	
	private Context mContext;
	private List<JSONObject> mData;

	public PhotoNewsListViewAdapter(Context context, List<JSONObject> photoList) {
		super(context, R.layout.photo_news_item, photoList);
		mContext = context;
		mData = photoList;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View toReturn = convertView;
		if (toReturn == null) {
			LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			toReturn = inflater.inflate(R.layout.photo_news_item, parent, false);
		}
		
		JSONObject currentPhotoPost = mData.get(position);
		
		ViewPager photoViewPager = (ViewPager)toReturn.findViewById(R.id.photos_view_pager);
		LinePageIndicator photoPageIndicator = (LinePageIndicator)toReturn.findViewById(R.id.photos_page_indicator);
		TextView photoMessages = (TextView)toReturn.findViewById(R.id.message_text_view);
		
		photoPageIndicator.setUnselectedColor(Color.BLACK);
		photoMessages.setText(getMessageString(currentPhotoPost));
		
		photoViewPager.setAdapter(new PhotoViewPagerAdapter(getPhotosFromMedia(currentPhotoPost)));
		photoPageIndicator.setViewPager(photoViewPager);
		
		if (photoViewPager.getAdapter().getCount() < 2) {
			photoPageIndicator.setVisibility(View.GONE);
		} else {
			photoPageIndicator.setVisibility(View.VISIBLE);
		}
		
		return toReturn;
	}
	
	private String getMessageString(JSONObject currentObj) {
		try {
			if (currentObj.has("message") && currentObj.getString("message").length() > 0) { //get message
				return currentObj.getString("message");
			}
		} catch (Exception e) {
			Log.e(TAG, "Error parsing JSON - " + Log.getStackTraceString(e));
			return "No message for post.";
		}
		
		return "No message for post.";
	}
	
	private ArrayList<JSONObject> getPhotosFromMedia(JSONObject currentObj) {
		ArrayList<JSONObject> toReturn = new ArrayList<JSONObject>();
		
		try {
			if (currentObj.has("attachment")) {
				JSONObject attachment = currentObj.getJSONObject("attachment");
				if (attachment.has("media")) {
					JSONArray mediaArray = attachment.getJSONArray("media");
					
					for(int i = 0; i < mediaArray.length(); i++) {
						JSONObject mediaItem = mediaArray.getJSONObject(i);
						
						if (mediaItem.has("type") && mediaItem.getString("type").equals("photo")) {
							toReturn.add(mediaItem);
						}
					}
				}
			}
		} catch(Exception e) {
			Log.e(TAG, "Error parsing media JSONArray - " + Log.getStackTraceString(e));
		}
		
		return toReturn;
	}
}
