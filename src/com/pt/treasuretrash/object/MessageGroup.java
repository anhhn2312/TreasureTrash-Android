package com.pt.treasuretrash.object;

import java.util.ArrayList;
import java.util.List;

public class MessageGroup extends BaseObject{

	private List<MessageThread> arrThread;
	private Item item;
	private int isExpired;
	private int newMessage;
	private int status;
	
	public static final int STATUS_NORMAL = 1;
	public static final int STATUS_DELETE_PENDING = 2;
	public static final int STATUS_DELETE_CONFIRM = 3;

	public MessageGroup() {
		arrThread = new ArrayList<MessageThread>();
		item = new Item();
	}

	public List<MessageThread> getArrThread() {
		return arrThread;
	}

	public void setArrThread(List<MessageThread> arrThread) {
		this.arrThread = arrThread;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public int getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(int newMessage) {
		this.newMessage = newMessage;
	}


	public int getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(int isExpired) {
		this.isExpired = isExpired;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
}
