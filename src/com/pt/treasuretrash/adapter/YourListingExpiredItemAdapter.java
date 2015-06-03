package com.pt.treasuretrash.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.databasehelper.TreasureTrashDbHelper;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.widget.HelveticaLightTextView;

public class YourListingExpiredItemAdapter extends ArrayAdapter<Item> {

	private TreasureTrashBaseActivity act;
	private ArrayList<Item> arrItem;
	private LayoutInflater inflater;
	private AQuery listAq;
	private TreasureTrashDbHelper dbHelper;

	public YourListingExpiredItemAdapter(TreasureTrashBaseActivity a,
			int resource, List<Item> objects) {
		super(a, resource, objects);
		// TODO Auto-generated constructor stub
		this.act = a;
		this.arrItem = (ArrayList<Item>) objects;
		inflater = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listAq = new AQuery(a);
		dbHelper = new TreasureTrashDbHelper(a);

	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int position = index;
		View v = convertView;
		final ViewHolder holder;
		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.row_expired_listing_item, null);
			holder.ivItem = (ImageView) v.findViewById(R.id.ivItem);
			holder.ivStatus = (ImageView) v.findViewById(R.id.ivStatus);
			holder.ivFavorite = (ImageView) v.findViewById(R.id.ivFavorite);
			holder.viewOpaque = v.findViewById(R.id.viewOpaque);
			holder.llMyPublishedListing = (LinearLayout) v
					.findViewById(R.id.llMyPublishedListing);
			holder.llPremium = (LinearLayout) v.findViewById(R.id.llPremium);
			holder.rlNewMessage = (RelativeLayout) v
					.findViewById(R.id.rlNewMessage);
			holder.tvNewMessage = (TextView) v.findViewById(R.id.tvNewMessage);
			holder.rltExpiredItem = (RelativeLayout) v
					.findViewById(R.id.rlt_expired_item);
			// holder.rltExpiredItem.setOnClickListener(null);
			// holder.lblRelist = (HelveticaLightTextView) v
			// .findViewById(R.id.lbl_relist);
			// holder.lblRelist.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// relist(position);
			// }
			// });

			int dimen = (act.screenWidth - (int) act.getResources()
					.getDimension(R.dimen.width_ll_transparent)) / 3;
			holder.ivItem.getLayoutParams().width = dimen;
			holder.ivItem.getLayoutParams().height = dimen;
			holder.viewOpaque.getLayoutParams().height = dimen;

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		final Item item = arrItem.get(position);
		if (item != null) {

			final AQuery aq = listAq.recycle(v);

			aq.id(holder.ivStatus).visibility(View.GONE);
			aq.id(holder.viewOpaque).visibility(View.GONE);
			aq.id(holder.ivFavorite).visibility(View.GONE);
			aq.id(holder.llMyPublishedListing).visibility(View.GONE);

			// aq.id(holder.ivItem).image(item.getImage(), false, true, 0, 0,
			// null,
			// 0, 1.0f);
			aq.id(holder.ivItem).image(item.getImage(), false, true, 0, 0,
					new BitmapAjaxCallback() {
						@SuppressLint("NewApi")
						@Override
						public void callback(String url, ImageView iv,
								Bitmap bm, AjaxStatus status) {
							if (bm == null) {
								// aq.id(holder.ivItem).image(
								// R.drawable.image_not_available);
							} else {
								aq.id(holder.ivItem).image(bm, 1.0f,
										AQuery.FADE_IN);
								aq.id(holder.ivStatus).visibility(View.GONE);
								aq.id(holder.viewOpaque).visibility(
										View.VISIBLE);

								if (item.getState().equalsIgnoreCase(
										Item.STATE_GONE)) {
									aq.id(holder.ivStatus).visibility(
											View.VISIBLE);
									aq.id(holder.viewOpaque).visibility(
											View.VISIBLE);
									aq.id(holder.ivStatus).image(
											R.drawable.image_item_gone);

								} else if (item.getType()
										.equals(Item.TYPE_FREE)) {

									aq.id(holder.ivStatus).visibility(
											View.VISIBLE);
									aq.id(holder.ivStatus).image(
											R.drawable.image_item_free);
								} else if (item.getType().equals(
										Item.TYPE_PREMIUM)) {
									aq.id(holder.llPremium).visibility(
											View.VISIBLE);

								} else if (item.getType().equals(
										Item.TYPE_QUICK)) {

									aq.id(holder.ivStatus).visibility(
											View.VISIBLE);
									aq.id(holder.ivStatus).image(
											R.drawable.image_item_quick);
								}

								if (item.getMyPublishedListing() == 1) {
									aq.id(holder.llMyPublishedListing)
											.visibility(View.VISIBLE);
								} else {
									aq.id(holder.llMyPublishedListing)
											.visibility(View.GONE);
								}

								// check if have message
								int messagenubmer = dbHelper
										.getNewMessagesCountByItem(
												item.getId(),
												GlobalValue.myAccount.getId());
								
								if (messagenubmer > 0) {
									aq.id(holder.rlNewMessage).visibility(
											View.VISIBLE);
									aq.id(holder.tvNewMessage).text(
											messagenubmer + "");
								} else {
									aq.id(holder.rlNewMessage).visibility(
											View.INVISIBLE);
									aq.id(holder.tvNewMessage).text(
											messagenubmer + "");
								}
							}
						}
					});

		}
		return v;
	}


	private class ViewHolder {
		private ImageView ivItem;
		private ImageView ivStatus, ivFavorite;
		private View viewOpaque;
		private LinearLayout llMyPublishedListing, llPremium;
		private RelativeLayout rltExpiredItem;
		private HelveticaLightTextView lblRelist;
		private RelativeLayout rlNewMessage;
		private TextView tvNewMessage;
	}
}
