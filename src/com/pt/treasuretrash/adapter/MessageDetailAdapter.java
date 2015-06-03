package com.pt.treasuretrash.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.androidquery.AQuery;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.object.MessageItem;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MessageDetailAdapter extends ArrayAdapter<MessageItem> {

	private Activity act;
	private ArrayList<MessageItem> arrMessageItem;
	private LayoutInflater inflater;
	private AQuery listAq;
	private ImageLoader imageLoader;
	private DisplayImageOptions displayOptions;
	private DisplayImageOptions options;
	private SimpleDateFormat sdfDayMonthYear = new SimpleDateFormat(
			"dd MMM yyyy");
	private SimpleDateFormat sdfHourMinute = new SimpleDateFormat("hh:mma");

	private IMessageDetail listener;

	public MessageDetailAdapter(Activity a, int resource,
			List<MessageItem> objects, IMessageDetail listener) {
		super(a, resource, objects);
		// TODO Auto-generated constructor stub
		this.act = a;
		this.arrMessageItem = (ArrayList<MessageItem>) objects;
		inflater = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listAq = new AQuery(a);
		this.listener = listener;
		initImageLoader();

	}

	private void initImageLoader() {
		ImageLoaderConfiguration config = (new ImageLoaderConfiguration.Builder(
				act).threadPriority(Thread.NORM_PRIORITY)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);

		displayOptions = new DisplayImageOptions.Builder().cacheOnDisc()
				.resetViewBeforeLoading()
				.showImageForEmptyUri(R.drawable.image_avatar_default)
				.showImageOnFail(R.drawable.image_avatar_default)
				.displayer(new RoundedBitmapDisplayer(0)).build();
	}

	@SuppressWarnings("deprecation")
	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int position = index;
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.row_message_detail, null);
			holder.ivAvatar = (ImageView) v.findViewById(R.id.ivAvatar);
			holder.tvMessage = (TextView) v.findViewById(R.id.tvMessage);
			holder.tvDate = (TextView) v.findViewById(R.id.tvDate);
			holder.llMessage = (LinearLayout) v.findViewById(R.id.llMessage);
			holder.llMessageContainer = (LinearLayout) v
					.findViewById(R.id.llMessageContainer);

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		final MessageItem item = arrMessageItem.get(position);
		if (item != null) {

			holder.tvMessage.setText(item.getMessage());

			long dateInLong = item.getDate();
			String date = sdfDayMonthYear.format(new Date(dateInLong * 1000))
					.toUpperCase();
			String hour = sdfHourMinute.format(new Date(dateInLong * 1000))
					.toUpperCase();
			holder.tvDate.setText(date + " at " + hour);

			if (item.getUserId().equals(GlobalValue.myAccount.getId())) {
				// Display on the right

				holder.llMessageContainer.setGravity(Gravity.RIGHT);

				RelativeLayout.LayoutParams avatarParams = new RelativeLayout.LayoutParams(
						(int) act.getResources().getDimension(
								R.dimen.button_size_lnormal), (int) act
								.getResources().getDimension(
										R.dimen.button_size_lnormal));
				avatarParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				holder.ivAvatar.setLayoutParams(avatarParams);

				RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				contentParams.addRule(RelativeLayout.LEFT_OF, R.id.ivAvatar);
				contentParams.setMargins(0, 0, (int) act.getResources()
						.getDimension(R.dimen.margin_padding_xtiny), 0);
				holder.llMessage.setGravity(Gravity.RIGHT);
				holder.llMessage.setLayoutParams(contentParams);

				holder.tvMessage
						.setBackgroundResource(R.drawable.bg_chat_right2);

			} else {
				holder.llMessageContainer.setGravity(Gravity.LEFT);

				RelativeLayout.LayoutParams avatarParams = new RelativeLayout.LayoutParams(
						(int) act.getResources().getDimension(
								R.dimen.button_size_lnormal), (int) act
								.getResources().getDimension(
										R.dimen.button_size_lnormal));
				avatarParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				holder.ivAvatar.setLayoutParams(avatarParams);

				RelativeLayout.LayoutParams contentParams = new RelativeLayout.LayoutParams(
						RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT);
				contentParams.addRule(RelativeLayout.RIGHT_OF, R.id.ivAvatar);
				contentParams.setMargins(
						(int) act.getResources().getDimension(
								R.dimen.margin_padding_xtiny), 0, 0, 0);
				holder.llMessage.setGravity(Gravity.LEFT);
				holder.llMessage.setLayoutParams(contentParams);

				holder.tvMessage.setBackgroundResource(R.drawable.bg_chat_left);
			}

			// listAq.id(holder.ivAvatar).image(item.getUserImage(), true, true,
			// 0, R.drawable.image_avatar_default);
			imageLoader.displayImage(item.getUserImage(), holder.ivAvatar,
					displayOptions);

			holder.ivAvatar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!item.getUserId().equals(GlobalValue.myAccount.getId())) {
						listener.onClickUserImage(position);
					}

				}
			});

		}
		return v;
	}

	private class ViewHolder {
		private ImageView ivAvatar;
		private TextView tvMessage, tvDate;
		private LinearLayout llMessageContainer, llMessage;

	}

	public interface IMessageDetail {
		void onClickUserImage(int position);
	}

}
