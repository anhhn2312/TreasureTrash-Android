package com.pt.treasuretrash.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.config.GlobalValue;

public class SplashActivity extends TreasureTrashBaseMessageActivity {

	Thread splashTread;
	int _splashTime = 1000;
	private Intent intent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		intent = new Intent(self, WalkthrouhActivity.class);
		Uri data = getIntent().getData();
		if(data != null){
			intent.setData(data);
		}
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			intent.putExtras(bundle);
		}
		
		showSplashScreen();
		
		
	}

	private void showSplashScreen() {
		splashTread = new Thread() {
			@Override
			public void run() {
				try {
					int wait = 0;
					// new DatabaseOpenhelper(self, new DatabaseConfig());
					while (wait < _splashTime) {
						sleep(100);
						wait += 100;
					}
				} catch (Exception e) {

				} finally {
					startActivity(intent);
					overridePendingTransition(R.anim.slide_in_left, R.anim.push_left_out);
					finish();
					interrupt();
				}
			}
		};
		splashTread.start();
	}

}
