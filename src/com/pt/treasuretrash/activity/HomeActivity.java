package com.pt.treasuretrash.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Stack;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.auth.FacebookHandle;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.plus.Plus;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.adapter.BlockedUserAdapter;
import com.pt.treasuretrash.adapter.BlockedUserAdapter.IOnBlockUnblock;
import com.pt.treasuretrash.adapter.ExpandableCategoriesAdapter;
import com.pt.treasuretrash.adapter.ExpandableCategoriesAdapter.IExpandableListView;
import com.pt.treasuretrash.adapter.LocationSearchAdapter;
import com.pt.treasuretrash.adapter.YourFreeListingItemAdapter;
import com.pt.treasuretrash.adapter.YourListingExpiredItemAdapter;
import com.pt.treasuretrash.adapter.YourListingExtraItemAdapter;
import com.pt.treasuretrash.adapter.YourListingItemAdapter;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseFragment;
import com.pt.treasuretrash.base.TreasureTrashBaseShareActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity.DialogListener;
import com.pt.treasuretrash.config.FacebookConstants;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.fragment.GalleryFragment;
import com.pt.treasuretrash.fragment.MessageListFragment;
import com.pt.treasuretrash.inapppurchase.IabHelper;
import com.pt.treasuretrash.inapppurchase.IabResult;
import com.pt.treasuretrash.inapppurchase.Inventory;
import com.pt.treasuretrash.inapppurchase.Purchase;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.modelmanager.ProductModelManager;
import com.pt.treasuretrash.network.AsyncHttpBase;
import com.pt.treasuretrash.network.StatusBackend;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.Category;
import com.pt.treasuretrash.object.CategoryGroup;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.object.ServerAlert;
import com.pt.treasuretrash.object.SocialAccount;
import com.pt.treasuretrash.pulltorefresh.library.PullToRefreshBase;
import com.pt.treasuretrash.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.pt.treasuretrash.service.PubnubService;
import com.pt.treasuretrash.slidingmenu.SlidingMenu;
import com.pt.treasuretrash.slidingmenu.SlidingMenu.IOnMenuToggle;
import com.pt.treasuretrash.utility.AndroidBug5497Workaround;
import com.pt.treasuretrash.utility.AppUtil;
import com.pt.treasuretrash.utility.BadgeUtils;
import com.pt.treasuretrash.utility.DialogUtility;
import com.pt.treasuretrash.utility.ExceptionHandler;
import com.pt.treasuretrash.utility.ImageUtil;
import com.pt.treasuretrash.utility.SmallUtility;
import com.pt.treasuretrash.utility.StringUtility;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.CustomDialog;
import com.pt.treasuretrash.widget.CustomDialog.OnCustomDialogClickListener;
import com.pt.treasuretrash.widget.HelveticaBoldTextView;
import com.pt.treasuretrash.widget.HelveticaLightTextView;
import com.pt.treasuretrash.widget.ProgressHUD;
import com.pt.treasuretrash.widget.SlidingLayer;
import com.pt.treasuretrash.widget.SlidingLayer.OnInteractListener;
import com.splunk.mint.Mint;

import eu.janmuller.android.simplecropimage.CropImage;

public class HomeActivity extends TreasureTrashBaseShareActivity implements
		OnClickListener {

	// private static final String TAG = "HomeActivity";

	private static final int PICK_FROM_FILE = 3;
	private static final int REQUEST_CODE_CROP_IMAGE = 1;
	private static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";

	private Stack<Integer> stackFragment = new Stack<Integer>();

	private boolean isGetHistory = false;
	public int currentState;
	public final int STATE_NORMAL = 1;
	public final int STATE_LOCATION_OFF = 0;

	public ImageView ivDiamond, ivRightMenu, ivCloseLeftMenu;
	private RelativeLayout rlCoachMark, rlHeader;
	private AutoBgButton btnFindLocalTreasure;
	private LinearLayout llGallery, llFavorite, llFeed, llAddListing, llBottom,
			llAlert, llWrapperAlertHome, llActualCoachMark, llAlertCategory;
	private TextView tvAlertMessage;
	private List<LinearLayout> listBottomButtons;
	private android.support.v4.app.FragmentManager fm;
	public List<Fragment> arrFragments;
	private Fragment defaultFragment;
	private Account temAccount;
	private File currentFile;
	private Uri fileUri;
	public static Bitmap selectImage;
	private Calendar birthdayCalendar;
	private boolean isShowConfirmPassword = false;
	private boolean isLoadingyourListing = false, isShowSlider = false;
	private boolean isLoadLeftMenuFirstTime = true;

	// for purchase
	// static final String KEY_5_SLOTS = "android.test.purchased";
	// static final String KEY_10_SLOTS = "android.test.purchased";
	// static final String ITEM_5_SLOTS = "tta5slots";
	// static final String ITEM_10_SLOTS = "tta10slots";
	static String CURRENT_PURCHASE = "";
	private static final int RC_REQUEST = 10001;
	private IabHelper mHelper;

	private boolean isClickOnAddAListing = false;
	// private boolean isClickOnRelist = false;
	// private int relistPosition = 0;

	public static boolean isHomeAlive = false;
	public static boolean isHomeVisible = false;
	private int fragmentSelected = 0;
	private String forceLoginAction = "";
	private boolean isChangedSearchLocation = false;

	// share account
	FacebookHandle fhandler = null;
	HelveticaLightTextView lblFaceBookShare, lblGoogleShare;
	ImageView imgFacebookShare, imgGoogleShare;
	private ArrayList<SocialAccount> arrSocialAccounts;

	// Categories Menu
	private List<CategoryGroup> arrCategoryGroups;
	private ExpandableCategoriesAdapter adapterCategory;
	private int rightMenuState = CategoryGroup.STATE_NORMAL;

	private Handler handler;

	// rightMenu
	private TextView tvEditCategories, tvBrowse, tvLocation, tvChange,
			tvLocationRight, tvDoneRight, tvCurrentLocationRight;
	// private LocationObj currentChooseLocation;
	private EditText etSearch, etRightLocation;
	private AutoBgButton btnSearch;
	private ImageView ivSearchLocationRight, ivFreeListings,
			ivPremimumListings, ivQuickSales;
	private SlidingLayer sllChangeLocation;
	private GoogleMap rightMap;
	private ListView lvLocationRight;
	private String currentLocation = null;
	private LocationObj tempRightLocObj = null;

	private final int BOTTOM_NORMAL_BACKGROUND[] = {
			R.drawable.bg_bottom_menu_gallery,
			R.drawable.bg_bottom_menu_message,
			R.drawable.bg_bottom_menu_favorite, R.drawable.bg_bottom_menu_feed,
			R.drawable.bg_bottom_menu_add_listing };

	private final int BOTTOM_SELECTED_BACKGROUND[] = {
			R.drawable.bg_bottom_menu_gallery_selected,
			R.drawable.bg_bottom_menu_message_selected,
			R.drawable.bg_bottom_menu_favorite_selected,
			R.drawable.bg_bottom_menu_feed_selected,
			R.drawable.bg_bottom_menu_add_listing_selected };

	public static final int FRAGMENT_GALLERY = 0;
	public static final int FRAGMENT_MESSAGE = 1;
	public static final int FRAGMENT_FAVORITE_ITEMS = 2;
	public static final int FRAGMENT_ACTIVITY_FEED = 3;
	public static final int FRAGMENT_MESSAGE_LIST = 4;
	public static final int FRAGMENT_ADD_LISTING = 5;

	private ArrayList<Item> arrFreeListings, arrActiveListings,
			arrExtraListings, arrExtraActiveListings, arrExpiredListings,
			arrGoneListings, arrYourListing;
	private YourListingItemAdapter goneAdapter;
	private YourFreeListingItemAdapter activeAdapter;
	private YourListingExtraItemAdapter extraAdapter;
	private YourListingExpiredItemAdapter expiredAdapter;

	public Bundle bundle;

	// Menu left action :
	private int CURRENT_SLIDER_TAB = 0;
	private static final int TAB_DEFAULT = 0;
	private static final int TAB_YOUR_DETAIL = 1;
	private static final int TAB_YOUR_LISTING = 2;
	private static final int TAB_SEARCH_LOCATION = 3;
	private static final int TAB_SHARE_SETTINGS = 4;
	private static final int TAB_BLOCKED_USER = 5;
	// check load data
	public static boolean isRefreshYourListings = false,
			isRefreshBlockedUser = true, isRefreshShareSettings = true,
			isRefreshGallery = false;

	private int leftMenuScrollDestX;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_layout);
		initInAppPurchase();
		self = this;
		// setup check key
		isRefreshYourListings = true;
		isRefreshShareSettings = true;
		isRefreshBlockedUser = true;

		self.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

		// Thread.setDefaultUncaughtExceptionHandler(new
		// ExceptionHandler(this));

		Log.d("",
				pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN));

		handler = new Handler();
		initUI();
		initMenu();
		initControl();
		initFragment();
		initData();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isHomeVisible = true;

		getIntentData();

		if (!mSllLeftMenu.isOpened()) {
			if (bundle != null) {
				if (bundle.containsKey(GlobalValue.KEY_ACTION)) {
					if (bundle.getString(GlobalValue.KEY_ACTION).equals(
							GlobalValue.KEY_ACTION_LOGIN_REFRESH_TOKEN)) {
						if (bundle
								.containsKey(GlobalValue.KEY_CONFIRM_CHANGE_EMAIL)) {
							showMessageDialog(
									getString(R.string.message_confirm_change_email_successfully),
									new DialogListener() {

										@Override
										public void onClose(Dialog dialog) {
											// TODO Auto-generated method stub

										}
									});
						}
						refreshToken();
					} else {
						initNormal();
					}
				} else {
					initNormal();
				}
			} else {
				initNormal();
			}
		} else {
			if (leftMenu.getBehindOffset() > 0) {
				closeSliderLayout(mSllLeftMenu, false);
			}

			// Refresh avatar
			if (CURRENT_SLIDER_TAB == TAB_YOUR_DETAIL) {
				if (selectImage != null) {
					imgAvatarChange.setImageBitmap(selectImage);
					imgAvatar.setImageBitmap(selectImage);
				}
			}

			// refresh your listing

			if (isRefreshYourListings && !isLoadLeftMenuFirstTime) {
				getAccountInfo();

			}
			if (isRefreshGallery) {
				// Intent intent = new Intent(
				// GlobalValue.INTENT_ACTION_UPDATE_LIST);
				// LocalBroadcastManager.getInstance(HomeActivity.this)
				// .sendBroadcast(intent);
				// getGalleryFragment().refreshGallery();
			}
		}

		// close login dialog
		if (!isLoggedIn()) {

			if (!leftMenu.isMenuShowing()) {
				llWrapperAlertHome.setVisibility(View.GONE);
				restoreLayoutBottom();
				// refreshTopLeftMenu();
				checkLoginUI();
			}

		}
		// load when comeback from error activity
		if (isError) {
			((TreasureTrashBaseFragment) arrFragments.get(currentFragment))
					.onShow();
			isError = false;
		}
	}

	private void initNormal() {
		if (GlobalValue.myAccount != null) {
			loadMenuLeftFirstTime();
		}
		checkLoginUI();
		getTotalNewMessages();
		if (arrCategoryGroups.size() == 0) {
			getCategories();

		} else {
			// initCategoriesMenu();
		}

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Send a screen view when the Activity is displayed to the user.
		isHomeAlive = true;

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		super.onDestroy();

		isHomeAlive = false;

		arrFragments = null;

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}

		// refresh check refresh key
		isRefreshYourListings = true;
		isRefreshShareSettings = true;
		isRefreshBlockedUser = true;

	}

	private void initMenu() {
		initLeftMenu(onLeftMenuToggleListener);
		initRightMenu(onRightMenuToggleListener);

		leftMenu.attachToActivity(HomeActivity.this,
				SlidingMenu.SLIDING_CONTENT, true);
		rightMenu.attachToActivity(HomeActivity.this,
				SlidingMenu.SLIDING_CONTENT, true);

		layoutWrapperMenuLeft.setOnClickListener(this);
		ivUserAvatar.setOnClickListener(this);
		tvLocation = (TextView) rightMenu.findViewById(R.id.tvLocation);

		llAlertCategory = (LinearLayout) rightMenu
				.findViewById(R.id.llAlertCategory);

		btnSearch = (AutoBgButton) rightMenu.findViewById(R.id.btnSearch);
		tvChange = (TextView) rightMenu
				.findViewById(R.id.tvChangeSearchLocation);
		etSearch = (EditText) rightMenu.findViewById(R.id.etSearch);
		tvBrowse = (TextView) rightMenu.findViewById(R.id.tvBrowse);
		tvEditCategories = (TextView) rightMenu
				.findViewById(R.id.tvEditCategories);

		ivFreeListings = (ImageView) rightMenu.findViewById(R.id.ivSearchFree);
		ivQuickSales = (ImageView) rightMenu.findViewById(R.id.ivSearchQuick);
		ivPremimumListings = (ImageView) rightMenu
				.findViewById(R.id.ivSearchPremium);

		sllChangeLocation = (SlidingLayer) rightMenu
				.findViewById(R.id.sll_change_location);
		sllChangeLocation.setStickTo(SlidingLayer.STICK_TO_RIGHT);
		sllChangeLocation.setCloseOnTapEnabled(false);

		tvDoneRight = (TextView) rightMenu.findViewById(R.id.tvDone);
		tvLocationRight = (TextView) rightMenu
				.findViewById(R.id.tvLocationRight);
		etRightLocation = (EditText) rightMenu
				.findViewById(R.id.etLocationRight);
		ivSearchLocationRight = (ImageView) rightMenu
				.findViewById(R.id.ivSearchLocationRight);
		lvLocationRight = (ListView) rightMenu
				.findViewById(R.id.lv_location_search_right);
		tvCurrentLocationRight = (TextView) rightMenu
				.findViewById(R.id.tvCurrentLocation);

		tvCurrentLocationRight.setOnClickListener(this);
		llAlertCategory.setOnClickListener(this);
		tvDoneRight.setOnClickListener(this);
		ivSearchLocationRight.setOnClickListener(this);

		ivFreeListings.setOnClickListener(this);
		ivQuickSales.setOnClickListener(this);
		ivPremimumListings.setOnClickListener(this);

		lvLocationRight.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				lvLocationRight.setVisibility(View.GONE);
				tempRightLocObj = mArrLocationSearch.get(position);
				placeRightMapTempLocation(tempRightLocObj);

			}
		});

		// share setting screen
		lblFaceBookShare = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lblFaceBookShare);
		lblGoogleShare = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lblGoogleShare);
		imgFacebookShare = (ImageView) leftMenu
				.findViewById(R.id.imgFacebookShare);
		imgGoogleShare = (ImageView) leftMenu.findViewById(R.id.imgGoogleShare);
	}

	private void initControl() {
		getParentView(tvLeftMenuSignup).setOnClickListener(this);
		getParentView(tvLeftMenuLogin).setOnClickListener(this);
		getParentView(tvLeftMenuYourListings).setOnClickListener(this);
		getParentView(tvLeftMenuYourDetails).setOnClickListener(this);
		getParentView(tvLeftMenuLocation).setOnClickListener(this);
		getParentView(tvLeftMenuShareSettings).setOnClickListener(this);
		getParentView(tvLeftMenuBlockMember).setOnClickListener(this);
		getParentView(tvLeftMenuLogout).setOnClickListener(this);
		getParentView(tvLeftMenuFeedback).setOnClickListener(this);
		getParentView(tvLeftMenuFAQ).setOnClickListener(this);
		getParentView(tvLeftMenuPolicy).setOnClickListener(this);
		getParentView(tvLeftMenuTerm).setOnClickListener(this);

		// set onclick for sliding layer
		layoutWrapperYourDetail.setOnClickListener(this);
		layoutWrapperYourListing.setOnClickListener(this);
		layoutWrapperChangeLocation.setOnClickListener(this);
		layoutWrapperSeachLocation.setOnClickListener(this);
		layoutWrapperShareSetting.setOnClickListener(this);
		layoutWrapperBlocker.setOnClickListener(this);

		ivLeftMenu.setOnClickListener(this);
		ivCloseLeftMenu.setOnClickListener(this);
		ivDiamond.setOnClickListener(this);
		ivRightMenu.setOnClickListener(this);
		llGallery.setOnClickListener(this);
		llMessage.setOnClickListener(this);
		llFavorite.setOnClickListener(this);
		llFeed.setOnClickListener(this);
		llAddListing.setOnClickListener(this);
		llAlert.setOnClickListener(this);
		llActualCoachMark.setOnClickListener(this);

		btnFindLocalTreasure.setOnClickListener(this);

		tvEditCategories.setOnClickListener(this);

		tvChange.setOnClickListener(this);
		btnSearch.setOnClickListener(this);

		etSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					processSearching();

					return true;
				}
				return false;
			}
		});

		lvCategories.setOnGroupClickListener(new OnGroupClickListener() {

			@Override
			public boolean onGroupClick(ExpandableListView parent, View v,
					int groupPosition, long id) {

				if (pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT) != 0
						&& pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG) != 0) {

					if (arrCategoryGroups.get(groupPosition).isActive()) {
						// showFragment(FRAGMENT_GALLERY);
						Category category = arrCategoryGroups
								.get(groupPosition).getGrandParent();

						getGalleryFragment().action = GlobalValue.KEY_ACTION_SEARCH;
						getGalleryFragment().searchType = getGalleryFragment().SEARCH_TYPE_CATEGORY;
						getGalleryFragment().setSearchAttribute(
								category.getId(), category.getName(), "", "",
								"");

						Intent intent = new Intent(
								GlobalValue.INTENT_ACTION_SEARCHING);
						LocalBroadcastManager.getInstance(HomeActivity.this)
								.sendBroadcast(intent);
						rightMenu.toggle();
						showFragment(FRAGMENT_GALLERY);
						stackFragment.push(FRAGMENT_GALLERY);

					}

				}
				return true;
			}

		});

		lvCategories.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {

				if (pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT) != 0
						&& pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG) != 0) {
					// showFragment(FRAGMENT_GALLERY);
					Category category = arrCategoryGroups.get(groupPosition)
							.getArrCategories().get(childPosition);

					getGalleryFragment().action = GlobalValue.KEY_ACTION_SEARCH;
					getGalleryFragment().searchType = getGalleryFragment().SEARCH_TYPE_CATEGORY;
					getGalleryFragment().setSearchAttribute(category.getId(),
							category.getName(), "", "", "");

					Intent intent = new Intent(
							GlobalValue.INTENT_ACTION_SEARCHING);
					LocalBroadcastManager.getInstance(HomeActivity.this)
							.sendBroadcast(intent);
					showFragment(FRAGMENT_GALLERY);
				}

				rightMenu.toggle();

				return true;
			}
		});

		// Your detail listener
		// llTransparentYourDetail.setOnClickListener(this);
		llMale.setOnClickListener(this);
		llFemale.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		lblChangeAvatar.setOnClickListener(this);
		lblChangeLocation.setOnClickListener(this);
		llTransparentChangeAvt.setOnClickListener(this);
		imgCamera.setOnClickListener(this);
		imgGallery.setOnClickListener(this);
		lblDOB.setOnClickListener(this);
		lblShowHidePassword.setOnClickListener(this);
		txtYourName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int color = Color.parseColor(getString(R.color.white));
				layoutYourName.setBackgroundColor(color);
				txtYourName.setBackgroundColor(color);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		txtYourEmail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int color = Color.parseColor(getString(R.color.white));
				layoutEmail.setBackgroundColor(color);
				txtYourEmail.setBackgroundColor(color);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		txtPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int color = Color.parseColor(getString(R.color.white));
				layoutPassword.setBackgroundColor(color);
				txtPassword.setBackgroundColor(color);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		txtPassword.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				// TODO Auto-generated method stub
				String currentpass = GlobalValue.myAccount.getPassword();
				String passChanged = txtPassword.getText().toString();
				if (actionId == EditorInfo.IME_ACTION_DONE
						&& (!currentpass.equals(passChanged))) {
					if (!isShowConfirmPassword)
						showConfirmChangePassword();
				}
				return false;
			}
		});

		// Listing listener
		// llTransparentListing.setOnClickListener(this);
		scrollListing.setOnRefreshListener(new OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(PullToRefreshBase<ScrollView> refreshView) {
				// TODO Auto-generated method stub
				isLoadingyourListing = true;
				getAccountInfo();
			}
		});
		lblSelectPack.setOnClickListener(this);
		grvFreeListings.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position < arrActiveListings.size()) {
					// GlobalValue.arrItems = arrActiveListings;
					// GlobalValue.currentItemPosition = position;
					Bundle bundle = new Bundle();
					bundle.putString(GlobalValue.KEY_ACTION_ITEM_DETAIL,
							GlobalValue.KEY_ACTION_FROM_GALLERY);
					bundle.putParcelableArrayList("arrItem", arrActiveListings);
					bundle.putInt("currentPosition", position);
					gotoActivity(self, ItemDetailsActivity.class, bundle);
				} else {
					gotoAddAListingWithDefaultType();
				}
			}
		});

		grvExtraListings.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if (position < arrExtraActiveListings.size()) {
					// GlobalValue.arrItems = arrExtraActiveListings;
					// GlobalValue.currentItemPosition = position;
					Bundle bundle = new Bundle();
					bundle.putParcelableArrayList("arrItem",
							arrExtraActiveListings);
					bundle.putInt("currentPosition", position);
					gotoActivity(self, ItemDetailsActivity.class, bundle);
				} else {
					gotoAddAListingWithAdditionalType();
				}
			}
		});

		grvExpiredListings.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// relistClick(position);
				// GlobalValue.arrItems = arrExpiredListings;
				// GlobalValue.currentItemPosition = position;
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("arrItem", arrExpiredListings);
				bundle.putInt("currentPosition", position);
				gotoActivity(self, ItemDetailsActivity.class, bundle);
			}
		});

		grvGoneListings.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				// GlobalValue.arrItems = arrGoneListings;
				// GlobalValue.currentItemPosition = position;
				Bundle bundle = new Bundle();
				bundle.putParcelableArrayList("arrItem", arrGoneListings);
				bundle.putInt("currentPosition", position);
				gotoActivity(self, ItemDetailsActivity.class, bundle);
			}
		});

		// Location listener
		llTransparentLocation.setOnClickListener(this);
		lblCurrentChangeLocation.setOnClickListener(this);
		mImgSearchLocation.setOnClickListener(this);
		lblSelectLocation.setOnClickListener(this);
		mLvLocationSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mLvLocationSearch.setVisibility(View.GONE);
				currentSearchLocObject = mArrLocationSearch.get(position);
				// Refresh maps
				initMapsChangeLocation();
			}
		});

		// search Location listener
		// llTransparentSearchLocation.setOnClickListener(this);
		lblCurrentSearchLocation.setOnClickListener(this);
		btnSearchLocation.setOnClickListener(this);
		lsvSearchLocation.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				isChangedSearchLocation = true;
				txtSearchLocation.setText("");
				etRightLocation.setText("");
				lsvSearchLocation.setVisibility(View.GONE);
				currentSearchLocObject = mArrLocationSearch.get(position);
				pref.putFloatValue(
						TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT,
						(float) currentSearchLocObject.getLatitude());
				pref.putFloatValue(
						TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG,
						(float) currentSearchLocObject.getLongitude());
				pref.putBooleanValue(
						TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_USE_CURRENT,
						false);
				pref.putStringValue(
						TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_NAME,
						currentSearchLocObject.getLocationAddress());

				// Refresh maps search location
				initMapsSearchLocation();
				// leftMenu.showContent(true);

			}
		});

		// Sharing listener
		// llTransparentShare.setOnClickListener(this);
		llConnectYourFb.setOnClickListener(this);
		llConnectYourGGP.setOnClickListener(this);

		// Blocking listener
		// llTransparentBlock.setOnClickListener(this);

		// bottom menu left
		tvLeftMenuFeedback.setOnClickListener(this);
		tvLeftMenuFAQ.setOnClickListener(this);
		tvLeftMenuPolicy.setOnClickListener(this);
		tvLeftMenuTerm.setOnClickListener(this);

		mSllLeftMenu.setOnInteractListener(new OnInteractListener() {

			@Override
			public void onOpened() {
				updateDataForSlider(CURRENT_SLIDER_TAB);
			}

			@Override
			public void onOpen() {

			}

			@Override
			public void onClosed() {
				handler.postDelayed(new Runnable() {
					public void run() {
						leftMenu.getCustomViewAbove().restoreMenu();

						updateMenuLeftSlider(CURRENT_SLIDER_TAB);

						if (isChangedSearchLocation) {
							isChangedSearchLocation = false;
							getGalleryFragment().action = GlobalValue.KEY_ACION_DISPLAY;
							Intent intent = new Intent(
									GlobalValue.INTENT_ACTION_REFRESH);
							LocalBroadcastManager.getInstance(self)
									.sendBroadcast(intent);
							showFragment(FRAGMENT_GALLERY);
						}
					}
				}, 10);

			}

			@Override
			public void onClose() {

			}
		});

		mSllLocation.setOnInteractListener(new OnInteractListener() {

			@Override
			public void onOpened() {
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						initMapsChangeLocation();

					}
				}, 1000);
			}

			@Override
			public void onOpen() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClosed() {
				mSllLocation.setVisibility(View.GONE);

			}

			@Override
			public void onClose() {
				// TODO Auto-generated method stub

			}
		});

		sllChangeLocation.setOnInteractListener(new OnInteractListener() {

			@Override
			public void onOpened() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onOpen() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClosed() {
				// TODO Auto-generated method stub

			}

			@Override
			public void onClose() {
				// TODO Auto-generated method stub

			}
		});

	}

	private void updateMenuLeftSlider(int currentTab) {
		// update menu active
		updateActiveMenuLeft(currentTab);
		// show hide tab
		updateShowHideSlider(currentTab);
		// load data
		if (currentTab != TAB_DEFAULT) {
			if (mSllLeftMenu.isOpened()) {
				updateDataForSlider(currentTab);
			} else {
				showSliderLayout(mSllLeftMenu);
			}
		}

	}

	private void updateDataForSlider(int currentSlider) {
		switch (currentSlider) {
		case TAB_YOUR_DETAIL:
			temAccount = null;
			currentSearchLocObject = null;
			showProfileDetail();
			break;
		case TAB_YOUR_LISTING:
			if (isRefreshYourListings) {
				getAccountInfo();
			}
			break;
		case TAB_SEARCH_LOCATION:
			initMapsSearchLocation();
			break;
		case TAB_SHARE_SETTINGS:
			if (isRefreshShareSettings)
				loadShareSettings();
			break;
		case TAB_BLOCKED_USER:
			if (isRefreshBlockedUser)
				getBlockedUserList(true);
			break;

		default:
			break;
		}
	}

	private void updateShowHideSlider(int currentSlider) {
		// show/hide tab
		switch (currentSlider) {
		case TAB_YOUR_DETAIL:
			// set layout
			layoutWrapperYourDetail.setVisibility(View.VISIBLE);
			layoutWrapperYourListing.setVisibility(View.GONE);
			layoutWrapperSeachLocation.setVisibility(View.GONE);
			layoutWrapperShareSetting.setVisibility(View.GONE);
			layoutWrapperBlocker.setVisibility(View.GONE);

			break;
		case TAB_YOUR_LISTING:
			// set layout
			layoutWrapperYourDetail.setVisibility(View.GONE);
			layoutWrapperYourListing.setVisibility(View.VISIBLE);
			layoutWrapperSeachLocation.setVisibility(View.GONE);
			layoutWrapperShareSetting.setVisibility(View.GONE);
			layoutWrapperBlocker.setVisibility(View.GONE);

			break;
		case TAB_SEARCH_LOCATION:
			// set layout
			layoutWrapperYourDetail.setVisibility(View.GONE);
			layoutWrapperYourListing.setVisibility(View.GONE);
			layoutWrapperSeachLocation.setVisibility(View.VISIBLE);
			layoutWrapperShareSetting.setVisibility(View.GONE);
			layoutWrapperBlocker.setVisibility(View.GONE);

			break;
		case TAB_SHARE_SETTINGS:
			// set layout
			layoutWrapperYourDetail.setVisibility(View.GONE);
			layoutWrapperYourListing.setVisibility(View.GONE);
			layoutWrapperSeachLocation.setVisibility(View.GONE);
			layoutWrapperShareSetting.setVisibility(View.VISIBLE);
			layoutWrapperBlocker.setVisibility(View.GONE);

			break;
		case TAB_BLOCKED_USER:
			// set layout
			layoutWrapperYourDetail.setVisibility(View.GONE);
			layoutWrapperYourListing.setVisibility(View.GONE);
			layoutWrapperSeachLocation.setVisibility(View.GONE);
			layoutWrapperShareSetting.setVisibility(View.GONE);
			layoutWrapperBlocker.setVisibility(View.VISIBLE);

			break;
		default:
			mSllLeftMenu.closeLayer(true);
			layoutWrapperYourDetail.setVisibility(View.GONE);
			layoutWrapperYourListing.setVisibility(View.GONE);
			layoutWrapperSeachLocation.setVisibility(View.GONE);
			layoutWrapperShareSetting.setVisibility(View.GONE);
			layoutWrapperBlocker.setVisibility(View.GONE);
			break;

		}
	}

	private void updateActiveMenuLeft(int currentSelectedMenu) {

		// tab your detail
		if (currentSelectedMenu == TAB_YOUR_DETAIL) {
			getParentView(tvLeftMenuYourDetails).setBackgroundColor(
					getResources().getColor(R.color.sign_up_teal));
			ivLeftMenuYourDetails
					.setImageResource(R.drawable.icon_menu_your_details_white);
			tvLeftMenuYourDetails.setTextColor(getResources().getColor(
					R.color.white));
		} else {
			getParentView(tvLeftMenuYourDetails).setBackgroundResource(
					R.drawable.bg_left_menu_item_selector);
			ivLeftMenuYourDetails
					.setImageResource(R.drawable.icon_menu_your_details);
			tvLeftMenuYourDetails.setTextColor(getResources().getColor(
					R.color.sign_up_teal));
		}

		// tab your listing
		if (currentSelectedMenu == TAB_YOUR_LISTING) {
			getParentView(tvLeftMenuYourListings).setBackgroundColor(
					getResources().getColor(R.color.sign_up_teal));
			ivLeftMenuYourListings
					.setImageResource(R.drawable.icon_menu_your_listings_white);
			tvLeftMenuYourListings.setTextColor(getResources().getColor(
					R.color.white));
		} else {
			getParentView(tvLeftMenuYourListings).setBackgroundResource(
					R.drawable.bg_left_menu_item_selector);
			ivLeftMenuYourListings
					.setImageResource(R.drawable.icon_menu_your_listings);
			tvLeftMenuYourListings.setTextColor(getResources().getColor(
					R.color.sign_up_teal));
		}

		// tab search location
		if (currentSelectedMenu == TAB_SEARCH_LOCATION) {
			getParentView(tvLeftMenuLocation).setBackgroundColor(
					getResources().getColor(R.color.sign_up_teal));
			ivLeftMenuLocation
					.setImageResource(R.drawable.icon_menu_location_white);
			tvLeftMenuLocation.setTextColor(getResources().getColor(
					R.color.white));
		} else {
			getParentView(tvLeftMenuLocation).setBackgroundResource(
					R.drawable.bg_left_menu_item_selector);
			ivLeftMenuLocation.setImageResource(R.drawable.icon_menu_location);
			tvLeftMenuLocation.setTextColor(getResources().getColor(
					R.color.sign_up_teal));
		}

		// tab your listing
		if (currentSelectedMenu == TAB_SHARE_SETTINGS) {
			getParentView(tvLeftMenuShareSettings).setBackgroundColor(
					getResources().getColor(R.color.sign_up_teal));
			ivLeftMenuShareSettings
					.setImageResource(R.drawable.icon_menu_share_settings_white);
			tvLeftMenuShareSettings.setTextColor(getResources().getColor(
					R.color.white));
		} else {
			getParentView(tvLeftMenuShareSettings).setBackgroundResource(
					R.drawable.bg_left_menu_item_selector);
			ivLeftMenuShareSettings
					.setImageResource(R.drawable.icon_menu_share_settings);
			tvLeftMenuShareSettings.setTextColor(getResources().getColor(
					R.color.sign_up_teal));
		}

		// tab blocked user
		if (currentSelectedMenu == TAB_BLOCKED_USER) {
			getParentView(tvLeftMenuBlockMember).setBackgroundColor(
					getResources().getColor(R.color.sign_up_teal));
			ivLeftMenuBlockMember
					.setImageResource(R.drawable.icon_menu_block_white);
			tvLeftMenuBlockMember.setTextColor(getResources().getColor(
					R.color.white));
		} else {
			getParentView(tvLeftMenuBlockMember).setBackgroundResource(
					R.drawable.bg_left_menu_item_selector);
			ivLeftMenuBlockMember.setImageResource(R.drawable.icon_menu_block);
			tvLeftMenuBlockMember.setTextColor(getResources().getColor(
					R.color.sign_up_teal));
		}
	}

	//

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		View view = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (view instanceof EditText && view.getId() != R.id.etInputMessage) {
			// if (view != getMessageListFragment().etInputMessage
			// && view != getMessageListFragment().btnSendMessage) {
			View w = getCurrentFocus();
			int scrcoords[] = new int[2];
			w.getLocationOnScreen(scrcoords);
			float x = event.getRawX() + w.getLeft() - scrcoords[0];
			float y = event.getRawY() + w.getTop() - scrcoords[1];

			if (event.getAction() == MotionEvent.ACTION_UP
					&& (x < w.getLeft() || x >= w.getRight() || y < w.getTop() || y > w
							.getBottom())) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
						.getWindowToken(), 0);
				if (view == txtPassword) {
					String currentpass = GlobalValue.myAccount.getPassword();
					String passChanged = txtPassword.getText().toString();
					if (!currentpass.equals(passChanged)) {
						if (!isShowConfirmPassword)
							showConfirmChangePassword();
					}
				}
			}
			// }

		}

		return ret;
	}

	private void initUI() {

		ivLeftMenu = (ImageView) findViewById(R.id.ivLeftMenu);
		ivCloseLeftMenu = (ImageView) findViewById(R.id.ivCloseLeftMenu);
		ivDiamond = (ImageView) findViewById(R.id.ivDiamond);
		ivRightMenu = (ImageView) findViewById(R.id.ivRightMenu);
		rlCoachMark = (RelativeLayout) findViewById(R.id.rlCoachMark);
		rlHeader = (RelativeLayout) findViewById(R.id.rlHeader);
		rlNewMessage = (RelativeLayout) findViewById(R.id.rlNewMessage);
		btnFindLocalTreasure = (AutoBgButton) findViewById(R.id.btnFindLocalTreasure);

		llAlert = (LinearLayout) findViewById(R.id.llAlertHome);
		llWrapperAlertHome = (LinearLayout) findViewById(R.id.llWrapperAlertHome);
		llBottom = (LinearLayout) findViewById(R.id.llBottom);

		llGallery = (LinearLayout) findViewById(R.id.llGallery);
		llMessage = (LinearLayout) findViewById(R.id.llMessage);
		llFavorite = (LinearLayout) findViewById(R.id.llFavorite);
		llAddListing = (LinearLayout) findViewById(R.id.llAddListing);
		llFeed = (LinearLayout) findViewById(R.id.llFeed);
		llActualCoachMark = (LinearLayout) findViewById(R.id.llActualCoachMark);

		tvAlertMessage = (TextView) findViewById(R.id.tvAlertMessage);
		tvNewMessage = (TextView) findViewById(R.id.tvNewMessage);

		listBottomButtons = new ArrayList<LinearLayout>();
		listBottomButtons.add(llGallery);
		listBottomButtons.add(llMessage);
		listBottomButtons.add(llFavorite);
		listBottomButtons.add(llFeed);
		listBottomButtons.add(llAddListing);

	}

	private void initData() {

		arrYourListing = new ArrayList<Item>();
		// load data menu left first time
		arrCategoryGroups = new ArrayList<CategoryGroup>();
		adapterCategory = new ExpandableCategoriesAdapter(self,
				expandableListviewListener);
		adapterCategory.setData(arrCategoryGroups);
		lvCategories.setAdapter(adapterCategory);

		llBottom.setBackgroundColor(getResources().getColor(
				R.color.sign_up_teal));
		if (!pref
				.getBooleanValue(TreasureTrashSharedPreferences.PREF_HOME_FIRST_LAUNCH)) {
			rlCoachMark.setVisibility(View.VISIBLE);
			pref.putBooleanValue(
					TreasureTrashSharedPreferences.PREF_HOME_FIRST_LAUNCH, true);
		} else {
			rlCoachMark.setVisibility(View.GONE);
		}

		getGalleryFragment().action = GlobalValue.KEY_ACION_DISPLAY; // Default
																		// for
		// gallery is
		// load all
		// items

		bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(GlobalValue.KEY_ACTION)) {
				// Log.d("BUNDLE_KEY",
				// bundle.getString(GlobalValue.KEY_ACTION));
				if (bundle.getString(GlobalValue.KEY_ACTION).equals(
						GlobalValue.KEY_ACION_DISPLAY)) {
					getGalleryFragment().action = GlobalValue.KEY_ACION_DISPLAY;
				} else if (bundle.getString(GlobalValue.KEY_ACTION).equals(
						GlobalValue.KEY_ACTION_SEARCH)) {
					getGalleryFragment().action = GlobalValue.KEY_ACTION_SEARCH;
					getGalleryFragment().searchType = getGalleryFragment().SEARCH_TYPE_CATEGORY;
					if (bundle.containsKey(GlobalValue.KEY_CATEGORY_ID)) {
						String categoryId = bundle
								.getString(GlobalValue.KEY_CATEGORY_ID);
						String categoryName = bundle
								.getString(GlobalValue.KEY_CATEGORY_NAME);

						getGalleryFragment().setSearchAttribute(categoryId,
								categoryName, "", "", "");
					}
				} else if (bundle.getString(GlobalValue.KEY_ACTION).equals(
						GlobalValue.KEY_ACTION_SHOW_BY_LOCATION)) {
					getGalleryFragment().action = GlobalValue.KEY_ACTION_SHOW_BY_LOCATION;
				} else if (bundle.getString(GlobalValue.KEY_ACTION).equals(
						GlobalValue.KEY_ACTION_SHOW_PUBLISHED_LISTING)) {
					// getAccountInfo();
					getGalleryFragment().action = GlobalValue.KEY_ACTION_SHOW_PUBLISHED_LISTING;
				} else if (bundle.getString(GlobalValue.KEY_ACTION).equals(
						GlobalValue.KEY_ACTION_SHOW_MESSAGES)) {
					MessageListFragment messageListFragment = (MessageListFragment) arrFragments
							.get(FRAGMENT_MESSAGE_LIST);
					messageListFragment.action = GlobalValue.KEY_ACTION_SHOW_MESSAGES;
					messageListFragment.itemId = GlobalValue.KEY_ACTION_SHOW_MESSAGES;
					messageListFragment.type = MessageListFragment.TYPE_YOUR_LISTING;
				}
				if (bundle.getString(GlobalValue.KEY_ACTION).equals(
						GlobalValue.KEY_ACTION_SHOW_MESSAGES)) {
					showFragment(FRAGMENT_MESSAGE_LIST);
				}
				if (bundle.getString(GlobalValue.KEY_ACTION).equals(
						GlobalValue.KEY_ACTION_AFTER_LOGIN)) {
					// getInitBlockerAndBlockedUsers();
					initNormal();
					if (isPubnubServiceConnected && !isGetHistory) {
						isGetHistory = true;
						long lastTimeReceivedMessage = pref
								.getLongValue(TreasureTrashSharedPreferences.PREF_LAST_TIME_RECEIVED_MESSAGE
										+ pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID));
						mPubnubService
								.getHistoryByChannel(
										pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID),
										lastTimeReceivedMessage);
					}

				}
				if (bundle.getString(GlobalValue.KEY_ACTION).equalsIgnoreCase(
						GlobalValue.KEY_ACTION_AFTER_DELETE)) {
					leftMenu.showMenu(true);
					// CURRENT_SLIDER_TAB = TAB_YOUR_LISTING;
					// updateMenuLeftSlider(CURRENT_SLIDER_TAB);

				}
			}

			else if (bundle
					.containsKey(GlobalValue.KEY_ACTION_FROM_NOTIFICATION)) {
				actionFromNotification();
			}

			if (bundle.containsKey(GlobalValue.FORCED_LOGIN_ACTION)) {
				forceLoginAction = bundle
						.getString(GlobalValue.FORCED_LOGIN_ACTION);
				forceLoginActionOptions();
			}

		}

		if (GlobalValue.HOME_FIRST_LAUNCH) {
			GlobalValue.HOME_FIRST_LAUNCH = false;
		} else {
			rlCoachMark.setVisibility(View.GONE);
			if (pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT) != 0
					&& pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG) != 0)
				// showFragment(FRAGMENT_DEFAULT);
				if (!forceLoginAction.equals(""))
					showFragment(FRAGMENT_GALLERY);
		}

		// first load menu left :

	}

	private void actionFromNotification() {
		String action = bundle
				.getString(GlobalValue.KEY_ACTION_FROM_NOTIFICATION);

		if (action.equalsIgnoreCase(GlobalValue.KEY_MESSAGE_RECEIVED)) {
			showFragment(FRAGMENT_MESSAGE);
			return;
		}

		if (action.equalsIgnoreCase(ServerAlert.NOTIFICATION_MSG_LISTING)) {
			String itemId = bundle.getString(GlobalValue.KEY_ITEM_ID);
			Item item = new Item();
			item.setId(itemId);
			item.setTitle("");
			item.setImage("");

			ArrayList<Item> list = new ArrayList<Item>();
			list.add(item);

			// GlobalValue.arrItems = list;
			// GlobalValue.currentItemPosition = 0;

			Bundle bundle = new Bundle();
			bundle.putString(GlobalValue.KEY_ACTION_ITEM_DETAIL,
					GlobalValue.KEY_ACTION_ITEM_DETAIL_SINGLE_ITEM);

			bundle.putParcelableArrayList("arrItem", list);
			bundle.putInt("currentPosition", 0);

			gotoActivity(self, ItemDetailsActivity.class, bundle);

			return;
		}
		if (action.equalsIgnoreCase(ServerAlert.NOTIFICATION_MSG_GONE)
				|| action.equalsIgnoreCase(ServerAlert.NOTIFICATION_MSG_FOLLOW)
				|| action
						.equalsIgnoreCase(ServerAlert.NOTIFICATION_FRIEND_JOIN)) {
			showFragment(FRAGMENT_ACTIVITY_FEED);
			return;
		}
		if (action.equalsIgnoreCase(ServerAlert.NOTIFICATION_MSG_EXPIRE)) {
			String messageContent = bundle.getString(PubnubService.CONTENT);
			onReceiveItemExpired(messageContent);
			return;
		}
		if (action.equals(ServerAlert.NOTIFICATION_FREE_MESSAGE)) {
			String messageContent = bundle.getString(PubnubService.CONTENT);
			// Log.i("MESSAGE_CONTENT", messageContent);
			showMessageDialog(messageContent, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method stub

				}
			});
			return;
		}
	}

	private void getIntentData() {
		try {
			Uri data = getIntent().getData();
			if (data != null && data.toString().contains("Item")) {
				String path = data.toString();
				String itemID = path.substring(path.indexOf("=") + 1,
						path.length());
				Log.i("EMAIL_INTENT", itemID);
				loadItem(itemID);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void loadMenuLeftFirstTime() {
		if (isRefreshYourListings) {
			loadListingsList();
		}
		if (isRefreshShareSettings) {
			loadShareSettings();
			isRefreshShareSettings = false;
		}
		if (isRefreshBlockedUser) {
			getBlockedUserList(false);
			isRefreshBlockedUser = false;
		}
		// load data menu left first time
		if (GlobalValue.myAccount != null) {
			showProfileDetail();
			// initMapsSearchLocation();
		}

	}

	private void forceLoginActionOptions() {
		if (forceLoginAction
				.equals(GlobalValue.KEY_FORCED_LOGIN_ACTION_MESSAGE)) {
			showFragment(FRAGMENT_MESSAGE);
			forceLoginAction = "";
		} else if (forceLoginAction
				.equals(GlobalValue.KEY_FORCED_LOGIN_ACTION_SAVED_ITEM)) {
			showFragment(FRAGMENT_FAVORITE_ITEMS);
			forceLoginAction = "";
		} else if (forceLoginAction
				.equals(GlobalValue.KEY_FORCED_LOGIN_ACTION_ACTIVITY_FEED)) {
			showFragment(FRAGMENT_ACTIVITY_FEED);
			forceLoginAction = "";
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_ADD_LISTING)) {
			gotoActivity(self, AddAListingActivity.class);
			forceLoginAction = "";
		}
	}

	private void initFragment() {

		fm = getSupportFragmentManager();

		arrFragments = new ArrayList<Fragment>();

		arrFragments.add((Fragment) fm.findFragmentById(R.id.fragmentGallery));
		arrFragments.add((Fragment) fm.findFragmentById(R.id.fragmentMessage));
		arrFragments.add((Fragment) fm
				.findFragmentById(R.id.fragmentFavoriteItems));
		arrFragments.add((Fragment) fm
				.findFragmentById(R.id.fragmentActivityFeed));
		arrFragments.add((Fragment) fm
				.findFragmentById(R.id.fragmentMesssageList));

		// show default fragment
		FragmentTransaction transaction = fm.beginTransaction();
		for (Fragment fragment : arrFragments) {
			transaction.hide(fragment);
		}
		transaction.commit();
		showFragment(FRAGMENT_GALLERY);

	}

	private GalleryFragment getGalleryFragment() {
		return (GalleryFragment) arrFragments.get(FRAGMENT_GALLERY);
	}

	private MessageListFragment getMessageListFragment() {
		return (MessageListFragment) arrFragments.get(FRAGMENT_MESSAGE_LIST);
	}

	public void disableBottomLayout() {
		rlCoachMark.setVisibility(View.GONE);
		llBottom.setBackgroundColor(getResources().getColor(
				R.color.sign_up_light_gray));
		llGallery.setOnClickListener(null);
		llMessage.setOnClickListener(null);
		llFavorite.setOnClickListener(null);
		llFeed.setOnClickListener(null);
		llAddListing.setOnClickListener(null);
		ivLeftMenu.setOnClickListener(null);
		ivRightMenu.setOnClickListener(null);
		rightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		rightMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_NONE);
		rightMenu.setBehindOffset(screenWidth);
		leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		llGallery.setBackgroundResource(R.drawable.bg_bottom_menu_gallery);
	}

	public void enableBottomLayout() {
		rlCoachMark.setVisibility(View.GONE);
		llBottom.setBackgroundColor(getResources().getColor(
				R.color.sign_up_teal));
		llGallery.setOnClickListener(this);
		llMessage.setOnClickListener(this);
		llFavorite.setOnClickListener(this);
		llFeed.setOnClickListener(this);
		llAddListing.setOnClickListener(this);
		ivLeftMenu.setOnClickListener(this);
		ivRightMenu.setOnClickListener(this);

		rightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		rightMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		// Pass on the activity result to the helper for handling
		Log.i("ACTIVITY_RESULT", requestCode + "--" + resultCode);
		super.onActivityResult(requestCode, resultCode, data);

		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			switch (requestCode) {
			case FacebookConstants.REQUEST_FACEBOOK_SSO:
				if (fhandler != null) {
					fhandler.onActivityResult(requestCode, resultCode, data);
				}
				break;
			case PICK_FROM_FILE:
				try {
					InputStream inputStreams = getContentResolver()
							.openInputStream(data.getData());
					FileOutputStream fileOutputStream = new FileOutputStream(
							currentFile);
					copyStream(inputStreams, fileOutputStream);
					fileOutputStream.close();
					inputStreams.close();
					startCropImage();

				} catch (Exception e) {
					// Log.e(TAG, "Error while creating temp file", e);
				}
				break;

			case REQUEST_CODE_CROP_IMAGE:
				if (resultCode == RESULT_OK) {
					String path = data.getStringExtra(CropImage.IMAGE_PATH);
					if (path == null) {
						return;
					}
					selectImage = BitmapFactory.decodeFile(currentFile
							.getPath());
					selectImage = ImageUtil.getResizedBitmap(selectImage, 300,
							300);
					imgAvatarChange.setImageBitmap(selectImage);
					imgAvatar.setImageBitmap(selectImage);
				}
				break;

			case RC_SIGN_IN:
				if (resultCode == RESULT_OK) {
					if (!mGoogleApiClient.isConnecting()) {
						mGoogleApiClient.connect();
					}
				}
				mIntentInProgress = false;
				break;
			}

		} else {
			// Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	@Override
	public void onClick(View v) {

		getGalleryFragment().closeShareLayout();

		if (v == layoutWrapperYourDetail || v == layoutWrapperYourListing
				|| v == layoutWrapperChangeLocation
				|| v == layoutWrapperSeachLocation
				|| v == layoutWrapperShareSetting || v == layoutWrapperBlocker) {
			// Toast.makeText(self, "aaaaaaaaaaa", Toast.LENGTH_SHORT).show();
			return;
		}

		if (v == getParentView(tvLeftMenuSignup)) {
			gotoActivity(self, SignupActivity.class);
			return;

		}

		if (v == getParentView(tvLeftMenuLogin)) {
			gotoActivity(self, LoginSelectionActivity.class);
			return;
		}

		if (v == getParentView(tvLeftMenuYourDetails)) {
			CURRENT_SLIDER_TAB = TAB_YOUR_DETAIL;
			updateMenuLeftSlider(CURRENT_SLIDER_TAB);
			return;

		}
		if (v == getParentView(tvLeftMenuYourListings)) {
			CURRENT_SLIDER_TAB = TAB_YOUR_LISTING;
			updateMenuLeftSlider(CURRENT_SLIDER_TAB);
			return;
		}

		if (v == getParentView(tvLeftMenuLocation)) {
			CURRENT_SLIDER_TAB = TAB_SEARCH_LOCATION;
			updateMenuLeftSlider(CURRENT_SLIDER_TAB);
			return;
		}
		if (v == getParentView(tvLeftMenuShareSettings)) {
			CURRENT_SLIDER_TAB = TAB_SHARE_SETTINGS;
			updateMenuLeftSlider(CURRENT_SLIDER_TAB);
			return;
		}
		if (v == getParentView(tvLeftMenuBlockMember)) {
			CURRENT_SLIDER_TAB = TAB_BLOCKED_USER;
			updateMenuLeftSlider(CURRENT_SLIDER_TAB);
			return;
		}
		if (v == getParentView(tvLeftMenuLogout)) {
			resetLogout();
			gotoActivity(self, WalkthrouhActivity.class);
			finish();
			return;
		}

		if (v == tvLeftMenuFeedback) {
			if (CURRENT_SLIDER_TAB != TAB_DEFAULT) {
				CURRENT_SLIDER_TAB = TAB_DEFAULT;
				updateActiveMenuLeft(CURRENT_SLIDER_TAB);
				closeSliderLayout(mSllLeftMenu, false);
			}
			sendFeedback();
			return;
		}

		if (v == tvLeftMenuFAQ) {
			if (CURRENT_SLIDER_TAB != TAB_DEFAULT) {
				CURRENT_SLIDER_TAB = TAB_DEFAULT;
				updateActiveMenuLeft(CURRENT_SLIDER_TAB);
				closeSliderLayout(mSllLeftMenu, false);
			}
			showMessageTermAndPrivacy(KEY_FAQ);
			return;
		}

		if (v == tvLeftMenuPolicy) {
			if (CURRENT_SLIDER_TAB != TAB_DEFAULT) {
				CURRENT_SLIDER_TAB = TAB_DEFAULT;
				updateActiveMenuLeft(CURRENT_SLIDER_TAB);
				closeSliderLayout(mSllLeftMenu, false);
			}
			showMessageTermAndPrivacy(KEY_PRIVACY_POLICY);
			return;
		}

		if (v == tvLeftMenuTerm) {
			if (CURRENT_SLIDER_TAB != TAB_DEFAULT) {
				CURRENT_SLIDER_TAB = TAB_DEFAULT;
				updateActiveMenuLeft(CURRENT_SLIDER_TAB);
				closeSliderLayout(mSllLeftMenu, false);
			}
			showMessageTermAndPrivacy(KEY_TERM_OF_USER);
			return;
		}

		if (v == layoutWrapperMenuLeft || v == ivUserAvatar) {
			if (CURRENT_SLIDER_TAB != TAB_DEFAULT) {
				CURRENT_SLIDER_TAB = TAB_DEFAULT;
				updateActiveMenuLeft(CURRENT_SLIDER_TAB);
				closeSliderLayout(mSllLeftMenu, false);
			}
			return;
		}

		if (v == ivLeftMenu) {
			if (rlCoachMark.getVisibility() == View.VISIBLE) {
				rlCoachMark.setVisibility(View.GONE);
			}
			leftMenu.toggle();
			return;
		}

		if (v == ivCloseLeftMenu) {
			if (rlCoachMark.getVisibility() == View.VISIBLE) {
				rlCoachMark.setVisibility(View.GONE);
			}
			leftMenu.toggle();
			return;
		}

		if (v == ivDiamond) {
			Intent intent = new Intent(GlobalValue.INTENT_ACTION_DIAMOND);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
			return;
		}

		if (v == ivRightMenu) {
			if (rlCoachMark.getVisibility() == View.VISIBLE) {
				rlCoachMark.setVisibility(View.GONE);
			}
			if (sllChangeLocation.isOpened()) {
				sllChangeLocation.closeLayer(true);
			} else {
				rightMenu.toggle();
			}
			return;
		}

		if (v == btnFindLocalTreasure || v == llActualCoachMark) {
			rlCoachMark.setVisibility(View.GONE);
			// replaceContentFragment(new GalleryFragment(), "", null);
			getGalleryFragment().action = GlobalValue.KEY_ACION_DISPLAY;
			showFragment(FRAGMENT_GALLERY);
			return;
		}

		if (v == llGallery) {
			if (rlCoachMark.getVisibility() == View.VISIBLE) {
				rlCoachMark.setVisibility(View.GONE);
			}
			if (llWrapperAlertHome.getVisibility() == View.VISIBLE) {
				llWrapperAlertHome.setVisibility(View.GONE);
				restoreLayoutBottom();
			}
			if (currentFragment == FRAGMENT_GALLERY) {
				if (getGalleryFragment().action
						.equals(GlobalValue.KEY_ACION_DISPLAY)) {
					return;
				} else {
					getGalleryFragment().arrItem.clear();
					getGalleryFragment().action = GlobalValue.KEY_ACION_DISPLAY;
				}
			}

			if (currentFragment != FRAGMENT_GALLERY) {
				if (getGalleryFragment().action
						.equals(GlobalValue.KEY_ACION_DISPLAY)) {

				} else {
					getGalleryFragment().arrItem.clear();
					getGalleryFragment().action = GlobalValue.KEY_ACION_DISPLAY;
				}
			}
			showFragment(FRAGMENT_GALLERY);
			return;
		}

		if (v == llMessage) {
			if (rlCoachMark.getVisibility() == View.VISIBLE) {
				rlCoachMark.setVisibility(View.GONE);
			}
			if (!isLoggedIn()) {
				drawLayoutBottomAlert(FRAGMENT_MESSAGE);
				llWrapperAlertHome.setVisibility(View.VISIBLE);
				fragmentSelected = FRAGMENT_MESSAGE;
				tvAlertMessage
						.setText(getString(R.string.message_alert_messaging));
			} else {
				showFragment(FRAGMENT_MESSAGE);
			}
			return;
		}

		if (v == llFavorite) {
			if (rlCoachMark.getVisibility() == View.VISIBLE) {
				rlCoachMark.setVisibility(View.GONE);
			}

			if (!isLoggedIn()) {
				drawLayoutBottomAlert(FRAGMENT_FAVORITE_ITEMS);
				llWrapperAlertHome.setVisibility(View.VISIBLE);
				fragmentSelected = FRAGMENT_FAVORITE_ITEMS;
				tvAlertMessage
						.setText(getString(R.string.message_alert_saved_item));
			} else {
				showFragment(FRAGMENT_FAVORITE_ITEMS);
			}
			return;
		}
		if (v == llFeed) {
			if (rlCoachMark.getVisibility() == View.VISIBLE) {
				rlCoachMark.setVisibility(View.GONE);
			}
			if (!isLoggedIn()) {
				drawLayoutBottomAlert(FRAGMENT_ACTIVITY_FEED);
				llWrapperAlertHome.setVisibility(View.VISIBLE);
				fragmentSelected = FRAGMENT_ACTIVITY_FEED;
				tvAlertMessage.setText(getString(R.string.message_alert_feed));
			} else {
				showFragment(FRAGMENT_ACTIVITY_FEED);
			}
			return;
		}
		if (v == llAddListing) {
			if (rlCoachMark.getVisibility() == View.VISIBLE) {
				rlCoachMark.setVisibility(View.GONE);
			}
			if (!isLoggedIn()) {
				drawLayoutBottomAlert(4);
				llWrapperAlertHome.setVisibility(View.VISIBLE);
				tvAlertMessage
						.setText(getString(R.string.message_alert_add_listing));
				fragmentSelected = FRAGMENT_ADD_LISTING;
				// Change icon add
				llAddListing
						.setBackgroundResource(R.drawable.bg_bottom_menu_add_listing_not_logged_in);
			} else {
				// Check slots if full
				if (GlobalValue.myAccount.getNumberFreeSlotActive() > 0) {
					// Go to AddAListingActivity
					if (isClickOnAddAListing) {
						gotoAddAListingWithAdditionalType();
					} else {
						gotoAddAListingWithDefaultType();
					}
				} else if (GlobalValue.myAccount.getNumberSingleSlotActive() > 0) {
					confirmUseSingleSlots();
				} else {
					// Coding buy new pack here
					showDialogWhenSlotIsFull();
				}
			}
			return;
		}

		if (v == llAlert) {
			llWrapperAlertHome.setVisibility(View.GONE);
			restoreLayoutBottom();
			return;
		}

		// action for update details

		if (v == llMale) {
			if (temAccount != null) {
				temAccount.setMale(true);
			}
			updateGenderUI(temAccount.isMale());
			return;
		}

		if (v == llFemale) {
			if (temAccount != null) {
				temAccount.setMale(false);
			}
			updateGenderUI(temAccount.isMale());
			return;
		}
		if (v == lblChangeLocation) {
			showSliderLayout(mSllLocation);
			return;
		}

		if (v == lblDOB) {
			showDatePickerDialog(birthdayCalendar.get(Calendar.YEAR),
					birthdayCalendar.get(Calendar.MONDAY),
					birthdayCalendar.get(Calendar.DAY_OF_MONTH));
			return;
		}

		if (v == lblChangeAvatar) {
			showDialogChangeAvatar();
			return;
		}

		if (v == llTransparentChangeAvt) {
			hideDialogChangeAvatar();
			return;
		}

		if (v == imgCamera) {
			Bundle bundle = new Bundle();
			bundle.putBoolean("isFrontFacing", true);
			gotoActivity(self, CameraAvataActivity.class, bundle);
			hideDialogChangeAvatar();
			return;
		}

		if (v == imgGallery) {
			openGallery();
			hideDialogChangeAvatar();
			return;
		}

		if (v == lblShowHidePassword) {
			onClickShowPassword();
			return;
		}

		if (v == btnSave) {

			String name = txtYourName.getText().toString();
			String email = txtYourEmail.getText().toString();
			String password = txtPassword.getText().toString();

			// name
			if (name.isEmpty()) {
				txtYourName.setText("");
				txtYourName.setHintTextColor(Color.RED);
				layoutYourName.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				txtYourName.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				txtYourName.setHint(getString(R.string.error_empty_username));
				return;
			} else {
				temAccount.setName(name);
			}
			// save email
			if (email.isEmpty()) {
				txtYourEmail.setText("");
				txtYourEmail.setHintTextColor(Color.RED);
				layoutEmail.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				txtYourEmail.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				txtYourEmail.setHint(getString(R.string.error_empty_email));
				return;
			}
			if (!StringUtility.isEmailValid(email)) {
				txtYourEmail.setText("");
				txtYourEmail.setHintTextColor(Color.RED);
				layoutEmail.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				txtYourEmail.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				txtYourEmail.setHint("");
				showMessageDialog(getString(R.string.error_email_not_valid),
						new DialogListener() {

							@Override
							public void onClose(Dialog dialog) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				return;
			}
			temAccount.setEmail(email);
			// save password
			if (password.isEmpty()) {
				txtPassword.setHintTextColor(Color.RED);
				layoutPassword.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				txtPassword.setHint(getString(R.string.error_empty_password));
				txtPassword.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				return;

			}

			if (password.contains(" ")) {
				txtPassword.setText("");
				txtPassword.setHintTextColor(Color.RED);
				layoutPassword.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				txtPassword.setHint(getString(R.string.error_have_spaces));
				txtPassword.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				return;
			}

			if (password.length() < 6) {
				txtPassword.setText("");
				txtPassword.setHintTextColor(Color.RED);
				layoutPassword.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				txtPassword.setHint("");
				txtPassword.setBackgroundColor(Color
						.parseColor(getString(R.color.bg_color_error_editext)));
				showMessageDialog(
						getString(R.string.error_password_6_characters),
						new DialogListener() {

							@Override
							public void onClose(Dialog dialog) {
								// TODO Auto-generated method stub
								dialog.dismiss();
							}
						});
				return;
			}

			temAccount.setPassword(password);

			if (selectImage != null) {
				uploadProfileAvata();
			} else {
				updateAccountInfo(temAccount, chkReceiveEmail.isChecked());
			}
			return;
		}

		// close your details

		// action for your listing

		if (v == llTransparentLocation) {
			currentSearchLocObject = null;
			closeSliderLayout(mSllLocation, true);
			return;
		}

		if (v == tvEditCategories) {
			if (!isLoggedIn()) {
				tvEditCategories.setBackgroundColor(getResources().getColor(
						R.color.sign_up_teal));
				tvEditCategories.setTextColor(getResources().getColor(
						R.color.white));
				llAlertCategory.setVisibility(View.VISIBLE);
			} else {
				if (rightMenuState == CategoryGroup.STATE_NORMAL) {
					rightMenuState = CategoryGroup.STATE_EDIT;
					onRightMenuEdit();

				} else {
					rightMenuState = CategoryGroup.STATE_NORMAL;
					onRightMenuDoneEditting();
				}
			}
			return;
		}

		if (v == tvChange) {
			sllChangeLocation.openLayer(true);
			initRightMaps();
			return;
		}

		if (v == btnSearch) {
			processSearching();
			return;
		}

		if (v == ivFreeListings) {
			getGalleryFragment().action = GlobalValue.KEY_ACTION_SEARCH;
			getGalleryFragment().searchType = getGalleryFragment().SEARCH_TYPE_ITEM_TYPE;
			getGalleryFragment().setSearchAttribute("", "", "", "",
					Item.TYPE_FREE);

			Intent intent = new Intent(GlobalValue.INTENT_ACTION_SEARCHING);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
			rightMenu.toggle();
			showFragment(FRAGMENT_GALLERY);
			return;
		}

		if (v == ivPremimumListings) {
			getGalleryFragment().action = GlobalValue.KEY_ACTION_SEARCH;
			getGalleryFragment().searchType = getGalleryFragment().SEARCH_TYPE_ITEM_TYPE;
			getGalleryFragment().setSearchAttribute("", "", "", "",
					Item.TYPE_PREMIUM);

			Intent intent = new Intent(GlobalValue.INTENT_ACTION_SEARCHING);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
			rightMenu.toggle();
			showFragment(FRAGMENT_GALLERY);
			return;
		}

		if (v == ivQuickSales) {
			getGalleryFragment().action = GlobalValue.KEY_ACTION_SEARCH;
			getGalleryFragment().searchType = getGalleryFragment().SEARCH_TYPE_ITEM_TYPE;
			getGalleryFragment().setSearchAttribute("", "", "", "",
					Item.TYPE_QUICK);

			Intent intent = new Intent(GlobalValue.INTENT_ACTION_SEARCHING);
			LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
			rightMenu.toggle();
			showFragment(FRAGMENT_GALLERY);
			return;
		}

		if (v == lblSelectPack) {
			showExtraAPackLayout();
			return;
		}

		// action for change location

		if (v == mImgSearchLocation) {
			searchListLocation(mTxtSearchLocation.getText().toString(),
					mLvLocationSearch);
			return;
		}
		if (v == lblCurrentChangeLocation) {
			setOnclickCurrentSearchLocation(lblCurrentChangeLocation);
			return;
		}

		if (v == tvCurrentLocationRight) {
			setOnclickCurrentSearchLocation(tvCurrentLocationRight);
			return;
		}

		if (v == lblSelectLocation) {
			if (temAccount != null) {
				temAccount.setLocation(currentSearchLocObject);
				lblLocation.setText(temAccount.getLocation()
						.getLocationAddress());
				currentSearchLocObject = null;
				closeSliderLayout(mSllLocation, true);
			}
			return;
		}

		if (v == tvDoneRight) {
			sllChangeLocation.closeLayer(true);
			currentSearchLocObject = tempRightLocObj;
			if (currentSearchLocObject != null) {
				pref.putFloatValue(
						TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT,
						(float) currentSearchLocObject.getLatitude());
				pref.putFloatValue(
						TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG,
						(float) currentSearchLocObject.getLongitude());
				pref.putBooleanValue(
						TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_USE_CURRENT,
						false);
				pref.putStringValue(
						TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_NAME,
						currentSearchLocObject.getLocationAddress());
			}
			getGalleryFragment().action = GlobalValue.KEY_ACION_DISPLAY;
			Intent intent = new Intent(GlobalValue.INTENT_ACTION_REFRESH);
			LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(
					intent);
			rightMenu.toggle();
			showFragment(FRAGMENT_GALLERY);
			return;
		}

		if (v == ivSearchLocationRight) {
			if (etRightLocation.getText().toString().trim().length() > 0) {
				searchListLocation(etRightLocation.getText().toString().trim(),
						lvLocationRight);
				return;
			}
		}

		// action for search location

		if (v == btnSearchLocation) {
			searchListLocation(txtSearchLocation.getText().toString(),
					lsvSearchLocation);
			return;
		}

		if (v == lblCurrentSearchLocation) {
			setOnclickCurrentSearchLocation(lblCurrentSearchLocation);
			return;
		}

		// Action for sharing
		if (v == llConnectYourFb) {
			connectYourFb();
			return;
		}
		if (v == llConnectYourGGP) {
			connectYourGGP();
			return;
		}
		if (v == llAlertCategory) {
			llAlertCategory.setVisibility(View.GONE);
			tvEditCategories.setBackgroundColor(getResources().getColor(
					R.color.transparent));
			tvEditCategories.setTextColor(getResources().getColor(
					R.color.sign_up_teal));
		}
	}

	private void confirmUseSingleSlots() {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_confirm_use_single_slots);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		TextView lblTitle = (TextView) dialog.findViewById(R.id.lblTitle);

		lblTitle.setText(getResources().getString(
				R.string.would_you_like_to_use_single_slot));
		HelveticaLightTextView btnNegative = (HelveticaLightTextView) dialog
				.findViewById(R.id.btnCancel);
		btnNegative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		HelveticaLightTextView btnPositive = (HelveticaLightTextView) dialog
				.findViewById(R.id.btnAccept);

		btnPositive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				gotoAddAListingWithAdditionalType();
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void processSearching() {
		hideSoftKeyboard(etSearch);
		if (etSearch.getText().toString().trim().length() > 0) {
			if (pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT) != 0
					&& pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG) != 0) {
				showFragment(FRAGMENT_GALLERY);

				String strSearch = etSearch.getText().toString().trim();
				getGalleryFragment().action = GlobalValue.KEY_ACTION_SEARCH;
				getGalleryFragment().searchType = getGalleryFragment().SEARCH_TYPE_KEYWORD;
				getGalleryFragment().setSearchAttribute("", "", strSearch, "",
						"");

				Intent intent = new Intent(GlobalValue.INTENT_ACTION_SEARCHING);
				LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
			}
			rightMenu.toggle();
		}
	}

	private void relistClick(final int position) {
		if (GlobalValue.myAccount.getNumberFreeSlotActive() > 0) {
			// relistItem(position, Item.SLOT_TYPE_DEFAULT);
			// GlobalValue.arrItems = arrExpiredListings;
			// GlobalValue.currentItemPosition = position;
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("arrItem", arrExpiredListings);
			bundle.putInt("currentPosition", 0);
			gotoActivity(self, ItemDetailsActivity.class);

		} else if (GlobalValue.myAccount.getNumberSingleSlotActive() > 0) {
			// Confirm use single slot
			// final Dialog dialog = new Dialog(self);
			// dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			// dialog.setContentView(R.layout.dialog_confirm_use_single_slots);
			// dialog.getWindow().setBackgroundDrawable(
			// new ColorDrawable(android.graphics.Color.TRANSPARENT));
			//
			// TextView lblTitle = (TextView)
			// dialog.findViewById(R.id.lblTitle);
			//
			// lblTitle.setText(getResources().getString(
			// R.string.would_you_like_to_use_single_slot));
			// HelveticaLightTextView btnNegative = (HelveticaLightTextView)
			// dialog
			// .findViewById(R.id.btnCancel);
			// btnNegative.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// dialog.dismiss();
			// }
			// });
			//
			// HelveticaLightTextView btnPositive = (HelveticaLightTextView)
			// dialog
			// .findViewById(R.id.btnAccept);
			//
			// btnPositive.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// // TODO Auto-generated method stub
			// dialog.dismiss();
			//
			// relistItem(position, Item.SLOT_TYPE_ADDITIONAL);
			// }
			// });
			//
			// dialog.show();
			// GlobalValue.arrItems = arrExpiredListings;
			// GlobalValue.currentItemPosition = position;
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("arrItem", arrExpiredListings);
			bundle.putInt("currentPosition", 0);
			gotoActivity(self, ItemDetailsActivity.class);
		} else {
			// isClickOnRelist = true;
			// relistPosition = position;

			// Coding buy new pack here
			showDialogWhenSlotIsFull();
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

				isClickOnAddAListing = true;

				CURRENT_PURCHASE = Item.ITEM_5_SLOTS;
				purchaseItem(Item.KEY_5_SLOTS, "");

				dialog.dismiss();
			}
		});

		lbl10Slots.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// confirmExtraAPack(lbl10Slots.getText().toString());

				isClickOnAddAListing = true;

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

	private void gotoAddAListingWithAdditionalType() {
		Bundle bundle = new Bundle();
		bundle.putString("slotType", Item.SLOT_TYPE_ADDITIONAL);
		gotoActivity(self, AddAListingActivity.class, bundle);
	}

	private void gotoAddAListingWithDefaultType() {
		Bundle bundle = new Bundle();
		bundle.putString("slotType", Item.SLOT_TYPE_DEFAULT);
		gotoActivity(self, AddAListingActivity.class, bundle);
	}

	private void relistItem(int position, String slotType) {
		ProductModelManager.relistItem(self, arrExpiredListings.get(position)
				.getId(), slotType, true, new ModelManagerListener() {

			@Override
			public void onSuccess(String json) {
				// loadListingsList();
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

	private void connectYourGGP() {
		if (!mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		} else {
			mGoogleApiClient.reconnect();
		}

	}

	private void connectYourFb() {
		fhandler = new FacebookHandle(self, FacebookConstants.FACEBOOK_APP_ID,
				FacebookConstants.PERMISSIONS);
		fhandler.sso(FacebookConstants.REQUEST_FACEBOOK_SSO);
		fhandler.reauth(new AbstractAjaxCallback<JSONObject, FacebookHandle>() {
			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);

				AccountModelManager.shareAccount(self,
						AccountModelManager.PROVIDER_FACEBOOK,
						fhandler.getToken(), true, new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								// TODO
								// Auto-generated
								// method stub
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_FACEBOOK_ACCESS_TOKEN,
										fhandler.getToken());
								openFacebookSession();
								loadShareSettings();

							}

							@Override
							public void onError(int statusCode, String json) {
								if (statusCode == AsyncHttpBase.RESPONSE_ERROR) {
									int errorCode = ParserUtility
											.parseErrorStatusCode(json);
									showSignupSocialErrorMessage(errorCode,
											Account.ACC_FACEBOOK);
									// String message =
									// getString(R.string.error_facebook_existed);
									// showMessageDialog(message,
									// new DialogListener() {
									//
									// @Override
									// public void onClose(
									// Dialog dialog) {
									// // TODO Auto-generated
									// // method stub
									// dialog.dismiss();
									// }
									// });
									return;
								}

								if (statusCode == AsyncHttpBase.AUTHORIZATION) {
									String message = getString(R.string.error_login_invalid);
									showMessageDialog(message,
											new DialogListener() {

												@Override
												public void onClose(
														Dialog dialog) {
													// TODO Auto-generated
													// method stub
													dialog.dismiss();
												}
											});
									return;
								}

							}

							@Override
							public void onError() {
								// TODO
								// Auto-generated
								// method stub

							}
						});
			}
		});
	}

	private void initRightMaps() {
		// Init text for label

		if (rightMap != null) {
			rightMap.clear();
		} else {
			rightMap = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.rightMap)).getMap();
			rightMap.setMyLocationEnabled(true);
			rightMap.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(final LatLng point) {
					// TODO Auto-generated method stub
					rightMap.clear();

					AccountModelManager.getLocationFromGoogle(self,
							point.latitude, point.longitude, false,
							new ModelManagerListener() {

								@Override
								public void onSuccess(String json) {
									// TODO Auto-generated method stub
									tempRightLocObj = ParserUtility
											.parseLocations(json).get(0);
									tempRightLocObj.setLatitude(point.latitude);
									tempRightLocObj
											.setLongitude(point.longitude);
									currentLocation = tempRightLocObj
											.getLocationAddress();
									tvLocation.setText(currentLocation);
									tvLocationRight.setText(currentLocation);
									rightMap.addMarker(
											new MarkerOptions().position(point))
											.setTitle(
													tempRightLocObj
															.getLocationAddress());
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
			});
		}
		LatLng latLng = new LatLng(
				pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
				pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG));

		rightMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

		rightMap.addMarker(new MarkerOptions().position(latLng)).setTitle(
				mLblLocationChanged.getText().toString());

	}

	private void placeRightMapTempLocation(LocationObj obj) {
		if (rightMap != null) {
			rightMap.clear();
		}

		LatLng latLng = new LatLng(obj.getLatitude(), obj.getLongitude());

		rightMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

		rightMap.addMarker(new MarkerOptions().position(latLng)).setTitle(
				mLblLocationChanged.getText().toString());

		String locationName = tempRightLocObj.getLocationAddress();
		tvLocation.setText(locationName);
		tvLocationRight.setText(locationName);
		txtSearchLocation.setText("");
		etRightLocation.setText("");

	}

	private void showExtraAPackLayout() {
		// Show dialog
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_extra_listings);
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

				isClickOnAddAListing = false;

				CURRENT_PURCHASE = Item.ITEM_5_SLOTS;
				showDialogConfirmPurchase(CURRENT_PURCHASE);

				dialog.dismiss();
			}
		});

		lbl10Slots.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// confirmExtraAPack(lbl10Slots.getText().toString());
				isClickOnAddAListing = false;
				CURRENT_PURCHASE = Item.ITEM_10_SLOTS;
				showDialogConfirmPurchase(CURRENT_PURCHASE);
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void showDialogConfirmPurchase(final String currentPurchase) {
		// Show dialog
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_confirm_purchase);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		final TextView lblPurchaseType = (TextView) dialog
				.findViewById(R.id.lblPurchaseType);
		final TextView lblCancel = (HelveticaLightTextView) dialog
				.findViewById(R.id.lblCancel);
		if (currentPurchase.equals(Item.ITEM_5_SLOTS)) {
			lblPurchaseType.setText(getString(R.string.buy_5_slots_confirm));
		} else {
			lblPurchaseType.setText(getString(R.string.buy_10_slots_confirm));
		}

		lblPurchaseType.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// confirmExtraAPack(lbl5Slots.getText().toString());
				isClickOnAddAListing = false;
				if (currentPurchase.equals(Item.ITEM_5_SLOTS)) {
					purchaseItem(Item.KEY_5_SLOTS, "");
				} else {
					purchaseItem(Item.KEY_10_SLOTS, "");
				}
				dialog.dismiss();
			}
		});

		lblCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void getYourListings(ArrayList<Item> arrItem) {

		if (arrFreeListings != null) {
			arrFreeListings.clear();
		} else {
			arrFreeListings = new ArrayList<Item>();
		}
		if (arrActiveListings != null) {
			arrActiveListings.clear();
		} else {
			arrActiveListings = new ArrayList<Item>();
		}
		if (arrExtraListings != null) {
			arrExtraListings.clear();
		} else {
			arrExtraListings = new ArrayList<Item>();
		}
		if (arrExtraActiveListings != null) {
			arrExtraActiveListings.clear();
		} else {
			arrExtraActiveListings = new ArrayList<Item>();
		}
		if (arrExpiredListings != null) {
			arrExpiredListings.clear();
		} else {
			arrExpiredListings = new ArrayList<Item>();
		}
		if (arrGoneListings != null) {
			arrGoneListings.clear();
		} else {
			arrGoneListings = new ArrayList<Item>();
		}

		for (Item item : arrItem) {
			if (item.getState().equals(Item.STATE_GONE)) {
				arrGoneListings.add(item);
			} else if (item.getState().equals(Item.STATE_EXPIRED)) {
				arrExpiredListings.add(item);
			} else {
				// Add free items
				if (item.getSlotType().equals(Item.SLOT_TYPE_DEFAULT)) {
					if (arrFreeListings.size() < GlobalValue.myAccount
							.getNumberFreeSlot()) {
						arrFreeListings.add(item);
						arrActiveListings.add(item);
					}
				} else if (item.getSlotType().equals(Item.SLOT_TYPE_ADDITIONAL)) {
					arrExtraListings.add(item);
					arrExtraActiveListings.add(item);
				}
			}

		}

		// Log.e(TAG, "Extra active size: " + arrExtraListings.size());

		for (int i = 0; i < GlobalValue.myAccount.getNumberSingleSlotActive(); i++) {
			Item it = null;
			arrExtraListings.add(it);
		}

		// Log.e(TAG, "Extra size: " + arrExtraListings.size());

		if (arrFreeListings.size() < GlobalValue.myAccount.getNumberFreeSlot()) {
			int countFreeSlot = GlobalValue.myAccount.getNumberFreeSlot()
					- arrActiveListings.size();
			for (int i = 0; i < countFreeSlot; i++) {
				Item it = null;
				arrFreeListings.add(it);
			}
		}

		// Fill free listings
		activeAdapter = new YourFreeListingItemAdapter(
				(TreasureTrashBaseActivity) self, R.id.grv_free_listings,
				arrFreeListings);
		grvFreeListings.setAdapter(activeAdapter);

		// Fill extra listings
		if (arrExtraListings.size() > 0) {
			// Show extra layout
			llExtraListings.setVisibility(View.VISIBLE);
			lblAddtionalTitle.setVisibility(View.GONE);
			lblMessageAdditonSlot
					.setText(getString(R.string.message_have_ad_slot));
			lblAboveMessageAdditonSlot
					.setText(getString(R.string.message_have_ad_slot));
			// Change pack text
			lblSelectPack.setText(getResources().getString(
					R.string.buy_another_pack));

			// Fill data
			extraAdapter = new YourListingExtraItemAdapter(
					(TreasureTrashBaseActivity) self, R.id.grv_extra_listings,
					arrExtraListings);
			grvExtraListings.setAdapter(extraAdapter);
		} else {
			// Hide extra layout
			llExtraListings.setVisibility(View.GONE);
			lblAddtionalTitle.setVisibility(View.VISIBLE);
			lblAddtionalTitle
					.setText(getString(R.string.buy_a_pack_of_additional_listings));
			// Change pack text
			lblSelectPack.setText(getResources().getString(
					R.string.select_a_pack));
			lblMessageAdditonSlot
					.setText(getString(R.string.message_have_not_ad_slot));
			lblAboveMessageAdditonSlot
					.setText(getString(R.string.message_have_not_ad_slot));
		}

		// Fill expired listings
		expiredAdapter = new YourListingExpiredItemAdapter(
				(TreasureTrashBaseActivity) self, R.id.grv_expired_listings,
				arrExpiredListings);
		grvExpiredListings.setAdapter(expiredAdapter);

		// Fill gone listings
		goneAdapter = new YourListingItemAdapter(
				(TreasureTrashBaseActivity) self, R.id.grv_gone_listings,
				arrGoneListings);
		grvGoneListings.setAdapter(goneAdapter);
	}

	private void loadListingsList() {

		boolean isShowDialog = false;
		if (isRefreshYourListings) {
			isShowDialog = true;
		}
		if (isLoadLeftMenuFirstTime) {
			isShowDialog = false;
			isLoadLeftMenuFirstTime = false;
		}
		ProductModelManager
				.getItemsByCurrentUser(
						self,
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LAT),
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LNG),
						200, 200, isShowDialog, new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								// TODO Auto-generated method stub
								arrYourListing.clear();
								arrYourListing = (ArrayList<Item>) ParserUtility
										.parseListItem(json);
								getYourListings(arrYourListing);
								if (isLoadingyourListing) {
									scrollListing.onRefreshComplete();
									isLoadingyourListing = false;
								}
								if (isRefreshYourListings) {
									isRefreshYourListings = false;
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

	public void getBlockedUserList(boolean isProgress) {
		AccountModelManager.getListBlockedUsers(self, isProgress,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						arrBlockedUSer = ParserUtility
								.parseBlockedUsersFully(json);
						if (arrBlockedUSer.size() > 0) {
							lvBlockedUSer.setVisibility(View.VISIBLE);
							lblNotifyBlockedUSer.setVisibility(View.VISIBLE);
							blockedUserAdapter = new BlockedUserAdapter(
									(TreasureTrashBaseActivity) self,
									R.id.lv_blocked_user, arrBlockedUSer,
									onBlockUnblockListener);
							lvBlockedUSer.setAdapter(blockedUserAdapter);

							lvBlockedUSer.setAdapter(blockedUserAdapter);
							lblNotifyBlockedUSer
									.setText(getString(R.string.tab_the_username_or_icon_to_unblock_user));
						} else {
							lvBlockedUSer.setVisibility(View.VISIBLE);
							lblNotifyBlockedUSer
									.setText(getString(R.string.you_havent_blocked_any_members));
						}

						List<String> arrBlocked = ParserUtility
								.parseBlockedUsers(json);
						List<String> arrBlocker = ParserUtility
								.parseBlocker(json);
						dbHelper.deleteAllBlockerAndBlockedUsers(GlobalValue.myAccount
								.getId());
						for (int i = 0; i < arrBlocked.size(); i++) {
							Log.i("ARR_BLOCKED", arrBlocked.get(i));
							dbHelper.insertBlockedUser(arrBlocked.get(i),
									GlobalValue.myAccount.getId(), true);
						}
						for (int i = 0; i < arrBlocker.size(); i++) {
							dbHelper.insertBlockedUser(arrBlocker.get(i),
									GlobalValue.myAccount.getId(), false);
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

	private void openGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(
				Intent.createChooser(intent,
						getResources().getString(R.string.choose_image_item)),
				PICK_FROM_FILE);
	}

	private void hideDialogChangeAvatar() {
		llTransparentChangeAvt.setVisibility(View.GONE);
	}

	private void showDialogChangeAvatar() {
		llTransparentChangeAvt.setVisibility(View.VISIBLE);
	}

	public void onSignupClick(View v) {
		// DialogUtility.showShortToast(self, "signup-click");
		gotoActivity(self, SignupActivity.class);
		// finish();
	}

	public void onLoginClick(View v) {
		Bundle bundle = new Bundle();
		switch (fragmentSelected) {
		case FRAGMENT_MESSAGE:
			bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
					GlobalValue.KEY_FORCED_LOGIN_ACTION_MESSAGE);
			break;
		case FRAGMENT_FAVORITE_ITEMS:
			bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
					GlobalValue.KEY_FORCED_LOGIN_ACTION_SAVED_ITEM);
			break;
		case FRAGMENT_ACTIVITY_FEED:
			bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
					GlobalValue.KEY_FORCED_LOGIN_ACTION_ACTIVITY_FEED);
			break;
		case FRAGMENT_ADD_LISTING:
			bundle.putString(GlobalValue.FORCED_LOGIN_ACTION,
					GlobalValue.KEY_FORCED_LOGIN_ACTION_ADD_LISTING);
			break;

		default:
			break;
		}

		gotoActivity(self, LoginSelectionActivity.class, bundle);
	}

	public void setTitleCustom(String title) {

	}

	@Override
	public void onBackPressed() {
		// backFragment(null);
		if (mSllLocation.isOpened()) {
			mSllLocation.closeLayer(true);
		} else if (mSllLeftMenu.isOpened()) {
			CURRENT_SLIDER_TAB = TAB_DEFAULT;
			updateActiveMenuLeft(CURRENT_SLIDER_TAB);
			closeSliderLayout(mSllLeftMenu, true);
		} else if (leftMenu.isMenuShowing()) {
			leftMenu.toggle(true);
		} else if (sllChangeLocation.isOpened()) {
			sllChangeLocation.closeLayer(true);
		} else if (rightMenu.isMenuShowing()) {
			rightMenu.toggle(true);
		} else {
			backFragment();
		}

	}

	public void setBottomMenu(int fragmentPosition) {
		for (int i = 0; i < listBottomButtons.size(); i++) {
			if (i == fragmentPosition) {
				listBottomButtons.get(i).setBackgroundResource(
						BOTTOM_SELECTED_BACKGROUND[i]);
			} else {
				listBottomButtons.get(i).setBackgroundResource(
						BOTTOM_NORMAL_BACKGROUND[i]);
			}
			if (totalNewMessages > 0) {
				if (fragmentPosition == HomeActivity.FRAGMENT_MESSAGE
						|| fragmentPosition == HomeActivity.FRAGMENT_MESSAGE_LIST) {
					llMessage
							.setBackgroundResource(R.drawable.bg_bottom_menu_message_new_selected);
				} else {
					llMessage
							.setBackgroundResource(R.drawable.bg_bottom_menu_message_new);
				}

			}
		}
	}

	public void showFragment(int fragmentIndex) {
		if (currentFragment != fragmentIndex
				|| fragmentIndex == FRAGMENT_GALLERY) {
			if (fragmentIndex == FRAGMENT_MESSAGE
					|| fragmentIndex == FRAGMENT_MESSAGE_LIST) {
				rlHeader.setVisibility(View.GONE);
			} else {
				rlHeader.setVisibility(View.VISIBLE);
			}

			FragmentTransaction transaction = fm.beginTransaction();
			// .setCustomAnimations(R.anim.slide_in_left,
			// R.anim.push_left_out);
			// transaction.hide(defaultFragment);
			for (Fragment fragment : arrFragments) {
				transaction.hide(fragment);
			}
			transaction.show(arrFragments.get(fragmentIndex));
			transaction.commit();
			currentFragment = fragmentIndex;
			if (fragmentIndex == FRAGMENT_GALLERY) {
				if (!getGalleryFragment().action
						.equals(GlobalValue.KEY_ACION_DISPLAY)) {
					return;
				}
			}
			stackFragment.push(currentFragment);
		}
	}

	private void drawLayoutBottomAlert(int selected) {
		for (int i = 0; i < listBottomButtons.size(); i++) {
			if (i != selected) {
				listBottomButtons.get(i).setBackgroundResource(
						BOTTOM_SELECTED_BACKGROUND[i]);
			} else {
				listBottomButtons.get(i).setBackgroundResource(
						BOTTOM_NORMAL_BACKGROUND[i]);
			}
		}
	}

	private void restoreLayoutBottom() {
		for (int i = 0; i < listBottomButtons.size(); i++) {

			listBottomButtons.get(i).setBackgroundResource(
					BOTTOM_NORMAL_BACKGROUND[i]);
			listBottomButtons.get(FRAGMENT_GALLERY).setBackgroundResource(
					R.drawable.bg_bottom_menu_gallery_selected);

		}
	}

	private void getCategories() {

		AccountModelManager.getcategories(self, false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {

						pref.putStringValue(
								TreasureTrashSharedPreferences.PREF_ALL_CATEGORIES,
								json);
						initCategoriesMenu();
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

	private void initCategoriesMenu() {
		String jsonCategory = pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ALL_CATEGORIES);
		ArrayList<Category> arrAllCategories = ParserUtility
				.parseCategories(jsonCategory);

		arrCategoryGroups.clear();

		// String strDisabledCategories = "";
		// if (isLoggedIn()) {
		// if (GlobalValue.myAccount != null) {
		// strDisabledCategories = dbHelper
		// .getCategoryPrefByAccount(GlobalValue.myAccount.getId());
		// }
		// }

		// Log.e("DISABLED_CATEGORIES", strDisabledCategories);

		// Get grandparent list category

		for (int i = 0; i < arrAllCategories.size(); i++) {
			if (arrAllCategories.get(i).getParentCategoryId().equals("-1")) {
				CategoryGroup group = new CategoryGroup();
				group.setGrandParent(arrAllCategories.get(i));
				arrCategoryGroups.add(group);

			}
		}

		for (int i = 0; i < arrCategoryGroups.size(); i++) {
			CategoryGroup group = arrCategoryGroups.get(i);
			if (group.getGrandParent().isHidden()) {
				group.setActive(false);
			}

			for (int j = 0; j < arrAllCategories.size(); j++) {
				Category curCategory = arrAllCategories.get(j);
				if (curCategory.getParentCategoryId().equals(
						group.getGrandParent().getId())) {
					group.getArrCategories().add(curCategory);
					for (int k = 0; k < arrAllCategories.size(); k++) {
						if (arrAllCategories.get(k).getParentCategoryId()
								.equals(curCategory.getId())) {
							group.getArrCategories().add(
									arrAllCategories.get(k));
						}
					}
				}
			}
		}

		adapterCategory.notifyDataSetChanged();
		// setListViewHeightBasedOnChildren(lvCategories);

	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		super.init();
		if (GlobalValue.myAccount != null) {
			aq.id(ivLeftMenu).image(GlobalValue.myAccount.getImageUrl(), false,
					true, 0, R.drawable.icon_left_menu_avatar);
		}
	}

	@Override
	public void onReceiveMessageSuccess(String message) {
		// TODO Auto-generated method stub
		super.onReceiveMessageSuccess(message);

		if (CURRENT_SLIDER_TAB == TAB_YOUR_LISTING) {
			getYourListings(arrYourListing);
		}

		Intent intent = new Intent(GlobalValue.INTENT_ACTION_MESSAGE_RECEIVED);
		intent.putExtra(GlobalValue.KEY_MESSAGE_RECEIVED, message);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	@Override
	public void onSendMessageSuccess() {
		super.onSendMessageSuccess();
		Intent intent = new Intent(
				GlobalValue.INTENT_ACTION_MESSAGE_SENT_SUCCESS);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	@Override
	public void onSendMessageError() {
		// TODO Auto-generated method stub
		super.onSendMessageError();
		Intent intent = new Intent(GlobalValue.INTENT_ACTION_MESSAGE_SENT_ERROR);
		LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	private void showSliderLayout(final SlidingLayer sll) {
		if (!sll.isOpened()) {
			if (sll != mSllLocation) {
				leftMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset_zero);
				leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
				sll.setVisibility(View.VISIBLE);
				leftMenu.getCustomViewAbove().setMenuFullScreen();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						sll.openLayer(true);
					}
				}, 10);

			} else {
				sll.setVisibility(View.VISIBLE);
				sll.openLayer(true);
			}
		}
	}

	private void closeSliderLayout(final SlidingLayer sll,
			final boolean isSmooth) {

		if (sll != mSllLocation) {

			leftMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
			leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
			sll.closeLayer(true);

		} else {
			sll.closeLayer(true);
		}

	}

	protected void showProfileDetail() {
		try {
			lblShowHidePassword.setText(R.string.show_password);
			if (temAccount != null) {
				temAccount = null;
			}
			temAccount = (Account) GlobalValue.myAccount.clone();

			birthdayCalendar = Calendar.getInstance();
			initCurrentTemFile();
			// set image :
			if (temAccount.getImageUrl().isEmpty()) {
				aq.id(imgAvatar).image(R.drawable.image_avatar_default);
				aq.id(imgAvatarChange).image(R.drawable.image_avatar_default);
			} else {
				aq.id(imgAvatar).image(temAccount.getImageUrl(), false, true,
						0, R.drawable.image_avatar_default);
				aq.id(imgAvatarChange).image(temAccount.getImageUrl(), false,
						true, 0, R.drawable.image_avatar_default);
			}
			// Set name
			lblYourName.setText(temAccount.getName());
			txtYourName.setText(temAccount.getName());
			txtPassword.setText(temAccount.getPassword());
			// Set location
			lblLocation.setText(temAccount.getLocation().getLocationAddress());

			// Set date of birth
			birthdayCalendar.setTimeInMillis(temAccount.getBirthDay());
			lblDOB.setText(StringUtility.convertTimeStampToDate(
					temAccount.getBirthDay() + "", "dd MMMM yyyy")
					.toUpperCase());
			// Set user name.
			lblUserName.setText(temAccount.getUsername());
			// Set email
			String newMail = pref
					.getStringValue(TreasureTrashSharedPreferences.PREF_NEW_EMAIL);
			String email = newMail.isEmpty() ? temAccount.getEmail() : newMail;
			txtYourEmail.setText(email);

			// Set gender
			updateGenderUI(temAccount.isMale());

		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void updateGenderUI(boolean isMale) {
		if (isMale) {
			llMale.setBackgroundResource(R.drawable.btn_male_active);
			llFemale.setBackgroundResource(R.drawable.btn_female_inactive);
		} else {
			llMale.setBackgroundResource(R.drawable.btn_male_inactive);
			llFemale.setBackgroundResource(R.drawable.btn_female_active);
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isHomeVisible = false;
	}

	private IOnMenuToggle onLeftMenuToggleListener = new IOnMenuToggle() {

		@Override
		public void onOpen() {
			ivLeftMenu.setVisibility(View.GONE);
			ivCloseLeftMenu.setVisibility(View.VISIBLE);

			rightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

			if (bundle != null) {
				if (bundle.containsKey(GlobalValue.KEY_ACTION)) {
					if (bundle.getString(GlobalValue.KEY_ACTION)
							.equalsIgnoreCase(
									GlobalValue.KEY_ACTION_AFTER_DELETE)) {
						CURRENT_SLIDER_TAB = TAB_YOUR_LISTING;
						updateMenuLeftSlider(CURRENT_SLIDER_TAB);
						bundle = null;
					}
				}
			}
		}

		@Override
		public void onClose() {
			// if (ivLeftMenu != null)
			// if (GlobalValue.myAccount != null) {
			// aq.id(ivLeftMenu).image(
			// GlobalValue.myAccount.getImageUrl(), false, true,
			// 0, R.drawable.icon_left_menu_avatar);
			// } else {
			// ivLeftMenu
			// .setImageResource(R.drawable.icon_left_menu_avatar);
			// }
			ivLeftMenu.setVisibility(View.VISIBLE);
			ivCloseLeftMenu.setVisibility(View.GONE);

			leftMenu.getContent().setVisibility(View.VISIBLE);
			rightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

		}
	};

	private IOnMenuToggle onRightMenuToggleListener = new IOnMenuToggle() {

		@Override
		public void onOpen() {
			if (ivRightMenu != null)
				ivRightMenu.setImageResource(R.drawable.icon_header_close);

			if (pref.getStringValue(
					TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_NAME)
					.equals("")) {
				getLocationName(
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG));
			} else {
				tvLocationRight
						.setText(pref
								.getStringValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_NAME));
				tvLocation
						.setText(pref
								.getStringValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_NAME));
			}

		}

		@Override
		public void onClose() {
			if (ivRightMenu != null)
				ivRightMenu.setImageResource(R.drawable.icon_header_menu);

		}
	};

	private IExpandableListView expandableListviewListener = new IExpandableListView() {

		@Override
		public void onExpandCollapseClick(int groupPosition) {
			if (arrCategoryGroups.get(groupPosition).getStatus() == CategoryGroup.STATUS_EXPAND) {
				lvCategories.collapseGroupWithAnimation(groupPosition);
				arrCategoryGroups.get(groupPosition).setStatus(
						CategoryGroup.STATUS_COLLAPSE);
				adapterCategory.notifyDataSetChanged();
				// setListViewHeightBasedOnChildren(lvCategories);

			} else {
				lvCategories.expandGroupWithAnimation(groupPosition);
				for (int i = 0; i < arrCategoryGroups.size(); i++) {
					if (i == groupPosition) {
						arrCategoryGroups.get(i).setStatus(
								CategoryGroup.STATUS_EXPAND);
					} else {
						if (arrCategoryGroups.get(i).getStatus() == CategoryGroup.STATUS_EXPAND) {
							lvCategories.collapseGroup(i);
							arrCategoryGroups.get(i).setStatus(
									CategoryGroup.STATUS_COLLAPSE);
						}

					}
				}
				adapterCategory.notifyDataSetChanged();

				// setListViewHeightBasedOnChildren(lvCategories);
			}

		}
	};

	private void collapseAllCategoriesGroup() {
		for (int i = 0; i < arrCategoryGroups.size(); i++) {
			if (arrCategoryGroups.get(i).getStatus() == CategoryGroup.STATUS_EXPAND) {
				lvCategories.collapseGroup(i);
				arrCategoryGroups.get(i).setStatus(
						CategoryGroup.STATUS_COLLAPSE);
			}
		}
	}

	private void onRightMenuEdit() {

		tvBrowse.setText(getString(R.string.editing_categories));
		tvEditCategories.setText(getString(R.string.done));

		for (int i = 0; i < arrCategoryGroups.size(); i++) {
			arrCategoryGroups.get(i).setState(CategoryGroup.STATE_EDIT);
		}

		adapterCategory.notifyDataSetChanged();
		collapseAllCategoriesGroup();

	}

	private void onRightMenuDoneEditting() {

		// // pref.putStringValue(
		// // TreasureTrashSharedPreferences.PREF_DISABLED_CATEGORIES,
		// // strDisable);
		// dbHelper.insertCategoryPref(strDisable,
		// GlobalValue.myAccount.getId());

		ArrayList<Category> list = new ArrayList<Category>();
		for (CategoryGroup group : arrCategoryGroups) {
			list.add(group.getGrandParent());
		}

		ProductModelManager.updateCategoryStatus(self, list, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						tvBrowse.setText(getString(R.string.or_browse));
						tvEditCategories.setText(getString(R.string.edit));

						for (int i = 0; i < arrCategoryGroups.size(); i++) {
							CategoryGroup group = arrCategoryGroups.get(i);

							group.setState(CategoryGroup.STATE_NORMAL);

						}

						adapterCategory.notifyDataSetChanged();

						Intent intent = new Intent(
								GlobalValue.INTENT_ACTION_REFRESH);
						LocalBroadcastManager.getInstance(HomeActivity.this)
								.sendBroadcast(intent);
						getGalleryFragment().action = GlobalValue.KEY_ACION_DISPLAY;
						showFragment(FRAGMENT_GALLERY);
						stackFragment.push(FRAGMENT_GALLERY);
						// rightMenu.toggle();

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
						// TODO Auto-generated method stub

					}
				});
	}

	private void initMapsChangeLocation() {
		mTxtSearchLocation.setText("");
		// Init text for label
		if (currentSearchLocObject == null)
			currentSearchLocObject = GlobalValue.myAccount.getLocation();

		// display address
		mLblLocationChanged
				.setText(currentSearchLocObject.getLocationAddress());

		if (maps != null) {
			maps.clear();
		} else {
			maps = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.maps)).getMap();
			maps.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(final LatLng point) {
					// TODO Auto-generated method stub
					maps.clear();

					AccountModelManager.getLocationFromGoogle(self,
							point.latitude, point.longitude, false,
							new ModelManagerListener() {

								@Override
								public void onSuccess(String json) {
									// TODO Auto-generated method stub
									currentSearchLocObject = ParserUtility
											.parseLocations(json).get(0);
									currentSearchLocObject
											.setLatitude(point.latitude);
									currentSearchLocObject
											.setLongitude(point.longitude);
									mLblLocationChanged
											.setText(currentSearchLocObject
													.getLocationAddress());
									maps.addMarker(
											new MarkerOptions().position(point))
											.setTitle(
													currentSearchLocObject
															.getLocationAddress());
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
			});
			maps.setMyLocationEnabled(true);
		}

		LatLng latLng = new LatLng(currentSearchLocObject.getLatitude(),
				currentSearchLocObject.getLongitude());
		maps.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
		maps.addMarker(new MarkerOptions().position(latLng)).setTitle(
				mLblLocationChanged.getText().toString());
	}

	private void initMapsSearchLocation() {
		// Init text for label
		txtSearchLocation.setText("");
		// display address
		if (currentSearchLocObject != null)
			lblSearchLocationChanged.setText(currentSearchLocObject
					.getLocationAddress());
		else {
			getLocationName(
					pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
					pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG));
		}

		if (mapSearchLocation != null) {
			mapSearchLocation.clear();
		} else {
			mapSearchLocation = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.mapSearchLocation)).getMap();
			mapSearchLocation.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(final LatLng point) {
					// TODO Auto-generated method stub

					AccountModelManager.getLocationFromGoogle(self,
							point.latitude, point.longitude, false,
							new ModelManagerListener() {

								@Override
								public void onSuccess(String json) {
									// TODO Auto-generated method stub
									currentSearchLocObject = ParserUtility
											.parseLocations(json).get(0);
									mapSearchLocation.clear();
									mapSearchLocation
											.addMarker(
													new MarkerOptions()
															.position(point))
											.setTitle(
													currentSearchLocObject
															.getLocationAddress());
									currentSearchLocObject
											.setLatitude(point.latitude);
									currentSearchLocObject
											.setLongitude(point.longitude);
									lblSearchLocationChanged
											.setText(currentSearchLocObject
													.getLocationAddress());

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
			});
			mapSearchLocation.setMyLocationEnabled(true);
		}

		LatLng latLng = null;
		if (currentSearchLocObject != null) {
			latLng = new LatLng(currentSearchLocObject.getLatitude(),
					currentSearchLocObject.getLongitude());
		} else {
			latLng = new LatLng(
					pref.getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LAT),
					pref.getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LNG));
		}
		mapSearchLocation.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,
				16));
		mapSearchLocation.addMarker(new MarkerOptions().position(latLng))
				.setTitle(lblSearchLocationChanged.getText().toString());
	}

	private void searchListLocation(String input, final ListView lv) {
		if (!input.trim().isEmpty()) {
			String url = "http://maps.google.com/maps/api/geocode/json?address="
					+ input.trim() + "&sensor=false";
			if (url.contains(" ")) {
				url = url.replace(" ", "%20");
			}
			AccountModelManager.getLocationBySearching(self, url, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							lv.setVisibility(View.VISIBLE);
							mArrLocationSearch = ParserUtility
									.parseLocations(json);
							mLocationAdapter = new LocationSearchAdapter(self,
									mArrLocationSearch);
							lv.setAdapter(mLocationAdapter);
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
	}

	private void showDatePickerDialog(int year, int month, int day) {

		DatePickerDialog dpd = new DatePickerDialog(this,
				android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// monthOfYear++;
						// dayOfMonth++;
						birthdayCalendar.set(year, monthOfYear, dayOfMonth);
						temAccount.setBirthDay(birthdayCalendar
								.getTimeInMillis());
						lblDOB.setText(StringUtility.convertTimeStampToDate(
								temAccount.getBirthDay() + "", "dd MMMM yyyy")
								.toUpperCase());
					}
				}, year, month, day);
		dpd.getWindow().getAttributes().gravity = Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL;
		dpd.show();
	}

	private void getAccountInfo() {
		// Set data
		boolean isShowDialog = false;
		if (isRefreshYourListings) {
			isShowDialog = true;
		}
		AccountModelManager.getAccountInfo(self, isShowDialog,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// Log.e(TAG, "JSon Profile: " + json);

						pref.putStringValue(
								TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON,
								json);

						GlobalValue.myAccount = ParserUtility.parseAccount(
								json, pref);

						loadListingsList();
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

	private void updateAccountInfo(final Account acc,
			boolean isReceiveNewsLetter) {

		if (!acc.getLocation().equals(GlobalValue.myAccount.getLocation())) {
			updateLocation(acc.getLocation());
		}

		AccountModelManager.updateProfile(this, acc, isReceiveNewsLetter, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						pref.putStringValue(
								TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON,
								json);
						GlobalValue.myAccount = ParserUtility.parseAccount(
								json, pref);
						isShowConfirmPassword = false;
						showMessageUpdatedSuccessEmail(acc);
						checkLoginUI();
						showProfileDetail();
					}

					@Override
					public void onError(int statusCode, String json) {
						// TODO Auto-generated method stub
						int errorCode = ParserUtility
								.parseErrorStatusCode(json);
						showErrorMessage(errorCode);
						isShowConfirmPassword = false;

					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub
						isShowConfirmPassword = false;
					}
				});

	}

	private void showMessageUpdatedSuccessEmail(Account acc) {
		if (!acc.getEmail().equals(GlobalValue.myAccount.getEmail())) {
			pref.putStringValue(TreasureTrashSharedPreferences.PREF_NEW_EMAIL,
					acc.getEmail());
			showMessageDialog(getString(R.string.updated_email_successfull),
					new DialogListener() {

						@Override
						public void onClose(Dialog dialog) {
							// TODO Auto-generated method stub

							dialog.dismiss();
						}
					});
		}
	}

	private void showErrorMessage(int errorCode) {
		if (errorCode == StatusBackend.EMAIL_EXISTED) {
			String message = getString(R.string.error_sign_up_email_existed_account);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}

		if (errorCode == StatusBackend.USER_EXISTED) {
			String message = getString(R.string.error_sign_up_username_existed);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}
		if (errorCode == StatusBackend.URL_INVALID) {
			String message = getString(R.string.error_default_message);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}
		if (errorCode == StatusBackend.USER_IS_LOCKED) {
			String message = getString(R.string.error_sign_up_username_existed);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}
		if (errorCode == StatusBackend.EXTERNAL_USER_EXISTING) {
			String message = getString(R.string.error_sign_up_email_existed_account);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}

	}

	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	private void startCropImage() {

		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, currentFile.getPath());
		intent.putExtra(CropImage.SCALE, true);
		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 3);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	private void uploadProfileAvata() {

		AccountModelManager.updateAvatar(this, currentFile, false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// Log.e(TAG, "Upload image: " + json);
						selectImage = null;
						updateAccountInfo(temAccount,
								chkReceiveEmail.isChecked());
						selectImage = null;

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

	private void initCurrentTemFile() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			currentFile = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE_NAME);
		} else {
			currentFile = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
		}
	}

	private void onClickShowPassword() {
		if (lblShowHidePassword
				.getText()
				.toString()
				.equalsIgnoreCase(
						getResources().getString(R.string.show_password))) {
			txtPassword.setTransformationMethod(null);
			lblShowHidePassword.setText(getResources().getString(
					R.string.hide_password));
		} else {
			txtPassword
					.setTransformationMethod(new PasswordTransformationMethod());
			lblShowHidePassword.setText(getResources().getString(
					R.string.show_password));
		}
	}

	private void updateLocation(final LocationObj location) {
		AccountModelManager.updateLocation(this, location, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						// Log.e("Home activity", "eee - update location -"
						// + location.getLocationAddress());
						GlobalValue.myAccount.setLocation(location);
						checkLoginUI();
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

	private void sendPurchaseToServer(String productName, String receipt,
			final String transactionId) {
		ProductModelManager.postAppProduct(self, productName, receipt,
				transactionId, true, new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {

						getAccountInfo();

						// Show extra layout
						if (llExtraListings.getVisibility() == View.GONE) {
							llExtraListings.setVisibility(View.VISIBLE);
						}

						if (isClickOnAddAListing) {
							isClickOnAddAListing = false;
							gotoAddAListingWithAdditionalType();
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

	private void setOnclickCurrentSearchLocation(final View view) {
		isChangedSearchLocation = true;
		AccountModelManager
				.getLocationFromGoogle(
						self,
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LAT),
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LNG),
						true, new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								// TODO Auto-generated method stub
								ArrayList<LocationObj> arrLocations = ParserUtility
										.parseLocations(json);
								if (arrLocations.size() > 0)
									currentSearchLocObject = arrLocations
											.get(0);

								currentSearchLocObject.setLatitude(pref
										.getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LAT));
								currentSearchLocObject.setLongitude(pref
										.getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LNG));

								pref.putFloatValue(
										TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT,
										pref.getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LAT));

								pref.putFloatValue(
										TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG,
										pref.getFloatValue(TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LNG));
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_NAME,
										currentSearchLocObject
												.getLocationAddress());

								if (CURRENT_SLIDER_TAB == TAB_SEARCH_LOCATION)
									initMapsSearchLocation();
								if (CURRENT_SLIDER_TAB == TAB_YOUR_DETAIL
										&& mSllLocation.isOpened()) {
									initMapsChangeLocation();
								}
								if (view == tvCurrentLocationRight) {
									tvLocation.setText(currentSearchLocObject
											.getLocationAddress());
									tvLocationRight
											.setText(currentSearchLocObject
													.getLocationAddress());
									initRightMaps();
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

	private void sendFeedback() {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_EMAIL,
				new String[] { getString(R.string.mail_feedback) });
		// need this to prompts email client only
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}

	private void loadShareSettings() {
		AccountModelManager.getSocialInfo(self, false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						arrSocialAccounts = ParserUtility
								.parseArrSocialAccounts(json);
						updateUIShareSetting(arrSocialAccounts);

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

	private void updateUIShareSetting(ArrayList<SocialAccount> arrSocialAccount) {

		SocialAccount currentSocial = null;
		boolean isExistFacebook = false, isExistGoogle = false;

		for (int i = 0; i < arrSocialAccount.size(); i++) {
			currentSocial = arrSocialAccount.get(i);
			if (currentSocial.getProvider().equalsIgnoreCase(
					SocialAccount.PROVIDER_FACEBOOK)) {
				imgFacebookShare.setImageResource(R.drawable.ic_facebook_teal);
				lblFaceBookShare.setText(currentSocial.getName());
				lblFaceBookShare.setTextColor(Color
						.parseColor(getString(R.color.sign_up_teal)));
				llConnectYourFb.setOnClickListener(null);
				isExistFacebook = true;
			} else if (currentSocial.getProvider().equalsIgnoreCase(
					SocialAccount.PROVIDER_GOOGLE)) {
				imgGoogleShare.setImageResource(R.drawable.ic_google_plus_teal);
				lblGoogleShare.setText(currentSocial.getName());
				lblGoogleShare.setTextColor(Color
						.parseColor(getString(R.color.sign_up_teal)));
				llConnectYourGGP.setOnClickListener(null);
				isExistGoogle = true;
			}
		}

		if (!isExistFacebook) {
			imgFacebookShare.setImageResource(R.drawable.ic_facebook_grey);
			lblFaceBookShare.setText(getString(R.string.connect_your_account));
			lblFaceBookShare.setTextColor(Color
					.parseColor(getString(R.color.gray_share_setting)));
			llConnectYourFb.setOnClickListener(this);
		}

		if (!isExistGoogle) {
			imgGoogleShare.setImageResource(R.drawable.ic_google_plus_gray);
			lblGoogleShare.setText(getString(R.string.connect_your_account));
			lblGoogleShare.setTextColor(Color
					.parseColor(getString(R.color.gray_share_setting)));
			llConnectYourGGP.setOnClickListener(this);
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// TODO Auto-generated method stub
		super.onConnected(connectionHint);
		String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
		new Authenticate(email).execute();
	}

	private class Authenticate extends AsyncTask<String, String, String> {
		ProgressHUD pDialog;
		String mEmail;
		String scope = "oauth2:" + Scopes.PLUS_LOGIN;

		public Authenticate(String email) {
			this.mEmail = email;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = ProgressHUD.show(context, "", true, false,
					new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							// TODO Auto-generated method stub

						}
					});
		}

		@Override
		protected void onPostExecute(String token) {
			// clear default account
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			pDialog.dismiss();
			if (token != null) {
				AccountModelManager.shareAccount(self,
						AccountModelManager.PROVIDER_GOOGLE, token, true,
						new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								// TODO Auto-generated method stub
								Toast.makeText(self,
										"share google successfully !",
										Toast.LENGTH_SHORT).show();
								loadShareSettings();
							}

							@Override
							public void onError(int statusCode, String json) {

								Log.e("Share google", "Status code : "
										+ statusCode);
								// Log.e("Share google", "json : " + json);
								if (statusCode == AsyncHttpBase.RESPONSE_ERROR) {
									// String message =
									// getString(R.string.error_google_existed);
									// showMessageDialog(message,
									// new DialogListener() {
									//
									// @Override
									// public void onClose(
									// Dialog dialog) {
									// // TODO Auto-generated
									// // method stub
									// dialog.dismiss();
									// }
									// });
									int errorCode = ParserUtility
											.parseErrorStatusCode(json);
									showSignupSocialErrorMessage(errorCode,
											Account.ACC_GOOGLE);
									return;
								}

								if (statusCode == AsyncHttpBase.AUTHORIZATION) {
									String message = getString(R.string.error_login_invalid);
									showMessageDialog(message,
											new DialogListener() {

												@Override
												public void onClose(
														Dialog dialog) {
													// TODO Auto-generated
													// method stub
													dialog.dismiss();
												}
											});
									return;
								}

							}

							@Override
							public void onError() {
								// TODO Auto-generated method stub

							}
						});
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String token = null;
			try {
				token = GoogleAuthUtil.getToken(self, mEmail, scope);
			} catch (IOException transientEx) {
				// Network or server error, try later
				Log.e("IOException", transientEx.toString());
			} catch (UserRecoverableAuthException e) {
				startActivityForResult(e.getIntent(), 1001);
				Log.e("AuthException", e.toString());
			} catch (GoogleAuthException authEx) {
				// The call is not ever expected to succeed
				// assuming you have already verified that
				// Google Play services is installed.
				Log.e("GoogleAuthException", authEx.toString());
			}
			return token;
		}

	}

	// class GetCurrentLocationNameTask extends AsyncTask<Double, Void, String>
	// {
	//
	// @Override
	// protected String doInBackground(Double... params) {
	// // TODO Auto-generated method stub
	// try {
	//
	// Double currentLat = params[0];
	// Double currentLng = params[1];
	// String location = gpsTracker.getName(currentLat, currentLng);
	// return location;
	// } catch (Exception e) {
	// e.printStackTrace();
	// return null;
	// }
	// }
	//
	// @Override
	// protected void onPostExecute(String result) {
	// // TODO Auto-generated method stub
	// super.onPostExecute(result);
	// currentLocation = result;
	// if (currentLocation != null) {
	// pref.putStringValue(
	// TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_NAME,
	// currentLocation);
	// tvLocationRight.setText(currentLocation);
	// tvLocation.setText(currentLocation);
	// lblSearchLocationChanged.setText(currentLocation);
	// }
	//
	// }
	//
	// }

	private void getLocationName(double lat, double lng) {
		AccountModelManager.getLocationFromGoogle(self, lat, lng, false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {

						LocationObj obj = ParserUtility.parseLocations(json)
								.get(0);
						currentLocation = obj.getLocationAddress();
						pref.putStringValue(
								TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_NAME,
								currentLocation);
						tvLocationRight.setText(currentLocation);
						tvLocation.setText(currentLocation);
						lblSearchLocationChanged.setText(currentLocation);

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

	private void showConfirmChangePassword() {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_custom_dialog);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		TextView lblTitle = (TextView) dialog.findViewById(R.id.lblTitle);
		TextView lblContent = (TextView) dialog.findViewById(R.id.lblContent);
		String password = txtPassword.getText().toString();

		lblTitle.setText("You are about to change your password to " + password
				+ ", is this correct?");
		lblContent.setVisibility(View.GONE);
		TextView btnNegative = (TextView) dialog.findViewById(R.id.btnNegative);
		btnNegative.setText(getResources().getString(R.string.cancel));
		btnNegative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				txtPassword.setText(GlobalValue.myAccount.getPassword());
				isShowConfirmPassword = false;
				dialog.dismiss();
			}
		});

		TextView btnPositive = (TextView) dialog.findViewById(R.id.btnPositive);
		btnPositive.setText("Accept");

		btnPositive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isShowConfirmPassword = true;
				dialog.dismiss();
			}
		});

		dialog.show();
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

	@Override
	public void onPubnubServiceConnected() {
		super.onPubnubServiceConnected();
		if (bundle != null && !isGetHistory) {
			if (bundle.containsKey(GlobalValue.KEY_ACTION)) {
				if (bundle.getString(GlobalValue.KEY_ACTION).equals(
						GlobalValue.KEY_ACTION_AFTER_LOGIN)) {
					getAccountInfo();
					isGetHistory = true;
					long lastTimeReceivedMessage = pref
							.getLongValue(TreasureTrashSharedPreferences.PREF_LAST_TIME_RECEIVED_MESSAGE
									+ pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID));
					mPubnubService
							.getHistoryByChannel(
									pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID),
									lastTimeReceivedMessage);
				}
			}
		}

	}

	@Override
	public void onPubnubServiceDisconnected() {
		super.onPubnubServiceDisconnected();
	}

	public void refreshToken() {
		checkLoginUI();

		if (AppUtil.isAccessTokenExpired(self)) {
			AccountModelManager.refreshToken(self, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							try {
								bundle = null;
								JSONObject obj = new JSONObject(json);
								String accessToken = "Bearer "
										+ obj.getString("access_token");
								String refreshToken = obj
										.getString("refresh_token");
								int expires_in = obj.getInt("expires_in");
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN,
										accessToken);
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_REFRESH_TOKEN,
										refreshToken);
								long currentTime = Calendar.getInstance()
										.getTimeInMillis() / 1000;
								pref.putLongValue(
										TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME,
										currentTime + expires_in);

								getInitAccountInfo();
								bundle = null;
								getGalleryFragment().loadAllItems();

							} catch (Exception e) {
								e.printStackTrace();
							}

						}

						@Override
						public void onError(int statusCode, String json) {
							showMessageDialog(
									getString(R.string.message_alert_general_error),
									new DialogListener() {

										@Override
										public void onClose(Dialog dialog) {
											// TODO Auto-generated method stub
											// resetLogout();
											Bundle bundle = new Bundle();
											bundle.putString(
													GlobalValue.KEY_ACTION,
													GlobalValue.KEY_ACTION_REFRESH_TOKEN_FAILED);
											gotoActivity(self,
													WalkthrouhActivity.class,
													bundle);
											finish();
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
											// resetLogout();
											Bundle bundle = new Bundle();
											bundle.putString(
													GlobalValue.KEY_ACTION,
													GlobalValue.KEY_ACTION_REFRESH_TOKEN_FAILED);
											gotoActivity(self,
													WalkthrouhActivity.class,
													bundle);
											finish();
										}
									});

						}
					});
		} else {
			getInitAccountInfo();
			bundle = null;
			getGalleryFragment().loadAllItems();
		}
	}

	public void getInitAccountInfo() {
		// Set data
		// AccountModelManager.getAccountInfo(self, true,
		// new ModelManagerListener() {
		//
		// @Override
		// public void onSuccess(String json) {
		// Log.e(TAG, "JSon Profile: " + json);
		//
		// pref.putStringValue(
		// TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON,
		// json);
		// json);
		// GlobalValue.myAccount = ParserUtility.parseAccount(
		// json, pref);
		// // init();

		String accountJson = pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON);
		GlobalValue.myAccount = ParserUtility.parseAccount(accountJson, pref);
		initNormal();

		// }
		//
		// @Override
		// public void onError(int statusCode, String json) {
		// // TODO Auto-generated method stub
		//
		// }
		//
		// @Override
		// public void onError() {
		// // TODO Auto-generated method stub
		//
		// }
		// });
	}

	private void showExitPopup() {
		new CustomDialog(self, getString(R.string.message_exit_app),
				getString(R.string.message_exit_content),
				getString(R.string.text_yes), getString(R.string.text_no),
				new OnCustomDialogClickListener() {

					@Override
					public void onYes() {
						resetExit();
						finish();
					}

					@Override
					public void onNo() {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNeutral() {

					}
				}).show();
	}

	private void backFragment() {
		Log.i("STACK_FRAGMENT", stackFragment.size() + "");
		if (stackFragment.size() <= 1) {
			showExitPopup();
		} else {
			stackFragment.pop();
			int previousFragment = stackFragment.pop();
			showFragment(previousFragment);
		}
	}

	@Override
	public void onBlockUnblock(String userId, String blockId, boolean isBlock) {
		Intent intent = new Intent(GlobalValue.INTENT_ACTION_BLOCK_UNBLOCK);
		intent.putExtra(PubnubService.KEY_USER_ID, userId);
		intent.putExtra(PubnubService.KEY_BLOCK_ID, blockId);
		intent.putExtra(PubnubService.KEY_IS_BLOCK, isBlock);
		LocalBroadcastManager.getInstance(HomeActivity.this).sendBroadcast(
				intent);
	}

	@Override
	public void onReceiveItemExpired(String message) {
		// TODO Auto-generated method stub

		new CustomDialog(self, message, "", getString(R.string.relist_item),
				getString(R.string.cancel), new OnCustomDialogClickListener() {

					@Override
					public void onYes() {
						leftMenu.showMenu(true);
						CURRENT_SLIDER_TAB = TAB_YOUR_LISTING;
						updateMenuLeftSlider(CURRENT_SLIDER_TAB);
					}

					@Override
					public void onNo() {

					}

					@Override
					public void onNeutral() {

					}
				}).show();
	}

	@Override
	public void gotoTermOfUse() {
		// TODO Auto-generated method stub
		super.gotoTermOfUse();
		showMessageTermAndPrivacy(KEY_TERM_OF_USER);
	}

	IOnBlockUnblock onBlockUnblockListener = new IOnBlockUnblock() {

		@Override
		public void onUnblock(int position) {
			arrBlockedUSer.remove(position);
			blockedUserAdapter.notifyDataSetChanged();
			if (arrBlockedUSer.size() == 0) {
				lblNotifyBlockedUSer
						.setText(getString(R.string.you_havent_blocked_any_members));
			} else {
				lblNotifyBlockedUSer
						.setText(getString(R.string.tab_the_username_or_icon_to_unblock_user));
			}

		}
	};

	private void loadItem(String itemID) {
		ProductModelManager
				.getItemDetails(
						self,
						itemID,
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG),
						screenWidth, screenWidth, true,
						new ModelManagerListener() {

							@Override
							public void onSuccess(final String json) {

								Item item = ParserUtility.parseItem(json);
								DialogUtility.showProgressDialog(self);
								aq.id(ivDiamond).image(item.getImage(), false,
										true, 0, 0, new BitmapAjaxCallback() {
											@Override
											protected void callback(String url,
													ImageView iv, Bitmap bm,
													AjaxStatus status) {
												if (bm != null) {
													DialogUtility
															.closeProgressDialog();
													Bundle bundle = new Bundle();
													bundle.putString(
															GlobalValue.KEY_ACTION_ITEM_DETAIL,
															GlobalValue.KEY_ACTION_FROM_EMAIL);
													bundle.putString(
															GlobalValue.KEY_ITEM_ID,
															json);
													gotoActivity(
															self,
															ItemDetailsActivity.class,
															bundle);
													finish();

												}
											}
										});

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

	public void showBottomLayout(boolean show) {
		if (show) {
			llBottom.setVisibility(View.VISIBLE);
		} else {
			llBottom.setVisibility(View.GONE);
		}
	}

}
