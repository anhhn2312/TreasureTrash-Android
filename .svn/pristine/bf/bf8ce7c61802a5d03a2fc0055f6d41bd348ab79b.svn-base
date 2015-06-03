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

import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.view.Window;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.widget.ProgressHUD;

/**
 * AsyncHttpBase is base class for AsyncHttpGet and AsyncHttpPost class
 * 
 */
public class AsyncHttpBase extends AsyncTask<String, Integer, String> {
	public static final int NETWORK_STATUS_OK = 0;
	public static final int NETWORK_STATUS_OFF = 1;
	public static final int NETWORK_STATUS_ERROR = 2;
	public static final int NETWORK_STATUS_ERROR_TIME_OUT = 3;
	public static final int AUTHORIZATION = 401;
	public static final int RESPONSE_ERROR = 400;
	public static final int RESPONSE_SUCCESS = 200;

	protected Context context;
	protected AsyncHttpResponseListener listener;
	protected List<NameValuePair> parameters;
	protected String response = null;
	protected boolean isShowWaitingDialog;
	protected int statusCode;
	protected ProgressDialog dialog;
	// Progressdi mProgressHUD;
	protected boolean isExternalParam = false;
	protected String jsonString = "";
	protected String url = "";
	protected String accessToken = "";
	protected TreasureTrashSharedPreferences pref;
	protected int constructorChoose;

	public AsyncHttpBase(Context context, AsyncHttpResponseListener listener) {
		this.context = context;
		this.listener = listener;
		this.parameters = new ArrayList<NameValuePair>();
		this.isShowWaitingDialog = false;
		this.dialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
		this.pref = new TreasureTrashSharedPreferences(context);
		this.constructorChoose = 1;
	}

	public AsyncHttpBase(Context context, AsyncHttpResponseListener listener,
			boolean isShowWaitingDialog) {
		this.context = context;
		this.listener = listener;
		this.parameters = new ArrayList<NameValuePair>();
		this.isShowWaitingDialog = isShowWaitingDialog;
		this.dialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
		this.pref = new TreasureTrashSharedPreferences(context);
		this.constructorChoose = 2;
	}

	public AsyncHttpBase(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> parameters) {
		this.context = context;
		this.listener = listener;
		this.parameters = parameters;
		this.isShowWaitingDialog = false;
		this.dialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
		this.pref = new TreasureTrashSharedPreferences(context);
		this.constructorChoose = 3;
	}

	public AsyncHttpBase(Context context, AsyncHttpResponseListener listener,
			List<NameValuePair> parameters, boolean isShowWaitingDialog) {
		this.context = context;
		this.listener = listener;
		this.parameters = parameters;
		this.isShowWaitingDialog = isShowWaitingDialog;
		this.dialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
		this.pref = new TreasureTrashSharedPreferences(context);
		this.constructorChoose = 4;
	}

	public AsyncHttpBase(Context context, AsyncHttpResponseListener listener,
			String json, boolean isShowWaitingDialog) {
		this.context = context;
		this.listener = listener;
		this.isExternalParam = true;
		this.jsonString = json;
		this.isShowWaitingDialog = isShowWaitingDialog;
		this.dialog = new ProgressDialog(context, R.style.ProgressDialogTheme);
		this.pref = new TreasureTrashSharedPreferences(context);
		this.constructorChoose = 5;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (isShowWaitingDialog) {
			if (dialog != null) {
				dialog.setCancelable(false);
				dialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
				dialog.show();
			}
		}
		listener.before();

	}

	@Override
	protected String doInBackground(String... args) {
		if (isNetworkAvailable(context)) {
			// Request to server if network is available
			this.url = args[0];
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
		if (!i.isConnectedOrConnecting()) {
			return false;
		}
		if (!i.isAvailable()) {
			return false;
		}
		return true;
	}

	@Override
	protected void onPostExecute(String result) {

		try {
			if (dialog != null && dialog.isShowing())
				dialog.dismiss();
		} catch (final IllegalArgumentException e) {
			// Handle or log or ignore
		} catch (final Exception e) {
			// Handle or log or ignore
		} finally {
			this.dialog = null;
		}
		Log.e("RESPONSE", response == null ? "NULL" : response);
		listener.after(statusCode, response);
		// }

	}

	/**
	 * Send request to server
	 * 
	 * @param url
	 * @return
	 */
	protected String request(String url) {
		// This function will be implemented in AsyncHttpGet and AsyncHttpPost
		// class
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
