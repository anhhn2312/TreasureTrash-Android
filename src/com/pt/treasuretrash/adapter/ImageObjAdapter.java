package com.pt.treasuretrash.adapter;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.object.ImageObj;

public class ImageObjAdapter extends BaseAdapter {
	private LayoutInflater inflater;
	private ArrayList<ImageObj> arrImageObj;
	private AQuery listAq;
	private Activity activity;
	private IOnFirstItemLoaded listener;
	private boolean isListener = false;

	public ImageObjAdapter(Activity contextimg, ArrayList<ImageObj> arrImageObj) {
		this.arrImageObj = arrImageObj;
		inflater = (LayoutInflater) contextimg
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listAq = new AQuery(contextimg);
		activity = contextimg;
	}
	
	public void setOnFirstItemLoadedListener(IOnFirstItemLoaded listener){
		this.listener = listener;
	}

	@Override
	public int getCount() {
		return arrImageObj.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		if (convertView == null) {
			holder = new Holder();
			convertView = inflater.inflate(R.layout.row_image_thumb, null);
			holder.image = (ImageView) convertView.findViewById(R.id.imgThumb);
			holder.progress = (ProgressBar) convertView
					.findViewById(R.id.prbImage);
			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}
		final ImageObj image = arrImageObj.get(position);
		if (image != null) {

			listAq.id(holder.image)
					.image(image.getImageUrl(), false, true, 0,
							R.drawable.image_not_available,
							new BitmapAjaxCallback() {
								@SuppressLint("NewApi")
								@Override
								public void callback(String url, ImageView iv,
										Bitmap bm, AjaxStatus status) {
									if (bm != null) {
//										Drawable d = new BitmapDrawable(
//												activity.getResources(), bm);
//										holder.image.setImageDrawable(d);
										listAq.id(holder.image).image(bm, 1.0f, AQuery.FADE_IN);
										if(position == 0 && !isListener){
											listener.onLoaded();
											isListener = true;
										}
									} else {
//										holder.image
//												.setImageDrawable(activity
//														.getResources()
//														.getDrawable(
//																R.drawable.image_not_available));
									}
								}
							});

			Log.d("THUMB_IMAGE", image.getImageUrl());
//			listAq.id(holder.image)
//					.progress(holder.progress)
//					.image(image.getImageUrl(), false, true, 0,
//							R.drawable.image_not_available, null,
//							AQuery.FADE_IN);

		}
		return convertView;
	}

	class Holder {
		ImageView image;
		ProgressBar progress;
	}
	
	@Override
	public void notifyDataSetChanged() {
		// TODO Auto-generated method stub
		super.notifyDataSetChanged();
		isListener = false;
	}
	
	public interface IOnFirstItemLoaded{
		void onLoaded();
	}
}
