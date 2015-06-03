package com.pt.treasuretrash.config;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;

public class TreasureTrashSharedPreferences {
	private String TAG = getClass().getSimpleName();

	public static final String APPLICATION_INSTALL_FIRST_TIME = "APPLICATION_INSTALL_FIRST_TIME";

	public static final String PREF_SETTING_USER_ID = "PREF_SETTING_USER_ID";

	public static final String PREF_SETTING_USER_ACCESS_TOKEN = "PREF_SETTING_USER_ACCESS_TOKEN";
	
	public static final String PREF_LAST_TIME_RECEIVED_MESSAGE = "PREF_LAST_TIME_RECEIVED_MESSAGE";

	public static final String PREF_FACEBOOK_ACCESS_TOKEN = "PREF_FACEBOOK_ACCESS_TOKEN";

	public static final String USE_ACTIVE_MAP = "USE_ACTIVE_MAP";

	public static final String PREF_MY_CURRENT_LOCATION_LNG = "PREF_MY_CURRENT_LOCATION_LNG";
	public static final String PREF_MY_CURRENT_LOCATION_LAT = "PREF_MY_CURRENT_LOCATION_LAT";
	public static final String PREF_SEARCH_LOCATION_LNG = "PREF_SEARCH_LOCATION_LNG";
	public static final String PREF_SEARCH_LOCATION_LAT = "PREF_SEARCH_LOCATION_LAT";
	public static final String PREF_SEARCH_LOCATION_NAME = "PREF_SEARCH_LOCATION_NAME";
	public static final String PREF_SEARCH_LOCATION_USE_CURRENT = "PREF_SEARCH_LOCATION_USE_CURRENT";

	public static final String PREF_DEFAULT_LANGUAGE = "PREF_DEFAULT_LANGUAGE";

	public static final String PREF_IS_FIRST_TIME_INSTALLED = "PREF_IS_FIRST_TIME_INSTALLED";
	public static final String PREF_IS_LOCATION_ALLOWED = "PREF_IS_LOCATION_ALLOWED";
	public static final String PREF_HOME_FIRST_LAUNCH = "PREF_HOME_FIRST_LAUNCH";
	public static final String PREF_TURN_OFF_RATE_REMIND = "PREF_TURN_OFF_RATE_REMIND";

	public static final String PREF_ACCOUNT_ID = "PREF_ACCOUNT_ID";
//	public static final String PREF_PUBNUB_CHANNEL = "PREF_PUBNUB_CHANNEL";
	public static final String PREF_ACCESS_TOKEN = "PREF_ACCESS_TOKEN";
	public static final String PREF_REFRESH_TOKEN = "PREF_REFRESH_TOKEN";
	public static final String PREF_TOKEN_EXPIRED_TIME = "PREF_TOKEN_EXPIRED_TIME";
	public static final String PREF_ALL_CATEGORIES = "PREF_ALL_CATEGORIES";
	
	
//	public static final String PREF_DISABLED_CATEGORIES = "PREF_DISABLED_CATEGORIES";

	// Pref Account
	public static String PREF_ACCOUNT_JSON= "account_json";
	public static String PREF_PASSWORD = "password";
	public static String PREF_USER_ID = "user_id";
	public static String PREF_NEW_EMAIL= "new_email";

	// font app
	public static String PREF_FONT_APP = "font_app";

	public String getFont() {
		return getStringValue(PREF_FONT_APP);
	}

	public void setFont(String font) {
		putStringValue(PREF_FONT_APP, font);
	}

	// ================================================================

	private static TreasureTrashSharedPreferences instance;

	private Context context;

	private TreasureTrashSharedPreferences() {
	}

	/**
	 * Constructor
	 * 
	 * @param context
	 * @return
	 */
	public static TreasureTrashSharedPreferences getInstance(Context context) {
		if (instance == null) {
			instance = new TreasureTrashSharedPreferences();
			instance.context = context;
		}
		return instance;
	}

	public TreasureTrashSharedPreferences(Context context) {
		this.context = context;
	}

	// ======================== UTILITY FUNCTIONS ========================

	// ======================== CORE FUNCTIONS ========================

	/**
	 * Save a long integer to SharedPreferences
	 * 
	 * @param key
	 * @param n
	 */
	public void putLongValue(String key, long n) {
		// SmartLog.log(TAG, "Set long integer value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putLong(key, n);
		editor.commit();
	}

	/**
	 * Read a long integer to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public long getLongValue(String key) {
		// SmartLog.log(TAG, "Get long integer value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		return pref.getLong(key, 0);
	}

	/**
	 * Save an integer to SharedPreferences
	 * 
	 * @param key
	 * @param n
	 */
	public void putIntValue(String key, int n) {
		// SmartLog.log(TAG, "Set integer value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putInt(key, n);
		editor.commit();
	}

	/**
	 * Read an integer to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public int getIntValue(String key) {
		// SmartLog.log(TAG, "Get integer value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		return pref.getInt(key, 0);
	}

	/**
	 * Save an string to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putStringValue(String key, String s) {
		// SmartLog.log(TAG, "Set string value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putString(key, s);
		editor.commit();
	}

	/**
	 * Read an string to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public String getStringValue(String key) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		return pref.getString(key, "");
	}

	/**
	 * Read an string to SharedPreferences
	 * 
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public String getStringValue(String key, String defaultValue) {
		// SmartLog.log(TAG, "Get string value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		return pref.getString(key, defaultValue);
	}

	/**
	 * Save an boolean to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putBooleanValue(String key, Boolean b) {
		// SmartLog.log(TAG, "Set boolean value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putBoolean(key, b);
		editor.commit();
	}

	/**
	 * Read an boolean to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public boolean getBooleanValue(String key) {
		// SmartLog.log(TAG, "Get boolean value");
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		return pref.getBoolean(key, false);
	}

	/**
	 * Save an float to SharedPreferences
	 * 
	 * @param key
	 * @param s
	 */
	public void putFloatValue(String key, float f) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		SharedPreferences.Editor editor = pref.edit();
		editor.putFloat(key, f);
		editor.commit();
	}
	

	/**
	 * Read an float to SharedPreferences
	 * 
	 * @param key
	 * @return
	 */
	public float getFloatValue(String key) {
		SharedPreferences pref = context.getSharedPreferences(
				GlobalValue.TREASURE_TRASH_PREFERENCES, 0);
		return pref.getFloat(key, 0.0f);
	}

}
