package com.pt.treasuretrash.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.widget.Button;

public class AutoBgButton extends Button {
	public AutoBgButton(Context context) {
		super(context);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		this.setTypeface(face);
	}

	public AutoBgButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		this.setTypeface(face);
	}

	public AutoBgButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		Typeface face = Typeface.createFromAsset(context.getAssets(),
				"fonts/Roboto-Light.ttf");
		this.setTypeface(face);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void setBackgroundDrawable(Drawable d) {
		// Replace the original background drawable (e.g. image) with a
		// LayerDrawable that
		// contains the original drawable.
		AutoBgButtonBackgroundDrawable layer = new AutoBgButtonBackgroundDrawable(d);
		super.setBackgroundDrawable(layer);
	}

	/**
	 * The stateful LayerDrawable used by this button.
	 */
	protected class AutoBgButtonBackgroundDrawable extends LayerDrawable {
		// The color filter to apply when the button is pressed
		protected ColorFilter _pressedFilter = new LightingColorFilter(Color.LTGRAY, 1);
		// Alpha value when the button is disabled
		protected int _disabledAlpha = 100;
		// Alpha value when the button is enabled
		protected int _fullAlpha = 255;

		public AutoBgButtonBackgroundDrawable(Drawable d) {
			super(new Drawable[] { d });
		}

		@Override
		protected boolean onStateChange(int[] states) {
			boolean enabled = false;
			boolean pressed = false;

			for (int state : states) {
				if (state == android.R.attr.state_enabled)
					enabled = true;
				else if (state == android.R.attr.state_pressed)
					pressed = true;
			}

			mutate();
			if (enabled && pressed) {
				setColorFilter(_pressedFilter);
			} else if (!enabled) {
				setColorFilter(null);
				setAlpha(_disabledAlpha);
			} else {
				setColorFilter(null);
				setAlpha(_fullAlpha);
			}

			invalidateSelf();

			return super.onStateChange(states);
		}

		@Override
		public boolean isStateful() {
			return true;
		}
	}
}
