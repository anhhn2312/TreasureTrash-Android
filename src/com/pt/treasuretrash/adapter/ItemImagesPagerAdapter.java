package com.pt.treasuretrash.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ImageView.ScaleType;

import com.androidquery.AQuery;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.object.ImageObj;

public class ItemImagesPagerAdapter extends PagerAdapter {

	private Activity mActivity;
	private ArrayList<ImageObj> mArrBitmap;
	private AQuery aq;
	private IOnImageClick listener;
	private LayoutInflater inflater;
	private SparseArray<View> arrViews = new SparseArray<View>();

	public ItemImagesPagerAdapter(Activity act, ArrayList<ImageObj> arr, IOnImageClick listener) {
		this.mActivity = act;
		this.mArrBitmap = arr;
		aq = new AQuery(act);
		inflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listener = listener;
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object instantiateItem(View collection, int position) {
		ImageView view = new ImageView(mActivity);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		view.setScaleType(ScaleType.FIT_XY);
		view.setBackgroundColor(mActivity.getResources().getColor(R.color.transparent));
		
		view.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				listener.onImageClick();
				
			}
		});

		Log.d("PAGER_IMAGE", mArrBitmap.get(position).getImageUrl());
		
		aq.id(view).image(mArrBitmap.get(position).getImageUrl(), false, true, 0, 0, null, AQuery.FADE_IN);
		((ViewPager) collection).addView(view, 0);
		
		arrViews.put(position, view);
		
		return view;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		try {
			return mArrBitmap.size();
		} catch (NullPointerException ex) {
			return 0;
		}
	}
	
	@Override
	public int getItemPosition(Object object) {
		// TODO Auto-generated method stub
		return POSITION_NONE;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == (View) arg1;
	}

	@Override
	public void destroyItem(View collection, int position, Object o) {
		((ViewPager) collection).removeView((View) o);
		arrViews.remove(position);
		o = null;
	}
	
	public interface IOnImageClick{
		void onImageClick();
		
	}
	
//	@Override
//	public void notifyDataSetChanged() {
//	    int key = 0;
//	    for(int i = 0; i < arrViews.size(); i++) {
//	       key = arrViews.keyAt(i);
//	       View view = arrViews.get(key);
//	       //<refresh view with new data>
//	    }
//	    super.notifyDataSetChanged();
//	}
	
	
	private class ViewHolder {
		private ImageView ivItem;
		private ImageView ivStatus, ivFavorite;
		private View viewOpaque;
		private LinearLayout llMyPublishedListing;
	}
}
