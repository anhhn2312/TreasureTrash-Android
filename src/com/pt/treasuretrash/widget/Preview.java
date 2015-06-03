package com.pt.treasuretrash.widget;

/**
 * @author Jose Davis Nidhin
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.Size;
import android.os.Build;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

public class Preview extends ViewGroup implements SurfaceHolder.Callback {
	private final String TAG = "Preview";

	SurfaceView mSurfaceView;
	SurfaceHolder mHolder;
	Size mPreviewSize;
	List<Size> mSupportedPreviewSizes;
	Camera mCamera;
	private boolean meteringAreaSupported;
	private Context context;
	private IOnSetPreviewSize listener;
	private boolean isPreviewConfig = false;

	public Preview(Context context, SurfaceView sv, IOnSetPreviewSize listener) {
		super(context);
		this.context = context;

		this.listener = listener;
		mSurfaceView = sv;
		// addView(mSurfaceView);

		mHolder = mSurfaceView.getHolder();
		mHolder.addCallback(this);
		mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
	}

	public void setCamera(Camera camera) {
		mCamera = camera;
		if (mCamera != null) {
			mSupportedPreviewSizes = mCamera.getParameters()
					.getSupportedPreviewSizes();
			requestLayout();

			// get Camera parameters
			Camera.Parameters params = mCamera.getParameters();

			List<String> focusModes = params.getSupportedFocusModes();
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO)) {
				// set the focus mode
				params.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
				// set Camera parameters
				mCamera.setParameters(params);
			}
		}
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// We purposely disregard child measurements because act as a
		// wrapper to a SurfaceView that centers the camera preview instead
		// of stretching it.
		final int width = resolveSize(getSuggestedMinimumWidth(),
				widthMeasureSpec);
		final int height = resolveSize(getSuggestedMinimumHeight(),
				heightMeasureSpec);
		setMeasuredDimension(width, height);

		if (mSupportedPreviewSizes != null) {
			if (!isPreviewConfig) {
				isPreviewConfig = true;
				mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes,
						width, height);
			}
		}
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		if (changed && getChildCount() > 0) {
			final View child = getChildAt(0);

			final int width = r - l;
			final int height = b - t;

			int previewWidth = width;
			int previewHeight = height;
			if (mPreviewSize != null) {
				previewWidth = mPreviewSize.width;
				previewHeight = mPreviewSize.height;
			}

			// Center the child SurfaceView within the parent.
			if (width * previewHeight > height * previewWidth) {
				final int scaledChildWidth = previewWidth * height
						/ previewHeight;
				child.layout((width - scaledChildWidth) / 2, 0,
						(width + scaledChildWidth) / 2, height);
			} else {
				final int scaledChildHeight = previewHeight * width
						/ previewWidth;
				child.layout(0, (height - scaledChildHeight) / 2, width,
						(height + scaledChildHeight) / 2);
			}
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// The Surface has been created, acquire the camera and tell it where
		// to draw.
		try {
			if (mCamera != null) {
				mCamera.setPreviewDisplay(holder);
			}
		} catch (IOException exception) {
			Log.e(TAG, "IOException caused by setPreviewDisplay()", exception);
		}
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// Surface will be destroyed when we return, so stop the preview.
		if (mCamera != null) {
			mCamera.stopPreview();
		}
	}

	private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
		final double ASPECT_TOLERANCE = 0.1;
		int screenHeight = ((Activity) context).getWindowManager()
				.getDefaultDisplay().getHeight();
		double targetRatio = (double) w / h;
		if (sizes == null)
			return null;

		Size optimalSize = null;
		
		if (optimalSize == null) {
			for (int i=sizes.size()-1; i>=0; i--) {
				Size size = sizes.get(i);
				Log.i("SUPPORT_PREVIEW_SIZE", size.width + "--" + size.height);
				if (size.width >= screenHeight || size.height >= screenHeight) {
					continue;

				}
				optimalSize = size;
			}
		}
		return optimalSize;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);

	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	private void focusOnTouch(MotionEvent event) {
		if (mCamera != null) {

			mCamera.cancelAutoFocus();
			Rect focusRect = calculateTapArea(event.getX(), event.getY(), 1f);
			Rect meteringRect = calculateTapArea(event.getX(), event.getY(),
					1.5f);

			// canvas = new Canvas();

			// paint.setColor(Color.WHITE);
			// paint.setStrokeWidth(3);
			// canvas.drawRect(focusRect, paint);
			// mSurfaceView.getHolder().unlockCanvasAndPost(canvas);

			Parameters parameters = mCamera.getParameters();
			parameters.setFocusMode(Parameters.FOCUS_MODE_AUTO);

			List<Camera.Area> list = new ArrayList<Camera.Area>();
			list.add(new Camera.Area(focusRect, 1000));
			parameters.setFocusAreas(list);

			if (meteringAreaSupported) {
				List<Camera.Area> list2 = new ArrayList<Camera.Area>();
				list2.add(new Camera.Area(meteringRect, 1000));
				parameters.setMeteringAreas(list2);
			}

			mCamera.setParameters(parameters);
			mCamera.autoFocus(new AutoFocusCallback() {

				@Override
				public void onAutoFocus(boolean success, Camera camera) {

					if (success) {
						Toast.makeText(context, "Camera focused",
								Toast.LENGTH_SHORT).show();

					} else {
						Toast.makeText(context, "Focus failed",
								Toast.LENGTH_SHORT).show();
					}

				}

			});
		}
	}

	private Rect calculateTapArea(float x, float y, float coefficient) {
		int areaSize = Float.valueOf(3 * coefficient).intValue();

		int left = clamp((int) x - areaSize / 2, 0, mSurfaceView.getWidth()
				- areaSize);
		int top = clamp((int) y - areaSize / 2, 0, mSurfaceView.getHeight()
				- areaSize);

		RectF rectF = new RectF(left, top, left + areaSize, top + areaSize);
		Matrix matrix = new Matrix();
		matrix.mapRect(rectF);

		return new Rect(Math.round(rectF.left), Math.round(rectF.top),
				Math.round(rectF.right), Math.round(rectF.bottom));
	}

	private int clamp(int x, int min, int max) {
		if (x > max) {
			return max;
		}
		if (x < min) {
			return min;
		}
		return x;
	}

	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	public void surfaceChanged(SurfaceHolder Sholder, int format, int w, int h) {
//		Log.i("SURFACE_CHANGE", w + "-" + h + " ||| " + mPreviewSize.width
//				+ "-" + mPreviewSize.height);
		if (mCamera != null) {

			Camera.Parameters parameters = mCamera.getParameters();
			
			List<String> focusModes = parameters.getSupportedFocusModes();
			if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
				parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
			}
			
			parameters.setPreviewSize(mPreviewSize.width, mPreviewSize.height);
//			parameters.set("orientation", "portrait");
			requestLayout();

			try{
				mCamera.setParameters(parameters);
				listener.onSetPreviewSize(mPreviewSize.width, mPreviewSize.height);
			}catch(Exception e){
				Log.d(this.getClass().getSimpleName(), "Set camera parameters failed");
			}
			
			mCamera.startPreview();
		}
	}

	public interface IOnSetPreviewSize {
		void onSetPreviewSize(int width, int height);
	}
}
