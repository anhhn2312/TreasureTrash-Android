package com.pt.treasuretrash.activity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.crop.CropOption;
import com.pt.treasuretrash.crop.CropOptionAdapter;
import com.pt.treasuretrash.object.ImageObj;
import com.pt.treasuretrash.utility.ImageUtil;
import com.pt.treasuretrash.widget.Preview;
import com.pt.treasuretrash.widget.Preview.IOnSetPreviewSize;

import eu.janmuller.android.simplecropimage.CropImage;

public class CameraActivity extends TreasureTrashBaseMessageActivity implements
		OnClickListener {
	private static final String TAG = "CamTestActivity";
	// Static final constants
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;
	private static final int REQUEST_CODE_CROP_IMAGE = 1;
	private static final String TEMP_PHOTO_FILE_NAME = "temp_photo.jpg";
	// Instance variables
	private Preview preview;
	private SurfaceView surfaceView;
	private ImageView btnCancel, btnSave, btnZoomIn, btnZoomout, btnCapture;
	private FrameLayout layoutSurfaceView;
	private ImageView imageView;
	private Camera camera;
	private Activity self;
	private Bitmap selectImage;
	private static final int mode_select_image = 0;
	private static final int mode_resize_image = 1;
	private int selected_mode = 0;
	private File currentFile;
	private AQuery aq;

	private ImageView mImgOpenGallery, mImgSwitchCamera;
	private int surfaceViewMaxHeight;
	private boolean isFrontFacing;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_image_process);
		self = this;
		aq = new AQuery(self);

		Bundle bundle = getIntent().getExtras();
		if (bundle == null) {
			isFrontFacing = false;
		} else {
			if (bundle.getBoolean("isFrontFacing")) {
				isFrontFacing = true;
			} else {
				isFrontFacing = false;
			}
		}

		initUI();
		initControlUI();
		initCurrentTemFile();

	}

	private void initUI() {
		btnCapture = (ImageView) findViewById(R.id.btnCapture);
		btnCancel = (ImageView) findViewById(R.id.btnCancel);
		btnSave = (ImageView) findViewById(R.id.btnSave);
		btnZoomIn = (ImageView) findViewById(R.id.btnAdd);
		btnZoomout = (ImageView) findViewById(R.id.btnSubtract);
		imageView = (ImageView) findViewById(R.id.CropImageView);
		surfaceView = (SurfaceView) findViewById(R.id.camerapreview);
		layoutSurfaceView = (FrameLayout) findViewById(R.id.layout);
		mImgOpenGallery = (ImageView) findViewById(R.id.img_gallery);
		mImgSwitchCamera = (ImageView) findViewById(R.id.img_switch_camera);
		initPreView();
	}

	private void initPreView() {
		preview = new Preview(this, surfaceView, iOnSetPreviewSize);
		preview.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		layoutSurfaceView.addView(preview);
		preview.setKeepScreenOn(true);
	}

	private void initControlUI() {
		updateUI(mode_select_image);
		btnCancel.setOnClickListener(this);
		btnSave.setOnClickListener(this);
		btnZoomIn.setOnClickListener(this);
		btnZoomout.setOnClickListener(this);
		btnCapture.setOnClickListener(this);
		mImgOpenGallery.setOnClickListener(this);
		mImgSwitchCamera.setOnClickListener(this);
		// load image galery
		if (GlobalValue.myAccount != null) {
			if (!GlobalValue.myAccount.getImageUrl().isEmpty()) {
				aq.id(mImgOpenGallery).image(
						GlobalValue.myAccount.getImageUrl(), false, true, 0, 0,
						new BitmapAjaxCallback() {
							@Override
							protected void callback(String url, ImageView iv,
									Bitmap bm, AjaxStatus status) {
								super.callback(url, iv, bm, status);
								if (bm == null) {
									mImgOpenGallery
											.setImageResource(R.drawable.image_avatar_default);
								}

							}
						});
			} else {
				mImgOpenGallery
						.setImageResource(R.drawable.image_avatar_default);
			}
		} else {
			mImgOpenGallery.setImageResource(R.drawable.image_avatar_default);
		}

	}

	@Override
	public void onResume() {

		super.onResume();

		// updateUI(mode_select_image);
		if (selected_mode == mode_select_image) {
			// if (camera != null) {
			// resetCam();
			// return;
			// }
			Log.e("aaaaaaaaaaaaa", "resume !!!");

			setCamera(isFrontFacing);
		}
	}

	private void setCamera(boolean isFrontFacing) {
		int numCams = Camera.getNumberOfCameras();
		if (numCams > 0) {
			try {
				if (!isFrontFacing) {
					camera = Camera.open(0);
					camera.setDisplayOrientation(90);
					camera.startPreview();
					preview.setCamera(camera);
				} else {
					if (numCams > 1) {
						camera = Camera.open(1);
						camera.setDisplayOrientation(90);
						camera.startPreview();
						preview.setCamera(camera);
					} else {
						camera = Camera.open(0);
						camera.setDisplayOrientation(90);
						camera.startPreview();
						preview.setCamera(camera);
					}

				}
			} catch (RuntimeException ex) {
				// Toast.makeText(self, "Camera is not found",
				// Toast.LENGTH_LONG).show();
			}
		}
	}

	@Override
	public void onPause() {
		if (camera != null) {
			// camera.stopPreview();
		}
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub

		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		if (camera != null) {
			preview.setCamera(null);
			camera.release();
			camera = null;
		}
		super.onDestroy();

	}

	private void resetCam() {
		if (camera != null) {
			try {
				camera.setPreviewDisplay(surfaceView.getHolder());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			camera.startPreview();
			preview.setCamera(camera);
		}
	}

	private void refreshGallery(File file) {
		Intent mediaScanIntent = new Intent(
				Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(Uri.fromFile(file));
		sendBroadcast(mediaScanIntent);
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
			if (!isFrontFacing) {
				bitmap = ImageUtil.getRotateBitmap(bitmap, 90);
			}
			else{
				bitmap = ImageUtil.getRotateBitmap(bitmap, -90);
			}
			new SaveImageTask().execute(bitmap);
			imageView.setImageBitmap(bitmap);
			updateUI(mode_resize_image);
		}
	};

	private void initCurrentTemFile() {
		String state = Environment.getExternalStorageState();
		if (Environment.MEDIA_MOUNTED.equals(state)) {
			currentFile = new File(Environment.getExternalStorageDirectory(),
					TEMP_PHOTO_FILE_NAME);
		} else {
			currentFile = new File(getFilesDir(), TEMP_PHOTO_FILE_NAME);
		}
	}

	private void updateUI(int mode) {
		switch (mode) {
		case mode_resize_image:
			if (camera != null)
				camera.stopPreview();
			btnSave.setVisibility(View.VISIBLE);
			layoutSurfaceView.setVisibility(View.GONE);
			imageView.setVisibility(View.VISIBLE);
			break;
		case mode_select_image:
			btnSave.setVisibility(View.GONE);
			imageView.setVisibility(View.GONE);
			layoutSurfaceView.setVisibility(View.VISIBLE);
			resetCam();
			break;
		default:
			break;
		}
		selected_mode = mode;
	}

	private class SaveImageTask extends AsyncTask<Bitmap, String, File> {

		@Override
		protected File doInBackground(Bitmap... data) {
			FileOutputStream outStream = null;
			// Write to SD Card

			try {
				if (currentFile.exists()) {
					currentFile.createNewFile();
				}
				outStream = new FileOutputStream(currentFile);
				data[0].compress(CompressFormat.JPEG, 80, outStream);
				outStream.flush();
				outStream.close();
				refreshGallery(currentFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return currentFile;
		}

		@Override
		protected void onPostExecute(File result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			if (result != null) {
				currentFile = result;
				startCropImage();
			}

		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if (v == btnCancel) {
			selectImage = null;
			if (currentFile != null)
				currentFile.deleteOnExit();
			updateUI(mode_select_image);
			finish();
			return;
		}
		if (v == btnSave) {
			if (selectImage != null) {
				ImageObj image = new ImageObj();
				image.setBitmap(selectImage);

				if (AddAListingActivity.arrLargeImage.size() - 1 >= AddAListingActivity.addPhotoPosition) {
					AddAListingActivity.arrLargeImage
							.remove(AddAListingActivity.addPhotoPosition);
				}
				if (AddAListingActivity.arrLargeImage.size() - 1 < AddAListingActivity.addPhotoPosition) {
					AddAListingActivity.arrLargeImage.add(image);
				} else {
					AddAListingActivity.arrLargeImage.add(
							AddAListingActivity.addPhotoPosition, image);
				}

				AddAListingActivity.isNew = true;
				selectImage = null;
				currentFile.deleteOnExit();
				onBackPressed();
			}
			return;
		}
		if (v == btnCapture) {
			if (selectImage == null)
				camera.takePicture(shutterCallback, rawCallback, jpegCallback);

			return;
		}
		if (v == mImgOpenGallery) {
			openGallery();
			return;
		}

		if (v == mImgSwitchCamera) {
			if (camera != null) {
				preview.setCamera(null);
				camera.release();
				camera = null;
			}
			Bundle bundle = new Bundle();
			bundle.putBoolean("isFrontFacing", isFrontFacing ? false : true);
			gotoActivity(self, CameraActivity.class, bundle, false);
			finish();
			return;
		}
	}

	private void openGallery() {
		Intent intent = new Intent();
		intent.setType("image/*");
		intent.setAction(Intent.ACTION_GET_CONTENT);
		startActivityForResult(
				Intent.createChooser(intent,
						getResources().getString(R.string.choose_image_item)),
				PICK_FROM_FILE);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		try {
			if (resultCode != RESULT_OK) {
				selectImage = null;
				currentFile.deleteOnExit();
				updateUI(mode_select_image);
				return;
			}

			switch (requestCode) {
			case PICK_FROM_FILE:
				try {
					InputStream inputStream = getContentResolver()
							.openInputStream(data.getData());
					Bitmap bm = BitmapFactory.decodeStream(inputStream);
					if (bm.getWidth() > bm.getHeight()) {
						bm = ImageUtil.getRotateBitmap(bm, 90);
					}

					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bm.compress(CompressFormat.JPEG, 100, bos);
					byte[] bitmapdata = bos.toByteArray();
					// write the bytes in file
					FileOutputStream fos = new FileOutputStream(currentFile);
					fos.write(bitmapdata);
					fos.flush();
					bos.flush();
					bos.close();
					fos.close();

					startCropImage();

				} catch (Exception e) {
					Log.e(TAG, "Error while creating temp file", e);
				}
				break;

			case REQUEST_CODE_CROP_IMAGE:

				String path = data.getStringExtra(CropImage.IMAGE_PATH);
				if (path == null) {

					return;
				}
				updateUI(mode_resize_image);
				selectImage = BitmapFactory.decodeFile(currentFile.getPath());
				// selectImage = ImageUtil.getResizedBitmap(selectImage, 800,
				// 800);
				imageView.setImageBitmap(selectImage);
				break;

			}
			super.onActivityResult(requestCode, resultCode, data);
		} catch (Exception ex) {
			ex.toString();
		}
	}

	private void startCropImage() {

		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, currentFile.getPath());
		intent.putExtra(CropImage.SCALE, true);
		intent.putExtra(CropImage.ASPECT_X, 4);
		intent.putExtra(CropImage.ASPECT_Y, 4);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
	}

	private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, 0);

		int size = list.size();

		if (size == 0) {
			Toast.makeText(this, "Can not find image crop app",
					Toast.LENGTH_SHORT).show();

			return;
		} else {
			intent.setData(Uri.fromFile(currentFile));
			intent.putExtra("crop", "true");
			intent.putExtra("outputX", 800);
			intent.putExtra("outputY", 800);
			// intent.putExtra("aspectX", 1);
			// intent.putExtra("aspectY", 1);
			intent.putExtra("scale", false);
			intent.putExtra("return-data", true);

			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);

				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));

				startActivityForResult(i, CROP_FROM_CAMERA);
			} else {
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();

					co.title = getPackageManager().getApplicationLabel(
							res.activityInfo.applicationInfo);
					co.icon = getPackageManager().getApplicationIcon(
							res.activityInfo.applicationInfo);
					co.appIntent = new Intent(intent);

					co.appIntent
							.setComponent(new ComponentName(
									res.activityInfo.packageName,
									res.activityInfo.name));

					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(
						getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Choose Crop App");
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								startActivityForResult(
										cropOptions.get(item).appIntent,
										CROP_FROM_CAMERA);
							}
						});

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {
						selectImage = null;
						currentFile.deleteOnExit();
						updateUI(mode_select_image);
					}
				});

				AlertDialog alert = builder.create();

				alert.show();
			}
		}
	}

	public static void copyStream(InputStream input, OutputStream output)
			throws IOException {

		byte[] buffer = new byte[1024];
		int bytesRead;
		while ((bytesRead = input.read(buffer)) != -1) {
			output.write(buffer, 0, bytesRead);
		}
	}

	IOnSetPreviewSize iOnSetPreviewSize = new IOnSetPreviewSize() {

		@Override
		public void onSetPreviewSize(int width, int height) {

			surfaceViewMaxHeight = (int) (screenHeight
					- getResources().getDimension(R.dimen.header_height) - getResources()
					.getDimension(R.dimen.layout_camera_bottom_child_height));

			int surfaceWidth = screenWidth;
			int surfaceHeight;
			if (width < height) {
				surfaceHeight = surfaceWidth * height / width;
				if (surfaceHeight > surfaceViewMaxHeight) {
					surfaceHeight = surfaceViewMaxHeight;
					surfaceWidth = surfaceHeight * width / height;
				}

			} else {
				surfaceHeight = surfaceWidth * width / height;
				if (surfaceHeight > surfaceViewMaxHeight) {
					surfaceHeight = surfaceViewMaxHeight;
					surfaceWidth = surfaceHeight * height / width;
				}
			}
			Log.i("OnSetPreviewSize", width + "-" + height + "-" + surfaceWidth
					+ "-" + surfaceHeight + "--" + surfaceViewMaxHeight);
			RelativeLayout.LayoutParams surfaceParams = new RelativeLayout.LayoutParams(
					surfaceWidth, surfaceHeight);
			surfaceParams.addRule(RelativeLayout.CENTER_IN_PARENT);
			layoutSurfaceView.setLayoutParams(surfaceParams);

		}
	};

}
