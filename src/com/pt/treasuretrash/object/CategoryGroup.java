package com.pt.treasuretrash.object;

import java.util.ArrayList;
import java.util.List;

public class CategoryGroup extends BaseObject{

	private Category grandParent;
	private List<Category> arrCategories;
	private int status;
	private boolean active;
	private int state;
	
	public static final int STATUS_COLLAPSE = 1;
	public static final int STATUS_EXPAND = 2;
	
	public static final int STATE_NORMAL = 1;
	public static final int STATE_EDIT = 2;

	public CategoryGroup(){
		this.arrCategories = new ArrayList<Category>();
		status = STATUS_COLLAPSE;
		active = true;
		state = STATE_NORMAL;
	}
	
	public Category getGrandParent() {
		return grandParent;
	}

	public void setGrandParent(Category grandParent) {
		this.grandParent = grandParent;
	}

	public List<Category> getArrCategories() {
		return arrCategories;
	}

	public void setArrCategories(List<Category> arrCategories) {
		this.arrCategories = arrCategories;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

}
