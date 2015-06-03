package com.pt.treasuretrash.utility;


import com.josh.treasuretrash.R;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

public class DialogUtility {

	private static AlertDialog alert;
	public static ProgressDialog progressDialog;
	public static  int progressDialogRequest = 0;

	/**
	 * Open progress dialog
	 * 
	 * @param text
	 */
	public static void showProgressDialog(Context context, String text) {
		synchronized (context) {
			progressDialogRequest++;
		}
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context,
					R.style.ProgressDialogTheme);
			progressDialog.setCancelable(false);
			progressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			progressDialog.show();
		}
	}

	public static void closeProgressDialog() {
//		synchronized () {
//			progressDialogRequest--;
//		}
		

		if (progressDialogRequest == 0) {
			if (progressDialog != null) {
				// fix bug force close here
				try {
					progressDialog.cancel();
					progressDialog = null;
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * Open progress dialog
	 */
	public static void showProgressDialog(Context context) {
		if (progressDialog == null) {
			progressDialog = new ProgressDialog(context,
					R.style.ProgressDialogTheme);
			progressDialog.setCancelable(false);
			progressDialog
					.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
			progressDialog.show();
		}
	}

	/**
	 * Show option dialog
	 * 
	 * @param titleId
	 * @param items
	 * @param positiveLabelId
	 * @param itemOnClick
	 * @param positiveOnClick
	 */
	public static void showOptionDialog(Context context, int titleId,
			String[] items, int positiveLabelId,
			DialogInterface.OnClickListener itemOnClick,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(titleId));
		builder.setItems(items, itemOnClick);
		builder.setPositiveButton(context.getString(positiveLabelId),
				positiveOnClick);
		alert = builder.create();
		alert.show();
	}

	/**
	 * Close dialog
	 */
	public static void closeDialog() {
		if (alert != null && alert.isShowing()) {
			alert.dismiss();
			alert = null;
		}
	}

	public static void showDialog(Context context, String message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.app_name));
		builder.setMessage(message);
		builder.setPositiveButton("OK", null);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show confirm dialog
	 * 
	 * @param context
	 * @param title
	 * @param message
	 * @param positiveLabel
	 * @param negativeLabel
	 * @param positiveOnClick
	 * @param negativeOnClick
	 */
	public static void showDialog(Context context, String title,
			String message, String positiveLabel, String negativeLabel,
			DialogInterface.OnClickListener positiveOnClick,
			DialogInterface.OnClickListener negativeOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setPositiveButton(positiveLabel, positiveOnClick);
		builder.setNegativeButton(negativeLabel, negativeOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show confirm dialog
	 * 
	 * @param messageId
	 * @param positiveLabelId
	 * @param negativeLabelId
	 * @param positiveOnClick
	 * @param negativeOnClick
	 */
	public static void showDialog(Context context, int messageId,
			int positiveLabelId, int negativeLabelId,
			DialogInterface.OnClickListener positiveOnClick,
			DialogInterface.OnClickListener negativeOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(R.string.app_name));
		builder.setMessage(context.getString(messageId));
		builder.setPositiveButton(context.getString(positiveLabelId),
				positiveOnClick);
		builder.setNegativeButton(context.getString(negativeLabelId),
				negativeOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show single option dialog
	 * 
	 * @param titleId
	 * @param items
	 * @param positiveLabelId
	 * @param itemOnClick
	 * @param positiveOnClick
	 */
	public static void showSingleOptionDialog(Context context, int titleId,
			String[] items, int positiveLabelId,
			DialogInterface.OnClickListener itemOnClick,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(titleId));
		builder.setSingleChoiceItems(items, 0, itemOnClick);
		builder.setPositiveButton(context.getString(positiveLabelId),
				positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show single option dialog
	 * 
	 * @param titleId
	 * @param items
	 * @param selectPosition
	 * @param positiveLabelId
	 * @param itemOnClick
	 * @param positiveOnClick
	 */
	public static void showSingleOptionDialog(Context context, int titleId,
			String[] items, int selectPosition, int positiveLabelId,
			DialogInterface.OnClickListener itemOnClick,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(titleId));
		builder.setSingleChoiceItems(items, selectPosition, itemOnClick);
		builder.setPositiveButton(context.getString(positiveLabelId),
				positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	/**
	 * Show simple option dialog
	 * 
	 * @param titleId
	 * @param items
	 * @param positiveLabelId
	 * @param itemOnClick
	 * @param positiveOnClick
	 */
	public static void showSimpleOptionDialog(Context context, int titleId,
			String[] items, int positiveLabelId,
			DialogInterface.OnClickListener itemOnClick,
			DialogInterface.OnClickListener positiveOnClick) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(context.getString(titleId));
		builder.setItems(items, itemOnClick);
		builder.setPositiveButton(context.getString(positiveLabelId),
				positiveOnClick);
		AlertDialog alert = builder.create();
		alert.show();
	}

	public static void showShortToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
	}

	public static void showLongToast(Context context, String message) {
		Toast.makeText(context, message, Toast.LENGTH_LONG).show();
	}

}
