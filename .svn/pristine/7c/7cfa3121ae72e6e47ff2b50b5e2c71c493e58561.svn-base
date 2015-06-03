package com.pt.treasuretrash.object;

import java.util.Currency;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;

import com.pt.treasuretrash.config.GlobalValue;

public class LocationObj extends BaseObject implements Parcelable {

	private String id = "", country = "", countryCode = "", state = "",
			shortDescription = "", district = "", address = "";

	private Double latitude = 0d, longitude = 0d;

	public LocationObj() {
		
	}

	public LocationObj(Parcel in){
		this.id = in.readString();
		this.country = in.readString();
		this.countryCode = in.readString();
		this.state = in.readString();
		this.shortDescription = in.readString();
		this.district = in.readString();
		this.address = in.readString();
		
		this.latitude = in.readDouble();
		this.longitude = in.readDouble();
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getCurrencySymbol() {
		try {
			String symbol = Currency.getInstance(new Locale("", countryCode))
					.getCurrencyCode();
			return GlobalValue.CURRENCY_MAP.get(symbol);
		} catch (Exception ex) {
			return "";
		}
	}

	public String getLocationAddress() {
		String location = "";
		location += getDistrict().isEmpty() ? "" : getDistrict() + ", ";
		location += getState().isEmpty() ? "" : getState() + ", ";
		location += getCountry().isEmpty() ? "" : getCountry();
		return location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(country);
		dest.writeString(countryCode);
		dest.writeString(state);
		dest.writeString(shortDescription);
		dest.writeString(district);
		dest.writeString(address);
		dest.writeDouble(latitude);
		dest.writeDouble(longitude);
		
	}
	
	public static final Parcelable.Creator<LocationObj> CREATOR = new Parcelable.Creator<LocationObj>() {

		@Override
		public LocationObj createFromParcel(Parcel in) {
			// TODO Auto-generated method stub
			return new LocationObj(in);
		}

		@Override
		public LocationObj[] newArray(int size) {
			// TODO Auto-generated method stub
			return new LocationObj[size];
		}
	};

}
