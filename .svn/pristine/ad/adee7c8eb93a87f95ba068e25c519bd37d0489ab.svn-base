package com.pt.treasuretrash.activity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.modelmanager.AccountModelManager;
import com.pt.treasuretrash.modelmanager.ModelManagerListener;
import com.pt.treasuretrash.utility.StringUtility;
import com.pt.treasuretrash.widget.HelveticaLightTextView;

@SuppressLint("NewApi")
public class ForgotPasswordActivity extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	private EditText txtEmail;
	private HelveticaLightTextView lblCancel, lblLogin, lblSignUp,
			btnGetPassword;
	private LinearLayout layoutEmail;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		self = this;
		setContentView(R.layout.activity_forgot_password);
		initUI();
		initControl();
	}

	private void initUI() {
		btnGetPassword = (HelveticaLightTextView) findViewById(R.id.btnGetPassword);
		txtEmail = (EditText) findViewById(R.id.txtEmail);
		lblLogin = (HelveticaLightTextView) findViewById(R.id.lblSignin);
		lblCancel = (HelveticaLightTextView) findViewById(R.id.lblCancel);
		lblSignUp = (HelveticaLightTextView) findViewById(R.id.lblSignUp);
		layoutEmail = (LinearLayout) findViewById(R.id.layoutEmail);
	}

	private void initControl() {
		btnGetPassword.setOnClickListener(this);
		lblLogin.setOnClickListener(this);
		lblCancel.setOnClickListener(this);
		lblSignUp.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v == btnGetPassword) {
			addAccountInfo();
			return;
		}
		if (v == lblLogin) {
			onClickLogin();
			return;
		}
		if (v == lblCancel) {
			onclickCancel();
			return;
		}
		if (v == lblSignUp) {
			onclickSignUp();
			return;
		}

	}

	private void onclickSignUp() {
		// TODO Auto-generated method stub
		gotoActivity(this, SignupActivity.class);
		finish();
	}

	private void onclickCancel() {
		// TODO Auto-generated method stub
		onBackPressed();
	}

	private void onClickLogin() {
		// TODO Auto-generated method stub
		onBackPressed();
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}

	private void addAccountInfo() {
		String email = txtEmail.getText().toString();

		// check email
		if (email.isEmpty()) {
			txtEmail.setText("");
			txtEmail.setHintTextColor(Color.RED);
			layoutEmail.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			txtEmail.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			txtEmail.setHint(getString(R.string.error_empty_email));
			return;
		} else if (!StringUtility.isEmailValid(email)) {
			txtEmail.setText("");
			txtEmail.setHintTextColor(Color.RED);
			layoutEmail.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			txtEmail.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			txtEmail.setHint("");
			showMessageDialog(getString(R.string.error_email_not_valid),
					new DialogListener() {

						@Override
						public void onClose(Dialog dialog) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			return;
		}

		AccountModelManager.ForgotPassword(this, email, true,
				new ModelManagerListener() {

					@Override
					public void onSuccess(String json) {
						// TODO Auto-generated method stub
						showMessageDialog(getString(R.string.message_reset_password),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub
										dialog.dismiss();
										onBackPressed();
									}
								});
					}

					@Override
					public void onError(int statusCode, String json) {
						// TODO Auto-generated method stub
						showMessageDialog(
								getString(R.string.error_forgot_password),
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
					}

					@Override
					public void onError() {
						// TODO Auto-generated method stub
						showMessageDialog("Reset password failed !",
								new DialogListener() {

									@Override
									public void onClose(Dialog dialog) {
										// TODO Auto-generated method stub
										dialog.dismiss();
									}
								});
					}
				});

	}
}
