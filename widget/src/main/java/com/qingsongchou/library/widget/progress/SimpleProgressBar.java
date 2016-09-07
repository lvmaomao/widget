package com.qingsongchou.library.widget.progress;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

import com.qingsongchou.library.widget.R;

/**
 * Created by wsl on 15-8-24.
 */
public class SimpleProgressBar extends View {

    private Paint paint;

    private int defaultHeight;

    private int progress;

    private Rect contentRect;

    public SimpleProgressBar(Context context) {
        this(context, null);
    }

    public SimpleProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SimpleProgressBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        Resources resources = getResources();
        defaultHeight = resources.getDimensionPixelSize(R.dimen.default_simple_progress_bar_height);

        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(defaultHeight);
        paint.setColor(0xff19b458);

        contentRect = new Rect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = measureDimension(defaultHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    protected int measureDimension(int defaultSize, int measureSpec) {
        int result;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, specSize);
        } else {
            result = defaultSize;
        }

        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setupBounds();
    }

    private void setupBounds() {
        contentRect.set(getPaddingLeft(), getPaddingTop(),
                getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(contentRect.left, contentRect.top);
        //unreached bar
        paint.setColor(0xffdedede);
        canvas.drawLine(0, 0, contentRect.width(), 0, paint);
        paint.setColor(0xff19b458);
        canvas.drawLine(0, 0, progress * contentRect.width() / 100, 0, paint);
        canvas.restore();
    }

    public void setProgress(int progress) {
        this.progress = progress;
        invalidate();
    }
}
