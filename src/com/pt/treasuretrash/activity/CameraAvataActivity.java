package com.pt.treasuretrash.activity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
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
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.utility.ImageUtil;
import com.pt.treasuretrash.widget.Preview;
import com.pt.treasuretrash.widget.Preview.IOnSetPreviewSize;

import eu.janmuller.android.simplecropimage.CropImage;

public class CameraAvataActivity extends TreasureTrashBaseMessageActivity
		implements OnClickListener {
	private static final String TAG = "CamTestActivity";
	// Static final constants
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
		mImgOpenGallery.setVisibility(View.GONE);
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
		mImgSwitchCamera.setOnClickListener(this);
		// load image galery

	}

	@Override
	public void onResume() {
		super.onResume();
		setCamera(isFrontFacing);
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
			camera.stopPreview();
			preview.setCamera(null);
			camera.release();
			camera = null;
		}
		super.onPause();
	}

	private void resetCam() {
		if (camera != null) {
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
			HomeActivity.selectImage = selectImage;
			selectImage = null;
			currentFile.deleteOnExit();
			onBackPressed();
			return;
		}
		if (v == btnCapture) {
			if (selectImage == null)
				camera.takePicture(shutterCallback, rawCallback, jpegCallback);
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

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != RESULT_OK) {
			selectImage = null;
			currentFile.deleteOnExit();
			updateUI(mode_select_image);
			return;
		}

		switch (requestCode) {
		case PICK_FROM_FILE:
			try {
				InputStream inputStream = getContentResolver().openInputStream(
						data.getData());
				FileOutputStream fileOutputStream = new FileOutputStream(
						currentFile);
				copyStream(inputStream, fileOutputStream);
				fileOutputStream.close();
				inputStream.close();
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
			selectImage = ImageUtil.getResizedBitmap(selectImage, 800, 800);
			imageView.setImageBitmap(selectImage);
			break;

		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void startCropImage() {

		Intent intent = new Intent(this, CropImage.class);
		intent.putExtra(CropImage.IMAGE_PATH, currentFile.getPath());
		intent.putExtra(CropImage.SCALE, true);
		intent.putExtra(CropImage.ASPECT_X, 3);
		intent.putExtra(CropImage.ASPECT_Y, 3);

		startActivityForResult(intent, REQUEST_CODE_CROP_IMAGE);
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

			surfaceViewMaxHeight = (int)(screenHeight
					- getResources().getDimension(R.dimen.header_height)
					- getResources().getDimension(
							R.dimen.layout_camera_bottom_child_height));

			int surfaceWidth = screenWidth;
			int surfaceHeight;
			if (width < height) {
				surfaceHeight = surfaceWidth * height / width;
				if(surfaceHeight > surfaceViewMaxHeight){
					surfaceHeight = surfaceViewMaxHeight;
					surfaceWidth = surfaceHeight*width/height;
				}

			} else {
				surfaceHeight = surfaceWidth * width / height;
				if(surfaceHeight > surfaceViewMaxHeight){
					surfaceHeight = surfaceViewMaxHeight;
					surfaceWidth = surfaceHeight*height/width;
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
