package com.pt.treasuretrash.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class DemoAdapter extends BaseAdapter {

//	private ArrayList<Category> mArrCategory;
//	private LayoutInflater mInflater;
//	private AQuery aq;
//	private Context context;
//
//	public CategoryAdapter(Context activity, ArrayList<Category> arrCategory) {
//		this.mArrCategory = arrCategory;
//		this.context = activity;
//		this.mInflater = (LayoutInflater) activity
//				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		aq = new AQuery(activity);
//	}
//
//	@Override
//	public int getCount() {
//		// TODO Auto-generated method stub
//		return mArrCategory.size();
//	}
//
//	@Override
//	public Object getItem(int position) {
//		// TODO Auto-generated method stub
//		return mArrCategory.get(position);
//	}
//
//	@Override
//	public long getItemId(int position) {
//		// TODO Auto-generated method stub
//		return position;
//	}
//
//	@Override
//	public View getView(int position, View convertView, ViewGroup parent) {
//		// TODO Auto-generated method stub
//		final HolderView holder;
//		if (convertView == null) {
//			holder = new HolderView();
//			// convertView = mInflater.inflate(R.layout.layout_item_history,
//			// null);
//			// holder.lblResNameHistory = (TextView)
//			// convertView.findViewById(R.id.lblResName_History);
//			convertView = mInflater.inflate(R.layout.row_category_list, null);
//			holder.imgCategory = (ImageView) convertView
//					.findViewById(R.id.imgFavourite);
//			holder.lblCategoryName = (TextView) convertView
//					.findViewById(R.id.lblFavourite);
//			holder.progressBar = (ProgressBar) convertView
//					.findViewById(R.id.progressBar1);
//			convertView.setTag(holder);
//		} else {
//			holder = (HolderView) convertView.getTag();
//		}
//		Category Category = mArrCategory.get(position);
//		if (Category != null) {
//			holder.lblCategoryName.setText(Category.getName());
//			// aq.id(holder.imgCategory).image(Category.getImage());
//
//			aq.id(holder.imgCategory).image(Category.getImage());
//			aq.id(holder.imgCategory)
//					.progress(holder.progressBar)
//					.image(Category.getImage(), true, true, 0,
//							R.drawable.no_image_available_horizontal,
//							new BitmapAjaxCallback() {
//								@SuppressLint("NewApi")
//								@Override
//								public void callback(String url, ImageView iv,
//										Bitmap bm, AjaxStatus status) {
//									if (bm != null) {
//										Drawable d = new BitmapDrawable(context
//												.getResources(), bm);
//										holder.imgCategory
//												.setBackgroundDrawable(d);
//									} else {
//										holder.imgCategory
//												.setBackgroundResource(R.drawable.no_image_available_horizontal);
//									}
//								}
//							});
//		}
//		return convertView;
//	}

	public class HolderView {
		private TextView lblCategoryName;
		private ImageView imgCategory;
		private ProgressBar progressBar;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
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
		return null;
	}

}
