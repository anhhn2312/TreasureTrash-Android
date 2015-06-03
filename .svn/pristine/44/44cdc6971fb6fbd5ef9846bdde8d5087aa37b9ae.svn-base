package com.pt.treasuretrash.modelmanager;

import java.util.ArrayList;
import java.util.Calendar;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.pt.treasuretrash.activity.WalkthrouhActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.config.WebserviceConfig;
import com.pt.treasuretrash.network.AsyncHttpDelete;
import com.pt.treasuretrash.network.AsyncHttpGet;
import com.pt.treasuretrash.network.AsyncHttpPost;
import com.pt.treasuretrash.network.AsyncHttpPut;
import com.pt.treasuretrash.network.AsyncHttpResponseListener;
import com.pt.treasuretrash.network.RefreshTokenTask;
import com.pt.treasuretrash.utility.AppUtil;

public class ModelManager {

	public static boolean checkTokenExpired(final Context context,
			final AsyncHttpPost post, final String url) {

		final TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);

		// pref.putLongValue(TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME,
		// 0);

		long currentTime = Calendar.getInstance().getTimeInMillis();

		if (AppUtil.isAccessTokenExpired(context)) {

			AccountModelManager.refreshToken(context, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							try {
								JSONObject obj = new JSONObject(json);
								String accessToken = "bearer "
										+ obj.getString("access_token");
								String refreshToken = obj
										.getString("refresh_token");
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN,
										accessToken);
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_REFRESH_TOKEN,
										refreshToken);
								long currentTime = Calendar.getInstance()
										.getTimeInMillis();
								pref.putLongValue(
										TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME,
										currentTime
												+ GlobalValue.TOKEN_ALIVE_TIME);

								post.execute(url);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}

						@Override
						public void onError(int statusCode, String json) {
							Intent intent = new Intent(context,
									WalkthrouhActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);

						}

						@Override
						public void onError() {
							Intent intent = new Intent(context,
									WalkthrouhActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);

						}
					});

			return true;
		} else {
			return false;
		}
	}

	public static boolean checkTokenExpired(final Context context,
			final AsyncHttpGet get, final String url) {

		final TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);

		long currentTime = Calendar.getInstance().getTimeInMillis();

		if (AppUtil.isAccessTokenExpired(context)) {

			AccountModelManager.refreshToken(context, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							try {
								JSONObject obj = new JSONObject(json);
								String accessToken = "bearer "
										+ obj.getString("access_token");
								String refreshToken = obj
										.getString("refresh_token");
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN,
										accessToken);
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_REFRESH_TOKEN,
										refreshToken);
								long currentTime = Calendar.getInstance()
										.getTimeInMillis();
								pref.putLongValue(
										TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME,
										currentTime
												+ GlobalValue.TOKEN_ALIVE_TIME);

								get.execute(url);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}

						@Override
						public void onError(int statusCode, String json) {
							Intent intent = new Intent(context,
									WalkthrouhActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);

						}

						@Override
						public void onError() {
							Intent intent = new Intent(context,
									WalkthrouhActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);

						}
					});
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkTokenExpired(final Context context,
			final AsyncHttpPut put, final String url) {

		final TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);

		long currentTime = Calendar.getInstance().getTimeInMillis();

		if (AppUtil.isAccessTokenExpired(context)) {

			AccountModelManager.refreshToken(context, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							try {
								JSONObject obj = new JSONObject(json);
								String accessToken = "bearer "
										+ obj.getString("access_token");
								String refreshToken = obj
										.getString("refresh_token");
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN,
										accessToken);
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_REFRESH_TOKEN,
										refreshToken);
								long currentTime = Calendar.getInstance()
										.getTimeInMillis();
								pref.putLongValue(
										TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME,
										currentTime
												+ GlobalValue.TOKEN_ALIVE_TIME);

								put.execute(url);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}

						@Override
						public void onError(int statusCode, String json) {
							Intent intent = new Intent(context,
									WalkthrouhActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);

						}

						@Override
						public void onError() {
							Intent intent = new Intent(context,
									WalkthrouhActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);

						}
					});
			return true;
		} else {
			return false;
		}
	}

	public static boolean checkTokenExpired(final Context context,
			final AsyncHttpDelete delete, final String url) {

		final TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);

		// pref.putLongValue(TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME,
		// 0);

		long currentTime = Calendar.getInstance().getTimeInMillis();

		if (AppUtil.isAccessTokenExpired(context)) {

			AccountModelManager.refreshToken(context, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							try {
								JSONObject obj = new JSONObject(json);
								String accessToken = "bearer "
										+ obj.getString("access_token");
								String refreshToken = obj
										.getString("refresh_token");
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN,
										accessToken);
								pref.putStringValue(
										TreasureTrashSharedPreferences.PREF_REFRESH_TOKEN,
										refreshToken);
								long currentTime = Calendar.getInstance()
										.getTimeInMillis();
								pref.putLongValue(
										TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME,
										currentTime
												+ GlobalValue.TOKEN_ALIVE_TIME);

								delete.execute(url);
							} catch (Exception e) {
								e.printStackTrace();
							}

						}

						@Override
						public void onError(int statusCode, String json) {
							Intent intent = new Intent(context,
									WalkthrouhActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);

						}

						@Override
						public void onError() {
							Intent intent = new Intent(context,
									WalkthrouhActivity.class);
							intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK
									| Intent.FLAG_ACTIVITY_NEW_TASK);
							context.startActivity(intent);

						}
					});
			return true;
		} else {
			return false;
		}
	}
}
