package com.pt.treasuretrash.utility;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

public class AssetUtil {
	/**
	 * To Load Image
	 * 
	 * @return Drawable
	 */
	public static Drawable getDrawable(Context context, String fileName) {
		Drawable drawable = null;
		try {
			Logger.e("AssetUtil", "Get image : " + fileName);
			AssetManager assetManager = context.getAssets();
			InputStream ims = assetManager.open(fileName);
			// create drawable from stream
			drawable = Drawable.createFromStream(ims, null);
		} catch (IOException e) {
			Logger.e("Error : getAsset - Drawable", "FileName = " + fileName);
			e.printStackTrace();
		}
		return drawable;
	}

	public static Bitmap getBitmap(Context context, String fileName) {
		Drawable drawable = null;
		try {
			AssetManager assetManager = context.getAssets();
			InputStream ims = assetManager.open(fileName);
			// create drawable from stream
			drawable = Drawable.createFromStream(ims, null);
		} catch (IOException e) {
			e.printStackTrace();
			Logger.e("Error : getAsset - Bitmap", "FileName = " + fileName);
		}
		return drawableToBitmap(drawable);
	}

	public static Bitmap drawableToBitmap(Drawable drawable) {
		if (drawable instanceof BitmapDrawable) {
			return ((BitmapDrawable) drawable).getBitmap();
		}

		Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(),
				drawable.getIntrinsicHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);
		drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
		drawable.draw(canvas);

		return bitmap;
	}

	/**
	 * To load content text file
	 * 
	 * @return String
	 */
	public static String getString(Context context, String fileName) {
		String content = null;
		try {
			AssetManager assetManager = context.getAssets();
			InputStream input = assetManager.open(fileName);

			int size = input.available();
			byte[] buffer = new byte[size];
			input.read(buffer);
			input.close();
			// byte buffer into a string
			content = new String(buffer);
		} catch (IOException e) {
		}
		return content;
	}

	/**
	 * To get all file inside folder
	 * 
	 * @return String[]
	 */
	public static String[] getFileName(Context context, String folderName) {
		String[] files = null;
		try {
			AssetManager assetManager = context.getAssets();
			files = assetManager.list(folderName);
		} catch (IOException e1) {
		}
		return files;
	}
}
