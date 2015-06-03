package com.pt.treasuretrash.utility;

import java.util.Calendar;

import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;

import android.content.Context;

public class AppUtil {

	public static boolean isAccessTokenExpired(Context context){
		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);
		
		long currentTime = Calendar.getInstance().getTimeInMillis()/1000;
		
		if (!pref.getStringValue(
				TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN).equals("")
				&& pref.getLongValue(TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME) < currentTime) {
			return true;
		}
		return false;
	}
}
