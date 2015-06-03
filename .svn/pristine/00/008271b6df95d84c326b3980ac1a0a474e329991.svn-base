package com.pt.treasuretrash.adapter;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
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

public class YourFreeListingItemAdapter extends ArrayAdapter<Item> {

	private TreasureTrashBaseActivity act;
	private ArrayList<Item> arrItem;
	private LayoutInflater inflater;
	private AQuery listAq;
	private TreasureTrashDbHelper dbHelper;
	private int totalRow = 0;

	public YourFreeListingItemAdapter(TreasureTrashBaseActivity a,
			int resource, List<Item> objects) {
		super(a, resource, objects);
		// TODO Auto-generated constructor stub
		this.act = a;
		this.arrItem = (ArrayList<Item>) objects;
		inflater = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		dbHelper = new TreasureTrashDbHelper(a);
		totalRow = (this.arrItem.size() / 3) + 1;
		listAq = new AQuery(a);

	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int position = index;
		int messagenubmer = 0;
		View v = convertView;
		final ViewHolder holder;
		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.row_gallery_item, null);
			holder.ivItem = (ImageView) v.findViewById(R.id.ivItem);
			holder.ivStatus = (ImageView) v.findViewById(R.id.ivStatus);
			holder.ivFavorite = (ImageView) v.findViewById(R.id.ivFavorite);
			holder.viewOpaque = v.findViewById(R.id.viewOpaque);
			holder.view1dayToGo = v.findViewById(R.id.view1dayToGo);
			holder.viewOpaque.getLayoutParams().height = act.screenWidth / 3;
			holder.llMyPublishedListing = (LinearLayout) v
					.findViewById(R.id.llMyPublishedListing);
			holder.rlNewMessage = (RelativeLayout) v
					.findViewById(R.id.rlNewMessage);
			holder.llPremium = (LinearLayout) v.findViewById(R.id.llPremium);
			holder.tvNewMessage = (TextView) v.findViewById(R.id.tvNewMessage);

			int dimen = (act.screenWidth - (int) act.getResources()
					.getDimension(R.dimen.width_ll_transparent)) / 3;
			holder.ivItem.getLayoutParams().width = dimen;
			holder.ivItem.getLayoutParams().height = dimen;
			holder.view1dayToGo.getLayoutParams().width = dimen;
			holder.view1dayToGo.getLayoutParams().height = dimen;

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
			aq.id(holder.llPremium).visibility(View.GONE);

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
								aq.id(holder.viewOpaque).visibility(View.GONE);

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
								}
								else if (item.getType().equals(Item.TYPE_QUICK)) {

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
							}

							// check item have to go a day :
							if (item.isToGo()) {
								holder.view1dayToGo.setVisibility(View.VISIBLE);
							} else {
								holder.view1dayToGo.setVisibility(View.GONE);
							}

							// check if have message
							int messagenubmer = dbHelper
									.getNewMessagesCountByItem(item.getId(),
											GlobalValue.myAccount.getId());
							if (messagenubmer > 0) {
								holder.rlNewMessage.setVisibility(View.VISIBLE);
								holder.tvNewMessage.setText(messagenubmer + "");
							} else {
								holder.rlNewMessage
										.setVisibility(View.INVISIBLE);
								holder.tvNewMessage.setText(messagenubmer + "");
							}

						}
					});

		} else {
			final AQuery aq = listAq.recycle(v);

			// check if this only one row
			if (totalRow <= 1) {
				aq.id(holder.ivItem).image(
						R.drawable.ic_add_free_listings_right);
			} else {
				if (((position / 3) + 1) == totalRow) {
					aq.id(holder.ivItem).image(
							R.drawable.ic_add_free_listings_bottom_right);
				} else {
					aq.id(holder.ivItem).image(
							R.drawable.ic_add_free_listings_bottom_right);
				}
			}

//			if (position == (arrItem.size() - 1)) {
//				aq.id(holder.ivItem).image(R.drawable.ic_add_free_listings);
//			}

		}
		return v;
	}

	private class ViewHolder {
		private ImageView ivItem;
		private ImageView ivStatus, ivFavorite;
		private View viewOpaque, view1dayToGo;
		private LinearLayout llMyPublishedListing, llPremium;
		private RelativeLayout rlNewMessage;
		private TextView tvNewMessage;
	}
}
