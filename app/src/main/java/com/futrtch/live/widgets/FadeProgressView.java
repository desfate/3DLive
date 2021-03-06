package com.futrtch.live.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.futrtch.live.R;
import com.futrtch.live.utils.DeviceUtil;

public class FadeProgressView extends View {

	private int mColor;
	private float mStrokeWidth;

	private long mTotal;
	private long mCurrent;

	private Paint mPaint;
	private RectF mRectF;// 矩形

	public FadeProgressView(Context context) {
		this(context, null);
	}

	public FadeProgressView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public FadeProgressView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initAttr(attrs);
		init();
	}

	private void initAttr(AttributeSet attrs) {
		if (attrs == null)
			return;
		TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.FadeProgressView);
		mTotal = typedArray.getInt(R.styleable.FadeProgressView_total, 100);
		mCurrent = typedArray.getInt(R.styleable.FadeProgressView_current, 0);
		mColor = typedArray.getColor(R.styleable.FadeProgressView_color, 0xffff9500);
		mStrokeWidth = typedArray.getDimension(R.styleable.FadeProgressView_strokeWidth,
				DeviceUtil.dpToPx(getContext(), 1.5f));
		typedArray.recycle();
	}

	private void init() {
		mPaint = new Paint();
		mPaint.setColor(mColor);
		mPaint.setStyle(Paint.Style.STROKE);
		mPaint.setStrokeWidth(mStrokeWidth);
		mPaint.setAntiAlias(true);

		mRectF = new RectF();
	}

	@Override
	protected void onDraw(Canvas canvas) {
		float angle = mCurrent / (mTotal / 360f);

		float w = mStrokeWidth / 2;
		mRectF.top = w;
		mRectF.left = w;
		mRectF.right = getWidth() - w;
		mRectF.bottom = getWidth() - w;

		canvas.drawArc(mRectF, -90, angle, false, mPaint);
	}

	public void flush() {
		invalidate();
	}

	public void setTotal(long total) {
		this.mTotal = total;
	}

	public void setCurrent(long current) {
		if (current > mTotal)
			current = mTotal;
		this.mCurrent = current;
	}

	public long getTotal() {
		return mTotal;
	}

	public long getCurrent() {
		return mCurrent;
	}

	public long getProgress() {
		if (mTotal == 0)
			return 0;
		return mCurrent * 100 / mTotal;
	}

}
