package com.pt.treasuretrash.network;

import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.config.WebserviceConfig;

import android.content.Context;
import android.util.Log;

/**
 * AsyncHttpGet makes http post request based on AsyncTask
 * 
 */
public class AsyncHttpPost extends AsyncHttpBase {

	public AsyncHttpPost(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> parameters, boolean isShowDilog) {
		super(context, listener, parameters, isShowDilog);
		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);
		this.accessToken = pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN);
	}

	public AsyncHttpPost(Context context, AsyncHttpResponseListener listener,
			String json, boolean isShowDilog) {
		super(context, listener, json, isShowDilog);
		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);
		this.accessToken = pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN);
	}

	public AsyncHttpPost(Context context, AsyncHttpResponseProcess process,
			List<NameValuePair> parameters, boolean isShowDilag) {
		super(context, process, parameters, isShowDilag);
		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(
				context);
		this.accessToken = pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN);
	}

	@Override
	protected String request(String url) {
		try {
			this.url = url;
			Log.d("POST_URL", url);
			HttpParams params = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(params,
					Config.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, Config.NETWORK_TIME_OUT);
			HttpClient httpclient = createHttpClient(url, params);

			HttpPost httppost = new HttpPost(url);
			httppost.setHeader(WebserviceConfig.KEY_HEADER_AUTHORIZATION,
					accessToken);

			if (!isExternalParam)
				httppost.setEntity(new UrlEncodedFormEntity(parameters, "UTF-8"));
			else {
				StringEntity se;
				se = new StringEntity(jsonString, HTTP.UTF_8);
				httppost.setEntity(se);
				httppost.setHeader("Accept", "application/json");
				httppost.setHeader("Content-type",
						"application/json;charset=UTF-8");
				Log.e("json params httpPost", "json params : " + jsonString);
			}

			
			HttpResponse httpresponse = httpclient.execute(httppost);
			response = EntityUtils.toString(httpresponse.getEntity(),
					HTTP.UTF_8);
			statusCode = httpresponse.getStatusLine().getStatusCode();
			Log.d(this.getClass().getSimpleName() + "-ACCESS TOKEN",
					accessToken);
			Log.d(this.getClass().getSimpleName() + " response", response);
		} catch (ConnectTimeoutException e) {
			statusCode = NETWORK_STATUS_ERROR_TIME_OUT;
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;
	}
}
