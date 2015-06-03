package com.pt.treasuretrash.modelmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pt.treasuretrash.PacketUtility;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.config.WebserviceConfig;
import com.pt.treasuretrash.network.AsyncHttpBase;
import com.pt.treasuretrash.network.AsyncHttpGet;
import com.pt.treasuretrash.network.AsyncHttpPost;
import com.pt.treasuretrash.network.AsyncHttpPut;
import com.pt.treasuretrash.network.AsyncHttpResponseProcess;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.LocationObj;

public class AccountModelManager extends ModelManager {
	private static String TAG = "ModelManager";
	private final static String GRANT_TYPE_PASSWORD = "password";
	public final static String GRANT_TYPE_REFRESH_TOKEN = "refresh_token";
	public final static String PROVIDER_FACEBOOK = "Facebook";
	public final static String PROVIDER_GOOGLE = "Google";

	public final static String CLIENT_ID = "TreasureTrashDevAndroid";
	public final static String CLIENT_SECRET = "ttaios2014";

	// =================================================================
	// login and register

	public static void LoginNormal(Context context, String userName,
			String password, boolean isProgess,
			final ModelManagerListener listener) {
		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createLoginNormalParams(GRANT_TYPE_PASSWORD, userName,
						password, CLIENT_ID, CLIENT_SECRET);
		AsyncHttpPost post = new AsyncHttpPost(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		post.execute(WebserviceConfig.LOGIN_NORMAL);
	}

	public static void RegisterNormal(Context context, Account acc,
			boolean isProgess, final ModelManagerListener listener) {
		String params = ParameterFactory.createRegisterNormalParams(acc);
		AsyncHttpPost post = new AsyncHttpPost(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		post.execute(WebserviceConfig.REGISTER);
	}

	public static void refreshToken(Context context, boolean isProgess,
			final ModelManagerListener listener) {
		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);
		String refreshToken = pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_REFRESH_TOKEN);
		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createRefreshTokenParams(GRANT_TYPE_REFRESH_TOKEN,
						refreshToken, CLIENT_ID, CLIENT_SECRET);
		AsyncHttpPost post = new AsyncHttpPost(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (response.length() < 800) {
								listener.onError();
							} else {
								if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS) {
									listener.onSuccess(response);
								} else
									listener.onError(StatusCode, response);
							}
						} else
							listener.onError();
					}
				}, params, isProgess);
		post.execute(WebserviceConfig.REFRESH_TOKEN);
	}

	public static void checkAuthorSocial(Context context, String provider,
			boolean isProgess, final ModelManagerListener listener) {
		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createLoginSocialParams(provider, CLIENT_ID,
						PacketUtility.getPacketName());
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							Log.i("REFRESH_TOKEN_RESPONSE", response);
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		post.execute(WebserviceConfig.LOGIN_SOCIAL);
	}

	public static void registerSocialInfo(Context context, Account acc,
			boolean isProgess, final ModelManagerListener listener) {
		String params = ParameterFactory.createRegisterSocialParams(acc);
		AsyncHttpPost post = new AsyncHttpPost(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		post.execute(WebserviceConfig.REGISTER_SOCIAL);
	}

	public static void loginSocial(Context context, String provider,
			String socialToken, boolean isProgess,
			final ModelManagerListener listener) {
		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createGetAccountSocialLoginParams(provider, socialToken);
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		post.execute(WebserviceConfig.GET_ACCOUNT_SOCIAL_LOGIN);
	}

	public static void shareAccount(Context context, String provider,
			String socialToken, boolean isProgess,
			final ModelManagerListener listener) {
		String url = WebserviceConfig.SHARE_ACCOUNT;
		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createGetAccountSocialLoginParams(provider, socialToken);
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		// post.execute(WebserviceConfig.SHARE_ACCOUNT);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void getSocialInfo(Context context, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.GET_SOCIAL_INFO;
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							listener.onSuccess(response);
						} else
							listener.onError();
					}
				}, new ArrayList<NameValuePair>(), isProgess);
		// post.execute(WebserviceConfig.GET_SOCIAL_INFO);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void ForgotPassword(Context context, String email,
			boolean isProgess, final ModelManagerListener listener) {

		JSONObject json = new JSONObject();
		try {
			json.put("email", email);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AsyncHttpPost post = new AsyncHttpPost(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, json.toString(), isProgess);
		post.execute(WebserviceConfig.FORGOT_PASSWORD);
	}

	public static void getAccountInfo(Context context, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.GET_ACCOUNT_PROFILE;
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, new ArrayList<NameValuePair>(), isProgess);
		// post.execute(WebserviceConfig.GET_ACCOUNT_PROFILE);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void updateProfile(Context context, Account acc,
			boolean isReceiveNewsLetter, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.UPDATE_PROFILE;
		String params = ParameterFactory.createUpdateProfileParams(acc,
				isReceiveNewsLetter);
		AsyncHttpPut post = new AsyncHttpPut(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		// post.execute(WebserviceConfig.UPDATE_PROFILE);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void getProfileUserById(Context context, String userId,
			boolean isProgess, final ModelManagerListener listener) {

		String url = WebserviceConfig.GET_PROFILE_BY_ID + "/" + userId
				+ "/GetProfileByUserId";
		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createUserIdParams(userId);
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		// post.execute(WebserviceConfig.GET_PROFILE_BY_ID + "/" + userId
		// + "/GetProfileByUserId");

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void blockUser(Context context, String userId,
			boolean isProgess, final ModelManagerListener listener) {
		String url = WebserviceConfig.BLOCK_USER;

		JSONObject json = new JSONObject();
		try {
			json.put("BlockingId", userId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AsyncHttpPost post = new AsyncHttpPost(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, json.toString(), isProgess);
		// post.execute(WebserviceConfig.BLOCK_USER);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void followUser(Context context, String userId,
			boolean isProgess, final ModelManagerListener listener) {

		String url = WebserviceConfig.FOLLOW_USER;
		JSONObject json = new JSONObject();
		try {
			json.put("FollowingId", userId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AsyncHttpPost post = new AsyncHttpPost(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, json.toString(), isProgess);
		// post.execute(WebserviceConfig.FOLLOW_USER);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void getListBlockedUsers(Context context, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.GET_BLOCKED_USERS;
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, new ArrayList<NameValuePair>(), isProgess);
		// post.execute(WebserviceConfig.GET_BLOCKED_USERS);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void getFollowedUsers(Context context, String userId,
			boolean isProgess, final ModelManagerListener listener) {

		String url = WebserviceConfig.GET_FOLLOWED_USERS;
		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createUserIdParams(userId);
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		// post.execute(WebserviceConfig.GET_FOLLOWED_USERS);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void getFollowingUsers(Context context, String userId,
			boolean isProgess, final ModelManagerListener listener) {

		String url = WebserviceConfig.GET_FOLLOWING_USERS;
		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createUserIdParams(userId);
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		// post.execute(WebserviceConfig.GET_FOLLOWING_USERS);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void getLocation(Context context, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.GET_CURRENT_LOCATION;
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS) {
								listener.onSuccess(response);
							} else {
								listener.onError(StatusCode, response);
							}
						} else
							listener.onError();
					}
				}, new ArrayList<NameValuePair>(), isProgess);
		// post.execute(WebserviceConfig.GET_CURRENT_LOCATION);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void setNumberSingleUseSlot(Context context, int numberSlot,
			boolean isProgess, final ModelManagerListener listener) {

		String url = WebserviceConfig.SET_NUMBER_SINGLE_USE_SLOT;
		JSONObject json = new JSONObject();
		try {
			json.put("Slot", numberSlot + "");
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		AsyncHttpPut post = new AsyncHttpPut(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, json.toString(), isProgess);
		// post.execute(WebserviceConfig.SET_NUMBER_SINGLE_USE_SLOT);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void unBlockUser(Context context, String userId,
			boolean isProgess, final ModelManagerListener listener) {
		String url = WebserviceConfig.UNBLOCK_USER;

		JSONObject json = new JSONObject();
		try {
			json.put("BlockingId", userId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AsyncHttpPost post = new AsyncHttpPost(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, json.toString(), isProgess);
		// post.execute(WebserviceConfig.UNBLOCK_USER);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void unFollowUser(Context context, String userId,
			boolean isProgess, final ModelManagerListener listener) {

		String url = WebserviceConfig.UNFOLLOW_USER;
		JSONObject json = new JSONObject();
		try {
			json.put("FollowingId", userId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AsyncHttpPost post = new AsyncHttpPost(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, json.toString(), isProgess);
		// post.execute(WebserviceConfig.UNFOLLOW_USER);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void updateAvatar(Context context, File file,
			boolean isProgess, final ModelManagerListener listener) {

		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);
		String accessToken = pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("image", file);

		AjaxCallback<String> cb = new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				// TODO Auto-generated method stub
				listener.onSuccess(object);
			}

		};
		cb.header(WebserviceConfig.KEY_HEADER_AUTHORIZATION, accessToken);
		// cb.method(AQuery.METHOD_PUT);
		AQuery aq = new AQuery(context);
		aq.ajax(WebserviceConfig.UPDATE_AVATAR, params, String.class, cb);
	}

	public static void updateLocation(Context context, LocationObj location,
			boolean isProgess, final ModelManagerListener listener) {

		String url = WebserviceConfig.UPDATE_LOCATION;
		JSONObject json = new JSONObject();

		try {
			json.put("Id", location.getId());
			json.put("Country", location.getCountry());
			json.put("CountryCode", location.getCountryCode());
			json.put("State", location.getState());
			json.put("Suburb", location.getDistrict());
			json.put("Longitude", location.getLongitude());
			json.put("Latitude", location.getLatitude());
			json.put("ShortDescription", location.getShortDescription());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AsyncHttpPut post = new AsyncHttpPut(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, json.toString(), isProgess);
		// post.execute(WebserviceConfig.UPDATE_LOCATION);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void getLocationInfo(Context context, double latitude,
			double longitude, boolean isProgess,
			final ModelManagerListener listener) {
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("latlng", latitude + "," + longitude));
		params.add(new BasicNameValuePair("sensor", "true"));
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS)
								listener.onSuccess(response);
							else
								listener.onError(StatusCode, response);
						} else
							listener.onError();
					}
				}, params, isProgess);
		post.execute("http://maps.googleapis.com/maps/api/geocode/json");
	}

	public static void getLocationFromGoogle(Context context, double latitude,
			double longitude, boolean isProgess,
			final ModelManagerListener listener) {

		String url = "http://maps.googleapis.com/maps/api/geocode/json?latlng="
				+ latitude + "," + longitude + "&sensor=true";

		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS) {
								listener.onSuccess(response);
							} else {
								listener.onError(StatusCode, response);
							}
						} else
							listener.onError();
					}
				}, new ArrayList<NameValuePair>(), isProgess);
		post.execute(url);
	}

	public static void getcategories(Context context, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.GET_CATEGORIES;

		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS) {
								listener.onSuccess(response);
							} else {
								listener.onError(StatusCode, response);
							}
						} else
							listener.onError();
					}
				}, new ArrayList<NameValuePair>(), isProgess);
		// post.execute(url);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void getLocationBySearching(Context context, String url,
			boolean isProgess, final ModelManagerListener listener) {

		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						Log.e(TAG, "Search result: " + response);
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS) {
								listener.onSuccess(response);
							} else {
								listener.onError(StatusCode, response);
							}
						} else
							listener.onError();
					}
				}, new ArrayList<NameValuePair>(), isProgess);
		post.execute(url);
	}

	public static void getActivityFeed(Context context, boolean isProgress,
			final ModelManagerListener listener) {
		String url = WebserviceConfig.ACTIVITY_FEED;

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("lastActivityId", "0"));
		AsyncHttpGet post = new AsyncHttpGet(context,
				new AsyncHttpResponseProcess() {
					@Override
					public void processIfResponseSuccess(String response,
							int StatusCode) {
						// Log.e(TAG, "Search result: " + response);
						if (response != null) {
							if (StatusCode == AsyncHttpBase.RESPONSE_SUCCESS) {
								listener.onSuccess(response);
							} else {
								listener.onError(StatusCode, response);
							}
						} else
							listener.onError();
					}
				}, params, isProgress);
		// post.execute(WebserviceConfig.ACTIVITY_FEED);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}
}