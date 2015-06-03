package com.pt.treasuretrash.activity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.pt.treasuretrash.PacketUtility;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.base.TreasureTrashBaseFunctionsActivity;
import com.pt.treasuretrash.base.TreasureTrashBaseMessageActivity;
import com.pt.treasuretrash.object.Account;

@SuppressLint("NewApi")
public class ExternalLoginActivity extends TreasureTrashBaseMessageActivity
		implements OnClickListener {

	private WebView webview;
	private ProgressBar prgPageLoading;
	private String actionType = "";
	private String url = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_external_login);
		initUI();
		initControl();
		initData();
	}

	private void initData() {
		// TODO Auto-generated method stub
		Bundle b = getIntent().getExtras();
		if (b.containsKey(Account.ACTION_KEY)) {
			actionType = b.getString(Account.ACTION_KEY);
			url = b.getString("url");
		}
		webview.loadUrl(url);

	}

	private void initUI() {
		prgPageLoading = (ProgressBar) findViewById(R.id.prgPageLoading);
		webview = (WebView) findViewById(R.id.webviewExternalLogin);
		webview.setHorizontalScrollBarEnabled(true);
		webview.getSettings().setAllowFileAccess(true);
		webview.getSettings().setJavaScriptEnabled(true);
		webview.getSettings().setBuiltInZoomControls(true);
		webview.getSettings().setUseWideViewPort(true);
		webview.setInitialScale(1);

		webview.setWebChromeClient(new WebChromeClient() {

			public void onProgressChanged(WebView webview, int progress) {

				self.setProgress(progress * 100);
				prgPageLoading.setProgress(progress);

			}

		});

		webview.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {

				Log.e("Web External login", "start link : " + url);
				if (!url.contains(PacketUtility.getPacketName() + ":///#")) {
                       
				}
				super.onPageStarted(webview, url, favicon);
				prgPageLoading.setVisibility(View.VISIBLE);

			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {

				Log.e("Web External login", "new link : " + url);
				if (!url.contains(PacketUtility.getPacketName())) {
					webview.loadUrl(url);
				} else {

				}
				return true;

			}

			@Override
			public void onPageFinished(WebView view, String url) {

				super.onPageFinished(webview, url);
				prgPageLoading.setProgress(0);
				prgPageLoading.setVisibility(View.GONE);

			}

			@Override
			public void onReceivedSslError(WebView view,
					SslErrorHandler handler, SslError error) {
				super.onReceivedSslError(view, handler, error);
				handler.proceed();

			}
		});
	}

	private void initControl() {
	}

	@Override
	public void onClick(View v) {

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
	}
	
	
    
	private Account addAccountFromResponseLink(String url)
	{
		Account acc=new Account();
		
		//get params
		url=url.replace("%20", " ");
		String strParams=url.split("#")[0];
		String[] arrParams=strParams.split("&");
		String[] param=null;
		for (String string : arrParams) {
			param=string.split("=");
		}
		
		return acc;
	}
}
