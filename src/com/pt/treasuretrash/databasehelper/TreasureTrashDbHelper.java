package com.pt.treasuretrash.databasehelper;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pt.treasuretrash.fragment.MessageListFragment;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.MessageGroup;
import com.pt.treasuretrash.object.MessageItem;
import com.pt.treasuretrash.object.MessageThread;

public class TreasureTrashDbHelper extends SQLiteOpenHelper {

	private String TAG = this.getClass().getSimpleName();

	private static final String DATABASE_NAME = "com.pt.treasuretrash.db";
	private static final int SCHEMA_VERSION = 3;
	public static final String TABLE_BLOCKED_USER = "blocked_user";
	public static final String TABLE_CATEGORY_PREF = "category_pref";
	public static final String TABLE_MESSAGE = "message";

	public static final String BLOCKED_COLUMN_ID = "_id";
	public static final String BLOCKED_COLUMN_BLOCKED_ID = "blocked_id";
	public static final String BLOCKED_COLUMN_ACCOUNT_ID = "account_id";
	public static final String BLOCKED_COLUMN_IS_BLOCKED_BY_ME = "is_block_by_me";

	public static final String CATEGORY_PREF_COLUMN_ID = "_id";
	public static final String CATEGORY_PREF_COLUMN_DISABLED = "disabled_category";
	public static final String CATEGORY_PREF_COLUMN_ACCOUNT_ID = "account_id";

	public static final String MESSAGE_COLUMN_ID = "_id";
	public static final String MESSAGE_COLUMN_THREAD_ID = "thread_id";
	public static final String MESSAGE_COLUMN_AVATAR_URL = "avatar_url";
	public static final String MESSAGE_COLUMN_CONTENT = "content";
	public static final String MESSAGE_COLUMN_DATE = "date";
	public static final String MESSAGE_COLUMN_DISPLAY_NAME = "display_name";
	public static final String MESSAGE_COLUMN_IS_EXPIRED = "is_expired";
	public static final String MESSAGE_COLUMN_LISTING_ID = "listing_id";
	public static final String MESSAGE_COLUMN_LISTING_NAME = "listing_name";
	public static final String MESSAGE_COLUMN_LISTING_OWNER_NAME = "listing_owner_name";
	public static final String MESSAGE_COLUMN_LISTING_URL = "listing_url";
	public static final String MESSAGE_COLUMN_LISTING_OWNER_ID = "listing_owner_id";
	public static final String MESSAGE_COLUMN_USER_ID = "user_id";
	public static final String MESSAGE_COLUMN_READ = "read";
	public static final String MESSAGE_COLUMN_AWAY_ID = "away_id";
	public static final String MESSAGE_COLUMN_AWAY_IMAGE = "away_image";
	public static final String MESSAGE_COLUMN_AWAY_NAME = "away_name";
	public static final String MESSAGE_COLUMN_ACCOUNT_ID = "account_id";

	public TreasureTrashDbHelper(Context context) {
		super(context, DATABASE_NAME, null, SCHEMA_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + TABLE_BLOCKED_USER + "("
				+ BLOCKED_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ BLOCKED_COLUMN_BLOCKED_ID + " TEXT,"
				+ BLOCKED_COLUMN_IS_BLOCKED_BY_ME + " INTEGER,"
				+ BLOCKED_COLUMN_ACCOUNT_ID + " TEXT" + ");");

		db.execSQL("CREATE TABLE " + TABLE_CATEGORY_PREF + "("
				+ CATEGORY_PREF_COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ CATEGORY_PREF_COLUMN_DISABLED + " TEXT,"
				+ CATEGORY_PREF_COLUMN_ACCOUNT_ID + " TEXT" + ");");

		db.execSQL("CREATE TABLE " + TABLE_MESSAGE + "(" + MESSAGE_COLUMN_ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ MESSAGE_COLUMN_THREAD_ID + " TEXT,"
				+ MESSAGE_COLUMN_AVATAR_URL + " TEXT," + MESSAGE_COLUMN_CONTENT
				+ " TEXT," + MESSAGE_COLUMN_DATE + " INTEGER,"
				+ MESSAGE_COLUMN_DISPLAY_NAME + " TEXT,"
				+ MESSAGE_COLUMN_IS_EXPIRED + " INTEGER,"
				+ MESSAGE_COLUMN_LISTING_ID + " TEXT,"
				+ MESSAGE_COLUMN_LISTING_NAME + " TEXT,"
				+ MESSAGE_COLUMN_LISTING_OWNER_NAME + " TEXT,"
				+ MESSAGE_COLUMN_LISTING_URL + " INTEGER,"
				+ MESSAGE_COLUMN_LISTING_OWNER_ID + " TEXT,"
				+ MESSAGE_COLUMN_USER_ID + " TEXT," + MESSAGE_COLUMN_READ
				+ " INTEGER," + MESSAGE_COLUMN_AWAY_ID + " TEXT,"
				+ MESSAGE_COLUMN_AWAY_IMAGE + " TEXT,"
				+ MESSAGE_COLUMN_AWAY_NAME + " TEXT,"
				+ MESSAGE_COLUMN_ACCOUNT_ID + " TEXT" + ");");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub

	}

	public void deleteById(String tableName, int id) {
		getReadableDatabase().execSQL(
				"delete from " + tableName + " where _id = '" + id + "'");
		Log.d(TAG, "deleted id=" + id);
	}

	public void updateReadMessageForItem(String threadId, String userId,
			long date, String accountId) {
		String query = "UPDATE " + TABLE_MESSAGE + " SET "
				+ MESSAGE_COLUMN_READ + " = '" + 1 + "'" + " WHERE "
				+ MESSAGE_COLUMN_THREAD_ID + " = '" + threadId + "'" + " AND "
				+ MESSAGE_COLUMN_USER_ID + " = '" + userId + "'" + " AND "
				+ MESSAGE_COLUMN_DATE + " = '" + date + "' AND "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		getReadableDatabase().execSQL(query);
	}

	public void updateReadMessageForList(List<MessageItem> listMessage) {

		List<MessageItem> list = new ArrayList<MessageItem>();
		for (int i = 0; i < listMessage.size(); i++) {
			if (listMessage.get(i).getRead() == 0) {
				list.add(listMessage.get(i));
			}
		}
		if (list.size() > 0) {
			String ids = "(";
			for (int i = 0; i < list.size(); i++) {
				ids += "'" + list.get(i).getId() + "'";
				if (i != list.size() - 1) {
					ids += ",";
				}
			}
			ids += ")";

			String query = "UPDATE " + TABLE_MESSAGE + " SET "
					+ MESSAGE_COLUMN_READ + " = '" + 1 + "'" + " WHERE "
					+ MESSAGE_COLUMN_ID + " IN " + ids;
			getReadableDatabase().execSQL(query);
		}
	}

	public List<MessageGroup> getAllMessageGroup(String accountId, int type) {
		String query = "";
		if (type == MessageListFragment.TYPE_YOUR_LISTING) {
			query = "Select * from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "' and "
					+ MESSAGE_COLUMN_LISTING_OWNER_ID + " = '" + accountId
					+ "' and " + MESSAGE_COLUMN_IS_EXPIRED + " = '0'"
					+ " group by " + MESSAGE_COLUMN_LISTING_ID + " order by " + MESSAGE_COLUMN_DATE + " desc"  ;
		} else if (type == MessageListFragment.TYPE_YOUR_ENQUIRY) {
			query = "Select * from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_LISTING_OWNER_ID + " <> '" + accountId
					+ "' and " + MESSAGE_COLUMN_IS_EXPIRED + " = '0' and "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'"
					+ " group by " + MESSAGE_COLUMN_LISTING_ID + " order by " + MESSAGE_COLUMN_DATE + " desc";
		} else if (type == MessageListFragment.TYPE_EXPIRED) {
			query = "Select * from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_IS_EXPIRED + " = '1' and "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'"
					+ " group by " + MESSAGE_COLUMN_LISTING_ID + " order by " + MESSAGE_COLUMN_DATE + " desc";
		}

		Cursor c = null;
		c = getReadableDatabase().rawQuery(query, null);
		List<MessageGroup> list = parseListMessageGroupFromCursor(c);
		c.close();
		return list;
	}

	public List<String> getAllChattedItems(String accountId) {
		List<String> list = new ArrayList<String>();
		String query = "Select * from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'"
				+ " group by " + MESSAGE_COLUMN_LISTING_ID;

		Cursor c = getReadableDatabase().rawQuery(query, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {

			String itemId = c.getString(7);
			list.add(itemId);
			c.moveToNext();
		}
		c.close();
		return list;

	}

	public boolean checkExpiredStatusChangeForItem(Item item, String accountId) {
		String query = "Select * from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_LISTING_ID + " = '" + item.getId() + "' and "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "' limit 1";
		Cursor c = getReadableDatabase().rawQuery(query, null);
		int expired = 0;

		c.moveToFirst();
		while (!c.isAfterLast()) {
			expired = c.getInt(6);
			c.moveToNext();
		}
		boolean isExpired = expired == 0 ? false : true;

		if (isExpired == item.isExpired()) {
			return false;
		} else {
			return true;
		}
	}

	public void updateExpiredStatusForItems(List<Item> items, String accountId) {

		for (Item item : items) {
			int expired = item.isExpired() ? 1 : 0;
			if (checkExpiredStatusChangeForItem(item, accountId)) {
				String query = "UPDATE " + TABLE_MESSAGE + " SET "
						+ MESSAGE_COLUMN_IS_EXPIRED + " = '" + expired + "'"
						+ " WHERE " + MESSAGE_COLUMN_LISTING_ID + " = '"
						+ item.getId() + "'" + " AND "
						+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
				getReadableDatabase().execSQL(query);
			}
		}

	}

	public List<MessageThread> getAllMessageThread(String accountId, int type) {
		String query = "";
		if (type == MessageListFragment.TYPE_YOUR_LISTING) {
			query = "Select * from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "' and "
					+ MESSAGE_COLUMN_LISTING_OWNER_ID + " = '" + accountId
					+ "' and " + MESSAGE_COLUMN_IS_EXPIRED + " = '0'"
					+ " group by " + MESSAGE_COLUMN_THREAD_ID + " order by " + MESSAGE_COLUMN_DATE + " desc";
		} else if (type == MessageListFragment.TYPE_YOUR_ENQUIRY) {
			query = "Select * from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_LISTING_OWNER_ID + " <> '" + accountId
					+ "' and " + MESSAGE_COLUMN_IS_EXPIRED + " = '0' and "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'"
					+ " group by " + MESSAGE_COLUMN_THREAD_ID + " order by " + MESSAGE_COLUMN_DATE + " desc";
		} else if (type == MessageListFragment.TYPE_EXPIRED) {
			query = "Select * from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_IS_EXPIRED + " = '1' and "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'"
					+ " group by " + MESSAGE_COLUMN_THREAD_ID + " order by " + MESSAGE_COLUMN_DATE + " desc";
		}

		Cursor c = null;
		c = getReadableDatabase().rawQuery(query, null);
		List<MessageThread> list = parseListMessageThreadFromCursor(c);
		c.close();
		return list;
	}

	public List<MessageGroup> getAllMessageGroupByItem(String accountId,
			int type, String itemId) {
		String query = "";
		if (type == MessageListFragment.TYPE_YOUR_LISTING) {
			query = "Select * from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "' and "
					+ MESSAGE_COLUMN_LISTING_OWNER_ID + " = '" + accountId
					+ "' and " + MESSAGE_COLUMN_LISTING_ID + " = '" + itemId
					+ "' and " + MESSAGE_COLUMN_IS_EXPIRED + " = '0'"
					+ " group by " + MESSAGE_COLUMN_LISTING_ID;
		} else if (type == MessageListFragment.TYPE_YOUR_ENQUIRY) {
			query = "Select * from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_LISTING_OWNER_ID + " <> '" + accountId
					+ "' and " + MESSAGE_COLUMN_LISTING_ID + " = '" + itemId
					+ "' and " + MESSAGE_COLUMN_IS_EXPIRED + " = '0' and "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'"
					+ " group by " + MESSAGE_COLUMN_LISTING_ID;
		} else if (type == MessageListFragment.TYPE_EXPIRED) {
			query = "Select _id from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_IS_EXPIRED + " = '1' and "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'"
					+ "' and " + MESSAGE_COLUMN_LISTING_ID + " = '" + itemId
					+ " group by " + MESSAGE_COLUMN_LISTING_ID;
		}

		Cursor c = null;
		c = getReadableDatabase().rawQuery(query, null);
		List<MessageGroup> list = parseListMessageGroupFromCursor(c);
		c.close();
		return list;
	}

	public List<MessageThread> getAllMessageThreadByItem(String accountId,
			int type, String itemId) {
		String query = "";
		if (type == MessageListFragment.TYPE_YOUR_LISTING) {
			query = "Select * from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "' and "
					+ MESSAGE_COLUMN_LISTING_OWNER_ID + " = '" + accountId
					+ "' and " + MESSAGE_COLUMN_LISTING_ID + " = '" + itemId
					+ "' and " + MESSAGE_COLUMN_IS_EXPIRED + " = '0'"
					+ " group by " + MESSAGE_COLUMN_THREAD_ID;
		} else if (type == MessageListFragment.TYPE_YOUR_ENQUIRY) {
			query = "Select * from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_LISTING_OWNER_ID + " <> '" + accountId
					+ "' and " + MESSAGE_COLUMN_LISTING_ID + " = '" + itemId
					+ "' and " + MESSAGE_COLUMN_IS_EXPIRED + " = '0' and "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'"
					+ " group by " + MESSAGE_COLUMN_THREAD_ID;
		} else if (type == MessageListFragment.TYPE_EXPIRED) {
			query = "Select _id from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_IS_EXPIRED + " = '1' and "
					+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'"
					+ "' and " + MESSAGE_COLUMN_LISTING_ID + " = '" + itemId
					+ " group by " + MESSAGE_COLUMN_THREAD_ID;
		}

		Cursor c = null;
		c = getReadableDatabase().rawQuery(query, null);
		List<MessageThread> list = parseListMessageThreadFromCursor(c);
		c.close();
		return list;
	}

	public int getMessageNumberByItem(String itemId) {
		String query = "Select * from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_LISTING_ID + " = '" + itemId + "'";

		Cursor c = getReadableDatabase().rawQuery(query, null);
		return c.getCount();
	}

	public List<MessageThread> getAllThreadByUserId(String userId,
			String accountId) {
		String query = "Select * from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "' and "
				+ MESSAGE_COLUMN_AWAY_ID + " = '" + userId + "' group by "
				+ MESSAGE_COLUMN_THREAD_ID;

		Cursor c = null;
		c = getReadableDatabase().rawQuery(query, null);
		List<MessageThread> list = parseListMessageThreadFromCursor(c);
		c.close();
		return list;
	}

	public void deleteMessageByListThread(List<MessageThread> arrThread) {
		List<String> list = new ArrayList<String>();
		for (int i = 0; i < arrThread.size(); i++) {
			list.add(arrThread.get(i).getThreadId());
		}
		if (list.size() > 0) {
			String ids = "(";
			for (int i = 0; i < list.size(); i++) {
				ids += "'" + list.get(i) + "'";
				if (i != list.size() - 1) {
					ids += ",";
				}
			}
			ids += ")";

			String query = "delete from " + TABLE_MESSAGE + " where "
					+ MESSAGE_COLUMN_THREAD_ID + " in " + ids;
			getReadableDatabase().execSQL(query);
		}
	}

	public void deleteMessageByThread(String threadId) {
		getReadableDatabase().execSQL(
				"delete from " + TABLE_MESSAGE + " where "
						+ MESSAGE_COLUMN_THREAD_ID + " = '" + threadId + "'");
		// Log.d(TAG, "deleted id=" + id);
	}

	public void deleteMessageByGroup(String itemId) {
		getReadableDatabase().execSQL(
				"delete from " + TABLE_MESSAGE + " where "
						+ MESSAGE_COLUMN_LISTING_ID + " = '" + itemId + "'");
	}

	public void insertBlockedUser(String blockedId, String accountId, boolean isBlockedByMe) {
		if (!checkBlockedUser(blockedId, accountId, isBlockedByMe)) {
			try {
				ContentValues cv = new ContentValues();

				cv.put(BLOCKED_COLUMN_BLOCKED_ID, blockedId);
				cv.put(BLOCKED_COLUMN_ACCOUNT_ID, accountId);
				cv.put(BLOCKED_COLUMN_IS_BLOCKED_BY_ME, isBlockedByMe?1:0);

				getWritableDatabase().insert(TABLE_BLOCKED_USER,
						BLOCKED_COLUMN_ID, cv);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void deleteBlockedUser(String blockedId, String accountId, boolean isBlockedByMe) {
		if (checkBlockedUser(blockedId, accountId, isBlockedByMe)) {
			String query = "delete from " + TABLE_BLOCKED_USER + " where "
					+ BLOCKED_COLUMN_BLOCKED_ID + " = '" + blockedId + "' and "
					+ BLOCKED_COLUMN_ACCOUNT_ID + " = '" + accountId + "' and "
					+ BLOCKED_COLUMN_IS_BLOCKED_BY_ME + " = '" + (isBlockedByMe?1:0) + "'";

			getReadableDatabase().execSQL(query);
		}
	}
	
	public void deleteAllBlockerAndBlockedUsers(String accountId){
		String query = "delete from " + TABLE_BLOCKED_USER + " where "
				+ BLOCKED_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		getReadableDatabase().execSQL(query);
	}

	public void insertMessage(MessageItem item, String accountId) {
		try {
			ContentValues cv = new ContentValues();

			cv.put(MESSAGE_COLUMN_THREAD_ID, item.getThreadId());
			cv.put(MESSAGE_COLUMN_AVATAR_URL, item.getUserImage());
			cv.put(MESSAGE_COLUMN_CONTENT, item.getMessage());
			cv.put(MESSAGE_COLUMN_DATE, item.getDate());
			cv.put(MESSAGE_COLUMN_DISPLAY_NAME, item.getUserName());
			cv.put(MESSAGE_COLUMN_IS_EXPIRED, item.getIsExpired());
			cv.put(MESSAGE_COLUMN_LISTING_ID, item.getItemId());
			cv.put(MESSAGE_COLUMN_LISTING_NAME, item.getItemName());
			cv.put(MESSAGE_COLUMN_LISTING_OWNER_NAME,
					item.getListingOwnerName());
			cv.put(MESSAGE_COLUMN_LISTING_URL, item.getItemImage());
			cv.put(MESSAGE_COLUMN_LISTING_OWNER_ID, item.getListingOwnerId());
			cv.put(MESSAGE_COLUMN_USER_ID, item.getUserId());
			cv.put(MESSAGE_COLUMN_READ, item.getRead());
			cv.put(MESSAGE_COLUMN_AWAY_ID, item.getAwayId());
			cv.put(MESSAGE_COLUMN_AWAY_IMAGE, item.getAwayImage());
			cv.put(MESSAGE_COLUMN_AWAY_NAME, item.getAwayName());
			cv.put(MESSAGE_COLUMN_ACCOUNT_ID, accountId);

			getWritableDatabase().insert(TABLE_MESSAGE, MESSAGE_COLUMN_ID, cv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void insertMessagesList(List<MessageItem> list, String accountId) {
		for (MessageItem item : list) {
//			if (!checkBlockedUser(item.getAwayId(), accountId, true))
				insertMessage(item, accountId);
		}
	}

	public boolean checkBlockedUser(String userId, String accountId, boolean isBlockByMe) {
		int iBlock = isBlockByMe?1:0;
		String query = "Select _id from " + TABLE_BLOCKED_USER + " where "
				+ BLOCKED_COLUMN_BLOCKED_ID + " = '" + userId + "' and "
				+ BLOCKED_COLUMN_IS_BLOCKED_BY_ME + " = '" + iBlock + "' and "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		Cursor c = getReadableDatabase().rawQuery(query, null);
		if (c.getCount() > 0) {
			return true;
		} else {
			return false;
		}
	}

	public MessageThread getNewMessagesByThread(MessageThread thread,
			String accountId) {
		String threadId = thread.getThreadId();
		String query = "Select * from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_THREAD_ID + " = '" + threadId + "' and "
				+ MESSAGE_COLUMN_READ + " = '0' and "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		Cursor c = null;
		c = getReadableDatabase().rawQuery(query, null);
		thread.setNewMessage(c.getCount());
		return thread;
	}

	public int getNewMessagesCountByItem(String itemId, String accountId) {
		String query = "Select * from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_LISTING_ID + " = '" + itemId + "' and "
				+ MESSAGE_COLUMN_READ + " = '0' and "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		Cursor c = null;
		c = getReadableDatabase().rawQuery(query, null);
		int count = c.getCount();
		c.close();
		return count;
	}

	public int getTotalNewMessages(String accountId) {
		String query = "Select _id from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_READ + " = '0' and "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		Cursor c = null;
		c = getReadableDatabase().rawQuery(query, null);
		return c.getCount();
	}

	public int getMyPostingNewMessages(String accountId) {
		String query = "Select _id from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_READ + " = '0' and "
				+ MESSAGE_COLUMN_LISTING_OWNER_ID + " = '" + accountId
				+ "' and " + MESSAGE_COLUMN_IS_EXPIRED + " = '0' and "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		;
		Cursor c = getReadableDatabase().rawQuery(query, null);
		return c.getCount();
	}

	public int getMyEnquiriesNewMessages(String accountId) {
		String query = "Select _id from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_READ + " = '0' and "
				+ MESSAGE_COLUMN_LISTING_OWNER_ID + " <> '" + accountId
				+ "' and " + MESSAGE_COLUMN_IS_EXPIRED + " = '0' and "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		Cursor c = getReadableDatabase().rawQuery(query, null);
		return c.getCount();
	}

	public int getExpiredNewMessages(String accountId) {
		String query = "Select _id from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_IS_EXPIRED + " = '1' and "
				+ MESSAGE_COLUMN_READ + " = '0' and "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		;
		Cursor c = getReadableDatabase().rawQuery(query, null);
		return c.getCount();
	}

	public List<MessageItem> getHistoryMessageByThread(String threadId,
			String accountId) {
		String query = "Select * from " + TABLE_MESSAGE + " where "
				+ MESSAGE_COLUMN_THREAD_ID + " = '" + threadId + "' and "
				+ MESSAGE_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		Cursor c = null;
		c = getReadableDatabase().rawQuery(query, null);
		List<MessageItem> list = parseListMessageFromCursor(c);
		c.close();
		return list;
	}

	private List<MessageGroup> parseListMessageGroupFromCursor(Cursor c) {
		List<MessageGroup> list = new ArrayList<MessageGroup>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			MessageGroup group = new MessageGroup();

			group.setStatus(MessageGroup.STATUS_NORMAL);
			group.getItem().setId(c.getString(7));
			group.getItem().setTitle(c.getString(8));
			group.getItem().setUserName(c.getString(9));
			group.getItem().setImage(c.getString(10));
			group.getItem().setUserID(c.getString(11));
			group.setIsExpired(c.getInt(6));
			list.add(group);
			c.moveToNext();

		}
		return list;
	}

	private List<MessageThread> parseListMessageThreadFromCursor(Cursor c) {
		List<MessageThread> list = new ArrayList<MessageThread>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			MessageThread thread = new MessageThread();

			thread.setStatus(MessageThread.STATUS_NORMAL);
			thread.setThreadId(c.getString(1));
			thread.setItemId(c.getString(7));
			thread.setAwayId(c.getString(14));
			thread.setAwayName(c.getString(16));
			String awayImage = c.getString(15);
			if (awayImage != null && !awayImage.equals("")) {
				thread.setAwayImage(awayImage);
			}
			list.add(thread);
			c.moveToNext();

		}
		return list;
	}

	private List<MessageItem> parseListMessageFromCursor(Cursor c) {
		List<MessageItem> list = new ArrayList<MessageItem>();

		c.moveToFirst();
		while (!c.isAfterLast()) {
			MessageItem item = new MessageItem();

			item.setId(c.getInt(0));
			item.setThreadId(c.getString(1));
			item.setUserImage(c.getString(2));
			item.setMessage(c.getString(3));
			item.setDate(c.getLong(4));
			item.setUserName(c.getString(5));
			item.setIsExpired(c.getInt(6));
			item.setItemId(c.getString(7));
			item.setItemName(c.getString(8));
			item.setListingOwnerName(c.getString(9));
			item.setItemImage(c.getString(10));
			item.setListingOwnerId(c.getString(11));
			item.setUserId(c.getString(12));
			item.setRead(c.getInt(13));
			item.setAwayId(c.getString(14));
			item.setAwayImage(c.getString(15));
			item.setAwayName(c.getString(16));

			list.add(item);
			c.moveToNext();

		}
		return list;
	}

	public void insertDisabledCategoryPref(String pref, String accountId) {
		try {
			ContentValues cv = new ContentValues();

			cv.put(CATEGORY_PREF_COLUMN_DISABLED, pref);
			cv.put(CATEGORY_PREF_COLUMN_ACCOUNT_ID, accountId);

			getWritableDatabase().insert(TABLE_CATEGORY_PREF,
					CATEGORY_PREF_COLUMN_ID, cv);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getDisabledCategoryPrefByAccount(String accountId) {
		String pref = "";
		String query = "Select * from " + TABLE_CATEGORY_PREF + " where "
				+ CATEGORY_PREF_COLUMN_ACCOUNT_ID + " = '" + accountId + "'";
		Cursor c = null;
		c = getReadableDatabase().rawQuery(query, null);

		c.moveToFirst();
		while (!c.isAfterLast()) {
			pref = c.getString(1);
			c.moveToNext();

		}
		c.close();
		return pref;
	}

}
