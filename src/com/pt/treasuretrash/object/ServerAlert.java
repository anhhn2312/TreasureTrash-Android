package com.pt.treasuretrash.object;

public class ServerAlert extends BaseObject{
	private String message;
	private String locKey;
	private String actionLocKey;
	
	private String type;
	private String id;
	private String userName, itemName;
	private int numberSlot;
	
	
	public static String NOTIFICATION_MSG_LISTING = "NOTIFICATION_MSG_LISTING";
	public static String NOTIFICATION_MSG_GONE = "NOTIFICATION_MSG_GONE";
	public static String NOTIFICATION_MSG_FOLLOW = "NOTIFICATION_MSG_FOLLOW";
	public static String NOTIFICATION_MSG_ADD_SINGLE_SLOT = "NOTIFICATION_MSG_ADD_SINGLE_SLOT";
	public static String NOTIFICATION_MSG_ADD_FREE_SLOT = "NOTIFICATION_MSG_ADD_FREE_SLOT";
	public static String NOTIFICATION_MSG_SUBTRACT_SINGLE_SLOT = "NOTIFICATION_MSG_SUBTRACT_SINGLE_SLOT";
	public static String NOTIFICATION_MSG_SUBTRACT_FREE_SLOT = "NOTIFICATION_MSG_SUBTRACT_FREE_SLOT";
	public static String NOTIFICATION_MSG_EXPIRE = "NOTIFICATION_MSG_EXPIRE";
	public static String NOTIFICATION_FLAGGED_ITEM = "NOTIFICATION_FLAGGED_ITEM";
	public static String NOTIFICATION_FRIEND_JOIN = "NOTIFICATION_FRIEND_JOIN";
	public static String NOTIFICATION_FREE_MESSAGE = "NOTIFICATION_FREE_MESSAGE";
	
	public static String TYPE_ADD_FREE_SLOT = "AddFreeSlot";
	
	
//	{
//		 pn_apns : 
//		 {
//		  aps : 
//		  {
//		   alert : {
//		    loc-key: "NOTIFICATION_MSG_LISTING or NOTIFICATION_MSG_GONE or NOTIFICATION_MSG_FOLLOW or NOTIFICATION_MSG_ADD_SINGLE_SLOT or NOTIFICATION_MSG_ADD_FREE_SLOT or NOTIFICATION_MSG_SUBTRACT_SINGLE_SLOT or NOTIFICATION_MSG_SUBTRACT_FREE_SLOT or NOTIFICATION_MSG_EXPIRE or NOTIFICATION_FLAGGED_ITEM",
//		    loc-args: "maybe have user name, item title('your_username', 'item_title')",
//		    action-loc-key: "Relist or empty"
//		   }
//		  },
//		  type : 'flagged or Expire or Listing or Follow or Gone',
//		  id : item Id,
//		  numberOfSlot : numberSlot
//		 }
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getLocKey() {
		return locKey;
	}
	public void setLocKey(String locKey) {
		this.locKey = locKey;
	}

	public String getActionLocKey() {
		return actionLocKey;
	}
	public void setActionLocKey(String actionLocKey) {
		this.actionLocKey = actionLocKey;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public int getNumberSlot() {
		return numberSlot;
	}
	public void setNumberSlot(int numberSlot) {
		this.numberSlot = numberSlot;
	}
	public String getLocArg1() {
		return userName;
	}
	public void setLocArg1(String userName) {
		this.userName = userName;
	}
	public String getLocArg2() {
		return itemName;
	}
	public void setLocArg2(String itemName) {
		this.itemName = itemName;
	}
	
}
