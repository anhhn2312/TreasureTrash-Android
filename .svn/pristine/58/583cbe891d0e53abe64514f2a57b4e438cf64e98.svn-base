package com.pt.treasuretrash.activity;

import java.io.IOException;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.content.IntentSender.SendIntentException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.androidquery.auth.FacebookHandle;
import com.androidquery.callback.AbstractAjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity.DialogListener;
import com.pt.treasuretrash.config.FacebookConstants;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.network.AsyncHttpBase;
import com.pt.treasuretrash.utility.SmallUtility;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.ProgressDialog;
import com.pt.treasuretrash.widget.ProgressHUD;

@SuppressLint("NewApi")
public class LoginEmailActivity extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	private AutoBgButton btnLogin, btnLoginFacebook, btnLoginGoogle;
	private EditText etUsername, etPassword;
	private TextView lblCancel, lblSignup, lblForgotPassword;

	private GoogleApiClient mGoogleApiClient;
	private boolean mIntentInProgress;
	private static final int RC_SIGN_IN = 0;
	private FacebookHandle handler;
	private Bundle bundle;
	private String forceLoginAction = null;
	private static Activity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_normal_scroll);
		instance = this;

		bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(GlobalValue.FORCED_LOGIN_ACTION)) {
				forceLoginAction = bundle
						.getString(GlobalValue.FORCED_LOGIN_ACTION);
			}
		}

		initUI();
		initControl();
	}

	public static Activity getInstance() {
		return instance;
	}

	private void initUI() {
		btnLogin = (AutoBgButton) findViewById(R.id.btnLogin);
		btnLoginFacebook = (AutoBgButton) findViewById(R.id.btnLoginFacebook);
		btnLoginGoogle = (AutoBgButton) findViewById(R.id.btnLoginGoogle);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etPassword = (EditText) findViewById(R.id.etPassword);
		lblCancel = (TextView) findViewById(R.id.tvCancel);
		lblSignup = (TextView) findViewById(R.id.tvSignUp);
		lblForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);

	}

	private void initControl() {
		buildGoogleApiClient();
		btnLogin.setOnClickListener(this);
		btnLoginFacebook.setOnClickListener(this);
		btnLoginGoogle.setOnClickListener(this);
		lblCancel.setOnClickListener(this);
		lblSignup.setOnClickListener(this);
		lblForgotPassword.setOnClickListener(this);
		etPassword.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView view, int actionId,
					KeyEvent event) {
				int result = actionId & EditorInfo.IME_MASK_ACTION;
				switch (result) {
				case EditorInfo.IME_ACTION_DONE:
					login();
					break;
				case EditorInfo.IME_ACTION_NEXT:
					// next stuff
					break;
				}
				return false;
			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == btnLogin) {
			login();
			return;
		}
		if (v == btnLoginFacebook) {
			onclickLoginFacebook(AccountModelManager.PROVIDER_FACEBOOK);
			return;
		}
		if (v == btnLoginGoogle) {
			onclickLoginGoogle();
			return;
		}
		if (v == lblCancel) {
			onclickCancel();
			return;
		}
		if (v == lblSignup) {
			onclickSignUp();
			return;
		}
		if (v == lblForgotPassword) {
			gotoActivity(self, ForgotPasswordActivity.class);
			return;
		}

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
			mIntentInProgress = false;
			if (resultCode == RESULT_OK)
				if (!mGoogleApiClient.isConnecting()) {
					mGoogleApiClient.connect();
				}
			break;
		default:
			super.onActivityResult(requestCode, resultCode, data);
			break;
		}
	}

	private void onclickSignUp() {
		// TODO Auto-generated method stub
		gotoActivity(this, SignupActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}

	private void onclickCancel() {
		// TODO Auto-generated method stub

		super.onBackPressed();
	}

	private void onLoginSucess() {
		if (forceLoginAction != null) {
			forceLoginActionOptions();

		} else {
			getAccountInfoAfterLogin(HomeActivity.class, null);
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		instance = null;
	}

	private void login() {
		String username = etUsername.getText().toString();
		String password = etPassword.getText().toString();

		// check user
		if (username.isEmpty()) {
			etUsername.requestFocus();
			showSoftKeyboard(etUsername);
			return;

		}

		// check pass
		if (password.isEmpty()) {
			etPassword.requestFocus();
			showSoftKeyboard(etPassword);
			return;
		}
		// show dialog
		final ProgressHUD dialog;
		dialog = ProgressHUD.show(context, "", true, false,
				new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub

					}
				});
		AccountModelManager.LoginNormal(self, username, password, false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						Toast.makeText(self,
								getString(R.string.login_successfull),
								Toast.LENGTH_SHORT).show();
						Log.e("Login email", "login info : " + json);
						// parse result :
						try {
							ParserUtility.parseJsonLoginSuccess(self, json);
							// go to home
							onLoginSucess();
							dialog.dismiss();

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}

					}

					@Override
					public void onError(int statusCode, String json) {
						dialog.dismiss();
						// TODO Auto-generated method stub
						if (statusCode == AsyncHttpBase.RESPONSE_ERROR) {
							String message = getString(R.string.error_default_message);
							showMessageDialog(message, new DialogListener() {

								@Override
								public void onClose(Dialog dialog) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
							return;
						}

						if (statusCode == AsyncHttpBase.AUTHORIZATION) {
							String message = getString(R.string.error_login_invalid);
							showMessageDialog(message, new DialogListener() {

								@Override
								public void onClose(Dialog dialog) {
									// TODO Auto-generated method stub
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

	private void onclickLoginGoogle() {
		// TODO Auto-generated method stub
		if (!mGoogleApiClient.isConnected()) {
			mGoogleApiClient.connect();
		} else {
			mGoogleApiClient.reconnect();
		}
	}

	private void onclickLoginFacebook(final String provider) {
		handler = new FacebookHandle(self, FacebookConstants.FACEBOOK_APP_ID,
				FacebookConstants.PERMISSIONS);
		handler.sso(FacebookConstants.REQUEST_FACEBOOK_SSO);
		handler.reauth(new AbstractAjaxCallback<JSONObject, FacebookHandle>() {
			@Override
			public void callback(String url, JSONObject object,
					AjaxStatus status) {
				// TODO Auto-generated method stub
				super.callback(url, object, status);
				final ProgressHUD dialog;
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
				
				Log.i("FACEBOOK_TOKEN", handler.getToken());
				AccountModelManager.loginSocial(self, provider,
						handler.getToken(), false, new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								// TODO Auto-generated method stub
								ParserUtility.parseJsonLoginSuccess(self, json);
								openFacebookSession();

								if (forceLoginAction != null) {
									forceLoginActionOptions();
								} else {
									Toast.makeText(
											self,
											getString(R.string.login_successfull),
											Toast.LENGTH_SHORT).show();
									getAccountInfoAfterLogin(
											HomeActivity.class, null);
								}
								dialog.dismiss();

							}

							@Override
							public void onError(int statusCode, String json) {
								// TODO Auto-generated method stub
								dialog.dismiss();
								if (statusCode == AsyncHttpBase.AUTHORIZATION) {
									String message = getString(R.string.error_social_account_not_register);
									showMessageDialog(message,
											new DialogListener() {

												@Override
												public void onClose(
														Dialog dialog) {
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
		});
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
			if (token != null) {
				AccountModelManager.loginSocial(self,
						AccountModelManager.PROVIDER_GOOGLE, token, true,
						new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								// TODO Auto-generated method stub
								try {
									ParserUtility.parseJsonLoginSuccess(self,
											json);

									if (forceLoginAction != null) {
										forceLoginActionOptions();

									} else {
										getAccountInfoAfterLogin(
												HomeActivity.class, null);
									}

									// clear default account
									Plus.AccountApi
											.clearDefaultAccount(mGoogleApiClient);
									mGoogleApiClient.disconnect();
									pDialog.dismiss();

								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}

							@Override
							public void onError(int statusCode, String json) {
								// clear default account
								Plus.AccountApi
										.clearDefaultAccount(mGoogleApiClient);
								mGoogleApiClient.disconnect();
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
								Plus.AccountApi
										.clearDefaultAccount(mGoogleApiClient);
								mGoogleApiClient.disconnect();
								// TODO Auto-generated method stub
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

	private void gotoHomeActivity() {
		// TODO Auto-generated method stub
		gotoActivity(this, HomeActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TOP);
		this.finish();
	}

	private void forceLoginActionOptions() {
		if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_FAVORITE)) {
			getAccountInfoAfterLogin(ItemDetailsActivity.class, bundle);
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_CONTACT_SELLER)) {
			getAccountInfoAfterLogin(ItemDetailsActivity.class, bundle);
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_FLAG_ITEM)) {
			getAccountInfoAfterLogin(ItemDetailsActivity.class, bundle);
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_FOLLOW_SELLER)) {
			getAccountInfoAfterLogin(ItemDetailsActivity.class, bundle);

		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_SELLER_PROFILE)) {
			getAccountInfoAfterLogin(ItemDetailsActivity.class, bundle);
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_SHARE)) {
			getAccountInfoAfterLogin(ItemDetailsActivity.class, bundle);
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_MESSAGE)) {
			getAccountInfoAfterLogin(HomeActivity.class, bundle);
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_SAVED_ITEM)) {
			getAccountInfoAfterLogin(HomeActivity.class, bundle);
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_ACTIVITY_FEED)) {
			getAccountInfoAfterLogin(HomeActivity.class, bundle);
		} else if (forceLoginAction
				.equalsIgnoreCase(GlobalValue.KEY_FORCED_LOGIN_ACTION_ADD_LISTING)) {
			getAccountInfoAfterLogin(HomeActivity.class, bundle);
		}

	}
}
