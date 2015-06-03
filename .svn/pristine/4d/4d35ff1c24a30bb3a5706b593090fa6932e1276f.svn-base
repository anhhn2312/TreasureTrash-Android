package com.pt.treasuretrash.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.josh.treasuretrash.R;

public final class ImagesFragment extends Fragment {
	private int imageResource;
	private View view;
	private RelativeLayout img;
	private ImageView iv;
	static ImagesFragment fragment;

	public static ImagesFragment newInstance(int imageResource) {
		fragment = new ImagesFragment();
		fragment.imageResource = imageResource;
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.layout_image, null);
		initUI();
		return view;
	}

	private void initUI() {
		iv = (ImageView)view.findViewById(R.id.image);
		iv.setImageResource(imageResource);
//		img = (RelativeLayout) view.findViewById(R.id.image);
//		img.setBackgroundResource(imageResource);
	}
	
	public void destroy(){
//		img.setBackgroundResource(R.drawable.ic_launcher);
		img = null;
		iv = null;
		fragment = null;
		
	}
	
	

}
