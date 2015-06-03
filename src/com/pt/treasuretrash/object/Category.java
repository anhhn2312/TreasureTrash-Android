package com.pt.treasuretrash.object;

import android.os.Parcel;
import android.os.Parcelable;

public class Category extends BaseObject implements Parcelable{
	// "id": 5,
	// "name": "Womens    ",
	// "isHidden": false,
	// "parentCategoryId": 1

	private String id, name, parentCategoryId;
	private boolean isHidden;

	public Category() {
		// TODO Auto-generated constructor stub
	}

	public Category(String id, String name, boolean isHidden,
			String parentcategoryId) {
		this.id = id;
		this.name = name;
		this.isHidden = isHidden;
		this.parentCategoryId = parentcategoryId;
	}
	
	public Category(Parcel in){
		this.id = in.readString();
		this.name = in.readString();
		this.isHidden = in.readInt() == 1;
		this.parentCategoryId = in.readString();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentCategoryId() {
		return parentCategoryId;
	}

	public void setParentCategoryId(String parentCategoryId) {
		this.parentCategoryId = parentCategoryId;
	}

	public boolean isHidden() {
		return isHidden;
	}

	public void setHidden(boolean isHidden) {
		this.isHidden = isHidden;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
//	private String id, name, parentCategoryId;
//	private boolean isHidden;

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(name);
		dest.writeInt(isHidden?1:0);
		dest.writeString(parentCategoryId);
		
	}
	
	 public static final Parcelable.Creator<Category> CREATOR = new Parcelable.Creator<Category>() {
	        @Override
			public Category createFromParcel(Parcel in) {
	            return new Category(in);
	        }

	        @Override
			public Category[] newArray(int size) {
	        return new Category[size];
	        }
	    };
	
	

}
