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
        mPaint.setColor(0xffC5F6F7);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setStrokeWidth(0.1f);
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
//        mRadius = px2dip(getContext(), mHeight * 0.15f);
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
        Path path1 = new Path();
        path1.moveTo(mCurrentPointX, mCurrentPointY - mRadius);
        path1.quadTo((mCurrentPointX + mStartPointX) / 2, 0, mStartPointX, (mCurrentPointY - mRadius) / (mPercent * mPercent * mPercent * mPercent * mPercent));
        path1.lineTo(mStartPointX, 0);
        path1.lineTo(mCurrentPointX, 0);
        canvas.drawPath(path1, mPaint);
        Path path2 = new Path();
        path2.moveTo(mCurrentPointX, mRadius - mCurrentPointY);
        path2.quadTo((mCurrentPointX + mStartPointX) / 2, 0, mStartPointX, (mRadius - mCurrentPointY) / (mPercent * mPercent * mPercent * mPercent * mPercent));
        path2.lineTo(mStartPointX, 0);
        path2.lineTo(mCurrentPointX, 0);
        canvas.drawPath(path2, mPaint);
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

//    public void end() {
//        mCurrentPointX ++ ;
//        if (mCurrentPointX == mStartPointX){
//            return;
//        }
//        postInvalidateDelayed(100);
//    }

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
