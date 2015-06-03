package com.pt.treasuretrash.base;

import java.util.Calendar;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.josh.treasuretrash.R;
import com.pt.treasuretrash.activity.HomeActivity;
import com.pt.treasuretrash.config.TreasureTrashSharedPreferences;
import com.pt.treasuretrash.config.GlobalValue;
import com.pt.treasuretrash.utility.SmartLog;
import com.pt.treasuretrash.utility.StringUtility;

import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class TreasureTrashBaseFragment extends Fragment {

	public static final int REQUEST_CODE_NOT_USE = -10000000;
	public static final int RESULT_CODE_NOT_USE = -10000001;
	public String TAG;
	public String titlePage;
	protected TreasureTrashSharedPreferences preferenceInstance;
	private Bundle mData;
	protected ProgressDialog progressDialog;
	protected int windowWidth;
	protected int windowHeight;
	public Handler handler;
	public Activity self;
	public ImageLoader imageLoader;
	public DisplayImageOptions options, avatarOptions;

	public abstract int getFragmentResource();

	public abstract void initView(View view);

	public abstract void initData(View view);

	public abstract void initControl(View view);

	public abstract void onShow();

	/**
	 * onFragmentBackPressed use key back if return true will execute
	 * onBackPressed of activity
	 * 
	 * @return
	 */
	protected boolean onFragmentBackPressed() {
		// will be process in sub fragment
		//
		return true;
	}

	public void setTitlePage(String titlePage) {
		this.titlePage = titlePage;
	}

	public String getTitlePage() {
		return titlePage;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		handler = new Handler();
		self = getActivity();
		TAG = this.getClass().getSimpleName();
		windowWidth = getActivity().getWindowManager().getDefaultDisplay()
				.getWidth();
		windowHeight = getActivity().getWindowManager().getDefaultDisplay()
				.getHeight();
		initImageLoader();

	}

	private void initImageLoader() {
		ImageLoaderConfiguration config = (new ImageLoaderConfiguration.Builder(
				self).threadPriority(Thread.NORM_PRIORITY)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.discCacheSize(50 * 1024 * 1024) // 50 Mb
				.tasksProcessingOrder(QueueProcessingType.LIFO)).build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(config);

		options = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().resetViewBeforeLoading()
				.showImageForEmptyUri(R.drawable.image_not_available)
				.showImageOnFail(R.drawable.image_not_available)
				.displayer(new RoundedBitmapDisplayer(0)).build();

		avatarOptions = new DisplayImageOptions.Builder().cacheInMemory()
				.cacheOnDisc().resetViewBeforeLoading()
				.showImageForEmptyUri(R.drawable.image_avatar_default)
				.showImageOnFail(R.drawable.image_avatar_default)
				.displayer(new RoundedBitmapDisplayer(0)).build();

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		if (!hidden) {
			onShow();
		} else {
			onHide();
		}
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		setTile(titlePage);
		setMarker();

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int fragmentResource = getFragmentResource();
		if (fragmentResource > 0) {
			View v = inflater.inflate(fragmentResource, container, false);
			initView(v);
			initControl(v);
			initData(v);

			return v;
		}
		return null;
	}

	public HomeActivity getHomeActivity() {
		return (HomeActivity) self;
	}

	@Override
	public void setArguments(Bundle args) {
		// super.setArguments(args);
		this.mData = args;
	}

	/**
	 * use getArgumentsData() instead getArguments()
	 * 
	 * @return Bundle
	 */
	public Bundle getArgumentsData() {
		return this.mData;
	}

	public void setTile(String title) {
		this.titlePage = title;
		((HomeActivity) self).setTitle(title);
	}

	public void setMarker() {
	}

	// public void setTileByUser(String title)
	// {
	// this.titlePage = title;
	// ((HomeActivty) getActivity()).setTitleByUserCustom(title);
	// }

	public String createTitleByUser(String nameLook, String byUserName) {
		StringBuilder builder = new StringBuilder();
		if (!StringUtility.isEmpty(nameLook)) {
			builder.append(nameLook + "\n");
		}
		builder.append("Title by" + " " + byUserName);

		SmartLog.logD("builder:" + builder.toString());
		return builder.toString();
	}

	/**
	 * Clear application notification
	 */
	public void clearAppNotification() {
		// TODO: Clear application notification
		NotificationManager notificationManager = (NotificationManager) getActivity()
				.getSystemService(Context.NOTIFICATION_SERVICE);
		notificationManager.cancelAll();
	}

	// public void backStackFragment()
	// {
	// FragmentManager fm = getActivity().getSupportFragmentManager();
	// fm.popBackStack();
	// }

	/**
	 * Go to other activity
	 * 
	 * @param context
	 * @param cla
	 */

	public void onHide() {

	}

	protected View getParentView(View v) {
		View parentView = (View) v.getParent();
		return parentView;
	}

	public void gotoActivity(Context context, Class<?> cla) {
		Intent intent = new Intent(context, cla);
		startActivity(intent);
		self.overridePendingTransition(R.anim.slide_in_left,
				R.anim.push_left_out);
	}

	public void gotoActivity(Context context, Class<?> cla, int flag) {
		Intent intent = new Intent(context, cla);
		intent.setFlags(flag);
		startActivity(intent);
		self.overridePendingTransition(R.anim.slide_in_left,
				R.anim.push_left_out);
	}

	public void gotoActivity(Context context, Class<?> cla, Bundle b) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(b);
		startActivity(intent);
		self.overridePendingTransition(R.anim.slide_in_left,
				R.anim.push_left_out);
	}

	public void gotoActivity(Class<?> cla) {
		Intent intent = new Intent(self, cla);
		self.startActivity(intent);
		self.overridePendingTransition(R.anim.slide_in_left,
				R.anim.push_left_out);
	}

	public void gotoActivity(Class<?> cla, int flag) {
		Intent intent = new Intent(self, cla);
		intent.setFlags(flag);
		self.startActivity(intent);
		self.overridePendingTransition(R.anim.slide_in_left,
				R.anim.push_left_out);
	}

	/**
	 * Goto activity with bundle
	 * 
	 * @param context
	 * @param cla
	 * @param bundle
	 */
	public void gotoActivity(Class<?> cla, Bundle bundle) {
		Intent intent = new Intent(getActivity(), cla);
		intent.putExtras(bundle);
		self.startActivity(intent);
		self.overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_left);
	}

	/**
	 * Go to other activity
	 * 
	 * @param context
	 * @param cla
	 */
	public void gotoActivityForResult(Context context, Class<?> cla,
			int requestCode) {
		Intent intent = new Intent(context, cla);
		startActivityForResult(intent, requestCode);
	}

	/**
	 * Goto activity with bundle
	 * 
	 * @param context
	 * @param cla
	 * @param bundle
	 * @param requestCode
	 */
	public void gotoActivityForResult(Context context, Class<?> cla,
			Bundle bundle, int requestCode) {
		Intent intent = new Intent(context, cla);
		intent.putExtras(bundle);
		startActivityForResult(intent, requestCode);
	}

	// @Override
	// public void onDestroy() {
	// // TODO Auto-generated method stub
	// super.onDestroy();
	//
	// }

}