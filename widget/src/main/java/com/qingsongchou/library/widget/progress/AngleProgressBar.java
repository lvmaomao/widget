package com.qingsongchou.library.widget.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.qingsongchou.library.widget.R;

/**
 * AngleProgressBar
 * Created by smileCloud on 16/9/26.
 */

public class AngleProgressBar extends View {

    private int mWidth, mHeight;
    private float angleHeight;
    private float progress;
    private Paint mPaint;

    public AngleProgressBar(Context context) {
        this(context, null);
    }

    public AngleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AngleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AngleProgressBar);
        if (a == null) {
            return;
        }
        angleHeight = a.getDimensionPixelSize(R.styleable.AngleProgressBar_angleHeight, (int) dp2px(5));
        a.recycle();
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(angleHeight);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        AngleProgress(canvas);
    }

    private void AngleProgress(Canvas canvas) {
        canvas.save();
        canvas.translate(mWidth / 2, mHeight / 2);
        mPaint.setColor(0xffEAECEE);
        canvas.drawLine(-mWidth / 2 + angleHeight / 2, 0, mWidth / 2 - angleHeight / 2, 0, mPaint);
        if (progress > 0 && progress <= 1) {
            mPaint.setColor(0xff43ac43);
            canvas.drawLine(-mWidth / 2 + angleHeight / 2, 0, mWidth * progress - mWidth / 2 - angleHeight / 2, 0, mPaint);
        }
        canvas.restore();
    }

    public void setProgress(int progress) {
        if (progress >= 100) {
            progress = 100;
        }
        if (progress == 1) {
            progress = 2;
        }
        this.progress = progress / 100f;
        invalidate();
    }

    public void setProgress(float progress) {
        if (progress >= 100) {
            progress = 100;
        }
        if (progress == 1) {
            progress = 2;
        }
        this.progress = progress / 100f;
        invalidate();
    }

    public void setProgress(double progress) {
        if (progress >= 100) {
            progress = 100;
        }
        if (progress == 1) {
            progress = 2;
        }
        this.progress = (float) (progress / 100f);
        invalidate();
    }

//    @Override
//    protected Parcelable onSaveInstanceState() {
//        Parcelable superState = super.onSaveInstanceState();
//        ProgressValue value = new ProgressValue(superState);
//        value.progress = progress;
//        return value;
//    }
//
//    @Override
//    protected void onRestoreInstanceState(Parcelable state) {
//        super.onRestoreInstanceState(state);
//        if (state instanceof ProgressValue) {
//            ProgressValue value = (ProgressValue) state;
//            this.progress = value.progress;
//            invalidate();
//        }
//    }
//
//    public static class ProgressValue extends BaseSavedState {
//
//        private float progress;
//
//        private ProgressValue(Parcel source) {
//            super(source);
//            progress = source.readFloat();
//        }
//
//        public ProgressValue(Parcelable superState) {
//            super(superState);
//        }
//
//        @Override
//        public void writeToParcel(Parcel dest, int flags) {
//            super.writeToParcel(dest,flags);
//            dest.writeFloat(progress);
//        }
//
//        public static final Creator<ProgressValue> CREATOR = new Creator<ProgressValue>() {
//            @Override
//            public ProgressValue createFromParcel(Parcel in) {
//                return new ProgressValue(in);
//            }
//
//            @Override
//            public ProgressValue[] newArray(int size) {
//                return new ProgressValue[size];
//            }
//        };
//    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected float dp2px(float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }
}
