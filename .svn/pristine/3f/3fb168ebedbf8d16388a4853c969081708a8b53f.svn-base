/*
 * Name: $RCSfile: ParameterFactory.java,v $
 * Version: $Revision: 1.1 $
 * Date: $Date: Oct 31, 2011 2:45:36 PM $
 *
 * Copyright (C) 2011 COMPANY_NAME, Inc. All rights reserved.
 */

package com.pt.treasuretrash.modelmanager;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.android.gms.internal.hr;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.Category;
import com.pt.treasuretrash.object.ImageObj;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.object.SearchObj;

/**
 * ParameterFactory class builds parameters for web service apis
 * 
 */
@SuppressLint("NewApi")
public final class ParameterFactory {

	private static String TAG = "ParameterFactory";

	// Login ,register

	public static List<NameValuePair> createLoginNormalParams(String grantType,
			String username, String password, String clientId,
			String clientSecret) {

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("grant_type", grantType));
		parameters.add(new BasicNameValuePair("username", username));
		parameters.add(new BasicNameValuePair("password", password));
		parameters.add(new BasicNameValuePair("client_id", clientId));
		parameters.add(new BasicNameValuePair("client_secret", clientSecret));

		return parameters;
	}

	public static String createRegisterNormalParams(Account acc) {

		JSONObject json = new JSONObject();
		try {
			json.put("Username", acc.getUsername());
			json.put("Email", acc.getEmail());
			json.put("Name", acc.getName());
			json.put("Password", acc.getPassword());
			json.put("ConfirmPassword", acc.getPassword());
			json.put("Gender", acc.isMale());
			json.put("Birthday", acc.getBirthDay() / 1000);
			json.put("Address", "");

			// add location
			JSONObject jsonLocation = new JSONObject();
			jsonLocation.put("Country", acc.getLocation().getCountry());
			jsonLocation.put("CountryCode", acc.getLocation().getCountryCode());
			jsonLocation.put("State", acc.getLocation().getState());
			jsonLocation.put("Suburb", acc.getLocation().getDistrict());
			jsonLocation.put("Longitude", acc.getLocation().getLongitude());
			jsonLocation.put("Latitude", acc.getLocation().getLatitude());
			jsonLocation.put("ShortDescription", acc.getLocation()
					.getShortDescription());

			json.put("Location", jsonLocation);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();
	}

	public static List<NameValuePair> createRefreshTokenParams(
			String grantType, String refreshTokenKey, String clientId,
			String clientSecret) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("grant_type", grantType));
		parameters
				.add(new BasicNameValuePair("Refresh_token", refreshTokenKey));
		parameters.add(new BasicNameValuePair("client_id", clientId));
		parameters.add(new BasicNameValuePair("client_secret", clientSecret));

		return parameters;
	}

	public static List<NameValuePair> createLoginSocialParams(String provider,
			String clientId, String redirectURI) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("provider", provider));
		parameters.add(new BasicNameValuePair("client_id", clientId));
		parameters.add(new BasicNameValuePair("redirect_uri", redirectURI));
		return parameters;
	}

	public static String createRegisterSocialParams(Account acc) {

		JSONObject json = new JSONObject();
		try {
			json.put("Username", acc.getUsername());
			json.put("Provider", acc.getExternalType());
			json.put("ExternalAccessToken", acc.getExternalAccessToken());
			json.put("Email", acc.getEmail());
			json.put("Name", acc.getName());
			json.put("Gender", acc.isMale());
			json.put("Birthday", acc.getBirthDay() / 1000);
			if (!acc.getImageUrl().isEmpty())
				json.put("Picture ", acc.getImageUrl());

			// add location
			JSONObject jsonLocation = new JSONObject();
			jsonLocation.put("Country", acc.getLocation().getCountry());
			jsonLocation.put("CountryCode", acc.getLocation().getCountryCode());
			jsonLocation.put("State", acc.getLocation().getState());
			jsonLocation.put("Suburb", acc.getLocation().getDistrict());
			jsonLocation.put("Longitude", acc.getLocation().getLongitude());
			jsonLocation.put("Latitude", acc.getLocation().getLatitude());
			jsonLocation.put("ShortDescription", acc.getLocation()
					.getShortDescription());

			json.put("Location", jsonLocation);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();

	}

	// Provider
	// externalAccessToken

	public static List<NameValuePair> createGetAccountSocialLoginParams(
			String provider, String socialAccessToken) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("Provider", provider));
		parameters.add(new BasicNameValuePair("externalAccessToken",
				socialAccessToken));

		return parameters;
	}

	public static String createUpdateProfileParams(Account acc,
			boolean isReceiveNewsLetter) {

		JSONObject json = new JSONObject();
		try {
			json.put("Id", acc.getId());
			json.put("UserName", acc.getUsername());
			json.put("Email", acc.getEmail());
			json.put("Name", acc.getName());
			json.put("ReceiveNewsLetter", isReceiveNewsLetter);
			if (!acc.getPassword().isEmpty())
				json.put("Password", acc.getPassword());
			json.put("Gender", acc.isMale());
			json.put("Birthday", acc.getBirthDay() / 1000);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();

	}

	public static List<NameValuePair> createUserIdParams(String userID) {

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("userId", userID));
		return parameters;
	}

	public static String createUpdateLocationParams(LocationObj location) {
		// create json :
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

		return json.toString();
	}

	// =============================================================
	// param product
	public static List<NameValuePair> createGetProductsByNameParams(String name) {

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("name", name));

		return parameters;
	}

	public static List<NameValuePair> createPostAppProductParams(
			String productname, String receipt, String transactionId) {
		// create json :
		// {
		// "ProductName": "",
		// "Receipt": "",
		// "TransactionId": ""
		// }
		JSONObject json = new JSONObject();

		try {
			json.put("ProductName", productname);
			json.put("Receipt", receipt);
			json.put("TransactionId", transactionId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("receiptModel", json.toString()));

		return parameters;
	}

	// category
	public static List<NameValuePair> createUpdateCategoriesStatusParams(
			ArrayList<Category> arrCategories) {
		JSONObject json = new JSONObject();
		JSONArray arrJsons = new JSONArray();
		try {
			JSONObject item;
			for (Category category : arrCategories) {
				item = new JSONObject();
				item.put("CategoryId", category.getId());
				item.put("Enable", category.isHidden());
				arrJsons.put(item);
			}
			json.putOpt("CategoryStateModels", arrJsons);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("model", json.toString()));

		return parameters;
	}

	// favourite

	public static List<NameValuePair> createGetFavouriteByKeywordAndCategoryParams(
			String keyword, String categoryId) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("keyword", keyword));
		parameters.add(new BasicNameValuePair("categoryId", categoryId));
		return parameters;
	}

	// item
	public static List<NameValuePair> createItemDetailsParams(String itemId,
			double currentlatitude, double currentlongitude, int width,
			int height) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("id", itemId));
		parameters
				.add(new BasicNameValuePair("latitude", currentlatitude + ""));
		parameters.add(new BasicNameValuePair("longitude", currentlongitude
				+ ""));
		parameters.add(new BasicNameValuePair("width", width + ""));
		parameters.add(new BasicNameValuePair("height", height + ""));
		return parameters;
	}

	public static List<NameValuePair> createDeleteItemParams(String itemId) {
		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("id", itemId));
		return parameters;
	}

	public static String createNewItemParams(Item item, boolean isEdit) {

		JSONObject json = new JSONObject();

		try {
			if (isEdit) {
				json.put("Id", item.getId());
			}
			json.put("Title", item.getTitle());
			json.put("Cost", item.getCost() + "");
			json.put("Currency", item.getLocation().getCountryCode());
			json.put("SlotImage", item.getSlotImage() + "");
			json.put("SlotType", item.getSlotType() + "");
			json.put("Description", item.getDescription() + "");
			json.put("CategoryId", item.getCategory().getId());
			// put json location :
			JSONObject jsonLocation = new JSONObject();
			jsonLocation.put("Country", item.getLocation().getCountry());
			jsonLocation
					.put("CountryCode", item.getLocation().getCountryCode());
			jsonLocation.put("State", item.getLocation().getState());
			jsonLocation.put("Suburb", item.getLocation().getDistrict());
			jsonLocation.put("Longitude", item.getLocation().getLongitude());
			jsonLocation.put("Latitude", item.getLocation().getLatitude());
			jsonLocation.put("ShortDescription", item.getLocation()
					.getShortDescription());
			json.put("Location", jsonLocation);

			// put json item type :
			JSONObject jsonItemType = new JSONObject();
			jsonItemType.put("Type", item.getType());
			json.put("ItemType", jsonItemType);

			// put list image
			JSONArray arrJsonImages = new JSONArray();
			JSONObject jsonImage;
			for (ImageObj image : item.getArrImages()) {
				jsonImage = new JSONObject();
				// jsonImage.put("Id", image.getId());
				jsonImage.put("ImageUrl", image.getImageUrl());
				jsonImage.put("Id", image.getId());
				arrJsonImages.put(jsonImage);
			}
			json.put("ItemImages", arrJsonImages);

			// put transaction ID

			if (!item.getArrTransactionId().isEmpty()) {
				JSONArray arrJsonTransaction = new JSONArray();
				JSONObject jsonTransaction;
				for (String transId : item.getArrTransactionId()) {
					jsonTransaction = new JSONObject();
					jsonTransaction.put("TransactionId", transId);
					arrJsonTransaction.put(jsonTransaction);
				}
				json.put("Transactions", arrJsonTransaction);
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();
	}

	public static String createMarksGoneItemParams(String destination) {
		// create json :
		// {
		// "ProductName": "",
		// "Receipt": "",
		// "TransactionId": ""
		// }
		JSONObject json = new JSONObject();

		try {
			json.put("GoneWhere", destination);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return json.toString();
	}

	public static List<NameValuePair> createSearchParams(SearchObj search) {

		// {
		// "Keyword": "",
		// "CategoryId": 0,
		// "Longitude": 0,
		// "Latitude": 0,
		// "PageSize": 0,
		// "CurrentPage": 0,
		// "SearchField": "",
		// "Width": 0,
		// "Height": 0
		// }
		JSONObject json = new JSONObject();

		try {
			json.put("Keyword", search.getKeyWord());
			json.put("CategoryId", search.getCategoryId());
			json.put("ItemType", search.getItemType());
			json.put("Latitude", search.getLatitude());
			json.put("Longitude", search.getLongitide());
			json.put("PageSize", search.getPageSize());
			json.put("CurrentPage", search.getCurrentPage());
			json.put("SearchField", search.getSearchField());
			json.put("Width", search.getImageWidth());
			json.put("Height", search.getImageHeight());
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		List<NameValuePair> parameters = new ArrayList<NameValuePair>();
		parameters.add(new BasicNameValuePair("searchItemParameter", json
				.toString()));

		Log.d(TAG, json.toString());

		return parameters;
	}

}