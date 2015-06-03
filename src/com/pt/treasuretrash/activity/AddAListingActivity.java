package com.pt.treasuretrash.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TableRow;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.adapter.ImagePagerAdapter;
import com.pt.treasuretrash.adapter.LocationSearchAdapter;
import com.pt.treasuretrash.base.TreasureTrashBaseActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.inapppurchase.IabHelper;
import com.pt.treasuretrash.inapppurchase.IabResult;
import com.pt.treasuretrash.inapppurchase.Inventory;
import com.pt.treasuretrash.inapppurchase.Purchase;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.modelmanager.ParserUtility;
import com.pt.treasuretrash.modelmanager.ProductModelManager;
import com.pt.treasuretrash.object.Category;
import com.pt.treasuretrash.object.ImageObj;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.utility.AndroidBug5497Workaround;
import com.pt.treasuretrash.utility.DialogUtility;
import com.pt.treasuretrash.utility.NetworkUtil;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.HelveticaLightTextView;
import com.pt.treasuretrash.widget.SlidingLayer;
import com.pt.treasuretrash.widget.TwoWayView;

public class AddAListingActivity extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	private final String TAG = "AddAListingActivity";
	private AddAListingActivity self;
	private ScrollView mScrAddAListing;
	private ImageView mImgClear, mImgCancelListing;
	private EditText mTxtItemTitle, mTxtDescription, mTxtPrice;
	private AutoBgButton mImgPreview;
	private LinearLayout mLlAdd3More, mLlFreeType, mLlRegularType,
			mLlQuickType, mLlPremiumType, mLlTransparent;
	private TextView mLblLocation, mLblChangeLocation, mLblCategory;
	private TextView mLblFree, mLblFreeFree, mLblRegular, mLblRegularFree,
			mLblQuick, mLblQuickFee, mLblPremium, mLblPremiumFee;
	private TextView lblAddPhotoNumber;
	private LinearLayout mLlAddPhoto;
	private TwoWayView mTwAddPhoto;
	private AddAPhotoAdapter mAddPhotoAdapter;
	private LinearLayout layoutItemTitle, llPrice;
	private RelativeLayout layoutItemPrice, layoutItemCategory, rlPager;
	private LinearLayout mLlHintTitle, mLlHintDescription, mLlLocation;
	private RelativeLayout mRltHintTile, mRltHintDesc;
	private TextView mLblHintTitleLeft, mLblHintTitleRight, mLblHintDescLeft,
			mLblHintDescRight;
	private HelveticaLightTextView mLblCurrency;
	private static Activity instance;

	private String TYPE_SELECTED = Item.TYPE_REGULAR;

	private SlidingLayer mSllChangeLocation;
	private TextView mLblLocationChanged, mLblDone, lblCurrentLocation;
	private LocationObj locObject;
	private GoogleMap maps;
	private EditText mTxtSearchLocation;
	private ImageView mImgSearchLocation;
	public static ArrayList<ImageObj> arrLargeImage;
	private ArrayList<ImageObj> arrSmallImage;
	private ImagePagerAdapter mImgAdapter;
	private ViewPager mImagePager;
	private int countAddMore = 0;
	public static Item item;
	public static int addPhotoPosition;
	public static int numImagesToUpload;

	private Category mCategory;

	private AQuery aq;
	private String action = "";

	private ArrayList<LocationObj> mArrLocationSearch;
	private LocationSearchAdapter mLocationAdapter;
	private ListView mLvLocationSearch;

	public static boolean isNew = false;
	public static HashMap<Integer, Boolean> mapImageFailed;
	public ArrayList<Integer> listUploadedIndex;

	// for purchase
	// static final String KEY_ADD_MORE = "android.test.purchased";
	private static final int RC_REQUEST = 10001;
	private IabHelper mHelper;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		// GlobalValue.constructor(self);
		setContentView(R.layout.activity_add_a_listing);
		AndroidBug5497Workaround.assistActivity(this, null);

		numImagesToUpload = 0;
		NetworkUtil.enableStrictMode();
		initInAppPurchase();
		self = this;
		instance = this;
		aq = new AQuery(self);
		item = null;
		mapImageFailed = new HashMap<Integer, Boolean>();
		listUploadedIndex = new ArrayList<Integer>();

		initUI();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(GlobalValue.KEY_ACTION)) {
				action = bundle.getString(GlobalValue.KEY_ACTION);
				try {
					item = (Item) GlobalValue.currentItem.clone();
				} catch (CloneNotSupportedException e) {
					// TODO Auto-generated catch block
					item = GlobalValue.currentItem;
				}
				locObject = item.getLocation();
			}
		}
		// create new item
		if (item == null) {
			initNewItem();
		} else {
			mLlHintTitle.setVisibility(View.GONE);
			mLlHintDescription.setVisibility(View.GONE);
		}
		initListImages();
		initItem();
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initControl();
			}
		}, 1000);

		modifyLayoutPrice();
	}
	
	private void modifyLayoutPrice(){
		
		if (mTxtPrice.getText().toString().length() > 0 && !mTxtPrice.getText().toString().isEmpty()) {
			mTxtPrice.setHint("");
			mLblCurrency.setVisibility(View.VISIBLE);
			mLblCurrency.setText(locObject.getCurrencySymbol());
		} else {
			mTxtPrice.setHint("Price (" + locObject.getCurrencySymbol()
					+ ")");
			mLblCurrency.setVisibility(View.GONE);
		}
		
		if (locObject.getCurrencySymbol().equalsIgnoreCase(
				GlobalValue.CURRENCY_MAP.get("VND"))) {
			RelativeLayout.LayoutParams paramsLblCurrency = new RelativeLayout.LayoutParams(
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			paramsLblCurrency.addRule(RelativeLayout.CENTER_IN_PARENT);
			paramsLblCurrency.addRule(RelativeLayout.RIGHT_OF, R.id.ll_price);
			mLblCurrency.setLayoutParams(paramsLblCurrency);
			
			RelativeLayout.LayoutParams paramsLlPrice = new RelativeLayout.LayoutParams(
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			paramsLlPrice.addRule(RelativeLayout.CENTER_IN_PARENT);
			paramsLlPrice.addRule(RelativeLayout.RIGHT_OF, R.id.img_price);
			llPrice.setLayoutParams(paramsLlPrice);
		}
		else{
			RelativeLayout.LayoutParams paramsLblCurrency = new RelativeLayout.LayoutParams(
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			paramsLblCurrency.addRule(RelativeLayout.CENTER_IN_PARENT);
			paramsLblCurrency.addRule(RelativeLayout.RIGHT_OF, R.id.img_price);
			mLblCurrency.setLayoutParams(paramsLblCurrency);
			
			RelativeLayout.LayoutParams paramsLlPrice = new RelativeLayout.LayoutParams(
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT,
					android.widget.RelativeLayout.LayoutParams.WRAP_CONTENT);
			
			paramsLlPrice.addRule(RelativeLayout.CENTER_IN_PARENT);
			paramsLlPrice.addRule(RelativeLayout.RIGHT_OF, R.id.lbl_currency);
			llPrice.setLayoutParams(paramsLlPrice);
		}
	}

	private void initListImages() {
		arrLargeImage = new ArrayList<ImageObj>();
		arrSmallImage = new ArrayList<ImageObj>();
		// Init pager.
		// if (arrLargeImage.size() > 0) {
		mImgAdapter = new ImagePagerAdapter(self, arrLargeImage);
		mImagePager.setAdapter(mImgAdapter);
		// }
		// Init small image
		mAddPhotoAdapter = new AddAPhotoAdapter(self, arrSmallImage);
		mTwAddPhoto.setAdapter(mAddPhotoAdapter);
	}

	public static Activity getInstance() {
		return instance;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		// reset layout
		if (mTxtItemTitle.getText().length() > 0) {
			mLlHintTitle.setVisibility(View.GONE);
		}
		if (mTxtDescription.getText().length() > 0) {
			mLlHintDescription.setVisibility(View.GONE);
		}

		// Set width/height pager
		mImagePager.getLayoutParams().width = screenWidth;
		mImagePager.getLayoutParams().height = screenWidth;

		addImagesMore();

		// Upload image
		if (isNew) {
			mImgAdapter.notifyDataSetChanged();
			if (mImagePager.getChildCount() > 0) {
				mImagePager.setCurrentItem(0);
			}
			uploadImage(addPhotoPosition);
			isNew = false;
		}
	}

	private void uploadImage(final int imageIndex) {
		if (!listUploadedIndex.contains(imageIndex)) {
			numImagesToUpload++;
			listUploadedIndex.add(imageIndex);
		}
		// Convert bitmap to file
		File filesDir = getApplicationContext().getFilesDir();

		File imageFile = new File(filesDir, "photo"
				+ arrLargeImage.get(arrLargeImage.size() - 1) + ".jpg");
		OutputStream os;
		try {
			os = new FileOutputStream(imageFile);
			arrLargeImage.get(arrLargeImage.size() - 1).getBitmap()
					.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
		}

		ProductModelManager.postFile(self, imageFile, false,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						Log.e(TAG, "Uploaded image: " + json);
						try {
							JSONArray arrImage = new JSONArray(json);
							arrLargeImage.get(arrLargeImage.size() - 1)
									.setImageUrl(arrImage.getString(0));

							AddAListingActivity.item
									.setArrImages(arrLargeImage);

							for (int i = 0; i < arrLargeImage.size(); i++) {
								Log.e(TAG, "Image[" + i + "] = "
										+ arrLargeImage.get(i).getImageUrl());
							}
							isNew = false;

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onError(int statusCode, String json) {
						DialogUtility.showShortToast(self, "Image upload fail");
						mapImageFailed.put(imageIndex, false);
					}

					@Override
					public void onError() {
						DialogUtility.showShortToast(self, "Image upload fail");
						mapImageFailed.put(imageIndex, false);
					}
				});
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();

	}

	@Override
	protected void onDestroy() {

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}
		instance = null;
		super.onDestroy();
	}

	@Override
	public void onBackPressed() {
		if (mSllChangeLocation.isOpened()) {
			mSllChangeLocation.closeLayer(true);
		} else {
			showCancelCreationListing();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (mHelper == null)
			return;

		// Pass on the activity result to the helper for handling
		if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
			super.onActivityResult(requestCode, resultCode, data);
		} else {
			Log.d(TAG, "onActivityResult handled by IABUtil.");
		}
	}

	private void increaseSlots() {
		countAddMore++;
		addImagesMore();
	}

	private void addImagesMore() {
		arrSmallImage.clear();
		arrSmallImage.addAll(arrLargeImage);

		for (int i = 0; i < (countAddMore * 3) - arrLargeImage.size(); i++) {
			arrSmallImage.add(new ImageObj());
		}

		lblAddPhotoNumber.setText(getString(R.string.add_up_to) + " "
				+ arrSmallImage.size() + " photos");
		item.setSlotImage(arrSmallImage.size());
		mAddPhotoAdapter.setExistedUserImage(true);
		mAddPhotoAdapter.notifyDataSetChanged();
		mImgAdapter.notifyDataSetChanged();
	}

	private void initUI() {
		// RelativeLayout.LayoutParams pagerParams = new
		// RelativeLayout.LayoutParams(
		// RelativeLayout.LayoutParams.MATCH_PARENT,
		// RelativeLayout.LayoutParams.WRAP_CONTENT);
		// pagerParams.setMargins(0,
		// (int)getResources().getDimension(R.dimen.height_listing_header)/2, 0,
		// 0);
		// rlPager.setLayoutParams(pagerParams);

		mImagePager = (ViewPager) findViewById(R.id.pager);
		mScrAddAListing = (ScrollView) findViewById(R.id.scr_add_a_listing);
		mImgClear = (ImageView) findViewById(R.id.img_clear);
		mTxtItemTitle = (EditText) findViewById(R.id.txt_item_title);
		mTxtDescription = (EditText) findViewById(R.id.txt_description);
		mTxtPrice = (EditText) findViewById(R.id.txt_price);
		mLlAdd3More = (LinearLayout) findViewById(R.id.ll_add_3_more);
		mLblCategory = (TextView) findViewById(R.id.lbl_category);
		mLblLocation = (TextView) findViewById(R.id.lbl_location);
		mLblChangeLocation = (TextView) findViewById(R.id.lbl_change_location);
		mImgCancelListing = (ImageView) findViewById(R.id.img_cancel_listing);
		mImgPreview = (AutoBgButton) findViewById(R.id.img_preview_listing);
		mLblFree = (TextView) findViewById(R.id.lbl_free);
		mLblFreeFree = (TextView) findViewById(R.id.lbl_free_free);
		mLblRegular = (TextView) findViewById(R.id.lbl_regular);
		mLblRegularFree = (TextView) findViewById(R.id.lbl_regular_free);
		mLblQuick = (TextView) findViewById(R.id.lbl_quick);
		mLblQuickFee = (TextView) findViewById(R.id.lbl_quick_fee);
		mLblPremium = (TextView) findViewById(R.id.lbl_premium);
		mLblPremiumFee = (TextView) findViewById(R.id.lbl_premium_fee);
		mLlFreeType = (LinearLayout) findViewById(R.id.ll_free);
		mLlRegularType = (LinearLayout) findViewById(R.id.ll_regular);
		typeSelected(mLlRegularType, mLblRegular, mLblRegularFree);
		mLlQuickType = (LinearLayout) findViewById(R.id.ll_quick);
		mLlPremiumType = (LinearLayout) findViewById(R.id.ll_premium);
		mTwAddPhoto = (TwoWayView) findViewById(R.id.tw_add_photo);
		mLlAddPhoto = (LinearLayout) findViewById(R.id.ll_add_photo);
		layoutItemTitle = (LinearLayout) findViewById(R.id.layoutTitle);
		layoutItemPrice = (RelativeLayout) findViewById(R.id.layoutPrice);
		layoutItemCategory = (RelativeLayout) findViewById(R.id.layoutCategory);
		lblAddPhotoNumber = (TextView) findViewById(R.id.lblPhotoNumber);
		mLblCurrency = (HelveticaLightTextView) findViewById(R.id.lbl_currency);
		lblCurrentLocation = (TextView) findViewById(R.id.lblCurrentLocation);
		mLlLocation = (LinearLayout) findViewById(R.id.ll_location);
		llPrice = (LinearLayout) findViewById(R.id.ll_price);

		mLlHintDescription = (LinearLayout) findViewById(R.id.ll_hint_description);
		mLlHintTitle = (LinearLayout) findViewById(R.id.ll_hint_title);
		mRltHintDesc = (RelativeLayout) findViewById(R.id.rlt_description);
		mRltHintTile = (RelativeLayout) findViewById(R.id.rlt_hint_title);
		mLblHintDescLeft = (TextView) findViewById(R.id.lbl_hint_desc_left);
		mLblHintDescRight = (TextView) findViewById(R.id.lbl_hint_desc_right);
		mLblHintTitleLeft = (TextView) findViewById(R.id.lbl_hint_title_left);
		mLblHintTitleRight = (TextView) findViewById(R.id.lbl_hint_title_right);

		mLlTransparent = (LinearLayout) findViewById(R.id.llTransparent);
		mLblLocationChanged = (TextView) findViewById(R.id.lbl_location_changed);
		mLblLocationChanged.setText(mLblLocation.getText().toString());
		mLblLocationChanged.setSelected(true);
		mLblDone = (TextView) findViewById(R.id.lbl_done);
		mTxtSearchLocation = (EditText) findViewById(R.id.txt_type_location);
		mImgSearchLocation = (ImageView) findViewById(R.id.img_search_location);
		mSllChangeLocation = (SlidingLayer) findViewById(R.id.sll_change_location);
		mSllChangeLocation.setStickTo(SlidingLayer.STICK_TO_RIGHT);
		mSllChangeLocation.setCloseOnTapEnabled(false);
		mLvLocationSearch = (ListView) findViewById(R.id.lv_location_search);

	}

	private void initNewItem() {
		item = new Item();
		mLlHintTitle.setVisibility(View.VISIBLE);
		mLlHintDescription.setVisibility(View.VISIBLE);
		try {
			if (locObject == null) {
				locObject = (LocationObj) GlobalValue.myAccount.getLocation()
						.clone();
			}
		} catch (Exception ex) {
			GlobalValue.myAccount = ParserUtility
					.parseAccount(
							pref.getStringValue(TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON),
							pref);
			locObject = GlobalValue.myAccount.getLocation();
		}
		item.setLocation(locObject);
		item.setType(Item.TYPE_REGULAR);
		item.setCost(0);
		item.setSlotImage(3);
	}

	private void initControl() {
		mImgClear.setOnClickListener(this);
		mLlAdd3More.setOnClickListener(this);
		mLblCategory.setOnClickListener(this);
		mLblChangeLocation.setOnClickListener(this);
		mLlFreeType.setOnClickListener(this);
		mLlPremiumType.setOnClickListener(this);
		mLlQuickType.setOnClickListener(this);
		mLlRegularType.setOnClickListener(this);
		mImgCancelListing.setOnClickListener(this);
		mImgPreview.setOnClickListener(this);
		lblCurrentLocation.setOnClickListener(this);
		layoutItemPrice.setOnClickListener(this);

		// Take a photo listener
		mTwAddPhoto.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				addPhotoPosition = position;
				takeAPhoto();
			}
		});

		mTxtItemTitle.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				// int color = Color.parseColor(getString(R.color.white));
				// mTxtItemTitle.setBackgroundColor(color);
				// layoutItemTitle.setBackgroundColor(color);

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0) {
					mLlHintTitle.setVisibility(View.GONE);
					layoutItemTitle.setBackgroundResource(R.color.white);
					// mTxtItemTitle.setBackgroundColor(Color
					// .parseColor(getString(R.color.white)));
				} else {
					mLblHintTitleLeft.setTextColor(getResources().getColor(
							R.color.text_color_listing));
					mLblHintTitleRight.setTextColor(getResources().getColor(
							R.color.text_color_listing));
					mLlHintTitle.setVisibility(View.VISIBLE);
					// layoutItemTitle
					// .setBackgroundResource(R.drawable.bg_error_layout);
				}
			}
		});

		mTxtDescription.setOnTouchListener(new View.OnTouchListener() {

			@Override
			public boolean onTouch(final View v, final MotionEvent motionEvent) {
				if (v.getId() == R.id.txt_description) {
					v.getParent().requestDisallowInterceptTouchEvent(true);
					switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_UP:
						v.getParent().requestDisallowInterceptTouchEvent(false);
						break;
					}
				}
				return false;
			}
		});

		mTxtDescription.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (s.toString().length() > 0) {
					mLlHintDescription.setVisibility(View.GONE);
					mRltHintDesc.setBackgroundResource(R.color.white);
					mLblHintDescLeft.setTextColor(getResources().getColor(
							R.color.text_color_listing));
					mLblHintDescRight.setTextColor(getResources().getColor(
							R.color.text_color_listing));
				} else {
					mLlHintDescription.setVisibility(View.VISIBLE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				// int color = Color.parseColor(getString(R.color.white));
				// mRltHintDesc.setBackgroundColor(color);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// if (s.toString().length() > 11) {
				// int color = Color.parseColor(getString(R.color.white));
				// mRltHintDesc.setBackgroundColor(color);
				// } else {
				// mRltHintDesc
				// .setBackgroundResource(R.drawable.bg_error_layout_desc);
				// }
			}
		});

		mTxtPrice.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int color = Color.parseColor(getString(R.color.white));

				mTxtPrice.setBackgroundColor(getResources().getColor(
						R.color.transparent));
				mTxtPrice.setHintTextColor(Color
						.parseColor(getString(R.color.text_color_listing)));
				layoutItemPrice
						.setBackgroundResource(R.drawable.bg_border_gray_price_n_cate);
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.toString().length() > 0 && !s.toString().isEmpty()) {
					mTxtPrice.setHint("");
					mLblCurrency.setVisibility(View.VISIBLE);
					mLblCurrency.setText(locObject.getCurrencySymbol());
				} else {
					mTxtPrice.setHint("Price (" + locObject.getCurrencySymbol()
							+ ")");
					mLblCurrency.setVisibility(View.GONE);
				}
			}
		});

		// Choose type
		mLlFreeType.setOnClickListener(this);
		mLlRegularType.setOnClickListener(this);
		mLlQuickType.setOnClickListener(this);
		mLlPremiumType.setOnClickListener(this);

		// Search location
		mLlTransparent.setOnClickListener(this);
		mImgSearchLocation.setOnClickListener(this);
		mLblDone.setOnClickListener(this);

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
	}

	private void initItem() {
		if (action.equals(GlobalValue.KEY_ACTION_EDIT_ITEM)) {
			ImageObj image = null;
			int imageSize = item.getArrImages().size();
			Log.e("Edit item", "total image size :" + item.getSlotImage());
			Log.e("Edit item", "total existed size :" + imageSize);
			// add image existed
			mImagePager.setBackgroundDrawable(new BitmapDrawable(aq
					.getCachedImage(item.getImage())));
			for (int i = 0; i < imageSize; i++) {
				image = item.getArrImages().get(i);
				arrSmallImage.add(image);
				arrLargeImage.add(image);
			}
			// add avalable slot image
			for (int i = 0; i < item.getSlotImage() - imageSize; i++) {
				image = new ImageObj();
				arrSmallImage.add(image);
			}

			int totalSlot = item.getSlotImage();
			countAddMore = totalSlot % 3 != 0 ? totalSlot / 3 + 1
					: totalSlot / 3;

			lblAddPhotoNumber.setText(getString(R.string.add_up_to) + " "
					+ arrSmallImage.size() + " photos");
			mImgAdapter.notifyDataSetChanged();
			mAddPhotoAdapter.notifyDataSetChanged();

			mTxtItemTitle.setText(item.getTitle());
			mTxtDescription.setText(item.getDescription());
			mLblCategory.setText(item.getCategory().getName());
			mCategory = item.getCategory();
			mLblCurrency.setText(item.getCurrency());
		} else {
			increaseSlots();
		}

		mLblLocation.setText(item.getLocation().getLocationAddress());
		changeLocationWidth();
		if (item.getType().equals(Item.TYPE_FREE)) {
			typeSelected(mLlFreeType, mLblFree, mLblFreeFree);
			mTxtPrice.setEnabled(false);
			TYPE_SELECTED = Item.TYPE_FREE;
			mTxtPrice.setText("");
			mTxtPrice.setHint("Free");
			mLblCurrency.setVisibility(View.GONE);

			typeUnSelected(mLlRegularType, mLblRegular, mLblRegularFree);
			typeUnSelected(mLlQuickType, mLblQuick, mLblQuickFee);
			typeUnSelected(mLlPremiumType, mLblPremium, mLblPremiumFee);

		} else if (item.getType().equals(Item.TYPE_REGULAR)) {
			typeSelected(mLlRegularType, mLblRegular, mLblRegularFree);
			TYPE_SELECTED = Item.TYPE_REGULAR;
			mTxtPrice.setEnabled(true);
			if (item.getCost() != 0) {
				mTxtPrice.setHint("");
				mTxtPrice.setText(item.getCost() + "");
				mLblCurrency.setVisibility(View.VISIBLE);
			} else {
				mTxtPrice.setText("");
				mTxtPrice.setHint("Price (" + locObject.getCurrencySymbol()
						+ ")");
				mLblCurrency.setVisibility(View.GONE);
			}

			typeUnSelected(mLlFreeType, mLblFree, mLblFreeFree);
			typeUnSelected(mLlQuickType, mLblQuick, mLblQuickFee);
			typeUnSelected(mLlPremiumType, mLblPremium, mLblPremiumFee);

		} else if (item.getType().equals(Item.TYPE_QUICK)) {
			typeSelected(mLlQuickType, mLblQuick, mLblQuickFee);
			TYPE_SELECTED = Item.TYPE_QUICK;
			mTxtPrice.setEnabled(true);
			if (item.getCost() != 0) {
				mTxtPrice.setHint("");
				mTxtPrice.setText(item.getCost() + "");
				mLblCurrency.setVisibility(View.VISIBLE);
			} else {
				mTxtPrice.setText("");
				mTxtPrice.setHint("Price (" + locObject.getCurrencySymbol()
						+ ")");
				mLblCurrency.setVisibility(View.GONE);
			}

			typeUnSelected(mLlRegularType, mLblRegular, mLblRegularFree);
			typeUnSelected(mLlFreeType, mLblFree, mLblFreeFree);
			typeUnSelected(mLlPremiumType, mLblPremium, mLblPremiumFee);

		} else if (item.getType().equals(Item.TYPE_PREMIUM)) {
			typeSelected(mLlPremiumType, mLblPremium, mLblPremiumFee);
			TYPE_SELECTED = Item.TYPE_PREMIUM;
			mTxtPrice.setEnabled(true);
			if (item.getCost() != 0) {
				mTxtPrice.setHint("");
				mTxtPrice.setText(item.getCost() + "");
				mLblCurrency.setVisibility(View.VISIBLE);
			} else {
				mTxtPrice.setText("");
				mTxtPrice.setHint("Price (" + locObject.getCurrencySymbol()
						+ ")");
				mLblCurrency.setVisibility(View.GONE);
			}

			typeUnSelected(mLlRegularType, mLblRegular, mLblRegularFree);
			typeUnSelected(mLlQuickType, mLblQuick, mLblQuickFee);
			typeUnSelected(mLlFreeType, mLblFree, mLblFreeFree);
		}

	}

	private void typeSelected(LinearLayout layout, TextView lblType,
			TextView lblFee) {
		layout.setBackgroundColor(getResources().getColor(
				R.color.type_color_selected));
		lblType.setTextColor(getResources().getColor(R.color.white));
		lblFee.setTextColor(getResources().getColor(R.color.white));
	}

	private void typeUnSelected(LinearLayout layout, TextView lblType,
			TextView lblFee) {
		layout.setBackgroundColor(getResources().getColor(R.color.white));
		lblType.setTextColor(getResources()
				.getColor(R.color.text_color_listing));
		lblFee.setTextColor(getResources().getColor(R.color.text_color_listing));
	}

	@Override
	public void onClick(View v) {
		if (v == mImgClear) {
			if (!mTxtItemTitle.getText().toString().equals("")) {
				mTxtItemTitle.setText("");
			}
		} else if (v == mLblCategory || v == layoutItemCategory) {
			showCategoriesDialog();
		} else if (v == mImgCancelListing) {
			showCancelCreationListing();
		} else if (v == mLblChangeLocation || v == mLlTransparent) {
			changeLocation();
		} else if (v == mImgSearchLocation) {
			searchListLocation(mTxtSearchLocation.getText().toString());
		} else if (v == mLblDone) {
			if (mSllChangeLocation.isOpened()) {
				mSllChangeLocation.closeLayer(true);

				// Set text
				mLblLocation.setText(mLblLocationChanged.getText().toString());

				// Change location textview width
				changeLocationWidth();

				// Change currency
				if (mTxtPrice.getText().toString().length() == 0) {
					mTxtPrice.setHint("Price(" + locObject.getCurrencySymbol()
							+ ")");
				}
				mLblCurrency.setText(locObject.getCurrencySymbol());
				modifyLayoutPrice();
			}
		} else if (v == mLlAdd3More) {
			// Purchase an item here
			purchaseItem(Item.KEY_ADD_3_PHOTOS, "");
		} else if (v == mLlFreeType) {
			if (!TYPE_SELECTED.equals(Item.TYPE_FREE)) {
				typeSelected(mLlFreeType, mLblFree, mLblFreeFree);
				if (!mTxtPrice.getText().toString().equals("")) {
					mTxtPrice.setText("");
				}
				mTxtPrice.setHint("Free");
				mTxtPrice.setHintTextColor(Color
						.parseColor(getString(R.color.text_color_listing)));
				mTxtPrice.setEnabled(false);
				TYPE_SELECTED = Item.TYPE_FREE;

				typeUnSelected(mLlRegularType, mLblRegular, mLblRegularFree);
				typeUnSelected(mLlQuickType, mLblQuick, mLblQuickFee);
				typeUnSelected(mLlPremiumType, mLblPremium, mLblPremiumFee);
			}

		} else if (v == mLlRegularType) {
			if (!TYPE_SELECTED.equals(Item.TYPE_REGULAR)) {
				typeSelected(mLlRegularType, mLblRegular, mLblRegularFree);
				if (mTxtPrice.getText().toString().isEmpty()) {
					mTxtPrice.setHint("Price (" + locObject.getCurrencySymbol()
							+ ")");
				} else {
					mTxtPrice.setHint("");
				}
				mTxtPrice.setHintTextColor(Color
						.parseColor(getString(R.color.text_color_listing)));
				mTxtPrice.setEnabled(true);
				TYPE_SELECTED = Item.TYPE_REGULAR;

				typeUnSelected(mLlFreeType, mLblFree, mLblFreeFree);
				typeUnSelected(mLlQuickType, mLblQuick, mLblQuickFee);
				typeUnSelected(mLlPremiumType, mLblPremium, mLblPremiumFee);
			}

		} else if (v == mLlQuickType) {
			if (!TYPE_SELECTED.equals(Item.TYPE_QUICK)) {
				typeSelected(mLlQuickType, mLblQuick, mLblQuickFee);
				if (mTxtPrice.getText().toString().isEmpty()) {
					mTxtPrice.setHint("Price (" + locObject.getCurrencySymbol()
							+ ")");
				} else {
					mTxtPrice.setHint("");
				}
				mTxtPrice.setHintTextColor(Color
						.parseColor(getString(R.color.text_color_listing)));
				mTxtPrice.setEnabled(true);
				TYPE_SELECTED = Item.TYPE_QUICK;

				typeUnSelected(mLlRegularType, mLblRegular, mLblRegularFree);
				typeUnSelected(mLlFreeType, mLblFree, mLblFreeFree);
				typeUnSelected(mLlPremiumType, mLblPremium, mLblPremiumFee);
			}

		} else if (v == mLlPremiumType) {
			if (!TYPE_SELECTED.equals(Item.TYPE_PREMIUM)) {
				typeSelected(mLlPremiumType, mLblPremium, mLblPremiumFee);
				if (mTxtPrice.getText().toString().isEmpty()) {
					mTxtPrice.setHint("Price (" + locObject.getCurrencySymbol()
							+ ")");
				} else {
					mTxtPrice.setHint("");
				}
				mTxtPrice.setHintTextColor(Color
						.parseColor(getString(R.color.text_color_listing)));
				mTxtPrice.setEnabled(true);
				TYPE_SELECTED = Item.TYPE_PREMIUM;

				typeUnSelected(mLlRegularType, mLblRegular, mLblRegularFree);
				typeUnSelected(mLlQuickType, mLblQuick, mLblQuickFee);
				typeUnSelected(mLlFreeType, mLblFree, mLblFreeFree);
			}
		} else if (v == mImgPreview) {
			if (action.equals(GlobalValue.KEY_ACTION_EDIT_ITEM)) {
				previewListing();
			} else {
				previewListing();
			}
		} else if (v == lblCurrentLocation) {
			getCurrentLocation();
		} else if (v == layoutItemPrice) {
			mTxtPrice.requestFocus();
			showSoftKeyboard(mTxtPrice);
		}
	}

	private void changeLocationWidth() {
		mLblLocation.measure(0, 0);
		mLblChangeLocation.measure(0, 0);
		int widthLocation = mLblLocation.getMeasuredWidth();
		int widthLayoutLocation = (int) (self.screenWidth
				- (getResources().getDimension(R.dimen.margin_padding_normal) * 5) - getResources()
				.getDimension(R.dimen.width_icon_xtiny));
		int widthChangeLocation = mLblChangeLocation.getMeasuredWidth();

		if (widthLayoutLocation - widthLocation < widthChangeLocation) {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					widthLayoutLocation - widthChangeLocation,
					RelativeLayout.LayoutParams.MATCH_PARENT);
			params.setMargins(
					(int) (getResources()
							.getDimension(R.dimen.width_icon_xtiny) + getResources()
							.getDimension(R.dimen.margin_padding_normal)), 0,
					(int) (getResources()
							.getDimension(R.dimen.margin_padding_normal)), 0);
			mLblLocation.setLayoutParams(params);
		} else {
			RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
					widthLocation, RelativeLayout.LayoutParams.MATCH_PARENT);
			params.setMargins(
					(int) (getResources()
							.getDimension(R.dimen.width_icon_xtiny) + getResources()
							.getDimension(R.dimen.margin_padding_normal)), 0,
					(int) (getResources()
							.getDimension(R.dimen.margin_padding_normal)), 0);
			mLblLocation.setLayoutParams(params);
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

	private void previewListing() {
		// validate form
		boolean isValidTitle = true, isValidDescription = true, isValidPrice = true, isValidCategory = true, isValidImage = true;
		String title = mTxtItemTitle.getText().toString();

		String description = mTxtDescription.getText().toString();
		String price = mTxtPrice.getText().toString();

		// valid title
		if (title.isEmpty()) {
			mTxtItemTitle.setText("");
			layoutItemTitle
					.setBackgroundResource(R.drawable.bg_error_layout_title);
			mLblHintTitleLeft.setTextColor(Color.RED);
			mLblHintTitleRight.setTextColor(Color.RED);
			// mTxtItemTitle.setBackgroundColor(Color
			// .parseColor(getString(R.color.bg_color_error_editext)));

			// Go to top of screen
			mScrAddAListing.fullScroll(View.FOCUS_UP);
			mScrAddAListing.pageScroll(View.FOCUS_UP);

			// Focus on title
			mTxtItemTitle.requestFocus();
			showSoftKeyboard(mTxtItemTitle);
			isValidTitle = false;
		} else if (title.length() > 25) {
			mTxtItemTitle.setText("");
			layoutItemTitle.setBackgroundResource(R.drawable.bg_error_layout);
			mLblHintTitleLeft.setTextColor(Color.RED);
			mLblHintTitleRight.setTextColor(Color.RED);
			mTxtItemTitle.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			isValidTitle = false;
		} else {
			layoutItemTitle.setBackgroundResource(R.color.white);
			mTxtItemTitle.setBackgroundColor(Color
					.parseColor(getString(R.color.white)));
		}
		// valid description
		if (description.isEmpty()) {
			mLblHintDescLeft.setTextColor(Color.RED);
			mLblHintDescRight.setTextColor(Color.RED);
			mRltHintDesc.setBackgroundResource(R.drawable.bg_error_layout_desc);
			isValidDescription = false;
		} else if (description.length() < 12) {
			mLblHintDescLeft.setTextColor(Color.RED);
			mLblHintDescRight.setTextColor(Color.RED);
			mRltHintDesc.setBackgroundResource(R.drawable.bg_error_layout_desc);
			isValidDescription = false;

			// Show alert when description is short(Little than 12 characters).

			showMessageDialog(getString(R.string.add_some_more_description),
					new DialogListener() {

						@Override
						public void onClose(Dialog dialog) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
		} else {
			mRltHintDesc.setBackgroundResource(R.color.white);
		}
		// valid price
		if (!TYPE_SELECTED.equals(Item.TYPE_FREE)) {
			if (price.isEmpty()) {
				mTxtPrice.setText("");
				mTxtPrice.setHintTextColor(Color.RED);
				layoutItemPrice
						.setBackgroundResource(R.drawable.bg_error_layout_price_n_cate);

				int bottom = mTxtPrice.getPaddingBottom();
				int top = mTxtPrice.getPaddingTop();
				int right = mTxtPrice.getPaddingRight();
				int left = mTxtPrice.getPaddingLeft();

				mTxtPrice
						.setBackgroundResource(R.drawable.bg_et_price_error_validation);
				mTxtPrice.setPadding(left, top, right, bottom);
				isValidPrice = false;
			}
			if(!isInteger(price)){
				mTxtPrice.setHintTextColor(Color.RED);
				layoutItemPrice
						.setBackgroundResource(R.drawable.bg_error_layout_price_n_cate);

				int bottom = mTxtPrice.getPaddingBottom();
				int top = mTxtPrice.getPaddingTop();
				int right = mTxtPrice.getPaddingRight();
				int left = mTxtPrice.getPaddingLeft();

				mTxtPrice
						.setBackgroundResource(R.drawable.bg_et_price_error_validation);
				mTxtPrice.setPadding(left, top, right, bottom);
				isValidPrice = false;
				
				showMessageDialog(getString(R.string.message_alert_integer_price), new DialogListener() {
					
					@Override
					public void onClose(Dialog dialog) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		}

		// valid category
		if (mCategory == null) {
			layoutItemCategory
					.setBackgroundResource(R.drawable.bg_error_layout_price_n_cate);
			mLblCategory.setTextColor(Color.RED);
			isValidCategory = false;
		}

		// valid select image
		if (arrLargeImage.size() > 0) {
			mAddPhotoAdapter.setExistedUserImage(true);
			mAddPhotoAdapter.notifyDataSetChanged();
			isValidImage = true;
		} else {
			mAddPhotoAdapter.setExistedUserImage(false);
			mAddPhotoAdapter.notifyDataSetChanged();
			isValidImage = false;
		}

		// Init item
		if (isValidTitle && isValidDescription && isValidPrice
				&& isValidCategory && isValidImage) {

			item.setTitle(mTxtItemTitle.getText().toString());
			item.setDescription(description);

			item.setType(TYPE_SELECTED);
			if (!TYPE_SELECTED.equals(Item.TYPE_FREE)) {
				if (TYPE_SELECTED.equals(Item.TYPE_REGULAR) && price.isEmpty()) {
					item.setCost(0);
				} else
					item.setCost(Long.valueOf(price));
			} else {
				item.setCost(0);
			}

			item.setCurrency(GlobalValue.myAccount.getLocation()
					.getCountryCode());
			item.setSlotImage(arrSmallImage.size());
			if (item.getSlotType() == null) {
				// Set slotType is Additional
				if (GlobalValue.myAccount.getNumberFreeSlotActive() == 0) {
					item.setSlotType(Item.SLOT_TYPE_ADDITIONAL);
				} else {
					Bundle bundleSlotType = getIntent().getExtras();
					if (bundleSlotType != null) {
						try {
							if (bundleSlotType
									.getString("slotType")
									.equalsIgnoreCase(Item.SLOT_TYPE_ADDITIONAL)) {
								item.setSlotType(Item.SLOT_TYPE_ADDITIONAL);
							} else if (bundleSlotType.getString("slotType")
									.equalsIgnoreCase(Item.SLOT_TYPE_DEFAULT)) {
								item.setSlotType(Item.SLOT_TYPE_DEFAULT);
							}
						} catch (NullPointerException ex) {
							item.setSlotType(Item.SLOT_TYPE_DEFAULT);
						}

					} else {
						item.setSlotType(Item.ITEM_KEY_SLOT_TYPE_DEFAULT);
					}
				}
			}
			item.setCategory(mCategory);

			if (locObject != null) {
				item.setLocation(locObject);
			} else {
				item.setLocation(GlobalValue.myAccount.getLocation());
			}
			item.setCurrency(item.getLocation().getCurrencySymbol());

			if (action.equals(GlobalValue.KEY_ACTION_EDIT_ITEM)) {
				Bundle bundle = new Bundle();
				bundle.putString(GlobalValue.KEY_ACTION,
						GlobalValue.KEY_ACTION_EDIT_ITEM);
				gotoActivity(self, PreviewListingActivity.class, bundle);
			} else {
				gotoActivity(self, PreviewListingActivity.class);
			}
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
		mTxtSearchLocation.setText("");
		if (locObject == null) {
			locObject = GlobalValue.myAccount.getLocation();
		}
		mLblLocationChanged.setText(locObject.getLocationAddress());

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

	private void searchListLocation(String input) {
		if (!input.trim().isEmpty()) {
			String url = "http://maps.google.com/maps/api/geocode/json?address="
					+ input.trim() + "&sensor=false";
			if (url.contains(" ")) {
				url = url.replace(" ", "%20");
			}
			AccountModelManager.getLocationBySearching(self, url, false,
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

	private void takeAPhoto() {
		gotoActivity(self, CameraActivity.class);
	}

	private void changeCategoryStyle() {
		layoutItemCategory
				.setBackgroundResource(R.drawable.bg_border_gray_price_n_cate);
		mLblCategory.setTextColor(Color
				.parseColor(getString(R.color.text_color_listing)));
	}

	private void showCategoriesDialog() {
		// Fix bug show category popup twice
		mLblCategory.setEnabled(false);

		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_choose_category);
		dialog.getWindow().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.bg_dialog_categories));

		LinearLayout llCategory = (LinearLayout) dialog
				.findViewById(R.id.ll_choose_category);

		String jsonCategory = pref
				.getStringValue(TreasureTrashSharedPreferences.PREF_ALL_CATEGORIES);
		ArrayList<Category> arrAllCategories = ParserUtility
				.parseCategories(jsonCategory);

		final ArrayList<Category> arrGrandParent = new ArrayList<Category>();
		ArrayList<Category> arrParent = new ArrayList<Category>();
		ArrayList<Category> arrGrandChild = new ArrayList<Category>();

		// Get grandparent list category

		for (int i = 0; i < arrAllCategories.size(); i++) {
			if (arrAllCategories.get(i).getParentCategoryId().equals("-1")) {
				arrGrandParent.add(arrAllCategories.get(i));
			}
		}

		// Get parent and grandchild list category
		for (int i = 0; i < arrAllCategories.size(); i++) {
			if (!arrAllCategories.get(i).getParentCategoryId().equals("-1")) {
				String id = arrAllCategories.get(i).getParentCategoryId();
				boolean isParent = false;
				for (int j = 0; j < arrGrandParent.size(); j++) {
					if (id.equals(arrGrandParent.get(j).getId())) {
						isParent = true;
						break;
					}
				}

				if (isParent) {
					arrParent.add(arrAllCategories.get(i));
				} else {
					arrGrandChild.add(arrAllCategories.get(i));
				}
			}
		}

		// Create layout
		for (int i = 0; i < arrGrandParent.size(); i++) {
			final int catePos = i;

			// Create grand parent layout
			TableRow rowGrandParent = new TableRow(self);
			rowGrandParent.setId(Integer
					.parseInt(arrGrandParent.get(i).getId()));
			rowGrandParent.setLayoutParams(new LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

			final HelveticaLightTextView lblGrandParent = new HelveticaLightTextView(
					self);
			lblGrandParent.setId(Integer
					.parseInt(arrGrandParent.get(i).getId()));
			lblGrandParent.setText(arrGrandParent.get(i).getName().trim());
			lblGrandParent.setTextColor(getResources().getColor(
					R.color.text_color_listing_green));
			lblGrandParent.setCompoundDrawablesWithIntrinsicBounds(
					getResources().getDrawable(R.drawable.ic_minus_2), null,
					null, null);
			lblGrandParent.setCompoundDrawablePadding(5);
			lblGrandParent.setGravity(Gravity.CENTER_VERTICAL);

			if (getResources().getBoolean(R.bool.isTablet))
				lblGrandParent.setTextSize(30);
			else
				lblGrandParent.setTextSize(18);

			lblGrandParent.setClickable(true);
			lblGrandParent.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.bg_add_photo));
			// lblGrandParent.setPadding(0, 0, 0, 10);
			lblGrandParent.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mLblCategory.setText(lblGrandParent.getText().toString());
					mCategory = arrGrandParent.get(catePos);
					dialog.dismiss();

					changeCategoryStyle();
				}
			});

			rowGrandParent.addView(lblGrandParent);
			llCategory.addView(rowGrandParent);

			// Create parent by grand parent
			final ArrayList<Category> arrParentByGrandParent = new ArrayList<Category>();
			for (int j = 0; j < arrParent.size(); j++) {
				if (arrParent.get(j).getParentCategoryId()
						.equals(arrGrandParent.get(i).getId())) {
					arrParentByGrandParent.add(arrParent.get(j));
				}
			}

			// Create parent by grand parent and child by parent layout
			Log.e(TAG, "Parent by parent: " + arrParentByGrandParent.size());
			for (int j = 0; j < arrParentByGrandParent.size(); j++) {
				final int cateParent = j;

				TableRow rowParent = new TableRow(self);
				rowParent.setId(Integer.parseInt(arrParentByGrandParent.get(j)
						.getId()));
				rowParent.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

				final HelveticaLightTextView lblParent = new HelveticaLightTextView(
						self);
				lblParent.setId(Integer.parseInt(arrParentByGrandParent.get(j)
						.getId()));
				lblParent.setText(arrParentByGrandParent.get(j).getName()
						.trim());
				lblParent.setTextColor(getResources().getColor(
						R.color.text_color_listing_green));

				if (getResources().getBoolean(R.bool.isTablet))
					lblParent.setTextSize(28.3f);
				else
					lblParent.setTextSize(17);

				lblParent.setClickable(true);
				lblParent.setBackgroundDrawable(getResources().getDrawable(
						R.drawable.bg_add_photo));
				int maginleftParent = (int) getResources().getDimension(
						R.dimen.category_parent_magin);
				lblParent.setPadding(maginleftParent, 0, 0, 5);
				lblParent.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						mLblCategory.setText(lblParent.getText().toString());
						mCategory = arrParentByGrandParent.get(cateParent);
						dialog.dismiss();

						changeCategoryStyle();
					}
				});

				rowParent.addView(lblParent);
				llCategory.addView(rowParent);

				// Create child by parent
				final ArrayList<Category> arrChildByParent = new ArrayList<Category>();
				for (int k = 0; k < arrGrandChild.size(); k++) {
					if (arrGrandChild.get(k).getParentCategoryId()
							.equals(arrParentByGrandParent.get(j).getId())) {
						arrChildByParent.add(arrGrandChild.get(k));
					}
				}

				// Create child layout
				Log.e(TAG, "Child by parent: " + arrChildByParent.size());
				for (int k = 0; k < arrChildByParent.size(); k++) {
					final int cateChild = k;

					TableRow rowChild = new TableRow(self);
					rowChild.setId(Integer.parseInt(arrChildByParent.get(k)
							.getId()));
					rowChild.setLayoutParams(new LayoutParams(
							LayoutParams.MATCH_PARENT,
							LayoutParams.WRAP_CONTENT));

					final HelveticaLightTextView lblChild = new HelveticaLightTextView(
							self);
					lblChild.setId(Integer.parseInt(arrChildByParent.get(k)
							.getId()));
					lblChild.setText(arrChildByParent.get(k).getName().trim());
					lblChild.setTextColor(getResources().getColor(
							R.color.text_color_listing_green));

					if (getResources().getBoolean(R.bool.isTablet))
						lblChild.setTextSize(26.7f);
					else
						lblChild.setTextSize(16);

					lblChild.setClickable(true);
					lblChild.setBackgroundDrawable(getResources().getDrawable(
							R.drawable.bg_add_photo));
					int maginleftChildren = (int) getResources().getDimension(
							R.dimen.category_children_magin);
					lblChild.setPadding(maginleftChildren, 0, 0, 5);
					lblChild.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							mLblCategory.setText(lblChild.getText().toString());
							mCategory = arrChildByParent.get(cateChild);
							dialog.dismiss();

							changeCategoryStyle();
						}
					});

					rowChild.addView(lblChild);
					llCategory.addView(rowChild);
				}
			}

			// Add horizontal bar when not the last item.
			if (i < arrGrandParent.size() - 1) {
				View hozBar = new View(self);
				hozBar.setLayoutParams(new LayoutParams(
						LayoutParams.MATCH_PARENT, 2));
				hozBar.setBackgroundColor(getResources().getColor(
						R.color.text_color_listing));
				hozBar.setPadding(0, 10, 0, 0);

				llCategory.addView(hozBar);
			}
		}

		// Fix bug show category popup twice
		dialog.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss(DialogInterface dialog) {
				// TODO Auto-generated method stub
				mLblCategory.setEnabled(true);
			}
		});

		// Show dialog
		dialog.show();
	}

	private void showCancelCreationListing() {
		final Dialog dialog = new Dialog(self);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.dialog_cancel_listing);
		dialog.getWindow().setBackgroundDrawable(
				new ColorDrawable(android.graphics.Color.TRANSPARENT));

		RelativeLayout rltCancel = (RelativeLayout) dialog
				.findViewById(R.id.rlt_cancel);
		rltCancel.getLayoutParams().width = screenWidth;
		rltCancel.getLayoutParams().height = screenHeight;

		TextView lblTitle = (TextView) dialog.findViewById(R.id.lblTitle);
		TextView lblContent = (TextView) dialog.findViewById(R.id.lblContent);

		lblTitle.setText("Are you sure you want to\ncancel this? You can't undo.");
		lblContent.setVisibility(View.GONE);
		HelveticaLightTextView btnNegative = (HelveticaLightTextView) dialog
				.findViewById(R.id.btnNegative);
		btnNegative.setText(getResources().getString(R.string.cancel));
		btnNegative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				// self.onBackPressed();
				// gotoActivity(self, HomeActivity.class);
				self.finish();
				overridePendingTransition(R.anim.slide_in_right,
						R.anim.slide_out_right);

			}
		});

		HelveticaLightTextView btnPositive = (HelveticaLightTextView) dialog
				.findViewById(R.id.btnPositive);
		btnPositive.setText("Don't cancel");

		btnPositive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void sendPurchaseToServer(String productName, String receipt,
			final String transactionId) {
		ProductModelManager.postAppProduct(self, productName, receipt,
				transactionId, true, new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						item.getArrTransactionId().add(transactionId);
						increaseSlots();
						mAddPhotoAdapter.notifyDataSetChanged();
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

	private class AddAPhotoAdapter extends BaseAdapter {

		private LayoutInflater mInflater;
		private TreasureTrashBaseActivity mActivity;
		private ArrayList<ImageObj> mArrImage;
		private boolean isExistedUserImage = true;
		private AQuery aq;

		public AddAPhotoAdapter(TreasureTrashBaseActivity act,
				ArrayList<ImageObj> arrImg) {
			this.mActivity = act;
			this.mArrImage = arrImg;
			mInflater = (LayoutInflater) act
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			aq = new AQuery(act);
		}

		public void setExistedUserImage(boolean isExistedUserImage) {
			this.isExistedUserImage = isExistedUserImage;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			try {
				return mArrImage.size();
			} catch (NullPointerException ex) {
				return 0;
			}
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Holder holder = null;
			if (convertView == null) {
				holder = new Holder();
				convertView = mInflater.inflate(R.layout.item_add_photo, null);
				holder.imgAddAPhoto = (ImageView) convertView
						.findViewById(R.id.img_take_photo);

				int dimen = ((mActivity.screenWidth / 2)
						- (int) mActivity.getResources().getDimension(
								R.dimen.margin_padding_normal) - (int) mActivity
						.getResources().getDimension(
								R.dimen.margin_padding_small) / 2) / 3;

				holder.imgAddAPhoto.getLayoutParams().width = dimen;

				mLlAddPhoto.getLayoutParams().height = dimen;

				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			ImageObj image = mArrImage.get(position);
			if (image != null) {
				if (isExistedUserImage) {
					holder.imgAddAPhoto
							.setBackgroundResource(R.drawable.bg_add_image_teal);
					if (image.getBitmap() != null) {
						holder.imgAddAPhoto.setImageBitmap(image.getBitmap());
					} else if (image.getImageUrl() != null
							&& !image.getImageUrl().isEmpty()) {
						aq.id(holder.imgAddAPhoto).image(image.getImageUrl());
					} else {
						holder.imgAddAPhoto.setImageResource(image
								.getResourceNoImage());
					}
				} else {
					holder.imgAddAPhoto.setImageResource(image
							.getResourceNoImage());
					holder.imgAddAPhoto
							.setBackgroundResource(R.drawable.bg_add_image_red);
				}

			}

			return convertView;
		}
	}

	class Holder {
		ImageView imgAddAPhoto;
	}

	/*----- Methods for in-app billing -----*/
	private void initInAppPurchase() {
		// Create the helper, passing it our context and the public key to
		// verify signatures with
		Log.d(TAG, "Creating IAB helper.");
		mHelper = new IabHelper(this, Item.BASE64ENCODEDPUBLICKEY);

		// enable debug logging (for a production application, you should set
		// this to false).
		mHelper.enableDebugLogging(true);

		// Start setup. This is asynchronous and the specified listener
		// will be called once setup completes.
		Log.d(TAG, "Starting setup.");
		mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
			public void onIabSetupFinished(IabResult result) {
				Log.d(TAG, "Setup finished.");

				if (!result.isSuccess()) {
					// Oh noes, there was a problem.
					Log.e(TAG, "Problem setting up in-app billing: " + result);
					return;
				}

				// Have we been disposed of in the meantime? If so, quit.
				if (mHelper == null)
					return;

				// IAB is fully set up. Now, let's get an inventory of stuff we
				// own.
				Log.d(TAG, "Setup successful. Querying inventory.");
				mHelper.queryInventoryAsync(mGotInventoryListener);
			}
		});
	}

	private void purchaseItem(String skuId, String payload) {
		/*
		 * TODO: for security, generate your payload here for verification. See
		 * the comments on verifyDeveloperPayload() for more info. Since this is
		 * a SAMPLE, we just use an empty string, but on a production app you
		 * should carefully generate this.
		 */
		// mHelper.launchPurchaseFlow(this, skuId, IabHelper.ITEM_TYPE_INAPP,
		// RC_REQUEST, mPurchaseFinishedListener, payload);
		mHelper.launchPurchaseFlow(this, skuId, RC_REQUEST,
				mPurchaseFinishedListener, payload);
	}

	// Listener that's called when we finish querying the items and
	// subscriptions we own
	IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {
		public void onQueryInventoryFinished(IabResult result,
				Inventory inventory) {
			Log.d(TAG, "Query inventory finished.");

			// Have we been disposed of in the meantime? If so, quit.
			// TODO: IAP: need to change after receving google account of
			// Treasure Trash
			/* temp */
			if (inventory.hasPurchase(Item.KEY_ADD_3_PHOTOS)) {
				Log.e(TAG, "Query inventory finished. " + result);
				mHelper.consumeAsync(
						inventory.getPurchase(Item.KEY_ADD_3_PHOTOS), null);
				return;
			}
			/* end temp */
			if (mHelper == null)
				return;

			// Is it a failure?
			if (result.isFailure()) {
				Log.e(TAG, "Failed to query inventory: " + result);
				return;
			}

			Log.d(TAG, "Query inventory was successful.");

			/*
			 * Check for items we own. Notice that for each purchase, we check
			 * the developer payload to see if it's correct! See
			 * verifyDeveloperPayload().
			 */

			// Do we have the premium upgrade?
			Purchase addMore = inventory.getPurchase(Item.KEY_ADD_3_PHOTOS);
			if (addMore != null && verifyDeveloperPayload(addMore)) {
				Log.d(TAG, "We have gas. Consuming it.");
				mHelper.consumeAsync(addMore, mConsumeFinishedListener);
				return;
			}

			Log.d(TAG, "Initial inventory query finished; enabling main UI.");
		}
	};

	/** Verifies the developer payload of a purchase. */
	boolean verifyDeveloperPayload(Purchase p) {
		String payload = p.getDeveloperPayload();

		/*
		 * TODO: verify that the developer payload of the purchase is correct.
		 * It will be the same one that you sent when initiating the purchase.
		 * 
		 * WARNING: Locally generating a random string when starting a purchase
		 * and verifying it here might seem like a good approach, but this will
		 * fail in the case where the user purchases an item on one device and
		 * then uses your app on a different device, because on the other device
		 * you will not have access to the random string you originally
		 * generated.
		 * 
		 * So a good developer payload has these characteristics:
		 * 
		 * 1. If two different users purchase an item, the payload is different
		 * between them, so that one user's purchase can't be replayed to
		 * another user.
		 * 
		 * 2. The payload must be such that you can verify it even when the app
		 * wasn't the one who initiated the purchase flow (so that items
		 * purchased by the user on one device work on other devices owned by
		 * the user).
		 * 
		 * Using your own server to store and verify developer payloads across
		 * app installations is recommended.
		 */

		return true;
	}

	// Callback for when a purchase is finished
	IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
		public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
			Log.d(TAG, "Purchase finished: " + result + ", purchase: "
					+ purchase);

			// if we were disposed of in the meantime, quit.
			// TODO: IAP: need to change after receiving google account of
			// Treasure Trash
			/* temp */

			if (purchase != null) {
				Log.e(TAG, "Query inventory finished. " + result);
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
				return;
			}

			/* end temp */
			if (mHelper == null)
				return;

			if (result.isFailure()) {
				Log.e(TAG, "Error purchasing: " + result);
				return;
			}
			if (!verifyDeveloperPayload(purchase)) {
				Log.e(TAG,
						"Error purchasing. Authenticity verification failed.");
				return;
			}

			Log.d(TAG, "Purchase successful.");

			// Consuming item
			if (purchase.getSku().equals(Item.KEY_ADD_3_PHOTOS)) {
				// bought 1/4 tank of gas. So consume it.
				Log.d(TAG, "Purchase is gas. Starting gas consumption.");
				mHelper.consumeAsync(purchase, mConsumeFinishedListener);
			}
		}
	};

	// Called when consumption is complete
	IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
		public void onConsumeFinished(Purchase purchase, IabResult result) {
			Log.d(TAG, "Consumption finished. Purchase: " + purchase
					+ ", result: " + result);

			// if we were disposed of in the meantime, quit.
			if (mHelper == null)
				return;

			// We know this is the "gas" sku because it's the only one we
			// consume,
			// so we don't check which sku was consumed. If you have more than
			// one
			// sku, you probably should check...
			if (result.isSuccess()) {
				// successfully consumed, so we apply the effects of the item in
				// our
				// game world's logic, which in our case means filling the gas
				// tank a bit
				Log.d(TAG, "Consumption successful. Provisioning.");
				if (purchase.getSku().equals(Item.KEY_ADD_3_PHOTOS)) {
					sendPurchaseToServer(Item.PURCHASE_ADD_3_PHOTO,
							"DemoPurchase", ""
									+ Calendar.getInstance().getTimeInMillis());
					Log.e("AddAListingActivity", "Add more");
				}
			} else {
				Log.e(TAG, "Error while consuming: " + result);
			}
			Log.d(TAG, "End consumption flow.");
		}
	};
	
	private boolean isInteger(String s){
		try{
			Integer.parseInt(s);
			
		}catch(Exception e){
			return false;
		}
		return true;
	}
}