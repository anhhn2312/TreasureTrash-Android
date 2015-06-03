package com.pt.treasuretrash.object;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import com.josh.treasuretrash.R;

public class ImageObj extends BaseObject implements Parcelable{

	private String id, imageUrl;
	private Bitmap bitmap;

	public ImageObj() {
	}

	public ImageObj(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	public ImageObj(Parcel in){
		this.id = in.readString();
		this.imageUrl = in.readString();
//		this.bitmap = Bitmap.CREATOR.createFromParcel(in);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public int getResourceNoImage() {
		return R.drawable.addlisting_icon_addimage;
	}

	public int getResourceError() {
		return R.drawable.ic_add_image_red;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(imageUrl);
//		dest.writeParcelable(bitmap, flags);
	}
	
	public static final Parcelable.Creator<ImageObj> CREATOR = new Parcelable.Creator<ImageObj>() {

		@Override
		public ImageObj createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new ImageObj(in);
		}

		@Override
		public ImageObj[] newArray(int size) {
			// TODO Auto-generated method stub
			return new ImageObj[size];
		}
	};

}
