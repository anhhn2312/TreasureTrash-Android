package com.pt.treasuretrash.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.adapter.ImagePagerAdapter;
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
import com.pt.treasuretrash.network.AsyncHttpBase;
import com.pt.treasuretrash.object.ImageObj;
import com.pt.treasuretrash.object.Item;
import com.pt.treasuretrash.object.LocationObj;
import com.pt.treasuretrash.utility.DialogUtility;
import com.pt.treasuretrash.widget.AutoBgButton;
import com.pt.treasuretrash.widget.CustomDialog;
import com.pt.treasuretrash.widget.CustomDialog.OnCustomDialogClickListener;
import com.pt.treasuretrash.widget.ProgressDialog;
import com.pt.treasuretrash.widget.ProgressHUD;
import com.pt.treasuretrash.widget.TwoWayView;

public class PreviewListingActivity extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	private Item currentItem;

	private boolean isEdit = false;
	private ScrollView svDescription;
	private TwoWayView lvThumb;
	private AutoBgButton btnBack, btnPrev, btnNext, mImgPublishListing;
	private TextView tvName, tvPrice, tvCategory, tvLocation, tvSeller,
			tvDescription, tvDistance;
	private ImageView mImgBack, ivReward;
	private ViewPager mPager;
	private ImagePagerAdapter mImgAdapter;
	private AddAPhotoAdapter mAddPhotoAdapter;
	private int imgPosition = 0;
	private Intent intentPurchase = null;
	private String action = "";
	private static Activity instance;
	private LinearLayout llPremium, llGone;
	private ImageView ivGone;
	private View viewTransparent;
	private boolean isClickCancelContinueWithFailedImage;

	private ProgressHUD prog;
	Handler handler;

	// for purchase
	// static final String KEY_QUICK_TYPE = "android.test.purchased";
	// static final String KEY_PREMIUM_TYPE = "android.test.purchased";
	private static final int RC_REQUEST = 10001;
	private IabHelper mHelper;
	private int numImageUploadAgainFailed = 0, numImageUploadAgainSuccess;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_listing);
		initInAppPurchase();
		instance = this;
		handler = new Handler();

		initUI();
		initData();
		fillDetails();

		Bundle bundle = getIntent().getExtras();

		if (bundle != null) {
			if (bundle.containsKey(GlobalValue.KEY_ACTION)) {
				action = bundle.getString(GlobalValue.KEY_ACTION);
			}
		}

		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				initControl();
			}
		}, 1000);

	}

	public static Activity getInstance() {
		return instance;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// Set width/height pager
		mPager.getLayoutParams().width = screenWidth;
		mPager.getLayoutParams().height = screenWidth;

		Log.e(TAG, "Width: " + mPager.getLayoutParams().width);
		Log.e(TAG, "Height: " + mPager.getLayoutParams().height);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();

		// very important:
		Log.d(TAG, "Destroying helper.");
		if (mHelper != null) {
			mHelper.dispose();
			mHelper = null;
		}

		instance = null;
	}

	private void initUI() {
		llPremium = (LinearLayout) findViewById(R.id.llPremium);
		llGone = (LinearLayout) findViewById(R.id.llGone);
		ivGone = (ImageView) findViewById(R.id.ivGone);
		viewTransparent = findViewById(R.id.viewTransparent);

		lvThumb = (TwoWayView) findViewById(R.id.lvThumb);

		btnBack = (AutoBgButton) findViewById(R.id.btnBack);
		btnNext = (AutoBgButton) findViewById(R.id.btnNext);
		btnPrev = (AutoBgButton) findViewById(R.id.btnPrev);

		tvName = (TextView) findViewById(R.id.tvName);
		tvName.setSelected(true);
		tvPrice = (TextView) findViewById(R.id.tvPrice);
		tvCategory = (TextView) findViewById(R.id.tvCategory);
		tvLocation = (TextView) findViewById(R.id.tvLocation);
		tvLocation.setSelected(true);
		tvDescription = (TextView) findViewById(R.id.tvDescription);
		tvDescription.setMinHeight((int) screenWidth * 10 / 44);

		tvDistance = (TextView) findViewById(R.id.tvDistance);
		tvSeller = (TextView) findViewById(R.id.tvSeller);

		ivReward = (ImageView) findViewById(R.id.ivReward);
		mImgBack = (ImageView) findViewById(R.id.img_back);
		mImgPublishListing = (AutoBgButton) findViewById(R.id.img_publish_listing);
		mPager = (ViewPager) findViewById(R.id.pager);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				screenWidth, screenWidth);
		params.setMargins(0,
				(int) getResources().getDimension(R.dimen.header_height) / 2,
				0, 0);
		mPager.setLayoutParams(params);

		svDescription = (ScrollView) findViewById(R.id.svDescription);
		LinearLayout.LayoutParams svDescriptionParams = new LinearLayout.LayoutParams(
				LayoutParams.MATCH_PARENT, (int) screenWidth * 10 / 44);
		svDescription.setLayoutParams(svDescriptionParams);

		svDescription.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View __v, MotionEvent __event) {
				if (__event.getAction() == MotionEvent.ACTION_DOWN) {
					// Disallow the touch request for parent scroll on touch of
					// child view
					requestDisallowParentInterceptTouchEvent(__v, true);
				} else if (__event.getAction() == MotionEvent.ACTION_UP
						|| __event.getAction() == MotionEvent.ACTION_CANCEL) {
					// Re-allows parent events
					requestDisallowParentInterceptTouchEvent(__v, false);
				}
				return false;
			}
		});
	}

	private void requestDisallowParentInterceptTouchEvent(View __v,
			Boolean __disallowIntercept) {
		while (__v.getParent() != null && __v.getParent() instanceof View) {
			if (__v.getParent() instanceof ScrollView) {
				__v.getParent().requestDisallowInterceptTouchEvent(
						__disallowIntercept);
			}
			__v = (View) __v.getParent();
		}
	}

	private void initData() {
		// Init pager.
		if (AddAListingActivity.arrLargeImage.size() > 0) {
			// Set large images
			mImgAdapter = new ImagePagerAdapter(self,
					AddAListingActivity.arrLargeImage);

			mPager.setAdapter(mImgAdapter);

			// Set small images
			mAddPhotoAdapter = new AddAPhotoAdapter(
					(TreasureTrashBaseActivity) self,
					AddAListingActivity.arrLargeImage);
			lvThumb.setAdapter(mAddPhotoAdapter);
		}

		tvName.setText(AddAListingActivity.item.getTitle());
		tvDescription.setText(AddAListingActivity.item.getDescription());
	}

	private void initControl() {
		btnNext.setOnClickListener(this);
		btnPrev.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		mImgBack.setOnClickListener(this);
		mImgPublishListing.setOnClickListener(this);

		lvThumb.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				mPager.setCurrentItem(position);
			}

		});

	}

	private void fillDetails() {

		tvCategory.setText(AddAListingActivity.item.getCategory().getName());
		LocationObj locationObj = AddAListingActivity.item.getLocation();

		String currencySymbol = locationObj.getCurrencySymbol();
		String price = currencySymbol.equalsIgnoreCase(GlobalValue.CURRENCY_MAP
				.get("VND")) ? AddAListingActivity.item.getCost()
				+ currencySymbol : currencySymbol
				+ AddAListingActivity.item.getCost();
		tvPrice.setText(AddAListingActivity.item.getCost() != 0 ? price
				: "Free");
		tvLocation.setText(AddAListingActivity.item.getLocation()
				.getLocationAddress());

		tvSeller.setText(GlobalValue.myAccount.getName());

		// set reward :
		if (GlobalValue.myAccount.getRewardType().equalsIgnoreCase(
				Item.REWARD_GOLD)) {
			ivReward.setImageResource(R.drawable.image_star_gold);
		} else if (GlobalValue.myAccount.getRewardType().equalsIgnoreCase(
				Item.REWARD_SILVER)) {
			ivReward.setImageResource(R.drawable.image_star_silver);
		} else if (GlobalValue.myAccount.getRewardType().equalsIgnoreCase(
				Item.REWARD_BRONZE)) {
			ivReward.setImageResource(R.drawable.image_star_bronze);
		} else if (GlobalValue.myAccount.getRewardType().equalsIgnoreCase(
				Item.REWARD_NEW_MEMBER)) {
			ivReward.setImageResource(R.drawable.image_new_member);
		}

		if (AddAListingActivity.item.getType().equals(Item.TYPE_PREMIUM)) {
			llPremium.setVisibility(View.VISIBLE);

		} else {
			llPremium.setVisibility(View.INVISIBLE);
		}

		if (AddAListingActivity.item.getType().equalsIgnoreCase(Item.TYPE_FREE)) {
			llGone.setVisibility(View.VISIBLE);
			ivGone.setImageResource(R.drawable.image_item_free);
		} else if (AddAListingActivity.item.getType().equalsIgnoreCase(
				Item.TYPE_QUICK)) {
			llGone.setVisibility(View.VISIBLE);
			ivGone.setImageResource(R.drawable.image_item_quick);
		} else {
			llGone.setVisibility(View.GONE);
		}

	}

	@Override
	public void onClick(View v) {
		if (v == btnBack || v == mImgBack) {
			super.onBackPressed();
		} else if (v == btnNext) {
			nextImage();
		} else if (v == btnPrev) {
			prevImage();
		} else if (v == mImgPublishListing) {
			// Fix bug doulbe publishing
			mImgPublishListing.setEnabled(false);

			if (action.equalsIgnoreCase(GlobalValue.KEY_ACTION_EDIT_ITEM)) {
				if (!AddAListingActivity.item.getType().equals(
						GlobalValue.currentItem.getType())) {
					if (AddAListingActivity.item.getType().equals(
							Item.TYPE_QUICK)) {
						// Purchase an item here
						purchaseItem(Item.KEY_QUICK_TYPE, "");

					} else if (AddAListingActivity.item.getType().equals(
							Item.TYPE_PREMIUM)) {
						// Purchase an item here
						purchaseItem(Item.KEY_PREMIUM_TYPE, "");

					} else {
						publishListing();
					}
				} else {
					publishListing();
				}
			} else {
				if (AddAListingActivity.item.getType().equals(Item.TYPE_QUICK)) {
					// Purchase an item here
					purchaseItem(Item.KEY_QUICK_TYPE, "");

				} else if (AddAListingActivity.item.getType().equals(
						Item.TYPE_PREMIUM)) {
					// Purchase an item here
					purchaseItem(Item.KEY_PREMIUM_TYPE, "");

				} else {
					publishListing();
				}
			}

		}
	}

	private void prevImage() {
		if (imgPosition > 0) {
			imgPosition--;
			mPager.setCurrentItem(imgPosition, true);
		}
	}

	private void nextImage() {
		if (imgPosition < AddAListingActivity.arrLargeImage.size() - 1) {
			imgPosition++;
			mPager.setCurrentItem(imgPosition, true);
		}
	}

	// private void uploadAllImages() {
	// // Show progress bar
	// showProgressDialog();
	//
	// // Convert bitmap to file
	// ArrayList<File> files = new ArrayList<File>();
	// File filesDir = getApplicationContext().getFilesDir();
	//
	// for (int i = 0; i < AddAListingActivity.arrLargeImage.size(); i++) {
	// File imageFile = new File(filesDir, "photo" + i + ".jpg");
	// OutputStream os;
	// try {
	// os = new FileOutputStream(imageFile);
	// AddAListingActivity.arrLargeImage.get(i).getBitmap()
	// .compress(Bitmap.CompressFormat.JPEG, 100, os);
	// files.add(imageFile);
	// os.flush();
	// os.close();
	// } catch (Exception e) {
	// Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
	// }
	//
	// ProductModelManager.postFile(self, files, true,
	// new ModelManagerListener() {
	//
	// @Override
	// public void onSuccess(String json) {
	// Log.e(TAG, "Upload image: " + json);
	// try {
	// JSONArray arrImage = new JSONArray(json);
	//
	// for (int i = 0; i < arrImage.length(); i++) {
	// AddAListingActivity.arrLargeImage.get(i)
	// .setImageUrl(arrImage.getString(i));
	// }
	//
	// AddAListingActivity.item
	// .setArrImages(AddAListingActivity.arrLargeImage);
	// publishListing();
	//
	// } catch (JSONException e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }
	//
	// @Override
	// public void onError(int statusCode, String json) {
	// // TODO Auto-generated method stub
	//
	// }
	//
	// @Override
	// public void onError() {
	// // TODO Auto-generated method stub
	//
	// }
	// });
	// }
	// }

	private void publishListing() {

		if (action.equalsIgnoreCase(GlobalValue.KEY_ACTION_EDIT_ITEM)) {
			isEdit = true;
			if (!checkIfImageUploadedFailed(isEdit,
					AddAListingActivity.mapImageFailed.size())) {
				updateItem(isEdit);
			}
		} else {
			if (!checkIfImageUploadedFailed(isEdit,
					AddAListingActivity.mapImageFailed.size())) {
				createNewItem(isEdit);
			}
		}

	}

	private boolean checkIfImageUploadedFailed(boolean isEdit,
			int numImageFailed) {
		if (numImageFailed != 0) {
			boolean allImageFailed = (numImageFailed == AddAListingActivity.numImagesToUpload);
			if (isClickCancelContinueWithFailedImage) {
				tryUploadImageAgain();
			} else {
				showFailedImageOptionDialog(isEdit, allImageFailed);
			}

			return true;
		} else {
			return false;
		}
	}

	private void showFailedImageOptionDialog(final boolean isEdit,
			final boolean isAllImageFailed) {
		new CustomDialog(self, AddAListingActivity.mapImageFailed.size()
				+ (AddAListingActivity.mapImageFailed.size() > 1 ? " images"
						: " image") + " failed to upload", "",
				getString(R.string.continue_), getString(R.string.cancel),
				new OnCustomDialogClickListener() {

					@Override
					public void onYes() {

						if (!isAllImageFailed) {
							uploadWithSuccessImages();
						} else {
							showPopupAllImagesFailed();
						}
					}

					@Override
					public void onNo() {
						isClickCancelContinueWithFailedImage = true;
						mImgPublishListing.setEnabled(true);
					}

					@Override
					public void onNeutral() {

					}
				}).show();
	}

	private void showPopupAllImagesFailed() {
		showMessageDialog(
				getString(R.string.message_alert_all_image_upload_failed),
				new DialogListener() {

					@Override
					public void onClose(Dialog dialog) {
						tryUploadImageAgain();

					}
				});
	}

	private void uploadWithSuccessImages() {
		Iterator it;

		it = AddAListingActivity.mapImageFailed.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			int index = (Integer) pair.getKey();
			AddAListingActivity.arrLargeImage.remove(index);

		}

		ArrayList<ImageObj> newImageArray = new ArrayList<ImageObj>();
		for (int i = 0; i < AddAListingActivity.arrLargeImage.size(); i++) {
			if (!AddAListingActivity.mapImageFailed.containsKey(i)) {
				newImageArray.add(AddAListingActivity.arrLargeImage.get(i));
			}
		}
		AddAListingActivity.item.setArrImages(newImageArray);

		if (isEdit) {
			updateItem(isEdit);
		} else {
			createNewItem(isEdit);
		}

	}

	private void tryUploadImageAgain() {
		numImageUploadAgainFailed = 0;
		numImageUploadAgainSuccess = 0;
		isClickCancelContinueWithFailedImage = false;
		Iterator it;

		it = AddAListingActivity.mapImageFailed.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			boolean isUploaded = (Boolean) pair.getValue();
			if (isUploaded) {
				it.remove();
				AddAListingActivity.numImagesToUpload--;
			}
		}

		it = AddAListingActivity.mapImageFailed.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry pair = (Map.Entry) it.next();
			int key = (Integer) pair.getKey();
			uploadImage(key);
		}
	}

	private void uploadImage(final int imageIndex) {
		// Convert bitmap to file
		File filesDir = getApplicationContext().getFilesDir();

		File imageFile = new File(filesDir, "photo"
				+ AddAListingActivity.arrLargeImage.get(imageIndex) + ".jpg");
		OutputStream os;
		try {
			os = new FileOutputStream(imageFile);
			AddAListingActivity.arrLargeImage.get(imageIndex).getBitmap()
					.compress(Bitmap.CompressFormat.JPEG, 100, os);
			os.flush();
			os.close();
		} catch (Exception e) {
			Log.e(getClass().getSimpleName(), "Error writing bitmap", e);
		}

		ProductModelManager.postFile(self, imageFile, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						Log.e(TAG, "Uploaded image: " + json);
						try {
							JSONArray arrImage = new JSONArray(json);
							AddAListingActivity.arrLargeImage.get(imageIndex)
									.setImageUrl(arrImage.getString(0));

							AddAListingActivity.item
									.setArrImages(AddAListingActivity.arrLargeImage);

							numImageUploadAgainSuccess++;
							AddAListingActivity.mapImageFailed.put(imageIndex,
									true);
							Log.i("UPLOAD_IMAGE_AGAIN",
									numImageUploadAgainSuccess + "--"
											+ numImageUploadAgainFailed);
							if (numImageUploadAgainSuccess
									+ numImageUploadAgainFailed == AddAListingActivity.numImagesToUpload) {
								tryUploadImageResult();
							}

						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}

					@Override
					public void onError(int statusCode, String json) {
						DialogUtility.showShortToast(self, "Image upload fail");
						numImageUploadAgainFailed++;
						if (numImageUploadAgainSuccess
								+ numImageUploadAgainFailed == AddAListingActivity.numImagesToUpload) {
							tryUploadImageResult();
						}

					}

					@Override
					public void onError() {
						DialogUtility.showShortToast(self, "Image upload fail");
						numImageUploadAgainFailed++;
						if (numImageUploadAgainSuccess
								+ numImageUploadAgainFailed == AddAListingActivity.numImagesToUpload) {
							tryUploadImageResult();
						}
					}
				});
	}

	private void tryUploadImageResult() {
		mImgPublishListing.setEnabled(true);

		if (action.equalsIgnoreCase(GlobalValue.KEY_ACTION_EDIT_ITEM)) {
			isEdit = true;
			if (!checkIfImageUploadedFailed(isEdit, numImageUploadAgainFailed)) {
				updateItem(isEdit);
			}
		} else {
			if (!checkIfImageUploadedFailed(isEdit, numImageUploadAgainFailed)) {
				createNewItem(isEdit);
			}
		}

	}

	private void createNewItem(boolean isEdit) {
		// Show progress bar.
		showProgressBar();

		ProductModelManager.createNewItem(self, AddAListingActivity.item, true,
				isEdit, new ModelManagerListener() {

					@Override
					public void onSuccess(final String json) {

						AddAListingActivity.item = ParserUtility
								.parseItem(json);
						loadAgainItem(AddAListingActivity.item.getId(), false);
						// Fix bug doulbe publishing

					}

					@Override
					public void onError(int statusCode, String json) {
						mImgPublishListing.setEnabled(true);
						closeProgressBar();
						if (statusCode == AsyncHttpBase.NETWORK_STATUS_ERROR_TIME_OUT) {
							showMessageDialog(
									getString(R.string.error_timeout_publish_item),
									new DialogListener() {

										@Override
										public void onClose(Dialog dialog) {
											// TODO Auto-generated method stub
											dialog.dismiss();
										}
									});
						}

					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub
						mImgPublishListing.setEnabled(true);
						closeProgressBar();
						showMessageDialog(
								getString(R.string.error_default_message),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
					}
				});
	}

	private void updateItem(boolean isEdit) {
		// Show progress bar.
		showProgressBar();

		ProductModelManager.updateItem(self, AddAListingActivity.item, true,
				isEdit, new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// Close progress bar
						loadAgainItem(AddAListingActivity.item.getId(), true);

					}

					@Override
					public void onError(int statusCode, String json) {
						Log.e(TAG, "Error: " + json);
						mImgPublishListing.setEnabled(true);
						closeProgressBar();
						showMessageDialog(
								getString(R.string.error_default_message),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub
						mImgPublishListing.setEnabled(true);
						closeProgressBar();
						showMessageDialog(
								getString(R.string.error_default_message),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
					}
				});
	}

	private void loadAgainItem(String itemId, final boolean isUpdated) {
		ProductModelManager
				.getItemDetails(
						self,
						itemId,
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LAT),
						pref.getFloatValue(TreasureTrashSharedPreferences.PREF_SEARCH_LOCATION_LNG),
						screenWidth, screenWidth, true,
						new ModelManagerListener() {

							@Override
							public void onSuccess(String json) {

								closeProgressBar();
								mImgPublishListing.setEnabled(true);
								AddAListingActivity.item = ParserUtility
										.parseItem(json);
								if (isUpdated) {
									// refresh yourlisting
//									GlobalValue.arrItems
//											.remove(GlobalValue.currentItemPosition);
//									GlobalValue.arrItems.add(
//											GlobalValue.currentItemPosition,
//											AddAListingActivity.item);
									HomeActivity.isRefreshYourListings = true;

									processAfterUpdateItem();
								} else {
									showMessageDialog(
											"Your item has been successfully posted",
											"OK", new DialogListener() {

												@Override
												public void onClose(
														Dialog dialog) {
													dialog.dismiss();
													GlobalValue.publishedItem = AddAListingActivity.item;
													// refresh yourlisting
													HomeActivity.isRefreshYourListings = true;
													getAccountInfo();

												}
											});
								}
							}

							@Override
							public void onError(int statusCode, String json) {
								showMessageDialog(
										getString(R.string.message_alert_invalid_item),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												// TODO Auto-generated method
												// stub

											}
										});
							}

							@Override
							public void onError() {
								showMessageDialog(
										getString(R.string.message_alert_invalid_item),
										new DialogListener() {

											@Override
											public void onClose(Dialog dialog) {
												// TODO Auto-generated method
												// stub

											}
										});

							}
						});
	}

	private void processAfterUpdateItem() {
		DialogUtility.showProgressDialog(self);
		aq.id(ivReward).image(AddAListingActivity.item.getImage(), false, true,
				0, 0, new BitmapAjaxCallback() {
					@Override
					protected void callback(String url, ImageView iv,
							Bitmap bm, AjaxStatus status) {
						if (bm != null) {
							DialogUtility.closeProgressDialog();
							showMessageDialog(
									"Your item has been successfully updated.",
									"OK", new DialogListener() {

										@Override
										public void onClose(Dialog dialog) {
											// TODO Auto-generated
											// method
											// stub
//											Bundle bundle = new Bundle();
//											bundle.putString(
//													GlobalValue.KEY_ACTION_ITEM_DETAIL,
//													GlobalValue.KEY_DISPLAY_AFTER_PUBLISH);

											gotoActivity(
													self,
													ItemDetailsActivity.class,
													Intent.FLAG_ACTIVITY_CLEAR_TOP);

										}
									});

						}
					}
				});

	}

	private void getAccountInfo() {
		// Set data
		AccountModelManager.getAccountInfo(self, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						Log.e(TAG, "JSon Profile: " + json);

						pref.putStringValue(
								TreasureTrashSharedPreferences.PREF_ACCOUNT_JSON,
								json);

						GlobalValue.myAccount = ParserUtility.parseAccount(
								json, pref);

						Bundle bundle = new Bundle();
						bundle.putString(GlobalValue.KEY_ACTION,
								GlobalValue.KEY_ACTION_SHOW_PUBLISHED_LISTING);
						gotoActivity(self, HomeActivity.class, bundle,
								Intent.FLAG_ACTIVITY_CLEAR_TOP);
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

	private void showProgressBar() {
		prog = ProgressHUD.show(self, "", true, true, new OnCancelListener() {

			@Override
			public void onCancel(DialogInterface dialog) {
				// TODO Auto-generated method stub
			}
		});
	}

	private void closeProgressBar() {
		if (prog != null) {
			prog.dismiss();
		}
	}

	private void sendPurchaseToServer(String productName, String receipt,
			final String transactionId) {
		ProductModelManager.postAppProduct(self, productName, receipt,
				transactionId, true, new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						AddAListingActivity.item.getArrTransactionId().add(
								transactionId);
						publishListing();

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

		public AddAPhotoAdapter(TreasureTrashBaseActivity act,
				ArrayList<ImageObj> arrImg) {
			this.mActivity = act;
			this.mArrImage = arrImg;
			mInflater = (LayoutInflater) act
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

				int dimen = ((mActivity.screenWidth - (int) getResources()
						.getDimension(R.dimen.margin_padding_normal) * 2) / 6);

				holder.imgAddAPhoto.getLayoutParams().width = dimen;

				lvThumb.getLayoutParams().height = dimen;

				convertView.setTag(holder);
			} else {
				holder = (Holder) convertView.getTag();
			}

			ImageObj image = mArrImage.get(position);
			if (image != null) {
				holder.imgAddAPhoto
						.setBackgroundResource(R.drawable.bg_add_image_teal);
				if (image.getBitmap() != null) {
					holder.imgAddAPhoto.setImageBitmap(image.getBitmap());
				} else if (image.getImageUrl() != null
						&& !image.getImageUrl().isEmpty()) {
					aq.id(holder.imgAddAPhoto).image(image.getImageUrl());
				}
			}

			return convertView;
		}
	}

	class Holder {
		ImageView imgAddAPhoto;
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

			// Have we been disposed of in the meantime? If so, quit.
			// TODO: IAP: need to change after receiving google account of
			// Treasure Trash
			/* temp */

			if (inventory.hasPurchase(Item.KEY_QUICK_TYPE)) {
				Log.e(TAG, "Query inventory finished. " + result);
				mHelper.consumeAsync(
						inventory.getPurchase(Item.KEY_QUICK_TYPE), null);
				return;
			} else if (inventory.hasPurchase(Item.KEY_PREMIUM_TYPE)) {
				Log.e(TAG, "Query inventory finished. " + result);
				mHelper.consumeAsync(
						inventory.getPurchase(Item.KEY_PREMIUM_TYPE), null);
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
			Purchase quick = inventory.getPurchase(Item.KEY_QUICK_TYPE);
			if (quick != null && verifyDeveloperPayload(quick)) {
				Log.d(TAG, "We have gas. Consuming it.");
				mHelper.consumeAsync(quick, mConsumeFinishedListener);
				return;
			}
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
			if (purchase.getSku().equals(Item.KEY_QUICK_TYPE)
					|| purchase.getSku().equals(Item.KEY_PREMIUM_TYPE)) {
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
				sendPurchaseToServer(
						AddAListingActivity.item.getPurchaseType(),
						"DemoPurchase", ""
								+ Calendar.getInstance().getTimeInMillis());
				Log.e("PreviewActivity", "Purchased an type");
			} else {
				Log.e(TAG, "Error while consuming: " + result);
			}
			Log.d(TAG, "End consumption flow.");
		}
	};
}
