package com.pt.treasuretrash.utility;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Handler;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class AndroidBug5497Workaround {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    public static void assistActivity (Activity activity, IToggleFullScreen listener) {
        new AndroidBug5497Workaround(activity, listener);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private IToggleFullScreen listener;
    private Handler handler;

    private AndroidBug5497Workaround(final Activity activity, IToggleFullScreen listener) {
    	
    	this.listener = listener;
    	handler = new Handler();
    	
        FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {
                possiblyResizeChildOfContent(activity);
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent(Activity activity) {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard/4)) {
                // keyboard probably just became visible
//                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference;
            	clearFullScreen(activity);
            } else {
                // keyboard probably just became hidden
//                frameLayoutParams.height = usableHeightSansKeyboard;
            	forceFullScreen(activity);
            }
//            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }
    
    private void clearFullScreen(final Activity activity){
    	activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        if(listener != null){
        	listener.onNotFullscreen();
        }
        
//        handler.postDelayed(new Runnable() {
//			
//			@Override
//			public void run() {
//				forceFullScreen(activity);
//				
//			}
//		}, 500);
    }
    
    private void forceFullScreen(Activity activity){
    	 activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
         activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
         
         if(listener != null){
         	listener.onFullscreen();
         }
    }
    
    public interface IToggleFullScreen{
    	void onNotFullscreen();
    	void onFullscreen();
    }
    

}
