package com.pt.treasuretrash.modelmanager;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;

import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.Category;
import com.pt.treasuretrash.object.Feed;
import com.pt.treasuretrash.object.ImageObj;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.object.MessageItem;
import com.pt.treasuretrash.object.SocialAccount;
import com.pt.treasuretrash.utility.DateUtil;
import com.pt.treasuretrash.utility.ImageUtil;

public class ParserUtility {

	private final static String KEY_DATA = "data";

	public static List<Item> parseListItem(String json) {
		List<Item> list = new ArrayList<Item>();
		try {
			JSONArray entry = new JSONArray(json);

			for (int i = 0; i < entry.length(); i++) {
				JSONObject obj = entry.getJSONObject(i);
				Item item = new Item();

				item.setTitle(getStringValue(obj, "title"));
				item.setCost(getLongValue(obj, "cost"));
				item.setCurrency(getStringValue(obj, "currency"));
				item.setDescription(getStringValue(obj, "description"));
				item.setState(getStringValue(obj, "state"));
				item.setStatus(getStringValue(obj, "status"));
				item.setType(getStringValue(obj, "type"));
				item.setDistance(getDoubleValue(obj, "distance"));
				item.setImage(getStringValue(obj, "image"));
				item.setSlotImage(getIntValue(obj, "slotImage"));
				item.setSlotType(getStringValue(obj, "slotType"));
				item.setFavourited(getBooleanValue(obj, "isFavourited"));
				item.setFlagged(getBooleanValue(obj, "isFlagged"));
				item.setExpiredDate(getLongValue(obj, "expiredDate") * 1000);
				item.setId(getStringValue(obj, "id"));
				item.setUserID(getStringValue(obj, "userId"));
				item.setUserName(getStringValue(obj, "userName"));
				item.setRewardType(getStringValue(obj, "rewardType"));

				if (isJsonObject(obj, "categoryModel")) {
					JSONObject categoryObj = obj.getJSONObject("categoryModel");
					Category category = new Category();
					category.setId(getStringValue(categoryObj, "id"));
					category.setName(getStringValue(categoryObj, "name"));
					category.setHidden(getBooleanValue(categoryObj, "isHidden"));
					category.setParentCategoryId(getStringValue(categoryObj,
							"parentCategoryId"));
					item.setCategory(category);
				}

				if (isJsonObject(obj, "location")) {

					JSONObject locationObj = obj.getJSONObject("location");
					LocationObj location = new LocationObj();
					location.setId(getStringValue(locationObj, "id"));
					location.setCountry(getStringValue(locationObj, "country"));
					location.setCountryCode(getStringValue(locationObj,
							"countryCode"));
					location.setState(getStringValue(locationObj, "state"));
					location.setDistrict(getStringValue(locationObj, "suburb"));
					location.setLongitude(getDoubleValue(locationObj,
							"longitude"));
					location.setLatitude(getDoubleValue(locationObj, "latitude"));
					location.setShortDescription(getStringValue(locationObj,
							"shortDescription"));
					item.setLocation(location);
				}

				JSONArray imagesArr = obj.getJSONArray("itemImages");
				ArrayList<ImageObj> arrImagesObj = new ArrayList<ImageObj>();
				for (int j = 0; j < imagesArr.length(); j++) {
					ImageObj image = new ImageObj();
					JSONObject imageObj = imagesArr.getJSONObject(j);
					image.setId(getStringValue(imageObj, "id"));
					image.setImageUrl(getStringValue(imageObj, "imageUrl"));
					arrImagesObj.add(image);
				}
				item.setArrImages(arrImagesObj);

				list.add(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static Item parseItem(String json) {
		Item item = new Item();
		try {
			JSONObject obj = new JSONObject(json);
			item.setTitle(getStringValue(obj, "title"));
			item.setCost(getLongValue(obj, "cost"));
			item.setCurrency(getStringValue(obj, "currency"));
			item.setDescription(getStringValue(obj, "description"));
			item.setState(getStringValue(obj, "state"));
			item.setStatus(getStringValue(obj, "status"));
			item.setType(getStringValue(obj, "type"));
			item.setDistance(getDoubleValue(obj, "distance"));
			item.setImage(getStringValue(obj, "image"));
			item.setSlotImage(getIntValue(obj, "slotImage"));
			item.setSlotType(getStringValue(obj, "slotType"));
			item.setFavourited(getBooleanValue(obj, "isFavourited"));
			item.setFlagged(getBooleanValue(obj, "isFlagged"));
			item.setExpiredDate(getLongValue(obj, "expiredDate") * 1000);
			item.setId(getStringValue(obj, "id"));
			item.setUserID(getStringValue(obj, "userId"));
			item.setUserName(getStringValue(obj, "userName"));
			item.setRewardType(getStringValue(obj, "rewardType"));

			if (isJsonObject(obj, "categoryModel")) {
				JSONObject categoryObj = obj.getJSONObject("categoryModel");
				Category category = new Category();
				category.setId(getStringValue(categoryObj, "id"));
				category.setName(getStringValue(categoryObj, "name"));
				category.setHidden(getBooleanValue(categoryObj, "isHidden"));
				category.setParentCategoryId(getStringValue(categoryObj,
						"parentCategoryId"));
				item.setCategory(category);
			}

			if (isJsonObject(obj, "location")) {

				JSONObject locationObj = obj.getJSONObject("location");
				LocationObj location = new LocationObj();
				location.setId(getStringValue(locationObj, "id"));
				location.setCountry(getStringValue(locationObj, "country"));
				location.setCountryCode(getStringValue(locationObj,
						"countryCode"));
				location.setState(getStringValue(locationObj, "state"));
				location.setDistrict(getStringValue(locationObj, "suburb"));
				location.setLongitude(getDoubleValue(locationObj, "longitude"));
				location.setLatitude(getDoubleValue(locationObj, "latitude"));
				location.setShortDescription(getStringValue(locationObj,
						"shortDescription"));
				item.setLocation(location);
			}

			JSONArray imagesArr = obj.getJSONArray("itemImages");
			ArrayList<ImageObj> arrImagesObj = new ArrayList<ImageObj>();
			for (int i = 0; i < imagesArr.length(); i++) {
				ImageObj image = new ImageObj();
				JSONObject imageObj = imagesArr.getJSONObject(i);
				image.setId(getStringValue(imageObj, "id"));
				image.setImageUrl(getStringValue(imageObj, "imageUrl"));
				arrImagesObj.add(image);
			}
			item.setArrImages(arrImagesObj);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return item;
	}

	public static List<Item> parseListFavoriteItem(String json, int imageSize) {
		List<Item> list = new ArrayList<Item>();
		try {
			JSONArray entry = new JSONArray(json);

			for (int i = 0; i < entry.length(); i++) {
				JSONObject entryObj = entry.getJSONObject(i);
				Item item = new Item();

				JSONObject obj = entryObj.getJSONObject("item");

				item.setTitle(getStringValue(obj, "title"));
				item.setCost(getIntValue(obj, "cost"));
				item.setCurrency(getStringValue(obj, "currency"));
				item.setDescription(getStringValue(obj, "description"));
				item.setState(getStringValue(obj, "state"));
				item.setStatus(getStringValue(obj, "status"));
				item.setType(getStringValue(obj, "type"));
				item.setDistance(getDoubleValue(obj, "distance"));

				String strImage = getStringValue(obj, "image");
				if (strImage.length() > 3) {
					strImage = strImage.substring(0, strImage.length() - 3);
					strImage += imageSize;
				}
				item.setImage(strImage);

				item.setImage(getStringValue(obj, "image"));

				item.setSlotImage(getIntValue(obj, "slotImage"));
				item.setSlotType(getStringValue(obj, "slotType"));
				item.setFavourited(getBooleanValue(obj, "isFavourited"));
				item.setFlagged(getBooleanValue(obj, "isFlagged"));
				item.setExpiredDate(getLongValue(obj, "expiredDate") * 1000);
				item.setId(getStringValue(obj, "id"));
				item.setUserID(getStringValue(obj, "userId"));
				item.setUserName(getStringValue(obj, "userName"));
				item.setRewardType(getStringValue(obj, "rewardType"));

				if (isJsonObject(obj, "categoryModel")) {
					JSONObject categoryObj = obj.getJSONObject("categoryModel");
					Category category = new Category();
					category.setId(getStringValue(categoryObj, "id"));
					category.setName(getStringValue(categoryObj, "name"));
					category.setHidden(getBooleanValue(categoryObj, "isHidden"));
					category.setParentCategoryId(getStringValue(categoryObj,
							"parentCategoryId"));
					item.setCategory(category);
				}

				if (isJsonObject(obj, "location")) {

					JSONObject locationObj = obj.getJSONObject("location");
					LocationObj location = new LocationObj();
					location.setId(getStringValue(locationObj, "id"));
					location.setCountry(getStringValue(locationObj, "country"));
					location.setCountryCode(getStringValue(locationObj,
							"countryCode"));
					location.setState(getStringValue(locationObj, "state"));
					location.setDistrict(getStringValue(locationObj, "suburb"));
					location.setLongitude(getDoubleValue(locationObj,
							"longitude"));
					location.setLatitude(getDoubleValue(locationObj, "latitude"));
					location.setShortDescription(getStringValue(locationObj,
							"shortDescription"));
					item.setLocation(location);
				}

				JSONArray imagesArr = obj.getJSONArray("itemImages");
				ArrayList<ImageObj> arrImagesObj = new ArrayList<ImageObj>();
				for (int j = 0; j < imagesArr.length(); j++) {
					ImageObj image = new ImageObj();
					JSONObject imageObj = imagesArr.getJSONObject(j);
					image.setId(getStringValue(imageObj, "id"));
					image.setImageUrl(getStringValue(imageObj, "imageUrl"));
					arrImagesObj.add(image);
				}
				item.setArrImages(arrImagesObj);

				list.add(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<Item> parseListCheckExpiredItems(String json) {
		List<Item> list = new ArrayList<Item>();

		try {
			JSONArray arr = new JSONArray(json);
			for (int i = 0; i < arr.length(); i++) {
				JSONObject obj = arr.getJSONObject(i);
				Item item = new Item();
				item.setId(getStringValue(obj, "id"));
				item.setTitle(getStringValue(obj, "title"));
				item.setUserID(getStringValue(obj, "userName"));
				item.setExpired(getBooleanValue(obj, "isExpired"));

				list.add(item);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static Account parseFacebookAccount(String tokenAccess,
			String facebookJson) {
		Account account = new Account();
		account.setExternalAccessToken(tokenAccess);
		account.setExternalAccount(true);
		account.setAccount_type(Account.ACC_FACEBOOK);
		// parse facebook json about me
		JSONObject accountJson = null;
		try {
			accountJson = new JSONObject(facebookJson);
			account.setEmail(getStringValue(accountJson, "email"));
			account.setName(getStringValue(accountJson, "name"));
			// get gender :
			String gender = getStringValue(accountJson, "gender");
			account.setMale(gender.equalsIgnoreCase("male"));
			// get date
			if (!accountJson.isNull("birthday")) {
				String strDate = getStringValue(accountJson, "birthday");
				Date date = null;
				try {
					date = DateUtil.facebookDateFormat.parse(strDate);
				} catch (ParseException e) {
					date = DateUtil.postDateMainListItemFormat
							.parse(DateUtil.Defaut_Birthday);
				}
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				account.setBirthDay(calendar.getTimeInMillis());
			}
			// get image
			String id = getStringValue(accountJson, "id");
			account.setImageUrl("https://graph.facebook.com/" + id + "/picture");

			Log.e("Register", "Facebook info: " + facebookJson);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return account;
	}

	public static Account parseGoogleAccount(String email, String accesstoken,
			String json) {
		Account account = new Account();
		account.setExternalAccessToken(accesstoken);
		account.setExternalAccount(true);
		account.setAccount_type(Account.ACC_GOOGLE);

		JSONObject accountJson = null, nameJson = null, imageJson = null;
		try {
			accountJson = new JSONObject(json);

			account.setEmail(email);
			// get name
			nameJson = accountJson.getJSONObject("name");
			account.setName(getStringValue(nameJson, "givenName") + " "
					+ getStringValue(nameJson, "familyName"));
			// get gender :
			String gender = getStringValue(accountJson, "gender");
			account.setMale(gender.equalsIgnoreCase("male"));
			// get date
			if (accountJson.isNull("birthday")) {
				Date date = DateUtil.postDateMainListItemFormat
						.parse(DateUtil.Defaut_Birthday);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				account.setBirthDay(calendar.getTimeInMillis());
			} else {
				String birthday = getStringValue(accountJson, "birthday");
				Date date = DateUtil.googleDateFormat.parse(birthday);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(date);
				account.setBirthDay(calendar.getTimeInMillis());
			}
			// get image
			nameJson = accountJson.getJSONObject("image");
			account.setImageUrl(getStringValue(nameJson, "url"));
			account.setExternalType(AccountModelManager.PROVIDER_GOOGLE);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return account;
	}

	public static LocationObj parseLocation(Double latitude, Double longitude,
			String json) {
		LocationObj location = new LocationObj();
		location.setLatitude(latitude);
		location.setLongitude(longitude);

		try {
			JSONObject jsonObj = new JSONObject(json);
			JSONArray arrJsons = null;
			JSONObject address;
			if (!jsonObj.isNull("results")) {
				arrJsons = jsonObj.getJSONArray("results");
				if (arrJsons.length() > 0) {
					address = arrJsons.getJSONObject(0);
					location.setCountry(address.getString(""));
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return location;
	}

	public static Account parseAccount(String json,
			TreasureTrashSharedPreferences pref) {
		Account account = new Account();
		LocationObj locObj = new LocationObj();
		JSONObject accountJson = null;
		try {
			accountJson = new JSONObject(json);

			account.setId(accountJson.getString("id"));
			pref.putStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID,
					accountJson.getString("id"));

			account.setName(accountJson.getString("name"));
			account.setImageUrl(accountJson.getString("imageUrl"));
			account.setUsername(accountJson.getString("userName"));
			account.setEmail(accountJson.getString("email"));
			account.setBirthDay(accountJson.getLong("birthday") * 1000);
			account.setMale(accountJson.getBoolean("gender"));
			account.setPassword(accountJson.getString("password"));
			account.setReceiveNewsLetter(accountJson
					.getBoolean("receiveNewsLetter"));
			account.setRewardType(accountJson.getString("rewardType"));
			account.setNumberFreeSlot(accountJson.getInt("numberOfFreeSlot"));
			account.setNumberFreeSlotActive(accountJson
					.getInt("numberOfFreeSlotActive"));
			account.setNumberSingleUseSlot(accountJson
					.getInt("numberOfSingleUseSlot"));
			account.setNumberSingleSlotActive(accountJson
					.getInt("numberOfSingleUseSlotActive"));
			account.setTotalExpiredAddition(accountJson
					.getInt("totalItemExpiredOfAdditional"));
			account.setTotalExpiredDefault(accountJson
					.getInt("totalItemExpiredOfDefault"));

			// get location
			JSONObject locJson = accountJson.getJSONObject("location");
			locObj.setId(getStringValue(locJson, "id"));
			locObj.setCountry(locJson.getString("country"));
			locObj.setState(locJson.getString("state"));
			locObj.setCountryCode(locJson.getString("countryCode"));
			locObj.setDistrict(locJson.getString("suburb"));
			locObj.setLatitude(locJson.getDouble("latitude"));
			locObj.setLongitude(locJson.getDouble("longitude"));
			locObj.setShortDescription(locJson.getString("shortDescription"));

			account.setLocation(locObj);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return account;
	}

	public static Account parseAccountById(String json, int imageSize) {
		Account account = new Account();
		LocationObj locObj = new LocationObj();
		ArrayList<Item> arrItems = new ArrayList<Item>();
		try {
			JSONObject obj = new JSONObject(json);

			account.setFollowed(getBooleanValue(obj, "isFollowed"));

			account.setTotalFollower(getIntValue(obj, "totalFollower"));
			account.setTotalFollowing(getIntValue(obj, "totalFollowing"));

			JSONObject accountJsonObj = obj.getJSONObject("user");

			account.setId(getStringValue(accountJsonObj, "id"));
			account.setUsername(getStringValue(accountJsonObj, "userName"));
			account.setName(getStringValue(accountJsonObj, "name"));
			account.setEmail(getStringValue(accountJsonObj, "email"));
			account.setReceiveNewsLetter(getBooleanValue(accountJsonObj,
					"receiveNewsLetter"));
			account.setBirthDay(getLongValue(accountJsonObj, "birthday") * 1000);
			account.setMale(getBooleanValue(accountJsonObj, "gender"));
			account.setImageUrl(getStringValue(accountJsonObj, "imageUrl"));
			account.setRewardType(getStringValue(accountJsonObj, "rewardType"));
			account.setNumberFreeSlot(getIntValue(accountJsonObj,
					"numberOfFreeSlot"));
			account.setNumberFreeSlotActive(getIntValue(accountJsonObj,
					"numberOfFreeSlotActive"));
			account.setNumberSingleUseSlot(getIntValue(accountJsonObj,
					"numberOfSingleUseSlot"));
			account.setNumberSingleSlotActive(getIntValue(accountJsonObj,
					"numberOfSingleUseSlotActive"));

			JSONObject locJsonObj = accountJsonObj.getJSONObject("location");

			locObj.setId(getStringValue(locJsonObj, "id"));
			locObj.setCountry(getStringValue(locJsonObj, "country"));
			locObj.setState(getStringValue(locJsonObj, "state"));
			locObj.setCountryCode(getStringValue(locJsonObj, "countryCode"));
			locObj.setDistrict(getStringValue(locJsonObj, "suburb"));
			locObj.setLatitude(getDoubleValue(locJsonObj, "latitude"));
			locObj.setLongitude(getDoubleValue(locJsonObj, "longitude"));
			locObj.setShortDescription(getStringValue(locJsonObj,
					"shortDescription"));

			JSONArray itemsJsonArr = obj.getJSONArray("items");
			for (int i = 0; i < itemsJsonArr.length(); i++) {
				JSONObject itemObj = itemsJsonArr.getJSONObject(i);
				Item item = new Item();
				item.setTitle(getStringValue(itemObj, "title"));
				item.setCost(getIntValue(itemObj, "cost"));
				item.setCurrency(getStringValue(itemObj, "currency"));
				item.setDescription(getStringValue(itemObj, "description"));
				item.setState(getStringValue(itemObj, "state"));
				item.setStatus(getStringValue(itemObj, "status"));
				item.setType(getStringValue(itemObj, "type"));
				item.setDistance(getDoubleValue(itemObj, "distance"));

				String strImage = getStringValue(itemObj, "image");
				if (strImage.length() > 3) {
					strImage = strImage.substring(0, strImage.length() - 3);
					strImage += imageSize;

				}

				// Log.d("SELLER_ITEMS", strImage);
				item.setImage(strImage);

				item.setSlotImage(getIntValue(itemObj, "slotImage"));
				item.setSlotType(getStringValue(itemObj, "slotType"));
				item.setFavourited(getBooleanValue(itemObj, "isFavourited"));
				item.setFlagged(getBooleanValue(itemObj, "isFlagged"));
				item.setExpiredDate(getLongValue(itemObj, "expiredDate") * 1000);
				item.setId(getStringValue(itemObj, "id"));
				item.setUserID(getStringValue(itemObj, "userId"));
				item.setUserName(getStringValue(itemObj, "userName"));
				item.setRewardType(getStringValue(itemObj, "rewardType"));

				if (isJsonObject(itemObj, "categoryModel")) {
					JSONObject categoryObj = itemObj
							.getJSONObject("categoryModel");
					Category category = new Category();
					category.setId(getStringValue(categoryObj, "id"));
					category.setName(getStringValue(categoryObj, "name"));
					category.setHidden(getBooleanValue(categoryObj, "isHidden"));
					category.setParentCategoryId(getStringValue(categoryObj,
							"parentCategoryId"));
					item.setCategory(category);
				}

				if (isJsonObject(itemObj, "location")) {

					JSONObject locationObj = itemObj.getJSONObject("location");
					LocationObj location = new LocationObj();
					location.setId(getStringValue(locationObj, "id"));
					location.setCountry(getStringValue(locationObj, "country"));
					location.setCountryCode(getStringValue(locationObj,
							"countryCode"));
					location.setState(getStringValue(locationObj, "state"));
					location.setDistrict(getStringValue(locationObj, "suburb"));
					location.setLongitude(getDoubleValue(locationObj,
							"longitude"));
					location.setLatitude(getDoubleValue(locationObj, "latitude"));
					location.setShortDescription(getStringValue(locationObj,
							"shortDescription"));
					item.setLocation(location);
				}

				JSONArray imagesArr = itemObj.getJSONArray("itemImages");
				ArrayList<ImageObj> arrImagesObj = new ArrayList<ImageObj>();
				for (int j = 0; j < imagesArr.length(); j++) {
					ImageObj image = new ImageObj();
					JSONObject imageObj = imagesArr.getJSONObject(j);
					image.setId(getStringValue(imageObj, "id"));
					image.setImageUrl(getStringValue(imageObj, "imageUrl"));
					arrImagesObj.add(image);
				}
				item.setArrImages(arrImagesObj);

				arrItems.add(item);
			}

			account.setLocation(locObj);
			account.setArrItems(arrItems);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return account;
	}

	public static List<Account> parseListFollowerUsers(String json) {
		List<Account> list = new ArrayList<Account>();

		try {
			JSONArray array = new JSONArray(json);
			for (int j = 0; j < array.length(); j++) {
				JSONObject obj = array.getJSONObject(j);
				Account account = new Account();
				LocationObj locObj = new LocationObj();
				ArrayList<Item> arrItems = new ArrayList<Item>();

				account.setFollowed(getBooleanValue(obj, "currentUserFollowed"));

				JSONObject accountJsonObj = obj.getJSONObject("user");

				account.setId(getStringValue(accountJsonObj, "id"));
				account.setUsername(getStringValue(accountJsonObj, "userName"));
				account.setName(getStringValue(accountJsonObj, "name"));
				account.setEmail(getStringValue(accountJsonObj, "email"));
				account.setReceiveNewsLetter(getBooleanValue(accountJsonObj,
						"receiveNewsLetter"));
				account.setBirthDay(getLongValue(accountJsonObj, "birthday") * 1000);
				account.setMale(getBooleanValue(accountJsonObj, "gender"));
				account.setImageUrl(getStringValue(accountJsonObj, "imageUrl"));
				account.setRewardType(getStringValue(accountJsonObj,
						"rewardType"));
				account.setNumberFreeSlot(getIntValue(accountJsonObj,
						"numberOfFreeSlot"));
				account.setNumberFreeSlotActive(getIntValue(accountJsonObj,
						"numberOfFreeSlotActive"));
				account.setNumberSingleUseSlot(getIntValue(accountJsonObj,
						"numberOfSingleUseSlot"));
				account.setNumberSingleSlotActive(getIntValue(accountJsonObj,
						"numberOfSingleUseSlotActive"));

				JSONObject locJsonObj = accountJsonObj
						.getJSONObject("location");

				locObj.setId(getStringValue(locJsonObj, "id"));
				locObj.setCountry(getStringValue(locJsonObj, "country"));
				locObj.setState(getStringValue(locJsonObj, "state"));
				locObj.setCountryCode(getStringValue(locJsonObj, "countryCode"));
				locObj.setDistrict(getStringValue(locJsonObj, "suburb"));
				locObj.setLatitude(getDoubleValue(locJsonObj, "latitude"));
				locObj.setLongitude(getDoubleValue(locJsonObj, "longitude"));
				locObj.setShortDescription(getStringValue(locJsonObj,
						"shortDescription"));

				account.setLocation(locObj);
				account.setArrItems(arrItems);

				list.add(account);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return list;
	}

	public static String parseCountryCode(String json) {
		JSONObject jsonObj = null;
		String countryCode = "xx";
		try {
			jsonObj = new JSONObject(json);
			JSONArray arrResult = jsonObj.getJSONArray("results");
			JSONObject firstObj = arrResult.getJSONObject(0);
			JSONArray arrAddress = firstObj.getJSONArray("address_components");
			for (int i = 0; i < arrAddress.length(); i++) {
				JSONObject obj = arrAddress.getJSONObject(i);
				if (obj.getString("types").contains("country")) {
					countryCode = obj.getString("short_name");
					break;
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return countryCode;
	}

	public static List<MessageItem> parseListMessage(String json, String myId) {
		List<MessageItem> list = new ArrayList<MessageItem>();

		try {
			JSONArray array = new JSONArray(json);
			JSONArray arrContent = array.getJSONArray(0);
			for (int i = 0; i < arrContent.length(); i++) {
				JSONObject obj = arrContent.getJSONObject(i);
				MessageItem message = new MessageItem();

				message.setMessage(getStringValue(obj, "content"));
				message.setListingOwnerName(getStringValue(obj,
						"listingOwnName"));
				message.setListingOwnerId(getStringValue(obj, "listingown"));
				message.setItemName(getStringValue(obj, "listingName"));
				message.setItemImage(getStringValue(obj, "listingURL"));
				message.setUserId(getStringValue(obj, "userid"));

				if (isStringObject("date", obj)) {
					String date = getStringValue(obj, "date");
					long dateLong = (long) Double.parseDouble(date);
					// Log.d("DATE_LONG", dateLong + "");
					message.setDate(dateLong);
				} else if (isIntegerObject("date", obj)) {
					long dateLong = getIntValue(obj, "date");
					message.setDate(dateLong);
				} else {
					continue;
				}

				message.setItemId(getStringValue(obj, "listingID"));
				message.setUserName(getStringValue(obj, "displayname"));
				message.setUserImage(getStringValue(obj, "avatarURL"));

				if (isBooleanObject("isExpired", obj)) {
					boolean isExpired = getBooleanValue(obj, "isExpired");
					if (isExpired) {
						message.setIsExpired(1);
					} else {
						message.setIsExpired(0);
					}
				} else if (isStringObject("isExpired", obj)) {
					String isExpired = getStringValue(obj, "isExpired");
					if (isExpired.equals("1")) {
						message.setIsExpired(1);
					} else if (isExpired.equals("0")) {
						message.setIsExpired(0);
					}
				} else {
					continue;
				}

				String threadId = "";
				if (getStringValue(obj, "userid").equals(
						getStringValue(obj, "listingown"))) {
					threadId = (getStringValue(obj, "userid") + myId + getStringValue(
							obj, "listingID"));
				} else {
					threadId = (myId + getStringValue(obj, "userid") + getStringValue(
							obj, "listingID"));
				}

				message.setThreadId(threadId);
				message.setRead(0);
				message.setAwayId(getStringValue(obj, "userid"));
				message.setAwayName(getStringValue(obj, "displayname"));
				message.setAwayImage(getStringValue(obj, "avatarURL"));

				list.add(message);

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static MessageItem parseReceivedMessage(String json, String myId) {
		MessageItem message = new MessageItem();

		try {
			JSONObject obj = new JSONObject(json);

			message.setMessage(getStringValue(obj, "content"));
			message.setListingOwnerName(getStringValue(obj, "listingOwnName"));
			message.setListingOwnerId(getStringValue(obj, "listingown"));
			message.setItemName(getStringValue(obj, "listingName"));
			message.setItemImage(getStringValue(obj, "listingURL"));
			message.setUserId(getStringValue(obj, "userid"));

			String date = getStringValue(obj, "date");
			long dateLong = (long) Double.parseDouble(date);
			// Log.d("DATE_LONG", dateLong + "");
			message.setDate(dateLong);

			message.setItemId(getStringValue(obj, "listingID"));
			message.setUserName(getStringValue(obj, "displayname"));
			message.setUserImage(getStringValue(obj, "avatarURL"));

			boolean isExpired = getBooleanValue(obj, "isExpired");
			if (isExpired) {
				message.setIsExpired(1);
			} else {
				message.setIsExpired(0);
			}

			String threadId = "";
			if (getStringValue(obj, "userid").equals(
					getStringValue(obj, "listingown"))) {
				threadId = (getStringValue(obj, "userid") + myId + getStringValue(
						obj, "listingID"));
			} else {
				threadId = (myId + getStringValue(obj, "userid") + getStringValue(
						obj, "listingID"));
			}

			message.setThreadId(threadId);
			message.setRead(0);
			message.setAwayId(getStringValue(obj, "userid"));
			message.setAwayName(getStringValue(obj, "displayname"));
			message.setAwayImage(getStringValue(obj, "avatarURL"));

		} catch (Exception e) {

		}
		return message;
	}

	public static List<String> parseBlockedUsers(String json) {
		List<String> list = new ArrayList<String>();

		try {
			JSONObject obj = new JSONObject(json);

			JSONArray arrBlocked = obj.getJSONArray("blockedUsers");
			for (int i = 0; i < arrBlocked.length(); i++) {
				list.add(arrBlocked.getJSONObject(i).getString("blockingId"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static List<String> parseBlocker(String json) {
		List<String> list = new ArrayList<String>();

		try {
			JSONObject obj = new JSONObject(json);

			JSONArray arrBlocked = obj.getJSONArray("blockers");
			for (int i = 0; i < arrBlocked.length(); i++) {
				list.add(arrBlocked.getJSONObject(i).getString("blockerId"));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<Account> parseBlockedUsersFully(String json) {
		ArrayList<Account> arrBlocked = new ArrayList<Account>();

		try {
			JSONObject obj = new JSONObject(json);

			JSONArray arrJson = obj.getJSONArray("blockedUsers");
			for (int i = 0; i < arrJson.length(); i++) {
				JSONObject objBlocked = arrJson.getJSONObject(i);
				JSONObject objUser = objBlocked.getJSONObject("user");
				// JSONObject objLoc = objUser.getJSONObject("location");

				Account acc = new Account();
				LocationObj loc = new LocationObj();

				acc.setId(objUser.getString("id"));
				acc.setUsername(objUser.getString("name"));
				acc.setEmail(objUser.getString("email"));
				acc.setImageUrl(objUser.getString("imageUrl"));

				acc.setLocation(loc);

				arrBlocked.add(acc);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return arrBlocked;
	}

	public static ArrayList<Category> parseCategories(String json) {
		ArrayList<Category> arrCategory = new ArrayList<Category>();
		JSONArray jsonArray = null;
		try {
			jsonArray = new JSONArray(json);
			JSONObject jsonObj = null;
			Category cate = null;
			for (int i = 0; i < jsonArray.length(); i++) {
				jsonObj = jsonArray.getJSONObject(i);
				cate = new Category(jsonObj.getInt("id") + "",
						jsonObj.getString("name"),
						jsonObj.getBoolean("isHidden"),
						jsonObj.getInt("parentCategoryId") + "");

				arrCategory.add(cate);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arrCategory;
	}

	public static int parseErrorStatusCode(String json) {
		int code = 0;
		try {
			JSONObject obj = new JSONObject(json);
			if (!obj.isNull("errorCode")) {
				code = obj.getInt("errorCode");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return code;
	}

	public static void parseJsonLoginSuccess(Context context, String json) {
		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);
		try {
			JSONObject jsonObj = new JSONObject(json);
			String accessToken = "Bearer " + jsonObj.getString("access_token");
			pref.putStringValue(
					TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN,
					accessToken);

			if (!jsonObj.isNull("refresh_token")) {
				String refreshToken = jsonObj.getString("refresh_token");
				pref.putStringValue(
						TreasureTrashSharedPreferences.PREF_REFRESH_TOKEN,
						refreshToken);
			}
			
			int expires_in = jsonObj.getInt("expires_in");

			long currentTime = Calendar.getInstance().getTimeInMillis()/1000;
			pref.putLongValue(
					TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME,
					currentTime + expires_in);
			Log.d("TOKEN_EXPIRED",
					currentTime
							+ "--"
							+ pref.getLongValue(TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME)
							+ "");

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static ArrayList<LocationObj> parseLocations(String json) {
		ArrayList<LocationObj> arrLoc = new ArrayList<LocationObj>();
		try {
			JSONObject jsonObj = new JSONObject(json);
			if (jsonObj != null) {
				JSONArray arrJson = jsonObj.getJSONArray("results");
				LocationObj locObj = null;
				ArrayList<JSONObject> arrCorrectAddress = null;
				JSONArray arrAddress = null;
				JSONObject js = null;
				for (int i = 0; i < arrJson.length(); i++) {
					JSONObject obj = arrJson.getJSONObject(i);
					locObj = new LocationObj();

					// Get latitude and longitude
					JSONObject objGeometry = obj.getJSONObject("geometry");
					JSONObject objLatLong = objGeometry
							.getJSONObject("location");
					locObj.setLatitude(objLatLong.getDouble("lat"));
					locObj.setLongitude(objLatLong.getDouble("lng"));
					locObj.setAddress(obj.getString("formatted_address"));
					// Get city, country
					arrAddress = obj.getJSONArray("address_components");
					arrCorrectAddress = new ArrayList<JSONObject>();
					for (int j = 0; j < arrAddress.length(); j++) {
						js = arrAddress.getJSONObject(j);
						if (js.getString("types").contains("political")) {
							arrCorrectAddress.add(js);
						}
					}

					int levelAddress = arrCorrectAddress.size();

					if (levelAddress >= 3) {

						// get country
						JSONObject objCountry = arrCorrectAddress
								.get(levelAddress - 1);
						locObj.setCountryCode(objCountry
								.getString("short_name"));
						locObj.setCountry(objCountry.getString("long_name"));
						// get state
						JSONObject objState = arrCorrectAddress
								.get(levelAddress - 2);
						locObj.setState(objState.getString("short_name"));
						// get state
						JSONObject objDistrict = arrCorrectAddress
								.get(levelAddress - 3);
						locObj.setDistrict(objDistrict.getString("long_name"));
					} else if (levelAddress == 2) {
						// get country
						JSONObject objCountry = arrCorrectAddress
								.get(levelAddress - 1);
						locObj.setCountryCode(objCountry
								.getString("short_name"));
						locObj.setCountry(objCountry.getString("long_name"));
						// get state
						JSONObject objState = arrCorrectAddress
								.get(levelAddress - 2);
						locObj.setState(objState.getString("long_name"));
					} else {
						// get country
						JSONObject objCountry = arrCorrectAddress
								.get(levelAddress - 1);
						locObj.setCountryCode(objCountry
								.getString("short_name"));
						locObj.setCountry(objCountry.getString("long_name"));
					}

					arrLoc.add(locObj);
				}
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return arrLoc;
	}

	public static List<Feed> parseActivityFeed(String json, int itemSize) {
		List<Feed> list = new ArrayList<Feed>();
		Calendar c = Calendar.getInstance();

		try {
			JSONArray arrFeed = new JSONArray(json);
			for (int i = 0; i < arrFeed.length(); i++) {
				Feed feed = new Feed();
				JSONObject obj = arrFeed.getJSONObject(i);

				feed.setId(getStringValue(obj, "id"));
				feed.setUserId(getStringValue(obj, "userId"));
				feed.setUserName(getStringValue(obj, "userName"));

				String userImage = getStringValue(obj, "userImg");
				userImage = ImageUtil.addImageSize(userImage, itemSize);
				feed.setUserImg(userImage);

				feed.setItemId(getStringValue(obj, "itemId"));
				feed.setItemTitle(getStringValue(obj, "itemTitle"));

				String itemImage = getStringValue(obj, "itemImg");
				itemImage = ImageUtil.addImageSize(itemImage, itemSize);
				feed.setItemImg(itemImage);

				feed.setType(getStringValue(obj, "activityType"));
				feed.setPriority(getIntValue(obj, "priority"));
				feed.setCreatedDate(getDoubleValue(obj, "createdDate")
						.intValue());

				feed.setFollowed(getBooleanValue(obj, "isFollowed"));

				feed.setLoadTime(System.currentTimeMillis() / 1000);

				list.add(feed);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public static ArrayList<SocialAccount> parseArrSocialAccounts(String json) {
		ArrayList<SocialAccount> arr = new ArrayList<SocialAccount>();

		try {
			JSONObject jsonObject = new JSONObject(json);
			JSONArray arrSocials = jsonObject.getJSONArray("socialLoginInfos");
			JSONObject jsonObj = null;
			SocialAccount account = null;
			for (int i = 0; i < arrSocials.length(); i++) {
				jsonObj = arrSocials.getJSONObject(i);
				account = new SocialAccount();
				account.setProvider(jsonObj.getString("provider"));
				account.setProviderKey(jsonObj.getString("providerKey"));
				account.setName(jsonObj.getString("name"));
				account.setPictureUrl(jsonObj.getString("pictureUrl"));
				account.setMale(jsonObj.getBoolean("gender"));
				if (!jsonObj.isNull("email")) {
					account.setEmail(jsonObj.getString("email"));
				} else {
					account.setEmail("");
				}
				arr.add(account);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return arr;
	}

	// ==========================================================================================================

	private static boolean getBooleanValue(JSONObject obj, String name) {
		try {
			return obj.getBoolean(name);
		} catch (Exception e) {
			return false;
		}
	}

	private static String getStringValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? "" : obj.getString(key);
		} catch (JSONException e) {
			return "";
		}
	}

	private static long getLongValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? 0L : obj.getLong(key);
		} catch (JSONException e) {
			return 0L;
		}
	}

	private static int getIntValue(JSONObject obj, String key) {
		try {
			return obj.isNull(key) ? 0 : obj.getInt(key);
		} catch (JSONException e) {
			return 0;
		}
	}

	private static Double getDoubleValue(JSONObject obj, String key) {
		double d = 0.0;
		try {
			return obj.isNull(key) ? d : obj.getDouble(key);
		} catch (JSONException e) {
			return d;
		}
	}

	public static boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isJsonObject(JSONObject parent, String key) {
		try {
			JSONObject jObj = parent.getJSONObject(key);
		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	public static boolean isDouble(String string) {
		try {
			Double.parseDouble(string);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	public static boolean isBooleanObject(String key, JSONObject obj) {
		try {
			obj.getBoolean(key);

		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	public static boolean isStringObject(String key, JSONObject obj) {
		try {
			obj.getString(key);

		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	public static boolean isIntegerObject(String key, JSONObject obj) {
		try {
			obj.getInt(key);

		} catch (JSONException e) {
			return false;
		}
		return true;
	}

	public static boolean hasKey(String key, JSONObject obj) {
		try {
			obj.getString(key);
		} catch (Exception e) {
			return false;
		}
		return true;
	}

}
