package com.pt.treasuretrash.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.androidquery.callback.BitmapAjaxCallback;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.HomeActivity;
import com.pt.treasuretrash.activity.ItemDetailsActivity;
import com.pt.treasuretrash.activity.SellerProfileActivity;
import com.pt.treasuretrash.adapter.ActivityFeedAdapter;
import com.pt.treasuretrash.adapter.ActivityFeedAdapter.IOnFeedClick;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity.DialogListener;
import com.pt.treasuretrash.base.TreasureTrashBaseFragment;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.modelmanager.ProductModelManager;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.Feed;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.widget.AutoBgButton;

public class ActivityFeedFragment extends TreasureTrashBaseFragment implements
		OnClickListener {

	private ListView lvFeed;
	private AutoBgButton btnRefresh;
	private List<Feed> arrFeed;
	private ActivityFeedAdapter adapterFeed;
	private LinearLayout llInviteFriends, llFeedList;
	private AutoBgButton btnInviteFb, btnInviteGoogle, btnInviteEmail,
			btnInviteMessage;

	@Override
	public int getFragmentResource() {
		// TODO Auto-generated method stub
		return R.layout.fragment_activity_feed;
	}

	@Override
	public void initView(View view) {
		lvFeed = (ListView) view.findViewById(R.id.lvFeed);

		btnRefresh = (AutoBgButton) view.findViewById(R.id.btnRefresh);
		btnInviteFb = (AutoBgButton) view.findViewById(R.id.btnInviteFb);
		btnInviteGoogle = (AutoBgButton) view
				.findViewById(R.id.btnInviteGoogle);
		btnInviteEmail = (AutoBgButton) view.findViewById(R.id.btnInviteEmail);
		btnInviteMessage = (AutoBgButton) view
				.findViewById(R.id.btnInviteMessage);

		llFeedList = (LinearLayout) view.findViewById(R.id.llFeedList);
		llInviteFriends = (LinearLayout) view.findViewById(R.id.llInviteFriend);

		llInviteFriends.setVisibility(View.GONE);
		llFeedList.setVisibility(View.GONE);
		btnRefresh.setVisibility(View.GONE);

	}

	@Override
	public void initData(View view) {
		arrFeed = new ArrayList<Feed>();
		adapterFeed = new ActivityFeedAdapter((TreasureTrashBaseActivity) self,
				R.id.lvFeed, arrFeed, listener);
		lvFeed.setAdapter(adapterFeed);

	}

	@Override
	public void initControl(View view) {
		btnRefresh.setOnClickListener(this);
		btnInviteFb.setOnClickListener(this);
		btnInviteGoogle.setOnClickListener(this);
		btnInviteEmail.setOnClickListener(this);
		btnInviteMessage.setOnClickListener(this);

	}

	@Override
	public void onShow() {
		getHomeActivity().setBottomMenu(HomeActivity.FRAGMENT_ACTIVITY_FEED);

		// if (arrFeed.size() == 0) {
		// loadActivityFeed();
		// }
		getFollowingUsers();
	}
	
	private void loadActivityFeed() {
		AccountModelManager.getActivityFeed(self, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						List<Feed> list = ParserUtility.parseActivityFeed(json,
								(int) getHomeActivity().IMAGE_SIZE / 9);
						arrFeed.clear();
						arrFeed.addAll(list);
						adapterFeed.notifyDataSetChanged();

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

	@Override
	public void onClick(View v) {
		if (v == btnRefresh) {
			loadActivityFeed();
		}

		if (v == btnInviteFb) {
			getHomeActivity().onInviteFacebook();
		}

		if (v == btnInviteGoogle) {
			getHomeActivity().onInviteGoogle();
		}

		if (v == btnInviteMessage) {
			getHomeActivity().onInviteMessage();
		}

		if (v == btnInviteEmail) {
			getHomeActivity().onInviteEmail();
		}

	}

	@Override
	public void onHide() {
		// TODO Auto-generated method stub
		super.onHide();
		BitmapAjaxCallback.clearCache();
		arrFeed.clear();
		adapterFeed.notifyDataSetChanged();
	}

	IOnFeedClick listener = new IOnFeedClick() {

		@Override
		public void onClickFollow(int position) {
			String userId = arrFeed.get(position).getUserId();
			followUser(userId, position);

		}

		@Override
		public void onClickUnfollow(int position) {
			String userId = arrFeed.get(position).getUserId();
			unfollowUser(userId, position);
		}

		@Override
		public void onClickUserAvatar(int position) {
			Bundle b = new Bundle();
			b.putString(GlobalValue.KEY_SELLER_ID, arrFeed.get(position)
					.getUserId());
			gotoActivity(self, SellerProfileActivity.class, b);

		}

		@Override
		public void onClickItemImage(int position) {
			Item item = new Item();
			item.setId(arrFeed.get(position).getItemId());
			item.setTitle(arrFeed.get(position).getItemTitle());
			item.setImage(arrFeed.get(position).getItemImg());

			checkItemExist(item);

		}
	};

	private void checkItemExist(final Item item) {
		ProductModelManager
				.getItemDetails(
						self,
						item.getId(),
						getHomeActivity().pref
								.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
						getHomeActivity().pref
								.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG),
						getHomeActivity().screenWidth,
						getHomeActivity().screenWidth, true,
						new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								ArrayList<Item> list = new ArrayList<Item>();
								list.add(item);

//								GlobalValue.arrItems = list;
//								GlobalValue.currentItemPosition = 0;

								Bundle bundle = new Bundle();
								bundle.putString(
										GlobalValue.KEY_ACTION_ITEM_DETAIL,
										GlobalValue.KEY_ACTION_ITEM_DETAIL_SINGLE_ITEM);
								
								bundle.putParcelableArrayList("arrItem", list);
								bundle.putInt("currentPosition", 0);

								gotoActivity(self, ItemDetailsActivity.class,
										bundle);
							}

							@Override
							public void onError(int statusCode, String json) {
								getHomeActivity()
										.showMessageDialog(
												getString(R.string.message_alert_invalid_item),
												new DialogListener() {

													@Override
													public void onClose(
															Dialog dialog) {
														// TODO Auto-generated
														// method
														// stub

													}
												});
							}

							@Override
							public void onError() {
								getHomeActivity()
										.showMessageDialog(
												getString(R.string.message_alert_invalid_item),
												new DialogListener() {

													@Override
													public void onClose(
															Dialog dialog) {
														// TODO Auto-generated
														// method
														// stub

													}
												});
							}
						});
	}

	private void followUser(String userId, final int position) {
		AccountModelManager.followUser(self, userId, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						arrFeed.get(position).setFollowed(true);
						adapterFeed.notifyDataSetChanged();
					}

					@Override
					public void onError(int statusCode, String json) {
						getHomeActivity()
								.showMessageDialog(
										getString(R.string.message_alert_general_error),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												// TODO Auto-generated method
												// stub

											}
										});

					}

					@Override
					public void onError() {
						getHomeActivity()
								.showMessageDialog(
										getString(R.string.message_alert_general_error),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												// TODO Auto-generated method
												// stub

											}
										});
					}
				});
	}

	private void unfollowUser(String userId, final int position) {
		AccountModelManager.unFollowUser(self, userId, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						arrFeed.get(position).setFollowed(false);
						adapterFeed.notifyDataSetChanged();
					}

					@Override
					public void onError(int statusCode, String json) {
						getHomeActivity()
								.showMessageDialog(
										getString(R.string.message_alert_general_error),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												// TODO Auto-generated method
												// stub

											}
										});

					}

					@Override
					public void onError() {
						getHomeActivity()
								.showMessageDialog(
										getString(R.string.message_alert_general_error),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												// TODO Auto-generated method
												// stub

											}
										});
					}
				});
	}

	private void getFollowingUsers() {

		AccountModelManager.getFollowingUsers(self,
				GlobalValue.myAccount.getId(), true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						List<Account> list = ParserUtility
								.parseListFollowerUsers(json);
						if (list.size() > 0) {
							llFeedList.setVisibility(View.VISIBLE);
							llInviteFriends.setVisibility(View.GONE);
							btnRefresh.setVisibility(View.VISIBLE);
							loadActivityFeed();
						} else {
							llInviteFriends.setVisibility(View.VISIBLE);
							llFeedList.setVisibility(View.GONE);
							btnRefresh.setVisibility(View.INVISIBLE);
						}

					}

					@Override
					public void onError(int statusCode, String json) {
						getHomeActivity()
								.showMessageDialog(
										getString(R.string.message_alert_general_error),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												// TODO Auto-generated method
												// stub
											}
										});

					}

					@Override
					public void onError() {
						getHomeActivity()
								.showMessageDialog(
										getString(R.string.message_alert_general_error),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												// TODO Auto-generated method
												// stub

											}
										});
					}
				});

	}

}
