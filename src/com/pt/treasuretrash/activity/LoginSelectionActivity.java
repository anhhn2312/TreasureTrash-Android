package com.pt.treasuretrash.activity;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Arrays;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.androidquery.auth.FacebookHandle;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.pt.treasuretrash.PacketUtility;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.config.FacebookConstants;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.network.AsyncHttpBase;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.HelveticaLightTextView;
import com.pt.treasuretrash.widget.ProgressHUD;

public class LoginSelectionActivity extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	private AutoBgButton btnLoginFacebook, btnLoginGoogle, btnLoginNormal;
	private HelveticaLightTextView tvSignUp, tvCancel;
	private FacebookHandle handler;
	// google variable :
	private GoogleApiClient mGoogleApiClient;
	private static final int RC_SIGN_IN = 0;
	private boolean mIntentInProgress;
	private Bundle bundle = new Bundle();
	private String forceLoginAction;
	private ProgressHUD dialog;
	private static  Activity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		self = this;
		instance = this;
		getAppKeyHash();
		setContentView(R.layout.activity_login_selection);
		initUI();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initControl();
			}
		}, 1000);

	}

	private void initUI() {
		bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(GlobalValue.FORCED_LOGIN_ACTION)) {
				forceLoginAction = bundle
						.getString(GlobalValue.FORCED_LOGIN_ACTION);
			}
		}

		btnLoginFacebook = (AutoBgButton) findViewById(R.id.btnLoginFacebook);
		btnLoginGoogle = (AutoBgButton) findViewById(R.id.btnLoginGoogle);
		btnLoginNormal = (AutoBgButton) findViewById(R.id.btnLoginNormal);
		tvSignUp = (HelveticaLightTextView) findViewById(R.id.tvSignUp);
		tvCancel = (HelveticaLightTextView) findViewById(R.id.tvCancel);
	}

	private void initControl() {
		buildGoogleApiClient();
		btnLoginFacebook.setOnClickListener(this);
		btnLoginGoogle.setOnClickListener(this);
		btnLoginNormal.setOnClickListener(this);
		tvSignUp.setOnClickListener(this);
		tvCancel.setOnClickListener(this);
	}

	public static Activity getInstance() {
		return instance;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		instance = null;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnLoginFacebook:
			onclickLoginFacebook(AccountModelManager.PROVIDER_FACEBOOK);
			break;
		case R.id.btnLoginGoogle:
			onclickLoginGoogle();
			break;
		case R.id.btnLoginNormal:
			if (bundle == null) {
				bundle = new Bundle();
			}
			gotoActivity(self, LoginEmailActivity.class, bundle);
			break;
		case R.id.tvSignUp:
			gotoActivity(self, SignupActivity.class);
			break;
		case R.id.tvCancel:
			super.onBackPressed();
			break;
		default:
			break;
		}
	}

	private void onclickLoginFacebook(final String provider) {
		// TODO Auto-generated method stub
		handler = new FacebookHandle(self, FacebookConstants.FACEBOOK_APP_ID,
				FacebookConstants.PERMISSIONS);
		handler.sso(FacebookConstants.REQUEST_FACEBOOK_SSO);

		handler.reauth(new AbstractAjaxCallback<JSONObject, FacebookHandle>() {
			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {
				super.callback(url, object, status);
				dialog = ProgressHUD.show(context, "", true, false,
						new OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								// TODO Auto-generated method stub

							}
						});

				pref.putStringValue(
						TreasureTrashSharedPreferences.PREF_FACEBOOK_ACCESS_TOKEN,
						handler.getToken());

				processLoginFacebook(provider, handler.getToken());

			}
		});

	}

	private void processLoginFacebook(String provider, String accessToken) {
		AccountModelManager.loginSocial(self, provider, accessToken, false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						ParserUtility.parseJsonLoginSuccess(self, json);

						openFacebookSession();

						if (forceLoginAction != null
								&& forceLoginAction
										.equals(GlobalValue.KEY_FORCED_LOGIN_ACTION_FAVORITE)) {
							gotoActivity(self, ItemDetailsActivity.class,
									bundle, Intent.FLAG_ACTIVITY_CLEAR_TOP);
							finish();
						} else {
							Toast.makeText(self,
									getString(R.string.login_successfull),
									Toast.LENGTH_SHORT).show();
							// gotoHomeActivity();
							getAccountInfoAfterLogin(HomeActivity.class, null);
						}
						dialog.dismiss();

					}

					@Override
					public void onError(int statusCode, String json) {
						// TODO Auto-generated method stub
						dialog.dismiss();
						if (statusCode == AsyncHttpBase.AUTHORIZATION) {
							String message = getString(R.string.error_social_account_not_register);
							showMessageDialog(message, new DialogListener() {

								@Override
								public void onClose(Dialog dialog) {
									// TODO
									// Auto-generated
									// method stub
									dialog.dismiss();
								}
							});
							return;
						}
					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub
						dialog.dismiss();
					}
				});
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
			if (resultCode == RESULT_OK)
				if (!mGoogleApiClient.isConnecting()) {
					mGoogleApiClient.connect();
				}
			mIntentInProgress = false;
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	private void gotoHomeActivity() {
		// TODO Auto-generated method stub
		gotoActivity(self, HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		finish();
	}

	// ==================================================================
	// google

	private void onclickLoginGoogle() {
		// TODO Auto-generated method stub
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
			Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
			mGoogleApiClient.disconnect();
			if (token != null) {
				AccountModelManager.loginSocial(self,
						AccountModelManager.PROVIDER_GOOGLE, token, false,
						new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								// TODO Auto-generated method stub
								ParserUtility.parseJsonLoginSuccess(self, json);

								if (forceLoginAction != null
										&& forceLoginAction
												.equals(GlobalValue.KEY_FORCED_LOGIN_ACTION_FAVORITE)) {
									gotoActivity(self,
											ItemDetailsActivity.class, bundle,
											Intent.FLAG_ACTIVITY_CLEAR_TOP);
									finish();
								} else {
									Toast.makeText(
											self,
											getString(R.string.login_successfull),
											Toast.LENGTH_SHORT).show();
									// gotoHomeActivity();
									getAccountInfoAfterLogin(
											HomeActivity.class, null);
								}
								// clear default account
								pDialog.dismiss();

							}

							@Override
							public void onError(int statusCode, String json) {
								// TODO Auto-generated method stub
								// clear default account
								pDialog.dismiss();
								if (statusCode == AsyncHttpBase.RESPONSE_ERROR) {
									String message = getString(R.string.error_default_message);
									showMessageDialog(message,
											new DialogListener() {

												@Override
												public void onClose(
														Dialog dialog) {
													// TODO Auto-generated
													// method stub
													dialog.dismiss();
												}
											});
									return;
								}

								if (statusCode == AsyncHttpBase.AUTHORIZATION) {
									String message = getString(R.string.error_social_account_not_register);
									showMessageDialog(message,
											new DialogListener() {

												@Override
												public void onClose(
														Dialog dialog) {
													// TODO Auto-generated
													// method stub
													dialog.dismiss();
												}
											});
									return;
								}
							}

							@Override
							public void onError() {
								// clear default account

								pDialog.dismiss();
							}
						});
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

	};

	// ==================================================================

	private void getAppKeyHash() {
		try {
			// key app
			PackageInfo info = getPackageManager().getPackageInfo(
					PacketUtility.getPacketName(),
					PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md;

				md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String something = new String(Base64.encode(md.digest(), 0));
				Log.d("Hash key", "HASH KEY : " + something);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
