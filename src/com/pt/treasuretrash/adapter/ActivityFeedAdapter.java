package com.pt.treasuretrash.adapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.Feed;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.utility.CustomClickableSpan;

public class ActivityFeedAdapter extends ArrayAdapter<Feed> {

	private TreasureTrashBaseActivity act;
	private ArrayList<Feed> arrFeed;
	private LayoutInflater inflater;
	private AQuery listAq;
	private AQuery aq;
	private ImageLoader imageLoader;
	private DisplayImageOptions options;
	private SimpleDateFormat sdfWeekday = new SimpleDateFormat("EEEE");
	private IOnFeedClick listener;
	private final int MAX_LINES = 2;
	private String content = "";
	// private SpannableString spanContent;
	private SpannableStringBuilder spanBuilder;
	private CustomClickableSpan clickable;
	private String time = "";

	public ActivityFeedAdapter(TreasureTrashBaseActivity a, int resource,
			List<Feed> objects, IOnFeedClick listener) {
		super(a, resource, objects);
		// TODO Auto-generated constructor stub
		this.act = a;
		this.arrFeed = (ArrayList<Feed>) objects;
		inflater = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listAq = new AQuery(a);
		aq = new AQuery(a);
		this.listener = listener;

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
			v = inflater.inflate(R.layout.row_activity_feed, null);
			holder.ivAvatar = (ImageView) v.findViewById(R.id.ivAvatar);
			holder.ivAction = (ImageView) v.findViewById(R.id.ivAction);
			holder.tvContent = (TextView) v.findViewById(R.id.tvContent);
			holder.ivItem = (ImageView) v.findViewById(R.id.ivItem);
			holder.llGone = (LinearLayout) v.findViewById(R.id.llGone);
			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		final Feed feed = arrFeed.get(position);
		if (feed != null) {

			final AQuery aq = listAq.recycle(v);

			// aq.id(holder.ivAvatar).image(feed.getUserImg(), false, true, 0,
			// R.drawable.image_avatar_default_no_border);
			aq.id(holder.ivAvatar).image(feed.getUserImg(), false, true, 0,
					R.drawable.image_avatar_default_no_border, null,
					AQuery.FADE_IN);

			// Log.d("FEED_TIME", feed.getLoadTime() + "--" +
			// feed.getCreatedDate());
			int second = (int) (feed.getLoadTime() - feed.getCreatedDate());

			time = second
					+ (second > 1 ? act.getString(R.string._seconds) : act
							.getString(R.string._second))
					+ act.getString(R.string._ago);
			if (second > 59) {
				int minute = (int) second / 60;
				time = minute
						+ (minute > 1 ? act.getString(R.string._minutes) : act
								.getString(R.string._minute))
						+ act.getString(R.string._ago);
				if (minute > 59) {
					int hour = (int) minute / 60;
					time = hour
							+ (hour > 1 ? act.getString(R.string._hours) : act
									.getString(R.string._hour))
							+ act.getString(R.string._ago);
					if (hour > 23) {
						int day = (int) hour / 24;
						time = day
								+ (day > 1 ? act.getString(R.string._days)
										: act.getString(R.string._day))
								+ act.getString(R.string._ago);
					}
				}
			}

			if (feed.getType().equals(Feed.TYPE_LISTING)) {
				aq.id(holder.ivAction).visibility(View.GONE);
				aq.id(holder.ivItem).visibility(View.VISIBLE);
				aq.id(holder.llGone).visibility(View.GONE);
				aq.id(holder.ivAction).visibility(View.GONE);

				// aq.id(holder.ivItem).image(feed.getItemImg(), false, true, 0,
				// R.drawable.image_not_available);
				aq.id(holder.ivItem).image(feed.getItemImg(), false, true, 0,
						R.drawable.image_not_available, null, AQuery.FADE_IN);

				content = createHtmlContentText(feed, time);

				// aq.id(holder.tvContent).text(Html.fromHtml(content));
			} else if (feed.getType().equals(Feed.TYPE_GONE)) {

				aq.id(holder.ivAction).visibility(View.GONE);
				aq.id(holder.ivItem).visibility(View.VISIBLE);
				aq.id(holder.llGone).visibility(View.VISIBLE);
				aq.id(holder.ivAction).visibility(View.GONE);
				// aq.id(holder.ivItem).image(feed.getItemImg(), false, true, 0,
				// R.drawable.image_not_available);

				aq.id(holder.ivItem).image(feed.getItemImg(), false, true, 0,
						R.drawable.image_not_available, null, AQuery.FADE_IN);

				content = createHtmlContentText(feed, time);

			} else if (feed.getType().equals(Feed.TYPE_FOLLOW)) {
				aq.id(holder.ivAction).visibility(View.VISIBLE);
				aq.id(holder.ivItem).visibility(View.GONE);
				aq.id(holder.llGone).visibility(View.GONE);
				aq.id(holder.ivAction).visibility(View.VISIBLE);

				content = createHtmlContentText(feed, time);

				if (feed.isFollowed()) {
					aq.id(holder.ivAction).image(R.drawable.btn_followed);
				} else {
					aq.id(holder.ivAction).image(R.drawable.btn_add_follow);
				}
			} else if (feed.getType().equals(Feed.TYPE_FRIEND_JOIN)) {
				aq.id(holder.ivAction).visibility(View.VISIBLE);
				aq.id(holder.ivItem).visibility(View.GONE);
				aq.id(holder.llGone).visibility(View.GONE);
				aq.id(holder.ivAction).visibility(View.VISIBLE);

				content = createHtmlContentText(feed, time);

				if (feed.isFollowed()) {
					aq.id(holder.ivAction).image(R.drawable.btn_followed);
				} else {
					aq.id(holder.ivAction).image(R.drawable.btn_add_follow);
				}
			}

			spanBuilder = new SpannableStringBuilder(content);

			ForegroundColorSpan fcsTeal = new ForegroundColorSpan(act
					.getResources().getColor(R.color.sign_up_teal));

			ForegroundColorSpan fcsGray = new ForegroundColorSpan(act
					.getResources().getColor(R.color.feed_time_color));

			ForegroundColorSpan fcsRed = new ForegroundColorSpan(act
					.getResources().getColor(R.color.red));

			clickable = new CustomClickableSpan() {

				@Override
				public void onClick(View widget) {
					listener.onClickUserAvatar(position);
				}
			};
			if (feed.getType().equalsIgnoreCase(Feed.TYPE_FRIEND_JOIN)) {
				aq.id(holder.tvContent).text(spanBuilder);
				holder.tvContent.post(new Runnable() {

					@Override
					public void run() {
						if (holder.tvContent.getLineCount() > MAX_LINES
								&& !feed.isConfigedRow()) {
							int charChop = 4;
							do {
								String userName = feed.getUserName();
								userName = userName.substring(0,
										userName.length() - charChop);
								userName += "...";
								feed.setUserName(userName);
								content = createHtmlContentText(feed, time);
								spanBuilder = new SpannableStringBuilder(
										content);
								aq.id(holder.tvContent).text(spanBuilder);
								charChop++;

							} while (holder.tvContent.getLineCount() > MAX_LINES);
							feed.setConfigedRow(true);
							notifyDataSetChanged();
						}

					}
				});

				
				spanBuilder.setSpan(fcsTeal, 54, 54 + feed.getUserName()
						.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spanBuilder.setSpan(fcsGray,
						spanBuilder.length() - time.length(),
						spanBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spanBuilder.setSpan(clickable, 54, 54 + feed.getUserName()
						.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// notifyDataSetChanged();

			} else {
				if (feed.getType().equalsIgnoreCase(Feed.TYPE_GONE)) {
					spanBuilder.setSpan(fcsRed,
							spanBuilder.length() - time.length() - 5,
							spanBuilder.length() - time.length(),
							Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
				spanBuilder.setSpan(fcsTeal, 0, feed.getUserName().length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spanBuilder.setSpan(fcsGray,
						spanBuilder.length() - time.length(),
						spanBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				spanBuilder.setSpan(clickable, 0, feed.getUserName().length(),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
				// notifyDataSetChanged();
			}

			aq.id(holder.tvContent).text(spanBuilder);
			holder.tvContent
					.setMovementMethod(LinkMovementMethod.getInstance());

			holder.ivAction.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (!feed.isFollowed()) {
						listener.onClickFollow(position);
					} else {
						listener.onClickUnfollow(position);
					}

				}
			});

			holder.ivAvatar.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onClickUserAvatar(position);

				}
			});

			holder.ivItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onClickItemImage(position);

				}
			});

			holder.tvContent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// listener.onClickUserAvatar(position);

				}
			});

		}
		return v;
	}

	// private String createHtmlContentText(Feed feed, String time) {
	// String content = "";
	// if (feed.getType().equals(Feed.TYPE_LISTING)) {
	// content = "<font color='#06a8a8'>" + feed.getUserName() + "</font>"
	// + " <font color='#ffffff'>listed \"" + feed.getItemTitle()
	// + "\"</font> " + "<font color='#787878'>" + time
	// + "</font>";
	// } else if (feed.getType().equals(Feed.TYPE_FOLLOW)) {
	// content = "<font color='#06a8a8'>" + feed.getUserName() + "</font>"
	// + " <font color='#ffffff'>started following you</font> "
	// + "<font color='#787878'>" + time + "</font>";
	// }
	//
	// else if (feed.getType().equals(Feed.TYPE_GONE)) {
	// content = "<font color='#06a8a8'>" + feed.getUserName() + "</font>"
	// + "<font color='#ffffff'> marked \"" + feed.getItemTitle()
	// + "\" " + "as</font> " + "<font color='#ff0000'>" + "GONE"
	// + "</font> " + "<font color='#787878'>" + time + "</font>";
	// }
	//
	// else if (feed.getType().equals(Feed.TYPE_FRIEND_JOIN)) {
	// content =
	// " <font color='#ffffff'>Your Facebook friend has just joined TreasureTrash as </font> "
	// + "<font color='#06a8a8'>"
	// + feed.getUserName()
	// + "</font>"
	// + " <font color='#787878'>" + time + "</font>";
	// }
	//
	// return content;
	// }

	private String createHtmlContentText(Feed feed, String time) {
		String content = "";
		if (feed.getType().equals(Feed.TYPE_LISTING)) {
			content = feed.getUserName() + " listed \"" + feed.getItemTitle()
					+ "\" " + time;
		} else if (feed.getType().equals(Feed.TYPE_FOLLOW)) {
			content = feed.getUserName() + " started following you " + time;
		}

		else if (feed.getType().equals(Feed.TYPE_GONE)) {
			content = feed.getUserName() + " marked \"" + feed.getItemTitle()
					+ "\" " + "as " + "GONE " + time;
		}

		else if (feed.getType().equals(Feed.TYPE_FRIEND_JOIN)) {
			content = "Your Facebook friend has just joined TreasureTrash as "
					+ feed.getUserName() + " " + time;
		}

		return content;
	}

	private class ViewHolder {
		private ImageView ivAvatar, ivAction, ivItem;
		private TextView tvContent;
		private LinearLayout llGone;

	}

	public interface IOnFeedClick {
		void onClickFollow(int position);

		void onClickUnfollow(int position);

		void onClickUserAvatar(int position);

		void onClickItemImage(int position);
	}
}
