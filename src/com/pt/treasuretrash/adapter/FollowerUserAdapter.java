package com.pt.treasuretrash.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.LocationObj;

public class FollowerUserAdapter extends ArrayAdapter<Account> {
	private TreasureTrashBaseActivity act;
	private ArrayList<Account> arrUsers;
	private LayoutInflater inflater;
	private AQuery listAq;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;

	public FollowerUserAdapter(TreasureTrashBaseActivity a, int resource,
			List<Account> objects) {
		super(a, resource, objects);
		// TODO Auto-generated constructor stub
		this.act = a;
		this.arrUsers = (ArrayList<Account>) objects;
		inflater = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listAq = new AQuery(a);

	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int position = index;
		View v = convertView;
		final ViewHolder holder;
		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.row_follower_user, null);
			holder.ivAvatar = (ImageView) v.findViewById(R.id.ivAvatar);
			holder.ivAction = (ImageView) v.findViewById(R.id.ivAction);
			holder.tvUserName = (TextView) v.findViewById(R.id.tvUserName);
			holder.tvLocation = (TextView) v.findViewById(R.id.tvLocation);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		final Account user = arrUsers.get(position);
		if (user != null) {

			final AQuery aq = listAq.recycle(v);

			aq.id(holder.ivAvatar).image(user.getImageUrl(), false, true, 0,
					R.drawable.image_avatar_default_no_border);
			if (user.isFollowed()) {
				aq.id(holder.ivAction).image(R.drawable.btn_followed);
			} else {
				aq.id(holder.ivAction).image(R.drawable.btn_add_follow);
			}

			String location = "";
			LocationObj locationObj = user.getLocation();
			location += locationObj.getDistrict().equals("") ? "" : locationObj
					.getDistrict() + ", ";
			location += locationObj.getState().equals("") ? "" : locationObj
					.getState() + ", ";
			location += locationObj.getCountry().equals("") ? "" : locationObj
					.getCountry();

			aq.id(holder.tvUserName).text(user.getUsername());
			aq.id(holder.tvLocation).text(location);

		}
		return v;
	}

	private class ViewHolder {
		private ImageView ivAvatar, ivAction;
		private TextView tvUserName, tvLocation;

	}
}
