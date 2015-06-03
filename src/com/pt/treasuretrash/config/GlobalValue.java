package com.pt.treasuretrash.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.Item;

public class GlobalValue {

	public static final String TREASURE_TRASH_PREFERENCES = "treasure_trash_preferences";

	public static Account myAccount;

	public static boolean HOME_FIRST_LAUNCH = true;
	
	// public static ArrayList<Category> arrCategory;

	// Location
	// public static Double mCurrentLat = 0d, mCurrentLng = 0d;

	// Gallery
	public static List<Item> arrItems;
	public static int currentItemPosition;
	public static int GALLERY_PAGE_SIZE = 30;
	public static int GALLERY_ITEM_SIZE = 300;
	public static int TOKEN_ALIVE_TIME = 59*24 * 60*60; // 59 days
	// public static int TOKEN_ALIVE_TIME = 10*1000; //50 minutes

	public static String INTENT_ACTION_DIAMOND = "com.pt.treasuretrash.intent.action.diamond";
	public static String INTENT_ACTION_SEARCHING = "com.pt.treasuretrash.intent.action.searching";
	public static String INTENT_ACTION_REFRESH = "com.pt.treasuretrash.intent.action.refresh";
	public static String INTENT_ACTION_UPDATE_LIST = "com.pt.treasuretrash.intent.action.update.list";
	public static String INTENT_ACTION_MESSAGE_HISTORY = "com.pt.treasuretrash.intent.action.messagehistory";
	public static String INTENT_ACTION_MESSAGE_RECEIVED = "com.pt.treasuretrash.intent.action.messagereceived";
	public static String INTENT_ACTION_MESSAGE_SENT_SUCCESS = "com.pt.treasuretrash.intent.action.messagesentsuccess";
	public static String INTENT_ACTION_MESSAGE_SENT_ERROR = "com.pt.treasuretrash.intent.action.messagesenterror";
	public static String INTENT_ACTION_BLOCK_UNBLOCK = "com.pt.treasuretrash.intent.action.blockunblock";

	// Bundle Key
	public static String KEY_CATEGORY_ID = "category_id";
	public static String KEY_CATEGORY_NAME = "category_name";
	public static String KEY_ACTION = "action";
	public static String KEY_ITEM_ID = "item_id";
	public static String KEY_LOCATION_OBJECT = "location_obj";

	public static String KEY_ACTION_AFTER_LOGIN = "action_after_login";
	public static String KEY_ACTION_LOGIN_REFRESH_TOKEN = "action_login_refresh_token";
	public static String KEY_ACTION_FROM_NOTIFICATION = "action_from_notification";
	public static String KEY_LOC_NOTIFICATION = "key_loc_notification";
	public static String KEY_ACTION_FROM_EMAIL = "KEY_ACTION_FROM_EMAIL";
	public static String KEY_ACTION_AFTER_DELETE = "KEY_ACTION_AFTER_DELETE";
	public static String KEY_ACTION_REFRESH_TOKEN_FAILED = "KEY_ACTION_REFRESH_TOKEN_FAILED";
	public static String KEY_CONFIRM_CHANGE_EMAIL = "KEY_CONFIRM_CHANGE_EMAIL";

	public static String URL_REDIRECT_CONFIRM_EMAIL = "https://play.google.com/store/apps/details?id=com.pt.treasuretrash";
	
	public static String KEY_FACEBOOK_TOKEN = "KEY_FACEBOOK_TOKEN";

	// Gallery fragment keys
	public static String KEY_ACION_DISPLAY = "action_display";
	public static String KEY_ACTION_SEARCH = "action_search";
	public static String KEY_ACTION_SHOW_BY_LOCATION = "action_show_by_location";
	public static String KEY_ACTION_SHOW_PUBLISHED_LISTING = "action_show_published_listing";
	public static String KEY_ACTION_SHOW_MESSAGES = "action_show_messages";

	// Edit Item key
	public static String KEY_ACTION_EDIT_ITEM = "action_edit_item";

	// Item details
	public static String KEY_ACTION_ITEM_DETAIL = "KEY_ACTION_ITEM_DETAIL";
	public static String KEY_ACTION_ITEM_DETAIL_SINGLE_ITEM = "KEY_ACTION_ITEM_DETAIL_SINGLE_ITEM";
	public static String KEY_ACTION_FROM_GALLERY = "KEY_ACTION_FROM_GALLERY";
	public static String KEY_ACTION_FROM_FAVORITE = "KEY_ACTION_FROM_FAVORITE";

	// Current item for contacting seller
	public static Item currentItem;

	// Message
	// public static List<MessageGroup> arrMessageGroup;
	public static String KEY_MESSAGE_HISTORY = "message_history";
	public static String KEY_MESSAGE_RECEIVED = "message_history";

	public static Item publishedItem;

	// Activity Feed
	public static String KEY_SELLER_ID = "seller_id";

	public static String FORCED_LOGIN_ACTION = "forced_login_action";
	public static String KEY_FORCED_LOGIN_ACTION_FAVORITE = "KEY_FORCED_LOGIN_ACTION_FAVORITE";
	public static String KEY_FORCED_LOGIN_ACTION_FLAG_ITEM = "KEY_FORCED_LOGIN_ACTION_FLAG_ITEM";
	public static String KEY_FORCED_LOGIN_ACTION_SHARE = "KEY_FORCED_LOGIN_ACTION_SHARE";
	public static String KEY_FORCED_LOGIN_ACTION_CONTACT_SELLER = "KEY_FORCED_LOGIN_ACTION_CONTACT_SELLER";
	public static String KEY_FORCED_LOGIN_ACTION_FOLLOW_SELLER = "KEY_FORCED_LOGIN_ACTION_FOLLOW_SELLER";
	public static String KEY_FORCED_LOGIN_ACTION_SELLER_PROFILE = "KEY_FORCED_LOGIN_ACTION_SELLER_PROFILE";
	public static String KEY_FORCED_LOGIN_ACTION_MESSAGE = "KEY_FORCED_LOGIN_ACTION_MESSAGE";
	public static String KEY_FORCED_LOGIN_ACTION_SAVED_ITEM = "KEY_FORCED_LOGIN_ACTION_SAVED_ITEM";
	public static String KEY_FORCED_LOGIN_ACTION_ACTIVITY_FEED = "KEY_FORCED_LOGIN_ACTIVITY_FEED";
	public static String KEY_FORCED_LOGIN_ACTION_ADD_LISTING = "KEY_FORCED_LOGIN_ADD_LISTING";
	public static String KEY_DISPLAY_AFTER_PUBLISH = "KEY_DISPLAY_AFTER_PUBLISH";
	
	public static final Map<String, String> CURRENCY_MAP = new HashMap<String, String>(){
        {
        	put("ALL","Lek");
            put("AFN","؋");
            put("ARS","$");
            put("AWG","ƒ");
            put("AUD","$");
            put("AZN","ман");
            put("BSD","$");
            put("BBD","$");
            put("BYR","p.");
            put("BZD","BZ$");
            put("BMD","$");
            put("BOB","$b");
            put("BAM","KM");
            put("BWP","P");
            put("BGN","лв");
            put("BRL","R$");
            put("BND","$");
            put("KHR","៛");
            put("CAD","$");
            put("KYD","$");
            put("CLP","$");
            put("CNY","¥");
            put("COP","$");
            put("CRC","₡");
            put("HRK","kn");
            put("CUP","₱");
            put("CZK","Kč");
            put("DKK","kr");
            put("DOP","RD$");
            put("XCD","$");
            put("EGP","£");
            put("SVC","$");
            put("EEK","kr");
            put("EUR","€");
            put("FKP","£");
            put("FJD","$");
            
            put("GHC","¢");
            put("GIP","£");
            put("GTQ","Q");
            put("GGP","£");
            put("GYD","$");
            put("HNL","L");
            put("HKD","$");
            put("HUF","Ft");
            put("ISK","kr");
            put("INR","₹");
            put("IDR","Rp");
            put("IRR","﷼");
            put("IMP","£");
            put("ILS","₪");
            put("JMD","J$");
            put("JPY","¥");
            put("JEP","£");
            put("KZT","лв");
            put("KPW","₩");
            put("KRW","₩");
            put("KGS","лв");
            put("LAK","₭");
            put("LVL","Ls");
            put("LBP","£");
            put("LRD","$");
            put("LTL","Lt");
            put("MKD","ден");
            put("MYR","RM");
            put("MUR","₨");
            put("MXN","$");
            put("MNT","₮");
            put("MZN","MT");
            put("NAD","$");
            put("NPR","₨");
            put("ANG","ƒ");
            put("NZD","$");
            put("NIO","C$");
            put("NGN","₦");
            put("NOK","kr");
            put("OMR","﷼");
            put("PKR","₨");
            put("PAB","B/.");
            put("PYG","Gs");
            put("PEN","S/.");
            put("PHP","₱");
            put("PLN","zł");
            put("QAR","﷼");
            put("RON","lei");
            put("RUB","руб");
            
            put("SHP","£");
            put("SAR","﷼");
            put("RSD","Дин.");
            put("SCR","₨");
            put("SGD","$");
            put("SBD","$");
            put("SOS","S");
            put("ZAR","S");
            put("LKR","₨");
            put("SEK","kr");
            put("CHF","CHF");
            put("SRD","$");
            put("SYP","£");
            put("TWD","NT$");
            put("THB","฿");
            put("TTD","TT$");
            put("TRL","₤");
            put("TVD","$");
            put("UAH","₴");
            put("GBP","£");
            put("USD","$");
            put("UYU","$U");
            put("UZS","лв");
            put("VEF","Bs");
            put("VND","₫");
            put("YER","﷼");
            put("ZWD","Z$");
            
        }
};

	// ====================================================================

}
