package com.pt.treasuretrash.activity;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.fragment.ImagesFragment;
import com.pt.treasuretrash.indicator.CirclePageIndicator;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.modelmanager.ProductModelManager;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.service.PubnubService;
import com.pt.treasuretrash.utility.DeviceUtility;
import com.pt.treasuretrash.utility.DialogUtility;
import com.pt.treasuretrash.widget.CustomDialog;
import com.pt.treasuretrash.widget.CustomDialog.OnCustomDialogClickListener;
import com.splunk.mint.Mint;

public class WalkthrouhActivity extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	private CirclePageIndicator indicator;
	private ViewPager pager;
	private TextView tvSkip, tvSingup, tvLogin, tvDescription;
	private static Activity instance;
	private String action = "";
	private long mLastClickTime = 0;

	private final int[] BG_RESOURCES = { R.drawable.bg_walkthrough_1,
			R.drawable.bg_walkthrough_2, R.drawable.bg_walkthrough_3 };

	private final int[] BG_RESOURCES_LOW = { R.drawable.bg_walkthrough_1_low,
			R.drawable.bg_walkthrough_2_low, R.drawable.bg_walkthrough_3_low };

	ArrayList<Float> arrX = new ArrayList<Float>();
	private Uri data;
	private boolean isSwitchActivity = false;
	private ProgressBar pbWaiting;
	private ImageView ivTest;
	private FragmentPagerAdapter pagerAdapter;
	private ImagesFragment fm1, fm2, fm0;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// GlobalValue.constructor(self);
		setContentView(R.layout.activity_walkthrough_layout);
//		instance = this;

		Mint.initAndStartSession(WalkthrouhActivity.this, "376655fb");

		Bundle bundle1 = getIntent().getExtras();
		if (bundle1 != null) {
			if (bundle1.containsKey(GlobalValue.KEY_ACTION)) {
				action = bundle1.getString(GlobalValue.KEY_ACTION);
			}
		}

		if (!isTaskRoot()) {
			final Intent intent = getIntent();
			final String intentAction = intent.getAction();
			if (intent.hasCategory(Intent.CATEGORY_LAUNCHER)
					&& intentAction != null
					&& intentAction.equals(Intent.ACTION_MAIN)) {
				finish();
				return;
			}
		}

		initUI();
		initData();
		initControl();

		GlobalValue.myAccount = null;
		// pref.putBooleanValue(
		// TreasureTrashSharedPreferences.PREF_HOME_FIRST_LAUNCH, false);

		pref.putFloatValue(
				TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT, 0f);
		pref.putFloatValue(
				TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG, 0f);
		pref.putStringValue(
				TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_NAME, "");
		pref.putBooleanValue(
				TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_USE_CURRENT,
				true);

		Intent intent = new Intent(self, PubnubService.class);
		startService(intent);

		if (!pref
				.getBooleanValue(TreasureTrashSharedPreferences.PREF_IS_LOCATION_ALLOWED)) {
			showLocationDialog();
		} else {
			if (canGetLocation()) {
				pref.putFloatValue(
						TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LAT,
						(float) gpsTracker.getLatitude());
				pref.putFloatValue(
						TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LNG,
						(float) gpsTracker.getLongitude());

				pref.putFloatValue(
						TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT,
						(float) gpsTracker.getLatitude());
				pref.putFloatValue(
						TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG,
						(float) gpsTracker.getLongitude());
			}

			getIntentData();

		}

	}

	private void getIntentData() {
		try {
			
			data = getIntent().getData();
			if (data != null && data.toString().contains("Item")) {
				String path = data.toString();
				String itemID = path.substring(path.indexOf("=") + 1,
						path.length());
				Log.i("EMAIL_INTENT", itemID);
				loadItem(itemID);
			} else {
				if (isLoggedIn()
						&& !action
								.equalsIgnoreCase(GlobalValue.KEY_ACTION_REFRESH_TOKEN_FAILED)) {
					// refreshToken();

					Bundle bundle = new Bundle();
					bundle.putString(GlobalValue.KEY_ACTION,
							GlobalValue.KEY_ACTION_LOGIN_REFRESH_TOKEN);
					if (data != null
							&& data.toString().equalsIgnoreCase(
									GlobalValue.URL_REDIRECT_CONFIRM_EMAIL)) {
						bundle.putString(GlobalValue.KEY_CONFIRM_CHANGE_EMAIL,
								"1");
					}
					gotoActivity(self, HomeActivity.class, bundle);
					finish();
				} else {
					if (data != null
							&& data.toString().equalsIgnoreCase(
									GlobalValue.URL_REDIRECT_CONFIRM_EMAIL)) {
						showMessageDialog(
								getString(R.string.message_confirm_change_email_successfully),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub

									}
								});
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

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
								aq.id(ivTest).image(item.getImage(), false,
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

	public static Activity getInstance() {
//		return instance;
		return instance;
		
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		
		pager.setAdapter(null);
		pagerAdapter = null;
		pager = null;
		instance = null;
		super.onDestroy();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initControl();
	}

	private void initUI() {
		indicator = (CirclePageIndicator) findViewById(R.id.indicator);
		indicator.setDotDistance(10);
		indicator.setCentered(true);
		pager = (ViewPager) findViewById(R.id.imagePager);
		tvSkip = (TextView) findViewById(R.id.tvSkip);
		tvLogin = (TextView) findViewById(R.id.tvLogin);
		tvSingup = (TextView) findViewById(R.id.tvSignup);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
		pbWaiting = (ProgressBar) findViewById(R.id.progress_bar);
		ivTest = (ImageView) findViewById(R.id.ivTest);

	}

	private void initData() {

		if (DeviceUtility.getDeviceName().contains("HTC")) {
			fm0 = ImagesFragment.newInstance(BG_RESOURCES_LOW[0]);
			fm1 = ImagesFragment.newInstance(BG_RESOURCES_LOW[1]);
			fm2 = ImagesFragment.newInstance(BG_RESOURCES_LOW[2]);
		} else {
			fm0 = ImagesFragment.newInstance(BG_RESOURCES[0]);
			fm1 = ImagesFragment.newInstance(BG_RESOURCES[1]);
			fm2 = ImagesFragment.newInstance(BG_RESOURCES[2]);
		}

		pagerAdapter = new ImageFragmentAdapter(getSupportFragmentManager());
		pager.setOffscreenPageLimit(3);
		pager.setAdapter(pagerAdapter);
		indicator.setViewPager(pager);
	}

	private void initControl() {
		tvSkip.setOnClickListener(this);
		tvSingup.setOnClickListener(this);
		tvLogin.setOnClickListener(this);

		pager.setOnPageChangeListener(new OnPageChangeListener() {

			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				indicator.onPageSelected(position);
				// Swipe right to left would go to HomeActivity when in last
				// page.
				if (position == (BG_RESOURCES.length - 1)) {
					pager.setOnTouchListener(touchListener);
				} else {
					pager.setOnTouchListener(null);
				}
			}

			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
			}
		});

	}

	OnTouchListener touchListener = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub

			if (event.getAction() == MotionEvent.ACTION_MOVE) {
				// Log.e(TAG, "X = " + event.getX());
				arrX.add(event.getX());
				// Log.e(TAG, "arrX.size =" + arrX.size());
				if (arrX.get(0) - arrX.get(arrX.size() - 1) > 100) {
					if (!isSwitchActivity) {
						pager.setCurrentItem(2);
						isSwitchActivity = true;
						processToHomeActivity();
					}
				}
			} else {
				arrX.clear();
			}
			return false;
		}
	};

	private Runnable checkHomeVisibleHandler = new Runnable() {

		@Override
		public void run() {
			if (HomeActivity.isHomeVisible) {
				handler.removeCallbacks(this);
				finish();
			} else {
				handler.postDelayed(this, 500);
			}

		}
	};

	private void processToHomeActivity() {
		pbWaiting.setVisibility(View.VISIBLE);
		handler.postDelayed(checkHomeVisibleHandler, 300);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				// Intent intent = new Intent(self,
				// HomeActivity.class);
				// startActivity(intent);
				gotoActivity(self, HomeActivity.class);

			}
		}, 3000);
	}

	private void refreshToken() {
		AccountModelManager.refreshToken(self, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						try {
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

							// gotoActivity(self, HomeActivity.class);
							// finish();
							getAccountInfoAfterLogin(HomeActivity.class, null);

						} catch (Exception e) {
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

	private void showLocationDialog() {
		new CustomDialog(self, getString(R.string.walkthrouh_location_title),
				getString(R.string.walkthrough_location_content),
				getString(R.string.allow), getString(R.string.dont_allow),
				new OnCustomDialogClickListener() {

					@Override
					public void onYes() {
						if (canGetLocation()) {

							pref.putFloatValue(
									TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT,
									(float) gpsTracker.getLatitude());
							pref.putFloatValue(
									TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT,
									(float) gpsTracker.getLongitude());
							pref.putBooleanValue(
									TreasureTrashSharedPreferences.PREF_IS_LOCATION_ALLOWED,
									true);
							if (pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT) == 0
									&& pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG) == 0) {
								gotoActivity(self, HomeActivity.class);
								finish();
							}
						}
						getIntentData();
					}

					@Override
					public void onNo() {
						pref.putBooleanValue(
								TreasureTrashSharedPreferences.PREF_IS_LOCATION_ALLOWED,
								false);
						gotoActivity(self, ErrorActivity.class);
						finish();

					}

					@Override
					public void onNeutral() {
						// TODO Auto-generated method stub

					}
				}).show();
	}

	class ImageFragmentAdapter extends FragmentPagerAdapter {

		public ImageFragmentAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {

			switch (position) {
			case 0:
				return fm0;
			case 1:
				return fm1;
			case 2:
				return fm2;

			default:
				return null;
			}

		}

		@Override
		public CharSequence getPageTitle(int position) {
			return "";
		}

		@Override
		public int getCount() {
			return (BG_RESOURCES.length);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			super.destroyItem(container, position, object);
		}
	}

	@Override
	public void onClick(View v) {
		if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
			return;
		}
		mLastClickTime = SystemClock.elapsedRealtime();

		if (v == tvSkip) {
			processToHomeActivity();
			return;
		}
		if (v == tvLogin) {
			gotoActivity(self, LoginSelectionActivity.class);

			return;
		}
		if (v == tvSingup) {
			tvSingup.setEnabled(false);
			gotoActivity(self, SignupActivity.class);
			tvSingup.setEnabled(true);

			return;
		}

	}

	private void enableGPS() {
		Intent intent = new Intent("android.location.GPS_ENABLED_CHANGE");
		intent.putExtra("enabled", true);
		self.sendBroadcast(intent);
	}

	@Override
	public void onBackPressed() {
		if (pager.getCurrentItem() == 0) {
			showExitPopup();
		} else {
			pager.setCurrentItem(pager.getCurrentItem() - 1);
		}

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

}
