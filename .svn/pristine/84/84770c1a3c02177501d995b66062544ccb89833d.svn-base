package com.pt.treasuretrash.service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.HomeActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.databasehelper.TreasureTrashDbHelper;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.MessageItem;
import com.pt.treasuretrash.object.ServerAlert;
import com.pt.treasuretrash.utility.BadgeUtils;
import com.pt.treasuretrash.utility.DateUtil;
import com.pt.treasuretrash.utility.SmartLog;
import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;

public class PubnubService extends Service {

	TreasureTrashDbHelper dbHelper;
	Pubnub pubnub;
	GoogleCloudMessaging gcm;
	SharedPreferences prefs;
	Context context;
	public static String SENDER_ID = "132637906866";
	public static String REG_ID;
	private static final String APP_VERSION = "3.6.1";

	// My key
	// String PUBLISH_KEY = "pub-c-35e7bb68-1682-4df7-8ebb-e4d11435a279";
	// String SUBSCRIBE_KEY = "sub-c-a24257f2-8411-11e4-9fef-02ee2ddab7fe";
	// String CIPHER_KEY = "123456";
	// String SECRET_KEY =
	// "sec-c-OWJlYzg0NjAtZmE2Mi00MmRiLWFjNzMtMzRmNWZkNTA4YjQ0";
	// // String ORIGIN = "pubsub";
	// String AUTH_KEY = "123456";

	// OD key
	String PUBLISH_KEY = "pub-c-300afb84-954c-4839-9fe3-ad15224b7062";
	String SUBSCRIBE_KEY = "sub-c-78e0e564-7169-11e4-a4d7-02ee2ddab7fe";
	String SECRET_KEY = "sec-c-ZmI5MWM2MjUtYTRjOC00MGI2LTg0OGYtNWFmOWYzMTYwOTJm";
	String CIPHER_KEY = "";
	String AUTH_KEY = "";

	String UUID;
	Boolean SSL = true;

	private NotificationManager mNM;
	private TreasureTrashSharedPreferences pref;

	private int NOTIFICATION = 1;

	public static final String INTENT_FILTER = "com.treasuretrash.pubnubservice.intent";
	public static final String MESSAGE = "com.treasuretrash.pubnubservice.message";
	public static final String CONTENT = "com.treasuretrash.pubnubservice.content";
	public static final String KEY_RECEIVED_SERVER_MESSAGE = "received_server_message";
	public static final String KEY_RECEIVED_MESSAGE = "received_message";
	public static final String KEY_RECEIVED_HISTORY = "received_history";
	public static final String KEY_MESSAGE_SENT_SUCCESS = "sent_message_success";
	public static final String KEY_MESSAGE_SENT_ERROR = "sent_message_error";
	public static final String KEY_BLOCK = "KEY_BLOCK";
	public static final String KEY_USER_ID = "KEY_USER_ID";
	public static final String KEY_BLOCK_ID = "KEY_BLOCK_ID";
	public static final String KEY_IS_BLOCK = "KEY_IS_BLOCK";
	public static final String KEY_PN_BLOCK = "___pn_block___";
	public static final String KEY_PN_UNBLOCK = "___pn_un_block___";

	LocalBroadcastManager broadcaster;

	public class LocalBinder extends Binder {
		public PubnubService getService() {
			return PubnubService.this;
		}
	}

	BroadcastReceiver disconnectAndResubscribeReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			pubnub.disconnectAndResubscribe();

		}
	};

	@Override
	public void onCreate() {
		dbHelper = new TreasureTrashDbHelper(getApplicationContext());
		pref = new TreasureTrashSharedPreferences(getApplicationContext());
		mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		// Display a notification about us starting. We put an icon in the
		// status bar.
		// showNotification();
		init();

		registerReceiver(disconnectAndResubscribeReceiver, new IntentFilter(
				ConnectivityManager.CONNECTIVITY_ACTION));

		broadcaster = LocalBroadcastManager.getInstance(this);

		if (!pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID)
				.equals("")) {
			subscribe(pref
					.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID));
		}

	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		Log.i("LocalService", "Received start id " + startId + ": " + intent);
		// We want this service to continue running until it is explicitly
		// stopped, so return sticky.

		mNM.cancel(NOTIFICATION);
		if (!pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID)
				.equals("")) {
			subscribe(pref
					.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID));
		}

		// String fakeExpireMessage =
		// "{\"pn_apns\":{\"aps\":{\"alert\":{\"action-loc-key\":\"Relist\",\"loc-key\":\"NOTIFICATION_MSG_EXPIRE\",\"loc-args\":[\"huytest1\",\"Mouse 2\"]}},\"type\":\"Expire\",\"id\":\"630ac7cb-d4c7-e411-80e6-06d45807c4a2\",\"numberOfSlot\":0}}";
		// parseAlertMessage(fakeExpireMessage);

		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		// Cancel the persistent notification.
		mNM.cancel(NOTIFICATION);

		unregisterReceiver(disconnectAndResubscribeReceiver);

		// Tell the user we stopped.
		// Toast.makeText(this, "Service stopped", Toast.LENGTH_SHORT).show();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return mBinder;
	}

	private final IBinder mBinder = new LocalBinder();

	public void notifyUser(Object message) {
		try {
			if (message instanceof JSONObject) {
				final JSONObject obj = (JSONObject) message;
				// self.runOnUiThread(new Runnable() {
				// public void run() {
				// Toast.makeText(self.getApplicationContext(),
				// obj.toString(), Toast.LENGTH_LONG).show();

				Log.i("Received msg : ", String.valueOf(obj));
				// }
				// });

			} else if (message instanceof String) {
				final String obj = (String) message;
				// self.runOnUiThread(new Runnable() {
				// public void run() {
				// // Toast.makeText(self.getApplicationContext(), obj,
				// // Toast.LENGTH_LONG).show();
				Log.i("Received msg : ", obj.toString());
				// }
				// });

			} else if (message instanceof JSONArray) {
				final JSONArray obj = (JSONArray) message;
				// self.runOnUiThread(new Runnable() {
				// public void run() {
				// Toast.makeText(self.getApplicationContext(),
				// obj.toString(), Toast.LENGTH_LONG).show();
				Log.i("Received msg : ", obj.toString());
				// }
				// });
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void init() {
		pubnub = new Pubnub(PUBLISH_KEY, SUBSCRIBE_KEY, SECRET_KEY, CIPHER_KEY,
				SSL);
		pubnub.setCacheBusting(false);
		// pubnub.setOrigin(ORIGIN);
		pubnub.setAuthKey(AUTH_KEY);
	}

	public void subscribe(String channelName) {

		String channel = channelName;

		try {
			pubnub.subscribe(channel, new Callback() {
				@Override
				public void connectCallback(String channel, Object message) {
					notifyUser("SUBSCRIBE : CONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());
				}

				@Override
				public void disconnectCallback(String channel, Object message) {
					notifyUser("SUBSCRIBE : DISCONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());
				}

				@Override
				public void reconnectCallback(String channel, Object message) {
					notifyUser("SUBSCRIBE : RECONNECT on channel:" + channel
							+ " : " + message.getClass() + " : "
							+ message.toString());
				}

				@Override
				public void successCallback(String channel, Object message) {
					notifyUser("SUBSCRIBE : " + channel + " : "
							+ message.getClass() + " : " + message.toString());
					final String json = message.toString();

					try {
						JSONObject obj = new JSONObject(json);

						if (ParserUtility.hasKey("content", obj)) {
							MessageItem item = ParserUtility.parseReceivedMessage(
									json,
									pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID));
							if (!dbHelper.checkBlockedUser(
									item.getUserId(),
									pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID),
									true)) {
								dbHelper.insertMessage(
										item,
										pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID));
								pref.putLongValue(
										TreasureTrashSharedPreferences.PREF_LAST_TIME_RECEIVED_MESSAGE
												+ pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID),
										DateUtil.getUTCTime());

								Intent intent = new Intent(INTENT_FILTER);
								if (json != null) {
									intent.putExtra(MESSAGE,
											KEY_RECEIVED_MESSAGE);
									intent.putExtra(CONTENT, json);
									broadcaster.sendBroadcast(intent);
								}
								showNotification(item);
								BadgeUtils
										.showNewMessages(getApplicationContext());
							} else {
								Log.i("CHECK_BLOCK_USER", "true");
							}
						}

						else if (ParserUtility.hasKey("__cmd__", obj)) {
							String userId = "";
							String blockId = "";
							if (ParserUtility.hasKey("userid", obj))
								userId = obj.getString("userid");
							if (ParserUtility.hasKey("blockid", obj))
								blockId = obj.getString("blockid");
							String cmd = obj.getString("__cmd__");
							boolean isBlock = cmd
									.equalsIgnoreCase(KEY_PN_BLOCK) ? true
									: false;

							String myAccountId = pref
									.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_ID);
							if (userId.equalsIgnoreCase(myAccountId)) {
								if (isBlock)
									dbHelper.insertBlockedUser(blockId,
											myAccountId, true);
								else {
									dbHelper.deleteBlockedUser(blockId,
											myAccountId, true);
								}
							} else {
								if (isBlock) {
									dbHelper.insertBlockedUser(userId,
											myAccountId, false);
								} else {
									dbHelper.deleteBlockedUser(userId,
											myAccountId, false);
								}
							}

							Intent intent = new Intent(INTENT_FILTER);
							if (json != null) {
								intent.putExtra(MESSAGE, KEY_BLOCK);
								intent.putExtra(KEY_USER_ID, userId);
								intent.putExtra(KEY_BLOCK_ID, blockId);
								intent.putExtra(KEY_IS_BLOCK, isBlock);
								broadcaster.sendBroadcast(intent);
							}
						}

						else if (ParserUtility.isJsonObject(obj, "pn_apns")
								&& !ParserUtility.hasKey("userid", obj)) {
							// showNotification(json);
							parseAlertMessage(json);
							// showNotification(json);

						}

						// else if (ParserUtility.isJsonObject(obj, "alert")
						// && obj.getString("userid") == null) {
						// showNotification(json);
						// }

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

				}

				@Override
				public void errorCallback(String channel, PubnubError error) {
					notifyUser("SUBSCRIBE : ERROR on channel " + channel
							+ " : " + error.toString());
				}
			});

		} catch (Exception e) {

		}

	}

	public void _publish(final String channel, final String message,
			final boolean isCallBack) {

		Callback publishCallback = new Callback() {
			@Override
			public void successCallback(String channel, Object message) {
				notifyUser("PUBLISH : " + message);
				String json = message.toString();
				try {

					if (isCallBack) {
						Intent intent = new Intent(INTENT_FILTER);
						if (json != null) {
							intent.putExtra(MESSAGE, KEY_MESSAGE_SENT_SUCCESS);
							intent.putExtra(CONTENT, json);
							broadcaster.sendBroadcast(intent);
						}
					}

				} catch (Exception e) {
					e.printStackTrace();
				}
				// showNotification();
			}

			@Override
			public void errorCallback(String channel, PubnubError error) {
				notifyUser("PUBLISH ERROR : " + error);
				String json = message.toString();
				Intent intent = new Intent(INTENT_FILTER);
				if (json != null) {
					intent.putExtra(MESSAGE, KEY_MESSAGE_SENT_ERROR);
					intent.putExtra(CONTENT, json);
					broadcaster.sendBroadcast(intent);
				}
				// showNotification();

			}
		};

		try {
			Integer i = Integer.parseInt(message);
			pubnub.publish(channel, i, publishCallback);
			return;
		} catch (Exception e) {
		}

		try {
			Double d = Double.parseDouble(message);
			pubnub.publish(channel, d, publishCallback);
			return;
		} catch (Exception e) {
		}

		try {
			JSONArray js = new JSONArray(message);
			pubnub.publish(channel, js, publishCallback);
			return;
		} catch (Exception e) {
		}

		try {
			JSONObject js = new JSONObject(message);
			pubnub.publish(channel, js, publishCallback);
			return;
		} catch (Exception e) {
		}

		pubnub.publish(channel, message, publishCallback);
	}

	public void unsubscribe(String channelName) {

		String channel = channelName;
		pubnub.unsubscribe(channel);

	}

	public void getHistoryByChannel(String channelName, long timeStamp) {

		Log.d("GET_HISTORY", channelName + "::" + timeStamp);
		Callback callback = new Callback() {
			public void successCallback(String channel, Object response) {
				// Log.d("PUBNUB_HISTORY", response.toString());
				SmartLog.logLongString("PUBNUB_HISTORY", response.toString());
				final String json = response.toString();
				Intent intent = new Intent(INTENT_FILTER);
				if (json != null) {
					intent.putExtra(MESSAGE, KEY_RECEIVED_HISTORY);
					intent.putExtra(CONTENT, json);
					broadcaster.sendBroadcast(intent);
				}
			}

			public void errorCallback(String channel, PubnubError error) {
				Log.d("PUBNUB_HISTORY", error.toString());
			}
		};
		long timeToken = timeStamp * 10000000;
		Log.d("GET_HISTORY_TIME", timeToken + "");
		pubnub.history(channelName, timeToken, true, callback);

	}

	/**
	 * Show a notification while this service is running.
	 */
	private void showNotification(MessageItem item) {

		String notifyContent = item.getUserName()
				+ " has sent a message about " + item.getItemName();

		Intent notificationIntent = new Intent(this, HomeActivity.class);
		notificationIntent.putExtra(GlobalValue.KEY_ACTION_FROM_NOTIFICATION,
				GlobalValue.KEY_MESSAGE_RECEIVED);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
			
			

			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					this).setSmallIcon(R.drawable.icon_notification)
					.setContentText(notifyContent).setContentTitle("Message");
			

			builder.setContentIntent(contentIntent);
			builder.setAutoCancel(true);
			builder.setLights(Color.BLUE, 500, 500);
			long[] pattern = { 500, 500, 500, 500, 500 };
			builder.setVibrate(pattern);
			builder.setStyle(new NotificationCompat.InboxStyle());
			Uri alarmSound = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			builder.setSound(alarmSound);
			
			// Add as notification
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(NOTIFICATION, builder.build());

		} else {

			Notification n = new Notification(R.drawable.icon_notification,
					getString(R.string.app_name), System.currentTimeMillis());

			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			n.setLatestEventInfo(getApplicationContext(), "Message",
					notifyContent, contentIntent);

			n.defaults |= Notification.DEFAULT_VIBRATE;

			n.defaults |= Notification.DEFAULT_SOUND;

			n.flags |= Notification.FLAG_AUTO_CANCEL;

			NotificationManager nm = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.notify((int) NOTIFICATION, n);
		}
	}

	private void parseAlertMessage(String json) {
		try {
			ServerAlert serverAlert = new ServerAlert();
			JSONObject entryObj = new JSONObject(json);
			JSONObject pn_apnsObj = entryObj.getJSONObject("pn_apns");
			JSONObject apsObj = pn_apnsObj.getJSONObject("aps");
			JSONObject alertObj = apsObj.getJSONObject("alert");

			serverAlert.setLocKey(alertObj.getString("loc-key"));

			JSONArray arrLocArgs = alertObj.getJSONArray("loc-args");
			if (arrLocArgs.length() > 0) {
				serverAlert.setLocArg1(arrLocArgs.getString(0));
			}
			if (arrLocArgs.length() > 1) {
				serverAlert.setLocArg2(arrLocArgs.getString(1));
			}

			serverAlert.setActionLocKey(alertObj.getString("action-loc-key"));

			serverAlert.setType(pn_apnsObj.getString("type"));
			serverAlert.setId(pn_apnsObj.getString("id"));
			serverAlert.setNumberSlot(pn_apnsObj.getInt("numberOfSlot"));

			showNotification(serverAlert);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void sendBlockUnblockMessage(String userId, String userName,
			String blockId, boolean isBlock) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("userid", userId);
			obj.put("username", userName);
			obj.put("blockid", blockId);
			obj.put("__cmd__", isBlock ? KEY_PN_BLOCK : KEY_PN_UNBLOCK);

			_publish(blockId, obj.toString(), false);
			_publish(userId, obj.toString(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void showNotification(ServerAlert serverAlert) {

		String notifyContent = "";
		Intent notificationIntent = new Intent(this, HomeActivity.class);
		notificationIntent.putExtra(GlobalValue.KEY_ACTION_FROM_NOTIFICATION,
				serverAlert.getLocKey());

		if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_MSG_LISTING)) {
			notificationIntent.putExtra(GlobalValue.KEY_ITEM_ID,
					serverAlert.getId());
			notifyContent = serverAlert.getLocArg1() + " listed \""
					+ serverAlert.getLocArg2() + "\"";
		}

		else if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_MSG_GONE)) {
			notifyContent = serverAlert.getLocArg1() + " marked " + "\""
					+ serverAlert.getLocArg2() + "\" as GONE";
		}

		else if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_MSG_FOLLOW)) {
			notifyContent = serverAlert.getLocArg1() + " started following you";
		}

		else if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_MSG_ADD_SINGLE_SLOT)) {
			notifyContent = "TreasureTrash has gifted you "
					+ serverAlert.getNumberSlot() + " additional "
					+ (serverAlert.getNumberSlot() > 1 ? "slots" : "slot");

			notificationIntent.putExtra(CONTENT, notifyContent);
			Intent intent = new Intent(INTENT_FILTER);
			intent.putExtra(MESSAGE, KEY_RECEIVED_SERVER_MESSAGE);
			intent.putExtra(CONTENT, notifyContent);
			broadcaster.sendBroadcast(intent);
		}

		else if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_MSG_ADD_FREE_SLOT)) {

			notifyContent = "TreasureTrash has gifted you "
					+ serverAlert.getNumberSlot() + " additional "
					+ (serverAlert.getNumberSlot() > 1 ? "slots" : "slot");

			notificationIntent.putExtra(CONTENT, notifyContent);
			Intent intent = new Intent(INTENT_FILTER);
			intent.putExtra(MESSAGE, KEY_RECEIVED_SERVER_MESSAGE);
			intent.putExtra(CONTENT, notifyContent);
			broadcaster.sendBroadcast(intent);

		}

		else if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_MSG_SUBTRACT_SINGLE_SLOT)) {

			notifyContent = "TreasureTrash has subtracted you "
					+ serverAlert.getNumberSlot() + " additional "
					+ (serverAlert.getNumberSlot() > 1 ? "slots" : "slot");

			notificationIntent.putExtra(CONTENT, notifyContent);
			Intent intent = new Intent(INTENT_FILTER);
			intent.putExtra(MESSAGE, KEY_RECEIVED_SERVER_MESSAGE);
			intent.putExtra(CONTENT, notifyContent);
			broadcaster.sendBroadcast(intent);
		}

		else if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_MSG_SUBTRACT_FREE_SLOT)) {

			notifyContent = "TreasureTrash has subtracted you "
					+ serverAlert.getNumberSlot() + " additional "
					+ (serverAlert.getNumberSlot() > 1 ? "slots" : "slot");

			notificationIntent.putExtra(CONTENT, notifyContent);
			Intent intent = new Intent(INTENT_FILTER);
			intent.putExtra(MESSAGE, KEY_RECEIVED_SERVER_MESSAGE);
			intent.putExtra(CONTENT, notifyContent);
			broadcaster.sendBroadcast(intent);
		}

		else if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_MSG_EXPIRE)) {
			notifyContent = "One of your items on TreasureTrash is about to expired";

			Intent intent = new Intent(INTENT_FILTER);
			intent.putExtra(MESSAGE, KEY_RECEIVED_SERVER_MESSAGE);
			intent.putExtra(CONTENT, notifyContent);
			broadcaster.sendBroadcast(intent);
		}

		else if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_FLAGGED_ITEM)) {

		}

		else if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_FRIEND_JOIN)) {
			notifyContent = "Your Facebook Friend has just joined TreasureTrash as TT "
					+ serverAlert.getLocArg1();
		}

		else if (serverAlert.getLocKey().equalsIgnoreCase(
				ServerAlert.NOTIFICATION_FREE_MESSAGE)) {
			notifyContent = serverAlert.getLocArg1();
			notificationIntent.putExtra(CONTENT, notifyContent);
			Intent intent = new Intent(INTENT_FILTER);
			intent.putExtra(MESSAGE, KEY_RECEIVED_SERVER_MESSAGE);
			intent.putExtra(CONTENT, notifyContent);
			broadcaster.sendBroadcast(intent);

		}

		notificationIntent.putExtra(CONTENT, notifyContent);

		if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {

			NotificationCompat.Builder builder = new NotificationCompat.Builder(
					this).setSmallIcon(R.drawable.icon_notification)
					.setContentTitle(getString(R.string.app_name))
					.setContentText(notifyContent);

			// notificationIntent = new Intent(this, HomeActivity.class);

			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			builder.setContentIntent(contentIntent);
			builder.setAutoCancel(true);
			builder.setLights(Color.BLUE, 500, 500);
			long[] pattern = { 1000, 1000 };
			builder.setVibrate(pattern);
			builder.setStyle(new NotificationCompat.InboxStyle());
			Uri alarmSound = RingtoneManager
					.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
			builder.setSound(alarmSound);
			// Add as notification
			NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
			manager.notify(NOTIFICATION, builder.build());
		}

		// ==================================================
		else {

			Notification n = new Notification(R.drawable.icon_notification,
					getString(R.string.app_name), System.currentTimeMillis());
			PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
					notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

			n.setLatestEventInfo(getApplicationContext(),
					this.getString(R.string.app_name), notifyContent,
					contentIntent);

			n.defaults |= Notification.DEFAULT_VIBRATE;

			n.defaults |= Notification.DEFAULT_SOUND;

			n.flags |= Notification.FLAG_AUTO_CANCEL;

			NotificationManager nm = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			nm.notify((int) NOTIFICATION, n);
		}
	}

	private void showNotification(String json) {

		String notifyContent = "";

		NotificationCompat.Builder builder = new NotificationCompat.Builder(
				this).setSmallIcon(R.drawable.ic_launcher)
				.setContentTitle("Treasure Trash").setContentText(json);

		Intent notificationIntent = new Intent(this, HomeActivity.class);

		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

		builder.setContentIntent(contentIntent);
		builder.setAutoCancel(true);
		builder.setLights(Color.BLUE, 500, 500);
		long[] pattern = { 500, 500, 500, 500, 500 };
		builder.setVibrate(pattern);
		builder.setStyle(new NotificationCompat.InboxStyle());
		Uri alarmSound = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		builder.setSound(alarmSound);
		// Add as notification
		NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		manager.notify(NOTIFICATION, builder.build());
	}

}
