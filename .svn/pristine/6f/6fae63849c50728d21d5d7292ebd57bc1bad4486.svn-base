package com.pt.treasuretrash.adapter;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout.LayoutParams;

import com.androidquery.AQuery;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.adapter.ExpandableCategoriesAdapter.ChildHolder;
import com.pt.treasuretrash.adapter.ExpandableCategoriesAdapter.GroupHolder;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.object.Category;
import com.pt.treasuretrash.object.CategoryGroup;
import com.pt.treasuretrash.object.MessageGroup;
import com.pt.treasuretrash.object.MessageThread;
import com.pt.treasuretrash.utility.ImageUtil;
import com.pt.treasuretrash.widget.AnimatedExpandableListView;
import com.pt.treasuretrash.widget.AnimatedExpandableListView.AnimatedExpandableListAdapter;

public class MessageGroupAdapter extends AnimatedExpandableListAdapter {

	private TreasureTrashBaseActivity act;
	private ArrayList<MessageGroup> arrMessageGroup;
	private LayoutInflater inflater;
	private AQuery listAq;
	private IOnMessageItemClick listener;

	public MessageGroupAdapter(TreasureTrashBaseActivity a,
			IOnMessageItemClick listener) {
		// TODO Auto-generated constructor stub
		this.act = a;
		this.listener = listener;
		inflater = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		listAq = new AQuery(a);
	}

	public void setData(List<MessageGroup> objects) {
		this.arrMessageGroup = (ArrayList<MessageGroup>) objects;
	}

	@Override
	public MessageThread getChild(int groupPosition, int childPosition) {
		return arrMessageGroup.get(groupPosition).getArrThread()
				.get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		final GroupHolder holder;
		final MessageGroup group = getGroup(groupPosition);
		
		AnimatedExpandableListView mExpandableListView = (AnimatedExpandableListView) parent;
	    mExpandableListView.expandGroup(groupPosition);

		if (convertView == null) {
			holder = new GroupHolder();
			convertView = inflater.inflate(R.layout.row_message_group_list,
					parent, false);
			holder.ivItem = (ImageView) convertView.findViewById(R.id.ivItem);
			holder.tvItemName = (TextView) convertView
					.findViewById(R.id.tvItemName);
			holder.tvConfirmDelete = (TextView) convertView
					.findViewById(R.id.tvConfirmDelete);
			holder.ivDelete = (ImageView) convertView
					.findViewById(R.id.ivDelete);

			convertView.setTag(holder);
		}

		else {
			holder = (GroupHolder) convertView.getTag();
		}

		if (group != null) {

			// AQuery aq = listAq.recycle(v);
			if (group.getNewMessage() > 0) {
				holder.ivItem.setBackgroundResource(R.drawable.bg_border_red);
			} else {
				holder.ivItem.setBackgroundResource(R.drawable.bg_border_teal);
			}

			// imageLoader.displayImage(group.getItem().getImage(),
			// holder.ivItem, options);
			listAq.id(holder.ivItem).image(
					ImageUtil.convertGalleryImageSize(group.getItem()
							.getImage(), (int) act.IMAGE_SIZE / 9), false,
					true, 0, R.drawable.image_avatar_default);
			// Log.d("MESSAGE_ITEM_IMAGE",
			// ImageUtil.convertGalleryImageSize(group.getItem().getImage(),
			// (int) act.IMAGE_SIZE / 9));

			listAq.id(holder.tvItemName).text(group.getItem().getTitle());

			if (group.getStatus() == MessageGroup.STATUS_NORMAL) {
				holder.ivDelete
						.setImageResource(R.drawable.icon_delete_horizontal);
				holder.ivDelete.setVisibility(View.GONE);
				holder.tvConfirmDelete.setVisibility(View.GONE);
			}
			if (group.getStatus() == MessageGroup.STATUS_DELETE_PENDING) {
				holder.ivDelete
						.setImageResource(R.drawable.icon_delete_horizontal);
				holder.ivDelete.setVisibility(View.VISIBLE);
				holder.tvConfirmDelete.setVisibility(View.GONE);
			}

			holder.ivItem.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onClickItemImage(groupPosition);

				}
			});

			holder.ivDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (group.getStatus() == MessageGroup.STATUS_DELETE_PENDING) {
						holder.ivDelete
								.setImageResource(R.drawable.icon_delete_vertical);
						holder.tvConfirmDelete.setVisibility(View.VISIBLE);
						group.setStatus(MessageGroup.STATUS_DELETE_CONFIRM);
					} else {
						holder.ivDelete
								.setImageResource(R.drawable.icon_delete_horizontal);
						holder.tvConfirmDelete.setVisibility(View.GONE);
						group.setStatus(MessageGroup.STATUS_DELETE_PENDING);
					}
				}
			});

			holder.tvConfirmDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					listener.onClickDeleteGroup(groupPosition);

				}
			});

		}

		return convertView;
	}

	@Override
	public View getRealChildView(final int groupPosition,
			final int childPosition, boolean isLastChild, View convertView,
			ViewGroup parent) {
		final ChildHolder holder;
		final MessageGroup group = getGroup(groupPosition);
		final MessageThread thread = getChild(groupPosition, childPosition);

		if (convertView == null) {
			holder = new ChildHolder();
			convertView = inflater.inflate(R.layout.row_message_group_detail,
					parent, false);

			holder.tvUserName = (TextView) convertView
					.findViewById(R.id.tvUserName);
			holder.ivDelete = (ImageView) convertView
					.findViewById(R.id.ivDelete);
			holder.tvConfirmDelete = (TextView) convertView
					.findViewById(R.id.tvConfirmDelete);
			holder.tvGap = (TextView) convertView.findViewById(R.id.tvGap);
			holder.ivUser = (ImageView) convertView.findViewById(R.id.ivUser);
			holder.viewSeparator = (View)convertView.findViewById(R.id.viewSeparator);
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}
		
		if(childPosition == getChildrenCount(groupPosition)-1){
			holder.viewSeparator.setVisibility(View.VISIBLE);
		}
		else{
			holder.viewSeparator.setVisibility(View.GONE);
		}

		if (thread.getNewMessage() > 0) {
			holder.tvUserName.setTextColor(act.getResources().getColor(
					R.color.red));
		} else {
			holder.tvUserName.setTextColor(act.getResources().getColor(
					R.color.sign_up_teal));
		}

		holder.tvUserName.setText(thread.getAwayName()
				+ (thread.getNewMessage() > 0 ? " (" + thread.getNewMessage()
						+ " new)" : ""));

		listAq.id(holder.ivUser).image(
				ImageUtil.addImageSize(thread.getAwayImage(),
						(int) act.IMAGE_SIZE / 9), false, true, 0,
				R.drawable.image_avatar_default);
		// Log.d("MESSAGE_USER_IMAGE", ImageUtil
		// .addImageSize(thread.getAwayImage(),
		// (int) act.IMAGE_SIZE / 9));

		if (thread.getStatus() == MessageThread.STATUS_NORMAL) {
			holder.ivDelete.setImageResource(R.drawable.icon_delete_horizontal);
			holder.ivDelete.setVisibility(View.INVISIBLE);
			holder.tvConfirmDelete.setVisibility(View.GONE);
			holder.tvGap.setVisibility(View.GONE);
		}
		if (thread.getStatus() == MessageThread.STATUS_DELETE_PENDING) {
			holder.ivDelete.setImageResource(R.drawable.icon_delete_horizontal);
			holder.ivDelete.setVisibility(View.VISIBLE);
			holder.tvConfirmDelete.setVisibility(View.GONE);
			holder.tvGap.setVisibility(View.VISIBLE);
		}

		holder.ivDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (thread.getStatus() == MessageThread.STATUS_DELETE_PENDING) {
					holder.ivDelete
							.setImageResource(R.drawable.icon_delete_vertical);
					holder.tvConfirmDelete.setVisibility(View.VISIBLE);
					thread.setStatus(MessageThread.STATUS_DELETE_CONFIRM);
				} else {
					holder.ivDelete
							.setImageResource(R.drawable.icon_delete_horizontal);
					holder.tvConfirmDelete.setVisibility(View.GONE);
					thread.setStatus(MessageThread.STATUS_DELETE_PENDING);
				}
			}
		});

		holder.tvConfirmDelete.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClickDeleteThread(groupPosition, childPosition);

			}
		});

		convertView.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onClickMessageThread(group, thread,
						thread.getNewMessage());
			}
		});
		return convertView;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return arrMessageGroup.get(groupPosition).getArrThread().size();
	}

	@Override
	public MessageGroup getGroup(int groupPosition) {
		return arrMessageGroup.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return arrMessageGroup.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public boolean hasStableIds() {
		return true;
	}

	@Override
	public boolean isChildSelectable(int arg0, int arg1) {
		return true;
	}

	class ChildHolder {
		private ImageView ivUser, ivDelete;
		private TextView tvUserName, tvConfirmDelete, tvGap;
		private View viewSeparator;

	}

	class GroupHolder {
		private ImageView ivItem, ivDelete;
		private TextView tvItemName, tvConfirmDelete;
	}

	public interface IOnMessageItemClick {
		void onClickMessageThread(MessageGroup messageGroup,
				MessageThread messageThread, int newMessage);

		void onClickDeleteGroup(int groupPosition);

		void onClickDeleteThread(int groupPosition, int threadPosition);

		void onClickItemImage(int groupPosition);
	}
}
