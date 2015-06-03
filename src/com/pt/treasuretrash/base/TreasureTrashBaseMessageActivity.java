package com.pt.treasuretrash.base;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.Session.StatusCallback;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.ErrorActivity;
import com.pt.treasuretrash.activity.HomeActivity;
import com.pt.treasuretrash.activity.LoginEmailActivity;
import com.pt.treasuretrash.activity.LoginSelectionActivity;
import com.pt.treasuretrash.activity.SignupActivity;
import com.pt.treasuretrash.activity.SignupEmailActivity_1;
import com.pt.treasuretrash.activity.SignupEmailActivity_2;
import com.pt.treasuretrash.activity.WalkthrouhActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity.DialogListener;
import com.pt.treasuretrash.config.FacebookConstants;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.network.StatusBackend;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.object.MessageItem;
import com.pt.treasuretrash.service.PubnubService;
import com.pt.treasuretrash.utility.BadgeUtils;
import com.pt.treasuretrash.utility.DateUtil;
import com.pt.treasuretrash.widget.CustomDialog;
import com.pt.treasuretrash.widget.ProgressHUD;
import com.pt.treasuretrash.widget.CustomDialog.OnCustomDialogClickListener;

public class TreasureTrashBaseMessageActivity extends
		TreasureTrashBaseFunctionsActivity {

	public PubnubService mPubnubService;
	private boolean mIsBound;
	public BroadcastReceiver pubnubReceiver;

	public RelativeLayout rlNewMessage;
	public TextView tvNewMessage;
	public LinearLayout llMessage;

	public int currentFragment = -1;
	public static int totalNewMessages = 0;

	private ServiceConnection mConnection;
	public boolean isPubnubServiceConnected;

	public void onPubnubServiceConnected() {
		isPubnubServiceConnected = true;
		if (GlobalValue.myAccount != null) {
			mPubnubService
					.unsubscribe(pref
							.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID));
			mPubnubService.subscribe(GlobalValue.myAccount.getId());
		}
	}

	public void onPubnubServiceDisconnected() {
		isPubnubServiceConnected = false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mConnection = new ServiceConnection() {
			public void onServiceConnected(ComponentName className,
					IBinder service) {

				mPubnubService = ((PubnubService.LocalBinder) service)
						.getService();

				// Toast.makeText(self, "Pubnub service connected",
				// Toast.LENGTH_SHORT).show();

				onPubnubServiceConnected();
			}

			public void onServiceDisconnected(ComponentName className) {
				mPubnubService = null;
				// Toast.makeText(self, "Pubnub service disconnected",
				// Toast.LENGTH_SHORT).show();
				onPubnubServiceDisconnected();
			}
		};

		pubnubReceiver = new BroadcastReceiver() {

			@Override
			public void onReceive(Context context, Intent intent) {
				if (intent.getStringExtra(PubnubService.MESSAGE).equals(
						PubnubService.KEY_RECEIVED_MESSAGE)) {
					final String json = intent
							.getStringExtra(PubnubService.CONTENT);
					self.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							onReceiveMessageSuccess(json);

						}
					});
				}

				if (intent.getStringExtra(PubnubService.MESSAGE).equals(
						PubnubService.KEY_RECEIVED_HISTORY)) {
					final String json = intent
							.getStringExtra(PubnubService.CONTENT);
					self.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							onReceiveHistorySuccess(json);

						}
					});
				}

				if (intent.getStringExtra(PubnubService.MESSAGE).equals(
						PubnubService.KEY_MESSAGE_SENT_SUCCESS)) {
					final String json = intent
							.getStringExtra(PubnubService.CONTENT);
					self.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							onSendMessageSuccess();

						}
					});
				}

				if (intent.getStringExtra(PubnubService.MESSAGE).equals(
						PubnubService.KEY_MESSAGE_SENT_ERROR)) {
					final String json = intent
							.getStringExtra(PubnubService.CONTENT);
					self.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							onSendMessageError();

						}
					});
				}

				if (intent.getStringExtra(PubnubService.MESSAGE)
						.equalsIgnoreCase(PubnubService.KEY_BLOCK)) {
					final String userId = intent
							.getStringExtra(PubnubService.KEY_USER_ID);
					final String blockId = intent
							.getStringExtra(PubnubService.KEY_BLOCK_ID);
					final boolean isBlock = intent.getBooleanExtra(
							PubnubService.KEY_IS_BLOCK, true);
					self.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							onBlockUnblock(userId, blockId, isBlock);

						}
					});

				}
				if (intent.getStringExtra(PubnubService.MESSAGE)
						.equalsIgnoreCase(
								PubnubService.KEY_RECEIVED_SERVER_MESSAGE)) {

					String content = intent
							.getStringExtra(PubnubService.CONTENT);

					if (content.contains("expired")) {
						onReceiveItemExpired(content);
					} else {
						onReceiveServerMessage(content);
					}
				}

			}
		};
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		LocalBroadcastManager.getInstance(this)
				.registerReceiver((pubnubReceiver),
						new IntentFilter(PubnubService.INTENT_FILTER));
		bindService(new Intent(self, PubnubService.class), mConnection,
				Context.BIND_AUTO_CREATE);
		mIsBound = true;

		if (isLoggedIn()) {
			if (GlobalValue.myAccount == null) {

				if (!pref.getStringValue(
						TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON)
						.equals("")) {
					GlobalValue.myAccount = ParserUtility
							.parseAccount(
									pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON),
									pref);
				}
			}
		}

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		LocalBroadcastManager.getInstance(this).unregisterReceiver(
				pubnubReceiver);
		if (mIsBound) {
			unbindService(mConnection);
			mIsBound = false;
		}
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mConnection = null;
	}

	public void getAccountInfoAfterLogin(final Class nextActivity,
			final Bundle bundle) {
		// Set data
		AccountModelManager.getAccountInfo(self, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						Log.e(TAG, "JSon Profile: " + json);

						pref.putStringValue(
								TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON,
								json);
						GlobalValue.myAccount = ParserUtility.parseAccount(
								json, pref);

						if (bundle != null) {

							bundle.putString(GlobalValue.KEY_ACTION,
									GlobalValue.KEY_ACTION_AFTER_LOGIN);
							gotoActivity(self, nextActivity, bundle,
									Intent.FLAG_ACTIVITY_CLEAR_TOP);
						} else {
							Bundle newBundle = new Bundle();
							newBundle.putString(GlobalValue.KEY_ACTION,
									GlobalValue.KEY_ACTION_AFTER_LOGIN);
							gotoActivity(self, nextActivity, newBundle,
									Intent.FLAG_ACTIVITY_CLEAR_TOP);
						}

						// clear all activity
						clearAllActivities();

					}

					@Override
					public void onError(int statusCode, String json) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}

				});
	}

	private void clearAllActivities() {
		if (WalkthrouhActivity.getInstance() != null) {
			WalkthrouhActivity.getInstance().finish();
		}
		if (LoginSelectionActivity.getInstance() != null) {
			LoginSelectionActivity.getInstance().finish();
		}
		if (LoginEmailActivity.getInstance() != null) {
			LoginEmailActivity.getInstance().finish();
		}
		if (SignupActivity.getInstance() != null) {
			SignupActivity.getInstance().finish();
		}
		if (SignupEmailActivity_1.getInstance() != null) {
			SignupEmailActivity_1.getInstance().finish();
		}
		if (SignupEmailActivity_2.getInstance() != null) {
			SignupEmailActivity_2.getInstance().finish();
		}
	}

	public void getInitBlockerAndBlockedUsers() {
		AccountModelManager.getListBlockedUsers(self, false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						List<String> arrBlocked = ParserUtility
								.parseBlockedUsers(json);
						List<String> arrBlocker = ParserUtility
								.parseBlocker(json);
						dbHelper.deleteAllBlockerAndBlockedUsers(GlobalValue.myAccount
								.getId());
						for (int i = 0; i < arrBlocked.size(); i++) {
							Log.i("ARR_BLOCKED", arrBlocked.get(i));
							dbHelper.insertBlockedUser(arrBlocked.get(i),
									GlobalValue.myAccount.getId(), true);
						}
						for (int i = 0; i < arrBlocker.size(); i++) {
							dbHelper.insertBlockedUser(arrBlocker.get(i),
									GlobalValue.myAccount.getId(), false);
						}

					}

					@Override
					public void onError(int statusCode, String json) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				});
	}

	public String prepareMessage(MessageItem item) {
		JSONObject object = new JSONObject();
		try {
			object.put("userid", GlobalValue.myAccount.getId());
			object.put("avatarURL", item.getUserImage());
			object.put("content", item.getMessage());
			object.put("date", item.getDate());
			object.put("displayname", item.getUserName());
			object.put("isExpired", 0);
			object.put("listingID", item.getItemId());
			object.put("listingName", item.getItemName());
			object.put("listingOwnName", item.getListingOwnerName());
			object.put("listingURL", item.getItemImage());
			object.put("listingown", item.getListingOwnerId());

		} catch (Exception e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	public void onSendMessageSuccess() {
		// Todo
	}

	public void onSendMessageError() {
		// Todo
	}

	public void onReceiveHistorySuccess(String messages) {
		List<MessageItem> list = ParserUtility.parseListMessage(messages,
				GlobalValue.myAccount.getId());
		dbHelper.insertMessagesList(list, GlobalValue.myAccount.getId());
		pref.putLongValue(
				TreasureTrashSharedPreferences.PREF_LAST_TIME_RECEIVED_MESSAGE
						+ pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID),
				DateUtil.getUTCTime());
		getTotalNewMessages();
	}

	public void onReceiveHistoryError(String error) {
		// Todo
	}

	public void onReceiveMessageSuccess(String message) {
		adjustTotalNewMessages(+1);

	}

	public void onBlockUnblock(String userId, String blockId, boolean isBlock) {

	}

	public void getTotalNewMessages() {

		totalNewMessages = 0;
		if (isLoggedIn()) {

			try {
				totalNewMessages = dbHelper
						.getTotalNewMessages(GlobalValue.myAccount.getId());
			} catch (NullPointerException ex) {
				ex.toString();
			}
		}
		calculateNewMessages();

	}

	public void calculateNewMessages() {

		if (totalNewMessages > 0) {
			if (currentFragment == HomeActivity.FRAGMENT_MESSAGE
					|| currentFragment == HomeActivity.FRAGMENT_MESSAGE_LIST) {
				if (llMessage != null)
					llMessage
							.setBackgroundResource(R.drawable.bg_bottom_menu_message_new_selected);

			} else {
				if (llMessage != null)
					llMessage
							.setBackgroundResource(R.drawable.bg_bottom_menu_message_new);
			}
			if (rlNewMessage != null) {
				rlNewMessage.setVisibility(View.VISIBLE);
				tvNewMessage.setText(totalNewMessages + "");
				BadgeUtils.setBadge(self, totalNewMessages);
			}
			LinearLayout.LayoutParams messageParam = null;

			if (totalNewMessages > 99) {
				messageParam = new LinearLayout.LayoutParams(
						(int) getResources().getDimension(
								R.dimen.button_size_xsmall),
						(int) getResources().getDimension(
								R.dimen.button_size_small));
			} else {
				messageParam = new LinearLayout.LayoutParams(
						(int) getResources().getDimension(
								R.dimen.button_size_small),
						(int) getResources().getDimension(
								R.dimen.button_size_small));
			}
			if (rlNewMessage != null) {
				rlNewMessage.setLayoutParams(messageParam);
			}
		} else {
			if (currentFragment == HomeActivity.FRAGMENT_MESSAGE
					|| currentFragment == HomeActivity.FRAGMENT_MESSAGE_LIST) {
				if (llMessage != null)
					llMessage
							.setBackgroundResource(R.drawable.bg_bottom_menu_message_selected);

			} else {
				if (llMessage != null)
					llMessage
							.setBackgroundResource(R.drawable.bg_bottom_menu_message);
			}
			if (rlNewMessage != null) {
				rlNewMessage.setVisibility(View.INVISIBLE);
			}
		}
	}

	public void adjustTotalNewMessages(int adjust) {
		totalNewMessages += adjust;
		calculateNewMessages();
	}

	public void init() {

		if (GlobalValue.myAccount != null) {
			if (isPubnubServiceConnected) {
				mPubnubService
						.unsubscribe(pref
								.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID));
				mPubnubService.subscribe(GlobalValue.myAccount.getId());
			}
			pref.putStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID,
					GlobalValue.myAccount.getId());
			// getTotalNewMessages();
			// checkLoginUI();
			// getBlockedUsers();
		}

	}

	public void onReceiveServerMessage(String message) {
		showMessageDialog(message, new DialogListener() {

			@Override
			public void onClose(Dialog dialog) {
				// TODO Auto-generated method stub

			}
		});
	}

	public void onReceiveItemExpired(String message) {
		if (!HomeActivity.isHomeVisible) {
			new CustomDialog(self, message, "",
					getString(R.string.relist_item),
					getString(R.string.cancel),
					new OnCustomDialogClickListener() {

						@Override
						public void onYes() {
							Bundle bundle = new Bundle();
							bundle.putString(GlobalValue.KEY_ACTION,
									GlobalValue.KEY_ACTION_AFTER_DELETE);
							gotoActivity(self, HomeActivity.class, bundle,
									Intent.FLAG_ACTIVITY_CLEAR_TOP);
						}

						@Override
						public void onNo() {

						}

						@Override
						public void onNeutral() {

						}
					}).show();
		}
	}

	public void resetLogout() {
		mPubnubService
				.unsubscribe(pref
						.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID));

		pref.putStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN,
				"");

		pref.putStringValue(TreasureTrashSharedPreferences.PREF_REFRESH_TOKEN,
				"");

		pref.putStringValue(
				TreasureTrashSharedPreferences.PREF_FACEBOOK_ACCESS_TOKEN, "");

		pref.putLongValue(
				TreasureTrashSharedPreferences.PREF_TOKEN_EXPIRED_TIME, 0);
		pref.putStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON,
				"");
		pref.putStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID, "");
		pref.putStringValue(TreasureTrashSharedPreferences.PREF_NEW_EMAIL, "");

		// Intent intent = new Intent(self, PubnubService.class);
		// stopService(intent);
		GlobalValue.myAccount = null;
		BadgeUtils.clearBadge(self);
		finish();
	}

	public void resetExit() {
		// pref.putStringValue(TreasureTrashSharedPreferences.PREF_ACCESS_TOKEN,
		// "");
		// pref.putStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON,
		// "");
		GlobalValue.myAccount = null;
	}

	public Session getFacebookActiveSession() {
		return Session.getActiveSession();
	}

	public void openFacebookSession() {
		Session session = Session.getActiveSession();
		if (session != null && session.isOpened()) {
		} else {

			AccessToken token = AccessToken
					.createFromExistingAccessToken(
							pref.getStringValue(TreasureTrashSharedPreferences.PREF_FACEBOOK_ACCESS_TOKEN),
							null, null, AccessTokenSource.CLIENT_TOKEN, Arrays
									.asList(FacebookConstants.PERMISSIONS
											.split(",")));
			Session.openActiveSessionWithAccessToken(self, token,
					new StatusCallback() {

						@Override
						public void call(Session session, SessionState state,
								Exception exception) {
							if (session != null && session.isOpened()) {
							}

						}
					});
		}
	}

	public void showSignupSocialErrorMessage(int errorCode, int type) {

		if (errorCode == StatusBackend.USER_EXISTED) {
			String message = getString(R.string.error_sign_up_username_existed);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}

		if (errorCode == StatusBackend.EMAIL_EXISTED) {
			String message = getString(R.string.error_sign_up_email_existed_account);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}

		if (errorCode == StatusBackend.URL_INVALID) {
			String message = getString(R.string.error_default_message);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}
		if (errorCode == StatusBackend.USER_IS_LOCKED) {
			String message = getString(R.string.error_sign_up_username_existed);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}
		if (errorCode == StatusBackend.EXTERNAL_USER_EXISTING) {
			String message = "";
			if (type == Account.ACC_FACEBOOK) {
				message = getString(R.string.error_sign_up_social_user_existed_facebook);
			}
			else if(type == Account.ACC_GOOGLE){
				message = getString(R.string.error_sign_up_social_user_existed_google);
			}
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}

	}

}
