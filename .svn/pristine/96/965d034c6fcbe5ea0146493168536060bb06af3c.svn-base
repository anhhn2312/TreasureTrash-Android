package com.pt.treasuretrash.widget;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.josh.treasuretrash.R;

public class ProgressHUD extends ProgressDialog {
	
	static ProgressHUD progressDialog;
	
	public ProgressHUD(Context context) {
		super(context);
	}

	public ProgressHUD(Context context, int theme) {
		super(context, theme);
	}


	public void onWindowFocusChanged(boolean hasFocus){
//		ImageView imageView = (ImageView) findViewById(R.id.spinnerImageView);
//        AnimationDrawable spinner = (AnimationDrawable) imageView.getBackground();
//        spinner.start();
    }
	
	public void setMessage(CharSequence message) {
		if(message != null && message.length() > 0) {
			findViewById(R.id.message).setVisibility(View.VISIBLE);			
			TextView txt = (TextView)findViewById(R.id.message);
			txt.setText(message);
			txt.invalidate();
		}
	}
	
	public static ProgressHUD show(Context context, CharSequence message, boolean indeterminate, boolean cancelable,
			OnCancelListener cancelListener) {
//		ProgressHUD dialog = new ProgressHUD(context,R.style.ProgressHUD);
//		dialog.setTitle("");
//		dialog.setContentView(R.layout.progress_hud);
//		if(message == null || message.length() == 0) {
//			dialog.findViewById(R.id.message).setVisibility(View.GONE);			
//		} else {
//			TextView txt = (TextView)dialog.findViewById(R.id.message);
//			txt.setText(message);
//		}
//		ProgressHUD dialog = new ProgressHUD(context);
//		dialog.setCancelable(cancelable);
//		dialog.setOnCancelListener(cancelListener);
//		dialog.getWindow().getAttributes().gravity=Gravity.CENTER;
//		WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();  
//		lp.dimAmount=0.2f;
//		dialog.getWindow().setAttributes(lp); 
//		dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
//		dialog.show();
//		return dialog;
		
		progressDialog = new ProgressHUD(context,
				R.style.ProgressDialogTheme);
		progressDialog.setCancelable(false);
		progressDialog
				.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
		progressDialog.show();
		
		return progressDialog;
	}	
}
