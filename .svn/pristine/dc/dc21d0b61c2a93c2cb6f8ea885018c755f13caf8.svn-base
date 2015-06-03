package com.pt.treasuretrash.fragment;

import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.HomeActivity;
import com.pt.treasuretrash.activity.ItemDetailsActivity;
import com.pt.treasuretrash.adapter.FavouriteItemAdapter;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseFragment;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.modelmanager.ProductModelManager;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.pulltorefresh.library.PullToRefreshBase;
import com.pt.treasuretrash.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pt.treasuretrash.pulltorefresh.library.PullToRefreshScrollView;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.AutoFillHeightGridView;

public class FavoriteItemsFragment extends TreasureTrashBaseFragment {

	private RelativeLayout layoutNoFavourite;
	private AutoBgButton btnFindLocaltreasure;
	private PullToRefreshScrollView layoutFavourite;
	private ArrayList<Item> arrActiveFavourite, arrExpiredFavourite,
			arrGoneFavourite;
	private FavouriteItemAdapter activeAdapter, expriredAdapter, goneAdapter;
	private AutoFillHeightGridView grvExpiredFavourite, grvGoneFavourite;
	private AutoFillHeightGridView grvActiveFavourite;
	private TextView lblActiveFavourite, lblExpiredFavourite, lblGoneFavourite;
	private ImageView ivAddFavorite, ivAlreadyFavorite;
	private boolean isPullRefresh = false;
	public static boolean justChangeFavorite = false;
	
	private LocalBroadcastManager localBroadcastManager = null;

	private AQuery aq;

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(
					GlobalValue.INTENT_ACTION_DIAMOND)) {
				// layoutFavourite.scrollTo(0, 0);
				layoutFavourite.getRefreshableView().smoothScrollTo(0, 0);
			}

		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		aq = new AQuery(self);
	}

	@Override
	public int getFragmentResource() {
		// TODO Auto-generated method stub
		return R.layout.fragment_favorite_item;
	}

	@Override
	public void initView(View view) {
		// TODO Auto-generated method stub
		layoutNoFavourite = (RelativeLayout) view
				.findViewById(R.id.layoutNoFavourite);
		btnFindLocaltreasure = (AutoBgButton) view
				.findViewById(R.id.btnFindLocalTreasures);
		layoutFavourite = (PullToRefreshScrollView) view
				.findViewById(R.id.layoutShowFavourite);
		grvActiveFavourite = (AutoFillHeightGridView) view
				.findViewById(R.id.grvFavouriteActive);
		grvExpiredFavourite = (AutoFillHeightGridView) view
				.findViewById(R.id.grvFavouriteExpired);
		grvGoneFavourite = (AutoFillHeightGridView) view
				.findViewById(R.id.grvFavouriteGone);
		lblActiveFavourite = (TextView) view
				.findViewById(R.id.lblActiveFavourite);
		lblExpiredFavourite = (TextView) view
				.findViewById(R.id.lblExpiredFavourite);
		lblGoneFavourite = (TextView) view.findViewById(R.id.lblGoneFavourite);

		ivAddFavorite = (ImageView) view.findViewById(R.id.iv_AddFavorite);
		ivAlreadyFavorite = (ImageView) view
				.findViewById(R.id.iv_AlreadyFavorite);

		LayoutParams paramsAddFavorite = new LayoutParams(
				getHomeActivity().screenWidth * 94 / 640,
				getHomeActivity().screenWidth * 94 / 640 * 153 / 200);
		ivAddFavorite.setLayoutParams(paramsAddFavorite);

		LayoutParams paramsAlreadyFavorite = new LayoutParams(
				getHomeActivity().screenWidth * 94 / 640,
				getHomeActivity().screenWidth * 94 / 640 * 153 / 200);
		paramsAlreadyFavorite.height = paramsAlreadyFavorite.width * 153 / 200;
		paramsAlreadyFavorite.setMargins(
				0,
				(int) getResources().getDimension(
						R.dimen.margin_padding_xxsmall), 0, 0);
		ivAlreadyFavorite.setLayoutParams(paramsAlreadyFavorite);

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter filter = new IntentFilter();
		filter.addAction(GlobalValue.INTENT_ACTION_DIAMOND);
		
		localBroadcastManager = LocalBroadcastManager.getInstance(self);
		localBroadcastManager.registerReceiver(receiver,
				filter);
		
		if(justChangeFavorite){
			isPullRefresh = true;
			loadFavouriteItemList();
		}
		
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		localBroadcastManager.unregisterReceiver(receiver);
		localBroadcastManager = null;
	}

	@Override
	public void initData(View view) {
		// TODO Auto-generated method stub

	}

	@Override
	public void initControl(View view) {
		// TODO Auto-generated method stub
		initAllGridView();
		btnFindLocaltreasure.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getHomeActivity().showFragment(HomeActivity.FRAGMENT_GALLERY);
				getHomeActivity().setBottomMenu(HomeActivity.FRAGMENT_GALLERY);
			}
		});

		layoutFavourite
				.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

					@Override
					public void onRefresh(
							PullToRefreshBase<ScrollView> refreshView) {
						// TODO Auto-generated method stub
						isPullRefresh = true;
						loadFavouriteItemList();
					}
				});
	}

	private void initAllGridView() {
		// hide all text view
		lblActiveFavourite.setVisibility(View.GONE);
		lblExpiredFavourite.setVisibility(View.GONE);
		lblGoneFavourite.setVisibility(View.GONE);
		// list active favourite
		arrActiveFavourite = new ArrayList<Item>();
		activeAdapter = new FavouriteItemAdapter(
				(TreasureTrashBaseActivity) self, R.id.grvFavouriteActive,
				arrActiveFavourite);
		grvActiveFavourite.setAdapter(activeAdapter);
		// list active favourite

		arrExpiredFavourite = new ArrayList<Item>();
		expriredAdapter = new FavouriteItemAdapter(
				(TreasureTrashBaseActivity) self, R.id.grvFavouriteExpired,
				arrExpiredFavourite);
		grvExpiredFavourite.setAdapter(expriredAdapter);
		// list active favourite

		arrGoneFavourite = new ArrayList<Item>();
		goneAdapter = new FavouriteItemAdapter(
				(TreasureTrashBaseActivity) self, R.id.grvFavouriteGone,
				arrGoneFavourite);
		grvGoneFavourite.setAdapter(goneAdapter);
		//
		grvActiveFavourite.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (aq.getCachedImage(arrActiveFavourite.get(position)
						.getImage()) != null) {
					Bundle bundle = new Bundle();
					bundle.putString(GlobalValue.KEY_ACTION_ITEM_DETAIL,
							GlobalValue.KEY_ACTION_FROM_FAVORITE);
					bundle.putParcelableArrayList("arrItem", arrActiveFavourite);
					bundle.putInt("currentPosition", position);
					gotoActivity(self, ItemDetailsActivity.class, bundle);
				}

			}
		});
		grvExpiredFavourite.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (aq.getCachedImage(arrExpiredFavourite.get(position)
						.getImage()) != null) {
//					GlobalValue.arrItems = arrExpiredFavourite;
//					GlobalValue.currentItemPosition = position;
					Bundle bundle = new Bundle();
					bundle.putString(GlobalValue.KEY_ACTION_ITEM_DETAIL,
							GlobalValue.KEY_ACTION_FROM_FAVORITE);
					bundle.putParcelableArrayList("arrItem", arrExpiredFavourite);
					bundle.putInt("currentPosition", position);
					gotoActivity(self, ItemDetailsActivity.class, bundle);
				}
			}
		});
		grvGoneFavourite.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (aq.getCachedImage(arrGoneFavourite.get(position).getImage()) != null) {
//					GlobalValue.arrItems = arrGoneFavourite;
//					GlobalValue.currentItemPosition = position;
					Bundle bundle = new Bundle();
					bundle.putString(GlobalValue.KEY_ACTION_ITEM_DETAIL,
							GlobalValue.KEY_ACTION_FROM_FAVORITE);
					bundle.putParcelableArrayList("arrItem", arrGoneFavourite);
					bundle.putInt("currentPosition", position);
					gotoActivity(self, ItemDetailsActivity.class, bundle);
				}
			}
		});
	}

	@Override
	public void onShow() {
		getHomeActivity().setBottomMenu(HomeActivity.FRAGMENT_FAVORITE_ITEMS);

		loadFavouriteItemList();

	}
	
	@Override
	public void onHide() {
		// TODO Auto-generated method stub
		super.onHide();
		arrActiveFavourite.clear();
		arrExpiredFavourite.clear();
		arrGoneFavourite.clear();
		
		activeAdapter.notifyDataSetChanged();
		expriredAdapter.notifyDataSetChanged();
		goneAdapter.notifyDataSetChanged();
	}

	private void loadFavouriteItemList() {

		ProductModelManager.getListFavourites(self, !isPullRefresh,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub

						ArrayList<Item> arr = (ArrayList<Item>) ParserUtility
								.parseListFavoriteItem(json,
										getHomeActivity().IMAGE_SIZE);
						updateUI(arr.size() > 0);
						if (arr.size() > 0) {
							setDataForAllGrid(arr);
						}
						if (isPullRefresh) {
							layoutFavourite.onRefreshComplete();
							isPullRefresh = false;
						}

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

	private void setDataForAllGrid(ArrayList<Item> arrItem) {
		arrActiveFavourite.clear();
		arrExpiredFavourite.clear();
		arrGoneFavourite.clear();

		for (Item item : arrItem) {
			if (item.getState().equals(Item.STATE_GONE)) {
				arrGoneFavourite.add(item);
			} else if (item.getState().equals(Item.STATE_EXPIRED)) {
				arrExpiredFavourite.add(item);
			} else {
				arrActiveFavourite.add(item);
			}
		}

		activeAdapter.notifyDataSetChanged();
		expriredAdapter.notifyDataSetChanged();
		goneAdapter.notifyDataSetChanged();

		// check UI
		if (arrActiveFavourite.size() > 0) {
			grvActiveFavourite.setVisibility(View.VISIBLE);
			lblActiveFavourite.setVisibility(View.VISIBLE);
		} else {
			grvActiveFavourite.setVisibility(View.GONE);
			lblActiveFavourite.setVisibility(View.GONE);
		}
		if (arrExpiredFavourite.size() > 0) {
			grvExpiredFavourite.setVisibility(View.VISIBLE);
			lblExpiredFavourite.setVisibility(View.VISIBLE);
		} else {
			grvExpiredFavourite.setVisibility(View.GONE);
			lblExpiredFavourite.setVisibility(View.GONE);
		}
		if (arrGoneFavourite.size() > 0) {
			grvGoneFavourite.setVisibility(View.VISIBLE);
			lblGoneFavourite.setVisibility(View.VISIBLE);
		} else {
			grvGoneFavourite.setVisibility(View.GONE);
			lblGoneFavourite.setVisibility(View.GONE);
		}
	}

	private void updateUI(boolean isFavourite) {
		if (isFavourite) {
			layoutFavourite.setVisibility(View.VISIBLE);
			layoutNoFavourite.setVisibility(View.GONE);
		} else {
			layoutFavourite.setVisibility(View.GONE);
			layoutNoFavourite.setVisibility(View.VISIBLE);
		}
	}

}
