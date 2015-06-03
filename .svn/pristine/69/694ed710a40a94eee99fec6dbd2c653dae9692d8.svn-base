package com.pt.treasuretrash.utility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import android.util.Log;

/**
 * This utility class contains all Date operations
 * 
 * @author |dmb TEAM|
 * 
 */
public class DateUtil {

	/**
	 * Date format for the posts in the main page.
	 */
	public static final String Defaut_Birthday = "01.01.1990";
	public static final String POST_DATE_MAIN_LIST_ITEM = "dd.MM.yyyy";
	public static final String DATE_FORMAT_FACEBOOK = "MM/dd/yyyy";
	public static final String DATE_FORMAT_GOOGLE = "yyyy-MM-dd";

	/**
	 * Date format for the post in the single post page.
	 */
	public static final String POST_DATE_TITLE_FORMAT = "dd MMMM yyyy";

	/**
	 * Date format for the published posts.
	 */
	public static final String POSTS_DATE_PUBLISHED_FORMAT = "yyyy-MM-dd hh:mm:ss";

	public static final String POST_DATE_PUBLISHED_FULL_FORMAT = "EEE, dd MMM yyyy hh:mm:ss Z";

	public static final SimpleDateFormat facebookDateFormat = new SimpleDateFormat(
			DATE_FORMAT_FACEBOOK);
	
	public static final SimpleDateFormat googleDateFormat = new SimpleDateFormat(
			DATE_FORMAT_GOOGLE);
	/**
	 * <code>SimpleDateFormat</code> for the posts in the main page.
	 */
	public static final SimpleDateFormat postDateMainListItemFormat = new SimpleDateFormat(
			POST_DATE_MAIN_LIST_ITEM);

	/**
	 * <code>SimpleDateFormat</code> for the post in the single post page.
	 */
	public static final SimpleDateFormat postDateTitleFormat = new SimpleDateFormat(
			POST_DATE_TITLE_FORMAT);

	/**
	 * <code>SimpleDateFormat</code> for the published posts.
	 */
	public static final SimpleDateFormat postsDatePublishedFormatter = new SimpleDateFormat(
			POSTS_DATE_PUBLISHED_FORMAT);

	/**
	 * <code>SimpleDateFormat</code> for the published posts.
	 */
	public static final SimpleDateFormat postsFullDatePublishedFormatter = new SimpleDateFormat(
			POST_DATE_PUBLISHED_FULL_FORMAT);
	
	public static long getUTCTime(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

		try {
			Date utcDate = sdf.parse(sdf.format(new Date()));
//			utcDate.getTime();
			Log.d("UTC_TIME", utcDate.getTime()/1000 + "");
			return utcDate.getTime()/1000;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}
