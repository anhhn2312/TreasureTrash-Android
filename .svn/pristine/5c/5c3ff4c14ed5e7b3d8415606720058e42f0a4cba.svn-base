package com.pt.treasuretrash.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.graphics.Typeface;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.ErrorActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.utility.DeviceUtility;
import com.pt.treasuretrash.utility.DialogUtility;
import com.pt.treasuretrash.utility.DialogYesNo;
import com.pt.treasuretrash.utility.NetworkUtil;
import com.pt.treasuretrash.utility.DialogYesNo.OnDialogClickListener;
import com.pt.treasuretrash.utility.GPSTracker;
import com.pt.treasuretrash.utility.GPSTracker.IGpsTrackerListener;

public class TreasureTrashBaseFunctionsActivity extends
		TreasureTrashBaseActivity {

	public GPSTracker gpsTracker;
	public BroadcastReceiver gpsReceiver;
	public Typeface typeface;
	public AQuery aq;
	protected Handler baseHandler;
	protected boolean isError = false;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		gpsTracker = new GPSTracker(this);
		Display display = self.getWindowManager().getDefaultDisplay();
		screenWidth = display.getWidth();
		screenHeight = display.getHeight();
		if (DeviceUtility.getDeviceName().contains("HTC")) {
			IMAGE_SMALL_SIZE = ((int) screenWidth / 3 / 2);
		} else {
			IMAGE_SMALL_SIZE = ((int) screenWidth / 3);
		}
		IMAGE_SIZE = screenWidth;
		aq = new AQuery(self);
		createTypeFace(self);
		baseHandler = new Handler();
	}

	public void showSoftKeyboard(final View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.showSoftInput(v, InputMethodManager.SHOW_IMPLICIT);
	}

	public void hideSoftKeyboard(View v) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
	}

	public void createTypeFace(Context context) {
		if (pref.getFont().length() == 0) {
			pref.setFont("fonts/helveticaneuelight.ttf");
		}
		typeface = Typeface
				.createFromAsset(context.getAssets(), pref.getFont());
	}

	public void setTypeFace(TextView... lbl) {
		for (TextView textView : lbl) {
			textView.setTypeface(typeface);
		}
	}

	public void setTypeFace(Context context, String font, float size,
			TextView... lbl) {
		Typeface tf = Typeface.createFromAsset(context.getAssets(), font);
		for (TextView textView : lbl) {
			textView.setTextSize(size);
			textView.setTypeface(tf);
		}
	}

	public void setTextColor(int color, TextView... lbl) {
		for (TextView textView : lbl) {
			textView.setTextColor(color);
		}
	}

	public static void setTextColorForEditext(int color, EditText... lbl) {
		for (EditText textView : lbl) {
			textView.setTextColor(color);
		}
	}

	public void showLogoutDialog() {

		new DialogYesNo(self, "Logout", "Do you want to logout?",
				new OnDialogClickListener() {

					@Override
					public void onYes() {
						pref.putStringValue(
								TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON,
								"");
						// gotoActivity(self, LoginActivity.class);
						DialogUtility.showShortToast(self,
								"You has logged out from your account.");
						finish();
					}

					@Override
					public void onNo() {

					}

					@Override
					public void onNeutral() {
						// TODO Auto-generated method stub

					}
				}).show();
	}

	public void showCustomDialog() {

	}

	public boolean canGetLocation() {
		gpsTracker.getLocation();
		return gpsTracker.canGetLocation();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		baseHandler.postDelayed(checkErrorHander, 500);

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		baseHandler.removeCallbacks(checkErrorHander);
		// gpsTracker = null;
		super.onPause();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		isError = false;
		baseHandler = null;
		if (gpsTracker != null) {
			gpsTracker.destroy();
			gpsTracker = null;
		}
	}

	public boolean isLoggedIn() {
		if (pref.getStringValue(
				TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN).equals("")) {
			return false;
		} else {
			return true;
		}
	}

	public void checkLoginUI() {
		if (leftMenu != null) {
			if (!isLoggedIn()) {
				getParentView(tvLeftMenuYourDetails).setVisibility(View.GONE);
				getParentView(tvLeftMenuYourListings).setVisibility(View.GONE);
				getParentView(tvLeftMenuLocation).setVisibility(View.GONE);
				getParentView(tvLeftMenuShareSettings).setVisibility(View.GONE);
				getParentView(tvLeftMenuBlockMember).setVisibility(View.GONE);
				getParentView(tvLeftMenuLogout).setVisibility(View.GONE);

				getParentView(tvLeftMenuLogin).setVisibility(View.VISIBLE);
				getParentView(tvLeftMenuSignup).setVisibility(View.VISIBLE);
			} else {
				getParentView(tvLeftMenuYourDetails)
						.setVisibility(View.VISIBLE);
				getParentView(tvLeftMenuYourListings).setVisibility(
						View.VISIBLE);
				getParentView(tvLeftMenuLocation).setVisibility(View.VISIBLE);
				getParentView(tvLeftMenuShareSettings).setVisibility(
						View.VISIBLE);
				getParentView(tvLeftMenuBlockMember)
						.setVisibility(View.VISIBLE);
				getParentView(tvLeftMenuLogout).setVisibility(View.VISIBLE);

				getParentView(tvLeftMenuLogin).setVisibility(View.GONE);
				getParentView(tvLeftMenuSignup).setVisibility(View.GONE);

				if (GlobalValue.myAccount != null
						&& GlobalValue.myAccount.getLocation() != null) {

					tvLeftMenuName.setText("Hi, "
							+ GlobalValue.myAccount.getName());

					tvLeftMenuAddress.setText(GlobalValue.myAccount
							.getLocation().getLocationAddress());
					aq.id(ivUserAvatar).image(
							GlobalValue.myAccount.getImageUrl(), false, true,
							0, R.drawable.image_avatar_default);
					aq.id(ivLeftMenu).image(
							GlobalValue.myAccount.getImageUrl(), false, true,
							0, R.drawable.image_avatar_default);
				}

			}
		}

	}

	// IGpsTrackerListener listener = new IGpsTrackerListener() {
	//
	// @Override
	// public void onProviderDisabled() {
	//
	// }
	//
	// @Override
	// public void onLocationChanged(Location location) {
	// // onLocationChange(location);
	// // pref.putFloatValue(
	// // TreasureTrashSharedPreferences.PREF_MY_CURRENT_LAT,
	// // (float) location.getLatitude());
	// // pref.putFloatValue(
	// // TreasureTrashSharedPreferences.PREF_MY_CURRENT_LNG,
	// // (float) location.getLongitude());
	//
	// }
	//
	// @Override
	// public void onProviderEnabled() {
	//
	// }
	//
	// };

	private Runnable checkErrorHander = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (!canGetLocation() || !NetworkUtil.checkNetworkAvailable(self)) {
				isError = true;
				gotoActivity(self, ErrorActivity.class);
			} else {
				baseHandler.postDelayed(this, 1000);
				pref.putFloatValue(
						TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LAT,
						(float) gpsTracker.getLatitude());
				pref.putFloatValue(
						TreasureTrashSharedPreferences.PREF_MY_CURRENT_LOCATION_LNG,
						(float) gpsTracker.getLongitude());

				if (pref.getBooleanValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_USE_CURRENT)) {
					pref.putFloatValue(
							TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT,
							(float) gpsTracker.getLatitude());
					pref.putFloatValue(
							TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG,
							(float) gpsTracker.getLongitude());
				}

			}
		}
	};
}
