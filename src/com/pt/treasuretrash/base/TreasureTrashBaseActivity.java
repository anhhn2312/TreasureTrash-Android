package com.pt.treasuretrash.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.adapter.BlockedUserAdapter;
import com.pt.treasuretrash.adapter.LocationSearchAdapter;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.databasehelper.TreasureTrashDbHelper;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.modelmanager.ProductModelManager;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.pulltorefresh.library.PullToRefreshScrollView;
import com.pt.treasuretrash.slidingmenu.SlidingMenu;
import com.pt.treasuretrash.slidingmenu.SlidingMenu.IOnMenuToggle;
import com.pt.treasuretrash.utility.StringUtility;
import com.pt.treasuretrash.widget.AnimatedExpandableListView;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.AutoFillHeightGridView;
import com.pt.treasuretrash.widget.HelveticaLightTextView;
import com.pt.treasuretrash.widget.SlidingLayer;
import com.pt.treasuretrash.widget.SlidingLayer.OnInteractListener;

public class TreasureTrashBaseActivity extends FragmentActivity {

	public static final String KEY_TERM_OF_USER = "ttaTermOfUse";
	public static final String KEY_PRIVACY_POLICY = "ttaPrivacyPolicy";
	public static final String KEY_FAQ = "ttaFAQ";
	public String TAG;
	public Activity self;
	protected Account myAccount;
	public TreasureTrashDbHelper dbHelper;
	public int IMAGE_SIZE;
	public int IMAGE_SMALL_SIZE;
	public Handler handler;

	protected ProgressDialog progressDialog;
	protected LinearLayout userStatusLayout;
	protected Context context;
	public TreasureTrashSharedPreferences pref;

	public AutoBgButton btnMenu, btnTopBack;
	protected TextView lblsingername;
	public ProgressBar userSpinner;
	public SlidingMenu leftMenu, rightMenu;
	public SlidingLayer slidingLayer1;
	public AnimatedExpandableListView lvCategories;

	public int screenWidth, screenHeight;
	boolean isSendingMessage;

	// Left Menu
	public HelveticaLightTextView tvLeftMenuSignup, tvLeftMenuLogin,
			tvLeftMenuYourDetails, tvLeftMenuYourListings, tvLeftMenuLocation,
			tvLeftMenuShareSettings, tvLeftMenuBlockMember, tvLeftMenuLogout,
			tvLeftMenuFeedback, tvLeftMenuFAQ, tvLeftMenuPolicy,
			tvLeftMenuTerm, tvLeftMenuName, tvLeftMenuAddress,
			lblCurrentChangeLocation, lblCurrentSearchLocation;

	public LinearLayout layoutWrapperMenuLeft, llUserName,
			llTransparentLocation, llTransparentMenuLeft;

	public ImageView ivUserAvatar, ivLeftMenu, ivMenu, ivLeftMenuYourDetails,
			ivLeftMenuYourListings, ivLeftMenuLocation,
			ivLeftMenuShareSettings, ivLeftMenuBlockMember, ivLeftMenuLogout;

	public AutoBgButton btnEditProfile;

	// Declare sliding layer
	public SlidingLayer mSllLocation, mSllLeftMenu;

	// Layout your detail
	public HelveticaLightTextView lblYourName, lblLocation, lblChangeLocation,
			lblDOB, lblUserName, lblChangeAvatar, lblShowHidePassword,
			lblSelectLocation;
	public EditText txtYourName, txtYourEmail, txtPassword;
	public CheckBox chkReceiveEmail;
	public TextView btnSave;
	public LinearLayout llMale, llFemale, llTransparentChangeAvt;
	public LinearLayout layoutYourName, layoutEmail, layoutPassword;
	public ImageView imgAvatarChange, imgAvatar, imgCamera, imgGallery;

	// Layout listings
	public AutoFillHeightGridView grvFreeListings, grvExpiredListings,
			grvGoneListings, grvExtraListings;
	public PullToRefreshScrollView scrollListing;
	public HelveticaLightTextView lblSelectPack;
	public LinearLayout llExtraListings;
	public HelveticaLightTextView lblMessageAdditonSlot, lblAddtionalTitle,
			lblAboveMessageAdditonSlot;

	// Layout location
	public HelveticaLightTextView mLblLocationChanged;
	public LocationObj currentSearchLocObject;
	public GoogleMap maps;
	public EditText mTxtSearchLocation;
	public ListView mLvLocationSearch;
	public ImageView mImgSearchLocation;
	public ArrayList<LocationObj> mArrLocationSearch;
	public LocationSearchAdapter mLocationAdapter;

	// layout search location :
	public HelveticaLightTextView lblSearchLocationChanged;
	public GoogleMap mapSearchLocation;
	public EditText txtSearchLocation;
	public ListView lsvSearchLocation;
	public ImageView btnSearchLocation;

	public LinearLayout llConnectYourFb, llConnectYourGGP;
	public LinearLayout layoutWrapperShareSetting, layoutWrapperYourDetail,
			layoutWrapperYourListing, layoutWrapperChangeLocation,
			layoutWrapperSeachLocation, layoutWrapperBlocker;

	// Layout blocked user
	public ArrayList<Account> arrBlockedUSer;
	public BlockedUserAdapter blockedUserAdapter;
	public ListView lvBlockedUSer;
	public HelveticaLightTextView lblNotifyBlockedUSer;

	public List<View> menuMarkers = new ArrayList<View>();

	// layout top bar:
	public AutoBgButton btnSearch;
	public EditText etSearch;

	public interface DialogListener {
		public void onClose(Dialog dialog);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		self = this;
		context = this;
		handler = new Handler();
		TAG = self.getClass().getSimpleName();
		dbHelper = new TreasureTrashDbHelper(self);
		pref = new TreasureTrashSharedPreferences(self);
		// Init pref object.

		// initMenuMarkers();

		if (isLoggedIn()) {
			GlobalValue.myAccount = ParserUtility
					.parseAccount(
							pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON),
							pref);
		}

		registerBaseActivityReceiver();

	}

	protected void initLeftMenu(IOnMenuToggle listener) {
		leftMenu = new SlidingMenu(self, listener);
		leftMenu.setMenu(R.layout.menu_sliding_left);
		leftMenu.setMode(SlidingMenu.LEFT);
		leftMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		leftMenu.setShadowWidthRes(R.dimen.shadow_width);
		leftMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		leftMenu.setFadeDegree(0.5f);

		// slidingLayer1 =
		// (SlidingLayer)leftMenu.getMenu().findViewById(R.id.slidingLayer1);
		// LayoutParams rlp = (LayoutParams) slidingLayer1.getLayoutParams();
		// rlp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		// // rlp.width = 300;
		// // rlp.height = LayoutParams.MATCH_PARENT;
		// slidingLayer1.setLayoutParams(rlp);
		ivUserAvatar = (ImageView) leftMenu.findViewById(R.id.ivAvatar);
		layoutWrapperMenuLeft = (LinearLayout) leftMenu
				.findViewById(R.id.layoutWrapperMenuLeft);
		tvLeftMenuSignup = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvMenuSignup);
		tvLeftMenuLogin = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvMenuLogin);
		tvLeftMenuYourDetails = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvMenuYourDetails);
		tvLeftMenuYourListings = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvMenuYourListings);
		tvLeftMenuLocation = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvMenuLocation);
		tvLeftMenuShareSettings = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvMenuShareSettings);
		tvLeftMenuBlockMember = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvMenuBlockMember);
		tvLeftMenuLogout = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvMenuLogout);
		tvLeftMenuFeedback = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvFeedback);
		tvLeftMenuFAQ = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvFaq);
		tvLeftMenuPolicy = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvPrivacy);
		tvLeftMenuTerm = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvTerm);
		tvLeftMenuName = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvName);
		tvLeftMenuAddress = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.tvAddress);

		ivLeftMenuYourDetails = (ImageView) leftMenu
				.findViewById(R.id.ivMenuYourDetails);
		ivLeftMenuYourListings = (ImageView) leftMenu
				.findViewById(R.id.ivMenuYourListings);
		ivLeftMenuLocation = (ImageView) leftMenu
				.findViewById(R.id.ivMenuLocation);
		ivLeftMenuShareSettings = (ImageView) leftMenu
				.findViewById(R.id.ivMenuShareSettings);
		ivLeftMenuBlockMember = (ImageView) leftMenu
				.findViewById(R.id.ivMenuBlockMember);
		ivLeftMenuLogout = (ImageView) leftMenu.findViewById(R.id.ivMenuLogout);

		lvCategories = (AnimatedExpandableListView) leftMenu
				.findViewById(R.id.lvCategories);
		// ===================================================================================
		// sliding layer menu left
		mSllLeftMenu = (SlidingLayer) leftMenu.findViewById(R.id.sll_left_menu);
		mSllLeftMenu.setStickTo(SlidingLayer.STICK_TO_RIGHT);
		mSllLeftMenu.setCloseOnTapEnabled(false);
		llTransparentMenuLeft = (LinearLayout) leftMenu
				.findViewById(R.id.llTransparentSlider);

		// Sliding layer your detail

		lblYourName = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lbl_your_name);
		lblLocation = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lbl_location);
		lblChangeLocation = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lbl_change_location);
		lblDOB = (HelveticaLightTextView) leftMenu.findViewById(R.id.lbl_dob);
		lblUserName = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lbl_username);
		txtYourName = (EditText) leftMenu
				.findViewById(R.id.txt_your_name_detail);
		layoutYourName = (LinearLayout) leftMenu
				.findViewById(R.id.layoutYourName);
		layoutEmail = (LinearLayout) leftMenu.findViewById(R.id.layoutEmail);
		layoutPassword = (LinearLayout) leftMenu
				.findViewById(R.id.layoutPassword);
		txtYourEmail = (EditText) leftMenu.findViewById(R.id.txt_email_detail);
		txtPassword = (EditText) leftMenu.findViewById(R.id.txt_change_pass);
		btnSave = (TextView) leftMenu.findViewById(R.id.btnSaveChange);
		chkReceiveEmail = (CheckBox) leftMenu
				.findViewById(R.id.chk_receive_email);
		llMale = (LinearLayout) leftMenu.findViewById(R.id.btnMale);
		llFemale = (LinearLayout) leftMenu.findViewById(R.id.btnFemale);
		lblChangeAvatar = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lbl_change_avatar);
		imgAvatar = (ImageView) leftMenu.findViewById(R.id.img_avatar);
		imgAvatarChange = (ImageView) leftMenu
				.findViewById(R.id.img_avatar_change);
		imgCamera = (ImageView) leftMenu.findViewById(R.id.img_camera);
		imgGallery = (ImageView) leftMenu.findViewById(R.id.img_gallery);
		llTransparentChangeAvt = (LinearLayout) leftMenu
				.findViewById(R.id.ll_transparent_change_avt);
		lblShowHidePassword = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lblShowHidePassword);

		// Sliding layer listing
		scrollListing = (PullToRefreshScrollView) leftMenu
				.findViewById(R.id.scrollListing);
		grvExpiredListings = (AutoFillHeightGridView) leftMenu
				.findViewById(R.id.grv_expired_listings);
		grvFreeListings = (AutoFillHeightGridView) leftMenu
				.findViewById(R.id.grv_free_listings);
		grvExtraListings = (AutoFillHeightGridView) leftMenu
				.findViewById(R.id.grv_extra_listings);
		grvGoneListings = (AutoFillHeightGridView) leftMenu
				.findViewById(R.id.grv_gone_listings);
		lblSelectPack = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lbl_buy_a_pack);
		llExtraListings = (LinearLayout) leftMenu
				.findViewById(R.id.ll_extra_listings);
		lblAddtionalTitle = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lblAddtionalTitle);
		lblMessageAdditonSlot = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lblMessageAdditonSlot);
		lblAboveMessageAdditonSlot = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lblAboveMessageAdditonSlot);
		// Sliding layer location
		mSllLocation = (SlidingLayer) leftMenu.findViewById(R.id.sll_location);
		mSllLocation.setStickTo(SlidingLayer.STICK_TO_RIGHT);
		mSllLocation.setCloseOnTapEnabled(false);
		llTransparentLocation = (LinearLayout) leftMenu
				.findViewById(R.id.llTransparentLocation);
		mLblLocationChanged = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lbl_location_changed);
		lblSelectLocation = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lblDoneLocation);
		lblCurrentChangeLocation = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lblCurrentLocation);
		mLblLocationChanged.setText(tvLeftMenuAddress.getText().toString());
		mLblLocationChanged.setSelected(true);
		mTxtSearchLocation = (EditText) leftMenu
				.findViewById(R.id.txt_type_location);
		mImgSearchLocation = (ImageView) leftMenu
				.findViewById(R.id.img_search_location);
		mLvLocationSearch = (ListView) leftMenu
				.findViewById(R.id.lv_location_search);

		// Sliding layer search location
		lblSearchLocationChanged = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lbl_search_location_changed);
		lblCurrentSearchLocation = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lblCurrentSearchLocation);
		lblSearchLocationChanged.setSelected(true);
		txtSearchLocation = (EditText) leftMenu
				.findViewById(R.id.txtSearchLocation);
		btnSearchLocation = (ImageView) leftMenu
				.findViewById(R.id.btnSearchLocation);
		lsvSearchLocation = (ListView) leftMenu
				.findViewById(R.id.lsvLocationResult);

		// Sliding layer share
		llConnectYourFb = (LinearLayout) leftMenu
				.findViewById(R.id.ll_connect_facebook);
		llConnectYourGGP = (LinearLayout) leftMenu
				.findViewById(R.id.ll_connect_google_plus);

		// Sliding layer block
		lvBlockedUSer = (ListView) leftMenu.findViewById(R.id.lv_blocked_user);
		lblNotifyBlockedUSer = (HelveticaLightTextView) leftMenu
				.findViewById(R.id.lbl_notify_blocked_users);

		// wrapper for slider layout
		layoutWrapperYourDetail = (LinearLayout) leftMenu
				.findViewById(R.id.layoutWrapperYourDetails);
		layoutWrapperYourListing = (LinearLayout) leftMenu
				.findViewById(R.id.layoutWrapperYourListing);
		layoutWrapperChangeLocation = (LinearLayout) leftMenu
				.findViewById(R.id.layoutWrapperChangeLocation);
		layoutWrapperSeachLocation = (LinearLayout) leftMenu
				.findViewById(R.id.layoutWrapperSeachLocation);
		layoutWrapperShareSetting = (LinearLayout) leftMenu
				.findViewById(R.id.layoutWrapperShareSetting);
		layoutWrapperBlocker = (LinearLayout) leftMenu
				.findViewById(R.id.layoutWrapperBlocked);

	}

	public void initRightMenu(IOnMenuToggle listener) {
		rightMenu = new SlidingMenu(self, listener);
		rightMenu.setMenu(R.layout.menu_sliding_right);
		rightMenu.setMode(SlidingMenu.RIGHT);
		rightMenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		// rightMenu.setTouchModeBehind(SlidingMenu.TOUCHMODE_MARGIN);
		rightMenu.setShadowWidthRes(R.dimen.shadow_width);
		rightMenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		rightMenu.setFadeDegree(0.5f);

		lvCategories = (AnimatedExpandableListView) rightMenu
				.findViewById(R.id.lvCategories);
	}

	public void setSlidingLayerInteraction(final SlidingLayer sll) {
		sll.setOnInteractListener(new OnInteractListener() {

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
				sll.setVisibility(View.GONE);

			}

			@Override
			public void onClose() {
				// TODO Auto-generated method stub

			}
		});
	}

	// ========RECEIVER NOTIFICATION=============//

	@Override
	public void onResume() {
		super.onResume();

	}

	public boolean isLoggedIn() {
		if (pref.getStringValue(
				TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON).equals("")) {
			return false;
		} else {
			return true;
		}
	}

	protected View getParentView(View v) {
		View parentView = (View) v.getParent();
		return parentView;
	}

	public void doLogout() {
		AlertDialog.Builder builder = new AlertDialog.Builder(self);
		builder.setTitle(self.getResources().getString(R.string.text_log_out));
		builder.setMessage(self.getResources().getString(
				R.string.text_confirm_exit));
		builder.setPositiveButton(
				self.getResources().getString(R.string.text_btn_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// Log out.
						onLogoutSuccess();

					}
				});
		builder.setNegativeButton(
				self.getResources().getString(R.string.text_btn_cancel),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

		builder.create().show();
	}

	private void onLogoutSuccess() {

		pref.putStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON,
				"");
		// GlobalValue.myAccount = null;
		// checkLoggedInUI();
		Toast.makeText(self, "You are logged out successfully!",
				Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		self = null;
		context = null;
		System.gc();
		// registerBaseActivityReceiver();
		unRegisterBaseActivityReceiver();

	}

	public void changeInputKeyBoad() {

		Locale.setDefault(Locale.JAPANESE);
		Configuration config = getResources().getConfiguration();
		config.locale = Locale.JAPANESE;
		getBaseContext().getResources().updateConfiguration(config, null);

	}

	// hide key board when click outside
	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		View view = getCurrentFocus();
		boolean ret = super.dispatchTouchEvent(event);

		if (view instanceof EditText) {
			if (view.getId() != R.id.etInputMessage) {
				View w = getCurrentFocus();
				int scrcoords[] = new int[2];
				w.getLocationOnScreen(scrcoords);
				float x = event.getRawX() + w.getLeft() - scrcoords[0];
				float y = event.getRawY() + w.getTop() - scrcoords[1];

				if (event.getAction() == MotionEvent.ACTION_UP
						&& (x < w.getLeft() || x >= w.getRight()
								|| y < w.getTop() || y > w.getBottom())) {
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(getWindow().getCurrentFocus()
							.getWindowToken(), 0);
				}
			}

		}

		return ret;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
	}

	public void setMenuMarker(int viewId) {
		for (int i = 0; i < menuMarkers.size(); i++) {
			if (menuMarkers.get(i).getId() != viewId) {
				menuMarkers.get(i).setVisibility(View.INVISIBLE);
			} else {
				menuMarkers.get(i).setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	/**
	 * Go to other activity
	 * 
	 * @param context
	 * @param cla
	 */
	public void gotoActivity(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
	}

	public void goBackActivity(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);
	}

	public void gotoActivity(Context context, Class<?> cla, int flag) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(flag);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
	}

	/**
	 * goto view website
	 * 
	 * @param url
	 */
	public void gotoWeb(Uri uri) {
		Intent myIntent = new Intent(Intent.ACTION_VIEW, uri);
		startActivity(myIntent);
	}

	/**
	 * Go to other activity
	 * 
	 * @param context
	 * @param cla
	 */
	public void gotoActivityForResult(Context context, Class<?> cla,
			int requestCode) {
		Intent intent = new Intent(context, cla);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * Goto activity with bundle
	 * 
	 * @param context
	 * @param cla
	 * @param bundle
	 */
	public void gotoActivity(Context context, Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);

	}

	public void gotoActivity(Context context, Class<?> cla, Bundle bundle,
			boolean animation) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivity(intent);
		if (animation) {
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.push_left_out);
		}

	}

	public void goBackActivity(Context context, Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_right);

	}

	public void gotoActivity(Context context, Class<?> cla, Bundle bundle,
			int flag) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		intent.addFlags(flag);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);

	}

	public void reloadActivity(Activity activity, Bundle bundle) {
		Intent intent = getIntent();
		intent.putExtras(bundle);
		activity.finish();
		startActivity(intent);
	}

	/**
	 * Goto activity with bundle
	 * 
	 * @param context
	 * @param cla
	 * @param bundle
	 * @param requestCode
	 */
	public void gotoActivityForResult(Context context, Class<?> cla,
			Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * Goto web page
	 * 
	 * @param url
	 */
	protected void gotoWebPage(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
		startActivity(i);
	}

	/**
	 * Goto phone call
	 * 
	 * @param telNumber
	 */
	protected void gotoPhoneCallPage(String telNumber) {
		Intent i = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + telNumber));
		startActivity(i);
	}

	/**
	 * Close activity
	 */
	public void closeActivity(View v) {
		finish();
	}

	// ========RECEIVER NOTIFICATION=============//

	public static final String FINISH_ALL_ACTIVITIES = "com.danglv.asknshare.FINISH_ALL_ACTIVITIES_ACTIVITY_ACTION";
	private BaseActivityReceiver baseActivityReceiver = new BaseActivityReceiver();
	public static final IntentFilter INTENT_FILTER = createIntentFilter();

	private static IntentFilter createIntentFilter() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(FINISH_ALL_ACTIVITIES);
		return filter;
	}

	protected void registerBaseActivityReceiver() {
		registerReceiver(baseActivityReceiver, INTENT_FILTER);
	}

	protected void unRegisterBaseActivityReceiver() {
		unregisterReceiver(baseActivityReceiver);
	}

	public class BaseActivityReceiver extends BroadcastReceiver {
		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(FINISH_ALL_ACTIVITIES)) {
				finish();
			}
		}

	}

	protected void closeAllActivities() {
		sendBroadcast(new Intent(FINISH_ALL_ACTIVITIES));
	}

	// ********************* NOTIFICATION *******************

	// ======================= PROGRESS DIALOG ======================

	/**
	 * Open progress dialog
	 * 
	 * @param text
	 */
	public void showProgressDialog(String text) {
		if (progressDialog == null) {
			progressDialog = ProgressDialog.show(this,
					getString(R.string.app_name), text, true);
			progressDialog.setCancelable(false);
		}
	}

	/**
	 * Open progress dialog
	 */
	public void showProgressDialog() {
		progressDialog = new ProgressDialog(context,
				R.style.ProgressDialogTheme);
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	/**
	 * Close progress dialog
	 */
	public void closeProgressDialog() {
		if (progressDialog != null) {
			progressDialog.dismiss();
			// progressDialog.cancel();
			progressDialog = null;
		}
	}

	// public void initAdMobLayout()
	// {
	//
	// AdView adView = new AdView(this, AdSize.BANNER, "a151c2512222582");
	// LinearLayout layout = (LinearLayout) findViewById(R.id.layoutView);
	// if (layout != null)
	// {
	// layout.addView(adView);
	// AdRequest request = new AdRequest();
	// adView.loadAd(request);
	//
	// }
	// }

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	public void showMessageDialog(String message, final DialogListener listener) {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_message_dialog);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialog.setCancelable(false);
		TextView lblMessage = (TextView) dialog.findViewById(R.id.lblMessage);
		TextView lblClose = (TextView) dialog.findViewById(R.id.lblClose);

		lblMessage.setText(message);
		lblClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						listener.onClose(dialog);

					}
				}, 100);
			}
		});
		dialog.show();
	}

	public void showMessageDialog(String message, String textButton,
			final DialogListener listener) {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_message_dialog);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		dialog.setCancelable(false);
		TextView lblMessage = (TextView) dialog.findViewById(R.id.lblMessage);
		TextView lblClose = (TextView) dialog.findViewById(R.id.lblClose);
		lblClose.setText(textButton);

		lblMessage.setText(message);
		lblClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				handler.postDelayed(new Runnable() {

					@Override
					public void run() {
						listener.onClose(dialog);

					}
				}, 100);

			}
		});
		dialog.show();
	}

	public Activity getSelf() {
		return self;
	}

	public void setListViewHeightBasedOnChildren(
			AnimatedExpandableListView listView) {
		// ListAdapter listAdapter = listView.getAdapter();
		// if (listAdapter == null)
		// {
		// // pre-condition
		// return;
		// }
		//
		// int totalHeight = 0;
		// for (int i = 0; i < listAdapter.getCount(); i++)
		// {
		// View listItem = listAdapter.getView(i, null, listView);
		// listItem.measure(0, 0);
		// totalHeight += listItem.getMeasuredHeight();
		// }
		//
		// ViewGroup.LayoutParams params = listView.getLayoutParams();
		// params.height = totalHeight
		// + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		// listView.setLayoutParams(params);
		// listView.requestLayout();

		ListAdapter mAdapter = listView.getAdapter();

		int totalHeight = 0;

		for (int i = 0; i < mAdapter.getCount(); i++) {
			View mView = mAdapter.getView(i, null, listView);

			mView.measure(
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
					MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

			if (listView.isGroupExpanded(i)) {

			}

			totalHeight += mView.getMeasuredHeight();
			Log.w("LISTVIEW_HEIGHT" + i, String.valueOf(totalHeight));

		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight
				+ (listView.getDividerHeight() * (mAdapter.getCount() - 1));
		listView.setLayoutParams(params);
		listView.requestLayout();
	}

	public void showMessageTermAndPrivacy(String key) {
		ProductModelManager.getTermConditionDetails(self, key, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						try {
							JSONObject jsonObj = new JSONObject(json);
							String content = jsonObj.getString("content");
							content = content.replace("“", "\"")
									.replace("”", "\"").replace("’", "'");
							showMessageWebview(content);
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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

	@SuppressLint("SetJavaScriptEnabled")
	private void showMessageWebview(String html) {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_webview_message_dialog);
		dialog.getWindow().setBackgroundDrawableResource(
				android.R.color.transparent);
		//
		// final WindowManager.LayoutParams lp = new
		// WindowManager.LayoutParams();
		// lp.copyFrom(dialog.getWindow().getAttributes());
		// lp.width = screenWidth * 9 / 10;
		// lp.height = screenHeight * 7 / 10;

		dialog.setCancelable(false);
		WebView wbvHtmlPage = (WebView) dialog.findViewById(R.id.webView);
		wbvHtmlPage.getSettings().setJavaScriptEnabled(true);
		wbvHtmlPage.getSettings().setAppCacheEnabled(false);
		wbvHtmlPage.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
		wbvHtmlPage.setWebChromeClient(new WebChromeClient());

		if (getResources().getBoolean(R.bool.isTablet)) {
			wbvHtmlPage.getSettings().setTextSize(WebSettings.TextSize.LARGER);
		} else {
			wbvHtmlPage.getSettings().setTextSize(WebSettings.TextSize.NORMAL);
		}

		wbvHtmlPage.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				// TODO Auto-generated method stub
				super.onPageFinished(view, url);
				dialog.show();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				if (url.startsWith("mailto:")) {
					try {
						Intent emailIntent = new Intent(Intent.ACTION_SEND, Uri
								.parse(url));
						emailIntent.setType("message/rfc822");
						String recipient = StringUtility
								.getEmailFromMailToUrl(url);
						emailIntent.putExtra(
								android.content.Intent.EXTRA_EMAIL,
								new String[] { recipient });

						startActivity(Intent.createChooser(emailIntent,
								"Send email to " + recipient));
					} catch (Exception ex) {
					}
				} else if (url.contains("gotoTermOfUse")) {
					gotoTermOfUse();
				}
				return true;
			}
		});
		wbvHtmlPage.loadData(html, "text/html", "UTF-8");

		TextView lblClose = (TextView) dialog.findViewById(R.id.lblClose);

		lblClose.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				dialog.cancel();
			}
		});

	}

	public void gotoTermOfUse() {

	}

	public void showErrorMessageDefault() {
		showMessageDialog(getString(R.string.error_default_message),
				new DialogListener() {

					@Override
					public void onClose(Dialog dialog) {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
	}
}
