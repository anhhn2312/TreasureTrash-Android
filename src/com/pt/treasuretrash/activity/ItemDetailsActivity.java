package com.pt.treasuretrash.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.adapter.ImageObjAdapter;
import com.pt.treasuretrash.adapter.ImageObjAdapter.IOnFirstItemLoaded;
import com.pt.treasuretrash.adapter.ItemImagesPagerAdapter;
import com.pt.treasuretrash.adapter.ItemImagesPagerAdapter.IOnImageClick;
import com.pt.treasuretrash.base.TreasureTrashBaseShareActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.fragment.FavoriteItemsFragment;
import com.pt.treasuretrash.fragment.GalleryFragment;
import com.pt.treasuretrash.inapppurchase.IabHelper;
import com.pt.treasuretrash.inapppurchase.IabResult;
import com.pt.treasuretrash.inapppurchase.Inventory;
import com.pt.treasuretrash.inapppurchase.Purchase;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.modelmanager.ProductModelManager;
import com.pt.treasuretrash.object.ImageObj;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.object.MessageItem;
import com.pt.treasuretrash.object.SearchObj;
import com.pt.treasuretrash.utility.DialogUtility;
import com.pt.treasuretrash.utility.ImageUtil;
import com.pt.treasuretrash.utility.SmallUtility;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.CustomDialog;
import com.pt.treasuretrash.widget.CustomDialog.OnCustomDialogClickListener;
import com.pt.treasuretrash.widget.HelveticaBoldTextView;
import com.pt.treasuretrash.widget.HelveticaLightTextView;
import com.pt.treasuretrash.widget.HorizontalListView;

public class ItemDetailsActivity extends TreasureTrashBaseShareActivity
		implements OnClickListener {

	private List<Item> arrItems;
	private int currentPosition = -1, itemMessage;
	// private Item currentItem;
	private Item loadingItem;
	private ImageObjAdapter imageThumbAdapter;
	private ScrollView svMain, svDescription;

	private HorizontalListView lvThumb;
	private AutoBgButton btnBack, btnPrev, btnNext, btnShare, btnConfirmReport,
			btnCancelReport, btnEdit, btnDelete;
	private TextView tvName, tvPrice, tvCategory, tvLocation, tvSeller,
			tvDescription, tvDistance, tvContactAlert, tvContactSeller,
			tvEditItem, tvItemGone;
	private ImageView ivFlag, ivAddFavorite, ivContactSeller, ivReward,
			ivSellerAndMessage, btnRelist, ivItemExpired, ivGone, ivEditItem,
			ivItemGone;

	private View viewTransparent;

	private LinearLayout llAlertFlagItem, llAlertShareItem,
			llAlertFavoriteItem, llAlertContactSeller, llAlertSellerProfile,
			btnContactSeller, btnEditItem, btnItemGone, llEditOptions, llMain;

	private LinearLayout llAddToFavorite, llReportInapproriate, llReportOption,
			llGone, llShare, llFullGallery, llEdit, llRelist, llRelistSuccess,
			llPremium, llShareOptions;
	private LinearLayout llCloseContactSeller;
	private RelativeLayout rlHeader, rlContactSeller, rlRelist, rlFullGallery1,
			rlFullGallery2, rlBottom, rlDecor;
	private ItemImagesPagerAdapter adapterImages;
	private ViewPager pagerImages, pagerFullScreen;
	private ArrayList<ImageObj> arrItemImages;
	private ArrayList<ImageObj> arrThumbImages;
	private List<View> arrAlertLayout = new ArrayList<View>();

	private String forceLoginAction = null;
	private String displayAction = "";
	private String navigateFrom = "";

	private AQuery listAq;
	private Handler handler;
	private Bundle bundle;

	// For purchasing
	static String CURRENT_PURCHASE = "";
	private static final int RC_REQUEST = 10001;
	private IabHelper mHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_item_details_layout);
		initInAppPurchase();

		handler = new Handler();
		// printKeyHash(self);
		initUI();
		initControl();

		bundle = getIntent().getExtras();
		if (bundle != null) {
			Log.i(this.getClass().getSimpleName(), "Bundle not null");
			if (bundle.containsKey(GlobalValue.FORCED_LOGIN_ACTION)) {
				forceLoginAction = bundle
						.getString(GlobalValue.FORCED_LOGIN_ACTION);
			}
			if (bundle.containsKey(GlobalValue.KEY_ACTION_ITEM_DETAIL)) {
				displayAction = bundle
						.getString(GlobalValue.KEY_ACTION_ITEM_DETAIL);
				navigateFrom = bundle
						.getString(GlobalValue.KEY_ACTION_ITEM_DETAIL);
			}
		}

	}

	public static String printKeyHash(Activity context) {
		PackageInfo packageInfo;
		String key = null;
		try {

			// getting application package name, as defined in manifest
			String packageName = context.getApplicationContext()
					.getPackageName();

			// Retriving package info
			packageInfo = context.getPackageManager().getPackageInfo(
					packageName, PackageManager.GET_SIGNATURES);

			Log.e("Package Name=", context.getApplicationContext()
					.getPackageName());

			for (Signature signature : packageInfo.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				key = new String(Base64.encode(md.digest(), 0));

				// String key = new String(Base64.encodeBytes(md.digest()));
				Log.e("Key Hash=", key);

			}
		} catch (NameNotFoundException e1) {
			Log.e("Name not found", e1.toString());
		}

		catch (NoSuchAlgorithmException e) {
			Log.e("No such an algorithm", e.toString());
		} catch (Exception e) {
			Log.e("Exception", e.toString());
		}

		return key;
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// mGoogleApiClient.connect();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		hideSoftKeyboard(tvCategory);

		resetLayout();
		initData();
		if (loadingItem != null) {
			Log.i("Loading_item", "not null");
			// loadItemDetails();
			if (displayAction
					.equals(GlobalValue.KEY_ACTION_ITEM_DETAIL_SINGLE_ITEM)) {
				loadItemDetails(loadingItem.getId());
			} else {
				fillDetailsLayout();
			}
		}

	}

	private void disableBtnNextPrev() {
		btnNext.setVisibility(View.INVISIBLE);
		btnPrev.setVisibility(View.INVISIBLE);
	}

	private void resetLayout() {
		llShare.setBackgroundColor(getResources().getColor(R.color.transparent));
		llCloseContactSeller.setVisibility(View.GONE);
		ivAddFavorite.setBackgroundResource(R.drawable.btn_add_favorite_);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}

		pagerImages.setAdapter(null);
		pagerFullScreen.setAdapter(null);

		super.onDestroy();
	}

	private void initUI() {

		svMain = (ScrollView) findViewById(R.id.svMain);
		svDescription = (ScrollView) findViewById(R.id.svDescription);

		pagerImages = (ViewPager) findViewById(R.id.pager);
		pagerFullScreen = (ViewPager) findViewById(R.id.pagerFullScreen);

		lvThumb = (HorizontalListView) findViewById(R.id.lvThumb);

		viewTransparent = (View) findViewById(R.id.viewTransparent);

		llMain = (LinearLayout) findViewById(R.id.llMain);
		llAddToFavorite = (LinearLayout) findViewById(R.id.llAddToFavorite);
		llReportInapproriate = (LinearLayout) findViewById(R.id.llReportInapproriate);
		llReportOption = (LinearLayout) findViewById(R.id.llReportOptions);
		llShare = (LinearLayout) findViewById(R.id.llShare);
		llShareOptions = (LinearLayout) findViewById(R.id.llShareOptions);
		llFullGallery = (LinearLayout) findViewById(R.id.llFullGallery);
		llGone = (LinearLayout) findViewById(R.id.llGone);
		llEdit = (LinearLayout) findViewById(R.id.llEdit);
		llRelist = (LinearLayout) findViewById(R.id.llRelist);
		llRelistSuccess = (LinearLayout) findViewById(R.id.llRelistSuccess);
		llEditOptions = (LinearLayout) findViewById(R.id.llEditOptions);

		rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
		rlContactSeller = (RelativeLayout) findViewById(R.id.rlContactSeller);

		rlRelist = (RelativeLayout) findViewById(R.id.rlRelist);

		rlDecor = (RelativeLayout) findViewById(R.id.rlDecor);

		rlBottom = (RelativeLayout) findViewById(R.id.rlBotttom);

		rlFullGallery1 = (RelativeLayout) findViewById(R.id.rlFullGallery1);
		rlFullGallery2 = (RelativeLayout) findViewById(R.id.rlFullGallery2);

		btnBack = (AutoBgButton) findViewById(R.id.btnBack);
		btnNext = (AutoBgButton) findViewById(R.id.btnNext);
		btnPrev = (AutoBgButton) findViewById(R.id.btnPrev);
		btnShare = (AutoBgButton) findViewById(R.id.btnShare);
		btnContactSeller = (LinearLayout) findViewById(R.id.btnContactSeller);
		btnConfirmReport = (AutoBgButton) findViewById(R.id.btnConfirmReport);
		btnCancelReport = (AutoBgButton) findViewById(R.id.btnCancelReport);
		btnEditItem = (LinearLayout) findViewById(R.id.btnEditItem);
		btnItemGone = (LinearLayout) findViewById(R.id.btnItemGone);
		btnRelist = (ImageView) findViewById(R.id.btnRelist);
		ivItemExpired = (ImageView) findViewById(R.id.ivItemExpired);
		btnEdit = (AutoBgButton) findViewById(R.id.btnEdit);
		btnDelete = (AutoBgButton) findViewById(R.id.btnDelete);

		tvName = (TextView) findViewById(R.id.tvName);
		// tvName.setSelected(true);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		tvCategory = (TextView) findViewById(R.id.tvCategory);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		// tvLocation.setSelected(true);
		tvSeller = (TextView) findViewById(R.id.tvSeller);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
		tvDistance = (TextView) findViewById(R.id.tvDistance);
		tvContactAlert = (TextView) findViewById(R.id.tvContactAlert);
		tvContactSeller = (TextView) findViewById(R.id.tvContactSeller);
		tvEditItem = (TextView) findViewById(R.id.tvEdit);
		tvItemGone = (TextView) findViewById(R.id.tvItemGone);

		ivFlag = (ImageView) findViewById(R.id.ivFlag);
		ivAddFavorite = (ImageView) findViewById(R.id.ivAddFavorite);
		ivContactSeller = (ImageView) findViewById(R.id.ivContactSeller);
		ivReward = (ImageView) findViewById(R.id.ivReward);
		ivSellerAndMessage = (ImageView) findViewById(R.id.ivSellerAndMessage);
		ivGone = (ImageView) findViewById(R.id.ivGone);
		ivEditItem = (ImageView) findViewById(R.id.ivEdit);
		ivItemGone = (ImageView) findViewById(R.id.ivItemGone);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				screenWidth, screenWidth);
		params.setMargins(0,
				(int) getResources().getDimension(R.dimen.header_height) / 2,
				0, 0);
		pagerImages.setLayoutParams(params);
		viewTransparent.setLayoutParams(params);

		RelativeLayout.LayoutParams decorParams = (RelativeLayout.LayoutParams) rlDecor
				.getLayoutParams();
		decorParams.height = screenWidth
				- (int) (getResources().getDimension(R.dimen.header_height) / 2);
		rlDecor.setLayoutParams(decorParams);

		// tvDescription.setMinHeight((int) screenWidth * 10 / 44);
		RelativeLayout.LayoutParams svDescriptionParams = new RelativeLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) screenWidth * 10 / 44);
		// svDescriptionParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		svDescriptionParams.addRule(RelativeLayout.BELOW, R.id.rlTop);
		svDescription.setLayoutParams(svDescriptionParams);

		if (svMain != null) {
			svDescription.setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View __v, MotionEvent __event) {
					if (__event.getAction() == MotionEvent.ACTION_DOWN) {
						// Disallow the touch request for parent scroll on touch
						// of
						// child view
						requestDisallowParentInterceptTouchEvent(__v, true);
					} else if (__event.getAction() == MotionEvent.ACTION_UP
							|| __event.getAction() == MotionEvent.ACTION_CANCEL) {
						// Re-allows parent events
						requestDisallowParentInterceptTouchEvent(__v, false);
					}
					return false;
				}
			});
		}

		RelativeLayout.LayoutParams lvThumbParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT, (int) getResources()
						.getDimension(R.dimen.button_size_large));
		lvThumbParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		lvThumbParams.setMargins(
				(int) getResources()
						.getDimension(R.dimen.margin_padding_normal), 0, 0,
				(int) getResources()
						.getDimension(R.dimen.margin_padding_normal));
		lvThumb.setLayoutParams(lvThumbParams);

		RelativeLayout.LayoutParams relistParams = (RelativeLayout.LayoutParams) rlRelist
				.getLayoutParams();
		relistParams.height = (int) screenWidth * 3 / 10;
		relistParams.addRule(RelativeLayout.CENTER_IN_PARENT);
		rlRelist.setLayoutParams(relistParams);

		llPremium = (LinearLayout) findViewById(R.id.llPremium);
		llAlertFlagItem = (LinearLayout) findViewById(R.id.llAlertFlagItem);
		llAlertShareItem = (LinearLayout) findViewById(R.id.llAlertShareItem);
		llAlertFavoriteItem = (LinearLayout) findViewById(R.id.llAlertFavoriteItem);
		llAlertContactSeller = (LinearLayout) findViewById(R.id.llAlertContactSeller);
		llCloseContactSeller = (LinearLayout) findViewById(R.id.llCloseContactSeller);
		llAlertSellerProfile = (LinearLayout) findViewById(R.id.llAlertSellerProfile);

		arrAlertLayout.add(llAlertShareItem);
		arrAlertLayout.add(llAlertFavoriteItem);
		arrAlertLayout.add(llAlertFlagItem);
		arrAlertLayout.add(llAlertContactSeller);
		arrAlertLayout.add(llAlertSellerProfile);

	}

	private void requestDisallowParentInterceptTouchEvent(View __v,
			Boolean __disallowIntercept) {
		while (__v.getParent() != null && __v.getParent() instanceof View) {
			if (__v.getParent() instanceof ScrollView) {
				__v.getParent().requestDisallowInterceptTouchEvent(
						__disallowIntercept);
			}
			__v = (View) __v.getParent();
		}
	}

	private void initData() {

		this.listAq = new AQuery(self);

		arrItemImages = new ArrayList<ImageObj>();
		adapterImages = new ItemImagesPagerAdapter(self, arrItemImages,
				onImageClickListener);
		pagerImages.setAdapter(adapterImages);

		arrThumbImages = new ArrayList<ImageObj>();
		imageThumbAdapter = new ImageObjAdapter(self, arrThumbImages);
		lvThumb.setAdapter(imageThumbAdapter);

		if (displayAction.equalsIgnoreCase(GlobalValue.KEY_ACTION_FROM_EMAIL)) {
			Log.i("FROM_EAMIL", "FROM_EMAIL");
			loadingItem = ParserUtility.parseItem(bundle
					.getString(GlobalValue.KEY_ITEM_ID));
			arrItems = new ArrayList<Item>();
			arrItems.add(loadingItem);
			fillDetailsLayout();
			disableBtnNextPrev();
			// loadItemDetails(bundle.getString(GlobalValue.KEY_ITEM_ID));
		}

		else {
			if (arrItems == null) {

				bundle = getIntent().getExtras();

				if (bundle != null) {
					if (bundle.containsKey("arrItem")) {
						this.arrItems = bundle
								.getParcelableArrayList("arrItem");
					}
					if (this.currentPosition == -1) {
						if (bundle.containsKey("currentPosition")) {
							this.currentPosition = bundle
									.getInt("currentPosition");
						}
						loadingItem = arrItems.get(currentPosition);
						updateButtonsNextPrev();
					}

				} else {
					this.arrItems = GlobalValue.arrItems;
					this.currentPosition = GlobalValue.currentItemPosition;
					loadingItem = arrItems.get(currentPosition);
					updateButtonsNextPrev();
				}
			} else {
				loadingItem = arrItems.get(currentPosition);
				updateButtonsNextPrev();
			}
		}

	}

	private void initControl() {
		btnNext.setOnClickListener(this);
		btnPrev.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnShare.setOnClickListener(this);
		btnContactSeller.setOnClickListener(this);
		btnConfirmReport.setOnClickListener(this);
		btnCancelReport.setOnClickListener(this);
		btnEditItem.setOnClickListener(this);
		btnItemGone.setOnClickListener(this);
		btnRelist.setOnClickListener(this);
		btnEdit.setOnClickListener(this);
		btnDelete.setOnClickListener(this);

		tvCategory.setOnClickListener(this);
		tvLocation.setOnClickListener(this);
		tvSeller.setOnClickListener(this);
		tvDescription.setOnClickListener(this);

		ivAddFavorite.setOnClickListener(this);
		ivFlag.setOnClickListener(this);
		getParentView(ivFlag).setOnClickListener(this);

		svDescription.setOnClickListener(this);

		rlContactSeller.setOnClickListener(this);

		lvThumb.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				pagerImages.setCurrentItem(position);
			}

		});

		llMain.setOnClickListener(this);
		llAlertFlagItem.setOnClickListener(this);
		llAlertFavoriteItem.setOnClickListener(this);
		llAlertShareItem.setOnClickListener(this);
		llAlertContactSeller.setOnClickListener(this);
		llAlertSellerProfile.setOnClickListener(this);
		llShareOptions.setOnClickListener(this);
		llFullGallery.setOnClickListener(this);
		llEditOptions.setOnClickListener(this);

		rlHeader.setOnClickListener(this);
		rlFullGallery1.setOnClickListener(this);
		rlFullGallery2.setOnClickListener(this);
		findViewById(R.id.rlMain).setOnClickListener(this);
		llCloseContactSeller.setOnClickListener(this);

		// findViewById(R.id.tvContactLogin).setOnClickListener(new on);
		// findViewById(R.id.tvContactSignup).setOnClickListener(this);

	}

	private void loadItemDetails(String itemID) {

		if (loadingItem != null) {
			Drawable pagerBg = new BitmapDrawable(aq.getCachedImage(loadingItem
					.getImage()));
			pagerImages.setBackgroundDrawable(pagerBg);

			arrItemImages.clear();
			arrItemImages.add(new ImageObj(loadingItem.getImage()));
			adapterImages.notifyDataSetChanged();
		}

		ProductModelManager
				.getItemDetails(
						self,
						itemID,
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG),
						screenWidth, screenWidth, true,
						new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								Log.e("item details :", "json : " + json);
								loadingItem = ParserUtility.parseItem(json);
								fillDetailsLayout();
							}

							@Override
							public void onError(int statusCode, String json) {
								showMessageDialog(
										getString(R.string.message_alert_invalid_item),
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
								showMessageDialog(
										getString(R.string.message_alert_invalid_item),
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

	private void fillDetailsLayout() {
		try {
			GlobalValue.currentItem = loadingItem;
			itemMessage = 0;

			tvName.setText(loadingItem.getTitle());

			arrItemImages.clear();
			adapterImages.notifyDataSetChanged();

			if (!displayAction
					.equals(GlobalValue.KEY_ACTION_ITEM_DETAIL_SINGLE_ITEM)) {
				Drawable pagerBg = new BitmapDrawable(
						aq.getCachedImage(loadingItem.getImage()));
				pagerImages.setBackgroundDrawable(pagerBg);
			}

			arrThumbImages.clear();

			for (int i = 0; i < loadingItem.getArrImages().size(); i++) {
				ImageObj newObj = new ImageObj(
						ImageUtil.convertGalleryImageSize(loadingItem
								.getArrImages().get(i).getImageUrl(),
								IMAGE_SIZE));
				arrThumbImages.add(newObj);
			}
			// refresh horizontal image thumb listview
			imageThumbAdapter.notifyDataSetChanged();

			imageThumbAdapter
					.setOnFirstItemLoadedListener(new IOnFirstItemLoaded() {

						@Override
						public void onLoaded() {
							arrItemImages.clear();
							arrItemImages.addAll(arrThumbImages);
							adapterImages.notifyDataSetChanged();
							pagerImages.setCurrentItem(0);

						}
					});

			Log.i("IS_FAVORITE", loadingItem.isFavourited() + "");
			if (loadingItem.isFavourited()) {
				// llAddToFavorite.setVisibility(View.VISIBLE);
				ivAddFavorite
						.setBackgroundResource(R.drawable.btn_already_favorite);
			} else {
				llAddToFavorite.setVisibility(View.INVISIBLE);
				ivAddFavorite
						.setBackgroundResource(R.drawable.btn_add_favorite_);
			}

			if (loadingItem.isFlagged()) {
				ivFlag.setBackgroundResource(R.drawable.icon_flag_red);
			} else {
				ivFlag.setBackgroundResource(R.drawable.icon_flag);
			}

			LocationObj locationObj = loadingItem.getLocation();
			tvLocation.setText(locationObj.getLocationAddress());

			double distance = loadingItem.getDistance();
			tvDistance.setText(distance >= 1000 ? Math.round(distance / 1000)
					+ "km away" : (int) distance + "m away");

			String currencySymbol = locationObj.getCurrencySymbol();
			String price = currencySymbol
					.equalsIgnoreCase(GlobalValue.CURRENCY_MAP.get("VND")) ? loadingItem
					.getCost() + currencySymbol
					: currencySymbol + loadingItem.getCost();
			tvPrice.setText(loadingItem.getCost() != 0 ? price : "Free");
			tvCategory.setText(loadingItem.getCategory().getName());

			tvSeller.setTextColor(getResources().getColor(R.color.sign_up_teal));
			tvSeller.setText(loadingItem.getUserName());
			tvDescription.setText(loadingItem.getDescription());

			if (loadingItem.getType().equals(Item.TYPE_PREMIUM)) {
				llPremium.setVisibility(View.VISIBLE);

			} else {
				llPremium.setVisibility(View.INVISIBLE);
			}

			if (loadingItem.getState().equalsIgnoreCase(Item.STATE_GONE)) {
				llGone.setVisibility(View.VISIBLE);
				ivGone.setImageResource(R.drawable.image_item_gone);
				btnContactSeller
						.setBackgroundResource(R.drawable.btn_follow_seller);
				tvContactSeller.setText(getString(R.string.follow_seller));
				viewTransparent.setVisibility(View.VISIBLE);
				llPremium.setVisibility(View.GONE);
			} else {
				viewTransparent.setVisibility(View.GONE);
				if (loadingItem.getType().equalsIgnoreCase(Item.TYPE_FREE)) {
					llGone.setVisibility(View.VISIBLE);
					ivGone.setImageResource(R.drawable.image_item_free);
					btnContactSeller
							.setBackgroundResource(R.drawable.btn_contact_seller);
					tvContactSeller.setText(getString(R.string.contact_seller));
				} else if (loadingItem.getType().equalsIgnoreCase(
						Item.TYPE_QUICK)) {
					llGone.setVisibility(View.VISIBLE);
					ivGone.setImageResource(R.drawable.image_item_quick);
					btnContactSeller
							.setBackgroundResource(R.drawable.btn_contact_seller);
				} else {
					llGone.setVisibility(View.GONE);
					btnContactSeller
							.setBackgroundResource(R.drawable.btn_contact_seller);
				}
			}

			if (isLoggedIn()
					&& loadingItem.getUserID().equals(
							GlobalValue.myAccount.getId())) {

				ivFlag.setVisibility(View.INVISIBLE);
				llEdit.setVisibility(View.VISIBLE);
				rlContactSeller.setVisibility(View.GONE);
				ivReward.setVisibility(View.INVISIBLE);
				ivAddFavorite.setVisibility(View.INVISIBLE);

				itemMessage = dbHelper.getNewMessagesCountByItem(
						loadingItem.getId(), GlobalValue.myAccount.getId());
				if (itemMessage > 0) {
					ivSellerAndMessage
							.setImageResource(R.drawable.icon_message_has_message);
					tvSeller.setTextColor(getResources().getColor(R.color.red));
					tvSeller.setText("View " + itemMessage
							+ (itemMessage > 1 ? " messages" : " message"));
				} else {
					ivSellerAndMessage
							.setImageResource(R.drawable.icon_message_no_message);
					tvSeller.setTextColor(getResources().getColor(
							R.color.header_background));
					tvSeller.setText("No messages");
				}

				if (loadingItem.getState().equals(Item.STATE_GONE)) {
					myItemHasGone();

				} else {
					myItemActive();

				}

				Log.i("ITEM_STATE", loadingItem.getState());

				if (loadingItem.getState().equalsIgnoreCase(Item.STATE_EXPIRED)) {
					myItemHasExpired();
				} else {
					rlRelist.setBackgroundColor(getResources().getColor(
							R.color.transparent));
					llRelist.setVisibility(View.GONE);
				}

			} else {

				rlContactSeller.setVisibility(View.VISIBLE);
				llEdit.setVisibility(View.GONE);
				ivReward.setVisibility(View.VISIBLE);
				ivAddFavorite.setVisibility(View.VISIBLE);

				ivSellerAndMessage
						.setImageResource(R.drawable.icon_seller_details);

				if (loadingItem.getState().equalsIgnoreCase(Item.STATE_EXPIRED)) {
					currentItemHasExpired();
				} else {
					rlRelist.setBackgroundColor(getResources().getColor(
							R.color.transparent));
					llRelist.setVisibility(View.GONE);
				}

			}

			if (loadingItem.getRewardType().equalsIgnoreCase(
					Item.REWARD_NEW_MEMBER)) {
				LinearLayout.LayoutParams rewardParams = new LinearLayout.LayoutParams(
						(int) getResources().getDimension(
								R.dimen.button_size_xsmall),
						(int) getResources().getDimension(
								R.dimen.button_size_xsmall));
				rewardParams.setMargins(
						(int) getResources().getDimension(
								R.dimen.margin_padding_small), 0, 0, 0);
				ivReward.setLayoutParams(rewardParams);
			} else {
				LinearLayout.LayoutParams rewardParams = new LinearLayout.LayoutParams(
						(int) getResources().getDimension(
								R.dimen.button_size_small),
						(int) getResources().getDimension(
								R.dimen.button_size_small));
				rewardParams.setMargins(
						(int) getResources().getDimension(
								R.dimen.margin_padding_small), 0, 0, 0);
				ivReward.setLayoutParams(rewardParams);
			}

			if (loadingItem.getRewardType().equalsIgnoreCase(Item.REWARD_GOLD)) {
				ivReward.setImageResource(R.drawable.image_star_gold);
			} else if (loadingItem.getRewardType().equalsIgnoreCase(
					Item.REWARD_SILVER)) {
				ivReward.setImageResource(R.drawable.image_star_silver);

			} else if (loadingItem.getRewardType().equalsIgnoreCase(
					Item.REWARD_BRONZE)) {
				ivReward.setImageResource(R.drawable.image_star_bronze);
			} else if (loadingItem.getRewardType().equalsIgnoreCase(
					Item.REWARD_NEW_MEMBER)) {
				ivReward.setImageResource(R.drawable.image_new_member);
			}

			if (forceLoginAction != null) {
				forceLoginActionOptions();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void currentItemHasExpired() {
		rlRelist.setBackgroundColor(getResources().getColor(
				R.color.white_transparent_40));
		llRelist.setVisibility(View.VISIBLE);
		getParentView(btnRelist).setVisibility(View.GONE);
		getParentView(ivItemExpired).setVisibility(View.VISIBLE);
		llRelistSuccess.setVisibility(View.GONE);
	}

	private void myItemActive() {
		btnItemGone.setBackgroundColor(getResources().getColor(
				R.color.sign_up_teal));
		tvEditItem.setText(getString(R.string.edit_item));
		tvItemGone.setText(getString(R.string.item_gone));
		tvEditItem.setVisibility(View.VISIBLE);
		tvItemGone.setVisibility(View.VISIBLE);
		ivEditItem.setVisibility(View.VISIBLE);
		ivEditItem.setBackgroundResource(R.drawable.icon_pencil);
		ivItemGone.setVisibility(View.VISIBLE);
		ivItemGone.setBackgroundResource(R.drawable.icon_item_gone);
		btnEditItem.setBackgroundColor(getResources().getColor(
				R.color.sign_up_teal));
		btnItemGone.setBackgroundColor(getResources().getColor(
				R.color.sign_up_teal));
		// llGone.setVisibility(View.GONE);
		viewTransparent.setVisibility(View.GONE);
		btnEditItem.setOnClickListener(ItemDetailsActivity.this);
		btnItemGone.setOnClickListener(ItemDetailsActivity.this);

	}

	private void myItemHasGone() {
		btnItemGone.setBackgroundColor(getResources().getColor(R.color.red));
		ivItemGone.setVisibility(View.GONE);
		llPremium.setVisibility(View.INVISIBLE);
		ivEditItem.setVisibility(View.GONE);
		tvEditItem.setText(getString(R.string.relist_item));
	}

	private void myItemHasExpired() {
		rlRelist.setBackgroundColor(getResources().getColor(
				R.color.white_transparent_40));
		llRelist.setVisibility(View.VISIBLE);
		getParentView(btnRelist).setVisibility(View.VISIBLE);
		getParentView(ivItemExpired).setVisibility(View.VISIBLE);
		llRelistSuccess.setVisibility(View.GONE);
		btnEditItem.setBackgroundColor(getResources().getColor(
				R.color.btn_edit_item_disable));
		btnItemGone.setBackgroundColor(getResources().getColor(
				R.color.btn_edit_item_disable));
		ivEditItem.setVisibility(View.VISIBLE);
		ivItemGone.setVisibility(View.VISIBLE);
		ivEditItem.setBackgroundResource(R.drawable.icon_pencil_disable);
		ivItemGone.setBackgroundResource(R.drawable.icon_item_gone_disable);
		btnEditItem.setOnClickListener(null);
		btnItemGone.setOnClickListener(null);
	}

	private void forceLoginActionOptions() {
		if (forceLoginAction
				.equals(GlobalValue.KEY_FORCED_LOGIN_ACTION_FAVORITE)) {
			addFavorite();
		} else if (forceLoginAction
				.equals(GlobalValue.KEY_FORCED_LOGIN_ACTION_CONTACT_SELLER)) {
			contactSeller();
			forceLoginAction = "";
		} else if (forceLoginAction
				.equals(GlobalValue.KEY_FORCED_LOGIN_ACTION_FLAG_ITEM)) {
			flagItem();

		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_SHARE)) {
			llShareOptions.setVisibility(View.VISIBLE);
			llShare.setBackgroundColor(getResources().getColor(
					R.color.sign_up_teal));
			forceLoginAction = "";
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_FOLLOW_SELLER)) {
			followUser(loadingItem.getUserID());
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_SELLER_PROFILE)) {
			Bundle bundle = new Bundle();
			bundle.putString(GlobalValue.KEY_SELLER_ID, loadingItem.getUserID());
			gotoActivity(self, SellerProfileActivity.class, bundle);
			forceLoginAction = "";
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if (HomeActivity.isHomeAlive) {
			super.onBackPressed();
		} else {
			gotoActivity(self, HomeActivity.class,
					Intent.FLAG_ACTIVITY_NEW_TASK);
			finish();
		}
	}

	@Override
	public void onClick(View v) {

		closeAlertLayout(v);

		if (v == btnBack) {
			if (HomeActivity.isHomeAlive) {
				super.onBackPressed();
			} else {
				gotoActivity(self, HomeActivity.class,
						Intent.FLAG_ACTIVITY_NEW_TASK);
				finish();
			}

		}
		if (v == btnNext) {
			onBtnNextClick();
		}

		if (v == btnPrev) {
			onBtnPrevClick();
		}
		if (v == btnShare) {
			if (!isLoggedIn()) {
				showLayoutAlert(llAlertShareItem, btnShare);
			} else {
				llShare.setBackgroundColor(getResources().getColor(
						R.color.sign_up_teal));
				llShareOptions.setVisibility(View.VISIBLE);
			}
		}

		if (v == ivAddFavorite) {
			if (!isLoggedIn()) {

				// llAlertFavoriteItem.setVisibility(View.VISIBLE);
				showLayoutAlert(llAlertFavoriteItem, ivAddFavorite);
			} else {
				addFavorite();
			}
		}

		if (v == ivFlag || v == getParentView(ivFlag)) {
			if (isLoggedIn()) {
				if (!loadingItem.isFlagged()) {
					llReportInapproriate.setVisibility(View.VISIBLE);
					llReportOption.setVisibility(View.VISIBLE);
				}
			} else {
				// llAlertFlagItem.setVisibility(View.VISIBLE);
				showLayoutAlert(llAlertContactSeller, ivFlag);
			}
		}

		if (v == btnConfirmReport) {
			llReportOption.setVisibility(View.VISIBLE);
			new CustomDialog(self,
					getString(R.string.message_confirm_flag_item), "",
					getString(R.string.text_btn_flag),
					getString(R.string.text_btn_cancel),
					new OnCustomDialogClickListener() {

						@Override
						public void onYes() {
							llReportOption.setVisibility(View.INVISIBLE);

							flagItem();
						}

						@Override
						public void onNo() {
							llReportInapproriate.setVisibility(View.GONE);
						}

						@Override
						public void onNeutral() {
							// TODO Auto-generated method stub

						}
					}).show();
		}

		if (v == btnCancelReport) {
			llReportInapproriate.setVisibility(View.GONE);

		}

		if (v == tvCategory) {
			Bundle bundle = new Bundle();
			bundle.putString(GlobalValue.KEY_ACTION,
					GlobalValue.KEY_ACTION_SEARCH);
			bundle.putString(GlobalValue.KEY_CATEGORY_ID, loadingItem
					.getCategory().getId());
			bundle.putString(GlobalValue.KEY_CATEGORY_NAME, loadingItem
					.getCategory().getName());
			gotoActivity(self, HomeActivity.class, bundle,
					Intent.FLAG_ACTIVITY_CLEAR_TOP);
		}

		if (v == tvLocation) {
			Log.e("LOCATION", loadingItem.getLocation().getCountryCode() + "--"
					+ loadingItem.getLocation().getLatitude() + "--"
					+ loadingItem.getLocation().getLongitude());
			GalleryFragment.searchLocationObj = loadingItem.getLocation();
			Bundle bundle = new Bundle();
			bundle.putString(GlobalValue.KEY_ACTION,
					GlobalValue.KEY_ACTION_SHOW_BY_LOCATION);
			bundle.putString(GlobalValue.KEY_LOCATION_OBJECT, loadingItem
					.getLocation().toString());
			gotoActivity(self, HomeActivity.class, bundle,
					Intent.FLAG_ACTIVITY_CLEAR_TOP);

		}

		if (v == btnContactSeller) {
			contactSeller();
		}

		if (v == llAlertFlagItem) {
			llAlertFlagItem.setVisibility(View.GONE);

		}
		if (v == llAlertShareItem) {
			llAlertShareItem.setVisibility(View.GONE);
			llShare.setBackgroundColor(getResources().getColor(
					R.color.transparent));
		}
		if (v == llAlertFavoriteItem) {
			llAlertFavoriteItem.setVisibility(View.GONE);
			ivAddFavorite.setBackgroundResource(R.drawable.btn_add_favorite_);
		}
		if (v == llAlertContactSeller) {
			llAlertContactSeller.setVisibility(View.GONE);

		}

		if (v == llShareOptions) {
			llShareOptions.setVisibility(View.GONE);
			llShare.setBackgroundColor(getResources().getColor(
					R.color.transparent));
		}
		if (v == llAlertSellerProfile) {
			llAlertSellerProfile.setVisibility(View.GONE);
			ivContactSeller.setBackgroundColor(getResources().getColor(
					R.color.transparent));
			llCloseContactSeller.setBackgroundColor(getResources().getColor(
					R.color.transparent));
		}

		if (v == llEditOptions) {
			llEditOptions.setVisibility(View.GONE);
		}

		if (v == btnEditItem) {
			if (!loadingItem.getState().equalsIgnoreCase(Item.STATE_GONE)) {
				// GlobalValue.currentItemPosition = currentPosition;
				// Bundle bundle = new Bundle();
				// bundle.putString(GlobalValue.KEY_ACTION,
				// GlobalValue.KEY_ACTION_EDIT_ITEM);
				// gotoActivity(self, AddAListingActivity.class, bundle);
				llEditOptions.setVisibility(View.VISIBLE);
			} else {
				processRelistGoneItem();
			}
		}

		if (v == btnEdit) {
			GlobalValue.currentItemPosition = currentPosition;
			Bundle bundle = new Bundle();
			bundle.putString(GlobalValue.KEY_ACTION,
					GlobalValue.KEY_ACTION_EDIT_ITEM);
			gotoActivity(self, AddAListingActivity.class, bundle);
			llEditOptions.setVisibility(View.GONE);
		}

		if (v == btnDelete) {
			llEditOptions.setVisibility(View.GONE);
			new CustomDialog(self, getString(R.string.message_confirm_delete),
					"", getString(R.string.yes), getString(R.string.no),
					new OnCustomDialogClickListener() {

						@Override
						public void onYes() {

							deleteItem();
						}

						@Override
						public void onNo() {
							// llReportInapproriate.setVisibility(View.GONE);
						}

						@Override
						public void onNeutral() {
							// TODO Auto-generated method stub

						}
					}).show();
		}

		if (v == btnItemGone) {
			if (!loadingItem.getState().equals(Item.STATE_GONE)) {
				showMarkGoneDialog();
			}
		}

		if (v == btnRelist) {
			// relistItem();
			processRelist();
		}

		if (v == tvSeller) {

			if (!isLoggedIn()) {

				// llAlertSellerProfile.setVisibility(View.VISIBLE);
				showLayoutAlert(llAlertContactSeller, tvSeller);
			} else {
				if (loadingItem.getUserID().equals(
						GlobalValue.myAccount.getId())) {
					if (itemMessage > -1) {
						Bundle bundle = new Bundle();
						bundle.putString(GlobalValue.KEY_ACTION,
								GlobalValue.KEY_ACTION_SHOW_MESSAGES);
						bundle.putString(GlobalValue.KEY_ITEM_ID,
								loadingItem.getId());
						gotoActivity(self, HomeActivity.class, bundle,
								Intent.FLAG_ACTIVITY_CLEAR_TOP);
					}
				} else {
					Bundle bundle = new Bundle();
					bundle.putString(GlobalValue.KEY_SELLER_ID,
							loadingItem.getUserID());
					gotoActivity(self, SellerProfileActivity.class, bundle);
				}
			}
		}

		if (v == rlFullGallery1 || v == rlFullGallery2) {
			closeFullGallery();
		}

	}

	private void closeAlertLayout(View v) {
		if (llAlertShareItem.getVisibility() == View.VISIBLE) {
			closeLayoutAlert(llAlertShareItem);
		}
		if (llAlertFavoriteItem.getVisibility() == View.VISIBLE) {
			closeLayoutAlert(llAlertFavoriteItem);
		}
		if (llAlertContactSeller.getVisibility() == View.VISIBLE) {
			closeLayoutAlert(llAlertContactSeller);
		}
		if (llShareOptions.getVisibility() == View.VISIBLE) {
			closeLayoutAlert(llShareOptions);
		}
	}

	private void closeLayoutAlert(View v) {
		if (v == llAlertShareItem) {
			llAlertShareItem.setVisibility(View.GONE);
			llShare.setBackgroundColor(getResources().getColor(
					R.color.transparent));

		} else if (v == llAlertFavoriteItem) {
			llAlertFavoriteItem.setVisibility(View.GONE);
			ivAddFavorite.setBackgroundResource(R.drawable.btn_add_favorite_);
		} else if (v == llAlertContactSeller) {
			llAlertContactSeller.setVisibility(View.GONE);
			if (loadingItem.getState().equalsIgnoreCase(Item.STATE_GONE)) {
				btnContactSeller
						.setBackgroundResource(R.drawable.btn_follow_seller);
				tvContactSeller.setText(getString(R.string.follow_seller));

			} else {
				btnContactSeller
						.setBackgroundResource(R.drawable.btn_contact_seller);
				tvContactSeller.setText(getString(R.string.contact_seller));
			}
			ivContactSeller.setBackgroundColor(getResources().getColor(
					R.color.transparent));
			llCloseContactSeller.setBackgroundColor(getResources().getColor(
					R.color.transparent));
		} else if (v == llShareOptions) {
			llShareOptions.setVisibility(View.GONE);
			llShare.setBackgroundColor(getResources().getColor(
					R.color.transparent));
		}
	}

	private void onBtnPrevClick() {
		currentPosition--;
		if (currentPosition <= 0) {
			currentPosition = 0;
		}
		loadingItem = arrItems.get(currentPosition);
		updateButtonsNextPrev();
		// loadItemDetails();
		fillDetailsLayout();
	}

	private void onBtnNextClick() {
		if (currentPosition < arrItems.size() - 1
				&& aq.getCachedImage(arrItems.get(currentPosition + 1)
						.getImage()) == null) {
			DialogUtility.showProgressDialog(self);
			aq.id(ivFlag).image(arrItems.get(currentPosition + 1).getImage(),
					false, true, 0, 0, new BitmapAjaxCallback() {
						@Override
						protected void callback(String url, ImageView iv,
								Bitmap bm, AjaxStatus status) {
							if (bm != null) {
								DialogUtility.closeProgressDialog();
								onBtnNextClick();

							}
						}
					});
		} else {
			if (navigateFrom.equals(GlobalValue.KEY_ACTION_FROM_GALLERY)
					&& currentPosition == arrItems.size() - 1) {
				loadMoreItems();
			} else {
				currentPosition++;
				if (currentPosition >= arrItems.size()) {
					currentPosition = arrItems.size() - 1;
				}
				GlobalValue.currentItemPosition = currentPosition;
				updateButtonsNextPrev();
				loadingItem = arrItems.get(currentPosition);
				// loadItemDetails();
				fillDetailsLayout();
			}
		}
	}

	private void contactSeller() {
		if (!isLoggedIn()) {

			// llAlertContactSeller.setVisibility(View.VISIBLE);
			showLayoutAlert(llAlertContactSeller, btnContactSeller);
		} else {

			if (!loadingItem.getState().equals(Item.STATE_GONE)) {
				gotoActivity(self, ChatActivity.class,
						Intent.FLAG_ACTIVITY_NEW_TASK);
			} else {
				followUser(loadingItem.getUserID());
			}
		}
	}

	private void updateButtonsNextPrev() {
		btnPrev.setVisibility(View.VISIBLE);
		btnNext.setVisibility(View.VISIBLE);

		if (currentPosition >= arrItems.size() - 1) {
			if (navigateFrom.equals(GlobalValue.KEY_ACTION_FROM_GALLERY)
					&& GalleryFragment.hasMoreData) {
				btnNext.setVisibility(View.VISIBLE);
			} else {

				btnNext.setVisibility(View.INVISIBLE);
			}
		} else {
			btnNext.setVisibility(View.VISIBLE);
		}
		if (currentPosition <= 0) {
			btnPrev.setVisibility(View.INVISIBLE);

		} else {
			btnPrev.setVisibility(View.VISIBLE);
		}
	}

	private void addFavorite() {
		Log.d("ADD_FAVORITE", "ADD_FAVORITE");
		// ProductModelManager manager = new ProductModelManager(self);
		ProductModelManager.addFavourite(self, loadingItem.getId(), true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						forceLoginAction = "";
						String message = "";
						if (!loadingItem.isFavourited()) {
							message = getString(R.string.message_add_favorite_success);
							llAddToFavorite.setVisibility(View.VISIBLE);

							// Refresh
							if (navigateFrom
									.equalsIgnoreCase(GlobalValue.KEY_ACTION_FROM_GALLERY)) {
								GalleryFragment.justChangeFavGallery = true;
							}
							else if(navigateFrom.equalsIgnoreCase(GlobalValue.KEY_ACTION_FROM_FAVORITE)){
								FavoriteItemsFragment.justChangeFavorite = true;
							}

							llAddToFavorite.postDelayed(new Runnable() {

								@Override
								public void run() {
									llAddToFavorite
											.setVisibility(View.INVISIBLE);

								}
							}, 5000);
						} else {
							message = getString(R.string.message_remove_favorite_success);

							// Refresh
							if (navigateFrom
									.equalsIgnoreCase(GlobalValue.KEY_ACTION_FROM_GALLERY)) {
								GalleryFragment.justChangeFavGallery = true;
							}
							else if(navigateFrom.equalsIgnoreCase(GlobalValue.KEY_ACTION_FROM_FAVORITE)){
								FavoriteItemsFragment.justChangeFavorite = true;
							}
						}

						arrItems.get(currentPosition).setNeedToUpdate(true);

						if (!loadingItem.isFavourited()) {
							arrItems.get(currentPosition).setFavourited(true);
							ivAddFavorite
									.setBackgroundResource(R.drawable.btn_already_favorite);
						} else {
							arrItems.get(currentPosition).setFavourited(false);
							ivAddFavorite
									.setBackgroundResource(R.drawable.btn_add_favorite_);
						}
						// fillDetailsLayout();
					}

					@Override
					public void onError(int statusCode, String json) {
						forceLoginAction = "";
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
						forceLoginAction = "";
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

	private void flagItem() {
		ProductModelManager.addFlagItem(self, loadingItem.getId(), true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {

						forceLoginAction = "";

						String message = "";
						if (loadingItem.isFlagged()) {
							loadingItem.setFlagged(false);
							loadingItem.setFlagged(false);
							message = getString(R.string.message_unflag_success);

						} else {
							loadingItem.setFlagged(true);
							loadingItem.setFlagged(true);
							message = getString(R.string.message_flag_success);

						}
						fillDetailsLayout();

						showMessageDialog(
								getString(R.string.message_flag_success),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										llReportInapproriate
												.setVisibility(View.GONE);

									}
								});
					}

					@Override
					public void onError(int statusCode, String json) {
						forceLoginAction = "";
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
						forceLoginAction = "";
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

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		GlobalValue.arrItems = this.arrItems;
		GlobalValue.currentItemPosition = currentPosition;

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// Pass on the activity result to the helper for handling

		if (!mHelper.handleActivityResult(requestCode, resultCode, data)
				|| requestCode == 0) {
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	public void onSignupClick(View v) {
		// DialogUtility.showShortToast(self, "signup-click");
		gotoActivity(self, SignupActivity.class);
	}

	public void onLoginClick(View v) {
		// DialogUtility.showShortToast(self, "login-click");
		Bundle bundle = null;
		getParentView(getParentView(v)).setVisibility(View.GONE);
		switch (v.getId()) {
		case R.id.tvFavoriteLogin:
			bundle = new Bundle();
			bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
					GlobalValue.KEY_FORCED_LOGIN_ACTION_FAVORITE);
			GlobalValue.currentItemPosition = currentPosition;
			gotoActivity(self, LoginSelectionActivity.class, bundle);
			break;
		case R.id.tvContactLogin:
			// if
			// (!loadingItem.getUserID().equals(GlobalValue.myAccount.getId()))
			// {
			bundle = new Bundle();
			bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
					GlobalValue.KEY_FORCED_LOGIN_ACTION_CONTACT_SELLER);
			GlobalValue.currentItemPosition = currentPosition;
			gotoActivity(self, LoginSelectionActivity.class, bundle);
			// } else {
			// bundle = new Bundle();
			// bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
			// GlobalValue.KEY_FORCED_LOGIN_ACTION_FOLLOW_SELLER);
			// GlobalValue.currentItemPosition = currentPosition;
			// gotoActivity(self, LoginSelectionActivity.class, bundle);
			// }
			break;
		case R.id.tvFlagLogin:
			bundle = new Bundle();
			bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
					GlobalValue.KEY_FORCED_LOGIN_ACTION_FLAG_ITEM);
			GlobalValue.currentItemPosition = currentPosition;
			gotoActivity(self, LoginSelectionActivity.class, bundle);
			break;
		case R.id.tvShareLogin:
			bundle = new Bundle();
			bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
					GlobalValue.KEY_FORCED_LOGIN_ACTION_SHARE);
			GlobalValue.currentItemPosition = currentPosition;
			// llShareOptions.setVisibility(View.VISIBLE);
			gotoActivity(self, LoginSelectionActivity.class, bundle);
			break;
		case R.id.tvSellerLogin:
			bundle = new Bundle();
			bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
					GlobalValue.KEY_FORCED_LOGIN_ACTION_SELLER_PROFILE);
			GlobalValue.currentItemPosition = currentPosition;
			// llShareOptions.setVisibility(View.VISIBLE);
			gotoActivity(self, LoginSelectionActivity.class, bundle);
			break;

		default:
			break;
		}
		// gotoActivity(self, LoginSelectionActivity.class);
	}

	// public void onLoginFavoriteClick(View v) {
	// Bundle bundle = new Bundle();
	// bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
	// GlobalValue.KEY_FORCED_LOGIN_ACTION_FAVORITE);
	// GlobalValue.currentItemPosition = currentPosition;
	// gotoActivity(self, LoginSelectionActivity.class, bundle);
	// }

	public void onShareFacebook(View v) {
		super.onShareFacebook(v, loadingItem);
		llShare.setBackgroundColor(getResources().getColor(R.color.transparent));
		llShareOptions.setVisibility(View.GONE);

	}

	public void onShareGoogle(View v) {
		super.onShareGoogle(v, loadingItem);
		llShare.setBackgroundColor(getResources().getColor(R.color.transparent));
		llShareOptions.setVisibility(View.GONE);
	}

	public void onShareEmail(View v) {

		super.onShareEmail(v, loadingItem);
		llShare.setBackgroundColor(getResources().getColor(R.color.transparent));
		llShareOptions.setVisibility(View.GONE);
	}

	public void onShareMessage(View v) {

		super.onShareMessage(v, loadingItem);
		llShare.setBackgroundColor(getResources().getColor(R.color.transparent));
		llShareOptions.setVisibility(View.GONE);
	}

	private void showMarkGoneDialog() {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_choose_gone_where);
		dialog.getWindow().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.bg_dialog_categories));
		dialog.setCancelable(true);

		TextView tvTreasureTrash = (TextView) dialog
				.findViewById(R.id.tvTreasureTrash);
		TextView tvEbay = (TextView) dialog.findViewById(R.id.tvEbay);
		TextView tvOther = (TextView) dialog.findViewById(R.id.tvOther);

		tvTreasureTrash.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				markGoneItem(Item.GONE_TO_TREASURE_TRASH);
				dialog.dismiss();

			}
		});

		tvEbay.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				markGoneItem(Item.GONE_TO_EBAY);
				dialog.dismiss();

			}
		});

		tvOther.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				markGoneItem(Item.GONE_TO_OTHER);
				dialog.dismiss();

			}
		});

		dialog.show();
	}

	private void markGoneItem(String where) {
		ProductModelManager.markGonesItem(self, loadingItem.getId(), where,
				true, new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						loadingItem.setState(Item.STATE_GONE);

						// refresh yourlisting
						HomeActivity.isRefreshYourListings = true;

						fillDetailsLayout();
						// loadItemDetails();
						if (!pref
								.getBooleanValue(TreasureTrashSharedPreferences.PREF_TURN_OFF_RATE_REMIND))
							showRateDialog();

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

	private void showRateDialog() {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_rate_option);
		dialog.getWindow().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.bg_dialog_categories));
		dialog.setCancelable(true);

		TextView tvNoThanks = (TextView) dialog.findViewById(R.id.tvNoThanks);
		TextView tvRemindLater = (TextView) dialog
				.findViewById(R.id.tvRemindLater);
		TextView tvRate = (TextView) dialog.findViewById(R.id.tvRate);

		tvNoThanks.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				pref.putBooleanValue(
						TreasureTrashSharedPreferences.PREF_TURN_OFF_RATE_REMIND,
						true);
				dialog.dismiss();

			}
		});

		tvRemindLater.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();

			}
		});

		tvRate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
				String marketUrl = "market://details?id=" + getPackageName();
				self.startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse(marketUrl)));

			}
		});

		dialog.show();
	}

	private void processRelistGoneItem() {

		if (GlobalValue.myAccount.getNumberFreeSlotActive() > 0) {
			relistItem(Item.ITEM_KEY_SLOT_TYPE_DEFAULT);
		} else if (GlobalValue.myAccount.getNumberSingleSlotActive() > 0) {
			// Confirm relist to extra listing
			final Dialog dialog = new Dialog(self);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.dialog_confirm_relist);
			dialog.getWindow().setBackgroundDrawable(
					new ColorDrawable(android.graphics.Color.TRANSPARENT));

			RelativeLayout rltCancel = (RelativeLayout) dialog
					.findViewById(R.id.rlt_cancel);
			rltCancel.getLayoutParams().width = screenWidth;
			rltCancel.getLayoutParams().height = screenHeight;

			HelveticaLightTextView btnNegative = (HelveticaLightTextView) dialog
					.findViewById(R.id.btnNegative);
			btnNegative.setText(getResources().getString(R.string.cancel));
			btnNegative.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();
				}
			});

			HelveticaLightTextView btnPositive = (HelveticaLightTextView) dialog
					.findViewById(R.id.btnPositive);

			btnPositive.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					dialog.dismiss();

					relistItem(Item.ITEM_KEY_SLOT_TYPE_ADDITIONAL);
				}
			});

			dialog.show();
		} else {
			showDialogWhenSlotIsFull();
		}
	}

	private void processRelist() {
		String slotType = loadingItem.getSlotType();
		// check if is free item
		if (slotType.equals(Item.SLOT_TYPE_DEFAULT)) {
			if (GlobalValue.myAccount.getNumberFreeSlotActive() > 0) {
				relistItem(slotType);
			} else if (GlobalValue.myAccount.getNumberSingleSlotActive() > 0) {
				// Confirm relist to extra listing
				final Dialog dialog = new Dialog(self);
				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				dialog.setContentView(R.layout.dialog_confirm_relist);
				dialog.getWindow().setBackgroundDrawable(
						new ColorDrawable(android.graphics.Color.TRANSPARENT));

				RelativeLayout rltCancel = (RelativeLayout) dialog
						.findViewById(R.id.rlt_cancel);
				rltCancel.getLayoutParams().width = screenWidth;
				rltCancel.getLayoutParams().height = screenHeight;

				HelveticaLightTextView btnNegative = (HelveticaLightTextView) dialog
						.findViewById(R.id.btnNegative);
				btnNegative.setText(getResources().getString(R.string.cancel));
				btnNegative.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});

				HelveticaLightTextView btnPositive = (HelveticaLightTextView) dialog
						.findViewById(R.id.btnPositive);

				btnPositive.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						dialog.dismiss();

						relistItem(Item.ITEM_KEY_SLOT_TYPE_ADDITIONAL);
					}
				});

				dialog.show();
			} else {
				showDialogWhenSlotIsFull();
			}
		} else {
			relistItem(slotType);
		}
	}

	private void showDialogWhenSlotIsFull() {
		// Show dialog
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_when_slots_is_full);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		final HelveticaBoldTextView lbl5Slots = (HelveticaBoldTextView) dialog
				.findViewById(R.id.lbl_5_slots);
		final HelveticaBoldTextView lbl10Slots = (HelveticaBoldTextView) dialog
				.findViewById(R.id.lbl_10_slots);

		lbl5Slots.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// confirmExtraAPack(lbl5Slots.getText().toString());

				CURRENT_PURCHASE = Item.ITEM_5_SLOTS;
				purchaseItem(Item.KEY_5_SLOTS, "");

				dialog.dismiss();
			}
		});

		lbl10Slots.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// confirmExtraAPack(lbl10Slots.getText().toString());

				CURRENT_PURCHASE = Item.ITEM_10_SLOTS;
				purchaseItem(Item.KEY_10_SLOTS, "");

				dialog.dismiss();
			}
		});

		HelveticaLightTextView btnCancel = (HelveticaLightTextView) dialog
				.findViewById(R.id.btn_cancel_confirm);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void relistItem(String slotType) {
		ProductModelManager.relistItem(self, loadingItem.getId(), slotType,
				true, new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						getParentView(ivItemExpired).setVisibility(View.GONE);
						getParentView(btnRelist).setVisibility(View.GONE);
						llRelistSuccess.setVisibility(View.VISIBLE);
						rlRelist.setBackgroundColor(getResources().getColor(
								R.color.transparent));
						loadingItem = ParserUtility.parseItem(json);
						GlobalValue.arrItems.remove(currentPosition);
						GlobalValue.arrItems.add(currentPosition, loadingItem);

						DialogUtility.showProgressDialog(self);
						aq.id(ivFlag).image(loadingItem.getImage(), false,
								true, 0, 0, new BitmapAjaxCallback() {
									@Override
									protected void callback(String url,
											ImageView iv, Bitmap bm,
											AjaxStatus status) {
										if (bm != null) {
											DialogUtility.closeProgressDialog();
											processAfterRelist();

										}
									}
								});

					}

					@Override
					public void onError(int statusCode, String json) {
						showMessageDialog(
								getString(R.string.message_alert_general_error),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {

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

									}
								});

					}
				});
	}

	private void processAfterRelist() {
		myItemActive();
		fillDetailsLayout();

		HomeActivity.isRefreshYourListings = true;

		llRelistSuccess.setVisibility(View.VISIBLE);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// refresh yourlisting
				llRelistSuccess.setVisibility(View.GONE);

			}
		}, 3000);

	}

	private void deleteItem() {
		ProductModelManager.deleteItem(self, loadingItem.getId(), true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						showMessageDialog(
								getString(R.string.message_delete_item_success),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										Bundle bundle = new Bundle();
										bundle.putString(
												GlobalValue.KEY_ACTION,
												GlobalValue.KEY_ACTION_AFTER_DELETE);
										gotoActivity(self, HomeActivity.class,
												bundle,
												Intent.FLAG_ACTIVITY_CLEAR_TOP);
										// arrItems.remove(currentPosition);

										HomeActivity.isRefreshYourListings = true;
										// if (arrItems.size() > 0) {
										// onBtnPrevClick();
										//
										// } else {
										// finish();
										// }

									}
								});

					}

					@Override
					public void onError(int statusCode, String json) {
						showMessageDialog(
								getString(R.string.message_alert_general_error),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {

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

									}
								});

					}
				});
	}

	private IOnImageClick onImageClickListener = new IOnImageClick() {

		@Override
		public void onImageClick() {
			openFullGallery();

		}
	};

	private void openFullGallery() {

		closeAlertLayout(llFullGallery);
		pagerFullScreen.setAdapter(adapterImages);
		llFullGallery.setVisibility(View.VISIBLE);

	}

	private void closeFullGallery() {

		llFullGallery.setVisibility(View.GONE);
	}

	@Override
	public void onReceiveMessageSuccess(String message) {
		// TODO Auto-generated method stub
		super.onReceiveMessageSuccess(message);
		if (isLoggedIn()
				&& loadingItem.getUserID()
						.equals(GlobalValue.myAccount.getId())) {
			MessageItem item = ParserUtility.parseReceivedMessage(message,
					GlobalValue.myAccount.getId());
			if (item.getListingOwnerId().equals(GlobalValue.myAccount.getId())) {
				itemMessage++;
				if (itemMessage > 0) {
					ivSellerAndMessage
							.setImageResource(R.drawable.icon_message_has_message);
					tvSeller.setTextColor(getResources().getColor(R.color.red));
					tvSeller.setText("View " + itemMessage + " messages");
				} else {
					ivSellerAndMessage
							.setImageResource(R.drawable.icon_message_no_message);
					tvSeller.setTextColor(getResources().getColor(
							R.color.sign_up_teal));
					tvSeller.setText("No messages");
				}
			}
		}
	}

	private void showLayoutAlert(View view, View clickedView) {

		LinearLayout layout = (LinearLayout) view;
		for (View v : arrAlertLayout) {
			v.setVisibility(View.GONE);
		}
		layout.setVisibility(View.VISIBLE);

		if (view == llAlertShareItem) {
			llShare.setBackgroundColor(getResources().getColor(
					R.color.sign_up_teal));
		}

		else if (view == llAlertFavoriteItem) {
			ivAddFavorite
					.setBackgroundResource(R.drawable.btn_add_favorite_selected);
		}

		else if (view == llAlertContactSeller) {
			if (clickedView == btnContactSeller) {
				if (loadingItem.getState().equalsIgnoreCase(Item.STATE_GONE)) {
					btnContactSeller
							.setBackgroundResource(R.drawable.btn_follow_seller_disable);
					tvContactAlert
							.setText(getString(R.string.message_alert_follow_seller));
				} else {
					tvContactAlert
							.setText(getString(R.string.message_alert_contact_seller));
					btnContactSeller
							.setBackgroundResource(R.drawable.btn_contact_seller_disable);
				}
				ivContactSeller
						.setBackgroundResource(R.drawable.btn_close_contact_seller);
				llCloseContactSeller.setBackgroundColor(getResources()
						.getColor(R.color.sign_up_teal));
				llCloseContactSeller.setVisibility(View.VISIBLE);
			} else if (clickedView == tvSeller) {
				tvContactAlert
						.setText(getString(R.string.message_alert_seller_profile));
				btnContactSeller
						.setBackgroundResource(R.drawable.btn_contact_seller_disable);

				ivContactSeller
						.setBackgroundResource(R.drawable.btn_close_contact_seller);
				llCloseContactSeller.setBackgroundColor(getResources()
						.getColor(R.color.sign_up_teal));
				llCloseContactSeller.setVisibility(View.VISIBLE);
			} else if (clickedView == ivFlag) {
				tvContactAlert.setText(getString(R.string.message_alert_flag));
				btnContactSeller
						.setBackgroundResource(R.drawable.btn_contact_seller_disable);

				ivContactSeller
						.setBackgroundResource(R.drawable.btn_close_contact_seller);
				llCloseContactSeller.setBackgroundColor(getResources()
						.getColor(R.color.sign_up_teal));
				llCloseContactSeller.setVisibility(View.VISIBLE);
			}
		}

	}

	private void followUser(String userId) {
		Bundle bundle = new Bundle();
		bundle.putString(GlobalValue.KEY_SELLER_ID, loadingItem.getUserID());
		gotoActivity(self, SellerProfileActivity.class, bundle);
	}

	private void getAccountInfo() {
		// Set data
		AccountModelManager.getAccountInfo(self, false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						Log.e(TAG, "JSon Profile: " + json);

						pref.putStringValue(
								TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON,
								json);

						GlobalValue.myAccount = ParserUtility.parseAccount(
								json, pref);

						// Relist again
						processRelist();
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

	/*----- Methods for in-app billing -----*/
	private void initInAppPurchase() {
		// Create the helper, passing it our context and the public key to
		// verify signatures with
		Log.d(TAG, "Creating IAB helper.");
		mHelper = new IabHelper(this, Item.BASE64ENCODEDPUBLICKEY);

		// enable debug logging (for a production application, you should set
		// this to false).
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					Log.e(TAG, "Problem setting up in-app billing: " + result);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null)
					return;

				// IAB is fully set up. Now, let's get an inventory of stuff we
				// own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}

	private void purchaseItem(String skuId, String payload) {
		/*
		 * TODO: for security, generate your payload here for verification. See
		 * the comments on verifyDeveloperPayload() for more info. Since this is
		 * a SAMPLE, we just use an empty string, but on a production app you
		 * should carefully generate this.
		 */
		// mHelper.launchPurchaseFlow(this, skuId, IabHelper.ITEM_TYPE_INAPP,
		// RC_REQUEST, mPurchaseFinishedListener, payload);
		mHelper.launchPurchaseFlow(this, skuId, RC_REQUEST,
				mPurchaseFinishedListener, payload);
	}

	// Listener that's called when we finish querying the items and
	// subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");

			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			// TODO: IAP: need to change after receiving google account of
			// Treasure Trash
			/* temp */

			if (inventory.hasPurchase(Item.KEY_5_SLOTS)) {
				Log.e(TAG, "Query inventory finished. " + result);
				mHelper.consumeAsync(inventory.getPurchase(Item.KEY_5_SLOTS),
						null);
				return;
			} else if (inventory.hasPurchase(Item.KEY_10_SLOTS)) {
				Log.e(TAG, "Query inventory finished. " + result);
				mHelper.consumeAsync(inventory.getPurchase(Item.KEY_10_SLOTS),
						null);
				return;
			}
			/* end temp */
			if (mHelper == null)
				return;

			// Is it a failure?
			if (result.isFailure()) {
				Log.e(TAG, "Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// Do we have the premium upgrade?
			Purchase buy5 = inventory.getPurchase(Item.KEY_5_SLOTS);
			if (buy5 != null && verifyDeveloperPayload(buy5)) {
				Log.d(TAG, "We have gas. Consuming it.");
				mHelper.consumeAsync(buy5, mConsumeFinishedListener);
				return;
			}

			// Purchase buy10 = inventory.getPurchase(BUY_10_SLOTS);
			// if (buy10 != null && verifyDeveloperPayload(buy10)) {
			// Log.d(TAG, "We have gas. Consuming it.");
			// mHelper.consumeAsync(inventory.getPurchase(BUY_10_SLOTS),
			// mConsumeFinishedListener);
			// return;
			// }

			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 * 
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 * 
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */

		return true;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);

			// if we were disposed of in the meantime, quit.
			// TODO: IAP: need to change after receiving google account of
			// Treasure Trash
			/* temp */

			if (purchase != null) {
				Log.e(TAG, "Query inventory finished. " + result);
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				return;
			}

			/* end temp */
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				Log.e(TAG, "Error purchasing: " + result);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				Log.e(TAG,
						"Error purchasing. Authenticity verification failed.");
				return;
			}

			Log.d(TAG, "Purchase successful.");

			// Consuming item
			if (purchase.getSku().equals(Item.KEY_5_SLOTS)
					|| purchase.getSku().equals(Item.KEY_10_SLOTS)) {
				// bought 1/4 tank of gas. So consume it.
				Log.d(TAG, "Purchase is gas. Starting gas consumption.");
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			}
		}
	};

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase
					+ ", result: " + result);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			// We know this is the "gas" sku because it's the only one we
			// consume,
			// so we don't check which sku was consumed. If you have more than
			// one
			// sku, you probably should check...
			if (result.isSuccess()) {
				// successfully consumed, so we apply the effects of the item in
				// our
				// game world's logic, which in our case means filling the gas
				// tank a bit
				Log.d(TAG, "Consumption successful. Provisioning.");
				if (CURRENT_PURCHASE.equals(Item.ITEM_5_SLOTS)) {
					sendPurchaseToServer(Item.ITEM_5_SLOTS, "DemoPurchase", ""
							+ Calendar.getInstance().getTimeInMillis());
					Log.e("HomeActivity", "Bought 5 slots");
				} else {
					sendPurchaseToServer(Item.ITEM_10_SLOTS, "DemoPurchase", ""
							+ Calendar.getInstance().getTimeInMillis());
					Log.e("HomeActivity", "Bought 10 slots");
				}
			} else {
				Log.e(TAG, "Error while consuming: " + result);
			}
			Log.d(TAG, "End consumption flow.");
		}
	};

	private void sendPurchaseToServer(String productName, String receipt,
			final String transactionId) {
		ProductModelManager.postAppProduct(self, productName, receipt,
				transactionId, true, new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {

						getAccountInfo();

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

	public void loadMoreItems() {
		// TODO Auto-generated method stub
		if (GalleryFragment.hasMoreData) {
			List<Item> listItem = null;
			SearchObj obj = prepareSearchObject();
			ProductModelManager.searchItem(self, obj, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {

							Log.e("aaaaaaaaa", "aaaaaaaaaa :" + json);
							List<Item> listItem = ParserUtility
									.parseListItem(json);

							if (listItem.size() > 0) {
								HomeActivity.isRefreshGallery = true;
								arrItems.addAll(listItem);
								onBtnNextClick();
							} else {
								GalleryFragment.hasMoreData = false;
								DialogUtility.showShortToast(self,
										"No more data to show");
								btnNext.setVisibility(View.INVISIBLE);
							}

						}

						@Override
						public void onError() {
							DialogUtility.showShortToast(self, self
									.getString(R.string.message_server_error));
						}

						@Override
						public void onError(int statusCode, String json) {
							DialogUtility.showShortToast(self, self
									.getString(R.string.message_server_error));

						}
					});
		} else {
			DialogUtility.showShortToast(self, "No more data to show");

		}
	}

	private SearchObj prepareSearchObject() {

		GalleryFragment.currentPage++;
		SearchObj obj = new SearchObj();
		obj.setCurrentPage(GalleryFragment.currentPage);
		obj.setLatitude(pref
				.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT));
		obj.setLongitide(pref
				.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG));
		// obj.setLatitude(((HomeActivity) self).gpsTracker.getLatitude());
		// obj.setLongitide(((HomeActivity) self).gpsTracker.getLongitude());
		// obj.setLatitude(21.039225);
		// obj.setLongitide(105.828852);
		obj.setPageSize(GlobalValue.GALLERY_PAGE_SIZE);
		obj.setImageWidth(IMAGE_SMALL_SIZE);
		obj.setImageHeight(IMAGE_SMALL_SIZE);

		if (GalleryFragment.action.equals(GlobalValue.KEY_ACTION_SEARCH)) {
			obj.setCategoryId(GalleryFragment.categoryId);
			obj.setKeyWord(GalleryFragment.keyword);
			obj.setSearchField(GalleryFragment.searchField);
			obj.setItemType(GalleryFragment.itemSearchType);
		}

		return obj;
	}

}
