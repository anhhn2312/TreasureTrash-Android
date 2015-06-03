package com.pt.treasuretrash.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Path.FillType;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.adapter.FollowerUserAdapter;
import com.pt.treasuretrash.adapter.ItemAdapter;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.fragment.GalleryFragment;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.slidingmenu.SlidingMenu;
import com.pt.treasuretrash.slidingmenu.SlidingMenu.IOnMenuToggle;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.AutoFillHeightGridView;

public class SellerProfileActivity extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	private RelativeLayout rlFollow;

	private TextView tvUser, tvMemberType, tvFollow, tvListingCount,
			tvFollowerCount, tvFollowingCount, tvLocation;
	private ImageView ivAvatar, ivReward, ivCloseMenu;

	private AutoFillHeightGridView gvSelling, gvGone;
	private AutoBgButton btnFollow, btnBack, btnFollower, btnFollowing;

	private ArrayList<Item> arrSellingItems;
	private ArrayList<Item> arrGoneItems;

	private ItemAdapter adapterGone, adapterSelling;

	private GoogleMap mMap;
	private MarkerOptions markerOption = null;

	private Account currentUserAcc = null;
	private String currentUserId = null;

	private SlidingMenu rightMenu;

	private ListView lvUsers;

	private List<Account> arrUsers;
	private FollowerUserAdapter adapterUser;

	private AQuery aq;

	private final int UPDATE_CURRENT_ACC = 1;
	private final int UPDATE_RIGHT_MENU = 2;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seller_profile);
		initUI();
		initData();
		initControl();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (currentUserAcc == null && currentUserId != null) {
			loadUserProfile();
		}
	}

	private void initUI() {
		rlFollow = (RelativeLayout) findViewById(R.id.rlFollow);

		tvUser = (TextView) findViewById(R.id.tvUser);
		tvMemberType = (TextView) findViewById(R.id.tvMemberType);
		tvFollow = (TextView) findViewById(R.id.tvFollow);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvListingCount = (TextView) findViewById(R.id.tvListingCount);
		tvFollowerCount = (TextView) findViewById(R.id.tvFollowerCount);
		tvFollowingCount = (TextView) findViewById(R.id.tvFollowingCount);

		gvSelling = (AutoFillHeightGridView) findViewById(R.id.gvSelling);
		gvGone = (AutoFillHeightGridView) findViewById(R.id.gvGone);

		ivAvatar = (ImageView) findViewById(R.id.ivAvatar);
		ivReward = (ImageView) findViewById(R.id.ivMemberType);
		ivCloseMenu = (ImageView) findViewById(R.id.ivCloseMenu);

		btnFollow = (AutoBgButton) findViewById(R.id.btnFollow);
		btnBack = (AutoBgButton) findViewById(R.id.btnBack);

		initMenu();
		setupMap();

	}

	private void initData() {
		aq = new AQuery(self);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(GlobalValue.KEY_SELLER_ID)) {
				currentUserId = bundle.getString(GlobalValue.KEY_SELLER_ID);
				Log.d("SELLER_ID", currentUserId);
			}
		}

		arrGoneItems = new ArrayList<Item>();
		arrSellingItems = new ArrayList<Item>();
		adapterGone = new ItemAdapter((TreasureTrashBaseActivity) self,
				R.id.gvGone, arrGoneItems);
		gvGone.setAdapter(adapterGone);

		adapterSelling = new ItemAdapter((TreasureTrashBaseActivity) self,
				R.id.gvSelling, arrSellingItems);
		gvSelling.setAdapter(adapterSelling);

		arrUsers = new ArrayList<Account>();
		adapterUser = new FollowerUserAdapter((TreasureTrashBaseActivity) self,
				R.id.lvUsers, arrUsers);
		lvUsers.setAdapter(adapterUser);

	}

	private void initControl() {
		btnBack.setOnClickListener(this);
		btnFollow.setOnClickListener(this);
		ivCloseMenu.setOnClickListener(this);
		btnFollower.setOnClickListener(this);
		btnFollowing.setOnClickListener(this);
		tvLocation.setOnClickListener(this);
		tvFollow.setOnClickListener(this);
		findViewById(R.id.rlFollow).setOnClickListener(this);

		getParentView(tvFollowerCount).setOnClickListener(this);
		getParentView(tvFollowingCount).setOnClickListener(this);

		gvSelling.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				GlobalValue.arrItems = arrSellingItems;
//				GlobalValue.currentItemPosition = position;
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("arrItem", arrSellingItems);
				bundle.putInt("currentPosition", position);
				gotoActivity(self, ItemDetailsActivity.class, bundle);

			}

		});

		gvGone.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
//				GlobalValue.arrItems = arrGoneItems;
//				GlobalValue.currentItemPosition = position;
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("arrItem", arrGoneItems);
				bundle.putInt("currentPosition", position);
				gotoActivity(self, ItemDetailsActivity.class, bundle);

			}
		});

		lvUsers.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				onFollowClick(arrUsers.get(position), UPDATE_RIGHT_MENU);
			}
		});

	}

	private void initMenu() {
		rightMenu = new SlidingMenu(self, onMenuToggleListener);
		rightMenu.setMenu(R.layout.menu_seller_profile_right);
		rightMenu.setMode(SlidingMenu.RIGHT);
		rightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		rightMenu.setShadowWidthRes(R.dimen.shadow_width);
		rightMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		rightMenu.setFadeDegree(0.5f);

		rightMenu.attachToActivity(SellerProfileActivity.this,
				SlidingMenu.SLIDING_CONTENT, true);

		btnFollower = (AutoBgButton) rightMenu.findViewById(R.id.btnFollower);
		btnFollowing = (AutoBgButton) rightMenu.findViewById(R.id.btnFollowing);
		lvUsers = (ListView) rightMenu.findViewById(R.id.lvUsers);

	}

	private void setupMap() {
		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();

		mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
		mMap.setMyLocationEnabled(false);
		mMap.getUiSettings().setZoomControlsEnabled(false);
		mMap.getUiSettings().setZoomGesturesEnabled(false);
		mMap.getUiSettings().setMyLocationButtonEnabled(false);
		mMap.getUiSettings().setAllGesturesEnabled(false);

	}

	private void loadUserProfile() {
		AccountModelManager.getProfileUserById(self, currentUserId, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {

						currentUserAcc = ParserUtility.parseAccountById(json,
								IMAGE_SMALL_SIZE);
						fillDetails();

					}

					@Override
					public void onError(int statusCode, String json) {
						showMessageDialog(
								getString(R.string.message_alert_general_error),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub

									}
								});

					}

					@Override
					public void onError() {
						showMessageDialog(
								getString(R.string.message_alert_general_error),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub

									}
								});

					}
				});
	}

	private void fillDetails() {
		tvUser.setText(currentUserAcc.getUsername());

		if (currentUserAcc.getRewardType().equalsIgnoreCase(Item.REWARD_GOLD)) {
			ivReward.setBackgroundResource(R.drawable.image_star_gold);
			tvMemberType.setText(getString(R.string.gold_member));
		} else if (currentUserAcc.getRewardType().equalsIgnoreCase(
				Item.REWARD_SILVER)) {
			ivReward.setBackgroundResource(R.drawable.image_star_silver);
			tvMemberType.setText(getString(R.string.silver_member));

		} else if (currentUserAcc.getRewardType().equalsIgnoreCase(
				Item.REWARD_BRONZE)) {
			ivReward.setBackgroundResource(R.drawable.image_star_bronze);
			tvMemberType.setText(getString(R.string.bronze_member));
		} else if (currentUserAcc.getRewardType().equalsIgnoreCase(
				Item.REWARD_NEW_MEMBER)) {
			ivReward.setBackgroundResource(R.drawable.image_new_member);
			tvMemberType.setText(getString(R.string.new_member));
		}

		if (currentUserAcc.isFollowed()) {
			btnFollow.setBackgroundResource(R.drawable.btn_unfollow_user);
			tvFollow.setText(getString(R.string.unfollow));
		} else {
			btnFollow.setBackgroundResource(R.drawable.btn_follow_user);
			tvFollow.setText(getString(R.string.follow));
		}

		tvFollowerCount.setText(currentUserAcc.getTotalFollower() + "");
		tvFollowingCount.setText(currentUserAcc.getTotalFollowing() + "");
		String location = "";

		LocationObj locationObj = currentUserAcc.getLocation();
		location += locationObj.getDistrict().equals("") ? "" : locationObj
				.getDistrict() + ", ";
		location += locationObj.getState().equals("") ? "" : locationObj
				.getState() + ", ";
		location += locationObj.getCountry().equals("") ? "" : locationObj
				.getCountry();
		tvLocation.setText(location);


		Log.d("USER_IMAGE", currentUserAcc.getImageUrl());
		aq.id(ivAvatar).image(currentUserAcc.getImageUrl(), false, true, 0,
				R.drawable.image_avatar_default_no_border);

		arrGoneItems.clear();
		arrSellingItems.clear();
		for (int i = 0; i < currentUserAcc.getArrItems().size(); i++) {
			Item item = currentUserAcc.getArrItems().get(i);
			if (item.getState().equals(Item.STATE_GONE)) {
				arrGoneItems.add(item);
			} else {
				if (item.getState().equals(Item.STATE_ACTIVE)) {
					arrSellingItems.add(item);
				}
			}
		}

		tvListingCount.setText(currentUserAcc.getArrItems().size() + "");

		adapterGone.notifyDataSetChanged();
		adapterSelling.notifyDataSetChanged();
		
//		setupMap();

		setLocationLatLong(locationObj.getLatitude(),
				locationObj.getLongitude());

	}

	private void setLocationLatLong(double latitude, double longitude) {


		markerOption = new MarkerOptions().position(new LatLng(latitude,
				longitude));
		Marker currentMarker = mMap.addMarker(markerOption);

		// set filter
		LatLng latLng = new LatLng(latitude, longitude);
		// GlobalValue.CURRENT_LOCATION = latitude + "," + longitude;
		mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
		mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);// zoom :
																		// 2-21

	}

	@Override
	public void onClick(View v) {
		if (v == btnBack) {
			super.onBackPressed();
		}

		if (v == btnFollow || v == tvFollow || v == rlFollow) {
			onFollowClick(currentUserAcc, UPDATE_CURRENT_ACC);
		}

		if (v == ivCloseMenu) {
			rightMenu.toggle();
		}

		if (v == getParentView(tvFollowerCount)) {
			getFollowedUsers();
		}

		if (v == getParentView(tvFollowingCount)) {
			getFollowingUsers();
		}

		if (v == btnFollower) {
			getFollowedUsers();
		}

		if (v == btnFollowing) {
			getFollowingUsers();
		}

		if (v == tvLocation) {
			Bundle bundle = new Bundle();
			bundle.putString(GlobalValue.KEY_ACTION,
					GlobalValue.KEY_ACTION_SHOW_BY_LOCATION);
			GalleryFragment.searchLocationObj = currentUserAcc.getLocation();
			gotoActivity(self, HomeActivity.class, bundle,
					Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}

	}

	private void onFollowClick(final Account account, final int updateType) {
		if (account.isFollowed()) {
			AccountModelManager.unFollowUser(self, account.getId(), true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {

							updateFollow(false, updateType, account);

						}

						@Override
						public void onError(int statusCode, String json) {
							showMessageDialog(
									getString(R.string.message_alert_general_error),
									new DialogListener() {

										@Override
										public void onClose(Dialog dialog) {
											// TODO Auto-generated method stub

										}
									});

						}

						@Override
						public void onError() {
							showMessageDialog(
									getString(R.string.message_alert_general_error),
									new DialogListener() {

										@Override
										public void onClose(Dialog dialog) {
											// TODO Auto-generated method stub

										}
									});

						}
					});
		} else {
			AccountModelManager.followUser(self, account.getId(), true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {

							updateFollow(true, updateType, account);

						}

						@Override
						public void onError(int statusCode, String json) {
							showMessageDialog(
									getString(R.string.message_alert_general_error),
									new DialogListener() {

										@Override
										public void onClose(Dialog dialog) {
											// TODO Auto-generated method stub

										}
									});

						}

						@Override
						public void onError() {
							showMessageDialog(
									getString(R.string.message_alert_general_error),
									new DialogListener() {

										@Override
										public void onClose(Dialog dialog) {
											// TODO Auto-generated method stub

										}
									});

						}
					});
		}
	}

	private void updateFollow(boolean isFollowed, int updateType,
			Account account) {
		if (updateType == UPDATE_CURRENT_ACC) {
			if (isFollowed) {
				currentUserAcc.setFollowed(true);
				currentUserAcc.setTotalFollower(currentUserAcc
						.getTotalFollower() + 1);
				fillDetails();

			} else {
				currentUserAcc.setFollowed(false);
				currentUserAcc.setTotalFollower(currentUserAcc
						.getTotalFollower() - 1);
				fillDetails();
			}
		} else if (updateType == UPDATE_RIGHT_MENU) {
			if (isFollowed) {
				account.setFollowed(true);
				adapterUser.notifyDataSetChanged();

			} else {
				account.setFollowed(false);
				adapterUser.notifyDataSetChanged();
			}

		}
	}

	private void getFollowedUsers() {
		rightMenu.showMenu(true);
		btnFollower.setBackgroundResource(R.drawable.bg_follower_selected);
		btnFollower.setTextColor(getResources().getColor(R.color.white));
		btnFollowing.setBackgroundResource(R.drawable.bg_following);
		btnFollowing.setTextColor(getResources().getColor(R.color.sign_up_teal));

		AccountModelManager.getFollowedUsers(self, currentUserAcc.getId(),
				true, new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						List<Account> list = ParserUtility
								.parseListFollowerUsers(json);
						arrUsers.clear();
						arrUsers.addAll(list);
						adapterUser.notifyDataSetChanged();

					}

					@Override
					public void onError(int statusCode, String json) {
						showMessageDialog(
								getString(R.string.message_alert_general_error),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub

									}
								});

					}

					@Override
					public void onError() {
						showMessageDialog(
								getString(R.string.message_alert_general_error),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub

									}
								});

					}
				});
	}

	private void getFollowingUsers() {
		rightMenu.showMenu(true);
		btnFollower.setBackgroundResource(R.drawable.bg_follower);
		btnFollower.setTextColor(getResources().getColor(R.color.sign_up_teal));
		btnFollowing.setBackgroundResource(R.drawable.bg_following_selected);
		btnFollowing.setTextColor(getResources().getColor(R.color.white));

		AccountModelManager.getFollowingUsers(self, currentUserAcc.getId(),
				true, new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						List<Account> list = ParserUtility
								.parseListFollowerUsers(json);
						arrUsers.clear();
						arrUsers.addAll(list);
						adapterUser.notifyDataSetChanged();

					}

					@Override
					public void onError(int statusCode, String json) {
						showMessageDialog(
								getString(R.string.message_alert_general_error),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub

									}
								});

					}

					@Override
					public void onError() {
						showMessageDialog(
								getString(R.string.message_alert_general_error),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub

									}
								});

					}
				});
	}

	private IOnMenuToggle onMenuToggleListener = new IOnMenuToggle() {

		@Override
		public void onOpen() {
			rlFollow.setVisibility(View.GONE);
			ivCloseMenu.setVisibility(View.VISIBLE);

		}

		@Override
		public void onClose() {
			rlFollow.setVisibility(View.VISIBLE);
			ivCloseMenu.setVisibility(View.GONE);

		}
	};
}
