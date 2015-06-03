package com.pt.treasuretrash.fragment;

import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.android.gms.internal.db;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.HomeActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseFragment;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.modelmanager.ProductModelManager;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.slidingmenu.SlidingMenu;
import com.pt.treasuretrash.utility.DialogUtility;

public class MessageFragment extends TreasureTrashBaseFragment implements
		OnClickListener {

	private TextView tvYourListings, tvListingsCount, tvEnquiries,
			tvEnquiriesCount, tvExpired, tvExpiredCount;
	// public static List<MessageGroup> arrYourPostingGroup;
	// public static List<MessageGroup> arrYourEnquiryGroup;
	// public static List<MessageGroup> arrExpirtedGroup;
	public int yourPostingNewMsg = 0;
	public int yourEnquiryNewMsg = 0;
	public int expiredNewMsg = 0;
	public List<Item> arrCheckExpiredItems;
	private LocalBroadcastManager localBroadcastManager = null;

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {

			if (intent.getAction().equalsIgnoreCase(
					GlobalValue.INTENT_ACTION_MESSAGE_RECEIVED)) {
				String message = intent.getExtras().getString(
						GlobalValue.KEY_MESSAGE_RECEIVED);
				onReceiveMessage(message);
			}

		}
	};

	@Override
	public void onCreate(android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intent = new IntentFilter();
		intent.addAction(GlobalValue.INTENT_ACTION_MESSAGE_RECEIVED);
		
		localBroadcastManager = LocalBroadcastManager.getInstance(self);
		localBroadcastManager.registerReceiver(
				receiver, intent);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		localBroadcastManager.unregisterReceiver(receiver);
		localBroadcastManager = null;
	}

	@Override
	public int getFragmentResource() {
		// TODO Auto-generated method stub
		return R.layout.fragment_message;
	}

	@Override
	public void initView(View view) {
		tvYourListings = (TextView) view.findViewById(R.id.tvYourListings);
		tvListingsCount = (TextView) view
				.findViewById(R.id.tvYourListingsCount);
		tvEnquiries = (TextView) view.findViewById(R.id.tvYourEnquiries);
		tvEnquiriesCount = (TextView) view.findViewById(R.id.tvEnquiriesCount);
		tvExpired = (TextView) view.findViewById(R.id.tvExpiredItem);
		tvExpiredCount = (TextView) view.findViewById(R.id.tvExpiredCount);
	}

	@Override
	public void initData(View view) {
		// arrYourPostingGroup = new ArrayList<MessageGroup>();
		// arrYourEnquiryGroup = new ArrayList<MessageGroup>();
		// arrExpirtedGroup = new ArrayList<MessageGroup>();
	}

	@Override
	public void initControl(View view) {
		getParentView(tvYourListings).setOnClickListener(this);
		getParentView(tvEnquiries).setOnClickListener(this);
		getParentView(tvExpired).setOnClickListener(this);
	}

	@Override
	public void onShow() {
		getHomeActivity().rightMenu
				.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		getHomeActivity().setBottomMenu(HomeActivity.FRAGMENT_MESSAGE);
		// generateMessages();
		List<String> chattedItemList = getHomeActivity().dbHelper
				.getAllChattedItems(GlobalValue.myAccount.getId());

		if (chattedItemList.size() > 0) {
			ProductModelManager.checkListItemExpired(self, chattedItemList,
					true, new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							arrCheckExpiredItems = ParserUtility
									.parseListCheckExpiredItems(json);
							new UpdateCheckItemsExpired().execute();

						}

						@Override
						public void onError(int statusCode, String json) {
							new GenerateMessageTask().execute();

						}

						@Override
						public void onError() {
							new GenerateMessageTask().execute();

						}
					});
		} else {
			new GenerateMessageTask().execute();
		}

	}
	
	@Override
	public void onHide() {
		// TODO Auto-generated method stub
		getHomeActivity().rightMenu
		.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		
	}

	public void generateMessages() {
		if (GlobalValue.myAccount != null) {
			yourPostingNewMsg = 0;
			yourEnquiryNewMsg = 0;
			expiredNewMsg = 0;

			yourPostingNewMsg = getHomeActivity().dbHelper
					.getMyPostingNewMessages(GlobalValue.myAccount.getId());
			yourEnquiryNewMsg = getHomeActivity().dbHelper
					.getMyEnquiriesNewMessages(GlobalValue.myAccount.getId());
			expiredNewMsg = getHomeActivity().dbHelper
					.getExpiredNewMessages(GlobalValue.myAccount.getId());

			tvListingsCount.setText(yourPostingNewMsg > 0 ? "("
					+ yourPostingNewMsg + " new)" : "");
			tvEnquiriesCount.setText(yourEnquiryNewMsg > 0 ? "("
					+ yourEnquiryNewMsg + " new)" : "");
			tvExpiredCount.setText(expiredNewMsg > 0 ? "(" + expiredNewMsg
					+ " new)" : "");

			if (yourPostingNewMsg > 0) {
				tvYourListings.setTextColor(self.getResources().getColor(
						R.color.red));
				tvListingsCount.setTextColor(self.getResources().getColor(
						R.color.red));
			} else {
				tvYourListings.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
				tvListingsCount.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
			}

			if (yourEnquiryNewMsg > 0) {
				tvEnquiries.setTextColor(self.getResources().getColor(
						R.color.red));
				tvEnquiriesCount.setTextColor(self.getResources().getColor(
						R.color.red));
			} else {
				tvEnquiries.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
				tvEnquiriesCount.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
			}

			if (expiredNewMsg > 0) {
				tvExpired.setTextColor(self.getResources()
						.getColor(R.color.red));
				tvExpiredCount.setTextColor(self.getResources().getColor(
						R.color.red));
			} else {
				tvExpired.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
				tvExpiredCount.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
			}

		}
	}

	@Override
	public void onClick(View v) {
		if (v == getParentView(tvYourListings)) {
			MessageListFragment fragment = (MessageListFragment) getHomeActivity().arrFragments
					.get(HomeActivity.FRAGMENT_MESSAGE_LIST);
			fragment.type = MessageListFragment.TYPE_YOUR_LISTING;
			getHomeActivity().showFragment(HomeActivity.FRAGMENT_MESSAGE_LIST);

		}

		if (v == getParentView(tvEnquiries)) {
			MessageListFragment fragment = (MessageListFragment) getHomeActivity().arrFragments
					.get(HomeActivity.FRAGMENT_MESSAGE_LIST);
			fragment.type = MessageListFragment.TYPE_YOUR_ENQUIRY;
			getHomeActivity().showFragment(HomeActivity.FRAGMENT_MESSAGE_LIST);
		}

		if (v == getParentView(tvExpired)) {
			MessageListFragment fragment = (MessageListFragment) getHomeActivity().arrFragments
					.get(HomeActivity.FRAGMENT_MESSAGE_LIST);
			fragment.type = MessageListFragment.TYPE_EXPIRED;
			getHomeActivity().showFragment(HomeActivity.FRAGMENT_MESSAGE_LIST);
		}

	}

	private void onReceiveMessage(String message) {
		generateMessages();
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LocalBroadcastManager.getInstance(self).unregisterReceiver(receiver);
	}

	private class GenerateMessageTask extends AsyncTask<Void, Void, Void> {

		protected void onPreExecute() {
			DialogUtility.showProgressDialog(self);
		}

		@Override
		protected Void doInBackground(Void... params) {
			yourPostingNewMsg = 0;
			yourEnquiryNewMsg = 0;
			expiredNewMsg = 0;

			yourPostingNewMsg = getHomeActivity().dbHelper
					.getMyPostingNewMessages(GlobalValue.myAccount.getId());
			yourEnquiryNewMsg = getHomeActivity().dbHelper
					.getMyEnquiriesNewMessages(GlobalValue.myAccount.getId());
			expiredNewMsg = getHomeActivity().dbHelper
					.getExpiredNewMessages(GlobalValue.myAccount.getId());

			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			DialogUtility.closeProgressDialog();

			tvListingsCount.setText(yourPostingNewMsg > 0 ? "("
					+ yourPostingNewMsg + " new)" : "");
			tvEnquiriesCount.setText(yourEnquiryNewMsg > 0 ? "("
					+ yourEnquiryNewMsg + " new)" : "");
			tvExpiredCount.setText(expiredNewMsg > 0 ? "(" + expiredNewMsg
					+ " new)" : "");

			if (yourPostingNewMsg > 0) {
				tvYourListings.setTextColor(self.getResources().getColor(
						R.color.red));
				tvListingsCount.setTextColor(self.getResources().getColor(
						R.color.red));
			} else {
				tvYourListings.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
				tvListingsCount.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
			}

			if (yourEnquiryNewMsg > 0) {
				tvEnquiries.setTextColor(self.getResources().getColor(
						R.color.red));
				tvEnquiriesCount.setTextColor(self.getResources().getColor(
						R.color.red));
			} else {
				tvEnquiries.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
				tvEnquiriesCount.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
			}

			if (expiredNewMsg > 0) {
				tvExpired.setTextColor(self.getResources()
						.getColor(R.color.red));
				tvExpiredCount.setTextColor(self.getResources().getColor(
						R.color.red));
			} else {
				tvExpired.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
				tvExpiredCount.setTextColor(self.getResources().getColor(
						R.color.sign_up_teal));
			}

		}
	}

	private class UpdateCheckItemsExpired extends AsyncTask<Void, Void, Void> {

		@Override
		protected void onPreExecute() {
			DialogUtility.showProgressDialog(self);
		}

		@Override
		protected Void doInBackground(Void... params) {
			getHomeActivity().dbHelper.updateExpiredStatusForItems(
					arrCheckExpiredItems, GlobalValue.myAccount.getId());
			return null;

		}

		@Override
		protected void onPostExecute(Void result) {
			new GenerateMessageTask().execute();
		}

	}

}
