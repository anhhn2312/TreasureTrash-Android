package com.pt.treasuretrash.images.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.config.WebserviceConfig;

public class ImageLoader {

	private static final String TAG = "ImageLoader";
	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	Context mContext;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	public ImageLoader(Context context) {
		mContext = context;
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	final int stub_id = R.drawable.img_notfound;

	public void DisplayImage(String url, final ImageView imageView) {
		imageViews.put(imageView, url);
//		 Bitmap bitmap = memoryCache.get(url);
		Bitmap bitmap = getBitmap(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
			// final ScaleType scale = imageView.getScaleType();
			//
			// final int[] loader = new int[] { R.drawable.image_loading_1,
			// R.drawable.image_loading_2, R.drawable.image_loading_3,
			// R.drawable.image_loading_4, R.drawable.image_loading_5,
			// R.drawable.image_loading_6, R.drawable.image_loading_7,
			// R.drawable.image_loading_8 };
			//
			// imageView.setScaleType(ScaleType.CENTER_CROP);
			// imageView.setAdjustViewBounds(true);
			//
			// final Handler h = new Handler();
			// final Runnable r = new Runnable() {
			// int index = 0;
			//
			// public void run() {
			// index = index > 7 ? 0 : index;
			// imageView.setImageDrawable(mContext.getResources()
			// .getDrawable(loader[index++]));
			//
			// h.postDelayed(this, 100);
			// }
			// };
			// h.postDelayed(r, 100);
		}
	}

	public void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	public Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(WebserviceConfig.REQUEST_TIME_OUT);
			conn.setReadTimeout(WebserviceConfig.REQUEST_TIME_OUT);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			LazyListUtils.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			Log.i(TAG,
					"Error when get bitmap for Url :" + url + " : "
							+ ex.getMessage());
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 100;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}
			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {

		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}

	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			if (bitmap != null)
				photoToLoad.imageView.setImageBitmap(bitmap);
			else {
				photoToLoad.imageView.setImageResource(stub_id);
				// final ScaleType scale = photoToLoad.imageView.getScaleType();
				//
				// final int[] loader = new int[] { R.drawable.image_loading_1,
				// R.drawable.image_loading_2, R.drawable.image_loading_3,
				// R.drawable.image_loading_4, R.drawable.image_loading_5,
				// R.drawable.image_loading_6, R.drawable.image_loading_7,
				// R.drawable.image_loading_8 };
				//
				// photoToLoad.imageView.setScaleType(ScaleType.CENTER_CROP);;
				// photoToLoad.imageView.setAdjustViewBounds(true);
				//
				// final Handler h = new Handler();
				// final Runnable r = new Runnable() {
				// int index = 0;
				//
				// public void run() {
				// index = index > 7 ? 0 : index;
				// photoToLoad.imageView.setImageDrawable(mContext
				// .getResources().getDrawable(loader[index++]));
				//
				// h.postDelayed(this, 100);
				// }
				// };
				// h.postDelayed(r, 100);
			}
		}
	}

	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}

}
