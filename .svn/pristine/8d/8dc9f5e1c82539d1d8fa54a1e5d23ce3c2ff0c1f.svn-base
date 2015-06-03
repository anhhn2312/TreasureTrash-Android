package com.pt.treasuretrash.activity;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.database.DataSetObserver;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.adapter.MessageDetailAdapter;
import com.pt.treasuretrash.adapter.MessageDetailAdapter.IMessageDetail;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity.DialogListener;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.MessageItem;
import com.pt.treasuretrash.utility.AndroidBug5497Workaround;
import com.pt.treasuretrash.widget.AutoBgButton;

public class ChatActivity extends TreasureTrashBaseMessageActivity implements
		OnClickListener {

	int previousHeightDiffrence = 0;

	private Item currentItem;
	// private ImageView ivUser;
	private TextView tvName;
	private ListView lvMessageDetails;
	private EditText etInputMessage;
	private AutoBgButton btnBack;
	private ImageView btnSendMessage;
	private boolean isClickSendMessage = false;

	private List<MessageItem> arrMessage;
	private List<MessageItem> arrSendMessage;
	private MessageDetailAdapter adapter;

	private String currentChannel;
	private String threadId;
	Handler handler;
	private View mRootView;
	private int mLastHeightDifferece = 10;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);

		// getWindow().setSoftInputMode(
		// WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		AndroidBug5497Workaround.assistActivity(this, null);

		handler = new Handler();
		initUI();
		initControl();
		// initKeyboardObserver();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	private void checkIfBeingBlocked() {
		if (dbHelper.checkBlockedUser(currentItem.getUserID(),
				GlobalValue.myAccount.getId(), false)) {
			showMessageDialog(
					getString(R.string.message_alert_being_block_user) + " "
							+ currentItem.getUserName(), new DialogListener() {

						@Override
						public void onClose(Dialog dialog) {
							onBackPressed();

						}
					});
		}
	}

	@Override
	public void onPubnubServiceConnected() {
		// TODO Auto-generated method stub
		super.onPubnubServiceConnected();
		initData();
	}

	private void initUI() {
		mRootView = findViewById(R.id.parant_layout);
		btnBack = (AutoBgButton) findViewById(R.id.btnBack);
		tvName = (TextView) findViewById(R.id.tvName);
		etInputMessage = (EditText) findViewById(R.id.etInputMessage);
		btnSendMessage = (ImageView) findViewById(R.id.btnSendMessage);
		lvMessageDetails = (ListView) findViewById(R.id.lvMessageDetail);

		final float popUpheight = getResources().getDimension(
				R.dimen.keyboard_height);
		// end emoticons
	}

	private void initControl() {

		btnSendMessage.setOnClickListener(this);
		btnBack.setOnClickListener(this);

		etInputMessage.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (actionId == EditorInfo.IME_ACTION_SEND) {
					checkSendMessage();
					return true;
				}
				return false;
			}
		});
	}

	private void initData() {
		this.currentItem = GlobalValue.currentItem;

		tvName.setText(currentItem.getTitle());

		arrSendMessage = new ArrayList<MessageItem>();

		arrMessage = new ArrayList<MessageItem>();
		adapter = new MessageDetailAdapter(self, R.id.lvMessageDetail,
				arrMessage, iMessageDetail);
		lvMessageDetails
				.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		lvMessageDetails.setAdapter(adapter);

		// to scroll the list view to bottom on data change
		adapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				lvMessageDetails.setSelection(adapter.getCount() - 1);
			}
		});

		if (mPubnubService == null) {
			Log.e("PUBNUB_SERVICE", "null");
		}

		currentChannel = currentItem.getUserID();
		threadId = currentItem.getUserID() + GlobalValue.myAccount.getId()
				+ currentItem.getId();

		getAllHistoryByThread();
	}

	private void getAllHistoryByThread() {
		List<MessageItem> list = dbHelper.getHistoryMessageByThread(threadId,
				GlobalValue.myAccount.getId());
		arrMessage.addAll(list);
		adapter.notifyDataSetChanged();
		dbHelper.updateReadMessageForList(list);
	}

	@Override
	public void onClick(View v) {
		if (v == btnBack) {
			hideSoftKeyboard(etInputMessage);
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					onBackPressed();

				}
			}, 300);

		}
		if (v == btnSendMessage) {
			checkSendMessage();
		}

	}

	private void checkSendMessage() {
		if (!dbHelper.checkBlockedUser(currentItem.getUserID(),
				GlobalValue.myAccount.getId(), false)) {
			if (TextUtils.isEmpty(etInputMessage.getText())) {
				showMessageDialog(
						getString(R.string.message_alert_empty_message),
						new DialogListener() {

							@Override
							public void onClose(Dialog dialog) {
								// TODO Auto-generated method stub

							}
						});
			} else {
				if (!isClickSendMessage) {
					sendMessage();
					isClickSendMessage = true;
				}
			}
		} else {
			showMessageDialog(
					getString(R.string.message_alert_being_block_user) + " "
							+ currentItem.getUserName(), new DialogListener() {

						@Override
						public void onClose(Dialog dialog) {

						}
					});
		}
	}

	@Override
	public void onSendMessageSuccess() {
		arrMessage.add(arrSendMessage.get(0));
		adapter.notifyDataSetChanged();
		// assignMessage(arrSendMessage.get(0));
		dbHelper.insertMessage(arrSendMessage.get(0),
				GlobalValue.myAccount.getId());
		// insertExtra(arrSendMessage.get(0));
		arrSendMessage.remove(0);
		etInputMessage.setText("");
		showSoftKeyboard(etInputMessage);
		isClickSendMessage = false;
	}

	@Override
	public void onSendMessageError() {
		showMessageDialog(
				"There is an error sending your message. Please try again!",
				new DialogListener() {

					@Override
					public void onClose(Dialog dialog) {
						// TODO Auto-generated method stub

					}
				});
		arrSendMessage.remove(0);
		isClickSendMessage = false;
	}

	private void sendMessage() {
		MessageItem sendMessage = new MessageItem();

		Calendar cal = Calendar.getInstance();

		sendMessage.setThreadId(threadId);
		sendMessage.setDate(cal.getTimeInMillis() / 1000);
		sendMessage.setItemId(currentItem.getId());
		sendMessage.setItemImage(currentItem.getImage());
		sendMessage.setItemName(currentItem.getTitle());
		sendMessage.setListingOwnerId(currentItem.getUserID());
		sendMessage.setListingOwnerName(currentItem.getUserName());
		sendMessage.setMessage(etInputMessage.getText().toString().trim());
		sendMessage.setUserId(GlobalValue.myAccount.getId());
		sendMessage.setUserImage(GlobalValue.myAccount.getImageUrl());
		sendMessage.setUserName(GlobalValue.myAccount.getUsername());
		sendMessage.setRead(1);

		if (currentItem.getState().equals(Item.STATE_EXPIRED)) {
			sendMessage.setIsExpired(1);
		} else {
			sendMessage.setIsExpired(0);
		}
		sendMessage.setAwayId(currentItem.getUserID());
		sendMessage.setAwayImage("");
		sendMessage.setAwayName(currentItem.getUserName());

		arrSendMessage.add(sendMessage);

		String messageToSent = prepareMessage(sendMessage);

		mPubnubService._publish(currentChannel, messageToSent, true);

	}

	public String prepareMessage(MessageItem item) {
		JSONObject object = new JSONObject();
		try {
			object.put("userid", item.getUserId());
			object.put("avatarURL", item.getUserImage());
			object.put("content", item.getMessage());

			long date = item.getDate();
			object.put("date", date + "");

			object.put("displayname", item.getUserName());

			if (item.getIsExpired() == 0) {
				object.put("isExpired", false);
			} else {
				object.put("isExpired", true);
			}

			object.put("listingID", item.getItemId());
			object.put("listingName", item.getItemName());
			object.put("listingOwnName", item.getListingOwnerName());
			object.put("listingURL", item.getItemImage());
			object.put("listingown", item.getListingOwnerId());

			JSONObject apnsObj = new JSONObject();
			JSONObject apsObj = new JSONObject();
			JSONObject alertObj = new JSONObject();

			JSONArray locArgs = new JSONArray();
			locArgs.put(item.getAwayName());
			locArgs.put(item.getItemName());

			alertObj.put("loc-args", locArgs);
			alertObj.put("loc-key", "NOTIFICATION_MSG_NEW_MSG");

			apsObj.put("alert", alertObj);
			apnsObj.put("aps", apsObj);

			object.put("pn_apns", apnsObj);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return object.toString();
	}

	@Override
	public void onReceiveHistorySuccess(String messages) {
		// List<MessageItem> list = ParserUtility.parseListMessage(messages,
		// GlobalValue.myAccount.getId(), currentChannel);
		// arrMessage.addAll(list);
		// adapter.notifyDataSetChanged();
	}

	@Override
	public void onReceiveHistoryError(String error) {

	}

	@Override
	public void onReceiveMessageSuccess(String message) {
		super.onReceiveMessageSuccess(message);
		MessageItem item = ParserUtility.parseReceivedMessage(message,
				GlobalValue.myAccount.getId());
		if (item != null && item.getThreadId().equals(threadId)) {
			arrMessage.add(item);
			adapter.notifyDataSetChanged();
			dbHelper.updateReadMessageForItem(item.getThreadId(),
					item.getUserId(), item.getDate(),
					GlobalValue.myAccount.getId());
			adjustTotalNewMessages(-1);
		}

	}

	IMessageDetail iMessageDetail = new IMessageDetail() {

		@Override
		public void onClickUserImage(int position) {
			MessageItem item = arrMessage.get(position);
			Bundle b = new Bundle();
			b.putString(GlobalValue.KEY_SELLER_ID, item.getUserId());
			gotoActivity(self, SellerProfileActivity.class, b);

		}
	};

	private void initKeyboardObserver() {
		mRootView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					public void onGlobalLayout() {
						checkHeightDifference();
					}
				});
	}

	private void checkHeightDifference() {
		// get screen frame rectangle
		Rect r = new Rect();
		mRootView.getWindowVisibleDisplayFrame(r);
		// get screen height
		int screenHeight = mRootView.getRootView().getHeight();
		// calculate the height difference
		int heightDifference = screenHeight - (r.bottom - r.top);

		// if height difference is different then the last height difference and
		// is bigger then a third of the screen we can assume the keyboard is
		// open
		if (heightDifference > screenHeight / 3
				&& heightDifference != mLastHeightDifferece) {
			// keyboard visiblevisible
			// RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)
			// mRootView.getLayoutParams();
			// // set the root view height to screen height minus the height
			// difference
			// lp.height = screenHeight - heightDifference;
			// // call request layout so the changes will take affect
			// mRootView.requestLayout();
			// save the height difference so we will run this code only when a
			// change occurs.
			mLastHeightDifferece = heightDifference;

			getWindow().addFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
			getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		} else if (heightDifference != mLastHeightDifferece) {
			// keyboard hidden
			// get root view layout params and reset all the changes we have
			// made when the keyboard opened.
			// RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)
			// mRootView.getLayoutParams();
			// lp.height = screenHeight;
			// // call request layout so the changes will take affect
			// mRootView.requestLayout();
			// save the height difference so we will run this code only when a
			// change occurs.
			mLastHeightDifferece = heightDifference;

			getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
			getWindow().clearFlags(
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		}
	}
}
