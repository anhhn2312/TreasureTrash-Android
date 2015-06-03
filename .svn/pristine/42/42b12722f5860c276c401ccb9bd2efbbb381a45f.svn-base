package com.pt.treasuretrash.adapter;

import java.util.ArrayList;

import com.androidquery.AQuery;
import com.pt.treasuretrash.object.ImageObj;

import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

public class ImagePagerAdapter extends PagerAdapter {

	private Activity mActivity;
	private ArrayList<ImageObj> mArrBitmap;
	private AQuery aq;

	public ImagePagerAdapter(Activity act, ArrayList<ImageObj> arr) {
		this.mActivity = act;
		this.mArrBitmap = arr;
		aq = new AQuery(act);
	}

	@SuppressWarnings("deprecation")
	@Override
	public Object instantiateItem(View collection, int position) {
		ImageView view = new ImageView(mActivity);
		view.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,
				LayoutParams.FILL_PARENT));
		view.setScaleType(ScaleType.FIT_XY);

		ImageObj image = mArrBitmap.get(position);
		if (image.getBitmap() != null) {
			// view.setImageBitmap(bitmap);
			aq.id(view).image(image.getBitmap());
		} else if (!image.getImageUrl().isEmpty()) {
			aq.id(view).image(image.getImageUrl());
		}

		((ViewPager) collection).addView(view, 0);
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
	public boolean isViewFromObject(View arg0, Object arg1) {
		// TODO Auto-generated method stub
		return arg0 == (View) arg1;
	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		((ViewPager) arg0).removeView((View) arg2);
	}
}
