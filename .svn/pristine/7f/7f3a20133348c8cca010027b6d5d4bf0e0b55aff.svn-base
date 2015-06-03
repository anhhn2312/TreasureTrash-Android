package com.pt.treasuretrash.fragment;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.HomeActivity;
import com.pt.treasuretrash.activity.ItemDetailsActivity;
import com.pt.treasuretrash.adapter.ItemAdapter;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity.DialogListener;
import com.pt.treasuretrash.base.TreasureTrashBaseFragment;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.modelmanager.ProductModelManager;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.object.SearchObj;
import com.pt.treasuretrash.pulltorefresh.library.PullToRefreshBase;
import com.pt.treasuretrash.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.pt.treasuretrash.pulltorefresh.library.PullToRefreshGridView;
import com.pt.treasuretrash.utility.DialogUtility;
import com.pt.treasuretrash.utility.SmallUtility;
import com.pt.treasuretrash.widget.AutoBgButton;

public class GalleryFragment extends TreasureTrashBaseFragment implements
		OnClickListener {

	private PullToRefreshGridView mPullRefreshGridView;
	private LinearLayout llSearch;
	private RelativeLayout rlCategoryInfo, rlEditSearch;
	private TextView tvCategoryName;
	private EditText etSearch;
	private ImageView ivCategory;
	public ArrayList<Item> arrItem;
	public LinearLayout llShareOptions, llShare, llShareContainer, llFirstRow,
			llFirstRow_1, llFirstRow_2;
	public AutoBgButton btnShareFb, btnShareGoogle, btnShareEmail,
			btnShareMessage, btnSearch;
	private ItemAdapter adapter;
	public static int currentPage = 0;
	public static boolean hasMoreData = true, isProgress = true;
	public static String action;
	public static String keyword;
	public static String searchField;
	public static String categoryId;
	public static String categoryName;
	public static String itemSearchType;
	public static LocationObj searchLocationObj;
	public String searchType;

	public Item backUpPublishItem;

	public final String SEARCH_TYPE_CATEGORY = "SEARCH_TYPE_CATEGORY";
	public final String SEARCH_TYPE_KEYWORD = "SEARCH_TYPE_KEYWORD";
	public final String SEARCH_TYPE_ITEM_TYPE = "SEARCH_TYPE_ITEM_TYPE";

	public static boolean justChangeFavGallery = false;
//	public static boolean justRemovedFav = false;
	LocalBroadcastManager localBroadcastManager = null;

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(
					GlobalValue.INTENT_ACTION_DIAMOND)) {
				if (mPullRefreshGridView != null) {
					mPullRefreshGridView.getRefreshableView()
							.smoothScrollToPosition(0);
				}

			}

			if (intent.getAction().equalsIgnoreCase(
					GlobalValue.INTENT_ACTION_SEARCHING)) {
				arrItem.clear();
				action = GlobalValue.KEY_ACTION_SEARCH;
				isProgress = true;
				hasMoreData = true;
				currentPage = 0;
				// onShow();
			}

			if (intent.getAction().equalsIgnoreCase(
					GlobalValue.INTENT_ACTION_REFRESH)) {
				hasMoreData = true;
				isProgress = true;
				currentPage = 0;
				arrItem.clear();
				// initUIByAction();
				// if (action.equals(GlobalValue.KEY_ACTION_SHOW_BY_LOCATION)) {
				// showItemByLocation();
				// } else {
				// loadAllItems();
				// }

			}

		}
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		action = GlobalValue.KEY_ACION_DISPLAY;

	}

	@Override
	public int getFragmentResource() {
		// TODO Auto-generated method stub
		return R.layout.fragment_gallery;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// Refresh when item just is added/removed to favorite
		// if (justAddedFav) {
		// // arrItem.get(GlobalValue.currentItemPosition).setFavourited(true);
		// mPullRefreshGridView.setAdapter(adapter);
		// mPullRefreshGridView.getRefreshableView().smoothScrollToPosition(
		// GlobalValue.currentItemPosition);
		// justAddedFav = false;
		// }
		// if (justRemovedFav) {
		// // arrItem.get(GlobalValue.currentItemPosition).setFavourited(false);
		// mPullRefreshGridView.setAdapter(adapter);
		// mPullRefreshGridView.getRefreshableView().smoothScrollToPosition(
		// GlobalValue.currentItemPosition);
		// justRemovedFav = false;
		// }

		if (HomeActivity.isRefreshGallery || justChangeFavGallery) {
			List<Item> tempList = new ArrayList<Item>();
			tempList.addAll(GlobalValue.arrItems);
			arrItem.clear();
			arrItem.addAll(tempList);
			adapter.notifyDataSetChanged();
			mPullRefreshGridView.onRefreshComplete();
			mPullRefreshGridView.getRefreshableView().smoothScrollToPosition(
					GlobalValue.currentItemPosition);
			HomeActivity.isRefreshGallery = false;
			justChangeFavGallery = false;
		}

		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalValue.INTENT_ACTION_DIAMOND);
		filter.addAction(GlobalValue.INTENT_ACTION_SEARCHING);
		filter.addAction(GlobalValue.INTENT_ACTION_REFRESH);
		
		localBroadcastManager = LocalBroadcastManager.getInstance(self);
		localBroadcastManager.registerReceiver(receiver,
				filter);
	}
	
	

	@Override
	public void onShow() {

		backUpPublishItem = null;
		isProgress = true;
		hasMoreData = true;

		initUIByAction();

		if (arrItem.size() == 0) {
			if (action.equals(GlobalValue.KEY_ACTION_SHOW_BY_LOCATION)) {
				showItemByLocation();
			} else {
				if (getHomeActivity().bundle != null) {
					if (getHomeActivity().bundle
							.containsKey(GlobalValue.KEY_ACTION)) {
						if (getHomeActivity().bundle.getString(
								GlobalValue.KEY_ACTION).equals(
								GlobalValue.KEY_ACTION_LOGIN_REFRESH_TOKEN)) {

						} else {
							loadAllItems();
						}
					} else {
						loadAllItems();
					}
				} else {
					loadAllItems();
				}

			}
		}

		getHomeActivity().setBottomMenu(HomeActivity.FRAGMENT_GALLERY);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

		if (llShareOptions.getVisibility() == View.VISIBLE) {
			llShareOptions.setVisibility(View.GONE);
		}
		
		localBroadcastManager.unregisterReceiver(receiver);
		localBroadcastManager = null;
	}

	private void initUIByAction() {
		if (action.equals(GlobalValue.KEY_ACION_DISPLAY)) {

			// rlCategoryInfo.setVisibility(View.GONE);
			llSearch.setVisibility(View.GONE);
			// arrItem.clear();
		}

		else if (action.equals(GlobalValue.KEY_ACTION_SEARCH)) {

			arrItem.clear();

			llSearch.setVisibility(View.VISIBLE);
			rlCategoryInfo.setVisibility(View.VISIBLE);
			rlEditSearch.setVisibility(View.GONE);

			if (searchType.equals(SEARCH_TYPE_CATEGORY)) {
				ivCategory.setVisibility(View.VISIBLE);
				tvCategoryName.setText(categoryName);
			} else if (searchType.equals(SEARCH_TYPE_KEYWORD)) {
				ivCategory.setVisibility(View.GONE);
				tvCategoryName.setText("Results for \"" + keyword + "\"");
			} else if (searchType.equals(SEARCH_TYPE_ITEM_TYPE)) {
				ivCategory.setVisibility(View.GONE);
				tvCategoryName.setText("Results for " + itemSearchType
						+ " listings");
			}

		} else if (action.equals(GlobalValue.KEY_ACTION_SHOW_BY_LOCATION)) {
			arrItem.clear();
			llSearch.setVisibility(View.VISIBLE);
			rlCategoryInfo.setVisibility(View.VISIBLE);
			String location = "";
			if (searchLocationObj.getDistrict() != null) {
				location += searchLocationObj.getDistrict().equals("") ? ""
						: searchLocationObj.getDistrict() + ", ";
			}
			if (searchLocationObj.getState() != null) {
				location += searchLocationObj.getState().equals("") ? ""
						: searchLocationObj.getState() + ", ";
			}
			if (searchLocationObj.getCountry() != null) {
				location += searchLocationObj.getCountry().equals("") ? ""
						: searchLocationObj.getCountry();
			}
			tvCategoryName.setText(location);
		} else if (action.equals(GlobalValue.KEY_ACTION_SHOW_PUBLISHED_LISTING)) {

		}

	}

	@Override
	public void initView(View view) {
		mPullRefreshGridView = (PullToRefreshGridView) view
				.findViewById(R.id.pullGvGallery);

		llSearch = (LinearLayout) view.findViewById(R.id.llSearch);

		rlEditSearch = (RelativeLayout) view.findViewById(R.id.rlEditSearch);

		rlCategoryInfo = (RelativeLayout) view
				.findViewById(R.id.rlCategoryInfo);

		etSearch = (EditText) view.findViewById(R.id.etSearch);
		btnSearch = (AutoBgButton) view.findViewById(R.id.btnSearch);

		tvCategoryName = (TextView) view.findViewById(R.id.tvCategoryName);
		// tvCategoryName.setSelected(true);

		ivCategory = (ImageView) view.findViewById(R.id.ivCategoryArrow);

		btnShareFb = (AutoBgButton) view.findViewById(R.id.tvShareFb);
		btnShareGoogle = (AutoBgButton) view.findViewById(R.id.tvShareGoogle);
		btnShareEmail = (AutoBgButton) view.findViewById(R.id.tvShareEmail);
		btnShareMessage = (AutoBgButton) view.findViewById(R.id.tvShareMessage);

		llShareOptions = (LinearLayout) view.findViewById(R.id.llShareOptions);
		llShareOptions.setVisibility(View.GONE);

		llShare = (LinearLayout) view.findViewById(R.id.llShare);

		llFirstRow = (LinearLayout) view.findViewById(R.id.llFirstRow);

		llFirstRow_1 = (LinearLayout) view.findViewById(R.id.llFirstRow_1);
		llFirstRow_2 = (LinearLayout) view.findViewById(R.id.llFirstRow_2);

		LinearLayout.LayoutParams firstRowParams_1 = new LinearLayout.LayoutParams(
				getHomeActivity().screenWidth / 3,
				getHomeActivity().screenWidth / 3);
		llFirstRow_1.setLayoutParams(firstRowParams_1);
		llFirstRow_1.setBackgroundResource(R.drawable.bg_border_teal);

		LinearLayout.LayoutParams firstRowParams_2 = new LinearLayout.LayoutParams(
				getHomeActivity().screenWidth * 2 / 3,
				getHomeActivity().screenWidth / 3);
		llFirstRow_2.setLayoutParams(firstRowParams_2);
		llFirstRow_2.setBackgroundColor(getHomeActivity().getResources()
				.getColor(R.color.header_background_transparent_80));

		llShareContainer = (LinearLayout) view
				.findViewById(R.id.llShareContainer);

		mPullRefreshGridView
				.setOnRefreshListener(new OnRefreshListener2<GridView>() {

					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						hasMoreData = true;
						isProgress = false;
						currentPage = 0;
						arrItem.clear();
						if (action
								.equals(GlobalValue.KEY_ACTION_SHOW_BY_LOCATION)) {
							showItemByLocation();
						} else {
							loadAllItems();
						}
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<GridView> refreshView) {
						// mPullRefreshGridView.onRefreshComplete();
						if (!action
								.equals(GlobalValue.KEY_ACTION_SHOW_BY_LOCATION)) {
							currentPage++;
							isProgress = false;
							loadAllItems();
						} else {
							mPullRefreshGridView.onRefreshComplete();
						}
					}
				});

	}

	public void loadAllItems() {
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				if (hasMoreData) {
					List<Item> listItem = null;
					SearchObj obj = prepareSearchObject();
					ProductModelManager.searchItem(self, obj, isProgress,
							new ModelManagerListener() {

								@Override
								public void onSuccess(String json) {

									Log.e("aaaaaaaaa", "aaaaaaaaaa :" + json);
									List<Item> listItem = ParserUtility
											.parseListItem(json);

									if (listItem.size() > 0) {

										arrItem.addAll(listItem);
										if (action
												.equals(GlobalValue.KEY_ACTION_SHOW_PUBLISHED_LISTING)) {
											Log.i("PUBLISHED_ITEM",
													GlobalValue.publishedItem
															.getId());
											for (int i = 0; i < arrItem.size(); i++) {
												if (arrItem
														.get(i)
														.getId()
														.equalsIgnoreCase(
																GlobalValue.publishedItem
																		.getId())) {
													arrItem.remove(i);
													if (backUpPublishItem != null) {
														arrItem.add(i,
																backUpPublishItem);
													}
													break;
												}
											}

											

											GlobalValue.publishedItem
													.setMyPublishedListing(1);

											if (arrItem.size() <= listItem
													.size()) {
												
												if (arrItem.size() % 3 == 0) {
													backUpPublishItem = arrItem
															.get(arrItem.size() - 1);
													arrItem.remove(arrItem.size() - 1);
												}
												
												arrItem.add(
														0,
														GlobalValue.publishedItem);
											}

											llShareOptions
													.setVisibility(View.VISIBLE);
											action = GlobalValue.KEY_ACION_DISPLAY;

										}
									} else {
										hasMoreData = false;
										// DialogUtility.showShortToast(
										// self,
										// self.getString(R.string.no_more_data_to_show));

									}

									if (arrItem.size() == 0) {
										if (action
												.equals(GlobalValue.KEY_ACTION_SEARCH)
												&& searchType
														.equals(SEARCH_TYPE_KEYWORD)) {
											tvCategoryName
													.setText("No results for \""
															+ keyword + "\".");
											rlEditSearch
													.setVisibility(View.VISIBLE);

										}
									}
									adapter.notifyDataSetChanged();
									mPullRefreshGridView.onRefreshComplete();
									if (hasMoreData
											&& listItem.size() < arrItem.size()) {
										mPullRefreshGridView
												.getRefreshableView()
												.smoothScrollToPosition(
														arrItem.size()
																- listItem
																		.size()
																+ 1);
									}

									if (listItem.size() < GlobalValue.GALLERY_PAGE_SIZE) {
										hasMoreData = false;
									}

									// if (action
									// .equalsIgnoreCase(GlobalValue.KEY_ACTION_SHOW_PUBLISHED_LISTING))
									// {
									// action = GlobalValue.KEY_ACION_DISPLAY;
									// }

								}

								@Override
								public void onError() {
									mPullRefreshGridView.onRefreshComplete();
									DialogUtility.showShortToast(
											self,
											self.getString(R.string.message_server_error));
								}

								@Override
								public void onError(int statusCode, String json) {
									mPullRefreshGridView.onRefreshComplete();
									DialogUtility.showShortToast(
											self,
											self.getString(R.string.message_server_error));

								}
							});
				} else {
					// DialogUtility.showShortToast(self,
					// self.getString(R.string.no_more_data_to_show));
					mPullRefreshGridView.onRefreshComplete();
				}
			}
		}, 100);
		// if (getHomeActivity().pref
		// .getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LAT) !=
		// 0d
		// && getHomeActivity().pref
		// .getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LNG) !=
		// 0d) {
		//
		// } else {
		// getHomeActivity().showMessageDialog(
		// getString(R.string.message_alert_not_found_connection),
		// new DialogListener() {
		//
		// @Override
		// public void onClose(Dialog dialog) {
		// // TODO Auto-generated method stub
		//
		// }
		// });
		// }
	}

	private void showItemByLocation() {
		ProductModelManager
				.getItemByLocation(
						self,
						searchLocationObj.getCountry(),
						searchLocationObj.getState(),
						searchLocationObj.getDistrict() == null ? ""
								: searchLocationObj.getDistrict(),
						getHomeActivity().pref
								.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
						getHomeActivity().pref
								.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG),
						true, new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								mPullRefreshGridView.onRefreshComplete();
								Log.e("GALLERY_FRAGMENT", json);
								List<Item> listItem = ParserUtility
										.parseListItem(json);

								if (listItem.size() > 0) {
									arrItem.addAll(listItem);
									adapter.notifyDataSetChanged();
									hasMoreData = false;
								} else {
									hasMoreData = false;
									// DialogUtility.showShortToast(self, self
									// .getString(R.string.no_more_data_to_show));
								}

							}

							@Override
							public void onError(int statusCode, String json) {
								mPullRefreshGridView.onRefreshComplete();
								DialogUtility.showShortToast(
										self,
										self.getString(R.string.message_server_error));

							}

							@Override
							public void onError() {
								mPullRefreshGridView.onRefreshComplete();
								DialogUtility.showShortToast(
										self,
										self.getString(R.string.message_server_error));

							}
						});
	}

	private SearchObj prepareSearchObject() {

		SearchObj obj = new SearchObj();
		obj.setCurrentPage(currentPage);
		obj.setLatitude(getHomeActivity().pref
				.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT));
		obj.setLongitide(getHomeActivity().pref
				.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG));
		// obj.setLatitude(((HomeActivity) self).gpsTracker.getLatitude());
		// obj.setLongitide(((HomeActivity) self).gpsTracker.getLongitude());
		// obj.setLatitude(21.039225);
		// obj.setLongitide(105.828852);
		obj.setPageSize(GlobalValue.GALLERY_PAGE_SIZE);
		obj.setImageWidth(getHomeActivity().IMAGE_SMALL_SIZE);
		obj.setImageHeight(getHomeActivity().IMAGE_SMALL_SIZE);

		if (action.equals(GlobalValue.KEY_ACTION_SEARCH)) {
			obj.setCategoryId(categoryId);
			obj.setKeyWord(keyword);
			obj.setSearchField(searchField);
			obj.setItemType(itemSearchType);
		}

		return obj;
	}

	@Override
	public void initData(View view) {
		currentPage = 0;

		arrItem = new ArrayList<Item>();
		adapter = new ItemAdapter((TreasureTrashBaseActivity) self,
				R.id.pullGvGallery, arrItem);
		mPullRefreshGridView.setAdapter(adapter);

	}

	@Override
	public void initControl(View view) {
		etSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					processSearching();
					return true;
				}
				return false;
			}
		});

		mPullRefreshGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// DialogUtility.showShortToast(self, "position: " + position);
				if (getHomeActivity().aq.getCachedImage(arrItem.get(position)
						.getImage()) != null) {
					Bundle bundle = new Bundle();
					bundle.putString(GlobalValue.KEY_ACTION_ITEM_DETAIL,
							GlobalValue.KEY_ACTION_FROM_GALLERY);
					bundle.putParcelableArrayList("arrItem", arrItem);
					bundle.putInt("currentPosition", position);
					gotoActivity(self, ItemDetailsActivity.class, bundle);
				}
			}
		});

		ivCategory.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				getHomeActivity().rightMenu.toggle();

			}
		});

		llShare.setOnClickListener(this);
		llShareOptions.setOnClickListener(this);
		llShareContainer.setOnClickListener(this);
		btnShareFb.setOnClickListener(this);
		btnShareGoogle.setOnClickListener(this);
		btnShareEmail.setOnClickListener(this);
		btnShareMessage.setOnClickListener(this);
		btnSearch.setOnClickListener(this);

	}

	public void setSearchAttribute(String categoryId, String categoryName,
			String keyword, String searchField, String itemType) {
		this.categoryId = categoryId;
		this.categoryName = categoryName;
		this.keyword = keyword;
		this.searchField = searchField;
		this.itemSearchType = itemType;
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(
				receiver);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		arrItem = null;
		adapter = null;
	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
	}

	@Override
	public void onClick(View v) {
		if (v == llShareOptions || v == llShareContainer) {
			llShareOptions.setVisibility(View.GONE);
			// action = GlobalValue.KEY_ACION_DISPLAY;
			// arrItem.get(0).setMyPublishedListing(0);
			// adapter.notifyDataSetChanged();
			return;
		}

		if (v == btnShareFb) {
			getHomeActivity().onShareFacebook(v, GlobalValue.publishedItem);
			return;
		}
		if (v == btnShareGoogle) {
			getHomeActivity().onShareGoogle(v, GlobalValue.publishedItem);
			return;
		}
		if (v == btnShareEmail) {
			getHomeActivity().onShareEmail(v, GlobalValue.publishedItem);
			return;
		}
		if (v == btnShareMessage) {
			getHomeActivity().onShareMessage(v, GlobalValue.publishedItem);
			return;
		}
		if (v == btnSearch) {
			processSearching();
			return;
		}

	}

	private void processSearching() {
		if (etSearch.getText().toString().trim().length() > 0) {
			String strSearch = etSearch.getText().toString().trim();
			etSearch.setText("");
			setSearchAttribute("", "", strSearch, "", "");
			rlEditSearch.setVisibility(View.GONE);
			tvCategoryName.setText("Results for \"" + keyword + "\"");
			hasMoreData = true;
			loadAllItems();
		}

	}

	public void refreshGallery() {
		mPullRefreshGridView.getRefreshableView().scrollBy(0,
				-getHomeActivity().screenWidth);
		// arrItem.clear();
		// arrItem.addAll(GlobalValue.arrItems);
		// adapter.notifyDataSetChanged();
		HomeActivity.isRefreshGallery = false;
	}

	public void closeShareLayout() {
		if (llShareOptions.getVisibility() == View.VISIBLE) {
			llShareOptions.setVisibility(View.GONE);
		}
	}

}
