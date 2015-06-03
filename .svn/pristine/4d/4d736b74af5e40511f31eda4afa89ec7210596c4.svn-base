package com.pt.treasuretrash.utility;

import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.pt.treasuretrash.MyGoogleAnalyticApplycation;
import com.pt.treasuretrash.config.Constant;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.LinearLayout;

public class GoogleUtility {

	/*
	 * create the banner admob on activity
	 */
	public static void initBannerAdsOnActivity(Activity act,
			int layoutBannerAdsId) {
		AdView adView = new AdView(act);
		adView.setAdSize(AdSize.BANNER);
		adView.setAdUnitId(Constant.BANNER_ADMOD_KEY);
		LinearLayout layout = (LinearLayout) act
				.findViewById(layoutBannerAdsId);
		if (layout != null) {
			layout.addView(adView);
			AdRequest adRequest = new AdRequest.Builder().build();
			adView.loadAd(adRequest);
		}
	}

	/*
	 * create the banner admob on fragment
	 */


	public static void initInterstitialAdMobOnActivity(Activity act,
			int layoutBannerAdsId) {
		final InterstitialAd interstitialAd = new InterstitialAd(act);
		interstitialAd.setAdUnitId(Constant.INTERSTITIAL_ADMOD_KEY);
		interstitialAd.setAdListener(new AdListener() {
			@Override
			public void onAdLoaded() {
				// TODO Auto-generated method stub
				interstitialAd.show();
			}
		});
		AdRequest adRequest = new AdRequest.Builder().build();
		interstitialAd.loadAd(adRequest);
	}

	/*
	 * using google anylytic call when create main activity of app
	 */
	public static void initGoogleAnalytic(Activity act) {
		MyGoogleAnalyticApplycation.getGaTracker().set(Fields.SCREEN_NAME,
				act.getClass().getName());
		MyGoogleAnalyticApplycation.getGaTracker().send(
				MapBuilder.createAppView().build());
	}

}
