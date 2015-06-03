package com.pt.treasuretrash.object;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Currency;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

@SuppressLint("NewApi")
public class Item extends BaseObject implements Parcelable {

	// "Id": {},
	// "Title": "",
	// "Cost": 0,
	// "Currency": "",
	// "Description": "",
	// "State": "",
	// "Status": "",
	// "Type": "",
	// "SlotImage": 0,
	// "SlotType": "",
	// "CreatedDate": 0,
	// "ExpiredDate": 0,
	// "GoneDate": 0,
	// "Distance": 0,
	// "Image": "",
	// "TotalMessage": 0,
	// "IsFavourited": false,
	// "IsFlagged":false,
	// "CanEdit": false,
	// "UserId": {},
	// "UserName": "",
	// "RewardType": "",
	// "CategoryModel": {
	// "Id": 0,
	// "Name": "",
	// "IsHidden": false,
	// "ParentCategoryId": 0
	// },
	// "Location": {
	// "Id": {},
	// "Country": "",
	// "CountryCode": "",
	// "State": "",
	// "Suburb": "",
	// "Longitude": 0,
	// "Latitude": 0,
	// "ShortDescription": ""
	// },
	// "ItemImages": [
	// {
	// "Id": {},
	// "ImageUrl": ""
	// }
	// ]
	// }
	//
	// type of purchase
	public static final String BASE64ENCODEDPUBLICKEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjlOqjIXFkYxKkm8OKXUrimtPVGi4wCPhP1P7OwPhpIjy8HGtgoIdEEtj4df3ttV9tNKlSPN5E3I12jr4V2jFH0NVDDOURKzMbP2Sr/yhUboNkuOHJnn9Gta91JWrCj82e0xALNdvSg1luWu6rmNdq6z2JtPNuu2LvTqdizmrYiDr5G98J6ktp73Bl5xz3LLDSCyJN7NFZbHXKNucno74isvFlYkJjh41GGlvfoViJDaohPHdgcq/9wbMJ6ayGzF8jYH0Y4m5Dj577AM6Rzt3kspCO+xU6dFd7OF4kdSLKbYi0ymRlSah9zkQNBghbF939vjnXqDC4P6/pzPVMyGAkwIDAQAB";
	public final static String PURCHASE_ADD_3_PHOTO = "tta3moreimages";
	public final static String PURCHASE_QUICK = "ttaquicksale";
	public final static String PURCHASE_PREMIUM = "ttapremiumsale";

	public static final String KEY_5_SLOTS = "android.test.purchased";
	public static final String KEY_10_SLOTS = "android.test.purchased";
	public static final String KEY_ADD_3_PHOTOS = "android.test.purchased";
	public static final String KEY_QUICK_TYPE = "android.test.purchased";
	public static final String KEY_PREMIUM_TYPE = "android.test.purchased";

	public static final String ITEM_5_SLOTS = "tta5slots";
	public static final String ITEM_10_SLOTS = "tta10slots";

	public final static String TYPE_FREE = "Free";
	public final static String TYPE_REGULAR = "Regular";
	public final static String TYPE_QUICK = "Quick";
	public final static String TYPE_PREMIUM = "Premium";

	public final static String ITEM_KEY_SLOT_TYPE_DEFAULT = "Default";
	public final static String ITEM_KEY_SLOT_TYPE_ADDITIONAL = "Additional";

	public static String STATE_GONE = "Gone";
	public static String STATE_ACTIVE = "Active";
	public static String STATE_EXPIRED = "Expired";

	public static String REWARD_GOLD = "Gold";
	public static String REWARD_SILVER = "Silver";
	public static String REWARD_BRONZE = "Bronze";
	public static String REWARD_NEW_MEMBER = "New Member";

	public static String SLOT_TYPE_DEFAULT = "Default";
	public static String SLOT_TYPE_ADDITIONAL = "Additional";

	public static String GONE_TO_TREASURE_TRASH = "TreasureTrash";
	public static String GONE_TO_EBAY = "eBay";
	public static String GONE_TO_OTHER = "Other";

	private String id, title, currency, description, state, status, type,
			image, slotType, userID, userName, rewardType;
	private int slotImage, totalMessage;
	private long expiredDate, createDate, goneDate, cost;
	private boolean isFavourited, isCanEdit, isFlagged, isExpired;
	private Double distance = 0d;
	private int myPublishedListing;
	private ArrayList<String> arrTransactionId = new ArrayList<String>();
	private boolean needToUpdate;

	private Category category;
	private LocationObj location;
	private ArrayList<ImageObj> arrImages = new ArrayList<ImageObj>();

	public Item() {
		arrTransactionId = new ArrayList<String>();
		arrImages = new ArrayList<ImageObj>();
		this.needToUpdate = false;
	}

	public Item(Parcel in) {
		this.id = in.readString();
		this.title = in.readString();
		this.currency = in.readString();
		this.description = in.readString();
		this.state = in.readString();
		this.status = in.readString();
		this.type = in.readString();
		this.image = in.readString();
		this.slotType = in.readString();
		this.userID = in.readString();
		this.userName = in.readString();
		this.rewardType = in.readString();

		this.slotImage = in.readInt();
		this.totalMessage = in.readInt();
		this.myPublishedListing = in.readInt();

		this.expiredDate = in.readLong();
		this.createDate = in.readLong();
		this.goneDate = in.readLong();
		this.cost = in.readLong();

		this.isFavourited = in.readInt() == 1;
		this.isCanEdit = in.readInt() == 1;
		this.isFlagged = in.readInt() == 1;
		this.isExpired = in.readInt() == 1;
		this.needToUpdate = in.readInt() == 1;

		this.distance = in.readDouble();
		in.readStringList(arrTransactionId);
		this.category = in.readParcelable(Category.class.getClassLoader());
		this.location = in.readParcelable(LocationObj.class.getClassLoader());
		in.readTypedList(arrImages, ImageObj.CREATOR);
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		try {
			this.currency = Currency.getInstance(new Locale("", currency))
					.getCurrencyCode();
		} catch (Exception ex) {
			this.currency = currency;
		}

	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getSlotType() {
		return slotType;
	}

	public void setSlotType(String slotType) {
		this.slotType = slotType;
	}

	public Double getDistance() {
		return distance;
	}

	public void setDistance(Double distance) {
		this.distance = distance;
	}

	public int getSlotImage() {
		return slotImage;
	}

	public void setSlotImage(int slotImage) {
		this.slotImage = slotImage;
	}

	public long getCost() {
		return cost;
	}

	public void setCost(long cost) {
		this.cost = cost;
	}

	public long getExpiredDate() {
		return expiredDate;
	}

	public void setExpiredDate(long expiredDate) {
		this.expiredDate = expiredDate;
	}

	public boolean isFavourited() {
		return isFavourited;
	}

	public void setFavourited(boolean isFavourited) {
		this.isFavourited = isFavourited;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	public int getTotalMessage() {
		return totalMessage;
	}

	public void setTotalMessage(int totalMessage) {
		this.totalMessage = totalMessage;
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	public long getGoneDate() {
		return goneDate;
	}

	public void setGoneDate(long goneDate) {
		this.goneDate = goneDate;
	}

	public boolean isCanEdit() {
		return isCanEdit;
	}

	public void setCanEdit(boolean isCanEdit) {
		this.isCanEdit = isCanEdit;
	}

	public boolean isFlagged() {
		return isFlagged;
	}

	public void setFlagged(boolean isFlagged) {
		this.isFlagged = isFlagged;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public LocationObj getLocation() {
		return location;
	}

	public void setLocation(LocationObj location) {
		this.location = location;
	}

	public ArrayList<ImageObj> getArrImages() {
		return arrImages;
	}

	public void setArrImages(ArrayList<ImageObj> arrImages) {
		this.arrImages = arrImages;
	}

	public ArrayList<String> getArrTransactionId() {
		return arrTransactionId;
	}

	public void setArrTransactionId(ArrayList<String> arrTransactionId) {
		this.arrTransactionId = arrTransactionId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getMyPublishedListing() {
		return myPublishedListing;
	}

	public void setMyPublishedListing(int myPublishedListing) {
		this.myPublishedListing = myPublishedListing;
	}

	public String getPurchaseType() {
		if (type.equals(TYPE_QUICK)) {
			return PURCHASE_QUICK;
		} else
			return PURCHASE_PREMIUM;
	}

	public boolean isToGo() {
		long currentTime = Calendar.getInstance().getTimeInMillis();
		long timeAday = 1 * 24 * 60 * 60 * 1000;
		return expiredDate <= currentTime + timeAday;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	public boolean isNeedToUpdate() {
		return needToUpdate;
	}

	public void setNeedToUpdate(boolean needToUpdate) {
		this.needToUpdate = needToUpdate;
	}

	// private String id, title, currency, description, state, status, type,
	// image, slotType, userID, userName, rewardType;
	// private int slotImage, totalMessage;
	// private long expiredDate, createDate, goneDate, cost;
	// private boolean isFavourited, isCanEdit, isFlagged, isExpired,
	// needToUpdate;
	// private Double distance;
	// private ArrayList<String> arrTransactionId;
	//
	// private Category category;
	// private LocationObj location;
	// private ArrayList<ImageObj> arrImages;

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(title);
		dest.writeString(currency);
		dest.writeString(description);
		dest.writeString(state);
		dest.writeString(status);
		dest.writeString(type);
		dest.writeString(image);
		dest.writeString(slotType);
		dest.writeString(userID);
		dest.writeString(userName);
		dest.writeString(rewardType);

		dest.writeInt(slotImage);
		dest.writeInt(totalMessage);
		dest.writeInt(myPublishedListing);

		dest.writeLong(expiredDate);
		dest.writeLong(createDate);
		dest.writeLong(goneDate);
		dest.writeLong(cost);

		dest.writeInt(isFavourited ? 1 : 0);
		dest.writeInt(isCanEdit ? 1 : 0);
		dest.writeInt(isFlagged ? 1 : 0);
		dest.writeInt(isExpired ? 1 : 0);
		dest.writeInt(needToUpdate ? 1 : 0);

		dest.writeDouble(distance);
		dest.writeStringList(arrTransactionId);
		dest.writeParcelable(category, flags);
		dest.writeParcelable(location, flags);
		dest.writeTypedList(arrImages);

	}

	public static final Parcelable.Creator<Item> CREATOR = new Parcelable.Creator<Item>() {
		@Override
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}

		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};

	public void print() {

		// dest.writeString(id);
		// dest.writeString(title);
		// dest.writeString(currency);
		// dest.writeString(description);
		// dest.writeString(state);
		// dest.writeString(status);
		// dest.writeString(type);
		// dest.writeString(image);
		// dest.writeString(slotType);
		// dest.writeString(userID);
		// dest.writeString(userName);
		// dest.writeString(rewardType);
		//
		// dest.writeInt(slotImage);
		// dest.writeInt(totalMessage);
		// dest.writeInt(myPublishedListing);
		//
		// dest.writeLong(expiredDate);
		// dest.writeLong(createDate);
		// dest.writeLong(goneDate);
		// dest.writeLong(cost);
		//
		// dest.writeInt(isFavourited ? 1 : 0);
		// dest.writeInt(isCanEdit ? 1 : 0);
		// dest.writeInt(isFlagged ? 1 : 0);
		// dest.writeInt(isExpired ? 1 : 0);
		// dest.writeInt(needToUpdate ? 1 : 0);
		//
		// dest.writeDouble(distance);
		// dest.writeStringList(arrTransactionId);
		// dest.writeParcelable(category, flags);
		// dest.writeParcelable(location, flags);
		// dest.writeTypedList(arrImages);

		Log.i("Item", id + title + currency + description + state + status
				+ type + image + slotType + userID + userName + rewardType
				+ slotImage + totalMessage + myPublishedListing + expiredDate
				+ createDate + goneDate + cost + isExpired + isFavourited
				+ isCanEdit + needToUpdate + distance);
		Log.i("ITEM_CATEGORY",
				category.getId() + category.getName()
						+ category.getParentCategoryId());
		Log.i("ITEM_LOCATION",
						location.getId());
		
	}
}
