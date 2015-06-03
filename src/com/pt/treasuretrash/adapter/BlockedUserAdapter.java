package com.pt.treasuretrash.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.androidquery.AQuery;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.HomeActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.databasehelper.TreasureTrashDbHelper;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.widget.HelveticaLightTextView;

public class BlockedUserAdapter extends ArrayAdapter<Account> {
	private ArrayList<Account> arrUsers;
	private LayoutInflater inflater;
	private AQuery listAq;
	private Activity act;
	private IOnBlockUnblock listener;

	public BlockedUserAdapter(TreasureTrashBaseActivity a, int resource,
			ArrayList<Account> objects, IOnBlockUnblock listener) {
		super(a, resource, objects);
		// TODO Auto-generated constructor stub
		this.act = a;
		this.arrUsers = objects;
		inflater = (LayoutInflater) a
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.listener = listener;
		listAq = new AQuery(a);

	}

	@Override
	public View getView(int index, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final int position = index;
		View v = convertView;
		final ViewHolder holder;
		if (v == null) {
			holder = new ViewHolder();
			v = inflater.inflate(R.layout.row_block_user, null);
			holder.ivAvatar = (ImageView) v.findViewById(R.id.ivAvatar);
			holder.tvUserName = (HelveticaLightTextView) v
					.findViewById(R.id.tvUserName);
			holder.rltBlockedRow = (RelativeLayout) v
					.findViewById(R.id.rlt_blocked_row);

			// Unblock
			holder.rltBlockedRow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					unBlockUser(position);
				}
			});

			v.setTag(holder);
		} else {
			holder = (ViewHolder) v.getTag();
		}
		Account user = arrUsers.get(position);
		if (user != null) {

			final AQuery aq = listAq.recycle(v);

			aq.id(holder.ivAvatar).image(user.getImageUrl(), false, true, 0,
					R.drawable.image_avatar_default_no_border);
			aq.id(holder.tvUserName).text(user.getUsername());

		}
		return v;
	}

	private class ViewHolder {
		private ImageView ivAvatar;
		private HelveticaLightTextView tvUserName;
		private RelativeLayout rltBlockedRow;

	}

	private void unBlockUser(final int pos) {
		final Account userblock = arrUsers.get(pos);
		AccountModelManager.unBlockUser(act, userblock.getId(), true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						new TreasureTrashDbHelper(act).deleteBlockedUser(
								userblock.getId(),
								GlobalValue.myAccount.getId(), true);
						listener.onUnblock(pos);
//						((HomeActivity) act).getBlockedUserList(true);
						((HomeActivity) act).mPubnubService
								.sendBlockUnblockMessage(
										GlobalValue.myAccount.getId(),
										GlobalValue.myAccount.getUsername(),
										userblock.getId(), false);
					}

					@Override
					public void onError(int statusCode, String json) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				});
	}
	
	public interface IOnBlockUnblock{
		void onUnblock(int position);
	}
}
