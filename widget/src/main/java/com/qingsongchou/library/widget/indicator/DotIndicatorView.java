package com.qingsongchou.library.widget.indicator;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

/**
 * Created by wsl on 15-12-16.
 */
public class DotIndicatorView extends IndicatorView {

    private Paint paint;
    private boolean show;

    public DotIndicatorView(Context context) {
        this(context, null);
    }

    public DotIndicatorView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DotIndicatorView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        paint = new Paint();
        paint.setColor(0xffd3321b);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawDot(canvas);
    }

    private void drawDot(Canvas canvas) {
        float radius = dp2px(3f);
        float left = getDrawableRect().right - radius;
        float top = (int) dp2px(2f);
        float right = getDrawableRect().right + radius;
        float bottom = radius * 2 + top;

        float cx = (left + right) / 2 + dp2px(radius);
        float cy = (top + bottom) / 2;
        paint.setAlpha(show ? 255 : 0);
        canvas.drawCircle(cx, cy, radius, paint);
    }

    public void show() {
        this.show = true;
        invalidate();
    }

    public void hide() {
        this.show = false;
        invalidate();
    }
}
