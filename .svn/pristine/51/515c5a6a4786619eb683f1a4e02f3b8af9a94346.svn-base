package com.pt.treasuretrash.network;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.config.WebserviceConfig;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ParameterFactory;
import com.pt.treasuretrash.widget.ProgressDialog;

/**
 * AsyncHttpBase is base class for AsyncHttpGet and AsyncHttpPost class
 * 
 */
public class RefreshTokenTask extends AsyncTask<String, Integer, String> {
	public static final int NETWORK_STATUS_OK = 0;
	public static final int NETWORK_STATUS_OFF = 1;
	public static final int NETWORK_STATUS_ERROR = 2;
	public static final int AUTHORIZATION = 401;
	public static final int RESPONSE_ERROR = 400;
	public static final int RESPONSE_SUCCESS = 200;

	protected Context context;
	protected AsyncHttpResponseListener listener;
	protected List<NameValuePair> parameters;
	protected String response;
	protected boolean isShowWaitingDialog;
	protected int statusCode;
	protected ProgressDialog dialog;
	protected boolean isExternalParam = false;
	protected String jsonString = "";
	protected String url = "";
	protected String accessToken = "";
	protected TreasureTrashSharedPreferences pref;


	public RefreshTokenTask(Context context,
			AsyncHttpResponseListener listener, List<NameValuePair> parameters,
			boolean isShowWaitingDialog) {
		this.context = context;
		this.listener = listener;
		this.parameters = parameters;
		this.isShowWaitingDialog = isShowWaitingDialog;
//		this.dialog = new ProgressDialog(context);
		this.pref = new TreasureTrashSharedPreferences(context);
	}


	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (isShowWaitingDialog) {
			dialog.setCancelable(false);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			listener.before();
		}
	}

	@Override
	protected String doInBackground(String... args) {
		if (isNetworkAvailable(context)) {
			// Request to server if network is available
			return request(args[0]);
		} else {
			// Return status if network is not available
			statusCode = NETWORK_STATUS_OFF;
			return null;
		}
	}

	private boolean isNetworkAvailable(Context context) {
		ConnectivityManager conMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null) {
			return false;
		}
		if (!i.isConnected()) {
			return false;
		}
		if (!i.isAvailable()) {
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(String result) {
		// Call method to process http status code and response
//			dialog.dismiss();
			listener.after(statusCode, response);

	}

	/**
	 * Send request to server
	 * 
	 * @param url
	 * @return
	 */
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
		} catch (Exception e) {
			statusCode = NETWORK_STATUS_ERROR;
			e.printStackTrace();
		}
		return null;

	}

	// ============================================================================

	/**
	 * Create HttpClient based on HTTP or HTTPS protocol that is parsed from url
	 * parameter. With HTTPS protocol, we accept all SSL certificates.
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	@SuppressLint("DefaultLocale")
	protected HttpClient createHttpClient(String url, HttpParams params) {
		if ((url.toLowerCase().startsWith("https"))) {
			// HTTPS process
			try {

				KeyStore trustStore = KeyStore.getInstance(KeyStore
						.getDefaultType());
				trustStore.load(null, null);

				SSLSocketFactory sf = new MySSLSocketFactory(trustStore);
				sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
				HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
				HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

				SchemeRegistry registry = new SchemeRegistry();
				registry.register(new Scheme("http", PlainSocketFactory
						.getSocketFactory(), 80));
				registry.register(new Scheme("https", sf, 443));

				ClientConnectionManager ccm = new ThreadSafeClientConnManager(
						params, registry);

				return new DefaultHttpClient(ccm, params);
			} catch (Exception e) {
				return new DefaultHttpClient(params);
			}
		} else {
			// HTTP process
			return new DefaultHttpClient(params);
		}
	}

	// ============================ Https functions ============================

	/**
	 * Trust every server - dont check for any certificate
	 */
	private static void trustAllHosts() {
		// Create a trust manager that does not validate certificate chains
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return new java.security.cert.X509Certificate[] {};
			}

			public void checkClientTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) throws CertificateException {
			}
		} };

		// Install the all-trusting trust manager
		try {
			SSLContext sc = SSLContext.getInstance("TLS");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
					.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open HTTPS connection. Use this method to setup and accept all SSL
	 * certificates from HTTPS protocol.
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static HttpsURLConnection openSConnection(String url)
			throws IOException {
		URL theURL = new URL(url);
		trustAllHosts();
		HttpsURLConnection https = (HttpsURLConnection) theURL.openConnection();
		https.setHostnameVerifier(new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		});
		return https;
	}

	/**
	 * Open HTTP connection
	 * 
	 * @param url
	 * @return
	 * @throws IOException
	 */
	public static HttpURLConnection openConnection(String url)
			throws IOException {
		URL theURL = new URL(url);
		return (HttpURLConnection) theURL.openConnection();
	}
}
