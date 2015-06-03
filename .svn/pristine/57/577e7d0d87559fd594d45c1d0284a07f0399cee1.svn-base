package com.pt.treasuretrash.utility;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.Images;

import com.josh.treasuretrash.R;

public class ShareUtility {
	
	public static void shareText(Activity act, String text) {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");
		shareIntent.putExtra(Intent.EXTRA_TEXT, text.trim());
		act.startActivity(Intent.createChooser(shareIntent, "Share via..."));

	}

	// save image to galery before share
	public static void shareImageFromUrl(Activity act, String link) {
		Intent share = new Intent(Intent.ACTION_SEND);
		URL url = null;
		Uri uri = null;
		try {
			url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);

			connection.connect();

			InputStream input = connection.getInputStream();

			Bitmap immutableBpm = BitmapFactory.decodeStream(input);

			Bitmap mutableBitmap = immutableBpm.copy(Bitmap.Config.ARGB_8888,
					true);

			String path = Images.Media.insertImage(act.getContentResolver(),
					mutableBitmap, "Nur", null);

			uri = Uri.parse(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		share.setType("image/*");
		share.putExtra(Intent.EXTRA_STREAM, uri);

		act.startActivity(Intent.createChooser(share, "Share Image!"));
	}

	// save image to SDcard before share
	public static void shareImageInSdCardFromUrl(Activity act, String link,String urlLocalFolder) {
		Uri uri = null;
		URL url = null;
		// create folder
		File file = new File(urlLocalFolder);
		if (!file.exists()) {
			file.mkdirs();
		}

		try {
			url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);
			connection.connect();

			// check save to sd card :
			int nameMusicIndex = url.toString().lastIndexOf("/");
			String filename = url.toString().substring(nameMusicIndex,
					url.toString().length());
			File f = new File(urlLocalFolder, filename);
			if (!f.exists()) {

				// check format file

				// get input data
				InputStream input = connection.getInputStream();
				Bitmap immutableBpm = BitmapFactory.decodeStream(input);
				Bitmap mutableBitmap = immutableBpm.copy(
						Bitmap.Config.ARGB_8888, true);
				// set file output
				FileOutputStream fileOutputStream = new FileOutputStream(f);
				BufferedOutputStream bos = new BufferedOutputStream(
						fileOutputStream);
				mutableBitmap.compress(CompressFormat.PNG, 100, bos);
			}

			uri = Uri.parse(f.getAbsolutePath());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Intent share = new Intent(Intent.ACTION_SEND);
		share.setType("image/*");
		share.putExtra(Intent.EXTRA_STREAM, uri);

		act.startActivity(Intent.createChooser(share, "Share Image!"));
	}

	public static void setAsImageIntent(Activity act, String link) {
		URL url = null;
		Uri uri = null;
		try {
			url = new URL(link);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoInput(true);

			connection.connect();

			InputStream input = connection.getInputStream();

			Bitmap immutableBpm = BitmapFactory.decodeStream(input);

			Bitmap mutableBitmap = immutableBpm.copy(Bitmap.Config.ARGB_8888,
					true);

			String path = Images.Media.insertImage(act.getContentResolver(),
					mutableBitmap, "Nur", null);

			uri = Uri.parse(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
		intent.setDataAndType(uri, "image/jpg");
		intent.putExtra("mimeType", "image/jpg");
		act.startActivityForResult(Intent.createChooser(intent, "Set As"), 200);
	}

	public static boolean setRingtone(Activity act, String link,String urlLocalFolder) {
		File file = new File(urlLocalFolder);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			URL url = new URL(link);
			URLConnection conection = url.openConnection();
			conection.connect();
			// input stream to read file - with 8k buffer
			int nameMusicIndex = url.toString().lastIndexOf("/");
			String filename = url.toString().substring(nameMusicIndex,
					url.toString().length());
			File f = new File(urlLocalFolder, filename);
			if (!f.exists()) {
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				OutputStream output = new FileOutputStream(
						urlLocalFolder + filename);
				byte data[] = new byte[1024];
				int count;
				while ((count = input.read(data)) != -1) {
					// writing data to file
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			}

			ContentValues values = new ContentValues();
			values.put(MediaStore.MediaColumns.DATA, f.getAbsolutePath());
			values.put(MediaStore.MediaColumns.TITLE, filename);
			values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
			values.put(MediaStore.MediaColumns.SIZE, f.length());
			values.put(AudioColumns.ARTIST, act.getString(R.string.app_name));
			values.put(AudioColumns.IS_RINGTONE, true);
			values.put(AudioColumns.IS_NOTIFICATION, false);
			values.put(AudioColumns.IS_ALARM, false);
			values.put(AudioColumns.IS_MUSIC, false);

			Uri uri = MediaStore.Audio.Media.getContentUriForPath(f
					.getAbsolutePath());
			Uri newUri = act.getContentResolver().insert(uri, values);
			RingtoneManager.setActualDefaultRingtoneUri(act,
					RingtoneManager.TYPE_RINGTONE, newUri);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean setNotification(Activity act, String link,String urlLocalFolder) {
		File file = new File(urlLocalFolder);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			URL url = new URL(link);
			URLConnection conection = url.openConnection();
			conection.connect();
			// input stream to read file - with 8k buffer
			int nameMusicIndex = url.toString().lastIndexOf("/");
			String filename = url.toString().substring(nameMusicIndex,
					url.toString().length());
			File f = new File(urlLocalFolder, filename);
			if (!f.exists()) {
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				OutputStream output = new FileOutputStream(
						urlLocalFolder + filename);
				byte data[] = new byte[1024];
				int count;
				while ((count = input.read(data)) != -1) {
					// writing data to file
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			}

			ContentValues values = new ContentValues();
			values.put(MediaStore.MediaColumns.DATA, f.getAbsolutePath());
			values.put(MediaStore.MediaColumns.TITLE, filename);
			values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
			values.put(MediaStore.MediaColumns.SIZE, f.length());
			values.put(AudioColumns.ARTIST, act.getString(R.string.app_name));
			values.put(AudioColumns.IS_RINGTONE, false);
			values.put(AudioColumns.IS_NOTIFICATION, true);
			values.put(AudioColumns.IS_ALARM, false);
			values.put(AudioColumns.IS_MUSIC, false);

			Uri uri = MediaStore.Audio.Media.getContentUriForPath(f
					.getAbsolutePath());
			Uri newUri = act.getContentResolver().insert(uri, values);
			RingtoneManager.setActualDefaultRingtoneUri(act,
					RingtoneManager.TYPE_NOTIFICATION, newUri);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public static boolean setAlarm(Activity act, String link,String urlLocalFolder) {
		File file = new File(urlLocalFolder);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			URL url = new URL(link);
			URLConnection conection = url.openConnection();
			conection.connect();
			// input stream to read file - with 8k buffer
			int nameMusicIndex = url.toString().lastIndexOf("/");
			String filename = url.toString().substring(nameMusicIndex,
					url.toString().length());
			File f = new File(urlLocalFolder, filename);
			if (!f.exists()) {
				InputStream input = new BufferedInputStream(url.openStream(),
						8192);

				OutputStream output = new FileOutputStream(
						urlLocalFolder + filename);
				byte data[] = new byte[1024];
				int count;
				while ((count = input.read(data)) != -1) {
					// writing data to file
					output.write(data, 0, count);
				}

				output.flush();
				output.close();
				input.close();
			}

			ContentValues values = new ContentValues();
			values.put(MediaStore.MediaColumns.DATA, f.getAbsolutePath());
			values.put(MediaStore.MediaColumns.TITLE, filename);
			values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
			values.put(MediaStore.MediaColumns.SIZE, f.length());
			values.put(AudioColumns.ARTIST, act.getString(R.string.app_name));
			values.put(AudioColumns.IS_RINGTONE, false);
			values.put(AudioColumns.IS_NOTIFICATION, false);
			values.put(AudioColumns.IS_ALARM, true);
			values.put(AudioColumns.IS_MUSIC, false);
			
			

			Uri uri = MediaStore.Audio.Media.getContentUriForPath(f
					.getAbsolutePath());
			Uri newUri = act.getContentResolver().insert(uri, values);
			RingtoneManager.setActualDefaultRingtoneUri(act,
					RingtoneManager.TYPE_ALARM, newUri);

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	
}
