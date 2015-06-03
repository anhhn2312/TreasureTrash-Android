package com.pt.treasuretrash.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.utility.DialogUtility;
import com.pt.treasuretrash.utility.GPSTracker;
import com.pt.treasuretrash.utility.NetworkUtil;
import com.pt.treasuretrash.utility.GPSTracker.IGpsTrackerListener;
import com.pt.treasuretrash.widget.ProgressDialog;
import com.pt.treasuretrash.widget.ProgressHUD;

public class ErrorActivity extends Activity {

	private LinearLayout llLocationNotAllowed, llNoInternetConnection;
	private TextView btnTabToRetry;
	public GPSTracker gpsTracker;
	public TreasureTrashSharedPreferences pref;
	private Handler hander;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_error_layout);
		initUI();
		gpsTracker = new GPSTracker(null);
		pref = new TreasureTrashSharedPreferences(this);
		hander = new Handler();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (pref.getBooleanValue(TreasureTrashSharedPreferences.PREF_IS_LOCATION_ALLOWED)) {
			hander.postDelayed(checkErrorHander, 1000);
		} else {
			llLocationNotAllowed.setVisibility(View.VISIBLE);
			llNoInternetConnection.setVisibility(View.GONE);
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		hander.removeCallbacks(checkErrorHander);

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		hander = null;
	}

	private void initUI() {
		llLocationNotAllowed = (LinearLayout) findViewById(R.id.llLocationNotAllowed);
		llNoInternetConnection = (LinearLayout) findViewById(R.id.llNoInternetConnection);
		llLocationNotAllowed.setVisibility(View.GONE);
		llNoInternetConnection.setVisibility(View.GONE);
		btnTabToRetry = (TextView) findViewById(R.id.btnRetry);
		btnTabToRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ProgressDialog dialog = new ProgressDialog(
						ErrorActivity.this);
				dialog.show();
				if (NetworkUtil.checkNetworkAvailable(ErrorActivity.this)) {
					hander.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.dismiss();
							finish();
						}
					}, 1000);
				} else {
					hander.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					}, 2000);
				}
			}
		});
	}

	private Runnable checkErrorHander = new Runnable() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			if (canGetLocation()
					&& NetworkUtil.checkNetworkAvailable(ErrorActivity.this)) {

				if (gpsTracker.getLatitude() != 0
						&& gpsTracker.getLongitude() != 0) {
					pref.putFloatValue(
							TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT,
							(float) gpsTracker.getLatitude());
					pref.putFloatValue(
							TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG,
							(float) gpsTracker.getLongitude());

				}
				finish();

			} else {
				if (!NetworkUtil.checkNetworkAvailable(ErrorActivity.this)) {
					llLocationNotAllowed.setVisibility(View.GONE);
					llNoInternetConnection.setVisibility(View.VISIBLE);
				} else if (!canGetLocation()) {
					llLocationNotAllowed.setVisibility(View.VISIBLE);
					llNoInternetConnection.setVisibility(View.GONE);
				}

				hander.postDelayed(this, 1000);
			}
		}
	};

	IGpsTrackerListener listener = new IGpsTrackerListener() {

		@Override
		public void onProviderDisabled() {

		}

		@Override
		public void onLocationChanged(Location location) {
			// onLocationChange(location);

		}

		@Override
		public void onProviderEnabled() {

		}

	};

	public boolean canGetLocation() {
		gpsTracker.getLocation();
		return gpsTracker.canGetLocation();
	}

}
