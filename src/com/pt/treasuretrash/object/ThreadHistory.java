package com.pt.treasuretrash.object;

public class ThreadHistory extends BaseObject{

	private String userId;
	private String contactId;
	private String channelName;
	private long lastRead;
	private String itemId;
	
	public ThreadHistory(String userId, String contactId, String channelName, long lastRead, String itemId){
		this.userId = userId;
		this.contactId = contactId;
		this.channelName = channelName;
		this.lastRead = lastRead;
		this.itemId = itemId;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContactId() {
		return contactId;
	}
	public void setContactId(String contactId) {
		this.contactId = contactId;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public long getLastRead() {
		return lastRead;
	}
	public void setLastRead(long lastRead) {
		this.lastRead = lastRead;
	}
	public String getItemId() {
		return itemId;
	}
	public void setItemId(String itemId) {
		this.itemId = itemId;
	}
	
	
	
}
