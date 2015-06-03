package com.pt.treasuretrash.object;

import java.util.ArrayList;

import com.nostra13.universalimageloader.core.download.SlowNetworkImageDownloader;

public class Account extends BaseObject {

	public static final String ACTION_KEY = "action";
	public static final String ACTION_LOGIN = "login";
	public static final String ACTION_REGISTER = "register";
	// {
	// "Id": {},
	// "UserName": "",
	// "Email": "",
	// "Name": "",
	// "ReceiveNewsLetter": false,
	// "Birthday": 0,
	// "Gender": false,
	// "ImageUrl": "",
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
	// "RewardType": "",
	// "NumberOfFreeSlot": 0,
	// "NumberOfFreeSlotActive": 0,
	// "NumberOfSingleUseSlot": 0,
	// "NumberOfSingleUseSlotActive": 0

	// "IsFollowed": false,
	// "TotalFollower": 0,
	// "TotalFollowing": 0

	// }
	
	public static final int ACC_FACEBOOK = 1;
	public static final int ACC_GOOGLE = 2;

	private String id, username, password, email, name, imageUrl, rewardType;
	private String externalType = "";
	private boolean isReceiveNewsLetter = false, isMale = true,
			isFollowed = false, isExternalAccount = false;
	private int numberFreeSlot, numberFreeSlotActive, numberSingleUseSlot,
			numberSingleSlotActive, totalFollower, totalFollowing,
			totalExpiredDefault = 0, totalExpiredAddition = 0, account_type;
	private long birthDay=0;
	private LocationObj location;
	private LocationObj unsavedLocation;
	private ArrayList<Item> arrItems;
	private String externalAccessToken = "";
	private String newEmail = "";

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPassword() {
		return password == null ? "" : password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getRewardType() {
		return rewardType;
	}

	public void setRewardType(String rewardType) {
		this.rewardType = rewardType;
	}

	public boolean isReceiveNewsLetter() {
		return isReceiveNewsLetter;
	}

	public void setReceiveNewsLetter(boolean isReceiveNewsLetter) {
		this.isReceiveNewsLetter = isReceiveNewsLetter;
	}

	public boolean isMale() {
		return isMale;
	}

	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}

	public int getNumberFreeSlot() {
		return numberFreeSlot;
	}

	public void setNumberFreeSlot(int numberFreeSlot) {
		this.numberFreeSlot = numberFreeSlot;
	}

	public int getNumberFreeSlotActive() {
		return numberFreeSlotActive;
	}

	public void setNumberFreeSlotActive(int numberFreeSlotActive) {
		this.numberFreeSlotActive = numberFreeSlotActive;
	}

	public int getNumberSingleUseSlot() {
		return numberSingleUseSlot;
	}

	public void setNumberSingleUseSlot(int numberSingleUseSlot) {
		this.numberSingleUseSlot = numberSingleUseSlot;
	}

	public int getNumberSingleSlotActive() {
		return numberSingleSlotActive;
	}

	public void setNumberSingleSlotActive(int numberSingleSlotActive) {
		this.numberSingleSlotActive = numberSingleSlotActive;
	}

	public long getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(long birthDay) {
		this.birthDay = birthDay;
	}

	public LocationObj getLocation() {
		return this.location;
	}

	public void setLocation(LocationObj location) {
		this.location = location;
	}

	public ArrayList<Item> getArrItems() {
		return arrItems;
	}

	public void setArrItems(ArrayList<Item> arrItems) {
		this.arrItems = arrItems;
	}

	public boolean isFollowed() {
		return isFollowed;
	}

	public void setFollowed(boolean isFollowed) {
		this.isFollowed = isFollowed;
	}

	public int getTotalFollower() {
		return totalFollower;
	}

	public void setTotalFollower(int totalFollower) {
		this.totalFollower = totalFollower;
	}

	public int getTotalFollowing() {
		return totalFollowing;
	}

	public void setTotalFollowing(int totalFollowing) {
		this.totalFollowing = totalFollowing;
	}

	public boolean isExternalAccount() {
		return isExternalAccount;
	}

	public void setExternalAccount(boolean isExternalAccount) {
		this.isExternalAccount = isExternalAccount;
	}

	public String getExternalAccessToken() {
		return externalAccessToken;
	}

	public void setExternalAccessToken(String externalAccessToken) {
		this.externalAccessToken = externalAccessToken;
	}

	public String getExternalType() {
		return externalType;
	}

	public void setExternalType(String externalType) {
		this.externalType = externalType;
	}

	public LocationObj getUnsavedLocation() {
		return unsavedLocation;
	}

	public void setUnsavedLocation(LocationObj unsavedLocation) {
		this.unsavedLocation = unsavedLocation;
	}

	public String getNewEmail() {
		return newEmail;
	}

	public void setNewEmail(String newEmail) {
		this.newEmail = newEmail;
	}

	public int getTotalExpiredDefault() {
		return totalExpiredDefault;
	}

	public void setTotalExpiredDefault(int totalExpiredDefault) {
		this.totalExpiredDefault = totalExpiredDefault;
	}

	public int getTotalExpiredAddition() {
		return totalExpiredAddition;
	}

	public void setTotalExpiredAddition(int totalExpiredAddition) {
		this.totalExpiredAddition = totalExpiredAddition;
	}

	public int getTotalSlotAdditionCanUse() {
		return numberSingleUseSlot - totalExpiredAddition;
	}

	public int getTotalSlotAdditionUsed() {
		return getTotalSlotAdditionCanUse() - numberSingleSlotActive;
	}

	public int getAccount_type() {
		return account_type;
	}

	public void setAccount_type(int account_type) {
		this.account_type = account_type;
	}

}
