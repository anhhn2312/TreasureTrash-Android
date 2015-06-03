package com.pt.treasuretrash.modelmanager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.config.WebserviceConfig;
import com.pt.treasuretrash.network.AsyncHttpBase;
import com.pt.treasuretrash.network.AsyncHttpDelete;
import com.pt.treasuretrash.network.AsyncHttpGet;
import com.pt.treasuretrash.network.AsyncHttpPost;
import com.pt.treasuretrash.network.AsyncHttpPut;
import com.pt.treasuretrash.network.AsyncHttpResponseProcess;
import com.pt.treasuretrash.object.Category;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.SearchObj;
import com.pt.treasuretrash.widget.ProgressDialog;

public class ProductModelManager extends ModelManager {

	public static void getAppProducts(Context context, boolean isProgess,
			final ModelManagerListener listener) {

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

		post.execute(WebserviceConfig.GET_APP_PRODUCTS);
	}

	public static void getAppProductsByName(Context context, String name,
			boolean isProgess, final ModelManagerListener listener) {

		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createGetProductsByNameParams(name);
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
		post.execute(WebserviceConfig.GET_APP_PRODUCTS);
	}

	public static void postAppProduct(Context context, String productName,
			String receipt, String transactionID, boolean isProgess,
			final ModelManagerListener listener) {

		JSONObject json = new JSONObject();

		try {
			json.put("ProductName", productName);
			json.put("Receipt", receipt);
			json.put("TransactionId", transactionID);
			json.put("IsAndroid", true);
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
		post.execute(WebserviceConfig.GET_APP_PRODUCTS);
	}

	// ==== Category
	public static void getAllCategories(Context context, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.GET_CATEGORIES;
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
		// post.execute(WebserviceConfig.GET_CATEGORIES);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}

	}

	public static void updateCategoryStatus(Context context,
			ArrayList<Category> arrCategories, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.GET_CATEGORIES;
		JSONObject json = new JSONObject();
		JSONArray arrJsons = new JSONArray();
		try {
			JSONObject item;
			for (Category category : arrCategories) {
				item = new JSONObject();
				item.put("CategoryId", category.getId());
				item.put("Enable", !category.isHidden());
				arrJsons.put(item);
			}
			json.putOpt("CategoryStateModels", arrJsons);
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
		// post.execute(WebserviceConfig.GET_CATEGORIES);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	// item

	public static void getListFavouritesByKeyWordAndCateory(Context context,
			String keyword, String categoryId, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.FAVOURITES;

		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createGetFavouriteByKeywordAndCategoryParams(keyword,
						categoryId);

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
		// post.execute(WebserviceConfig.FAVOURITES);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void getListFavourites(Context context, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.FAVOURITES;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
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
		// post.execute(WebserviceConfig.FAVOURITES);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void addFavourite(Context context, String itemId,
			boolean isProgess, final ModelManagerListener listener) {

		JSONObject json = new JSONObject();
		String url = WebserviceConfig.FAVOURITES;
		try {
			json.put("ItemId", itemId);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final AsyncHttpPost post = new AsyncHttpPost(context,
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

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}

	}

	public static void addFlagItem(Context context, String itemId,
			boolean isProgess, final ModelManagerListener listener) {

		JSONObject json = new JSONObject();
		String url = WebserviceConfig.FLAGGED_ITEMS;
		try {
			json.put("ItemId", itemId);
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
		// post.execute(WebserviceConfig.FLAGGED_ITEMS);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	// width, height : image size you want
	// lat, long : user location
	public static void getItemDetails(Context context, String itemId,
			double latitude, double logitude, int width, int height,
			boolean isProgess, final ModelManagerListener listener) {

		String url = WebserviceConfig.ITEMS;
		ArrayList<NameValuePair> params = (ArrayList<NameValuePair>) ParameterFactory
				.createItemDetailsParams(itemId, latitude, logitude, width,
						height);

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
		// post.execute(WebserviceConfig.ITEMS);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void getItemsByCurrentUser(Context context, float latitude,
			float longitude, int width, int height, boolean isProgess,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.ITEMS;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("width", width + ""));
		params.add(new BasicNameValuePair("height", height + ""));
		params.add(new BasicNameValuePair("longitude", longitude + ""));
		params.add(new BasicNameValuePair("latitude", latitude + ""));

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
		// post.execute(WebserviceConfig.ITEMS);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void createNewItem(Context context, Item item,
			boolean isEdit, boolean isProgess,
			final ModelManagerListener listener) {

		String jsonParam = ParameterFactory.createNewItemParams(item, isEdit);
		String url = WebserviceConfig.ITEMS;

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
				}, jsonParam, isProgess);
		// post.execute(WebserviceConfig.ITEMS);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void updateItem(Context context, Item item, boolean isEdit,
			boolean isProgess, final ModelManagerListener listener) {

		String jsonParam = ParameterFactory.createNewItemParams(item, isEdit);
		String url = WebserviceConfig.ITEMS;

		Log.i("UPDATE_ITEM_JSON", jsonParam);

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
				}, jsonParam, isProgess);
		// post.execute(WebserviceConfig.ITEMS);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void markGonesItem(Context context, String itemId,
			String where, boolean isProgess, final ModelManagerListener listener) {

		String url = WebserviceConfig.ITEMS + "/" + itemId + "/MarksGone";
		String params = ParameterFactory.createMarksGoneItemParams(where);

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
		// post.execute(WebserviceConfig.ITEMS + "/" + itemId + "/MarksGone");

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void deleteItem(Context context, String itemId,
			boolean isProgress, final ModelManagerListener listener) {
		String url = WebserviceConfig.ITEMS + "/" + itemId + "/Delete";

		AsyncHttpDelete post = new AsyncHttpDelete(context,
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
				}, ParameterFactory.createDeleteItemParams(itemId), isProgress);

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

	public static void relistItem(Context context, String itemId,
			String slotType, boolean isProgess,
			final ModelManagerListener listener) {
		String url = WebserviceConfig.ITEMS + "/" + itemId + "/Relist";
		JSONObject json = new JSONObject();
		try {
			json.put("SlotType", slotType);
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
		// post.execute(WebserviceConfig.ITEMS + "/" + itemId + "/Relist");
		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}
	}

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

	public static void searchItem(Context context, SearchObj search,
			boolean isProgess, final ModelManagerListener listener) {

		// ArrayList<NameValuePair> params = (ArrayList<NameValuePair>)
		// ParameterFactory
		// .createSearchParams(search);
		String url = WebserviceConfig.ITEMS + "/GetAllItems";
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
		// post.execute(WebserviceConfig.ITEMS + "/GetAllItems");

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}

	}

	public static void getItemByLocation(Context context, String countryCode,
			String state, String suburb, double lat, double lng,
			boolean isProgess, final ModelManagerListener listener) {

		String url = WebserviceConfig.ITEMS + "/GetItemByLocation";

		JSONObject json = new JSONObject();

		try {
			json.put("Country", countryCode);
			json.put("State", state);
			json.put("Suburb", suburb);
			json.put("Latitude", lat);
			json.put("Longitude", lng);

		} catch (Exception e) {
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
		// post.execute(WebserviceConfig.ITEMS + "/GetItemByLocation");

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}

	}

	public static void checkListItemExpired(Context context,
			List<String> listItems, boolean isProgress,
			final ModelManagerListener listener) {

		String url = WebserviceConfig.CHECK_LIST_ITEM_EXPIRED;
		JSONObject obj = new JSONObject();

		try {
			JSONArray chattedItemArr = new JSONArray();
			for (String s : listItems) {
				chattedItemArr.put(s);
			}

			obj.put("ids", chattedItemArr);

			Log.i("CHATTED_ITEMS_OBJ", obj.toString());
		} catch (Exception e) {
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
				}, obj.toString(), isProgress);
		// post.execute(WebserviceConfig.ITEMS + "/GetItemByLocation");

		if (!checkTokenExpired(context, post, url)) {
			post.execute(url);
		}

	}

	public static void postFile(Context context, File file, boolean isProgess,
			final ModelManagerListener listener) {

		final ProgressDialog dialog = new ProgressDialog(context);
		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);
		String accessToken = pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("image", file);

		if (isProgess) {

			dialog.show();
		}

		AjaxCallback<String> cb = new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				if (status.getCode() == AjaxStatus.NETWORK_ERROR) {
					listener.onError();
				} else {
					listener.onSuccess(object);
				}
				// listener.onError();
			}

		};
		// cb.timeout(10);
//		cb.setTimeout(timeout);
		cb.header(WebserviceConfig.KEY_HEADER_AUTHORIZATION, accessToken);

		AQuery aq = new AQuery(context);
		aq.ajax(WebserviceConfig.ITEMS + "/PostFile", params, String.class, cb);

	}

	public static void postFiles(Context context, ArrayList<File> files,
			boolean isProgess, final ModelManagerListener listener) {

		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);
		String accessToken = pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN);
		Map<String, Object> params = new HashMap<String, Object>();
		for (int i = 0; i < files.size(); i++) {
			params.put("image" + i, files.get(i));
		}

		AjaxCallback<String> cb = new AjaxCallback<String>() {
			@Override
			public void callback(String url, String object, AjaxStatus status) {
				// TODO Auto-generated method stub
				listener.onSuccess(object);
			}

		};
		cb.header(WebserviceConfig.KEY_HEADER_AUTHORIZATION, accessToken);

		AQuery aq = new AQuery(context);
		aq.ajax(WebserviceConfig.ITEMS + "/PostFile", params, String.class, cb);

	}

	public static void getAllTermCondition(Context context, boolean isProgess,
			final ModelManagerListener listener) {

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
		post.execute(WebserviceConfig.TERM_CONDITION);
	}

	public static void getTermConditionDetails(Context context,
			String termName, boolean isProgess,
			final ModelManagerListener listener) {

		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("name", termName));

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
		post.execute(WebserviceConfig.TERM_CONDITION);
	}

}