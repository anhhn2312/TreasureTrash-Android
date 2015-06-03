package com.pt.treasuretrash.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import android.widget.Toast;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.object.Account;
import com.pt.treasuretrash.utility.StringUtility;
import com.pt.treasuretrash.widget.AutoBgButton;

@SuppressLint("NewApi")
public class SignupEmailActivity_1 extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	private AutoBgButton btnNext;
	private EditText etUsername, etEmail, etPassword;
	private TextView lblCancel, lblLogin, lblShowPassword;
	private LinearLayout layoutUsername, layoutEmail, layoutPassword;
	private Bundle bundle;
	private String forceLoginAction = "";
	private static Activity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		self = this;
		instance=this;
		setContentView(R.layout.activity_signup_email_1_scroll);
		
		bundle = getIntent().getExtras();
		if (bundle != null) {
			if (bundle.containsKey(GlobalValue.FORCED_LOGIN_ACTION)) {
				forceLoginAction = bundle
						.getString(GlobalValue.FORCED_LOGIN_ACTION);
			}
		}
		
		initUI();
		initControl();
		
		
	}
	
	public static Activity getInstance() {
		return instance;
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		instance=null;
	}

	private void initUI() {
		btnNext = (AutoBgButton) findViewById(R.id.btnNext);
		etUsername = (EditText) findViewById(R.id.etUsername);
		etEmail = (EditText) findViewById(R.id.etEmail);
		etPassword = (EditText) findViewById(R.id.etPassword);
		lblLogin = (TextView) findViewById(R.id.tvLogin);
		lblCancel = (TextView) findViewById(R.id.tvCancel);
		layoutEmail = (LinearLayout) findViewById(R.id.layoutEmail);
		layoutUsername = (LinearLayout) findViewById(R.id.layoutUsername);
		layoutPassword = (LinearLayout) findViewById(R.id.layoutPassword);
		lblShowPassword = (TextView) findViewById(R.id.lbl_show_password);

		setTypeFace(lblLogin, lblCancel);
	}

	private void initControl() {
		btnNext.setOnClickListener(this);
		lblLogin.setOnClickListener(this);
		lblCancel.setOnClickListener(this);
		lblShowPassword.setOnClickListener(this);
		etUsername.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int color = Color.parseColor(getString(R.color.white));
				layoutUsername.setBackgroundColor(color);
				etUsername.setBackgroundColor(color);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		etPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int color = Color.parseColor(getString(R.color.white));
				layoutPassword.setBackgroundColor(color);
				etPassword.setBackgroundColor(color);

			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});

		etPassword.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView view, int actionId,
					KeyEvent event) {
				int result = actionId & EditorInfo.IME_MASK_ACTION;
				switch (result) {
				case EditorInfo.IME_ACTION_DONE:
					addAccountInfo();
					break;
				case EditorInfo.IME_ACTION_NEXT:
					// next stuff
					break;
				}
				return false;
			}
		});

		etEmail.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub

			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				int color = Color.parseColor(getString(R.color.white));
				layoutEmail.setBackgroundColor(color);
				etEmail.setBackgroundColor(color);
			}

			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
	}

	@Override
	public void onClick(View v) {
		if (v == btnNext) {
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
		if (v == lblShowPassword) {
			onClickShowPassword();
			return;
		}

	}

	private void onClickShowPassword() {
		if (lblShowPassword
				.getText()
				.toString()
				.equalsIgnoreCase(
						getResources().getString(R.string.show_password))) {
			etPassword.setTransformationMethod(null);
			lblShowPassword.setText(getResources().getString(
					R.string.hide_password));
		} else {
			etPassword
					.setTransformationMethod(new PasswordTransformationMethod());
			lblShowPassword.setText(getResources().getString(
					R.string.show_password));
		}
	}

	private void onclickCancel() {
		// TODO Auto-generated method stub

		super.onBackPressed();
	}

	private void onClickLogin() {
		// TODO Auto-generated method stub
		gotoActivity(self, LoginSelectionActivity.class,
				Intent.FLAG_ACTIVITY_CLEAR_TOP);
	}

	private void addAccountInfo() {
		String username = etUsername.getText().toString();
		String email = etEmail.getText().toString();
		String password = etPassword.getText().toString();

		// check user
		if (username.isEmpty()) {
			etUsername.setText("");
			etUsername.setHintTextColor(Color.RED);
			layoutUsername.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etUsername.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etUsername.setHint(getString(R.string.error_empty_username));
			return;
		}

		if (username.contains(" ")) {
			etUsername.setText("");
			etUsername.setHintTextColor(Color.RED);
			layoutUsername.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etUsername.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etUsername.setHint(getString(R.string.error_have_spaces));
			return;
		}

		// check email
		if (email.isEmpty()) {
			etEmail.setText("");
			etEmail.setHintTextColor(Color.RED);
			layoutEmail.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etEmail.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etEmail.setHint(getString(R.string.error_empty_email));
			return;
		} else if (!StringUtility.isEmailValid(email)) {
			etEmail.setText("");
			etEmail.setHintTextColor(Color.RED);
			layoutEmail.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etEmail.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etEmail.setHint("");
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

		// check pass
		if (password.isEmpty()) {
			etPassword.setHintTextColor(Color.RED);
			layoutPassword.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etPassword.setHint(getString(R.string.error_empty_password));
			etPassword.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			return;
		}

		if (password.contains(" ")) {
			etPassword.setText("");
			etPassword.setHintTextColor(Color.RED);
			layoutPassword.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etPassword.setHint(getString(R.string.error_have_spaces));
			etPassword.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			return;
		}

		if (password.length() < 6) {
			etPassword.setText("");
			etPassword.setHintTextColor(Color.RED);
			layoutPassword.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			etPassword.setHint("");
			etPassword.setBackgroundColor(Color
					.parseColor(getString(R.color.bg_color_error_editext)));
			showMessageDialog(getString(R.string.error_password_6_characters),
					new DialogListener() {

						@Override
						public void onClose(Dialog dialog) {
							// TODO Auto-generated method stub
							dialog.dismiss();
						}
					});
			return;
		}

		// add to register account :
		if (SignupActivity.registerAccount == null) {
			SignupActivity.registerAccount = new Account();
		}

		SignupActivity.registerAccount.setUsername(username);
		SignupActivity.registerAccount.setEmail(email);
		SignupActivity.registerAccount.setPassword(password);
		SignupActivity.registerAccount.setExternalAccount(false);

		gotoActivity(self, SignupEmailActivity_2.class);
	}
}
