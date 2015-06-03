package com.pt.treasuretrash.utility;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogYesNo {
	
	private Context context;
	private String title, message;
	private OnDialogClickListener listener;
	
	public DialogYesNo(Context context, String title, String message, OnDialogClickListener listener){
		this.context = context;
		this.title = title;
		this.message = message;
		this.listener = listener;
	}
	
	public void show(){
		AlertDialog.Builder alertboxExit = new AlertDialog.Builder(
	            context);
	        alertboxExit.setTitle(title);
	        alertboxExit.setMessage(message);
	        alertboxExit.setCancelable(false);
	        alertboxExit.setPositiveButton("Yes",
	            new DialogInterface.OnClickListener() {
	                public void onClick(DialogInterface dialog, int id)
	                {
	                    // TODO Auto-generated method stub
	                    dialog.dismiss();
	                    listener.onYes();
	                }
	            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
	            public void onClick(DialogInterface dialog, int id)
	            {
	                // TODO Auto-generated method stub
	                dialog.dismiss();
	                listener.onNo();
	            }
	        });
	        alertboxExit.show();
	}

	public interface OnDialogClickListener {
		void onYes();

		void onNo();

		void onNeutral();
	}
}
