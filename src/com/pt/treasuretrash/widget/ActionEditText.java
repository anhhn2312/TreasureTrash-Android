package com.pt.treasuretrash.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

public class ActionEditText extends EditText {

	public ActionEditText(Context context) {
		super(context);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		this.setTypeface(face);
	}

	public ActionEditText(Context context, AttributeSet attrs) {
		super(context, attrs);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		this.setTypeface(face);
	}

	public ActionEditText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		this.setTypeface(face);
	}

	@Override
	public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
		InputConnection conn = super.onCreateInputConnection(outAttrs);
		outAttrs.imeOptions &= ~EditorInfo.IME_FLAG_NO_ENTER_ACTION;
		return conn;
	}
}
