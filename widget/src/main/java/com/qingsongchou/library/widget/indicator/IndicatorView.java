package com.qingsongchou.library.widget.indicator;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.qingsongchou.library.widget.R;

/**
 * Created by wsl on 15-8-21.
 */
public class IndicatorView extends View {

    private int width;
    private int height;

    private Drawable drawable;

    private String text;

    private int color;

    private float textSize;

    private Paint textPaint;

    private Rect contentRect;

    public IndicatorView(Context context) {
        this(context, null);
    }

    public IndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public IndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Resources resources =  getResources();

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.IndicatorView);
        text = a.getString(R.styleable.IndicatorView_android_text);
        color = a.getColor(R.styleable.IndicatorView_android_color, 0xff757575);
        textSize = a.getDimension(R.styleable.IndicatorView_indicatorTextSize, resources.getDimension(R.dimen.tab_text_size));
        drawable = a.getDrawable(R.styleable.IndicatorView_image);
        a.recycle();

        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.RED);
        textPaint.setTextSize(textSize);
//        textPaint.setTextSize(38);
        textPaint.setTextAlign(Paint.Align.CENTER);

        contentRect = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;

        setupBounds();
    }

    private void setupBounds() {
        contentRect.set(getPaddingLeft(), getPaddingTop(), width - getPaddingRight(),
                height - getPaddingBottom());
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if(drawable != null && drawable.isStateful()) {
            drawable.setState(getDrawableState());
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int drawableWidth = drawable.getIntrinsicWidth();
        int drawableHeight = drawable.getIntrinsicHeight();
        canvas.save();
        canvas.translate(contentRect.left, contentRect.top);
        drawable.setBounds((contentRect.width() - drawableWidth) / 2, 0, (contentRect.width() + drawableWidth) / 2, drawableHeight);
        drawable.draw(canvas);

        textPaint.setColor(isSelected() ? color : 0xff757575);
        float textHeight = textPaint.descent() - textPaint.ascent();
        canvas.drawText(text, contentRect.width() / 2, drawableHeight + textHeight, textPaint);
        canvas.restore();
    }

    public Rect getDrawableRect() {
        return drawable.getBounds();
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected float dp2px(float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dpVal, getResources().getDisplayMetrics());
    }

    /**
     * dp 2 px
     *
     * @param dpVal
     */
    protected float sp2px(float dpVal) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                dpVal, getResources().getDisplayMetrics());
    }
}