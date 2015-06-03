package com.pt.treasuretrash.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.object.Category;
import com.pt.treasuretrash.object.CategoryGroup;
import com.pt.treasuretrash.widget.AnimatedExpandableListView;
import com.pt.treasuretrash.widget.AnimatedExpandableListView.AnimatedExpandableListAdapter;

public class ExpandableCategoriesAdapter extends AnimatedExpandableListAdapter {
	private LayoutInflater inflater;
	private Context context;

	private List<CategoryGroup> arrCategoryGroups;
	private IExpandableListView listener;

	public ExpandableCategoriesAdapter(Context context,
			IExpandableListView listener) {
		inflater = LayoutInflater.from(context);
		this.context = context;
		this.listener = listener;
	}

	public void setData(List<CategoryGroup> items) {
		this.arrCategoryGroups = items;
	}

	@Override
	public Category getChild(int groupPosition, int childPosition) {
		return arrCategoryGroups.get(groupPosition).getArrCategories()
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
		final CategoryGroup group = getGroup(groupPosition);

		// if (convertView == null) {
		holder = new GroupHolder();
		convertView = inflater.inflate(R.layout.row_category_group, parent,
				false);
		holder.tvName = (TextView) convertView
				.findViewById(R.id.tvCategoryName);
		holder.ivExpandCollapse = (ImageView) convertView
				.findViewById(R.id.ivExpandCollapse);
		holder.btnToggle = (ToggleButton) convertView
				.findViewById(R.id.btnToggle);
		holder.llExpandCollapse = (LinearLayout) convertView
				.findViewById(R.id.llExpandCollapse);
		holder.llContainer = (LinearLayout) convertView
				.findViewById(R.id.llContainer);
		holder.rlContainer = (RelativeLayout) convertView
				.findViewById(R.id.rlContainer);

		// convertView.setTag(holder);
		// }

		// else {
		// holder = (GroupHolder) convertView.getTag();
		// }

		holder.tvName.setText(group.getGrandParent().getName());
		if (group.isActive()) {
			holder.btnToggle.setChecked(true);
		} else {
			holder.btnToggle.setChecked(false);
		}

		if (group.getStatus() == CategoryGroup.STATUS_COLLAPSE) {
			holder.ivExpandCollapse
					.setImageResource(R.drawable.icon_category_expand);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.setMargins(
					0,
					(int) context.getResources().getDimension(
							R.dimen.margin_padding_small),
					0,
					(int) context.getResources().getDimension(
							R.dimen.margin_padding_small));
			holder.rlContainer.setLayoutParams(params);

		} else {
			holder.ivExpandCollapse
					.setImageResource(R.drawable.icon_category_collapse);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.setMargins(
					0,
					(int) context.getResources().getDimension(
							R.dimen.margin_padding_small), 0, 2);
			holder.rlContainer.setLayoutParams(params);
		}

		if (group.isActive()) {
			holder.tvName.setTextColor(context.getResources().getColor(
					R.color.sign_up_teal));
			holder.ivExpandCollapse.setVisibility(View.VISIBLE);
			holder.llContainer.setBackgroundResource(R.drawable.bg_click_white);
		} else {
			holder.tvName.setTextColor(context.getResources().getColor(
					R.color.left_menu_horizontal_divider_not_transparent));
			holder.ivExpandCollapse.setVisibility(View.INVISIBLE);
			holder.llContainer
					.setBackgroundResource(R.drawable.bg_click_transparent);
		}

		if (group.getState() == CategoryGroup.STATE_EDIT) {
			holder.btnToggle.setVisibility(View.VISIBLE);
			holder.ivExpandCollapse.setVisibility(View.GONE);
		} else {
			holder.btnToggle.setVisibility(View.GONE);

		}

		holder.llExpandCollapse.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				listener.onExpandCollapseClick(groupPosition);

			}
		});

		holder.btnToggle
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						if (isChecked) {
							group.setActive(true);
							holder.tvName.setTextColor(context.getResources()
									.getColor(R.color.sign_up_teal));
							group.getGrandParent().setHidden(false);
							holder.llContainer
									.setBackgroundResource(R.drawable.bg_click_white);
							
							
						} else {
							group.setActive(false);
							holder.tvName
									.setTextColor(context
											.getResources()
											.getColor(
													R.color.left_menu_horizontal_divider_not_transparent));
							group.getGrandParent().setHidden(true);
							
						}
					}
				});
		
		

		return convertView;
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ChildHolder holder;
		CategoryGroup group = getGroup(groupPosition);
		Category category = getChild(groupPosition, childPosition);
		if (convertView == null) {
			holder = new ChildHolder();
			convertView = inflater.inflate(R.layout.row_category_child, parent,
					false);

			holder.tvName = (TextView) convertView
					.findViewById(R.id.tvCategoryName);
			holder.llContainer = (LinearLayout) convertView
					.findViewById(R.id.llContainer);
			convertView.setTag(holder);
		} else {
			holder = (ChildHolder) convertView.getTag();
		}

		if (childPosition == arrCategoryGroups.get(groupPosition)
				.getArrCategories().size() - 1) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.setMargins(0, 2, 0, (int) context.getResources()
					.getDimension(R.dimen.margin_padding_xsmall));
			holder.llContainer.setLayoutParams(params);
		}

		else if (childPosition == 0) {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.setMargins(0, 2, 0, 0);
			holder.llContainer.setLayoutParams(params);
		} else {
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			params.setMargins(0, 2, 0, 0);
			holder.llContainer.setLayoutParams(params);
		}

		holder.tvName.setText(category.getName());

		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);

		if (category.getParentCategoryId().equals(
				group.getGrandParent().getId())) {
			params.setMargins(
					(int) context.getResources().getDimension(
							R.dimen.margin_padding_small), 0, 0, 0);
			holder.tvName.setLayoutParams(params);
		} else {
			params.setMargins(
					(int) context.getResources().getDimension(
							R.dimen.margin_padding_large), 0, 0, 0);
			holder.tvName.setLayoutParams(params);
		}

		return convertView;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return arrCategoryGroups.get(groupPosition).getArrCategories().size();
	}

	@Override
	public CategoryGroup getGroup(int groupPosition) {
		return arrCategoryGroups.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return arrCategoryGroups.size();
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
		TextView tvName;
		LinearLayout llContainer;

	}

	class GroupHolder {
		TextView tvName;
		ImageView ivExpandCollapse;
		ToggleButton btnToggle;
		LinearLayout llExpandCollapse, llContainer;
		RelativeLayout rlContainer;
	}

	public interface IExpandableListView {
		void onExpandCollapseClick(int groupPosition);
	}

}
