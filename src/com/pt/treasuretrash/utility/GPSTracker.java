package com.pt.treasuretrash.utility;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

public class GPSTracker extends Service implements LocationListener {

	Cursor model;

	private final Context mContext;

	// flag for GPS status
	boolean isGPSEnabled = false;

	// flag for network status
	boolean isNetworkEnabled = false;

	// flag for GPS status
	boolean canGetLocation = false;

	Location location; // location
	double latitude; // latitude
	double longitude; // longitude

	// The minimum distance to change Updates in meters
	private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters

	// The minimum time between updates in milliseconds
	private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 1; // 1 minute

	// Declaring a Location Manager
	protected LocationManager locationManager;

//	private IGpsTrackerListener listener;

	public GPSTracker(Context context) {
		this.mContext = context;
	}

	public Location getLocation() {
		this.canGetLocation = false;
		try {
			locationManager = (LocationManager) mContext
					.getSystemService(LOCATION_SERVICE);

			// getting GPS status
			isGPSEnabled = locationManager
					.isProviderEnabled(LocationManager.GPS_PROVIDER);

			// getting network status
			isNetworkEnabled = locationManager
					.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
					&& isNetworkAvailable();

			if (!isGPSEnabled && !isNetworkEnabled) {
				// showSettingsAlert();
			} else {
				this.canGetLocation = true;
				// First get location from Network Provider
				if (isNetworkEnabled) {
//					locationManager.requestLocationUpdates(
//							LocationManager.NETWORK_PROVIDER,
//							MIN_TIME_BW_UPDATES,
//							MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
					Log.d("CheckNetwork", "CheckNetwork");
					if (locationManager != null) {
						location = locationManager
								.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
						if (location != null) {
							latitude = location.getLatitude();
							longitude = location.getLongitude();
						}
					}
				}
				// if GPS Enabled get lat/long using GPS Services
				if (isGPSEnabled) {
					if (location == null) {
						locationManager.requestLocationUpdates(
								LocationManager.GPS_PROVIDER,
								MIN_TIME_BW_UPDATES,
								MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
						Log.d("GPS Enabled", "GPS Enabled");
						if (locationManager != null) {
							location = locationManager
									.getLastKnownLocation(LocationManager.GPS_PROVIDER);
							if (location != null) {
								latitude = location.getLatitude();
								longitude = location.getLongitude();
							}
						}
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return location;
	}

	/**
	 * Stop using GPS listener Calling this function will stop using GPS in your
	 * app
	 * */
	public void stopUsingGPS() {
		if (locationManager != null) {
			locationManager.removeUpdates(GPSTracker.this);
		}
	}

	/**
	 * Function to get latitude
	 * */
	public double getLatitude() {
		if (location != null) {
			latitude = location.getLatitude();
		}

		// return latitude
		return latitude;
	}

	/**
	 * Function to get longitude
	 * */
	public double getLongitude() {
		if (location != null) {
			longitude = location.getLongitude();
		}

		// return longitude
		return longitude;
	}

	/**
	 * Function to check GPS/wifi enabled
	 * 
	 * @return boolean
	 * */
	public boolean canGetLocation() {
		return this.canGetLocation;
	}

	/**
	 * Function to show settings alert dialog On pressing Settings button will
	 * lauch Settings Options
	 * */
	public void showSettingsAlert() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);

		// Setting Dialog Title
		alertDialog.setTitle("GPS setting");

		// Setting Dialog Message
		alertDialog
				.setMessage("GPS is now disabled. Do you want to enter setting screen?");

		// On pressing Settings button
		alertDialog.setPositiveButton("Settings",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						Intent intent = new Intent(
								Settings.ACTION_LOCATION_SOURCE_SETTINGS);
						mContext.startActivity(intent);
					}
				});

		// on pressing cancel button
		alertDialog.setNegativeButton("Cancel",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		// Showing Alert Message
		alertDialog.show();
	}

	@Override
	public void onLocationChanged(Location location) {
//		listener.onLocationChanged(location);
//		Log.e("LOCATION_CHANGED", "LOCATION_CHANGED");
	}

	@Override
	public void onProviderDisabled(String provider) {
//		Toast.makeText(mContext,
//				"Attempted to ping your location, and GPS was disabled.",
//				Toast.LENGTH_LONG).show();
//		listener.onProviderDisabled();
	}

	@Override
	public void onProviderEnabled(String provider) {
//		listener.onProviderEnabled();
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	public String getName(Double lat, Double lng) throws java.io.IOException {
		String add = "";
		int i = 0;
		Geocoder geocoder = new Geocoder(mContext,
				java.util.Locale.getDefault());
		try {
			while (i < 5) {

				java.util.List<Address> addresses = geocoder.getFromLocation(
						lat, lng, 1);

				// Toast.makeText(this,"size"+addresses.size(),
				// Toast.LENGTH_LONG).show();
				Address obj = addresses.get(0);

				// add = obj.getAddressLine(2) + ", "
				// + obj.getAddressLine(3) + ", " + obj.getAddressLine(4) ;
				String state = "";
				String city = "";
				String country = "";

				if (obj.getSubAdminArea() != null) {
					state = obj.getSubAdminArea();
				} else if (obj.getAdminArea() != null) {
					state = obj.getAdminArea();
				}

				if (obj.getLocality() != null) {
					city = obj.getLocality();
				} else if (obj.getSubLocality() != null) {
					city = obj.getSubLocality();
				} else if (obj.getAdminArea() != null) {
					city = obj.getAdminArea();
				}

				if (obj.getCountryName() != null) {
					country = obj.getCountryName();
				}

				add = state + ", " + city + ", " + country;

				// Toast.makeText(this,add, Toast.LENGTH_LONG).show();
				if (!add.equals("")) {
					break;
				}
				i++;
			}
		} catch (Exception ex) {
			// Toast.makeText(this,"exception"+ex.getMessage(),
			// Toast.LENGTH_LONG).show();
		}
		return add;

	}

	protected boolean isNetworkAvailable() {
		ConnectivityManager conMgr = (ConnectivityManager) mContext
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo i = conMgr.getActiveNetworkInfo();
		if (i == null)
			return false;
		if (!i.isConnected())
			return false;
		if (!i.isAvailable())
			return false;
		return true;
	}
	
	public void destroy(){
		stopUsingGPS();
		locationManager = null;
	}

	public interface IGpsTrackerListener {
		void onLocationChanged(Location location);

		void onProviderDisabled();

		void onProviderEnabled();
	}
}