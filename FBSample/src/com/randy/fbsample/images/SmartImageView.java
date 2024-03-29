package com.randy.fbsample.images;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

public class SmartImageView extends ImageView {
    private static final int LOADING_THREADS = 4;
    private static ExecutorService threadPool = Executors.newFixedThreadPool(LOADING_THREADS);

    private SmartImageTask currentTask;


    public SmartImageView(Context context) {
        super(context);
    }

    public SmartImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SmartImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }


    // Helpers to set image by URL
    public void setImageUrl(String url) {
        setImage(new WebImage(url));
    }
    
    public void setImageUrl(String url, int w , int h) {
        setImage(new WebImage(url, w, h));
    }

    public void setImageUrl(String url, final Integer fallbackResource) {
        setImage(new WebImage(url), fallbackResource);
    }

    public void setImageUrl(String url, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new WebImage(url), fallbackResource, loadingResource);
    }


    // Helpers to set image by contact address book id
    public void setImageContact(long contactId) {
        setImage(new ContactImage(contactId));
    }

    public void setImageContact(long contactId, final Integer fallbackResource) {
        setImage(new ContactImage(contactId), fallbackResource);
    }

    public void setImageContact(long contactId, final Integer fallbackResource, final Integer loadingResource) {
        setImage(new ContactImage(contactId), fallbackResource, fallbackResource);
    }


    // Set image using SmartImage object
    public void setImage(final SmartImage image) {
        setImage(image, null, null);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource) {
        setImage(image, fallbackResource, fallbackResource);
    }

    public void setImage(final SmartImage image, final Integer fallbackResource, final Integer loadingResource) {
        // Set a loading resource
        if(loadingResource != null){
            setImageResource(loadingResource);
        }
        
        Bitmap myBitmap = image.getBitmapFromCache(getContext());
        if(myBitmap == null){
	        // Cancel any existing tasks for this image view
	        if(currentTask != null) {
	            currentTask.cancel();
	            currentTask = null;
	        }
	
	        // Set up the new task
	        currentTask = new SmartImageTask(getContext(), image);
	        currentTask.setOnCompleteHandler(new SmartImageTask.OnCompleteHandler() {
	            @Override
	            public void onComplete(Bitmap bitmap) {
	                if(bitmap != null) {
	                    setImageBitmap(bitmap);
	                } else {
	                    // Set fallback resource
	                    if(fallbackResource != null) {
	                        setImageResource(fallbackResource);
	                    }
	                }
	            }
	        });
	
	        // Run the task in a threadpool
	        threadPool.execute(currentTask);
        }else{
        	setImageBitmap(myBitmap);
        }
    }

    public static void cancelAllTasks() {
        threadPool.shutdownNow();
        threadPool = Executors.newFixedThreadPool(LOADING_THREADS);
    }
}