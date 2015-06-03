package com.pt.treasuretrash.activity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.auth.FacebookHandle;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.wallet.EnableWalletOptimizationReceiver;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseFunctionsActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.config.FacebookConstants;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.utility.NetworkUtil;
import com.pt.treasuretrash.utility.SmallUtility;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.HelveticaLightTextView;
import com.pt.treasuretrash.widget.ProgressDialog;
import com.pt.treasuretrash.widget.ProgressHUD;

public class SignupActivity extends TreasureTrashBaseMessageActivity implements
		OnClickListener {

	private FacebookHandle handler;

	private AutoBgButton btnSignUpFacebook, btnSignupGoogle, btnSignupNormal;
	private HelveticaLightTextView tvLogin, tvCancel;
	public static Account registerAccount;
	// google variable :
	private GoogleApiClient mGoogleApiClient;
	private static final int RC_SIGN_IN = 0;
	private boolean mIntentInProgress;
	private static Activity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup_layout);
		NetworkUtil.enableStrictMode();
		buildGoogleApiClient();
		self = this;
		instance = this;
		initUI();
		registerAccount = new Account();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initControl();
			}
		}, 1000);
	}

	public static Activity getInstance() {
		return instance;
	}

	private void initUI() {
		btnSignUpFacebook = (AutoBgButton) findViewById(R.id.btnSignupFacebook);
		btnSignupGoogle = (AutoBgButton) findViewById(R.id.btnSignupGoogle);
		btnSignupNormal = (AutoBgButton) findViewById(R.id.btnSignupNormal);
		tvLogin = (HelveticaLightTextView) findViewById(R.id.tvLogin);
		tvCancel = (HelveticaLightTextView) findViewById(R.id.tvCancel);
	}

	private void initControl() {

		btnSignUpFacebook.setOnClickListener(this);
		btnSignupGoogle.setOnClickListener(this);
		btnSignupNormal.setOnClickListener(this);
		tvLogin.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		instance = null;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnSignupFacebook:
			onclickSignUpFacebook(AccountModelManager.PROVIDER_FACEBOOK);
			break;
		case R.id.btnSignupGoogle:
			onclickSignUpGoogle();
			break;
		case R.id.btnSignupNormal:
			gotoActivity(self, SignupEmailActivity_1.class);
			break;
		case R.id.tvCancel:
			super.onBackPressed();
			break;
		case R.id.tvLogin:
			gotoActivity(self, LoginSelectionActivity.class,
					Intent.FLAG_ACTIVITY_CLEAR_TOP);
			break;
		default:
			break;
		}

	}

	private void onclickSignUpFacebook(final String provider) {
		// TODO Auto-generated method stub
		final AQuery aq = new AQuery(self);
		handler = new FacebookHandle(self, FacebookConstants.FACEBOOK_APP_ID,
				FacebookConstants.PERMISSIONS);
		handler.sso(FacebookConstants.REQUEST_FACEBOOK_SSO);
		final String Facebook_url = "https://graph.facebook.com/me";
		handler.reauth(new AbstractAjaxCallback<JSONObject, FacebookHandle>() {
			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);
				showProgressDialog();
				aq.auth(handler)
						.progress(progressDialog)
						.ajax(Facebook_url, JSONObject.class,
								new AjaxCallback<JSONObject>() {
									@Override
									public void callback(String url,
											JSONObject object, AjaxStatus status) {
										// TODO Auto-generated method stub
										super.callback(url, object, status);
										Log.e("LOGIN SELECTION",
												"aaaa access token :"
														+ handler.getToken());

										Log.e("LOGIN SELECTION", "aaaa json :"
												+ object.toString());
										registerAccount = ParserUtility
												.parseFacebookAccount(
														handler.getToken(),
														object.toString());
										registerAccount
												.setExternalType(provider);

										closeProgressDialog();
										Bundle bundle = new Bundle();
										bundle.putString(GlobalValue.KEY_FACEBOOK_TOKEN, handler.getToken());
										gotoActivity(self,
												SignupEmailActivity_2.class, bundle);
									}
								});
			}
		});
	}

	private void onclickSignUpGoogle() {
		if (!mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		} else {
			mGoogleApiClient.reconnect();
		}

	}

	private void buildGoogleApiClient() {

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(new ConnectionCallbacks() {

					@Override
					public void onConnectionSuspended(int arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onConnected(Bundle arg0) {
						// TODO Auto-generated method stub
						String email = Plus.AccountApi
								.getAccountName(mGoogleApiClient);
						new Authenticate(email).execute();

					}
				})
				.addOnConnectionFailedListener(
						new OnConnectionFailedListener() {

							@Override
							public void onConnectionFailed(
									ConnectionResult result) {
								// TODO Auto-generated method stub
								if (!mIntentInProgress
										&& result.hasResolution()) {
									try {
										mIntentInProgress = true;
										startIntentSenderForResult(result
												.getResolution()
												.getIntentSender(), RC_SIGN_IN,
												null, 0, 0, 0);
									} catch (SendIntentException e) {
										// The intent was canceled before it was
										// sent. Return to the default
										// state and attempt to connect to get
										// an updated ConnectionResult.
										mIntentInProgress = false;
										mGoogleApiClient.connect();
									}
								}

							}
						}).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN)
				.build();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch (requestCode) {
		case FacebookConstants.REQUEST_FACEBOOK_SSO:
			if (handler != null) {
				handler.onActivityResult(requestCode, resultCode, data);
			}
			break;
		case RC_SIGN_IN:
			if (resultCode == RESULT_OK) {
				mIntentInProgress = false;
				if (!mGoogleApiClient.isConnecting()) {
					mGoogleApiClient.connect();
				}
			} else {
				mIntentInProgress = false;
			}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	private class Authenticate extends AsyncTask<String, String, String> {
		ProgressHUD pDialog;
		String mEmail;
		String scope = "oauth2:" + Scopes.PLUS_LOGIN;

		public Authenticate(String email) {
			this.mEmail = email;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = ProgressHUD.show(context, "", true, false,
					new OnCancelListener() {

						@Override
						public void onCancel(DialogInterface dialog) {
							// TODO Auto-generated method stub

						}
					});
		}

		@Override
		protected void onPostExecute(String token) {
			pDialog.dismiss();
			if (token != null) {
				Log.e("Token", "Access Token retrieved:" + token);
				String urlStr = "https://www.googleapis.com/plus/v1/people/me?access_token="
						+ token;
				Log.e("Token", "url request:" + urlStr);
				URL url;
				try {
					url = new URL(urlStr);
					HttpURLConnection con = (HttpURLConnection) url
							.openConnection();
					int sc = con.getResponseCode();
					if (sc == 200) {
						InputStream is = con.getInputStream();
						String json = readResponse(is);
						is.close();
						registerAccount = ParserUtility.parseGoogleAccount(
								mEmail, token, json);
						gotoActivity(self, SignupEmailActivity_2.class);

						Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
						mGoogleApiClient.disconnect();

					} else if (sc == 401) {
						GoogleAuthUtil.invalidateToken(self, token);
						Log.e("Server auth error, please try again.", null);
					} else {
						Log.e("Server returned the following error code: " + sc,
								null);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		@Override
		protected String doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String token = null;
			try {
				token = GoogleAuthUtil.getToken(self, mEmail, scope);
			} catch (IOException transientEx) {
				// Network or server error, try later
				Log.e("IOException", transientEx.toString());
			} catch (UserRecoverableAuthException e) {
				startActivityForResult(e.getIntent(), 1001);
				Log.e("AuthException", e.toString());
			} catch (GoogleAuthException authEx) {
				// The call is not ever expected to succeed
				// assuming you have already verified that
				// Google Play services is installed.
				Log.e("GoogleAuthException", authEx.toString());
			}
			return token;
		}

		private String readResponse(InputStream is) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] data = new byte[2048];
			int len = 0;
			while ((len = is.read(data, 0, data.length)) >= 0) {
				bos.write(data, 0, len);
			}
			return new String(bos.toByteArray(), "UTF-8");
		}
	};

}
