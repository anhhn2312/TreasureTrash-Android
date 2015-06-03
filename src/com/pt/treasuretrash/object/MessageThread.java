package com.pt.treasuretrash.object;

import java.util.ArrayList;
import java.util.List;

public class MessageThread extends BaseObject{

	private String threadId;
	private String itemId;
	private List<MessageItem> arrMessage;
	private int newMessage;
	private String awayImage = "", awayName, awayId;
	private int status;
	public static final int STATUS_NORMAL = 1;
	public static final int STATUS_DELETE_PENDING = 2;
	public static final int STATUS_DELETE_CONFIRM = 3;
	
	public MessageThread(){
		arrMessage = new ArrayList<MessageItem>();
	}

	public String getThreadId() {
		return threadId;
	}

	public void setThreadId(String threadId) {
		this.threadId = threadId;
	}

	public List<MessageItem> getArrMessage() {
		return arrMessage;
	}

	public void setArrMessage(List<MessageItem> arrMessage) {
		this.arrMessage = arrMessage;
	}

	public int getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(int newMessage) {
		this.newMessage = newMessage;
	}

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getAwayImage() {
		return awayImage;
	}

	public void setAwayImage(String awayImage) {
		this.awayImage = awayImage;
	}

	public String getAwayName() {
		return awayName;
	}

	public void setAwayName(String awayName) {
		this.awayName = awayName;
	}

	public String getAwayId() {
		return awayId;
	}

	public void setAwayId(String awayId) {
		this.awayId = awayId;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}
	
}
