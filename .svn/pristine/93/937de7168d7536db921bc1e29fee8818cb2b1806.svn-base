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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.object.Item;

public class ItemAdapter extends BaseAdapter {

	private TreasureTrashBaseActivity act;
	private ArrayList<Item> arrItem;
	private LayoutInflater inflater;
	private AQuery listAq;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private boolean isDisplayedFirstTime;

	public ItemAdapter(TreasureTrashBaseActivity a, int resource,
			List<Item> objects) {
		
		// TODO Auto-generated constructor stub
		this.act = a;
		this.arrItem = (ArrayList<Item>) objects;
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
		final Item item = arrItem.get(position);
		isDisplayedFirstTime = true;

		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.row_gallery_item, null);
			holder.ivItem = (ImageView) v.findViewById(R.id.ivItem);
			holder.ivStatus = (ImageView) v.findViewById(R.id.ivStatus);
			holder.ivFavorite = (ImageView) v.findViewById(R.id.ivFavorite);
			holder.viewOpaque = v.findViewById(R.id.viewOpaque);
			holder.viewOpaque.getLayoutParams().height = act.screenWidth / 3;
			holder.llMyPublishedListing = (LinearLayout) v
					.findViewById(R.id.llMyPublishedListing);
			holder.llPremium = (LinearLayout) v.findViewById(R.id.llPremium);

			holder.ivItem.getLayoutParams().height = act.screenWidth / 3;
			holder.ivItem.getLayoutParams().width = act.screenWidth / 3;

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
			isDisplayedFirstTime = false;

			if (holder.itemId.equals(item.getId()) && !item.isNeedToUpdate()) {
				return v;
			}
			else{
				item.setNeedToUpdate(false);
			}
		}

		if (item != null) {

			final AQuery aq = listAq.recycle(v);

			holder.itemId = item.getId();

			aq.id(holder.ivStatus).visibility(View.GONE);
			aq.id(holder.viewOpaque).visibility(View.GONE);
			aq.id(holder.ivFavorite).visibility(View.GONE);
			aq.id(holder.llMyPublishedListing).visibility(View.GONE);
			aq.id(holder.llPremium).visibility(View.GONE);

//			Log.d("ITEM", item.getImage());

			aq.id(holder.ivItem).image(item.getImage(), false, true, 0, 0,
					new BitmapAjaxCallback() {
						@SuppressLint("NewApi")
						@Override
						public void callback(String url, ImageView iv,
								Bitmap bm, AjaxStatus status) {
							if (bm == null) {
							} else {
								aq.id(holder.ivItem).image(bm, 1.0f, isDisplayedFirstTime?0:AQuery.FADE_IN);
								// aq.id(holder.ivItem).image(bm, 1.0f,
								// AQuery.FADE_IN);
								aq.id(holder.ivStatus).visibility(View.GONE);
								aq.id(holder.viewOpaque).visibility(View.GONE);

								if (item.getType().equals(Item.TYPE_PREMIUM)) {
									aq.id(holder.llPremium).visibility(
											View.VISIBLE);
								}

								if (item.getState().equalsIgnoreCase(
										Item.STATE_GONE)) {
									aq.id(holder.ivStatus).visibility(
											View.VISIBLE);
									aq.id(holder.viewOpaque).visibility(
											View.VISIBLE);
									aq.id(holder.ivStatus).image(
											R.drawable.image_item_gone);
									aq.id(holder.llPremium).visibility(
											View.INVISIBLE);

								} else if (item.getType()
										.equals(Item.TYPE_FREE)) {

									aq.id(holder.ivStatus).visibility(
											View.VISIBLE);
									aq.id(holder.ivStatus).image(
											R.drawable.image_item_free);
								} else if (item.getType().equals(
										Item.TYPE_QUICK)) {

									aq.id(holder.ivStatus).visibility(
											View.VISIBLE);
									aq.id(holder.ivStatus).image(
											R.drawable.image_item_quick);
								}

								if (item.isFavourited()) {

									aq.id(holder.ivFavorite).visibility(
											View.VISIBLE);
								} else {
									aq.id(holder.ivFavorite).visibility(
											View.GONE);
								}
								// if (item.getMyPublishedListing() == 1) {
								// aq.id(holder.llMyPublishedListing)
								// .visibility(View.VISIBLE);
								// } else {
								// aq.id(holder.llMyPublishedListing)
								// .visibility(View.GONE);
								// }
							}
						}
					});

		}
		return v;
	}

	private class ViewHolder {
		private ImageView ivItem;
		private ImageView ivStatus, ivFavorite;
		private View viewOpaque, viewTransparentPublish;
		private LinearLayout llMyPublishedListing, llPremium;
		private String itemId;
	}
	

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return arrItem.size();
	}

	@Override
	public Item getItem(int position) {
		// TODO Auto-generated method stub
		return arrItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
