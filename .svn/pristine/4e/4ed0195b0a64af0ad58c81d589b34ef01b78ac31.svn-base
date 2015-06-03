package com.pt.treasuretrash.fragment;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.HomeActivity;
import com.pt.treasuretrash.activity.WalkthrouhActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseFragment;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.utility.NetworkUtil;
import com.pt.treasuretrash.widget.ProgressDialog;
import com.pt.treasuretrash.widget.ProgressHUD;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DefaultFragment extends TreasureTrashBaseFragment {

	private LinearLayout llLocationNotAllowed, llNoInternetConnection;
	private TextView btnTabToRetry;
	private Handler handler;

	@Override
	public int getFragmentResource() {
		return R.layout.demo_layout_new;
	}

	@Override
	public void initView(View view) {
		llLocationNotAllowed = (LinearLayout) view
				.findViewById(R.id.llLocationNotAllowed);
		llNoInternetConnection = (LinearLayout) view
				.findViewById(R.id.llNoInternetConnection);
		btnTabToRetry = (TextView) view.findViewById(R.id.btnRetry);
		handler = new Handler();
	}

	@Override
	public void initData(View view) {

	}

	@Override
	public void initControl(View view) {
		// TODO Auto-generated method stub
		btnTabToRetry.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final ProgressHUD dialog = ProgressHUD.show(self, "", true,
						true, new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								// TODO Auto-generated method stub
                                 
							}
						});
				if (NetworkUtil.checkNetworkAvailable(self)) {
					handler.postDelayed(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							dialog.dismiss();
							gotoActivity(self, WalkthrouhActivity.class);
							getHomeActivity().finish();
						}
					}, 1000);
				} else {
					handler.postDelayed(new Runnable() {

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

	@Override
	public void onShow() {
		getHomeActivity().setBottomMenu(-1);
		if (!NetworkUtil.checkNetworkAvailable(self)) {
			llLocationNotAllowed.setVisibility(View.GONE);
			llNoInternetConnection.setVisibility(View.VISIBLE);
			getHomeActivity().disableBottomLayout();
		} else if (getHomeActivity().pref
				.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT) == 0
				|| getHomeActivity().pref
						.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG) == 0) {
			llLocationNotAllowed.setVisibility(View.VISIBLE);
			llNoInternetConnection.setVisibility(View.GONE);
			getHomeActivity().disableBottomLayout();
			// getHomeActivity().currentState =
			// getHomeActivity().STATE_LOCATION_OFF;
//			handler.postDelayed(mUpdateLocationTask, 3000);
		} else {
			llLocationNotAllowed.setVisibility(View.GONE);
			llNoInternetConnection.setVisibility(View.GONE);
			getHomeActivity().showFragment(HomeActivity.FRAGMENT_GALLERY);
		}

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

//	private Runnable mUpdateLocationTask = new Runnable() {
//
//		@Override
//		public void run() {
//			if (getHomeActivity().canGetLocation()
//					&& NetworkUtil.checkNetworkAvailable(self)) {
//				Log.d("GPS_", getHomeActivity().gpsTracker.getLatitude() + "-"
//						+ getHomeActivity().gpsTracker.getLongitude());
//				if (getHomeActivity().gpsTracker.getLatitude() != 0
//						&& getHomeActivity().gpsTracker.getLongitude() != 0) {
//					getHomeActivity().pref.putFloatValue(
//							TreasureTrashSharedPreferences.PREF_MY_CURRENT_LAT,
//							(float) getHomeActivity().gpsTracker.getLatitude());
//					getHomeActivity().pref
//							.putFloatValue(
//									TreasureTrashSharedPreferences.PREF_MY_CURRENT_LNG,
//									(float) getHomeActivity().gpsTracker
//											.getLongitude());
//					// getHomeActivity().currentState =
//					// getHomeActivity().STATE_NORMAL;
//					// getHomeActivity().enableBottomLayout();
//					// getHomeActivity().showFragment(
//					// getHomeActivity().FRAGMENT_GALLERY);
//					handler.removeCallbacks(this);
//					
//					Bundle bundle = new Bundle();
//					if(getHomeActivity().isLoggedIn()){
//						bundle.putString(GlobalValue.KEY_ACTION, GlobalValue.KEY_ACTION_LOGIN_REFRESH_TOKEN);
//					}
//					gotoActivity(self, HomeActivity.class, bundle);
//					getHomeActivity().finish();
//				} else {
//					handler.postDelayed(this, 3000);
//				}
//
//			} else {
//				handler.postDelayed(this, 3000);
//			}
//
//		}
//	};

}
