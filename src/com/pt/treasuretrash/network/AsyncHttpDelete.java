package com.pt.treasuretrash.network;

import java.net.URLEncoder;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.content.Context;

import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.config.WebserviceConfig;
import com.pt.treasuretrash.utility.Logger;

/**
 * AsyncHttpGet makes http get request based on AsyncTask
 * 
 */
public class AsyncHttpDelete extends AsyncHttpBase {

	public AsyncHttpDelete(Context context,
			AsyncHttpResponseListener listener, List<NameValuePair> parameters,
			boolean isShowDilog) {
		super(context, listener, parameters, isShowDilog);
		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(context);
		this.accessToken = pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN);
	}

	public AsyncHttpDelete(Context context, 
			AsyncHttpResponseListener listener, String json, boolean isShowDilog) {
		super(context, listener, json, isShowDilog);
		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(context);
		this.accessToken = pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN);
	}

	public AsyncHttpDelete(Context context,
			AsyncHttpResponseProcess process, List<NameValuePair> parameters,
			boolean isShowDilag) {
		super(context, process, parameters, isShowDilag);
		TreasureTrashSharedPreferences pref = new TreasureTrashSharedPreferences(context);
		this.accessToken = pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN);
	}

	@Override
	protected String request(String url) {
		try {
			this.url = url;
			HttpParams params = new BasicHttpParams();
			String combinedParams = "";
			if (!parameters.isEmpty()) {
				combinedParams += "?";
				for (NameValuePair p : parameters) {
					String paramString = p.getName() + "="
							+ URLEncoder.encode(p.getValue(), "UTF-8");
					if (combinedParams.length() > 1) {
						combinedParams += "&" + paramString;
					} else {
						combinedParams += paramString;
					}
				}
			}
			HttpConnectionParams.setConnectionTimeout(params,
					Config.NETWORK_TIME_OUT);
			HttpConnectionParams.setSoTimeout(params, Config.NETWORK_TIME_OUT);
			HttpClient httpclient = createHttpClient(url, params);
			String link = url + combinedParams;
			HttpDelete httpget1 = new HttpDelete(link);
			Logger.d("HTTP GET", "URL : " + link);
			httpget1.setHeader(WebserviceConfig.KEY_HEADER_AUTHORIZATION,
					accessToken);

			HttpResponse httpresponse = httpclient.execute(httpget1);

			response = EntityUtils.toString(httpresponse.getEntity(),
					HTTP.UTF_8);
			statusCode = httpresponse.getStatusLine().getStatusCode();
		} catch (ConnectTimeoutException e) {
			statusCode = NETWORK_STATUS_ERROR_TIME_OUT;
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;
	}
}
