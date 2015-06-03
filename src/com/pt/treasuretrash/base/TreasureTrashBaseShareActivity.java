package com.pt.treasuretrash.base;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;

import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ShareCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenSource;
import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.Session.StatusCallback;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.PlusShare;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.config.Constant.Extra;
import com.pt.treasuretrash.config.FacebookConstants;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.config.WebserviceConfig;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.utility.DialogUtility;

public class TreasureTrashBaseShareActivity extends
		TreasureTrashBaseMessageActivity implements ConnectionCallbacks,
		OnConnectionFailedListener {

	private UiLifecycleHelper uiHelper;

	/* Request code used to invoke sign in user interactions. */
	public static final int RC_SIGN_IN = 300;

	public GoogleApiClient mGoogleApiClient;

	public boolean mIntentInProgress;

	public String rootFolder;

	public int currentAction;
	public static final int ACTION_SHARE = 1;
	public static final int ACTION_INVITE = 2;
	public static final int SHARE_GOOGLE_REQUEST_CODE = 0;
	public final String KEY_FB_GESTURE = "com.facebook.platform.extra.COMPLETION_GESTURE";

	// 03-16 10:10:25.004: I/ACTIVITY_RESULT(27291): 64207---1

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		uiHelper = new UiLifecycleHelper(this, null);
		uiHelper.onCreate(savedInstanceState);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this).addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN).build();

		rootFolder = Environment.getExternalStorageDirectory() + "/"
				+ getString(R.string.app_name) + "/";
		File folder = new File(rootFolder);
		if (!folder.exists()) {
			folder.mkdirs();
		}

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
		uiHelper.onResume();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	protected void onActivityResult(int requestCode, final int resultCode,
			Intent data) {
		// super.onActivityResult(requestCode, resultCode, data);

		Log.i("ACTIVITY_RESULT", requestCode + "--" + resultCode);

		if (requestCode == SHARE_GOOGLE_REQUEST_CODE && resultCode == RESULT_OK) {
			if (currentAction == ACTION_SHARE) {
				DialogUtility.showShortToast(self,
						getString(R.string.message_share_successfully));
			} else if (currentAction == ACTION_INVITE) {
				DialogUtility.showShortToast(self,
						getString(R.string.message_invite_successfully));
			}
		}

		uiHelper.onActivityResult(requestCode, resultCode, data,
				new FacebookDialog.Callback() {
					@Override
					public void onError(FacebookDialog.PendingCall pendingCall,
							Exception error, Bundle data) {
						Log.e("Activity",
								String.format("Error: %s", error.toString()));
						showMessageDialog(
								getString(R.string.message_alert_general_error),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub

									}
								});
					}

					@Override
					public void onComplete(
							FacebookDialog.PendingCall pendingCall, Bundle data) {
						for (String key : data.keySet()) {
							Log.d("FACEBOOK_BUNDLE", key + "--" + data.get(key));
						}
						// com.facebook.platform.extra.COMPLETION_GESTURE--cancel
						// com.facebook.platform.extra.DID_COMPLETE--true

						if (resultCode == RESULT_OK) {
							if (data.getString(KEY_FB_GESTURE)
									.equalsIgnoreCase("post")) {
								if (currentAction == ACTION_SHARE) {

									DialogUtility
											.showShortToast(
													self,
													getString(R.string.message_share_successfully));
								} else if (currentAction == ACTION_INVITE) {
									DialogUtility
											.showShortToast(
													self,
													getString(R.string.message_invite_successfully));
								}
							}
						}
					}
				});

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		uiHelper.onDestroy();
		mGoogleApiClient = null;
	}

	@Override
	public void onConnectionFailed(ConnectionResult result) {
		if (!mIntentInProgress && result.hasResolution()) {
			try {
				mIntentInProgress = true;
				startIntentSenderForResult(result.getResolution()
						.getIntentSender(), RC_SIGN_IN, null, 0, 0, 0);
			} catch (SendIntentException e) {
				// The intent was canceled before it was sent. Return to the
				// default
				// state and attempt to connect to get an updated
				// ConnectionResult.
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		// We've resolved any connection errors. mGoogleApiClient can be used to
		// access Google APIs on behalf of the user.
	}

	@Override
	public void onConnectionSuspended(int arg0) {
		mGoogleApiClient.connect();

	}

	public void onShareFacebook(View v, Item item) {

		currentAction = ACTION_SHARE;
		final String name = item.getTitle();
		final String itemLink = WebserviceConfig.APP_SHARE_ITEM_PATH
				+ item.getId();
		final String description = item.getDescription();
		final String caption = getString(R.string.message_sell_your_trash_find_a_treasure);
		final String imageUrl = item.getImage();

		if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			// Publish the post using the Share Dialog
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
					self).setName(name).setDescription(description)
					.setCaption(caption).setLink(itemLink).build();
			// shareDialog.present();
			uiHelper.trackPendingDialogCall(shareDialog.present());

		} else {

			Session.openActiveSession(self, true, new StatusCallback() {

				@Override
				public void call(Session session, SessionState state,
						Exception exception) {
					if (session != null && session.isOpened()) {
						shareFacebookViaFeedDialog(name, description, caption,
								itemLink, imageUrl);
					}
				}

			});
		}
	}

	private void shareFacebookViaFeedDialog(String name, String description,
			String caption, String link, String imageUrl) {
		Bundle params = new Bundle();
		params.putString("name", name);
		params.putString("caption", caption);
		params.putString("picture", imageUrl);
		params.putString("description", description);
		params.putString("link", link);

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(self,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(self, "Share successfully",
										Toast.LENGTH_SHORT).show();
							} else {
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							// Toast.makeText(self.getApplicationContext(),
							// "Error sharing", Toast.LENGTH_SHORT).show();
						} else {
							// Generic, ex: network error
							Toast.makeText(self.getApplicationContext(),
									"Error sharing", Toast.LENGTH_SHORT).show();
						}
					}

				}).build();
		feedDialog.show();
	}

	public void onShareGoogle(View v, Item item) {
		currentAction = ACTION_SHARE;
		try {

			String itemLink = WebserviceConfig.APP_SHARE_ITEM_PATH
					+ item.getId();

			Intent shareIntent = ShareCompat.IntentBuilder.from(this)
					.setSubject(item.getTitle()).setType("text/plain")
					.setText(itemLink).getIntent()
					.setPackage("com.google.android.apps.plus");

			startActivityForResult(shareIntent, SHARE_GOOGLE_REQUEST_CODE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onShareEmail(View v, Item item) {

		File shareFile = saveCurrentImageCache(item);
		Uri uri = Uri.parse("file://" + shareFile);

		String itemLink = WebserviceConfig.APP_SHARE_ITEM_PATH + item.getId();
		// String itemLink = "http://projectemplate.com/content/index.html";

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("image/*");
		// i.putExtra(Intent.EXTRA_EMAIL , new
		// String[]{currentUser.getmEmail()});
		i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		i.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_share_item)
				+ System.getProperty("line.separator") + itemLink);
		i.putExtra(Intent.EXTRA_STREAM, uri);
		try {
			startActivity(Intent.createChooser(i, "Share using"));
		} catch (android.content.ActivityNotFoundException ex) {
			DialogUtility.showLongToast(self,
					"There are no email clients installed.");
		}
	}

	public void onShareMessage(View v, Item item) {

		String itemLink = WebserviceConfig.APP_SHARE_ITEM_PATH + item.getId();

		String shareBody = getString(R.string.message_share_item)
				+ System.getProperty("line.separator") + itemLink;
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		// sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
		// getString(R.string.app_name));
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Share using"));
	}

	public void onInviteFacebook() {
		currentAction = ACTION_INVITE;
		final String link = getString(R.string.app_link_share_facebook);
		final String description = getString(R.string.message_invite_friend);
		final String caption = getString(R.string.message_invite_friend);

		if (FacebookDialog.canPresentShareDialog(getApplicationContext(),
				FacebookDialog.ShareDialogFeature.SHARE_DIALOG)) {
			// Publish the post using the Share Dialog
			FacebookDialog shareDialog = new FacebookDialog.ShareDialogBuilder(
					self).setDescription(description).setLink(link).build();
			// shareDialog.present();
			uiHelper.trackPendingDialogCall(shareDialog.present());
		}

		else {

			// share via web dialog
			Session.openActiveSession(self, true, new StatusCallback() {

				@Override
				public void call(Session session, SessionState state,
						Exception exception) {
					if (session != null && session.isOpened()) {
						inviteFacebookViaFeedDialog(link, description, caption,
								link);
					}
				}

			});
		}
	}

	private void inviteFacebookViaFeedDialog(String name, String description,
			String caption, String link) {
		Bundle params = new Bundle();
		// params.putString("name", name);
		// params.putString("caption", caption);
		params.putString("description", description);
		params.putString("link", link);

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(self,
				Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values,
							FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(self, "Invite successfully",
										Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								// Toast.makeText(self.getApplicationContext(),
								// "Publish cancelled", Toast.LENGTH_SHORT)
								// .show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							// Toast.makeText(self.getApplicationContext(),
							// "Error inviting", Toast.LENGTH_SHORT)
							// .show();
						} else {
							// Generic, ex: network error
							Toast.makeText(self.getApplicationContext(),
									"Error inviting", Toast.LENGTH_SHORT)
									.show();
						}
					}

				}).build();
		feedDialog.show();
	}

	public void onInviteGoogle() {
		currentAction = ACTION_INVITE;
		try {

			String link = getString(R.string.app_link_share_google);

			Intent shareIntent = ShareCompat.IntentBuilder
					.from(this)
					.setSubject("")
					.setType("text/plain")
					.setText(
							getString(R.string.message_invite_friend)
									+ System.getProperty("line.separator")
									+ link).getIntent()
					.setPackage("com.google.android.apps.plus");

			// Intent shareIntent = new PlusShare.Builder(this)
			// .setType("text/plain")
			// .setText(getString(R.string.message_invite_friend))
			// .setContentUrl(Uri.parse(link))
			// .getIntent();
			//
			//
			startActivityForResult(shareIntent, SHARE_GOOGLE_REQUEST_CODE);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void onInviteEmail() {
		String link = getString(R.string.app_link_share_google);

		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("message/rfc822");
		// i.putExtra(Intent.EXTRA_EMAIL , new
		// String[]{currentUser.getmEmail()});
		i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
		i.putExtra(Intent.EXTRA_TEXT, getString(R.string.message_invite_friend)
				+ System.getProperty("line.separator") + link);
		try {
			startActivity(Intent.createChooser(i, "Invite using"));
		} catch (android.content.ActivityNotFoundException ex) {
			DialogUtility.showLongToast(self,
					"There are no email clients installed.");
		}
	}

	public void onInviteMessage() {
		String itemLink = getString(R.string.app_link_share_google);

		String shareBody = getString(R.string.message_invite_friend)
				+ System.getProperty("line.separator") + itemLink;
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
		startActivity(Intent.createChooser(sharingIntent, "Invite using"));
	}

	private File saveCurrentImageCache(Item item) {
		Bitmap bitmap = aq.getCachedImage(item.getImage());
		// File cache = getExternalCacheDir();
		File tempFile = new File(rootFolder, item.getId() + ".jpg");

		if (tempFile.exists()) {
			return tempFile;
		} else {
			try {
				FileOutputStream out = new FileOutputStream(tempFile);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
				out.flush();
				out.close();
			} catch (IOException e) {
			}
		}
		return tempFile;
	}

}
