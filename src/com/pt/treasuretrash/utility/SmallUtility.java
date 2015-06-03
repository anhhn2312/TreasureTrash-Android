package com.pt.treasuretrash.utility;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.WindowManager;

public class SmallUtility {

	public static void hiddenKeyboard(Activity act) {
		act.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	public static boolean isAppInstalled(Context context, String packageName) {
		try {
			context.getPackageManager().getApplicationInfo(packageName, 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
	//com.facebook.katana
	public static boolean isFacebookAppInstalled(Context context) {
		try {
			context.getPackageManager().getApplicationInfo("com.facebook.katana", 0);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}
}
