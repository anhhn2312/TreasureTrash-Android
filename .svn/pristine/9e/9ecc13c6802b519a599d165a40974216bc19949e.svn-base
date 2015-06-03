package com.pt.treasuretrash.utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

/**
 * This utility class contains all image operations
 * 
 * @author |dmb TEAM|
 * 
 */
public class ImageUtil {
	// 16:9
	// public static final double aspectRation = 1.77;
	// 4:3
	/**
	 * Constant for the aspect ratio.
	 */
	public static final double aspectRationSlider = 1.77;
	public static final double aspectRationThumb = 1.5;

	public static int widthForSlider;
	public static int widthForThumbs;

	/**
	 * Constant for the maximum size of image thumbnails.
	 */
	public static final int MAX_IMAGE_SIZE_THUMNAILS = 150;

	/**
	 * Creates Bitmap image by given URL.
	 * 
	 * @param url
	 *            url of the image
	 * @return <code>Bitmap</code> object with the image
	 */
	public static Bitmap createBitmapFromUrl(String url) {
		Bitmap bitmap = null;
		try {
			bitmap = BitmapFactory.decodeStream((InputStream) new URL(url)
					.getContent());
		} catch (MalformedURLException e) {
			//
		} catch (IOException e) {
			//
		}

		return bitmap;
	}

	/**
	 * Creates thumbnail bitmap from given URL
	 * 
	 * @param url
	 *            <code>String</code> object containing the URL
	 * @return <code>Bitmap</code> object
	 */
	public static Bitmap createThumbBitmapFromUrl(String url) {

		InputStream stream = null;
		try {
			stream = (InputStream) new URL(url).getContent();
		} catch (MalformedURLException e) {
			//
		} catch (IOException e) {
			//
		}

		Bitmap bitmap = null;

		bitmap = BitmapFactory.decodeStream(stream);

		double newBitmapHeight = (double) MAX_IMAGE_SIZE_THUMNAILS
				/ aspectRationSlider;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outWidth = MAX_IMAGE_SIZE_THUMNAILS;
		options.outHeight = (int) newBitmapHeight;

		bitmap = Bitmap.createScaledBitmap(bitmap, MAX_IMAGE_SIZE_THUMNAILS,
				(int) newBitmapHeight, false);

		return bitmap;
	}

	/**
	 * Fixes the bitmap size for specific screens.
	 * 
	 * @param stream
	 *            input stream to fetch the bitmap
	 * @param screenWidth
	 *            widh of the screen
	 * @param url
	 *            url of the image
	 * @return <code>Bitmap</code> object with the image with fixed size
	 */
	public static Bitmap fixBitmapForSpecificScreen(InputStream stream,
			Integer screenWidth) {

		Bitmap bitmap = null;

		bitmap = BitmapFactory.decodeStream(stream);

		double newBitmapHeight = (double) screenWidth / aspectRationSlider;

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.outWidth = screenWidth;
		options.outHeight = (int) newBitmapHeight;

		bitmap = Bitmap.createScaledBitmap(bitmap, screenWidth,
				(int) newBitmapHeight, false);

		return bitmap;
	}

	public static Bitmap getRoundedBitmap(Bitmap bitmap) {
		final Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Bitmap.Config.ARGB_8888);
		final Canvas canvas = new Canvas(output);

		final int color = Color.RED;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
		final RectF rectF = new RectF(rect);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawOval(rectF, paint);

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		bitmap.recycle();

		return output;
	}

	public final static Bitmap getResizedBitmap(Bitmap bm, int newHeight,
			int newWidth) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		Log.d("BITMAP", width + "-" + height + "");
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
		// CREATE A MATRIX FOR THE MANIPULATION
		Matrix matrix = new Matrix();
		// RESIZE THE BIT MAP
		matrix.postScale(scaleWidth, scaleHeight);
		// RECREATE THE NEW BITMAP
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, false);

		return resizedBitmap;

	}

	public final static Bitmap getRotateBitmap(Bitmap bm, int rotate) {
		int width = bm.getWidth();
		int height = bm.getHeight();
		Matrix matrix = new Matrix();
		if (height < width)
			matrix.postRotate(rotate);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
				matrix, true);

		return resizedBitmap;

	}

	public static String convertImageSize(String url, int imageSize) {
		String newUrl = "";
		if (url != null) {
			if (url.length() > 3) {
				newUrl = url.substring(0, url.length() - 3);
				newUrl += imageSize;
			}
		}
		return newUrl;

	}

	public static String convertGalleryImageSize(String url, int imageSize) {
		String newUrl = "";
		if (url != null) {
			newUrl = url.substring(0, url.indexOf("?"));
			newUrl += "?width=" + imageSize + "&height=" + imageSize;
		}
		return newUrl;

	}
	public static String addImageSize(String url, int imageSize){
		String newUrl = url;
		if (url != null) {
			newUrl += "?width=" + imageSize;
		}
		return newUrl;
	}

}
