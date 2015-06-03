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
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.androidquery.AQuery;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.adapter.MessageDetailAdapter;
import com.pt.treasuretrash.adapter.MessageDetailAdapter.IMessageDetail;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.MessageItem;
import com.pt.treasuretrash.widget.AutoBgButton;

public class ContactSellerActivity extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	private Item currentItem;
	// private ImageView ivUser;
	private TextView tvName;
	private ListView lvMessageDetails;
	private EditText etInputMessage;
	private AutoBgButton btnBack;
	private ImageView btnSendMessage;
	private RelativeLayout rlContainer;
	private LinearLayout parentLayout;
	private boolean isClickSendMessage = false;

	private List<MessageItem> arrMessage;
	private List<MessageItem> arrSendMessage;
	private MessageDetailAdapter adapter;
	private AQuery aq;

	private String currentChannel;
	private String threadId;
	private static boolean keyboardHidden = true;
	private static int reduceHeight = 0;
	int previousHeightDiffrence = 0;
	private int keyboardHeight;
	private boolean isKeyBoardVisible;
	Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		aq = new AQuery(self);

		handler = new Handler();
		initUI();
		initControl();
		// initKeyboardObserver2();
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		checkIfBeingBlocked();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	private void checkIfBeingBlocked() {
		if (dbHelper.checkBlockedUser(currentItem.getUserID(),
				GlobalValue.myAccount.getId(), false)) {
			showMessageDialog(
					getString(R.string.message_alert_being_block_user)
							+ " " + currentItem.getUserName(),
					new DialogListener() {

						@Override
						public void onClose(Dialog dialog) {
							onBackPressed();

						}
					});
		}
	}

	private void initUI() {
		parentLayout = (LinearLayout) findViewById(R.id.llParent);
		tvName = (TextView) findViewById(R.id.tvName);
		lvMessageDetails = (ListView) findViewById(R.id.lvMessageDetail);
		etInputMessage = (EditText) findViewById(R.id.etInputMessage);
		etInputMessage.setText("");
		btnSendMessage = (ImageView) findViewById(R.id.btnSendMessage);
		btnBack = (AutoBgButton) findViewById(R.id.btnBack);

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

	private void initKeyboardObserver() {
		parentLayout.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {

						Rect r = new Rect();
						parentLayout.getWindowVisibleDisplayFrame(r);

						int screenHeight = parentLayout.getRootView()
								.getHeight();
						int heightDifference = screenHeight - (r.bottom);

						if (previousHeightDiffrence - heightDifference > 100) {
							// popupWindow.dismiss();
						}

						previousHeightDiffrence = heightDifference;
						if (heightDifference > 100) {

							isKeyBoardVisible = true;
							changeKeyboardHeight(heightDifference);

						} else {

							isKeyBoardVisible = false;

						}

					}
				});
	}

	private void changeKeyboardHeight(int height) {

		if (height > 100) {
			int mylistviewHeight = lvMessageDetails.getMeasuredHeight();
			keyboardHeight = height;
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					LayoutParams.MATCH_PARENT, keyboardHeight);
			// emoticonsCover.setLayoutParams(params);
			if (isKeyBoardVisible) {

				LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						mylistviewHeight - height);
				lvMessageDetails.setLayoutParams(mParam);
				lvMessageDetails.requestLayout();

			} else {

				LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,
						mylistviewHeight + height);
				lvMessageDetails.setLayoutParams(mParam);
				lvMessageDetails.requestLayout();

			}

		}

	}

	private void initKeyboardObserver2() {
		final View decorView = this.getWindow().getDecorView();
		decorView.getViewTreeObserver().addOnGlobalLayoutListener(
				new ViewTreeObserver.OnGlobalLayoutListener() {
					@Override
					public void onGlobalLayout() {
						Rect rect = new Rect();
						decorView.getWindowVisibleDisplayFrame(rect);

						int displayHeight = rect.bottom - rect.top;
						int height = decorView.getHeight();
						boolean keyboardHiddenTemp = (double) displayHeight
								/ height > 0.8;
						int mylistviewHeight = lvMessageDetails
								.getMeasuredHeight();

						if (keyboardHiddenTemp != keyboardHidden) {
							keyboardHidden = keyboardHiddenTemp;

							if (!keyboardHidden) {

								reduceHeight = height - displayHeight;

								LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.MATCH_PARENT,
										mylistviewHeight - reduceHeight);
								lvMessageDetails.setLayoutParams(mParam);
								lvMessageDetails.requestLayout();

							} else {

								LinearLayout.LayoutParams mParam = new LinearLayout.LayoutParams(
										LinearLayout.LayoutParams.MATCH_PARENT,
										mylistviewHeight + reduceHeight);
								lvMessageDetails.setLayoutParams(mParam);
								lvMessageDetails.requestLayout();

							}
						}

					}
				});
	}

	private void getAllHistoryByThread() {
		List<MessageItem> list = dbHelper.getHistoryMessageByThread(threadId,
				GlobalValue.myAccount.getId());
		arrMessage.addAll(list);
		adapter.notifyDataSetChanged();
		dbHelper.updateReadMessageForList(list);
	}

	private void initControl() {

		btnSendMessage.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		
		etInputMessage.setOnEditorActionListener(new OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if(actionId == EditorInfo.IME_ACTION_SEND){
					checkSendMessage();
					return true;
				}
				return false;
			}
		});
	}

	@Override
	public void onPubnubServiceConnected() {
		// TODO Auto-generated method stub
		super.onPubnubServiceConnected();
		initData();
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		return super.dispatchTouchEvent(event);
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
	
	private void checkSendMessage(){
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

	private void insertExtra(MessageItem message) {

		MessageItem sendMessage = new MessageItem();

		sendMessage.setThreadId(message.getThreadId());
		sendMessage.setDate(message.getDate());
		sendMessage.setItemId(message.getItemId());
		sendMessage.setItemImage(message.getItemImage());
		sendMessage.setItemName(message.getItemName());
		sendMessage.setListingOwnerId(message.getListingOwnerId());
		sendMessage.setListingOwnerName(message.getListingOwnerName());
		sendMessage.setMessage(message.getMessage());
		sendMessage.setUserId(message.getUserId());
		sendMessage.setUserImage(message.getUserImage());
		sendMessage.setUserName(message.getUserName());
		sendMessage.setRead(message.getRead());

		sendMessage.setIsExpired(message.getIsExpired());
		sendMessage.setAwayId(GlobalValue.myAccount.getId());
		sendMessage.setAwayImage(GlobalValue.myAccount.getImageUrl());
		sendMessage.setAwayName(GlobalValue.myAccount.getUsername());

		dbHelper.insertMessage(sendMessage, message.getAwayId());

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

}
