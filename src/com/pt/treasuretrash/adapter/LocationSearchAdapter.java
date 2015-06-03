package com.pt.treasuretrash.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.object.LocationObj;

public class LocationSearchAdapter extends BaseAdapter {

	private LayoutInflater mInflater;
	private ArrayList<LocationObj> mArrLocation;
	private Activity mActivity;

	public LocationSearchAdapter(Activity act, ArrayList<LocationObj> arrLoc) {
		this.mActivity = act;
		this.mArrLocation = arrLoc;
		mInflater = (LayoutInflater) act
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		try {
			return mArrLocation.size();
		} catch (NullPointerException ex) {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Holder holder = null;
		if (convertView == null) {
			holder = new Holder();
			convertView = mInflater
					.inflate(R.layout.item_location_search, null);

			holder.lblLocation = (TextView) convertView
					.findViewById(R.id.lbl_location_search);

			convertView.setTag(holder);
		} else {
			holder = (Holder) convertView.getTag();
		}

		LocationObj locObj = mArrLocation.get(position);
		if (locObj != null) {
			if (!locObj.getDistrict().equals("")
					&& !locObj.getState().equals("")
					&& !locObj.getCountry().equals("")) {
				holder.lblLocation.setText(locObj.getDistrict() + ", "
						+ locObj.getState() + ", " + locObj.getCountry());
			} else if (locObj.getDistrict().equals("")
					&& !locObj.getState().equals("")
					&& !locObj.getCountry().equals("")) {
				holder.lblLocation.setText(locObj.getState() + ", "
						+ locObj.getCountry());
			} else if (locObj.getState().equals("")
					&& locObj.getDistrict().equals("")
					&& !locObj.getCountry().equals("")) {
				holder.lblLocation.setText(locObj.getCountry());
			}
		}
		
		convertView.setBackgroundResource(R.drawable.bg_click_white);

		return convertView;
	}

	class Holder {
		TextView lblLocation;
	}
}
