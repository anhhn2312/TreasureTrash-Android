package com.pt.treasuretrash.activity;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.adapter.LocationSearchAdapter;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.network.AsyncHttpBase;
import com.pt.treasuretrash.network.StatusBackend;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.utility.DateUtil;
import com.pt.treasuretrash.utility.StringUtility;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.HelveticaLightTextView;
import com.pt.treasuretrash.widget.ProgressDialog;
import com.pt.treasuretrash.widget.ProgressHUD;
import com.pt.treasuretrash.widget.SlidingLayer;

@SuppressLint("ClickableViewAccessibility")
public class SignupEmailActivity_2 extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	// static
	private AutoBgButton btnNext;
	private EditText etUsername, etName, etEmail, etBirthday, etLocation;
	private HelveticaLightTextView lblUsernameTitle, lblNameTitle,
			lblEmailTitle, lblPolicy, lblTermOfUser, lblCancel;
	private ImageView ivUsername, ivTickUsername, ivUser, ivTickUser, ivEmail,
			ivTickEmail, ivBirthday;
	private LinearLayout btnMale, btnFemale, layoutUserName, layoutName,
			layoutEmail, layoutBirthday;
	private Date birthDate;
	private int selectedColor, unselectedColor;
	private Calendar birthdayCalendar;
	private LocationObj location, locObject;
	private String content_term = "", content_privacy = "";

	private SlidingLayer mSllChangeLocation;
	private TextView mLblLocationChanged, mLblDone, lblCurrentLocation;
	private GoogleMap maps;
	private EditText mTxtSearchLocation;
	private ImageView mImgSearchLocation;
	private LinearLayout mLlTransparent;
	private ArrayList<LocationObj> mArrLocationSearch;
	private LocationSearchAdapter mLocationAdapter;
	private ListView mLvLocationSearch;
	private String forceLoginAction = "";
	private static Activity instance;
	private String actionFrom = "";
	private String fbAccessToken = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		self = this;
		instance = this;
		setContentView(R.layout.activity_signup_email_2_scroll);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(GlobalValue.KEY_FACEBOOK_TOKEN)) {
				actionFrom = "facebook";
				fbAccessToken = bundle
						.getString(GlobalValue.KEY_FACEBOOK_TOKEN);
			}
		}

		initUI();
		// Get Location
		AccountModelManager
				.getLocationFromGoogle(
						self,
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG),
						true, new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								location = ParserUtility.parseLocations(json)
										.get(0);
								location.setLatitude(pref
										.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT));
								location.setLongitude(pref
										.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG));
								etLocation.setText(location
										.getLocationAddress());
							}

							@Override
							public void onError(int statusCode, String json) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onError() {
								// TODO Auto-generated method stub

							}
						});

		initControl();
		initData();
	}

	public static Activity getInstance() {
		return instance;
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		instance = this;
	}

	@Override
	public void onBackPressed() {
		if (mSllChangeLocation.isOpened()) {
			mSllChangeLocation.closeLayer(true);
		} else {
			super.onBackPressed();
		}
	}

	private void initUI() {
		btnNext = (AutoBgButton) findViewById(R.id.btnNext);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etName = (EditText) findViewById(R.id.etName);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etBirthday = (EditText) findViewById(R.id.etBirthday);
		lblUsernameTitle = (HelveticaLightTextView) findViewById(R.id.lblUsernameTitle);
		lblNameTitle = (HelveticaLightTextView) findViewById(R.id.lblNameTitle);
		lblEmailTitle = (HelveticaLightTextView) findViewById(R.id.lblEmailTitle);
		ivUsername = (ImageView) findViewById(R.id.ivUserName);
		ivUser = (ImageView) findViewById(R.id.ivUser);
		ivEmail = (ImageView) findViewById(R.id.ivEmail);
		ivTickUsername = (ImageView) findViewById(R.id.ivTickUsername);
		ivUser = (ImageView) findViewById(R.id.ivUser);
		ivTickUser = (ImageView) findViewById(R.id.ivTickUser);
		ivEmail = (ImageView) findViewById(R.id.ivEmail);
		ivTickEmail = (ImageView) findViewById(R.id.ivTickEmail);
		ivBirthday = (ImageView) findViewById(R.id.ivBirthday);
		btnMale = (LinearLayout) findViewById(R.id.btnMale);
		btnFemale = (LinearLayout) findViewById(R.id.btnFemale);
		layoutUserName = (LinearLayout) findViewById(R.id.layoutUserName);
		layoutName = (LinearLayout) findViewById(R.id.layoutName);
		layoutEmail = (LinearLayout) findViewById(R.id.layoutEmail);
		layoutBirthday = (LinearLayout) findViewById(R.id.layoutBirthday);
		etLocation = (EditText) findViewById(R.id.etLocation);
		lblTermOfUser = (HelveticaLightTextView) findViewById(R.id.lblTermAndConditions);
		lblPolicy = (HelveticaLightTextView) findViewById(R.id.lblPrivacy);
		lblCancel = (HelveticaLightTextView) findViewById(R.id.tvCancel);

		mSllChangeLocation = (SlidingLayer) findViewById(R.id.sll_change_location);
		mSllChangeLocation.setStickTo(SlidingLayer.STICK_TO_RIGHT);
		mSllChangeLocation.setCloseOnTapEnabled(false);
		mLblLocationChanged = (TextView) findViewById(R.id.lbl_location_changed);
		mLblLocationChanged.setSelected(true);
		mLblDone = (TextView) findViewById(R.id.lbl_done);
		lblCurrentLocation = (TextView) findViewById(R.id.lblCurrentLocation);
		mTxtSearchLocation = (EditText) findViewById(R.id.txt_type_location);
		mImgSearchLocation = (ImageView) findViewById(R.id.img_search_location);
		mLlTransparent = (LinearLayout) findViewById(R.id.llTransparent);
		mLvLocationSearch = (ListView) findViewById(R.id.lv_location_search);
	}

	private void initControl() {
		btnNext.setOnClickListener(this);
		btnMale.setOnClickListener(this);
		btnFemale.setOnClickListener(this);
		etBirthday.setOnClickListener(this);
		lblTermOfUser.setOnClickListener(this);
		lblPolicy.setOnClickListener(this);
		etLocation.setOnClickListener(this);
		mLblDone.setOnClickListener(this);
		lblCurrentLocation.setOnClickListener(this);
		mImgSearchLocation.setOnClickListener(this);
		mLlTransparent.setOnClickListener(this);
		lblCancel.setOnClickListener(this);
		mLvLocationSearch.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mLvLocationSearch.setVisibility(View.GONE);
				locObject = mArrLocationSearch.get(position);
				// Refresh maps
				initMaps();
			}
		});

		// set color :
		selectedColor = Color.parseColor(getString(R.color.sign_up_teal));
		unselectedColor = Color.parseColor(getString(R.color.sign_up_gray));

		etUsername.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int color = Color.parseColor(getString(R.color.white));
				layoutUserName.setBackgroundColor(color);
				etUsername.setBackgroundColor(color);
				lblUsernameTitle.setTextColor(Color
						.parseColor(getString(R.color.sign_up_teal)));
				ivUsername.setImageResource(R.drawable.icon_diamond_teal);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!s.toString().isEmpty())
					ivTickUsername.setVisibility(View.VISIBLE);
				else
					ivTickUsername.setVisibility(View.GONE);
			}
		});
		etName.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int color = Color.parseColor(getString(R.color.white));
				layoutName.setBackgroundColor(color);
				etName.setBackgroundColor(color);
				lblNameTitle.setTextColor(Color
						.parseColor(getString(R.color.sign_up_teal)));
				ivUser.setImageResource(R.drawable.icon_user_teal);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!s.toString().isEmpty())
					ivTickUser.setVisibility(View.VISIBLE);
				else
					ivTickUser.setVisibility(View.GONE);
			}
		});
		etEmail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int color = Color.parseColor(getString(R.color.white));
				layoutEmail.setBackgroundColor(color);
				etEmail.setBackgroundColor(color);
				lblEmailTitle.setTextColor(Color
						.parseColor(getString(R.color.sign_up_teal)));
				ivEmail.setImageResource(R.drawable.icon_signup_email_2_teal);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub
				if (!s.toString().isEmpty())
					ivTickEmail.setVisibility(View.VISIBLE);
				else
					ivTickEmail.setVisibility(View.GONE);
			}
		});

	}

	private void initData() {
		birthdayCalendar = Calendar.getInstance();
		ivBirthday.setVisibility(View.INVISIBLE);
		if (SignupActivity.registerAccount != null) {

			if (!SignupActivity.registerAccount.isExternalAccount()) {
				etUsername
						.setText(SignupActivity.registerAccount.getUsername());
				etEmail.setText(SignupActivity.registerAccount.getEmail());
				setTextColorForEditext(selectedColor, etUsername, etEmail);
				ivTickUsername.setVisibility(View.VISIBLE);
				ivTickUser.setVisibility(View.GONE);
				ivTickEmail.setVisibility(View.VISIBLE);
				birthDate = convertStringToDate(DateUtil.Defaut_Birthday);
				birthdayCalendar.setTime(birthDate);
			} else {
				if (SignupActivity.registerAccount.getEmail() != null
						&& !TextUtils.isEmpty(SignupActivity.registerAccount
								.getEmail())) {
					etUsername.setText(SignupActivity.registerAccount
							.getEmail().substring(
									0,
									SignupActivity.registerAccount.getEmail()
											.indexOf("@")));

					etEmail.setText(SignupActivity.registerAccount.getEmail());
				}

				if (SignupActivity.registerAccount.getName() != null
						&& !TextUtils.isEmpty(SignupActivity.registerAccount
								.getName())) {
					etName.setText(SignupActivity.registerAccount.getName());
				}
				if (SignupActivity.registerAccount.getBirthDay() != 0) {
					birthdayCalendar
							.setTimeInMillis(SignupActivity.registerAccount
									.getBirthDay());
					showBirthday();
				} else {
					birthDate = convertStringToDate(DateUtil.Defaut_Birthday);
					birthdayCalendar.setTime(birthDate);
				}
			}
		}

		updateGenderUI(SignupActivity.registerAccount.isMale());
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnNext) {
			processRegister();
			return;
		}
		if (v == etBirthday) {
			int color = Color.parseColor(getString(R.color.white));
			layoutBirthday.setBackgroundColor(color);
			etBirthday.setBackgroundColor(color);

			showDatePickerDialog(birthdayCalendar.get(Calendar.YEAR),
					birthdayCalendar.get(Calendar.MONDAY),
					birthdayCalendar.get(Calendar.DAY_OF_MONTH));
			return;
		}
		if (v == btnMale) {
			SignupActivity.registerAccount.setMale(true);
			updateGenderUI(true);
			return;
		}
		if (v == btnFemale) {
			SignupActivity.registerAccount.setMale(false);
			updateGenderUI(false);
			return;
		}

		if (v == lblTermOfUser) {
			showMessageTermAndPrivacy(KEY_TERM_OF_USER);
			return;
		}
		if (v == lblPolicy) {
			showMessageTermAndPrivacy(KEY_PRIVACY_POLICY);
			return;
		}
		if (v == etLocation || v == mLlTransparent) {
			changeLocation();
			return;
		}
		if (v == mLblDone) {
			if (mSllChangeLocation.isOpened()) {
				mSllChangeLocation.closeLayer(true);
				etLocation.setText(mLblLocationChanged.getText().toString());
				location = locObject;
			}
			return;
		}
		if (v == lblCurrentLocation) {
			getCurrentLocation();
			return;
		}
		if (v == mImgSearchLocation) {
			searchListLocation(mTxtSearchLocation.getText().toString());
			return;
		}

		if (v == lblCancel) {
			onBackPressed();
			return;
		}

	}

	private void getCurrentLocation() {
		AccountModelManager
				.getLocationFromGoogle(
						self,
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG),
						true, new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {
								locObject = ParserUtility.parseLocations(json)
										.get(0);
								locObject.setLatitude(pref
										.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT));
								locObject.setLongitude(pref
										.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG));
								mLvLocationSearch.setVisibility(View.GONE);
								// Refresh maps
								initMaps();
							}

							@Override
							public void onError(int statusCode, String json) {
								// TODO Auto-generated method stub

							}

							@Override
							public void onError() {
								// TODO Auto-generated method stub

							}
						});
	}

	private void searchListLocation(String input) {
		if (!input.trim().isEmpty()) {
			String url = "http://maps.google.com/maps/api/geocode/json?address="
					+ input.trim() + "&sensor=false";
			if (url.contains(" ")) {
				url = url.replace(" ", "%20");
			}
			AccountModelManager.getLocationBySearching(self, url, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							mLvLocationSearch.setVisibility(View.VISIBLE);
							mArrLocationSearch = ParserUtility
									.parseLocations(json);
							mLocationAdapter = new LocationSearchAdapter(self,
									mArrLocationSearch);
							mLvLocationSearch.setAdapter(mLocationAdapter);
						}

						@Override
						public void onError(int statusCode, String json) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onError() {
							// TODO Auto-generated method stub

						}
					});
		}
	}

	private void changeLocation() {
		if (!mSllChangeLocation.isOpened()) {
			mSllChangeLocation.openLayer(true);

			initMaps();
		} else {
			mSllChangeLocation.closeLayer(true);
		}
	}

	private void initMaps() {
		// Init text for label
		if (locObject == null) {
			try {
				locObject = (LocationObj) location.clone();
			} catch (CloneNotSupportedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		mLblLocationChanged.setText(locObject.getLocationAddress());
		mTxtSearchLocation.setText("");

		if (maps != null) {
			maps.clear();
		} else {
			maps = ((SupportMapFragment) getSupportFragmentManager()
					.findFragmentById(R.id.maps)).getMap();
			maps.setOnMapClickListener(new OnMapClickListener() {

				@Override
				public void onMapClick(final LatLng point) {
					// TODO Auto-generated method stub
					maps.clear();

					AccountModelManager.getLocationFromGoogle(self,
							point.latitude, point.longitude, false,
							new ModelManagerListener() {

								@Override
								public void onSuccess(String json) {
									// TODO Auto-generated method stub
									locObject = ParserUtility.parseLocations(
											json).get(0);
									locObject.setLatitude(point.latitude);
									locObject.setLongitude(point.longitude);
									mLblLocationChanged.setText(locObject
											.getLocationAddress());
									maps.addMarker(
											new MarkerOptions().position(point))
											.setTitle(
													locObject
															.getLocationAddress());
								}

								@Override
								public void onError(int statusCode, String json) {
									// TODO Auto-generated method stub

								}

								@Override
								public void onError() {
									// TODO Auto-generated method stub

								}
							});

				}
			});
			maps.setMyLocationEnabled(true);
		}

		LatLng latLng = new LatLng(locObject.getLatitude(),
				locObject.getLongitude());
		maps.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
		maps.addMarker(new MarkerOptions().position(latLng)).setTitle(
				mLblLocationChanged.getText().toString());
	}

	private void processRegister() {
		// TODO Auto-generated method stub
		String username = etUsername.getText().toString();
		String email = etEmail.getText().toString();
		String name = etName.getText().toString();

		// check user

		if (username.isEmpty()) {
			int color = Color
					.parseColor(getString(R.color.bg_color_error_editext));
			layoutUserName.setBackgroundColor(color);
			etUsername.setBackgroundColor(color);
			etUsername.setHintTextColor(Color.RED);
			etUsername.setHint(getString(R.string.error_empty_username));
			ivTickUsername.setVisibility(View.GONE);
			// lblUsernameTitle.setTextColor(Color.RED);
			ivUsername.setImageResource(R.drawable.icon_diamond_teal);
			return;
		}

		if (name.isEmpty()) {
			int color = Color
					.parseColor(getString(R.color.bg_color_error_editext));
			layoutName.setBackgroundColor(color);
			etName.setBackgroundColor(color);
			etName.setHintTextColor(Color.RED);
			etName.setHint(getString(R.string.error_empty_name));
			ivTickUser.setVisibility(View.GONE);
			// lblNameTitle.setTextColor(Color.RED);
			ivUser.setImageResource(R.drawable.icon_user_teal);
			return;
		}

		// check email
		if (email.isEmpty()) {
			int color = Color
					.parseColor(getString(R.color.bg_color_error_editext));
			layoutEmail.setBackgroundColor(color);
			etEmail.setHintTextColor(Color.RED);
			etEmail.setHint(getString(R.string.error_empty_email));
			etEmail.setBackgroundColor(color);
			ivTickEmail.setVisibility(View.GONE);
			// lblEmailTitle.setTextColor(Color.RED);
			ivEmail.setImageResource(R.drawable.icon_signup_email_2_teal);
			return;
		} else if (!StringUtility.isEmailValid(email)) {
			etEmail.setText("");
			int color = Color
					.parseColor(getString(R.color.bg_color_error_editext));
			layoutEmail.setBackgroundColor(color);
			etEmail.setHintTextColor(Color.RED);
			etEmail.setBackgroundColor(color);
			etEmail.setHint(getString(R.string.error_email_not_valid));
			ivTickEmail.setVisibility(View.GONE);
			// lblEmailTitle.setTextColor(Color.RED);
			ivEmail.setImageResource(R.drawable.icon_signup_email_2_teal);
			return;
		}

		// check dob
		if (etBirthday.getText().toString().isEmpty()) {
			int color = Color
					.parseColor(getString(R.color.bg_color_error_editext));
			layoutBirthday.setBackgroundColor(color);
			etBirthday.setHint(getString(R.string.error_birthday_empty));
			etBirthday.setHintTextColor(Color.RED);
			etBirthday.setBackgroundColor(color);
			ivTickEmail.setVisibility(View.GONE);
			return;
		}

		// add to register account :
		SignupActivity.registerAccount.setUsername(username);
		SignupActivity.registerAccount.setEmail(email);
		SignupActivity.registerAccount.setName(name);
		SignupActivity.registerAccount.setBirthDay(birthdayCalendar
				.getTimeInMillis());
		SignupActivity.registerAccount.setUsername(username);
		SignupActivity.registerAccount.setLocation(location);

		// call register :
		if (!SignupActivity.registerAccount.isExternalAccount()) {
			AccountModelManager.RegisterNormal(self,
					SignupActivity.registerAccount, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							// TODO Auto-generated method stub

							Toast.makeText(self,
									getString(R.string.register_successfull),
									Toast.LENGTH_SHORT).show();
							loginNormal();
						}

						@Override
						public void onError() {
							// TODO Auto-generated method stub
							Toast.makeText(self, "Register unsuccessfully!",
									Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onError(int statusCode, String json) {
							// TODO Auto-generated method stub
							int errorCode = ParserUtility
									.parseErrorStatusCode(json);
							showErrorMessage(errorCode);
						}
					});
		} else {
			AccountModelManager.registerSocialInfo(self,
					SignupActivity.registerAccount, true,
					new ModelManagerListener() {

						@Override
						public void onSuccess(String json) {
							pref.putStringValue(
									TreasureTrashSharedPreferences.PREF_FACEBOOK_ACCESS_TOKEN,
									fbAccessToken);
							openFacebookSession();
							Toast.makeText(self,
									getString(R.string.register_successfull),
									Toast.LENGTH_SHORT).show();
							loginExternal();
						}

						@Override
						public void onError(int statusCode, String json) {
							// TODO Auto-generated method stub
							int errorCode = ParserUtility
									.parseErrorStatusCode(json);

							String message = "";
							showSignupSocialErrorMessage(errorCode,
									SignupActivity.registerAccount
											.getAccount_type());

						}

						@Override
						public void onError() {
							// TODO Auto-generated method stub
							Toast.makeText(self, "Register unsuccessfully !",
									Toast.LENGTH_SHORT).show();
						}
					});
		}
	}

	private void showDatePickerDialog(int year, int month, int day) {

		DatePickerDialog dpd = new DatePickerDialog(this,
				android.R.style.Theme_Holo_Light_Dialog_NoActionBar,
				new DatePickerDialog.OnDateSetListener() {

					@Override
					public void onDateSet(DatePicker view, int year,
							int monthOfYear, int dayOfMonth) {
						// monthOfYear++;
						// dayOfMonth++;
						birthdayCalendar.set(year, monthOfYear, dayOfMonth);
						ivBirthday.setVisibility(View.VISIBLE);
						showBirthday();
					}
				}, year, month, day);
		dpd.getWindow().getAttributes().gravity = Gravity.BOTTOM
				| Gravity.CENTER_HORIZONTAL;
		dpd.show();
	}

	private Date convertStringToDate(String ddmmyyyy) {
		try {
			return DateUtil.postDateMainListItemFormat.parse(ddmmyyyy);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	private void showBirthday() {
		String strBirthDay = DateUtil.postDateTitleFormat
				.format(birthdayCalendar.getTime());
		etBirthday.setText(strBirthDay);
	}

	private void updateGenderUI(boolean isMale) {
		if (isMale) {
			btnMale.setBackgroundResource(R.drawable.btn_male_active);
			btnFemale.setBackgroundResource(R.drawable.btn_female_inactive);
		} else {
			btnMale.setBackgroundResource(R.drawable.btn_male_inactive);
			btnFemale.setBackgroundResource(R.drawable.btn_female_active);
		}
	}

	private void showErrorMessage(int errorCode) {

		if (errorCode == StatusBackend.USER_EXISTED) {
			String message = getString(R.string.error_sign_up_username_existed);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}

		if (errorCode == StatusBackend.EMAIL_EXISTED) {
			String message = getString(R.string.error_sign_up_email_existed_account);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}

		if (errorCode == StatusBackend.URL_INVALID) {
			String message = getString(R.string.error_default_message);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}
		if (errorCode == StatusBackend.USER_IS_LOCKED) {
			String message = getString(R.string.error_sign_up_username_existed);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}
		if (errorCode == StatusBackend.EXTERNAL_USER_EXISTING) {
			String message = getString(R.string.error_sign_up_social_user_existed_facebook);
			showMessageDialog(message, new DialogListener() {

				@Override
				public void onClose(Dialog dialog) {
					// TODO Auto-generated method
					// stub
					dialog.dismiss();
				}
			});
			return;
		}

	}

	private void loginNormal() {
		AccountModelManager.LoginNormal(self,
				SignupActivity.registerAccount.getUsername(),
				SignupActivity.registerAccount.getPassword(), true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						Toast.makeText(self,
								getString(R.string.login_successfull),
								Toast.LENGTH_SHORT).show();
						// parse result :
						try {
							ParserUtility.parseJsonLoginSuccess(self, json);
							// go to home
							// gotoActivity(self, HomeActivity.class,
							// Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getAccountInfoAfterLogin(HomeActivity.class, null);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							loginNormal();
						}

					}

					@Override
					public void onError(int statusCode, String json) {
						// TODO Auto-generated method stub
						if (statusCode == AsyncHttpBase.RESPONSE_ERROR) {
							String message = getString(R.string.error_default_message);
							showMessageDialog(message, new DialogListener() {

								@Override
								public void onClose(Dialog dialog) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
							return;
						}

						if (statusCode == AsyncHttpBase.AUTHORIZATION) {
							String message = getString(R.string.error_login_invalid);
							showMessageDialog(message, new DialogListener() {

								@Override
								public void onClose(Dialog dialog) {
									// TODO Auto-generated method stub
									dialog.dismiss();
								}
							});
							return;
						}

					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub

					}
				});
	}

	private void loginExternal() {
		final ProgressHUD prg = ProgressHUD.show(context, "", true, false,
				new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						// TODO Auto-generated method stub

					}
				});
		AccountModelManager.loginSocial(self,
				SignupActivity.registerAccount.getExternalType(),
				SignupActivity.registerAccount.getExternalAccessToken(), true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						try {
							if (prg != null)
								prg.dismiss();
							Toast.makeText(self,
									getString(R.string.login_successfull),
									Toast.LENGTH_SHORT).show();
							ParserUtility.parseJsonLoginSuccess(self, json);
							// go to home
							// gotoActivity(self, HomeActivity.class,
							// Intent.FLAG_ACTIVITY_CLEAR_TOP);
							getAccountInfoAfterLogin(HomeActivity.class, null);

						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onError(int statusCode, String json) {
						// TODO Auto-generated method stub
						if (statusCode == AsyncHttpBase.RESPONSE_ERROR) {
							String message = getString(R.string.error_default_message);
							showMessageDialog(message, new DialogListener() {

								@Override
								public void onClose(Dialog dialog) {
									dialog.dismiss();
								}
							});
							return;
						}

						if (statusCode == AsyncHttpBase.AUTHORIZATION) {
							String message = getString(R.string.error_login_invalid);
							showMessageDialog(message, new DialogListener() {

								@Override
								public void onClose(Dialog dialog) {
									// TODO
									// Auto-generated
									// method stub
									dialog.dismiss();
								}
							});
							return;
						}
						prg.dismiss();
					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub
						prg.dismiss();
					}
				});
	}

}
