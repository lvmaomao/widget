package com.qingsongchou.library.widget.bezier;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;

/**
 * PointerView
 * Created by smileCloud on 16/9/23.
 */

public class PointerView extends View {

    private Paint mPaint;
    private int mWidth, mHeight;
    private int mRadius;
    private float mStartPointX, mStartPointY, mEndPointX, mEndPointY;
    private float mCurrentPointX, mCurrentPointY;
    private float mPercent;

    public PointerView(Context context) {
        this(context, null);
    }

    public PointerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(0xffF5F6F7);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(0.1f);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mRadius = dipToPx(getContext(), 33 / 2);
        mStartPointX = mCurrentPointX = mWidth / 2 - mRadius;
        mStartPointY = mCurrentPointY = 0;
        mEndPointX = mRadius - (mWidth / 2);
        mEndPointY = 0;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvasCircle(canvas);
    }

    private void canvasCircle(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        canvas.drawCircle(mStartPointX, mStartPointY, mRadius, mPaint);
        canvas.drawCircle(mCurrentPointX, mCurrentPointY, mRadius, mPaint);
        Path path = new Path();
        path.moveTo(mCurrentPointX, mCurrentPointY - mRadius);
        path.quadTo((mCurrentPointX + mStartPointX) / 2, 0, mStartPointX, (mCurrentPointY - mRadius) / (mPercent * mPercent * mPercent * mPercent * mPercent));
        path.lineTo(mStartPointX, 0);
        path.lineTo(mCurrentPointX, 0);
        path.moveTo(mCurrentPointX, mRadius - mCurrentPointY);
        path.quadTo((mCurrentPointX + mStartPointX) / 2, 0, mStartPointX, (mRadius - mCurrentPointY) / (mPercent * mPercent * mPercent * mPercent * mPercent));
        path.lineTo(mStartPointX, 0);
        path.lineTo(mCurrentPointX, 0);
        canvas.drawPath(path, mPaint);
        canvas.restore();
    }

    public void run(float percent) {
        mPercent = percent;
        if (percent > 1) {
            return;
        }
        mCurrentPointX = mStartPointX * (1 - percent) + percent * mEndPointX;
        postInvalidate();
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dipToPx(Context context, int dip) {
        return (int) (dip * getScreenDensity(context) + 0.5f);
    }

    public static float getScreenDensity(Context context) {
        try {
            DisplayMetrics dm = new DisplayMetrics();
            ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay()
                    .getMetrics(dm);
            return dm.density;
        } catch (Exception e) {
            return DisplayMetrics.DENSITY_DEFAULT;
        }
    }
}
