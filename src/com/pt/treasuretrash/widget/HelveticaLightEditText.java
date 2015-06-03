package com.pt.treasuretrash.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

public class HelveticaLightEditText extends EditText {

	public HelveticaLightEditText(Context context) {
		super(context);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		this.setTypeface(face);
	}

	public HelveticaLightEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		this.setTypeface(face);
	}

	public HelveticaLightEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		this.setTypeface(face);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);

	}

}

