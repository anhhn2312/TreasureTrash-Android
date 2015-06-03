package com.pt.treasuretrash.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.TextView;

import com.josh.treasuretrash.R;

public class CustomDialog {

	private Context context;
	private String title, content, positiveLable, negativeLabel;
	private OnCustomDialogClickListener listener;

	public CustomDialog(Context context, String title, String content,
			String positiveLabel, String negativeLabel,
			OnCustomDialogClickListener listener) {
		this.context = context;
		this.title = title;
		this.content = content;
		this.positiveLable = positiveLabel;
		this.negativeLabel = negativeLabel;
		this.listener = listener;
	}

	public void show() {
		final Dialog dialog = new Dialog(context);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.layout_custom_dialog);
		dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		dialog.setCancelable(false);
		
		TextView lblTitle = (TextView) dialog.findViewById(R.id.lblTitle);
		TextView lblContent = (TextView) dialog.findViewById(R.id.lblContent);
		
		lblTitle.setText(title);
		lblContent.setText(content);
		
		if(content.equalsIgnoreCase("")){
			lblContent.setVisibility(View.GONE);
		}
		
		TextView btnNegative = (TextView) dialog
				.findViewById(R.id.btnNegative);
		btnNegative.setText(negativeLabel);
		btnNegative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				listener.onNo();
			}
		});


		TextView btnPositive = (TextView) dialog
				.findViewById(R.id.btnPositive);
		btnPositive.setText(positiveLable);

		btnPositive.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dialog.dismiss();
				listener.onYes();
			}
		});

		dialog.show();
	}

	public interface OnCustomDialogClickListener {
		void onYes();

		void onNo();

		void onNeutral();
	}
}
