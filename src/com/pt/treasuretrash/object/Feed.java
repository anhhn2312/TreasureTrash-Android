package com.pt.treasuretrash.object;

public class Feed extends BaseObject{

	private String id, userId, userName, userImg, itemId, itemTitle, itemImg, type;
	private int priority;
	private long createdDate, loadTime;
	private boolean followed;
	
	private boolean configedRow = false;
	
	public static String TYPE_GONE = "Gone";
	public static String TYPE_FOLLOW = "Follow";
	public static String TYPE_LISTING = "Listing";
	public static String TYPE_FRIEND_JOIN = "FriendJoin";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserImg() {
		return userImg;
	}
	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	public String getItemTitle() {
		return itemTitle;
	}
	public void setItemTitle(String itemTitle) {
		this.itemTitle = itemTitle;
	}
	public String getItemImg() {
		return itemImg;
	}
	public void setItemImg(String itemImg) {
		this.itemImg = itemImg;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public long getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}
	public boolean isFollowed() {
		return followed;
	}
	public void setFollowed(boolean followed) {
		this.followed = followed;
	}
	public long getLoadTime() {
		return loadTime;
	}
	public void setLoadTime(long loadTime) {
		this.loadTime = loadTime;
	}
	
	public boolean isConfigedRow() {
		return configedRow;
	}
	public void setConfigedRow(boolean configedRow) {
		this.configedRow = configedRow;
	}
	
	
}
