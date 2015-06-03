package com.pt.treasuretrash.utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.widget.EditText;

public final class StringUtility {
	public static boolean isEmpty(EditText editText) {
		if (editText == null
				|| editText.getEditableText() == null
				|| editText.getEditableText().toString().trim()
						.equalsIgnoreCase("")) {
			return true;
		}
		return false;
	}

	/**
	 * Check input string
	 * 
	 * @param editText
	 * @return
	 */
	public static boolean isEmpty(String editText) {
		if (editText == null || editText.trim().equalsIgnoreCase("")) {
			return true;
		}
		return false;
	}

	/**
	 * Merge all elements of a string array into a string
	 * 
	 * @param strings
	 * @param separator
	 * @return
	 */
	public static String join(String[] strings, String separator) {
		StringBuffer sb = new StringBuffer();
		int max = strings.length;
		for (int i = 0; i < max; i++) {
			if (i != 0)
				sb.append(separator);
			sb.append(strings[i]);
		}
		return sb.toString();
	}

	/**
	 * Convert current date time to string
	 * 
	 * @param updateTime
	 * @return
	 */
	public static String convertNowToFullDateString() {
		SimpleDateFormat dateformat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		dateformat.setTimeZone(TimeZone.getTimeZone("GMT"));

		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
		return dateformat.format(calendar.getTime());
	}

	/**
	 * Initial sync date string
	 * 
	 * @return
	 */
	public static String initDateString() {
		return "1900-01-01 09:00:00";
	}

	/**
	 * Convert a string divided by ";" to multiple xmpp users
	 * 
	 * @param userString
	 * @return
	 */
	public static String[] convertStringToXmppUsers(String userString) {
		return userString.split(";");
	}

	/**
	 * get Unique Random String
	 * 
	 * @return
	 */

	public static String getUniqueRandomString() {

		return String.valueOf(System.currentTimeMillis());

	}

	public static boolean isEmailValid(String email) {
		return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
	}

	public static String getTimeString(long millis) {
		StringBuffer buf = new StringBuffer();

		int hours = (int) (millis / (1000 * 60 * 60));
		int minutes = (int) ((millis % (1000 * 60 * 60)) / (1000 * 60));
		int seconds = (int) (((millis % (1000 * 60 * 60)) % (1000 * 60)) / 1000);

		buf.append(String.format("%02d", hours)).append(":")
				.append(String.format("%02d", minutes)).append(":")
				.append(String.format("%02d", seconds));

		return buf.toString();
	}

	public static String milliSecondsToTimer(long milliseconds) {
		String finalTimerString = "";
		String secondsString = "";

		// Convert total duration into time
		int hours = (int) (milliseconds / (1000 * 60 * 60));
		int minutes = (int) (milliseconds % (1000 * 60 * 60)) / (1000 * 60);
		int seconds = (int) ((milliseconds % (1000 * 60 * 60)) % (1000 * 60) / 1000);
		// Add hours if there
		if (hours > 0) {
			finalTimerString = hours + ":";
		}

		// Prepending 0 to seconds if it is one digit
		if (seconds < 10) {
			secondsString = "0" + seconds;
		} else {
			secondsString = "" + seconds;
		}

		finalTimerString = finalTimerString + minutes + ":" + secondsString;

		// return timer string
		return finalTimerString;
	}

	public static int getProgressPercentage(long currentDuration,
			long totalDuration) {
		Double percentage = (double) 0;

		long currentSeconds = (int) (currentDuration / 1000);
		long totalSeconds = (int) (totalDuration / 1000);

		// calculating percentage
		percentage = (((double) currentSeconds) / totalSeconds) * 100;

		// return percentage
		return percentage.intValue();
	}

	public static int progressToTimer(int progress, int totalDuration) {
		int currentDuration = 0;
		totalDuration = (int) (totalDuration / 1000);
		currentDuration = (int) ((((double) progress) / 100) * totalDuration);

		// return current duration in milliseconds
		return currentDuration * 1000;
	}

	public static String convertTimeStampToDate(String timeStamp,
			String outputFormat) {
		SimpleDateFormat formater = new SimpleDateFormat(outputFormat,
				Locale.getDefault());
		Date date = new Date(Long.parseLong(timeStamp));
		return formater.format(date);
	}
	
	public static String getEmailFromMailToUrl(String url){
		String mail="";
		
		if(url != null){
			mail = url.substring(url.indexOf(":")+1);
		}
		
		return mail;
	}
	
}
