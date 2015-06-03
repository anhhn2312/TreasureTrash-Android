package com.pt.treasuretrash.fragment;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.TranslateAnimation;
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
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.HomeActivity;
import com.pt.treasuretrash.activity.ItemDetailsActivity;
import com.pt.treasuretrash.activity.SellerProfileActivity;
import com.pt.treasuretrash.adapter.MessageDetailAdapter;
import com.pt.treasuretrash.adapter.MessageDetailAdapter.IMessageDetail;
import com.pt.treasuretrash.adapter.MessageGroupAdapter;
import com.pt.treasuretrash.adapter.MessageGroupAdapter.IOnMessageItemClick;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity.DialogListener;
import com.pt.treasuretrash.base.TreasureTrashBaseFragment;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.modelmanager.ProductModelManager;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.MessageGroup;
import com.pt.treasuretrash.object.MessageItem;
import com.pt.treasuretrash.object.MessageThread;
import com.pt.treasuretrash.service.PubnubService;
import com.pt.treasuretrash.slidingmenu.SlidingMenu;
import com.pt.treasuretrash.utility.AndroidBug5497Workaround;
import com.pt.treasuretrash.utility.AndroidBug5497Workaround.IToggleFullScreen;
import com.pt.treasuretrash.utility.DialogUtility;
import com.pt.treasuretrash.widget.AnimatedExpandableListView;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.CustomDialog;
import com.pt.treasuretrash.widget.CustomDialog.OnCustomDialogClickListener;

public class MessageListFragment extends TreasureTrashBaseFragment implements
		OnClickListener {

	private AnimatedExpandableListView lvMessage;
	private TextView tvBack, tvEdit, tvUserName, tvItemName;
	// private SlidingLayer sllMessageThread;
	private ImageView ivBlock, ivUser;
	public AutoBgButton btnSendMessage;
	public EditText etInputMessage;
	private RelativeLayout rlBlackTransparent;
	private LinearLayout llChat;
	private boolean isClickSendMessage = false;

	private ListView lvMessageDetails;
	public List<MessageGroup> arrMessageGroup;
	private MessageGroupAdapter messageGroupAdapter = null;

	private List<MessageItem> arrMessageDetails;
	private MessageDetailAdapter messageDetailAdapter = null;
	private List<MessageItem> arrSendMessage;

	public static String messageType;

	public MessageThread currentThread;
	public MessageGroup currentGroup;
	public int currentNewMessage;
	public String currentChannel;
	private Handler handler;

	private IntentFilter intentFilter;

	private AQuery aq;

	private static boolean keyboardHidden = true;
	private static int reduceHeight = 0;

	public static int type;
	public static final int TYPE_YOUR_LISTING = 1;
	public static final int TYPE_YOUR_ENQUIRY = 2;
	public static final int TYPE_EXPIRED = 3;

	private static final int STATUS_NORMAL = 1;
	private static final int STATUS_DELETE = 2;
	private int status = STATUS_NORMAL;

	public String action;
	public String itemId;
	
	private LocalBroadcastManager localBroadcastManager;

	protected BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equalsIgnoreCase(
					GlobalValue.INTENT_ACTION_MESSAGE_HISTORY)) {
				String history = intent.getExtras().getString(
						GlobalValue.KEY_MESSAGE_HISTORY);
				onReceiveHistory(history);
			}

			if (intent.getAction().equalsIgnoreCase(
					GlobalValue.INTENT_ACTION_MESSAGE_RECEIVED)) {
				String message = intent.getExtras().getString(
						GlobalValue.KEY_MESSAGE_RECEIVED);
				onReceiveMessage(message);
			}
			
			if (intent.getAction().equalsIgnoreCase(
					GlobalValue.INTENT_ACTION_MESSAGE_SENT_SUCCESS)) {
				onSendMessageSuccess();
			}
			
			if (intent.getAction().equalsIgnoreCase(
					GlobalValue.INTENT_ACTION_MESSAGE_SENT_ERROR)) {
				onSendMessageError();
			}
			
			if (intent.getAction().equalsIgnoreCase(
					GlobalValue.INTENT_ACTION_BLOCK_UNBLOCK)) {
				final String userId = intent
						.getStringExtra(PubnubService.KEY_USER_ID);
				final String blockId = intent
						.getStringExtra(PubnubService.KEY_BLOCK_ID);
				final boolean isBlock = intent.getBooleanExtra(
						PubnubService.KEY_IS_BLOCK, true);
				onBlockUnblockNotify(userId, blockId, isBlock);
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		aq = new AQuery(self);
		handler = new Handler();
		currentThread = new MessageThread();
		currentThread.setThreadId("0");

		arrSendMessage = new ArrayList<MessageItem>();

		// initKeyboardObserver();

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		IntentFilter intent = new IntentFilter();
		intent.addAction(GlobalValue.INTENT_ACTION_MESSAGE_HISTORY);
		intent.addAction(GlobalValue.INTENT_ACTION_MESSAGE_RECEIVED);
		intent.addAction(GlobalValue.INTENT_ACTION_MESSAGE_SENT_SUCCESS);
		intent.addAction(GlobalValue.INTENT_ACTION_MESSAGE_SENT_ERROR);
		intent.addAction(GlobalValue.INTENT_ACTION_BLOCK_UNBLOCK);
		
		localBroadcastManager = LocalBroadcastManager.getInstance(self);
		localBroadcastManager.registerReceiver(receiver,
				intent);
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		localBroadcastManager.unregisterReceiver(receiver);
		localBroadcastManager = null;
	}

	@Override
	public int getFragmentResource() {
		// TODO Auto-generated method stub
		return R.layout.fragment_message_list;
	}

	@Override
	public void initView(View view) {
		llChat = (LinearLayout) view.findViewById(R.id.llChat);
		lvMessage = (AnimatedExpandableListView) view
				.findViewById(R.id.lvMessageList);
		tvBack = (TextView) view.findViewById(R.id.tvBack);
		tvEdit = (TextView) view.findViewById(R.id.tvEditMessage);
		rlBlackTransparent = (RelativeLayout) view
				.findViewById(R.id.rlBlackTransparent);

		// sllMessageThread = (SlidingLayer) view
		// .findViewById(R.id.sllMessageThread);
		// sllMessageThread.setStickTo(SlidingLayer.STICK_TO_RIGHT);
		// sllMessageThread.setCloseOnTapEnabled(false);

		ivBlock = (ImageView) view.findViewById(R.id.ivBlock);
		ivUser = (ImageView) view.findViewById(R.id.ivUser);
		tvUserName = (TextView) view.findViewById(R.id.tvUserName);
		tvItemName = (TextView) view.findViewById(R.id.tvItemName);

		lvMessageDetails = (ListView) view.findViewById(R.id.lvMessageDetail);
		btnSendMessage = (AutoBgButton) view.findViewById(R.id.btnSendMessage);
		etInputMessage = (EditText) view.findViewById(R.id.etInputMessage);
		etInputMessage.setText("");

	}

	@Override
	public void initData(View view) {
		tvBack.setText("< Your listings");
		arrMessageGroup = new ArrayList<MessageGroup>();
		messageGroupAdapter = new MessageGroupAdapter(
				(TreasureTrashBaseActivity) self, listener);
		messageGroupAdapter.setData(arrMessageGroup);
		lvMessage.setAdapter(messageGroupAdapter);

		arrMessageDetails = new ArrayList<MessageItem>();
		messageDetailAdapter = new MessageDetailAdapter(self,
				R.id.lvMessageDetail, arrMessageDetails, iMessageDetail);
		lvMessageDetails
				.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		lvMessageDetails.setAdapter(messageDetailAdapter);

		// to scroll the list view to bottom on data change
		messageDetailAdapter.registerDataSetObserver(new DataSetObserver() {
			@Override
			public void onChanged() {
				super.onChanged();
				lvMessageDetails.setSelection(messageDetailAdapter.getCount() - 1);
			}
		});
	}

	@Override
	public void initControl(View view) {
		tvBack.setOnClickListener(this);
		tvEdit.setOnClickListener(this);
		ivBlock.setOnClickListener(this);
		view.findViewById(R.id.llTransparent).setOnClickListener(this);
		view.findViewById(R.id.llMain).setOnClickListener(this);
		btnSendMessage.setOnClickListener(this);
		ivUser.setOnClickListener(this);
		tvUserName.setOnClickListener(this);

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

	@Override
	public void onShow() {
		self.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
		AndroidBug5497Workaround.assistActivity(self, iToggleFullScreen);

		isClickSendMessage = false;
		getHomeActivity().rightMenu
				.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		getHomeActivity().setBottomMenu(HomeActivity.FRAGMENT_MESSAGE);

		init();
	}

	public void init() {

		if (type == TYPE_YOUR_LISTING) {
			tvBack.setText("< Your listings");
		} else if (type == TYPE_YOUR_ENQUIRY) {
			tvBack.setText("< Your enquiries");
		} else if (type == TYPE_EXPIRED) {
			tvBack.setText("< Expired items");
		}

		arrSendMessage = new ArrayList<MessageItem>();
		status = STATUS_NORMAL;

		new InitMessagesTask().execute();
		// getMessages();
		// setStatusNormal();
		// messageGroupAdapter.notifyDataSetChanged();
	}

	// private void getMessages() {
	//
	// List<MessageGroup> listGroups = null;
	// List<MessageThread> listThreads = null;
	//
	// if (!action.equals(GlobalValue.KEY_ACTION_SHOW_MESSAGES)) {
	// listGroups = getHomeActivity().dbHelper.getAllMessageGroup(
	// GlobalValue.myAccount.getId(), type);
	// Log.d("MESSAGE_HISTORY", listGroups.size() + "");
	//
	// listThreads = getHomeActivity().dbHelper.getAllMessageThread(
	// GlobalValue.myAccount.getId(), type);
	// } else {
	// listGroups = getHomeActivity().dbHelper.getAllMessageGroupByItem(
	// GlobalValue.myAccount.getId(), type, itemId);
	// Log.d("MESSAGE_HISTORY", listGroups.size() + "");
	//
	// listThreads = getHomeActivity().dbHelper.getAllMessageThreadByItem(
	// GlobalValue.myAccount.getId(), type, itemId);
	// }
	//
	// for (int i = 0; i < listThreads.size(); i++) {
	// MessageThread thread = listThreads.get(i);
	// thread = getHomeActivity().dbHelper.getNewMessagesByThread(thread,
	// GlobalValue.myAccount.getId());
	// }
	//
	// for (int i = 0; i < listGroups.size(); i++) {
	// int newMsgCount = 0;
	// MessageGroup group = listGroups.get(i);
	// for (MessageThread thread : listThreads) {
	// if (thread.getItemId().equals(group.getItem().getId())) {
	// group.getArrThread().add(thread);
	// newMsgCount += thread.getNewMessage();
	// }
	//
	// }
	// group.setNewMessage(newMsgCount);
	// }
	//
	// arrMessageGroup.clear();
	// arrMessageGroup.addAll(listGroups);
	// setStatusNormal();
	//
	// }

	@Override
	public void onClick(View v) {
		if (v == tvBack) {
			// if (status == STATUS_DELETE) {
			// setStatusNormal();
			// } else {
			getHomeActivity().showFragment(HomeActivity.FRAGMENT_MESSAGE);
			// }
		}
		if (v == tvEdit) {
			if (status == STATUS_DELETE) {
				setStatusNormal();
			} else {
				setStatusPendingDelete();
			}
		}
		if (v.getId() == R.id.llTransparent) {
			closeLayoutChat();

		}

		if (v.getId() == R.id.llMain) {
			if (status == STATUS_DELETE) {
				setStatusNormal();
			}
		}

		if (v == ivBlock) {
			rlBlackTransparent.setVisibility(View.VISIBLE);
			new CustomDialog(self,
					getString(R.string.message_alert_block_user), "",
					getString(R.string.text_btn_block),
					getString(R.string.text_btn_dont_block),
					new OnCustomDialogClickListener() {

						@Override
						public void onYes() {

							blockUser();
						}

						@Override
						public void onNo() {
							rlBlackTransparent.setVisibility(View.GONE);
						}

						@Override
						public void onNeutral() {
							// TODO Auto-generated method stub

						}
					}).show();
		}

		if (v == btnSendMessage) {
			checkSendMessage();
			return;
		}

		if (v == ivUser) {
			Item item = currentGroup.getItem();

			// List<Item> list = new ArrayList<Item>();
			// list.add(item);
			//
			// GlobalValue.arrItems = list;
			// GlobalValue.currentItemPosition = 0;
			//
			// Bundle bundle = new Bundle();
			// bundle.putString(GlobalValue.KEY_ACTION_ITEM_DETAIL,
			// GlobalValue.KEY_ACTION_ITEM_DETAIL_SINGLE_ITEM);
			//
			// gotoActivity(self, ItemDetailsActivity.class, bundle);
			checkItemExist(item);
		}
		if (v == tvUserName) {

			Bundle b = new Bundle();
			b.putString(GlobalValue.KEY_SELLER_ID, currentThread.getAwayId());
			gotoActivity(self, SellerProfileActivity.class, b);
		}

	}

	private void checkSendMessage() {
		if (!getHomeActivity().dbHelper
				.checkBlockedUser(currentThread.getAwayId(),
						GlobalValue.myAccount.getId(), false)) {
			if (TextUtils.isEmpty(etInputMessage.getText())) {
				getHomeActivity().showMessageDialog(
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
			getHomeActivity().showMessageDialog(
					getString(R.string.message_alert_being_block_user) + " "
							+ currentThread.getAwayName(),
					new DialogListener() {

						@Override
						public void onClose(Dialog dialog) {
							closeLayoutChat();

						}
					});
		}
	}

	private void blockUser() {

		AccountModelManager.blockUser(self, currentThread.getAwayId(), true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {

						// List<MessageThread> list = getHomeActivity().dbHelper
						// .getAllThreadByUserId(
						// currentThread.getAwayId(),
						// GlobalValue.myAccount.getId());
						// getHomeActivity().dbHelper
						// .deleteMessageByListThread(list);
						// getHomeActivity().dbHelper.insertBlockedUser(
						// currentThread.getAwayId(),
						// GlobalValue.myAccount.getId(), true);
						// closeLayoutChat();
						// HomeActivity.isRefreshBlockedUser = true;
						onBlockChattingUser(currentThread.getAwayId());
						getHomeActivity()
								.showMessageDialog(
										getString(R.string.message_alert_block_success),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												rlBlackTransparent
														.setVisibility(View.GONE);
												init();
											}
										});
						getHomeActivity().mPubnubService
								.sendBlockUnblockMessage(
										GlobalValue.myAccount.getId(),
										GlobalValue.myAccount.getUsername(),
										currentThread.getAwayId(), true);

					}

					@Override
					public void onError(int statusCode, String json) {
						getHomeActivity()
								.showMessageDialog(
										getString(R.string.message_alert_general_error),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												// TODO Auto-generated method
												// stub
											}
										});

					}

					@Override
					public void onError() {
						getHomeActivity()
								.showMessageDialog(
										getString(R.string.message_alert_general_error),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												// TODO Auto-generated method
												// stub

											}
										});

					}
				});
	}

	private void setStatusPendingDelete() {
		status = STATUS_DELETE;
		// tvBack.setVisibility(View.INVISIBLE);
		tvEdit.setText(self.getString(R.string.done));
		for (int i = 0; i < arrMessageGroup.size(); i++) {
			MessageGroup group = arrMessageGroup.get(i);
			group.setStatus(MessageGroup.STATUS_DELETE_PENDING);
			for (int j = 0; j < group.getArrThread().size(); j++) {
				MessageThread thread = group.getArrThread().get(j);
				thread.setStatus(MessageThread.STATUS_DELETE_PENDING);
			}
		}
		messageGroupAdapter.notifyDataSetChanged();
	}

	private void setStatusNormal() {
		status = STATUS_NORMAL;
		tvBack.setVisibility(View.VISIBLE);
		tvEdit.setText(self.getString(R.string.edit));
		for (int i = 0; i < arrMessageGroup.size(); i++) {
			MessageGroup group = arrMessageGroup.get(i);
			group.setStatus(MessageGroup.STATUS_NORMAL);
			for (int j = 0; j < group.getArrThread().size(); j++) {
				MessageThread thread = group.getArrThread().get(j);
				thread.setStatus(MessageThread.STATUS_NORMAL);
			}
		}
		messageGroupAdapter.notifyDataSetChanged();
		
		if(arrMessageGroup.size() == 0){
			tvEdit.setVisibility(View.INVISIBLE);
		}else{
			tvEdit.setVisibility(View.VISIBLE);
		}
	}

	private void sendMessage() {

		MessageItem sendMessage = new MessageItem();

		Calendar cal = Calendar.getInstance();

		sendMessage.setThreadId(currentThread.getThreadId());
		sendMessage.setDate(cal.getTimeInMillis() / 1000);
		sendMessage.setItemId(currentGroup.getItem().getId());
		sendMessage.setItemImage(currentGroup.getItem().getImage());
		sendMessage.setItemName(currentGroup.getItem().getTitle());
		sendMessage.setListingOwnerId(currentGroup.getItem().getUserID());
		sendMessage.setListingOwnerName(currentGroup.getItem().getUserName());
		sendMessage.setMessage(etInputMessage.getText().toString().trim());
		sendMessage.setRead(1);
		sendMessage.setUserId(GlobalValue.myAccount.getId());
		sendMessage.setUserImage(GlobalValue.myAccount.getImageUrl());
		sendMessage.setUserName(GlobalValue.myAccount.getName());
		sendMessage.setIsExpired(currentGroup.getIsExpired());
		sendMessage.setAwayId(currentThread.getAwayId());
		sendMessage.setAwayImage(currentThread.getAwayImage());
		sendMessage.setAwayName(currentThread.getAwayName());

		arrSendMessage.add(sendMessage);

		String messageToSent = prepareMessage(sendMessage);

		getHomeActivity().mPubnubService._publish(currentThread.getAwayId(),
				messageToSent, true);
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

	private void onReceiveHistory(String messages) {
	}

	private void onReceiveMessage(String message) {
		if (getHomeActivity().currentFragment == HomeActivity.FRAGMENT_MESSAGE_LIST) {
			if (isLayoutChatOpen()) {
				MessageItem item = ParserUtility.parseReceivedMessage(message,
						GlobalValue.myAccount.getId());
				if (item != null
						&& item.getThreadId().equals(
								currentThread.getThreadId())) {
					arrMessageDetails.add(item);
					messageDetailAdapter.notifyDataSetChanged();
					getHomeActivity().dbHelper.updateReadMessageForItem(
							item.getThreadId(), item.getUserId(),
							item.getDate(), GlobalValue.myAccount.getId());
					getHomeActivity().adjustTotalNewMessages(-1);
				} else {
					init();
				}
			} else {
				init();
			}
		}
	}

	private IOnMessageItemClick listener = new IOnMessageItemClick() {

		@Override
		public void onClickMessageThread(MessageGroup group,
				MessageThread messageThread, int newMessage) {
			if (status == STATUS_DELETE) {
				setStatusNormal();
			} else {
				currentGroup = group;
				currentThread = messageThread;
				currentNewMessage = newMessage;
				openLayoutChat();

			}

		}

		@Override
		public void onClickDeleteGroup(int groupPosition) {
			MessageGroup group = arrMessageGroup.get(groupPosition);
			arrMessageGroup.remove(groupPosition);
			messageGroupAdapter.notifyDataSetChanged();
			getHomeActivity().dbHelper.deleteMessageByGroup(group.getItem()
					.getId());
			getHomeActivity().getTotalNewMessages();
		}

		@Override
		public void onClickDeleteThread(int groupPosition, int threadPosition) {
			MessageGroup group = arrMessageGroup.get(groupPosition);
			MessageThread thread = group.getArrThread().get(threadPosition);
			getHomeActivity().dbHelper.deleteMessageByThread(thread
					.getThreadId());
			group.getArrThread().remove(threadPosition);
			if (group.getArrThread().size() == 0) {
				arrMessageGroup.remove(groupPosition);
				getHomeActivity().dbHelper.deleteMessageByGroup(group.getItem()
						.getId());
			}
			messageGroupAdapter.notifyDataSetChanged();
			getHomeActivity().getTotalNewMessages();

		}

		@Override
		public void onClickItemImage(int groupPosition) {
			MessageGroup group = arrMessageGroup.get(groupPosition);

			Item item = group.getItem();

			// List<Item> list = new ArrayList<Item>();
			// list.add(item);
			//
			// GlobalValue.arrItems = list;
			// GlobalValue.currentItemPosition = 0;
			//
			// Bundle bundle = new Bundle();
			// bundle.putString(GlobalValue.KEY_ACTION_ITEM_DETAIL,
			// GlobalValue.KEY_ACTION_ITEM_DETAIL_SINGLE_ITEM);
			//
			// gotoActivity(self, ItemDetailsActivity.class, bundle);

			checkItemExist(item);

		}
	};

	private void fillMessageDetails() {
		arrMessageDetails.clear();

		currentThread.setNewMessage(0);

		imageLoader.displayImage(currentGroup.getItem().getImage(), ivUser,
				options);
		tvUserName.setText(currentThread.getAwayName());
		tvItemName.setText(currentGroup.getItem().getTitle());

		Log.d("CURRENT_THREAD_NEW", currentThread.getNewMessage() + "");
		if (currentNewMessage > 0) {
			getHomeActivity().adjustTotalNewMessages(-currentNewMessage);

			currentThread.setNewMessage(0);
			currentGroup.setNewMessage(calculateGroupNewMessage(currentGroup));
			messageGroupAdapter.notifyDataSetChanged();
		}

		new GetMessageHistoryTask().execute();

	}

	private int calculateGroupNewMessage(MessageGroup group) {
		int newMsgCount = 0;
		List<MessageThread> listThreads = group.getArrThread();
		for (MessageThread thread : listThreads) {

			newMsgCount += thread.getNewMessage();

		}
		return newMsgCount;
	}

	private void onSendMessageSuccess() {
		arrMessageDetails.add(arrSendMessage.get(0));
		messageDetailAdapter.notifyDataSetChanged();
		// getHomeActivity().dbHelper.insertMessage(arrSendMessage.get(0),
		// GlobalValue.myAccount.getId());
		// arrSendMessage.remove(0);
		etInputMessage.setText("");
		getHomeActivity().showSoftKeyboard(etInputMessage);
		new InsertMessageTask().execute(new MessageItem[] { arrSendMessage
				.get(0) });
		getHomeActivity().showSoftKeyboard(etInputMessage);
		isClickSendMessage = false;
	}

	private void onSendMessageError() {
		getHomeActivity().showMessageDialog(
				"There is an error sending your message. Please try again!",
				new DialogListener() {

					@Override
					public void onClose(Dialog dialog) {
						getHomeActivity().showSoftKeyboard(etInputMessage);

					}
				});
		arrSendMessage.remove(0);
		isClickSendMessage = false;
	}

	private void getAllHistoryByThread() {
		List<MessageItem> list = getHomeActivity().dbHelper
				.getHistoryMessageByThread(currentThread.getThreadId(),
						GlobalValue.myAccount.getId());
		arrMessageDetails.addAll(list);
		messageDetailAdapter.notifyDataSetChanged();
		getHomeActivity().dbHelper.updateReadMessageForList(list);
	}

	private class InitMessagesTask extends
			AsyncTask<Void, Void, List<MessageGroup>> {

		List<MessageGroup> listGroups = null;
		List<MessageThread> listThreads = null;

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			// DialogUtility.showProgressDialog(self);

		}

		@Override
		protected List<MessageGroup> doInBackground(Void... params) {

			if (!action.equals(GlobalValue.KEY_ACTION_SHOW_MESSAGES)) {
				listGroups = getHomeActivity().dbHelper.getAllMessageGroup(
						GlobalValue.myAccount.getId(), type);
				Log.d("MESSAGE_HISTORY", listGroups.size() + "");

				listThreads = getHomeActivity().dbHelper.getAllMessageThread(
						GlobalValue.myAccount.getId(), type);
			} else {
				listGroups = getHomeActivity().dbHelper
						.getAllMessageGroupByItem(
								GlobalValue.myAccount.getId(), type, itemId);
				Log.d("MESSAGE_HISTORY", listGroups.size() + "");

				listThreads = getHomeActivity().dbHelper
						.getAllMessageThreadByItem(
								GlobalValue.myAccount.getId(), type, itemId);
			}

			for (int i = 0; i < listThreads.size(); i++) {
				MessageThread thread = listThreads.get(i);
				thread = getHomeActivity().dbHelper.getNewMessagesByThread(
						thread, GlobalValue.myAccount.getId());
			}

			for (int i = 0; i < listGroups.size(); i++) {
				int newMsgCount = 0;
				MessageGroup group = listGroups.get(i);
				for (MessageThread thread : listThreads) {
					if (thread.getItemId().equals(group.getItem().getId())) {
						group.getArrThread().add(thread);
						newMsgCount += thread.getNewMessage();
					}

				}
				group.setNewMessage(newMsgCount);
			}
			return listGroups;
		}

		@Override
		protected void onPostExecute(List<MessageGroup> listGroups) {
			// DialogUtility.closeProgressDialog();
			arrMessageGroup.clear();
			arrMessageGroup.addAll(listGroups);
			setStatusNormal();
		}
	}

	private class GetMessageHistoryTask extends
			AsyncTask<Void, Void, List<MessageItem>> {

		protected void onPreExecute() {
			DialogUtility.showProgressDialog(self);
		}

		@Override
		protected List<MessageItem> doInBackground(Void... params) {
			List<MessageItem> list = getHomeActivity().dbHelper
					.getHistoryMessageByThread(currentThread.getThreadId(),
							GlobalValue.myAccount.getId());

			getHomeActivity().dbHelper.updateReadMessageForList(list);

			return list;
		}

		protected void onPostExecute(List<MessageItem> list) {
			DialogUtility.closeProgressDialog();
			arrMessageDetails.clear();
			arrMessageDetails.addAll(list);
			messageDetailAdapter.notifyDataSetChanged();
		}

	}

	private class InsertMessageTask extends AsyncTask<MessageItem, Void, Void> {

		@Override
		protected Void doInBackground(MessageItem... params) {
			MessageItem item = params[0];
			getHomeActivity().dbHelper.insertMessage(item,
					GlobalValue.myAccount.getId());
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			arrSendMessage.remove(0);

		}

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		LocalBroadcastManager.getInstance(self).unregisterReceiver(receiver);
	}

	@Override
	public void onHide() {
		// TODO Auto-generated method stub
		super.onHide();
		action = "";
		closeLayoutChat();
		setStatusNormal();

		getHomeActivity().rightMenu
				.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		self.getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
	}

	private void initKeyboardObserver() {
		final View decorView = self.getWindow().getDecorView();
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

								reduceHeight = height
										- displayHeight
										- (int) self
												.getResources()
												.getDimension(
														R.dimen.layout_bottom_height);

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

	private void closeLayoutChat() {
		// sllMessageThread.closeLayer(true);
		llChat.setVisibility(View.GONE);

		ivUser.setBackgroundColor(getResources().getColor(R.color.transparent));
		ivUser.setImageDrawable(null);
		arrMessageDetails.clear();
		messageDetailAdapter.notifyDataSetChanged();
		etInputMessage.setText("");
		
		getHomeActivity().hideSoftKeyboard(etInputMessage);
		getHomeActivity().showBottomLayout(true);

	}

	private void openLayoutChat() {
		// sllMessageThread.openLayer(true);
		TranslateAnimation anim = new TranslateAnimation(
				getHomeActivity().screenWidth + 10, 0f, 0f, 0f); // might need
																	// to review
																	// the docs
		anim.setDuration(500); // set how long you want the animation

		llChat.setAnimation(anim);
		llChat.setVisibility(View.VISIBLE);
		handler.postDelayed(new Runnable() {

			@Override
			public void run() {
				fillMessageDetails();
			}
		}, 1000);
		
		getHomeActivity().showBottomLayout(false);

	}

	private boolean isLayoutChatOpen() {
		// return sllMessageThread.isOpened();
		if (llChat.getVisibility() == View.VISIBLE)
			return true;
		else
			return false;
	}

	IMessageDetail iMessageDetail = new IMessageDetail() {

		@Override
		public void onClickUserImage(int position) {
			MessageItem item = arrMessageDetails.get(position);
			Bundle b = new Bundle();
			b.putString(GlobalValue.KEY_SELLER_ID, item.getUserId());
			gotoActivity(self, SellerProfileActivity.class, b);

		}
	};

	private void checkItemExist(final Item item) {
		ProductModelManager
				.getItemDetails(
						self,
						item.getId(),
						getHomeActivity().pref
								.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
						getHomeActivity().pref
								.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG),
						getHomeActivity().screenWidth,
						getHomeActivity().screenWidth, true,
						new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								DialogUtility.showProgressDialog(self);
								aq.id(ivBlock).image(item.getImage(), false,
										true, 0, 0, new BitmapAjaxCallback() {
											@Override
											protected void callback(String url,
													ImageView iv, Bitmap bm,
													AjaxStatus status) {
												if (bm != null) {
													DialogUtility
															.closeProgressDialog();
													ArrayList<Item> list = new ArrayList<Item>();
													list.add(item);

//													GlobalValue.arrItems = list;
//													GlobalValue.currentItemPosition = 0;

													Bundle bundle = new Bundle();
													bundle.putString(
															GlobalValue.KEY_ACTION_ITEM_DETAIL,
															GlobalValue.KEY_ACTION_ITEM_DETAIL_SINGLE_ITEM);
													
													bundle.putParcelableArrayList("arrItem", list);
													bundle.putInt("currentPosition", 0);

													gotoActivity(
															self,
															ItemDetailsActivity.class,
															bundle);

												}
											}
										});
							}

							@Override
							public void onError(int statusCode, String json) {
								getHomeActivity()
										.showMessageDialog(
												getString(R.string.message_alert_listing_removed),
												new DialogListener() {

													@Override
													public void onClose(
															Dialog dialog) {
														// TODO Auto-generated
														// method
														// stub

													}
												});
							}

							@Override
							public void onError() {
								getHomeActivity()
										.showMessageDialog(
												getString(R.string.message_alert_invalid_item),
												new DialogListener() {

													@Override
													public void onClose(
															Dialog dialog) {
														// TODO Auto-generated
														// method
														// stub

													}
												});
							}
						});
	}

	private void onBlockChattingUser(String blockId) {
		List<MessageThread> list = getHomeActivity().dbHelper
				.getAllThreadByUserId(blockId, GlobalValue.myAccount.getId());
		getHomeActivity().dbHelper.deleteMessageByListThread(list);
		getHomeActivity().dbHelper.insertBlockedUser(blockId,
				GlobalValue.myAccount.getId(), true);
		HomeActivity.isRefreshBlockedUser = true;
		if (blockId.equalsIgnoreCase(currentThread.getAwayId())) {
			closeLayoutChat();
		}
		init();

	}

	private void onBeingBlockedByChattingUser(String userId) {

	}

	private void onBlockUnblockNotify(String userId, String blockId,
			boolean isBlock) {

		if (userId.equalsIgnoreCase(GlobalValue.myAccount.getId())) {
			if (isBlock) {
				onBlockChattingUser(blockId);
			} else {

			}

		} else {
			if (isBlock) {
				if (userId.equalsIgnoreCase(currentThread.getAwayId())
						&& llChat.getVisibility() == View.VISIBLE) {
					closeLayoutChat();
					getHomeActivity().showMessageDialog(
							getString(R.string.message_alert_being_block_user)
									+ " " + currentThread.getAwayName(),
							new DialogListener() {

								@Override
								public void onClose(Dialog dialog) {
									closeLayoutChat();

								}
							});
				}
			}
		}

	}
	
	IToggleFullScreen iToggleFullScreen = new IToggleFullScreen() {
		
		@Override
		public void onNotFullscreen() {
//			getHomeActivity().showBottomLayout(false);
			
		}

		@Override
		public void onFullscreen() {
//			getHomeActivity().showBottomLayout(true);			
		}
	};
}
