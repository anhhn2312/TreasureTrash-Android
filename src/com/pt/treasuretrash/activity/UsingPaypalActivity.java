package com.pt.treasuretrash.activity;

import java.math.BigDecimal;

import org.json.JSONException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;

public class UsingPaypalActivity extends TreasureTrashBaseMessageActivity implements OnClickListener {
	
	// declare paypal
		private static final String CONFIG_ENVIRONMENT = PaymentActivity.ENVIRONMENT_NO_NETWORK;
		// note that these credentials will differ between live & sandbox
		// environments.
		private static final String CONFIG_CLIENT_ID = "paypal App key";
		private static final String CONFIG_RECEIVER_EMAIL = "Email received";
		private static final int REQUEST_CODE_PAYMENT = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_main);
		//start paypal service
		startPayPalService();
		//call payment
		requestPaypal("Good app", "20");
		
		initUI();
		initControl();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		// Send a screen view when the Activity is displayed to the user.
		
	}

	private void initControl() {
		// TODO Auto-generated method stub

	}

	private void initUI() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
	
	// **************PayPal ***********************************************
	
	public void requestPaypal(String name,String price) {
		PayPalPayment thingToBuy = getThingToBuy(price,name);
		Intent intent = new Intent(self, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT,
				CONFIG_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL,
				CONFIG_RECEIVER_EMAIL);
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);

		startActivityForResult(intent, REQUEST_CODE_PAYMENT);
	}

		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			stopService(new Intent(this, PayPalService.class));
		}

		private void startPayPalService() {
			Intent intent = new Intent(this, PayPalService.class);
			intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT,
					CONFIG_ENVIRONMENT);
			intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
			intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL,
					CONFIG_RECEIVER_EMAIL);
			startService(intent);
		}

		private PayPalPayment getThingToBuy(String price,String name) {
			return new PayPalPayment(new BigDecimal(price), "USD",
					name);
		}

		@Override
		protected void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (requestCode == REQUEST_CODE_PAYMENT) {
				if (resultCode == Activity.RESULT_OK) {
					PaymentConfirmation confirm = data
							.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
					if (confirm != null) {
						try {
							Log.i("Paypal", confirm.toJSONObject().toString(4));
							Log.i("Paypal", confirm.getPayment().toJSONObject()
									.toString(4));

							Toast.makeText(
									getApplicationContext(),
									"PaymentConfirmation info received from PayPal",
									Toast.LENGTH_LONG).show();

						} catch (JSONException e) {
							Log.e("Paypal",
									"an extremely unlikely failure occurred: ", e);
						}
					}
				} else if (resultCode == Activity.RESULT_CANCELED) {
					Log.i("Paypal", "The user canceled.");
				} else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
					Log.i("paymentExample",
							"An invalid payment was submitted. Please see the docs.");
				}
			}

		}

}
