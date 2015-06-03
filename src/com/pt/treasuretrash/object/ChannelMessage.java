package com.pt.treasuretrash.object;

import java.util.List;

public class ChannelMessage extends BaseObject{

	private String channelName;
	private List<MessageItem> messageItem;
	
	public ChannelMessage() {
		
	}
	public ChannelMessage(String channelName, List<MessageItem> messageItem) {
		this.channelName = channelName;
		this.messageItem = messageItem;
	}
	public String getChannelName() {
		return channelName;
	}
	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}
	public List<MessageItem> getMessageItem() {
		return messageItem;
	}
	public void setMessageItem(List<MessageItem> messageItem) {
		this.messageItem = messageItem;
	}
	
	
}
